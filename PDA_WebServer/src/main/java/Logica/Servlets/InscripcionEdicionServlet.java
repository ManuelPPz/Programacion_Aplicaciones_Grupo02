package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
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
import java.util.List;

/**
 * ED-23 – Inscripción a Edición de Curso
 * GET con ?curso=X: Muestra la edición vigente del curso elegido para confirmar la inscripción.
 * POST:             Registra la inscripción del usuario logueado a la edición vigente.
 */
@WebServlet(name = "InscripcionEdicionServlet", urlPatterns = {"/InscripcionEdicionServlet"})
public class InscripcionEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);
        request.setAttribute("cursos", cliente.ListarClase(EnumDT.DT_CURSO));

        String curso = request.getParameter("curso");
        if (curso != null && !curso.isBlank()) {
            // Listamos ediciones del curso para que el servlet identifique la vigente
            List<DTMaster> ediciones = cliente.ListarEdiciones(curso.trim());
            request.setAttribute("ediciones",       ediciones);
            request.setAttribute("cursoSeleccionado", curso);
        }

        request.getRequestDispatcher("InterfacesJSP/InscripcionEdicionCurso.jsp").forward(request, response);
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
        String curso          = request.getParameter("curso");
        String edicion        = request.getParameter("edicion");  // nombre de la edición vigente

        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);

        if (curso == null || edicion == null || curso.isBlank() || edicion.isBlank()) {
            request.setAttribute("error", "Debe seleccionar un curso y una edición vigente.");
            doGet(request, response);
            return;
        }

        try {
            cliente.InscripcionAEdicionCurso(edicion.trim(), usuario.getNickname(), new Date());
            request.setAttribute("exito", "Inscripción a la edicion de curso '" + edicion + "' registrada correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al inscribirse: " + e.getMessage());
        }

        doGet(request, response);
    }
}
