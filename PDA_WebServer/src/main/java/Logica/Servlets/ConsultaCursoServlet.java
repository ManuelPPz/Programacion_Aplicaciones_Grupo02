package Logica.Servlets;

import Logica.DTsClasses.DTCurso;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTInstituto;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ED-20 – Consulta de Curso
 * GET sin parámetros:      Carga la lista completa de institutos y categorías para filtrar.
 * GET con ?filtroTipo+filtroValor: Carga los cursos del instituto o categoría elegida.
 * GET con ?curso=X:        Muestra el detalle completo del curso seleccionado.
 */
@WebServlet(name = "ConsultaCursoServlet", urlPatterns = {"/ConsultaCursoServlet"})
public class ConsultaCursoServlet extends HttpServlet {
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


        String curso = "PDA";
        request.setAttribute("cursoElegido", ico.ConsultaCurso(curso));
        request.setAttribute("edicionesDelCurso", ico.ListarEdiciones(curso));
        request.setAttribute("programasDelCurso", ico.ListarProgramasDeCurso(curso));

        request.getRequestDispatcher("InterfacesJSP/ConsultaCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
