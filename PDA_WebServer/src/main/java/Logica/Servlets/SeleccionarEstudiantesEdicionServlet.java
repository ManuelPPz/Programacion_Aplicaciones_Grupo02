package Logica.Servlets;

import Logica.Classes.Enum_Estado_inscripcion;
import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso: Seleccionar estudiantes para una edición de curso (con video).
 * Actor: Docente.
 *
 * GET  ?instituto=X            : cursos del instituto
 * GET  ...&curso=Y             : edición vigente del curso con sus inscripciones
 * POST instituto, curso, nomEdicion, estudiantes[], estado_{nick}=ACEPTADO|RECHAZADO
 *                              : registra la selección (el SC controla el cupo)
 *
 * @author leoli
 */
@WebServlet(name = "SeleccionarEstudiantesEdicionServlet", urlPatterns = {"/SeleccionarEstudiantesEdicionServlet"})
public class SeleccionarEstudiantesEdicionServlet extends HttpServlet {

    static final String VISTA = "/InterfacesJSP/SeleccionarEstudiantesEdicion.jsp";

    /** Punto de acceso al Servidor Central (se sobreescribe en los tests). */
    protected IController control() {
        return Fabric.GetInstance().GetIController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DTDocente docente = docenteLogueado(request);
        if (docente == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }
        cargarPantalla(request, docente);
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        DTDocente docente = docenteLogueado(request);
        if (docente == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }

        IController ic = control();
        String curso = limpio(request.getParameter("curso"));
        String nomEdicion = limpio(request.getParameter("nomEdicion"));
        String[] estudiantes = request.getParameterValues("estudiantes[]");

        try {
            // Solo se puede seleccionar en la edición vigente del curso, y si el docente participa en ella.
            DTEdicionCurso vigente = edicionVigente(ic, curso);
            if (vigente == null || nomEdicion == null || !nomEdicion.equals(vigente.getNombre())) {
                throw new Exception("La edición indicada no es la edición vigente del curso.");
            }
            if (!participa(docente, vigente)) {
                throw new Exception("No participás como docente en la edición '" + nomEdicion + "'.");
            }

            List<Object[]> cambios = new ArrayList<>();
            if (estudiantes != null) {
                for (String nickname : estudiantes) {
                    String valor = request.getParameter("estado_" + nickname);
                    if (valor == null || valor.isBlank()) {
                        continue; // el docente no tocó esta inscripción
                    }
                    Enum_Estado_inscripcion estado = buscarEstado(valor);
                    if (estado != Enum_Estado_inscripcion.ACEPTADO && estado != Enum_Estado_inscripcion.RECHAZADO) {
                        throw new Exception("Estado no válido para " + nickname + ": " + valor);
                    }
                    cambios.add(new Object[]{nickname, estado});
                }
            }
            if (cambios.isEmpty()) {
                throw new Exception("No se marcó ninguna inscripción como Aceptada o Rechazada.");
            }
            ic.AceptarEstudiantesAEdicionCurso(cambios, nomEdicion);
            request.setAttribute("mensajeExito", "La selección de estudiantes para la edición fue registrada con éxito.");
        } catch (Exception e) {
            request.setAttribute("mensajeError", "No se pudo registrar la selección: " + e.getMessage());
        }

        cargarPantalla(request, docente);
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    /** Carga institutos, cursos y la edición vigente según los filtros elegidos. */
    private void cargarPantalla(HttpServletRequest request, DTDocente docente) {
        IController ic = control();
        try {
            request.setAttribute("institutos", ic.ListarClase(EnumDT.DT_INSTITUTO));

            String instituto = limpio(request.getParameter("instituto"));
            if (instituto == null) {
                return;
            }
            request.setAttribute("instSeleccionado", instituto);
            request.setAttribute("cursos", ic.ListarCursos(instituto));

            String curso = limpio(request.getParameter("curso"));
            if (curso == null) {
                return;
            }
            request.setAttribute("cursoSeleccionado", curso);

            DTEdicionCurso vigente = edicionVigente(ic, curso);
            if (vigente == null) {
                request.setAttribute("mensajeInfo", "El curso '" + curso + "' no tiene una edición vigente.");
            } else if (!participa(docente, vigente)) {
                request.setAttribute("mensajeInfo", "No participás como docente en la edición vigente ('"
                        + vigente.getNombre() + "') del curso '" + curso + "'.");
            } else {
                request.setAttribute("dtEdicion", vigente);
                request.setAttribute("cantAceptados", contarAceptados(vigente));
            }
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al cargar datos: " + e.getMessage());
        }
    }

    private static DTEdicionCurso edicionVigente(IController ic, String curso) {
        if (curso == null) {
            return null;
        }
        DTMaster dt = ic.EdicionVigente(curso);
        return (dt instanceof DTEdicionCurso ed) ? ed : null;
    }

    private static boolean participa(DTDocente docente, DTEdicionCurso ed) {
        return ed.getDocentes() != null && ed.getDocentes().contains(docente.getNickname());
    }

    private static int contarAceptados(DTEdicionCurso ed) {
        int n = 0;
        if (ed.getMisInscripciones() != null) {
            for (var ins : ed.getMisInscripciones()) {
                if (ins != null && ins.getEstadoIns() == Enum_Estado_inscripcion.ACEPTADO) {
                    n++;
                }
            }
        }
        return n;
    }

    private static DTDocente docenteLogueado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("usuarioLogueado") : null;
        return (u instanceof DTDocente d) ? d : null;
    }

    private static String limpio(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    /** Busca la constante del enum ignorando mayúsculas/minúsculas; null si no existe. */
    private static Enum_Estado_inscripcion buscarEstado(String texto) {
        for (Enum_Estado_inscripcion e : Enum_Estado_inscripcion.values()) {
            if (e.name().equalsIgnoreCase(texto.trim())) {
                return e;
            }
        }
        return null;
    }
}
