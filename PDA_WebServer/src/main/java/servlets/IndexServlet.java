package servlets;

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
 * Servlet raíz – Menú de Inicio
 * Redirige al login si no hay sesión activa; si la hay, muestra el menú principal.
 */
@WebServlet(name = "IndexServlet", urlPatterns = {"/inicio", ""})
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }
        DTUsuarioBase usuario = (DTUsuarioBase) session.getAttribute("usuarioLogueado");
        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher("InterfacesJSP/MenuInicio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
