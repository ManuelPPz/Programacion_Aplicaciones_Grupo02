package servlets;

import DTsClasses.DTMaster;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import Logica.Fabric;
import Logica.IController;
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
 * ED-18 – Modificar Datos de Usuario
 * GET:  Muestra el formulario pre-cargado con los datos del usuario en sesión.
 * POST: Aplica los cambios permitidos (nombre, apellido, fechaNac). Nickname y correo son inmutables.
 */
@WebServlet(name = "ModificarUsuarioServlet", urlPatterns = {"/ModificarUsuarioServlet"})
public class ModificarUsuarioServlet extends HttpServlet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }

        IController control = Fabric.GetInstance().GetIController();
        DTUsuarioBase usuario = (DTUsuarioBase) session.getAttribute("usuarioLogueado");
        request.setAttribute("usuario", usuario);
        // Para el selector de institutos si es docente
        request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
        request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }

        DTUsuarioBase usuarioSesion = (DTUsuarioBase) session.getAttribute("usuarioLogueado");
        String nickname = usuarioSesion.getNickname(); // inmutable

        String nombre     = request.getParameter("nombre");
        String apellido   = request.getParameter("apellido");
        String fechaStr   = request.getParameter("fechaNacimiento");
        String tipoUsuario = request.getParameter("tipoUsuario");
        String[] institutoArr = request.getParameterValues("institutos");

        IController control = Fabric.GetInstance().GetIController();

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
        List<String> institutos = new ArrayList<>();
        if (esDocente && institutoArr != null) {
            institutos = Arrays.asList(institutoArr);
        }

        // Correo es inmutable → pasamos el correo actual
        control.ModificarUsuario(nickname, nombre.trim(), apellido.trim(),
                                  usuarioSesion.getCorreo(), esDocente, fechaNac, institutos, "");

        // Actualizar sesión con datos nuevos
        DTUsuarioBase usuarioActualizado = control.ConsultarUsuario(nickname);
        session.setAttribute("usuarioLogueado", usuarioActualizado);

        request.setAttribute("exito", "Datos modificados correctamente.");
        request.setAttribute("usuario", usuarioActualizado);
        request.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
        request.getRequestDispatcher("InterfacesJSP/ModificarDatosUsuario.jsp").forward(request, response);
    }
}
