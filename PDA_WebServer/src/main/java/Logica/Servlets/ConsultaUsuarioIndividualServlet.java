package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ConsultaUsuarioIndividualServlet", urlPatterns = {"/ConsultaUsuarioIndividualServlet"})
public class ConsultaUsuarioIndividualServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        String nicknameConsultado = request.getParameter("nickname");

        if (nicknameConsultado != null && !nicknameConsultado.isBlank()) {
            DTUsuarioBase usuarioConsultado = control.ConsultarUsuario(nicknameConsultado.trim());

            if (usuarioConsultado != null) {
                request.setAttribute("usuarioConsultado", usuarioConsultado);

                // 1. SEGUIDORES (nicknames que da el Servidor Central)
                List<DTUsuarioBase> listaSeguidores = new ArrayList<>();
                for (String nickSeguidor : control.ListarSeguidores(nicknameConsultado.trim())) {
                    DTUsuarioBase dtSeg = control.ConsultarUsuario(nickSeguidor);
                    if (dtSeg != null) {
                        listaSeguidores.add(dtSeg);
                    }
                }
                request.setAttribute("listaSeguidores", listaSeguidores);

                // 2. VERIFICAR SI EL USUARIO LOGUEADO YA SIGUE A ESTE PERFIL
                HttpSession session = request.getSession(false);
                DTMaster dtmLogueado = (session != null) ? (DTMaster) session.getAttribute("usuarioLogueado") : null;

                boolean loSigue = false;
                if (dtmLogueado instanceof DTUsuarioBase userLog) {
                    loSigue = control.ListarSeguidos(userLog.getNickname()).contains(usuarioConsultado.getNickname());
                }
                request.setAttribute("loSigue", loSigue);
                
            } else {
                request.setAttribute("error", "No se encontró al usuario.");
            }
        }

        request.getRequestDispatcher("/InterfacesJSP/ConsultaUsuarioIndividual.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}