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
import java.util.List;

/**
 * Ingreso de resultados de evaluaciones: el docente elige instituto, curso y
 * edición, carga la calificación (1 a 12) de cada estudiante y puede guardar
 * o guardar y finalizar la edición.
 *
 * @author mateo
 */
@WebServlet(name = "IngresarResultadosEvServlet", urlPatterns = {"/IngresarResultadosEvServlet"})
public class IngresarResultadosEvServlet extends HttpServlet {

    private static final String VISTA = "/InterfacesJSP/IngresoEvaluaciones.jsp";

    /** GET: carga los combos en cascada y la edición seleccionada. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController ic = Fabric.GetInstance().GetIController();

        try {
            // 1. Todos los institutos
            List<DTMaster> institutos = ic.ListarClase(EnumDT.DT_INSTITUTO);
            request.setAttribute("institutos", institutos);

            // 2. Instituto elegido -> cursos
            String nomInstituto = request.getParameter("instituto");
            if (nomInstituto != null && !nomInstituto.trim().isEmpty()) {
                request.setAttribute("instSeleccionado", nomInstituto);
                request.setAttribute("cursos", ic.ListarCursos(nomInstituto));

                // 3. Curso elegido -> ediciones
                String nomCurso = request.getParameter("curso");
                if (nomCurso != null && !nomCurso.trim().isEmpty()) {
                    request.setAttribute("cursoSeleccionado", nomCurso);
                    request.setAttribute("ediciones", ic.ListarEdiciones(nomCurso));

                    // 4. Edición elegida -> datos completos
                    String nomEdicion = request.getParameter("edicion");
                    if (nomEdicion != null && !nomEdicion.trim().isEmpty()) {
                        request.setAttribute("edicionSeleccionada", nomEdicion);
                        request.setAttribute("dtEdicion", ic.ConsultaEdicionCurso(nomEdicion));

                        if ("ranking".equals(request.getParameter("orden"))) {
                            request.setAttribute("ordenAplicado", "ranking");
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

    /** POST: guarda las calificaciones; con accion=finalizar además cierra la edición. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController ic = Fabric.GetInstance().GetIController();

        String nomEdicion = request.getParameter("nomEdicion");
        String[] estudiantes = request.getParameterValues("estudiantes[]");
        String accion = request.getParameter("accion"); // "guardar" o "finalizar"

        if (estudiantes != null && nomEdicion != null && !nomEdicion.trim().isEmpty()) {
            try {
                List<String> nicknames = new ArrayList<>();
                List<Integer> notas = new ArrayList<>();

                for (String nickname : estudiantes) {
                    String notaStr = request.getParameter("calificacion_" + nickname);
                    int nota;
                    try {
                        nota = (notaStr != null && !notaStr.isBlank()) ? Integer.parseInt(notaStr.trim()) : 1;
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("La calificación de " + nickname + " no es un número válido.");
                    }
                    nicknames.add(nickname);
                    notas.add(nota);
                }

                if ("finalizar".equals(accion)) {
                    ic.FinalizarEdicionDeCurso(nomEdicion.trim(), nicknames, notas);
                    request.setAttribute("mensajeExito", "Se registraron las calificaciones y se finalizó la edición.");
                } else {
                    ic.IngresarResultadosDeEvaluaciones(nomEdicion.trim(), nicknames, notas);
                    request.setAttribute("mensajeExito", "Calificaciones guardadas correctamente.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Error al procesar la solicitud: " + e.getMessage());
            }
        } else {
            request.setAttribute("mensajeError", "No se recibieron estudiantes para evaluar.");
        }

        // Mantiene los filtros recargando la vista
        doGet(request, response);
    }
}
