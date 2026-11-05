/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import jakarta.xml.ws.Service;
import Logica.WebServer.LabService;
import Logica.WebServer.Server;

/**
 * Servlet para el Caso de Uso: Listar aceptados a una edición de curso
 * @author leoli
 */
@WebServlet(name = "ListarAceptadosEdicionServlet", urlPatterns = {"/ListarAceptadosEdicionServlet"})
public class ListarEdicionAceptadosServlet extends HttpServlet {

    /**
     * GET: carga los combos en cascada (Instituto -> Curso -> Edición)
     * y consulta la edición seleccionada.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);

        try {
            // 1. Todos los institutos
            List<DTMaster> institutos = cliente.ListarClase(EnumDT.DT_INSTITUTO);
            request.setAttribute("institutos", institutos);

            // 2. Instituto elegido -> cursos
            String nomInstituto = request.getParameter("instituto");
            if (nomInstituto != null && !nomInstituto.trim().isEmpty()) {
                request.setAttribute("instSeleccionado", nomInstituto);
                List<DTMaster> cursos = cliente.ListarCursos(nomInstituto);
                request.setAttribute("cursos", cursos);

                // 3. Curso elegido -> ediciones
                String nomCurso = request.getParameter("curso");
                if (nomCurso != null && !nomCurso.trim().isEmpty()) {
                    request.setAttribute("cursoSeleccionado", nomCurso);
                    List<DTMaster> ediciones = cliente.ListarEdiciones(nomCurso);
                    request.setAttribute("ediciones", ediciones);

                    // 4. Edición elegida -> datos completos
                    String nomEdicion = request.getParameter("edicion");
                    if (nomEdicion != null && !nomEdicion.trim().isEmpty()) {
                        request.setAttribute("edicionSeleccionada", nomEdicion);

                        DTMaster dtEdicion = cliente.ConsultaEdicionCurso(nomEdicion);
                        request.setAttribute("dtEdicion", dtEdicion);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al cargar los datos: " + e.getMessage());
        }

        request.getRequestDispatcher("/InterfacesJSP/ListarEdicionAceptados.jsp").forward(request, response);
    }

    /**
     * POST: caso de uso de solo lectura, se procesa igual que GET.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}