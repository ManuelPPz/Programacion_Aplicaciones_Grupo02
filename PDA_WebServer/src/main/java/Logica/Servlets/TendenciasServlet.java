package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 7.3 Listado de tendencias: cursos más visitados, ordenados por cantidad de
 * consultas de mayor a menor. Las visitas las cuenta VisitaCursoFilter.
 */
@WebServlet(name = "TendenciasServlet", urlPatterns = {"/TendenciasServlet"})
public class TendenciasServlet extends HttpServlet {

    /** Todos los cursos que tengan al menos una visita. */
    static final int CANTIDAD = 0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        IController ico = Fabric.GetInstance().GetIController();

        // El menú lateral (InterfazBase) necesita institutos y categorías en la sesión
        List<DTMaster> institutos = ico.ListarClase(EnumDT.DT_INSTITUTO);
        if (institutos != null) {
            request.getSession().setAttribute("AuxListInstituto", institutos);
        }
        List<DTMaster> categorias = ico.ListarClase(EnumDT.DT_CATEGORIA);
        if (categorias != null) {
            request.getSession().setAttribute("AuxListCategoria", categorias);
        }

        request.setAttribute("tendencias", ico.ListarCursosMasVisitados(CANTIDAD));
        request.getRequestDispatcher("/InterfacesJSP/ListadoTendencias.jsp").forward(request, response);
    }
}
