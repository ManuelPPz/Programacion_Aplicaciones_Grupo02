package Logica.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Cierra la sesión del usuario logueado y vuelve al menú principal.
 * Conserva las listas de institutos y categorías del menú lateral,
 * que se guardan en la sesión y no pertenecen al usuario.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Object institutos = null;
        Object categorias = null;

        HttpSession anterior = request.getSession(false);
        if (anterior != null) {
            institutos = anterior.getAttribute("AuxListInstituto");
            categorias = anterior.getAttribute("AuxListCategoria");
            anterior.invalidate();
        }

        // Sesión nueva y limpia, solo con los datos del menú lateral
        HttpSession nueva = request.getSession(true);
        if (institutos != null) {
            nueva.setAttribute("AuxListInstituto", institutos);
        }
        if (categorias != null) {
            nueva.setAttribute("AuxListCategoria", categorias);
        }

        response.sendRedirect(request.getContextPath() + "/MainMenu");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cerrarSesion(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cerrarSesion(request, response);
    }
}
