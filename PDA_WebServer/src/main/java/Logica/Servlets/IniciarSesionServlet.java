package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * ED-13 – Iniciar Sesión
 * Valida las credenciales del usuario. Si son correctas, guarda el usuario
 * en sesión y redirige al menú principal; si no, vuelve al formulario con error.
 */
@WebServlet(name = "IniciarSesionServlet", urlPatterns = {"/IniciarSesionServlet"})
public class IniciarSesionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");          // nickname o correo
        String password = request.getParameter("password");

        if (id == null || id.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Debe completar todos los campos.");
            request.getRequestDispatcher("InterfacesJSP/IniciarSesion.jsp").forward(request, response);
            return;
        }

        IController control = Fabric.GetInstance().GetIController();
        DTMaster usuario = (DTMaster) control.ConsultaUsuarioAvanzada(id, password);

        if (usuario == null) {
            request.setAttribute("error", "Credenciales incorrectas. Intente nuevamente.");
            request.getRequestDispatcher("InterfacesJSP/IniciarSesion.jsp").forward(request, response);
            return;
        }

        // Credenciales válidas: guardar en sesión
        HttpSession session = request.getSession(true);
        session.setAttribute("usuarioLogueado", usuario);

        response.sendRedirect(request.getContextPath() + "/inicio");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("InterfacesJSP/IniciarSesion.jsp").forward(request, response);
    }
}
