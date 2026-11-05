package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.EnumDT;
import Logica.WebServer.LabService;
import Logica.WebServer.Server;
import Logica.util.ImagenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.xml.ws.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jakarta.xml.ws.Service;
import javax.xml.namespace.QName;
import java.net.URL;
/**
 * ED-21 – Alta de Edición de Curso
 * GET:  Carga la lista de cursos del instituto al que pertenece el docente logueado,
 *       y la lista de docentes disponibles para seleccionar como participantes.
 * POST: Registra la nueva edición del curso.
 */
@WebServlet(name = "AltaEdicionServlet", urlPatterns = {"/AltaEdicionServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB en memoria antes de guardar temporalmente
    maxFileSize = 1024 * 1024 * 10,      // Tamaño máximo por archivo: 10MB
    maxRequestSize = 1024 * 1024 * 50    // Tamaño máximo de la petición: 50MB
)
public class AltaEdicionServlet extends HttpServlet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            
            throws ServletException, IOException {
        
        
        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);
        
        HttpSession session  = request.getSession(false);
        List<DTMaster> cursos = new ArrayList<>();
        String instituto = "";
        if (session != null && session.getAttribute("usuarioLogueado") instanceof DTDocente doc) {
            if (!doc.getInstituto().isEmpty()) {
                instituto = doc.getInstituto();
                        if (!instituto.isBlank()) {
            cursos = cliente.ListarCursos(instituto);
            
            
            
        } else {
            cursos = cliente.ListarClase(EnumDT.DT_CURSO);
        }
            }
        }

        request.setAttribute("cursos",     cursos);
        request.setAttribute("docentes",   cliente.ListarDocentes(instituto));
        
        request.getRequestDispatcher("InterfacesJSP/AltaEdicionCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String instituto = "";

        // Obtener el instituto del docente en sesión
        if (session != null && session.getAttribute("usuarioLogueado") instanceof DTDocente doc) {
            if (doc.getInstituto() != null) {
                instituto = doc.getInstituto();
            }
        }

        String curso     = request.getParameter("curso");
        String nombre    = request.getParameter("nombre");
        String inicioStr = request.getParameter("fechaInicio");
        String finStr    = request.getParameter("fechaFin");
        String cupoStr   = request.getParameter("cupo");
        String[] docArr  = request.getParameterValues("docentes");
        
        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);

        // Validar campos obligatorios
        if (instituto.isBlank() || curso == null || nombre == null || inicioStr == null || finStr == null
                || curso.isBlank() || nombre.isBlank() || inicioStr.isBlank() || finStr.isBlank()) {
            request.setAttribute("error", "Instituto, curso, nombre y fechas son obligatorios.");
            doGet(request, response);
            return;
        }

        // Procesar subida de la imagen
        String nombreFinalImagen = ""; 
        try{
            nombreFinalImagen = ImagenUtil.guardar(request.getPart("imagen"), nombre, ImagenUtil.CARPETA_EDICIONCURSOS);
        }catch (Exception e) {
            System.err.println("Error grave al guardar la imagen en disco:");
             e.printStackTrace();
        }

        Date fInicio, fFin;
        try {
            fInicio = SDF.parse(inicioStr);
            fFin    = SDF.parse(finStr);
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha inválido.");
            doGet(request, response);
            return;
        }

        int cupo = 0;
        if (cupoStr != null && !cupoStr.isBlank()) {
            try { cupo = Integer.parseInt(cupoStr); } catch (NumberFormatException ignored) {}
        }

        List<String> docentes = docArr != null ? Arrays.asList(docArr) : new ArrayList<>();

        try {
            cliente.AltaEdicionCurso(instituto.trim(), curso.trim(), nombre.trim(),
                                     fInicio, fFin, cupo, docentes, new Date(), nombreFinalImagen);
            request.setAttribute("exito", "Edición '" + nombre + "' creada correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al crear la edición: " + e.getMessage());
        }

        doGet(request, response);
    }
}
