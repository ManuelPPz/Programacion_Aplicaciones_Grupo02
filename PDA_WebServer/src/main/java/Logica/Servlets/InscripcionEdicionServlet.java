package Logica.Servlets;

import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuario;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso: Inscripción a edición de curso (con video).
 *
 * GET  ?filtro=instituto|categoria&valor=X : cursos del instituto o de la categoría.
 * GET  ...&curso=Y                         : datos básicos de la edición vigente del curso
 *                                            y, si existe, la inscripción del estudiante.
 * POST accion=inscribir|modificar          : registra la inscripción (fecha actual, video
 *                                            opcional) o modifica el video de una existente.
 *
 * Solo para estudiantes logueados. Tras el POST se redirige al GET (patrón PRG) con el
 * mensaje de resultado guardado en la sesión.
 */
@WebServlet(name = "InscripcionEdicionServlet", urlPatterns = {"/InscripcionEdicionServlet"})
public class InscripcionEdicionServlet extends HttpServlet {

    static final String JSP = "/InterfacesJSP/InscripcionEdicionCurso.jsp";
    static final String FILTRO_INSTITUTO = "instituto";
    static final String FILTRO_CATEGORIA = "categoria";

    /** Punto de acceso al Servidor Central (sobrescribible en tests). */
    protected IController control() {
        return Fabric.GetInstance().GetIController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        DTUsuario estudiante = estudianteLogueado(request, response);
        if (estudiante == null) {
            return;
        }
        moverMensajesDeSesion(request);
        cargarPantalla(request, control(), estudiante.getNickname());
        request.getRequestDispatcher(JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        DTUsuario estudiante = estudianteLogueado(request, response);
        if (estudiante == null) {
            return;
        }

        String curso = limpio(request.getParameter("curso"));
        String edicion = limpio(request.getParameter("edicion"));
        String urlVideo = limpio(request.getParameter("urlVideo"));
        boolean modificar = "modificar".equals(request.getParameter("accion"));

        HttpSession session = request.getSession();
        IController ic = control();
        try {
            if (curso == null || edicion == null) {
                throw new Exception("Debe elegir un curso con una edición vigente.");
            }
            // La inscripción solo se permite a la edición vigente del curso elegido.
            DTMaster vigente = ic.EdicionVigente(curso);
            if (!(vigente instanceof DTEdicionCurso ed) || !edicion.equals(ed.getNombre())) {
                throw new Exception("La edición '" + edicion + "' no es la edición vigente del curso '" + curso + "'.");
            }
            if (modificar) {
                ic.ModificarInscripcionEdicion(estudiante.getNickname(), edicion, urlVideo);
                session.setAttribute("inscripcionExito", "Se modificó tu inscripción a la edición '" + edicion + "'.");
            } else {
                // Fecha null: el Servidor Central registra la inscripción en su fecha actual.
                ic.InscripcionAEdicionCurso(edicion, estudiante.getNickname(), null, urlVideo);
                session.setAttribute("inscripcionExito", "Te inscribiste a la edición '" + edicion + "'.");
            }
        } catch (Exception e) {
            String msg = (e.getMessage() == null || e.getMessage().isBlank())
                    ? "No se pudo completar la operación." : e.getMessage();
            session.setAttribute("inscripcionError", msg);
            session.setAttribute("inscripcionUrlVideo", urlVideo); // para no perder lo que escribió
        }

        response.sendRedirect(request.getContextPath() + "/InscripcionEdicionServlet" + consulta(
                request.getParameter("filtro"), request.getParameter("valor"), curso));
    }

    /** Estudiante logueado, o null (tras redirigir al login o mostrar el aviso) si no lo hay. */
    DTUsuario estudianteLogueado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object usuario = (session != null) ? session.getAttribute("usuarioLogueado") : null;
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return null;
        }
        if (!(usuario instanceof DTUsuario estudiante)) {
            request.setAttribute("soloEstudiantes", Boolean.TRUE);
            request.getRequestDispatcher(JSP).forward(request, response);
            return null;
        }
        return estudiante;
    }

    /** Carga en el request todo lo que muestra la pantalla según los parámetros elegidos. */
    void cargarPantalla(HttpServletRequest request, IController ic, String nickname) {
        String filtro = FILTRO_CATEGORIA.equals(request.getParameter("filtro")) ? FILTRO_CATEGORIA : FILTRO_INSTITUTO;
        String valor = limpio(request.getParameter("valor"));
        String curso = limpio(request.getParameter("curso"));

        request.setAttribute("filtro", filtro);
        request.setAttribute("valor", valor);
        request.setAttribute("cursoSeleccionado", curso);
        request.setAttribute("institutos", ic.ListarClase(EnumDT.DT_INSTITUTO));
        request.setAttribute("categorias", ic.ListarClase(EnumDT.DT_CATEGORIA));

        List<DTMaster> cursos = new ArrayList<>();
        if (valor != null) {
            cursos = FILTRO_CATEGORIA.equals(filtro) ? ic.ListarCursosXCategoria(valor) : ic.ListarCursos(valor);
        }
        request.setAttribute("cursos", cursos);

        if (curso != null) {
            DTMaster vigente = ic.EdicionVigente(curso);
            if (vigente instanceof DTEdicionCurso ed) {
                request.setAttribute("edicionVigente", ed);
                request.setAttribute("inscripcion", ic.ConsultarInscripcionEdicion(nickname, ed.getNombre()));
            }
        }
    }

    private static void moverMensajesDeSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        for (String[] par : new String[][]{{"inscripcionExito", "exito"}, {"inscripcionError", "error"},
                {"inscripcionUrlVideo", "urlVideoIngresada"}}) {
            Object valor = session.getAttribute(par[0]);
            if (valor != null) {
                request.setAttribute(par[1], valor);
                session.removeAttribute(par[0]);
            }
        }
    }

    static String consulta(String filtro, String valor, String curso) {
        StringBuilder sb = new StringBuilder();
        agregar(sb, "filtro", filtro);
        agregar(sb, "valor", valor);
        agregar(sb, "curso", curso);
        return sb.toString();
    }

    private static void agregar(StringBuilder sb, String nombre, String valor) {
        if (valor == null || valor.isBlank()) {
            return;
        }
        sb.append(sb.length() == 0 ? '?' : '&').append(nombre).append('=')
                .append(URLEncoder.encode(valor.trim(), StandardCharsets.UTF_8));
    }

    private static String limpio(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
