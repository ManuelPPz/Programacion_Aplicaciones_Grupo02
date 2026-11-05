package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import Logica.WebServer.LabService;
import Logica.WebServer.Server;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.Service;
import java.io.IOException;
import java.util.List;

/**
 * ED-16 / ED-17 – Consulta de Usuario (Listado General)
 * Compatible con la interfaz Web (JSP) y cliente Swing.
 */
@WebServlet(name = "ConsultaUsuarioServlet", urlPatterns = {"/ConsultaUsuarioServlet"})
public class ConsultaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);
        boolean isJavaClient = request.getHeader("User-Agent") != null && request.getHeader("User-Agent").contains("Java");

        // 1. Cargar la lista completa de usuarios para el selector Web
        List<DTMaster> usuarios = cliente.ListarClase(EnumDT.DT_USUARIO);
        request.setAttribute("usuarios", usuarios);

        // 2. Responder según el cliente
        if (isJavaClient) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Redirigimos a la vista que muestra la lista de selección
        request.getRequestDispatcher("InterfacesJSP/ConsultaUsuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}