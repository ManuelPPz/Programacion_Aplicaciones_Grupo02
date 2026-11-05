package Logica.Servlets;

import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;

import Logica.WebServer.LabService;
import Logica.WebServer.Server;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.Service;
import java.io.IOException;
import java.util.Date;


@WebServlet(name = "InscripcionProgramaServlet", urlPatterns = {"/InscripcionProgramaServlet"})
public class InscripcionProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);
        
        request.setAttribute("programas", cliente.ListarClase(EnumDT.DT_PROGRAMA));
        request.getRequestDispatcher("InterfacesJSP/InscripcionProgramaFormacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }

        DTUsuarioBase usuario = (DTUsuarioBase) session.getAttribute("usuarioLogueado");
        String programa       = request.getParameter("programa");

        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);

        if (programa == null || programa.isBlank()) {
            request.setAttribute("error", "Debe seleccionar un programa de formación.");
            request.setAttribute("programas", cliente.ListarClase(EnumDT.DT_PROGRAMA));
            request.getRequestDispatcher("InterfacesJSP/InscripcionProgramaFormacion.jsp").forward(request, response);
            return;
        }

        try {
            cliente.InscripcionUsuarioAProgramas(programa.trim(), usuario.getNickname(), new Date());
            request.setAttribute("exito", "Inscripción al programa '" + programa + "' registrada correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al inscribirse: " + e.getMessage());
        }

        request.setAttribute("programas", cliente.ListarClase(EnumDT.DT_PROGRAMA));
        request.getRequestDispatcher("InterfacesJSP/InscripcionProgramaFormacion.jsp").forward(request, response);
    }
}
