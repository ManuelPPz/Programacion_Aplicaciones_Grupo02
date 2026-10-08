package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
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
import java.util.List;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB en memoria antes de guardar temporalmente
    maxFileSize = 1024 * 1024 * 10,      // Tamaño máximo por archivo: 10MB
    maxRequestSize = 1024 * 1024 * 50    // Tamaño máximo de la petición: 50MB
)
@WebServlet(name = "AltaUsuarioServlet", urlPatterns = {"/AltaUsuarioServlet"})
public class AltaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();

        // Cargar los institutos disponibles para mostrarlos en el combo/desplegable de la JSP
        List<DTMaster> listaInstitutos = control.ListarClase(EnumDT.DT_INSTITUTO);
        request.setAttribute("institutos", listaInstitutos);

        request.getRequestDispatcher("InterfacesJSP/AltaUsuario.jsp").forward(request, response);
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();

        String nickname = request.getParameter("nickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String fechaNacStr = request.getParameter("fechaNac");
        String tipoUsuario = request.getParameter("tipoUsuario"); 

        String institutoSeleccionado = "";

        // Acepta tanto "docente" como "Docente"
        boolean esDocente = "docente".equalsIgnoreCase(tipoUsuario);
        if(esDocente){
            institutoSeleccionado = request.getParameter("instituto"); 
        }

        // PROCESAMIENTO Y GUARDADO DE LA IMAGEN EN DISCO
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

                nombreFinalImagen = nickname.trim().toLowerCase() + "_" + System.currentTimeMillis() + extension;

                String uploadPath = "C:" + File.separator + "mi_proyecto_data" + File.separator + "uploads" + File.separator + "perfiles";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); 
                }

                String filePath = uploadPath + File.separator + nombreFinalImagen;
                filePart.write(filePath);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar la imagen: " + e.getMessage());
        }

        try {
            Date fechaNac = Date.valueOf(fechaNacStr);

            if (esDocente && (institutoSeleccionado == null || institutoSeleccionado.isBlank())) {
                // Si la petición viene de la app de escritorio responder con código HTTP 400
                if (request.getHeader("User-Agent") != null && request.getHeader("User-Agent").contains("Java")) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Debe seleccionar un instituto para el docente.");
                    return;
                }
                request.setAttribute("error", "Debe seleccionar un instituto para el docente.");
                request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
                request.getRequestDispatcher("InterfacesJSP/AltaUsuario.jsp").forward(request, response);
                return;
            }

            String nombreInstituto = esDocente ? institutoSeleccionado.trim() : "";

            control.AgregarUsuario(
                nickname.trim(), 
                nombre.trim(), 
                apellido.trim(), 
                correo.trim(), 
                password.trim(), 
                fechaNac, 
                esDocente, 
                nombreInstituto, 
                nombreFinalImagen
            );

            //Respuesta segun el cliente
            // Si la petición viene de la aplicacion de escritorio
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.contains("Java")) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("OK");
                return;
            }

            // Si la petición viene del navegador Web
            DTMaster usuarioNuevo = control.ConsultaUsuarioAvanzada(correo.trim(), password.trim());
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuarioNuevo);
            response.sendRedirect(request.getContextPath() + "/MainMenu");

        } catch (Exception e) {
            if (request.getHeader("User-Agent") != null && request.getHeader("User-Agent").contains("Java")) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            }
            request.setAttribute("error", "Error al dar de alta el usuario: " + e.getMessage());
            request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
            request.getRequestDispatcher("InterfacesJSP/AltaUsuario.jsp").forward(request, response);
        }
    }
}