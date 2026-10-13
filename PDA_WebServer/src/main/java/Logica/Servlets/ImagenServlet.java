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
 * Sirve las imágenes guardadas en <user.home>/mi_proyecto_data.
 * Ejemplo: /imagenes/uploads/perfiles/foto_123.png
 */
@WebServlet(name = "ImagenServlet", urlPatterns = {"/imagenes/*"})
public class ImagenServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo(); // ej: /uploads/perfiles/foto_123.png
        if (path == null || path.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File base = ImagenUtil.getBaseDir().getCanonicalFile();
        File archivo = new File(base, path).getCanonicalFile();

        // Seguridad: impide salir de la carpeta base con ../
        boolean dentroDeBase = archivo.getPath().startsWith(base.getPath() + File.separator);
        if (!dentroDeBase || !archivo.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(archivo.getName());
        response.setContentType(mime != null ? mime : "application/octet-stream");
        response.setContentLengthLong(archivo.length());
        // La URL lleva ?v=<lastModified>, así que se puede cachear sin problema
        response.setHeader("Cache-Control", "public, max-age=31536000");

        Files.copy(archivo.toPath(), response.getOutputStream());
    }
}