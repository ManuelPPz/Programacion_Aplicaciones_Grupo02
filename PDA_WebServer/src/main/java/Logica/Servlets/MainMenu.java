package Logica.Servlets;
//Librerias de package logica
import Logica.DTsClasses.DTMaster;
import Logica.Logica.Fabric;
import Logica.Logica.IController;



import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MainMenu", urlPatterns = {"/MainMenu"})
public class MainMenu extends HttpServlet {
    IController ico;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si entran por GET (como el enlace de visitante), también los enviamos al JSP
        request.setAttribute("usuario", null);
        request.getRequestDispatcher("/InterfacesJSP/MenuInicio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ico = Fabric.GetInstance().GetIController();
        // 1. Capturar los datos del formulario
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        DTMaster dt = ico.ConsultaUsuarioAvanzada(email, password);
        if(dt!=null){
            // 2. (Opcional) Guardar datos en la solicitud para usarlos en el JSP
            request.setAttribute("usuario", dt);

            // 3. Redirigir la petición directamente al JSP (sin usar PrintWriter)
            request.getRequestDispatcher("/InterfacesJSP/MenuInicio.jsp").forward(request, response);
        }else {
            // USUARIO INVÁLIDO: Guardamos el mensaje y volvemos al login (index.jsp)
            request.setAttribute("errorLogin", "El usuario o la contraseña son incorrectos.");
            
            // Ajusta la ruta a tu index.jsp si está dentro de una carpeta (ej: "/InterfacesJSP/index.jsp" o "/index.jsp")
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Servlet del Menú Principal";
    }
}
