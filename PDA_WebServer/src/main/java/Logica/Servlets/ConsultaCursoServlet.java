package Logica.Servlets;

import Logica.DTsClasses.DTCurso;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTInstituto;
import Logica.DTsClasses.EnumDT;
import Logica.WebServer.LabService;
import Logica.WebServer.Server;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.Service;
import java.io.IOException;
import java.util.List;

/**
 * ED-20 – Consulta de Curso
 */
@WebServlet(name = "ConsultaCursoServlet", urlPatterns = {"/ConsultaCursoServlet"})
public class ConsultaCursoServlet extends HttpServlet {
        Server server;
        Service service;
        LabService cliente;

    private void CargarInstitutosEnSesion(HttpServletRequest request) {
        
        List<DTMaster> auxListInstituto = cliente.ListarClase(EnumDT.DT_INSTITUTO);
        if (auxListInstituto != null) {
            request.getSession().setAttribute("AuxListInstituto", auxListInstituto);
        }
    }

    private void CargarCategoriasEnSesion(HttpServletRequest request) {
        List<DTMaster> auxListCategoria = cliente.ListarClase(EnumDT.DT_CATEGORIA);
        if (auxListCategoria != null) {
            request.getSession().setAttribute("AuxListCategoria", auxListCategoria);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        server = new Server();
        service = server.getService();
        cliente = server.getPortLabService(service);
        String action = request.getParameter("action");
        if ("ingVisitantes".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }

        // Cargar listas de institutos y categorías en la sesión
        CargarInstitutosEnSesion(request);
        CargarCategoriasEnSesion(request);


        // 1. OBLIGATORIO: Cargar la lista completa de cursos para que el <select> tenga opciones
        request.setAttribute("cursos", cliente.ListarClase(EnumDT.DT_CURSO));

        // Obtener el curso de la petición (soporta tanto ?curso=X como ?nombre=X)
        String nombreCurso = request.getParameter("curso");
        if (nombreCurso == null || nombreCurso.isBlank()) {
            nombreCurso = request.getParameter("nombre");
        }

        if (nombreCurso != null && !nombreCurso.isBlank()) {
            String cursoTrim = nombreCurso.trim();
            request.setAttribute("cursoSeleccionado", cursoTrim);
            request.setAttribute("cursoElegido", cliente.ConsultaCurso(cursoTrim));
            
            // 2. CARGAR LAS EDICIONES DEL CURSO SELECCIONADO PARA EL PANEL DERECHO
            List<DTMaster> listaEdiciones = cliente.ListarEdiciones(cursoTrim);
            request.setAttribute("ediciones", listaEdiciones);

            // 3. CARGAR LOS PROGRAMAS DE FORMACION A LOS QUE PERTENECE EL CURSO (PANEL DERECHO)
            List<DTMaster> listaProgramas = cliente.ListarProgramasDeCurso(cursoTrim);
            request.setAttribute("programasDelCurso", listaProgramas);
        }

        request.getRequestDispatcher("/InterfacesJSP/ConsultaCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}