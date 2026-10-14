/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Servlets;

/**
 *
 * @author leoli
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

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
 * Servlet para el Caso de Uso: Listar aceptados a una edición de curso
 * @author leoli
 */
@WebServlet(name = "ListarAceptadosEdicionServlet", urlPatterns = {"/ListarAceptadosEdicionServlet", "/listar-aceptados-edicion"})
public class ListarEdicionAceptadosServlet extends HttpServlet {

    /**
     * MÉTODOS GET: Se encarga de cargar los combos desplegables en cascada
     * (Instituto -> Curso -> Edición) y consultar la edición seleccionada.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        IController ic = Fabric.GetInstance().GetIController();

        try {
            // 1. Obtener y enviar lista de todos los Institutos
            List<DTMaster> institutos = ic.ListarClase(EnumDT.DT_INSTITUTO);
            request.setAttribute("institutos", institutos);

            // 2. Si el docente seleccionó un Instituto -> Cargar Cursos asociados
            String nomInstituto = request.getParameter("instituto");
            if (nomInstituto != null && !nomInstituto.trim().isEmpty()) {
                request.setAttribute("instSeleccionado", nomInstituto);
                List<DTMaster> cursos = ic.ListarCursos(nomInstituto);
                request.setAttribute("cursos", cursos);

                // 3. Si el docente seleccionó un Curso -> Cargar Ediciones
                String nomCurso = request.getParameter("curso");
                if (nomCurso != null && !nomCurso.trim().isEmpty()) {
                    request.setAttribute("cursoSeleccionado", nomCurso);
                    List<DTMaster> ediciones = ic.ListarEdiciones(nomCurso);
                    request.setAttribute("ediciones", ediciones);

                    // 4. Si se selecciona la Edición de Curso
                    String nomEdicion = request.getParameter("edicion");
                    if (nomEdicion != null && !nomEdicion.trim().isEmpty()) {
                        request.setAttribute("edicionSeleccionada", nomEdicion);
                        
                        // Obtenemos los datos completos de la Edición seleccionada
                        DTMaster dtEdicion = ic.ConsultaEdicionCurso(nomEdicion);
                        request.setAttribute("dtEdicion", dtEdicion);
                    }
                }
            }
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al cargar los datos: " + e.getMessage());
        }

        // Redirigir la petición a la vista JSP
        request.getRequestDispatcher("/WEB-INF/views/listar-aceptados-edicion.jsp").forward(request, response);
    }

    /**
     * MÉTODOS POST: Al ser un caso de uso exclusivamente de consulta (solo lectura),
     * redirecciona al procesamiento del método GET.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
