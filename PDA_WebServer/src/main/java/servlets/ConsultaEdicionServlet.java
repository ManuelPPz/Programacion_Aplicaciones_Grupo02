package servlets;

import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTInstituto;
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
 * ED-22 – Consulta de Edición de Curso
 * GET con ?curso=X:   Trae las ediciones de ese curso.
 * GET con ?edicion=X: Trae el detalle completo de la edición seleccionada.
 */
@WebServlet(name = "ConsultaEdicionServlet", urlPatterns = {"/ConsultaEdicionServlet"})
public class ConsultaEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();

        // Siempre disponemos la lista completa de cursos para el primer selector
        request.setAttribute("cursos", control.ListarClase(EnumDT.DT_CURSO));

        String curso   = request.getParameter("curso");
        String edicion = request.getParameter("edicion");

        if (curso != null && !curso.isBlank()) {
            List<DTMaster> ediciones = control.ListarEdiciones(curso.trim());
            request.setAttribute("ediciones", ediciones);
            request.setAttribute("cursoSeleccionado", curso);
        }

        if (edicion != null && !edicion.isBlank()) {
            DTMaster detalle = control.ConsultaEdicionCurso(edicion.trim());
            if (detalle instanceof DTEdicionCurso dte) {
                request.setAttribute("edicionDetalle", dte);
            } else {
                request.setAttribute("error", "No se encontró la edición '" + edicion + "'.");
            }
        }

        request.getRequestDispatcher("InterfacesJSP/ConsultaEdicionCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
