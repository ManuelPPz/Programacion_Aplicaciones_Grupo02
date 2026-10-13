package Logica.Servlets;

import Logica.DTsClasses.DTCurso;
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
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ED-19 – Alta de Curso
 * GET:  Carga listas de institutos, categorías y cursos existentes.
 *       Si recibe 'instituto', retorna un JSON con los cursos filtrados para AJAX.
 * POST: Registra el nuevo curso en la lógica del servidor central.
 */
@WebServlet(name = "AltaCursoServlet", urlPatterns = {"/AltaCursoServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB en memoria antes de guardar temporalmente
    maxFileSize = 1024 * 1024 * 10,      // Tamaño máximo por archivo: 10MB
    maxRequestSize = 1024 * 1024 * 50    // Tamaño máximo de la petición: 50MB
)
public class AltaCursoServlet extends HttpServlet {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        String institutoParam = request.getParameter("instituto");

        // --- PETICIÓN AJAX: Retorna JSON de cursos filtrados por instituto ---
        // Validación estricta para ignorar valores nulos, vacíos o las cadenas "null"/"undefined"
        if (institutoParam != null 
                && !institutoParam.isBlank() 
                && !institutoParam.equalsIgnoreCase("null") 
                && !institutoParam.equalsIgnoreCase("undefined")) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            List<DTMaster> listaCursos = control.ListarClase(EnumDT.DT_CURSO);

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            if (listaCursos != null) {
                for (DTMaster master : listaCursos) {
                    if (master instanceof DTCurso) {
                        DTCurso curso = (DTCurso) master;

                        // Extraer el nombre en String para comparar correctamente
                        // Si getInstituto() retorna un DTInstituto, usa getInstituto().getNombre()
                        String nombreInst = (curso.getInstituto() != null) ? curso.getInstituto(): null;

                        if (nombreInst != null && nombreInst.equals(institutoParam)) {
                            if (first) {
                                json.append("{\"nombre\":\"").append(curso.getNombre()).append("\"}");
                                first = false;
                            } else {
                                json.append(",{\"nombre\":\"").append(curso.getNombre()).append("\"}");
                            }
                        }
                    }
                }
            }
            json.append("]");

            out.print(json.toString());
            out.flush();
            return;
        }

        // --- CARGA NORMAL DE LA PÁGINA (GET) ---
        cargarListas(request, control);
        request.getRequestDispatcher("InterfacesJSP/AltaCurso.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String instituto   = request.getParameter("instituto");
        String nombre      = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String durStr      = request.getParameter("duracion");
        String horasStr    = request.getParameter("horas");
        String credStr     = request.getParameter("creditos");
        String url         = request.getParameter("url");
        String fechaStr    = request.getParameter("fechaIngreso");
        String[] prevArr   = request.getParameterValues("previas");
        String[] catArr    = request.getParameterValues("categorias");

        // Procesar subida de la imagen
        String nombreFinalImagen = ""; 
        try {
            Part filePart = request.getPart("imagen");

            if (filePart != null && filePart.getSize() > 0) {
                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                String extension = "";
                int i = originalFileName.lastIndexOf('.');
                if (i > 0) {
                    extension = originalFileName.substring(i);
                }

                // LIMPIEZA DE NOMBRE: Reemplaza espacios y caracteres especiales por guiones bajos
                String nombreLimpio = nombre.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "_");
                nombreFinalImagen = nombreLimpio + "_" + System.currentTimeMillis() + extension;

                String uploadPath = "C:" + File.separator + "mi_proyecto_data" + File.separator + "uploads" + File.separator + "cursos";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // Crea toda la estructura de carpetas si no existe
                }

                String filePath = uploadPath + File.separator + nombreFinalImagen;
                filePart.write(filePath); // Guarda el archivo físicamente en disco
            }
        } catch (Exception e) {
            System.err.println("Error grave al guardar la imagen en disco:");
            e.printStackTrace(); // Ver el error exacto en la consola de Tomcat
        }

        // Obtener el docente responsable desde la sesión
        HttpSession session = request.getSession(false);
        String docente = (session != null && session.getAttribute("usuarioLogueado") instanceof DTUsuarioBase)
                ? ((DTUsuarioBase) session.getAttribute("usuarioLogueado")).getNickname()
                : "";

        IController control = Fabric.GetInstance().GetIController();

        // Validaciones de campos obligatorios
        if (instituto == null || nombre == null || descripcion == null || url == null
                || instituto.isBlank() || nombre.isBlank() || descripcion.isBlank() || url.isBlank()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            cargarListas(request, control);
            request.getRequestDispatcher("InterfacesJSP/AltaCurso.jsp").forward(request, response);
            return;
        }

        int duracion, creditos;
        float horas;
        Date fechaIngreso;
        try {
            duracion     = Integer.parseInt(durStr);
            horas        = Float.parseFloat(horasStr);
            creditos     = Integer.parseInt(credStr);
            fechaIngreso = (fechaStr != null && !fechaStr.isBlank()) ? SDF.parse(fechaStr) : new Date();
        } catch (NumberFormatException | ParseException e) {
            request.setAttribute("error", "Valores numéricos o de fecha inválidos.");
            cargarListas(request, control);
            request.getRequestDispatcher("InterfacesJSP/AltaCurso.jsp").forward(request, response);
            return;
        }

        List<String> previas    = prevArr != null ? Arrays.asList(prevArr) : new ArrayList<>();
        List<String> categorias = catArr  != null ? Arrays.asList(catArr)  : new ArrayList<>();

        try {
            control.AltaCurso(instituto.trim(), nombre.trim(), descripcion.trim(),
                               duracion, horas, creditos, url.trim(),
                               previas, fechaIngreso, docente, categorias, nombreFinalImagen);
            request.setAttribute("exito", "Curso '" + nombre + "' registrado correctamente.");
        } catch (Exception e) {
            request.setAttribute("error", "Error al registrar el curso: " + e.getMessage());
        }

        cargarListas(request, control);
        request.getRequestDispatcher("InterfacesJSP/AltaCurso.jsp").forward(request, response);
    }

    private void cargarListas(HttpServletRequest req, IController ctrl) {
        req.setAttribute("institutos", ctrl.ListarClase(EnumDT.DT_INSTITUTO));
        req.setAttribute("categorias", ctrl.ListarClase(EnumDT.DT_CATEGORIA));
        req.setAttribute("cursos",     ctrl.ListarClase(EnumDT.DT_CURSO));
    }
}