package Logica.Servlets;

import Logica.DTsClasses.DTUsuario;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Caso de uso: Valorar un Curso. Actor: Estudiante.
 *
 * GET  ?curso=X          : cursos en los que participa el estudiante y, si eligió uno,
 *                          el resumen de puntajes y su propia valoración
 * POST curso, puntaje    : registra la valoración (1 a 5 estrellas, una única vez)
 */
@WebServlet(name = "ValorarCursoServlet", urlPatterns = {"/ValorarCursoServlet"})
public class ValorarCursoServlet extends HttpServlet {

    static final String VISTA = "/InterfacesJSP/ValorarCurso.jsp";

    /** Punto de acceso al Servidor Central (se sobreescribe en los tests). */
    protected IController control() {
        return Fabric.GetInstance().GetIController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DTUsuario estudiante = estudianteLogueado(request);
        if (estudiante == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }
        cargarPantalla(request, estudiante, limpio(request.getParameter("curso")));
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        DTUsuario estudiante = estudianteLogueado(request);
        if (estudiante == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }
        String curso = limpio(request.getParameter("curso"));
        try {
            if (curso == null) {
                throw new Exception("Elegí un curso para valorar.");
            }
            int puntaje;
            try {
                puntaje = Integer.parseInt(request.getParameter("puntaje"));
            } catch (NumberFormatException e) {
                throw new Exception("Elegí de 1 a 5 estrellas.");
            }
            control().ValorarCurso(estudiante.getNickname(), curso, puntaje);
            request.setAttribute("mensajeExito", "¡Gracias! Tu valoración del curso '" + curso + "' quedó registrada.");
        } catch (Exception e) {
            request.setAttribute("mensajeError", "No se pudo registrar la valoración: " + e.getMessage());
        }
        cargarPantalla(request, estudiante, curso);
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    private void cargarPantalla(HttpServletRequest request, DTUsuario estudiante, String curso) {
        IController ic = control();
        try {
            List<String> cursos = ic.CursosParticipa(estudiante.getNickname());
            request.setAttribute("cursos", cursos);
            if (curso == null) {
                return;
            }
            if (!cursos.contains(curso)) {
                request.setAttribute("mensajeInfo", "No participás en el curso '" + curso + "'.");
                return;
            }
            request.setAttribute("cursoSeleccionado", curso);
            request.setAttribute("resumen", ic.ValoracionesCurso(curso, estudiante.getNickname()));
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al cargar datos: " + e.getMessage());
        }
    }

    private static DTUsuario estudianteLogueado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("usuarioLogueado") : null;
        return (u instanceof DTUsuario e) ? e : null;
    }

    private static String limpio(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
