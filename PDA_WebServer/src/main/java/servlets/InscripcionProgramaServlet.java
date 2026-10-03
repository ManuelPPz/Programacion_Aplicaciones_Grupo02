package servlets;

import Logica.DTsClasses.DTUsuarioBase;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * ED-27 – Inscripción a Programa de Formación
 * GET:  Carga la lista de programas disponibles.
 * POST: Registra la inscripción del usuario logueado al programa elegido.
 */
@WebServlet(name = "InscripcionProgramaServlet", urlPatterns = {"/InscripcionProgramaServlet"})
public class InscripcionProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        request.setAttribute("programas", control.ListarProgramaDeForm());
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

        IController control = Fabric.GetInstance().GetIController();

        if (programa == null || programa.isBlank()) {
            request.setAttribute("error", "Debe seleccionar un programa de formación.");
            request.setAttribute("programas", control.ListarProgramaDeForm());
            request.getRequestDispatcher("InterfacesJSP/InscripcionProgramaFormacion.jsp").forward(request, response);
            return;
        }

        try {
            control.InscripcionUsuarioAProgramas(programa.trim(), usuario.getNickname(), new Date());
            request.setAttribute("exito", "Inscripción al programa '" + programa + "' registrada correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al inscribirse: " + e.getMessage());
        }

        request.setAttribute("programas", control.ListarProgramaDeForm());
        request.getRequestDispatcher("InterfacesJSP/InscripcionProgramaFormacion.jsp").forward(request, response);
    }
}
