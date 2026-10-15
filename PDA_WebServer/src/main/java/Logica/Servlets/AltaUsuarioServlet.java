package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import Logica.util.ImagenUtil;
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

        // Cargar los institutos disponibles para mostrarlos en el desplegable de la JSP
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

        // Guardar imagen de perfil
        String nombreFinalImagen = "";
        try {
            nombreFinalImagen = ImagenUtil.guardar(request.getPart("imagen"), nombre, ImagenUtil.CARPETA_PERFILES);
        } catch (Exception e) {
            System.err.println("Error grave al guardar la imagen en disco:");
            e.printStackTrace();
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

            // 1. Verificación previa de Nickname repetido usando tu interfaz IController
            if (control.VerificarNickname(nickname.trim())) {
                throw new Exception("El nickname '" + nickname.trim() + "' ya se encuentra registrado.");
            }

            // 2. Verificación previa de Correo repetido usando tu interfaz IController
            if (control.VerificarCorreo(correo.trim())) {
                throw new Exception("El correo electrónico '" + correo.trim() + "' ya se encuentra registrado.");
            }

            String nombreInstituto = esDocente ? institutoSeleccionado.trim() : "";

            // Alta del usuario
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

            // Respuesta si la petición viene de la aplicación de escritorio (Java)
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

            String mensajeError = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

            // Asignamos el error al campo correspondiente basándonos en la excepción controlada
            if (mensajeError.contains("nickname")) {
                request.setAttribute("errorNicknameDuplicado", e.getMessage());
            } else if (mensajeError.contains("correo") || mensajeError.contains("electrónico")) {
                request.setAttribute("errorCorreoDuplicado", e.getMessage());
            } else {
                // Captura adicional por si ocurre un error de la base de datos o transacción
                Throwable causa = e;
                String traza = "";
                while (causa != null) {
                    if (causa.getMessage() != null) traza += " " + causa.getMessage().toLowerCase();
                    causa = causa.getCause();
                }

                if (traza.contains("nickname") || traza.contains("duplicate") || traza.contains("unique")) {
                    request.setAttribute("errorNicknameDuplicado", "El nickname '" + nickname + "' ya está en uso. Por favor elija otro.");
                } else if (traza.contains("correo") || traza.contains("email")) {
                    request.setAttribute("errorCorreoDuplicado", "El correo electrónico '" + correo + "' ya se encuentra registrado.");
                } else {
                    request.setAttribute("error", "Error al dar de alta el usuario: " + e.getMessage());
                }
            }

            request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
            request.getRequestDispatcher("InterfacesJSP/AltaUsuario.jsp").forward(request, response);
        }
    }
}