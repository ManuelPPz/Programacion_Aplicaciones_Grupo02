/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Servlets;

import Logica.Classes.Enum_Estado_inscripcion;
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
 *
 * @author leoli
 */
@WebServlet(name = "SeleccionarEstudiantesEdicionServlet", urlPatterns = {"/SeleccionarEstudiantesEdicionServlet"})
public class SeleccionarEstudiantesEdicionServlet extends HttpServlet {

    private static final String VISTA = "/InterfacesJSP/SeleccionarEstudiantesEdicion.jsp";

    /**
     * GET: carga los combos en cascada, consulta la edición seleccionada
     * y (opcionalmente) aplica el orden por ranking.
     */
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
                List<DTMaster> cursos = ic.ListarCursos(nomInstituto);
                request.setAttribute("cursos", cursos);

                // 3. Curso elegido -> ediciones
                String nomCurso = request.getParameter("curso");
                if (nomCurso != null && !nomCurso.trim().isEmpty()) {
                    request.setAttribute("cursoSeleccionado", nomCurso);
                    List<DTMaster> ediciones = ic.ListarEdiciones(nomCurso);
                    request.setAttribute("ediciones", ediciones);

                    // 4. Edición elegida -> datos completos
                    String nomEdicion = request.getParameter("edicion");
                    if (nomEdicion != null && !nomEdicion.trim().isEmpty()) {
                        request.setAttribute("edicionSeleccionada", nomEdicion);

                        DTMaster dtEdicion = ic.ConsultaEdicionCurso(nomEdicion);
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

    /**
     * POST: registra el nuevo estado ("Aceptada" / "Rechazada") de cada estudiante
     * y luego vuelve a cargar la pantalla con los filtros actuales.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController ic = Fabric.GetInstance().GetIController();

        String nomEdicion = request.getParameter("nomEdicion");
        String[] estudiantes = request.getParameterValues("estudiantes[]");

        if (estudiantes != null && nomEdicion != null && !nomEdicion.trim().isEmpty()) {
            try {
                // Cada elemento: {nickname (String), nuevo estado (Enum_Estado_inscripcion)}
                List<Object[]> cambios = new ArrayList<>();
                for (String nickname : estudiantes) {
                    String nuevoEstado = request.getParameter("estado_" + nickname);

                    if (nuevoEstado != null && !nuevoEstado.isEmpty()) {
                        Enum_Estado_inscripcion estado = buscarEstado(nuevoEstado);
                        if (estado == null) {
                            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
                        }
                        cambios.add(new Object[]{nickname, estado});
                    }
                }
                ic.AceptarEstudiantesAEdicionCurso(cambios, nomEdicion);
                request.setAttribute("mensajeExito",
                        "La selección de estudiantes para la edición fue registrada con éxito.");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Error al registrar la selección: " + e.getMessage());
            }
        } else {
            request.setAttribute("mensajeError", "No se recibieron estudiantes para actualizar.");
        }

        // Recarga combos y datos manteniendo los filtros (ya hace el forward a la vista)
        doGet(request, response);
    }

    /** Busca la constante del enum ignorando mayúsculas/minúsculas; null si no existe. */
    private Enum_Estado_inscripcion buscarEstado(String texto) {
        for (Enum_Estado_inscripcion e : Enum_Estado_inscripcion.values()) {
            if (e.name().equalsIgnoreCase(texto)) {
                return e;
            }
        }
        return null;
    }
}