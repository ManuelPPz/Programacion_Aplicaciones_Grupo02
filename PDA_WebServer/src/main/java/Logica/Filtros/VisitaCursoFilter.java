package Logica.Filtros;

import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listado de tendencias: cada vez que un usuario o visitante consulta un curso
 * (ConsultaCursoServlet con ?curso= o ?nombre=) se le suma una visita en el
 * Servidor Central. Así el conteo queda fuera de la lógica del servlet.
 *
 * Si el Servidor Central no puede registrar la visita, la consulta del curso
 * se muestra igual.
 */
@WebFilter(filterName = "VisitaCursoFilter", urlPatterns = {"/ConsultaCursoServlet"})
public class VisitaCursoFilter extends HttpFilter {

    private static final Logger LOG = Logger.getLogger(VisitaCursoFilter.class.getName());

    private final Supplier<IController> controlador;

    public VisitaCursoFilter() {
        this(() -> Fabric.GetInstance().GetIController());
    }

    /** Permite inyectar el controlador (tests). */
    public VisitaCursoFilter(Supplier<IController> controlador) {
        this.controlador = controlador;
    }

    /** Nombre del curso consultado (mismos parámetros que ConsultaCursoServlet), o null. */
    static String cursoConsultado(HttpServletRequest request) {
        String nombre = request.getParameter("curso");
        if (nombre == null || nombre.isBlank()) {
            nombre = request.getParameter("nombre");
        }
        return (nombre == null || nombre.isBlank()) ? null : nombre.trim();
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String curso = cursoConsultado(request);
        if (curso != null) {
            try {
                controlador.get().RegistrarVisitaCurso(curso);
            } catch (RuntimeException e) {
                LOG.log(Level.WARNING, "No se pudo registrar la visita al curso " + curso, e);
            }
        }
        chain.doFilter(request, response);
    }
}
