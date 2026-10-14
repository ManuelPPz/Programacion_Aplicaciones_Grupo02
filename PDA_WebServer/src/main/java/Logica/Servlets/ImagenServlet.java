package Logica.Servlets;

import Logica.util.ImagenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Sirve las imágenes guardadas en <user.home>/mi_proyecto_data/uploads/
 */
@WebServlet(name = "ImagenServlet", urlPatterns = {"/imagenes/*"})
public class ImagenServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo(); // ej: /programas/foto_123.png
        if (path == null || path.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Si por compatibilidad viene con /uploads/ al inicio, se limpia correctamente
        if (path.startsWith("/uploads/")) {
            path = path.substring(8); // "/uploads" tiene 8 caracteres
        }

        File baseUploads = new File(ImagenUtil.getBaseDir(), "uploads").getCanonicalFile();
        
        // Construcción limpia del archivo destino
        File archivo = new File(baseUploads, path).getCanonicalFile();

        // Seguridad: impide salir de la carpeta uploads mediante ../ y verifica la existencia del archivo
        if (!archivo.getPath().startsWith(baseUploads.getPath() + File.separator) || !archivo.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(archivo.getName());
        response.setContentType(mime != null ? mime : "application/octet-stream");
        response.setContentLengthLong(archivo.length());
        response.setHeader("Cache-Control", "public, max-age=31536000");

        Files.copy(archivo.toPath(), response.getOutputStream());
    }
}