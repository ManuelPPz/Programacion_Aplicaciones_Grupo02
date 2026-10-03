package servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTInstituto;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * ED-16 / ED-17 – Consulta de Usuario
 * GET sin parámetros: muestra la lista de usuarios para elegir.
 * GET con ?nickname=X: muestra el perfil completo del usuario seleccionado.
 */
@WebServlet(name = "ConsultaUsuarioServlet", urlPatterns = {"/ConsultaUsuarioServlet"})
public class ConsultaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();

        // Siempre cargamos la lista para el selector
        List<DTMaster> usuarios = control.ListarClase(EnumDT.DT_USUARIO);
        request.setAttribute("usuarios", usuarios);

        String nickname = request.getParameter("nickname");
        if (nickname != null && !nickname.isBlank()) {
            DTUsuarioBase usuario = control.ConsultarUsuario(nickname.trim());
            if (usuario == null) {
                request.setAttribute("error", "No se encontró el usuario '" + nickname + "'.");
            } else {
                request.setAttribute("usuarioConsultado", usuario);
            }
        }

        request.getRequestDispatcher("InterfacesJSP/ConsultaUsuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // El formulario de consulta usa GET, pero soporte POST por uniformidad
        doGet(request, response);
    }
}
