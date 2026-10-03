package servlets;

import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ED-24 – Crear Programa de Formación
 * GET:  Muestra el formulario vacío.
 * POST: Valida los campos y llama a IController.CrearProgramasDeFormacion().
 */
@WebServlet(name = "CrearProgramaServlet", urlPatterns = {"/CrearProgramaServlet"})
public class CrearProgramaServlet extends HttpServlet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("InterfacesJSP/CrearProgramaFormacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre      = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String inicioStr   = request.getParameter("fechaInicio");
        String finStr      = request.getParameter("fechaFin");

        if (nombre == null || descripcion == null || inicioStr == null || finStr == null
                || nombre.isBlank() || descripcion.isBlank() || inicioStr.isBlank() || finStr.isBlank()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("InterfacesJSP/CrearProgramaFormacion.jsp").forward(request, response);
            return;
        }

        Date fInicio, fFin;
        try {
            fInicio = SDF.parse(inicioStr);
            fFin    = SDF.parse(finStr);
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha inválido.");
            request.getRequestDispatcher("InterfacesJSP/CrearProgramaFormacion.jsp").forward(request, response);
            return;
        }

        if (!fFin.after(fInicio)) {
            request.setAttribute("error", "La fecha de fin debe ser posterior a la de inicio.");
            request.getRequestDispatcher("InterfacesJSP/CrearProgramaFormacion.jsp").forward(request, response);
            return;
        }

        IController control = Fabric.GetInstance().GetIController();
        try {
            control.CrearProgramasDeFormacion(nombre.trim(), descripcion.trim(), fInicio, fFin, new Date());
            request.setAttribute("exito", "Programa '" + nombre + "' creado correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al crear el programa: " + e.getMessage());
        }

        request.getRequestDispatcher("InterfacesJSP/CrearProgramaFormacion.jsp").forward(request, response);
    }
}
