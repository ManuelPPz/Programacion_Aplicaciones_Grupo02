package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.Logica.Fabric;
import Logica.Logica.IController;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "MainMenu", urlPatterns = {"/MainMenu"})
public class MainMenu extends HttpServlet {
    IController ico;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ingreso como visitante / Cierre de sesión implícito vía GET
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("usuarioLogueado");
        }
        request.getRequestDispatcher("/InterfacesJSP/MenuInicio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ico = Fabric.GetInstance().GetIController();
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        DTMaster dt = ico.ConsultaUsuarioAvanzada(email, password);
        
        if (dt != null) {
            // Guardar usuario en la SESIÓN HTTP
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", dt);
            request.setAttribute("usuario", dt); // Opcional: disponibilidad en alcance de request

            request.getRequestDispatcher("/InterfacesJSP/MenuInicio.jsp").forward(request, response);
        } else {
            // Error de autenticación
            request.setAttribute("errorLogin", "El usuario o la contraseña son incorrectos.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet del Menú Principal";
    }
}