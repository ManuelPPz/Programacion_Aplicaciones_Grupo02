package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuario;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;
import Logica.WebServer.LabService;
import Logica.WebServer.Server;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.Service;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ConsultaUsuarioIndividualServlet", urlPatterns = {"/ConsultaUsuarioIndividualServlet"})
public class ConsultaUsuarioIndividualServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);
        String nicknameConsultado = request.getParameter("nickname");

        if (nicknameConsultado != null && !nicknameConsultado.isBlank()) {
            DTUsuarioBase usuarioConsultado = cliente.ConsultarUsuario(nicknameConsultado.trim());

            if (usuarioConsultado != null) {
                request.setAttribute("usuarioConsultado", usuarioConsultado);

                // 1. CARGAR LISTA DE SEGUIDORES REALES DESDE LA BASE DE DATOS
                List<DTUsuarioBase> listaSeguidores = new ArrayList<>();
                DTUsuarioBase ubReal = cliente.ConsultarUsuario(nicknameConsultado.trim());
                
                if (ubReal != null) {
                    // CORRECCIÓN: Usamos getList() para obtener todos los usuarios y evaluar quiénes lo siguen
                    List<DTMaster> todosLosUsuarios = cliente.ListarClase(EnumDT.DT_USUARIO);
                    if (todosLosUsuarios != null) {
                        for (DTMaster dtm : todosLosUsuarios) {
                            if(dtm instanceof DTUsuarioBase dtu){
                                if (dtu.getSeguidos() != null && dtu.getSeguidos().contains(ubReal)) {
                                    DTUsuarioBase dtSeg = cliente.ConsultarUsuario(dtu.getNickname());
                                    if (dtSeg != null) {
                                        listaSeguidores.add(dtSeg);
                                    }
                                }
                            }
                            
                        }
                    }
                }
                request.setAttribute("listaSeguidores", listaSeguidores);

                // 2. VERIFICAR SI EL USUARIO LOGUEADO YA SIGUE A ESTE PERFIL
                HttpSession session = request.getSession(false);
                DTMaster dtmLogueado = (session != null) ? (DTMaster) session.getAttribute("usuarioLogueado") : null;

                boolean loSigue = false;
                if (dtmLogueado instanceof DTUsuarioBase userLog && ubReal != null) {
                    //Esta mal esta consulta ya que hay dependencia con el manejador, salteandose el controlador
                    DTUsuarioBase ubLogueadoReal = cliente.ConsultarUsuario(nicknameConsultado);
                    if (ubLogueadoReal != null && ubLogueadoReal.getSeguidos() != null) {
                        // Si el usuario logueado sigue al usuario consultado, cambiamos a true
                        if (ubLogueadoReal.getSeguidos().contains(ubReal)) {
                            loSigue = true;
                        }
                    }
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