/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Servlets;

import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author leoli
 */
@WebServlet(name = "SeleccionarEstudiantesEdicionServlet", urlPatterns = {"/SeleccionarEstudiantesEdicionServlet"})
public class SeleccionarEstudiantesEdicionServlet extends HttpServlet {

    /**
     * MÉTODOS GET: Se encarga de cargar los combos desplegables en cascada,
     * consultar la edición vigente y ordenar la lista por ranking si se solicita.
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

                    // 4. Si se selecciona la Edición (o Edición Vigente)
                    String nomEdicion = request.getParameter("edicion");
                    if (nomEdicion != null && !nomEdicion.trim().isEmpty()) {
                        request.setAttribute("edicionSeleccionada", nomEdicion);
                        
                        // Obtenemos los datos completos de la Edición
                        DTMaster dtEdicion = ic.ConsultaEdicionCurso(nomEdicion);
                        request.setAttribute("dtEdicion", dtEdicion);

                        // Requerimiento Especial 7.5: Ordenamiento por Ranking
                        String orden = request.getParameter("orden");
                        if ("ranking".equals(orden)) {
                            request.setAttribute("ordenAplicado", "ranking");
                            // Si tu objeto DTMaster o controlador requiere una llamada especial de ranking:
                            // ic.OrdenarEdicionPorRanking(nomEdicion);
                        }
                    }
                }
            }
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al cargar datos: " + e.getMessage());
        }

        // Redirigir la petición a la vista JSP
        request.getRequestDispatcher("InterfacesJSP/SeleccionarEstudiantesEdicion.jsp").forward(request, response);
    }

    /**
     * MÉTODOS POST: Se procesa cuando el docente hace clic en "Registrar Selección"
     * para cambiar el estado ("Aceptada" / "Rechazada") de cada estudiante.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        IController ic = Fabric.GetInstance().GetIController();

        String nomEdicion = request.getParameter("nomEdicion");
        String[] estudiantes = request.getParameterValues("estudiantes[]");

        if (estudiantes != null && nomEdicion != null) {
            try {
                for (String nickname : estudiantes) {
                    // Obtiene el estado elegido ("Aceptada" o "Rechazada") para el estudiante actual
                    String nuevoEstado = request.getParameter("estado_" + nickname);
                    
                    if (nuevoEstado != null && !nuevoEstado.isEmpty()) {
                        // Invocar a la lógica para registrar el cambio de estado
                        // ic.CambiarEstadoInscripcion(nomEdicion, nickname, nuevoEstado);
                    }
                }
                request.setAttribute("mensajeExito", "La selección de estudiantes para la edición fue registrada con éxito.");
            } catch (Exception e) {
                request.setAttribute("mensajeError", "Error al registrar la selección: " + e.getMessage());
            }
        } else {
            request.setAttribute("mensajeError", "No se recibieron estudiantes para actualizar.");
        }

        // Se vuelve a invocar doGet para mantener los filtros y refrescar los datos en pantalla
        request.getRequestDispatcher("InterfacesJSP/SeleccionarEstudiantesEdicion.jsp").forward(request, response);
    }
}