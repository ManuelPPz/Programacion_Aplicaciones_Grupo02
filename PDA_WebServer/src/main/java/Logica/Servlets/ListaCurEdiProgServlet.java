/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Logica.Servlets;

import Logica.DTsClasses.EnumDT;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;

//imports servidor web
import jakarta.xml.ws.Service;
import Logica.WebServer.LabService;
import Logica.WebServer.Server;
/**
 *
 * @author mateo
 */
@WebServlet(name = "ListaCurEdiProgServlet", urlPatterns = {"/ListaCurEdiProgServlet"})
public class ListaCurEdiProgServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            Server server = new Server();
            Service service = server.getService();
            LabService cliente = server.getPortLabService(service);
        
        String tipoDeBusqueda = request.getParameter("tipoDeBusqueda");
        String dato = request.getParameter("dato");
        
        if(tipoDeBusqueda!=null && tipoDeBusqueda.equals("Categoria")){
            request.setAttribute("ListaFinal", cliente.ListarCurEdiProg(EnumDT.DT_CATEGORIA, dato));
        }else if(tipoDeBusqueda!=null && tipoDeBusqueda.equals("Instituto")){
            request.setAttribute("ListaFinal", cliente.ListarCurEdiProg(EnumDT.DT_INSTITUTO, dato));
        }else{
            request.setAttribute("error", "No se encontraron datos.");
        }
        
        request.getRequestDispatcher("/InterfacesJSP/ConsultaCurEdiProg.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
