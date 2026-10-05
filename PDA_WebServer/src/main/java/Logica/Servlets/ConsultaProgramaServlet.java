package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTInstituto;
import Logica.DTsClasses.DTProgramaForm;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ED-26 – Consulta de Programa de Formación
 * GET sin parámetros:   Carga la lista de todos los programas.
 * GET con ?programa=X:  Muestra el detalle del programa seleccionado (nombre, descripción,
 *                       vigencia y lista de cursos que contiene).
 */
@WebServlet(name = "ConsultaProgramaServlet", urlPatterns = {"/ConsultaProgramaServlet"})
public class ConsultaProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        request.setAttribute("programas", control.ListarProgramaDeForm());

        String programa = request.getParameter("programa");
        if (programa != null && !programa.isBlank()) {
            DTMaster detalle = control.ConsultaProgramaFormacion(programa.trim());
            if (detalle instanceof DTProgramaForm dtp) {
                request.setAttribute("programaDetalle", dtp);
            } else {
                request.setAttribute("error", "No se encontró el programa '" + programa + "'.");
            }
        }

        request.getRequestDispatcher("InterfacesJSP/ConsultaProgramaFormacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
