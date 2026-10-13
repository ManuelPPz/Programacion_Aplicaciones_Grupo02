package Logica.Servlets;

import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ED-24 – Crear Programa de Formación
 * GET:  Muestra el formulario vacío.
 * POST: Valida los campos y llama a IController.CrearProgramasDeFormacion().
 */
@WebServlet(name = "CrearProgramaServlet", urlPatterns = {"/CrearProgramaServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB en memoria antes de guardar temporalmente
    maxFileSize = 1024 * 1024 * 10,      // Tamaño máximo por archivo: 10MB
    maxRequestSize = 1024 * 1024 * 50    // Tamaño máximo de la petición: 50MB
)
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
        
        // Procesar subida de la imagen
        String nombreFinalImagen = ""; 
        try {
            Part filePart = request.getPart("imagen");

            if (filePart != null && filePart.getSize() > 0) {
                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                String extension = "";
                int i = originalFileName.lastIndexOf('.');
                if (i > 0) {
                    extension = originalFileName.substring(i);
                }

                // LIMPIEZA DE NOMBRE: Reemplaza espacios y caracteres especiales por guiones bajos
                String nombreLimpio = nombre.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "_");
                nombreFinalImagen = nombreLimpio + "_" + System.currentTimeMillis() + extension;

                String uploadPath = "C:" + File.separator + "mi_proyecto_data" + File.separator + "uploads" + File.separator + "programas";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // Crea toda la estructura de carpetas si no existe
                }

                String filePath = uploadPath + File.separator + nombreFinalImagen;
                filePart.write(filePath); // Guarda el archivo físicamente en disco
            }
        } catch (Exception e) {
            System.err.println("Error grave al guardar la imagen en disco:");
            e.printStackTrace(); // Ver el error exacto en la consola de Tomcat
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
            control.CrearProgramasDeFormacion(nombre.trim(), descripcion.trim(), fInicio, fFin, new Date(), nombreFinalImagen);
            request.setAttribute("exito", "Programa '" + nombre + "' creado correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al crear el programa: " + e.getMessage());
        }

        request.getRequestDispatcher("InterfacesJSP/CrearProgramaFormacion.jsp").forward(request, response);
    }
}
