package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "MainMenu", urlPatterns = {"/MainMenu"})
public class MainMenu extends HttpServlet {
    IController ico;
    
    /*----------------Funciones de carga de lista de datos para el request-----------------*/
    private void CargarInstitutosEnSesion(HttpServletRequest request) {
        ico = Fabric.GetInstance().GetIController();
        List<DTMaster> auxListInstituto = ico.ListarClase(EnumDT.DT_INSTITUTO);
        if (auxListInstituto != null) {
            request.getSession().setAttribute("AuxListInstituto", auxListInstituto);
        }
    }
    private void CargarCategoriasEnSesion(HttpServletRequest request){
        ico = Fabric.GetInstance().GetIController();
        List<DTMaster> auxListCategoria = ico.ListarClase(EnumDT.DT_CATEGORIA);
        if (auxListCategoria != null) {
            request.getSession().setAttribute("AuxListCategoria", auxListCategoria);
        }
    }
    /*--------------------------------------------------------------------------------------*/
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("ingVisitantes".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // O session.removeAttribute("usuarioLogueado");
            }
        }
        // Cargar listas de institutos y categorías en la sesión
        CargarInstitutosEnSesion(request);
        CargarCategoriasEnSesion(request);

        // Despachar al JSP respetando la sesión actual
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
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", dt);
            
           CargarInstitutosEnSesion(request);
            CargarCategoriasEnSesion(request);
            
            request.getRequestDispatcher("/InterfacesJSP/MenuInicio.jsp").forward(request, response);
        } else {
            request.setAttribute("errorLogin", "El usuario o la contraseña son incorrectos.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet del Menú Principal";
    }
}