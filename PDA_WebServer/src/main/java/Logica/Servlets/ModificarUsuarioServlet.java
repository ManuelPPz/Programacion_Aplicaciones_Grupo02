package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuarioBase;
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
import java.util.Date;

/**
 * ED-18 – Modificar Datos de Usuario
 * GET:  Muestra el formulario pre-cargado con los datos del usuario en sesión.
 * POST: Aplica los cambios permitidos (nombre, apellido, fechaNac, contraseña e instituto si aplica).
 */
@WebServlet(name = "ModificarUsuarioServlet", urlPatterns = {"/ModificarUsuarioServlet"})
public class ModificarUsuarioServlet extends HttpServlet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

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
        
        request.setAttribute("usuario", usuario);
        // Carga la lista de institutos para el selector dropdown en caso de que sea docente
        request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
        request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        IController control = Fabric.GetInstance().GetIController();
        DTUsuarioBase usuarioSesion = (DTUsuarioBase) session.getAttribute("usuarioLogueado");
        
        String nickname = usuarioSesion.getNickname(); // Inmutable
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String fechaStr = request.getParameter("fechaNacimiento");
        String tipoUsuario = request.getParameter("tipoUsuario");
        
        // CAPTURA DEL INSTITUTO: Se lee como una sola cadena elegida en el dropdown
        String institutoSeleccionado = request.getParameter("instituto");
        
        // Corrección en la obtención de la contraseña
        String newPassword = request.getParameter("password");

        // Validación de campos obligatorios
        if (nombre == null || nombre.isBlank() || apellido == null || apellido.isBlank()
                || fechaStr == null || fechaStr.isBlank()) {
            
            request.setAttribute("error", "Nombre, apellido y fecha de nacimiento son obligatorios.");
            request.setAttribute("usuario", usuarioSesion);
            request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
            request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);
            return;
        }

        Date fechaNac;
        try {
            fechaNac = SDF.parse(fechaStr);
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha inválido.");
            request.setAttribute("usuario", usuarioSesion);
            request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
            request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);
            return;
        }

        boolean esDocente = "docente".equalsIgnoreCase(tipoUsuario);
        
        // Si es docente, se asigna el instituto seleccionado; de lo contrario, cadena vacía
        String nombreInstituto = (esDocente && institutoSeleccionado != null) ? institutoSeleccionado.trim() : "";
        
        // Manejo por si no se modifica la contraseña
        String passwordFinal = (newPassword != null && !newPassword.isBlank()) ? newPassword.trim() : "";

        try {
            // Invocación a la lógica pasando la cadena individual del instituto
            control.ModificarUsuario(
                nickname, 
                nombre.trim(), 
                apellido.trim(),
                passwordFinal, 
                esDocente, 
                fechaNac, 
                nombreInstituto, 
                ""
            );

            // Actualizar la información en la sesión HTTP con los datos recién modificados
            DTUsuarioBase usuarioActualizado = control.ConsultarUsuario(nickname);
            session.setAttribute("usuarioLogueado", usuarioActualizado);

            request.setAttribute("exito", "Datos modificados correctamente.");
            request.setAttribute("usuario", usuarioActualizado);
            request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
            request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error al modificar usuario: " + e.getMessage());
            request.setAttribute("usuario", usuarioSesion);
            request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
            request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);
        }
    }
}