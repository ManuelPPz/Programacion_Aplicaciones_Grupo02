package servlets;

import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import DTsClasses.EnumDT;
import Logica.Fabric;
import Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * ED-20 – Consulta de Curso
 * GET sin parámetros:      Carga la lista completa de institutos y categorías para filtrar.
 * GET con ?filtroTipo+filtroValor: Carga los cursos del instituto o categoría elegida.
 * GET con ?curso=X:        Muestra el detalle completo del curso seleccionado.
 */
@WebServlet(name = "ConsultaCursoServlet", urlPatterns = {"/ConsultaCursoServlet"})
public class ConsultaCursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();

        // Siempre cargamos listas de filtros
        request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
        request.setAttribute("categorias", control.ListarClase(EnumDT.DT_CATEGORIA));

        String instituto = request.getParameter("instituto");
        String curso     = request.getParameter("curso");

        // Si filtra por instituto → listar cursos de ese instituto
        if (instituto != null && !instituto.isBlank()) {
            List<DTMaster> cursosFiltrados = control.ListarCursos(instituto.trim());
            request.setAttribute("cursosFiltrados", cursosFiltrados);
            request.setAttribute("institutoSeleccionado", instituto);
        } else {
            // Sin filtro → mostrar todos
            request.setAttribute("cursosFiltrados", control.ListarClase(EnumDT.DT_CURSO));
        }

        // Si eligió un curso concreto → traer su detalle
        if (curso != null && !curso.isBlank()) {
            DTMaster detalle = control.ConsultaCurso(curso.trim());
            if (detalle instanceof DTCurso dtc) {
                request.setAttribute("cursoDetalle", dtc);
            } else {
                request.setAttribute("error", "No se encontró el curso '" + curso + "'.");
            }
        }

        request.getRequestDispatcher("InterfacesJSP/ConsultaCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
