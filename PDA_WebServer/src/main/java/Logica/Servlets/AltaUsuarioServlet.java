package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig; // <-- AGREGAR
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@MultipartConfig // <-- OBLIGATORIO para procesar formularios con enctype="multipart/form-data"
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
        
        // Capturar el nombre del instituto seleccionado de la pestaña/formulario anterior
        String institutoSeleccionado = "";

        boolean esDocente = "docente".equalsIgnoreCase(tipoUsuario);
        if(esDocente){
            institutoSeleccionado = request.getParameter("instituto"); 
        }
        try {
            Date fechaNac = Date.valueOf(fechaNacStr);

            // Validar que si es docente se haya recibido un instituto válido
            if (esDocente && (institutoSeleccionado == null || institutoSeleccionado.isBlank())) {
                request.setAttribute("error", "Debe seleccionar un instituto para el docente.");
                
                // Recargar lista de institutos para no perder el combo en caso de error
                request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
                request.getRequestDispatcher("InterfacesJSP/AltaUsuario.jsp").forward(request, response);
                return;
            }

            // Si es estudiante, el instituto se manda vacío o null
            String nombreInstituto = esDocente ? institutoSeleccionado.trim() : "";

            // Invocación a la lógica pasando el nombre del instituto seleccionado
            control.AgregarUsuario(
                nickname.trim(), 
                nombre.trim(), 
                apellido.trim(),
                correo.trim(), 
                password.trim(), 
                fechaNac, 
                esDocente, 
                nombreInstituto, 
                ""
            );

            // Redireccionar al menú o pantalla de éxito
            response.sendRedirect(request.getContextPath() + "/MainMenu");

        } catch (Exception e) {
            request.setAttribute("error", "Error al dar de alta el usuario: " + e.getMessage());
            request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
            request.getRequestDispatcher("InterfacesJSP/AltaUsuario.jsp").forward(request, response);
        }
    }
}