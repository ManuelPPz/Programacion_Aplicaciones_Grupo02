package Logica.Servlets;

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
import java.io.IOException;
import java.util.List;

/**
 * ED-22 – Consulta de Edición de Curso
 * GET con ?curso=X:         Trae las ediciones de ese curso.
 * GET con ?edicion=X o nombre: Trae el detalle completo de la edición seleccionada y precarga su curso.
 */
@WebServlet(name = "ConsultaEdicionServlet", urlPatterns = {"/ConsultaEdicionServlet"})
public class ConsultaEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();

        // 1. Siempre disponemos la lista completa de cursos para el primer selector
        request.setAttribute("cursos", control.ListarClase(EnumDT.DT_CURSO));

        String curso    = request.getParameter("curso");
        String edicion = request.getParameter("edicion");
        
        // Soporte adicional por si viene como ?nombre=... desde los enlaces de perfil
        if (edicion == null || edicion.isBlank()) {
            edicion = request.getParameter("nombre");
        }

        // 2. Si viene una edición directamente (ej. desde el perfil del usuario)
        if (edicion != null && !edicion.isBlank()) {
            DTMaster detalle = control.ConsultaEdicionCurso(edicion.trim());
            if (detalle instanceof DTEdicionCurso dte) {
                // Depuración para verificar en la consola si la imagen viaja desde el controlador
                System.out.println("--- CONSULTA EDICIÓN SERVLET ---");
                System.out.println("Edición consultada: " + dte.getNombre());
                System.out.println("Imagen obtenida del DTE: [" + dte.getImage() + "]");
                System.out.println("---------------------------------");

                request.setAttribute("edicionDetalle", dte);
                request.setAttribute("edicionSeleccionada", dte.getNombre());
                
                // Extraemos automáticamente el curso al que pertenece la edición
                curso = dte.getCurso();
                request.setAttribute("cursoSeleccionado", curso);
            } else {
                request.setAttribute("error", "No se encontró la edición '" + edicion + "'.");
            }
        }

        // 3. Si tenemos un curso seleccionado (ya sea por el formulario o extraído de la edición)
        if (curso != null && !curso.isBlank()) {
            List<DTMaster> ediciones = control.ListarEdiciones(curso.trim());
            request.setAttribute("ediciones", ediciones);
            request.setAttribute("cursoSeleccionado", curso.trim());
        }

        request.getRequestDispatcher("InterfacesJSP/ConsultaEdicionCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}