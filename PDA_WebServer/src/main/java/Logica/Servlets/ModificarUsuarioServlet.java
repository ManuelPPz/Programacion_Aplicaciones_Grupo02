package Logica.Servlets;

import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;

/**
 * ED-18 – Modificar Datos de Usuario
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB en memoria antes de guardar temporalmente
    maxFileSize = 1024 * 1024 * 10,      // Tamaño máximo por archivo: 10MB
    maxRequestSize = 1024 * 1024 * 50    // Tamaño máximo de la petición: 50MB
)
@WebServlet(name = "ModificarUsuarioServlet", urlPatterns = {"/ModificarUsuarioServlet"})
public class ModificarUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        IController control = Fabric.GetInstance().GetIController();
        DTUsuarioBase usuario = (DTUsuarioBase) session.getAttribute("usuarioLogueado");
        
        request.setAttribute("esDocente", usuario instanceof DTDocente);
        request.setAttribute("usuario", usuario);
        request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
        request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isJavaClient = request.getHeader("User-Agent") != null && request.getHeader("User-Agent").contains("Java");
        HttpSession session = request.getSession(false);

        // 1. Control de acceso/sesión para clientes Web
        if (!isJavaClient && (session == null || session.getAttribute("usuarioLogueado") == null)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        IController control = Fabric.GetInstance().GetIController();

        // 2. Obtención de datos del usuario
        DTUsuarioBase usuarioSesion = null;
        String nickname = "";

        if (isJavaClient) {
            nickname = request.getParameter("nickname");
            if (nickname != null && !nickname.isBlank()) {
                usuarioSesion = control.ConsultarUsuario(nickname.trim());
            }
        } else {
            usuarioSesion = (DTUsuarioBase) session.getAttribute("usuarioLogueado");
            if (usuarioSesion != null) {
                nickname = usuarioSesion.getNickname();
            }
        }

        if (nickname == null || nickname.isBlank() || usuarioSesion == null) {
            enviarError(request, response, "Usuario no especificado o inexistente.", usuarioSesion, control, isJavaClient, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String fechaStr = request.getParameter("fechaNacimiento");
        if (fechaStr == null) {
            fechaStr = request.getParameter("fechaNac");
        }
        String tipoUsuario = request.getParameter("tipoUsuario");
        String newPassword = request.getParameter("password");
        String institutoSeleccionado = request.getParameter("instituto");

        // 3. Validación de campos obligatorios
        if (nombre == null || nombre.isBlank() || apellido == null || apellido.isBlank() || fechaStr == null || fechaStr.isBlank()) {
            enviarError(request, response, "Nombre, apellido y fecha de nacimiento son obligatorios.", usuarioSesion, control, isJavaClient, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 4. Control de rol e instituto
        boolean esDocente = "docente".equalsIgnoreCase(tipoUsuario);
        if (esDocente && (institutoSeleccionado == null || institutoSeleccionado.isBlank())) {
            enviarError(request, response, "Debe seleccionar un instituto para el docente.", usuarioSesion, control, isJavaClient, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 5. Conversión de fecha
        Date fechaNac;
        try {
            fechaNac = Date.valueOf(fechaStr.trim());
        } catch (IllegalArgumentException e) {
            enviarError(request, response, "Formato de fecha inválido. Utilice YYYY-MM-DD.", usuarioSesion, control, isJavaClient, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 6. Procesamiento de Imagen y Borrado Anterior
        String nombreFinalImagen = usuarioSesion.getImg(); // Mantiene la previa por defecto

        try {
            Part filePart = request.getPart("imagen");

            if (filePart != null && filePart.getSize() > 0) {
                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                String extension = "";
                int i = originalFileName.lastIndexOf('.');
                if (i > 0) {
                    extension = originalFileName.substring(i);
                }

                // Generar nuevo nombre para el archivo
                nombreFinalImagen = nickname.trim().toLowerCase() + "_" + System.currentTimeMillis() + extension;
                String uploadPath = "C:" + File.separator + "mi_proyecto_data" + File.separator + "uploads" + File.separator + "perfiles";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); 
                }

                // Borrar imagen anterior del disco si existía
                String imagenAnterior = usuarioSesion.getImg();
                if (imagenAnterior != null && !imagenAnterior.isBlank()) {
                    File archivoAnterior = new File(uploadPath + File.separator + imagenAnterior);
                    if (archivoAnterior.exists() && archivoAnterior.isFile()) {
                        archivoAnterior.delete();
                    }
                }

                // Escribir nueva imagen
                filePart.write(uploadPath + File.separator + nombreFinalImagen);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar la imagen: " + e.getMessage());
        }

        String nombreInstituto = esDocente ? institutoSeleccionado.trim() : "";
        String passwordFinal = (newPassword != null && !newPassword.isBlank()) ? newPassword.trim() : "";

        // 7. Modificación en la Lógica de Negocio
        try {
            control.ModificarUsuario(
                nickname, 
                nombre.trim(), 
                apellido.trim(),
                passwordFinal, 
                esDocente, 
                fechaNac, 
                nombreInstituto, 
                nombreFinalImagen
            );

            // Si la petición vino del cliente Swing, finaliza con éxito HTTP 200 OK
            if (isJavaClient) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("OK");
                return;
            }

            // Si la petición vino de la web, actualizar la sesión HTTP
            DTUsuarioBase usuarioActualizado = control.ConsultarUsuario(nickname);
            session.setAttribute("usuarioLogueado", usuarioActualizado);

            request.setAttribute("exito", "Datos modificados correctamente.");
            request.setAttribute("usuario", usuarioActualizado);
            request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
            request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);

        } catch (Exception e) {
            enviarError(request, response, "Error al modificar usuario: " + e.getMessage(), usuarioSesion, control, isJavaClient, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método auxiliar para centralizar el manejo de respuestas de error.
     */
    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensajeError, 
                             DTUsuarioBase usuario, IController control, boolean isJavaClient, int statusCode) 
                             throws ServletException, IOException {
        if (isJavaClient) {
            response.sendError(statusCode, mensajeError);
            return;
        }
        request.setAttribute("error", mensajeError);
        request.setAttribute("usuario", usuario);
        request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
        request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);
    }
}