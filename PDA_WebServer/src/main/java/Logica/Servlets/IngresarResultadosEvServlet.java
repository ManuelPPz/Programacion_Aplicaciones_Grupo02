/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Logica.Servlets;

import Logica.Classes.Enum_Estado_inscripcion;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import Logica.WebServer.LabService;
import Logica.WebServer.Server;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.Service;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mateo
 */
@WebServlet(name = "IngresarResultadosEvServlet", urlPatterns = {"/IngresarResultadosEvServlet"})
public class IngresarResultadosEvServlet extends HttpServlet {

    private static final String VISTA = "/InterfacesJSP/IngresoEvaluaciones.jsp";

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

                        // Requerimiento 7.5: ordenar por ranking
                        String orden = request.getParameter("orden");
                        if ("ranking".equals(orden)) {
                            request.setAttribute("ordenAplicado", "ranking");
                            // Si tu controlador tiene una llamada de ranking, usala acá:
                            // ic.OrdenarEdicionPorRanking(nomEdicion);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al cargar datos: " + e.getMessage());
        }

        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);

        String nomEdicion = request.getParameter("nomEdicion");
        String[] estudiantes = request.getParameterValues("estudiantes[]");
        String accion = request.getParameter("accion"); // "guardar" o "finalizar"

        if (estudiantes != null && nomEdicion != null && !nomEdicion.trim().isEmpty()) {
            try {
                List<String> nicknames = new ArrayList<>();
                List<Integer> notas = new ArrayList<>();

                for (String nickname : estudiantes) {
                    String notaStr = request.getParameter("calificacion_" + nickname);
                    int nota = (notaStr != null && !notaStr.isEmpty()) ? Integer.parseInt(notaStr) : 1;

                    nicknames.add(nickname);
                    notas.add(nota);
                }

                // Llamada a tu servicio cliente WebService
                // cliente.IngresarResultadosEvaluaciones(nomEdicion, nicknames, notas);
                cliente.IngresarEvaluacionesDeEstudiantes(nomEdicion, nicknames, notas);
                if ("finalizar".equals(accion)) {
                    // cliente.FinalizarEdicionCurso(nomEdicion);
                    cliente.FinalizarEdicionDeCurso(nomEdicion, nicknames, notas);
                    request.setAttribute("mensajeExito", "Se registraron las calificaciones y se finalizó la edición.");
                } else {
                    request.setAttribute("mensajeExito", "Calificaciones guardadas correctamente.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Error al procesar la solicitud: " + e.getMessage());
            }
        } else {
            request.setAttribute("mensajeError", "No se recibieron estudiantes para evaluar.");
        }

        // Mantiene los filtros actualizados recargando la vista mediante doGet
        doGet(request, response);
    }

    private Enum_Estado_inscripcion buscarEstado(String texto) {
        for (Enum_Estado_inscripcion e : Enum_Estado_inscripcion.values()) {
            if (e.name().equalsIgnoreCase(texto)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
