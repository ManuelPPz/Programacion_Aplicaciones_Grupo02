package servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.DTDocente;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ED-21 – Alta de Edición de Curso
 * GET:  Carga la lista de cursos del instituto al que pertenece el docente logueado,
 *       y la lista de docentes disponibles para seleccionar como participantes.
 * POST: Registra la nueva edición del curso.
 */
@WebServlet(name = "AltaEdicionServlet", urlPatterns = {"/AltaEdicionServlet"})
public class AltaEdicionServlet extends HttpServlet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        HttpSession session  = request.getSession(false);

        // Para filtrar cursos del instituto del docente logueado
        String instituto = "";
        if (session != null && session.getAttribute("usuarioLogueado") instanceof DTDocente doc) {
            if (!doc.getInstitutos().isEmpty()) {
                instituto = doc.getInstitutos().get(0);
            }
        }

        List<DTMaster> cursos;
        if (!instituto.isBlank()) {
            cursos = control.ListarCursos(instituto);
        } else {
            cursos = control.ListarClase(EnumDT.DT_CURSO);
        }

        request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
        request.setAttribute("cursos",     cursos);
        request.setAttribute("docentes",   control.ListarClase(EnumDT.DT_USUARIO));
        request.getRequestDispatcher("InterfacesJSP/AltaEdicionCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String instituto   = request.getParameter("instituto");
        String curso       = request.getParameter("curso");
        String nombre      = request.getParameter("nombre");
        String inicioStr   = request.getParameter("fechaInicio");
        String finStr      = request.getParameter("fechaFin");
        String cupoStr     = request.getParameter("cupo");
        String[] docArr    = request.getParameterValues("docentes");

        IController control = Fabric.GetInstance().GetIController();

        if (instituto == null || curso == null || nombre == null || inicioStr == null || finStr == null
                || instituto.isBlank() || curso.isBlank() || nombre.isBlank()
                || inicioStr.isBlank() || finStr.isBlank()) {
            request.setAttribute("error", "Instituto, curso, nombre y fechas son obligatorios.");
            doGet(request, response);
            return;
        }

        Date fInicio, fFin;
        try {
            fInicio = SDF.parse(inicioStr);
            fFin    = SDF.parse(finStr);
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha inválido.");
            doGet(request, response);
            return;
        }

        int cupo = 0;
        if (cupoStr != null && !cupoStr.isBlank()) {
            try { cupo = Integer.parseInt(cupoStr); } catch (NumberFormatException ignored) {}
        }

        List<String> docentes = docArr != null ? Arrays.asList(docArr) : new ArrayList<>();

        try {
            control.AltaEdicionCurso(instituto.trim(), curso.trim(), nombre.trim(),
                                     fInicio, fFin, cupo, docentes, new Date());
            request.setAttribute("exito", "Edición '" + nombre + "' creada correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al crear la edición: " + e.getMessage());
        }

        doGet(request, response);
    }
}
