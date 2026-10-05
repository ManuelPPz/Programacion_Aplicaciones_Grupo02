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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ED-25 – Agregar Curso a Programa de Formación
 * GET:  Carga la lista de programas y la lista de cursos disponibles.
 * POST: Agrega el/los cursos seleccionados al programa elegido.
 */
@WebServlet(name = "AgregarCursoProgramaServlet", urlPatterns = {"/AgregarCursoProgramaServlet"})
public class AgregarCursoProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        request.setAttribute("programas", control.ListarProgramaDeForm());
        request.setAttribute("cursos",    control.ListarClase(EnumDT.DT_CURSO));
        request.getRequestDispatcher("InterfacesJSP/AgregarCursoProgramaFormacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String programa  = request.getParameter("programa");
        String[] cursArr = request.getParameterValues("cursos");

        IController control = Fabric.GetInstance().GetIController();

        if (programa == null || programa.isBlank() || cursArr == null || cursArr.length == 0) {
            request.setAttribute("error", "Debe seleccionar un programa y al menos un curso.");
            request.setAttribute("programas", control.ListarProgramaDeForm());
            request.setAttribute("cursos",    control.ListarClase(EnumDT.DT_CURSO));
            request.getRequestDispatcher("InterfacesJSP/AgregarCursoProgramaFormacion.jsp").forward(request, response);
            return;
        }

        List<String> cursos = Arrays.asList(cursArr);
        try {
            control.AgregarCursoAProgramas(programa.trim(), cursos);
            request.setAttribute("exito", "Curso(s) agregado(s) al programa '" + programa + "' correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al agregar curso(s): " + e.getMessage());
        }

        request.setAttribute("programas", control.ListarProgramaDeForm());
        request.setAttribute("cursos",    control.ListarClase(EnumDT.DT_CURSO));
        request.getRequestDispatcher("InterfacesJSP/AgregarCursoProgramaFormacion.jsp").forward(request, response);
    }
}
