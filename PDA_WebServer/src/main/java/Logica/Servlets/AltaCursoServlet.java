package Logica.Servlets;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ED-19 – Alta de Curso
 * GET:  Carga listas de institutos, categorías y cursos existentes (para previas).
 * POST: Registra el nuevo curso en la lógica del servidor central.
 */
@WebServlet(name = "AltaCursoServlet", urlPatterns = {"/AltaCursoServlet"})
public class AltaCursoServlet extends HttpServlet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();

        // Capturar si se seleccionó un instituto desde la vista
        String institutoParam = request.getParameter("instituto");

        cargarListas(request, control, institutoParam);

        request.getRequestDispatcher("InterfacesJSP/AltaCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String instituto  = request.getParameter("instituto");
        String nombre     = request.getParameter("nombre");
        String descripcion= request.getParameter("descripcion");
        String durStr     = request.getParameter("duracion");
        String horasStr   = request.getParameter("horas");
        String credStr    = request.getParameter("creditos");
        String url        = request.getParameter("url");
        String fechaStr   = request.getParameter("fechaIngreso");
        String[] prevArr  = request.getParameterValues("previas");
        String[] catArr   = request.getParameterValues("categorias");

        HttpSession session = request.getSession(false);
        String docente = (session != null && session.getAttribute("usuarioLogueado") != null)
                ? ((DTMaster) session.getAttribute("usuarioLogueado")).getClass().getSimpleName()
                : "";

        IController control = Fabric.GetInstance().GetIController();

        if (instituto == null || nombre == null || descripcion == null || url == null
                || instituto.isBlank() || nombre.isBlank() || descripcion.isBlank() || url.isBlank()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            cargarListas(request, control, instituto);
            request.getRequestDispatcher("InterfacesJSP/AltaCurso.jsp").forward(request, response);
            return;
        }

        int duracion, creditos;
        float horas;
        Date fechaIngreso;
        try {
            duracion  = Integer.parseInt(durStr);
            horas     = Float.parseFloat(horasStr);
            creditos  = Integer.parseInt(credStr);
            fechaIngreso = (fechaStr != null && !fechaStr.isBlank()) ? SDF.parse(fechaStr) : new Date();
        } catch (NumberFormatException | ParseException e) {
            request.setAttribute("error", "Valores numéricos o de fecha inválidos.");
            cargarListas(request, control, instituto);
            request.getRequestDispatcher("InterfacesJSP/AltaCurso.jsp").forward(request, response);
            return;
        }

        List<String> previas    = prevArr  != null ? Arrays.asList(prevArr)  : new ArrayList<>();
        List<String> categorias = catArr   != null ? Arrays.asList(catArr)   : new ArrayList<>();

        try {
            control.AltaCurso(instituto.trim(), nombre.trim(), descripcion.trim(),
                               duracion, horas, creditos, url.trim(),
                               previas, fechaIngreso, docente, categorias);
            request.setAttribute("exito", "Curso '" + nombre + "' registrado correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al registrar el curso: " + e.getMessage());
        }

        cargarListas(request, control, instituto);
        request.getRequestDispatcher("InterfacesJSP/AltaCurso.jsp").forward(request, response);
    }

    private void cargarListas(HttpServletRequest req, IController ctrl, String instituto) {
        req.setAttribute("institutos", ctrl.ListarClase(EnumDT.DT_INSTITUTO));
        req.setAttribute("categorias", ctrl.ListarClase(EnumDT.DT_CATEGORIA));

        // Solo cargamos los cursos si realmente hay un instituto seleccionado
        if (instituto != null && !instituto.isBlank()) {
            req.setAttribute("cursos", ctrl.ListarCursos(instituto.trim()));
            req.setAttribute("institutoSeleccionado", instituto.trim());
        } else {
            // Si no hay instituto seleccionado, enviamos una lista vacía
            req.setAttribute("cursos", new ArrayList<DTMaster>());
            req.setAttribute("institutoSeleccionado", null);
        }
    }
}
