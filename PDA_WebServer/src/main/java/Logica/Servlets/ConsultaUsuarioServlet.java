package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
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
 * ED-16 / ED-17 – Consulta de Usuario (Listado General)
 * Compatible con la interfaz Web (JSP) y cliente Swing.
 */
@WebServlet(name = "ConsultaUsuarioServlet", urlPatterns = {"/ConsultaUsuarioServlet"})
public class ConsultaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        boolean isJavaClient = request.getHeader("User-Agent") != null && request.getHeader("User-Agent").contains("Java");

        // 1. Cargar la lista completa de usuarios para el selector Web
        List<DTMaster> usuarios = control.ListarClase(EnumDT.DT_USUARIO);
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