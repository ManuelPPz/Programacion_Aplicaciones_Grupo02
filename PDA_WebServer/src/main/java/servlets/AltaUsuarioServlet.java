package servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTInstituto;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ED-15 – Alta de Usuario
 * GET:  Muestra el formulario con listas de institutos pre-cargadas.
 * POST: Captura los datos, valida contraseñas, y llama a IController.AgregarUsuario().
 */
@WebServlet(name = "AltaUsuarioServlet", urlPatterns = {"/AltaUsuarioServlet"})
@MultipartConfig
public class AltaUsuarioServlet extends HttpServlet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        List<DTMaster> institutos = control.ListarClase(EnumDT.DT_INSTITUTO);
        request.setAttribute("institutos", institutos);
        request.getRequestDispatcher("InterfacesJSP/AltaUsuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nickname    = request.getParameter("nickname");
        String nombre      = request.getParameter("nombre");
        String apellido    = request.getParameter("apellido");
        String correo      = request.getParameter("correo");
        String fechaStr    = request.getParameter("fechaNacimiento");
        String password    = request.getParameter("password");
        String passConfirm = request.getParameter("passwordConfirm");
        String tipoUsuario = request.getParameter("tipoUsuario");
        // Los institutos pueden ser múltiples (solo para Docente)
        String[] institutoArr = request.getParameterValues("institutos");

        IController control = Fabric.GetInstance().GetIController();

        // --- Validaciones ---
        if (nickname == null || nickname.isBlank() ||
            nombre   == null || nombre.isBlank()   ||
            apellido == null || apellido.isBlank()  ||
            correo   == null || correo.isBlank()    ||
            fechaStr == null || fechaStr.isBlank()  ||
            password == null || password.isBlank()) {

            reenviarConError(request, response, control, "Todos los campos son obligatorios.");
            return;
        }

        if (!password.equals(passConfirm)) {
            reenviarConError(request, response, control, "Las contraseñas no coinciden.");
            return;
        }

        Date fechaNac;
        try {
            fechaNac = SDF.parse(fechaStr);
        } catch (ParseException e) {
            reenviarConError(request, response, control, "Formato de fecha inválido.");
            return;
        }

        boolean esDocente = "docente".equalsIgnoreCase(tipoUsuario);
        List<String> institutos = new ArrayList<>();
        if (esDocente && institutoArr != null) {
            institutos = Arrays.asList(institutoArr);
        }

        try {
            // imgPath vacío por ahora; la carga de archivos requiere multipart config adicional
            control.AgregarUsuario(nickname.trim(), nombre.trim(), apellido.trim(),
                                   correo.trim(), fechaNac, esDocente, institutos, "");
            request.setAttribute("exito", "Usuario '" + nickname + "' registrado correctamente.");
            doGet(request, response);   // volver al formulario limpio
        } catch (Exception e) {
            reenviarConError(request, response, control, "Error al registrar usuario: " + e.getMessage());
        }
    }

    /** Carga las listas necesarias para el formulario y reenvía con mensaje de error. */
    private void reenviarConError(HttpServletRequest req, HttpServletResponse res,
                                   IController control, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.setAttribute("institutos", control.ListarClase(EnumDT.DT_INSTITUTO));
        req.getRequestDispatcher("InterfacesJSP/AltaUsuario.jsp").forward(req, res);
    }
}
