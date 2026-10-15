package Logica.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * ED-14 – Cierre de Sesión
 * Invalida la sesión HTTP actual y redirige al formulario de inicio de sesión.
 */
@WebServlet(name = "CierreSesionServlet", urlPatterns = {"/CierreSesionServlet"})
public class CierreSesionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Redirigir al login con mensaje de confirmación
        response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet?mensaje=Sesion+cerrada+correctamente");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("InterfacesJSP/CierreSesion.jsp").forward(request, response);
    }
}
