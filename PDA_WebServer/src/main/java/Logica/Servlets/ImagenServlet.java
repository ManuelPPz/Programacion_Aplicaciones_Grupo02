package Logica.Servlets;

import Logica.util.ImagenUtil;
import Logica.ws.ClienteWS;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Sirve /imagenes/<carpeta>/<nombre> pidiéndole la imagen al Servidor Central
 * por Web Service (las imágenes se guardan solo en la PC del Servidor Central).
 */
@WebServlet(name = "ImagenServlet", urlPatterns = {"/imagenes/*"})
public class ImagenServlet extends HttpServlet {

    private static final Set<String> CARPETAS = Set.of(
            ImagenUtil.CARPETA_PERFILES, ImagenUtil.CARPETA_CURSOS,
            ImagenUtil.CARPETA_PROGRAMAS, ImagenUtil.CARPETA_EDICIONCURSOS);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo(); // ej: /programas/foto_123.png
        if (path != null && path.startsWith("/uploads/")) {
            path = path.substring("/uploads".length()); // compatibilidad con URLs viejas
        }
        String[] partes = (path == null) ? new String[0] : path.split("/");
        // partes[0] es "" porque el path empieza con "/"
        if (partes.length != 3 || !CARPETAS.contains(partes[1]) || partes[2].isBlank()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String carpeta = partes[1];
        String nombre = partes[2];

        byte[] datos;
        try {
            datos = ClienteWS.getPuerto().obtenerImagen(carpeta, nombre);
        } catch (Exception e) {
            log("No se pudo obtener la imagen " + carpeta + "/" + nombre + " del Servidor Central", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return;
        }
        if (datos == null || datos.length == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(nombre);
        response.setContentType(mime != null ? mime : "application/octet-stream");
        response.setContentLength(datos.length);
        // El nombre incluye la fecha de subida: una imagen nueva siempre tiene otro nombre
        response.setHeader("Cache-Control", "public, max-age=86400");
        response.getOutputStream().write(datos);
    }
}
