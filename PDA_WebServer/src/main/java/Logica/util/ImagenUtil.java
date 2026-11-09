package Logica.util;

import Logica.ws.ClienteWS;
import Logica.ws.cliente.EdextWSException;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Imágenes del web. Ya no se guardan en esta PC: se envían al Servidor Central
 * por Web Service (SubirImagen) y se sirven pidiéndoselas (ver ImagenServlet).
 */
public final class ImagenUtil {

    public static final String CARPETA_PERFILES = "perfiles";
    public static final String CARPETA_CURSOS = "cursos";
    public static final String CARPETA_PROGRAMAS = "programas";
    public static final String CARPETA_EDICIONCURSOS = "edicioncursos";

    public static final String IMAGEN_POR_DEFECTO = "/Images/ImagenUserDefault.png";

    private ImagenUtil() {
    }

    /**
     * URL de una imagen guardada en el Servidor Central (servida por
     * ImagenServlet), o la imagen por defecto si no hay nombre. Los nombres
     * incluyen la fecha de subida, así que no hace falta "?v=" contra la caché.
     */
    public static String versionedUrl(String carpeta, String nombreImagen, String contextPath) {
        if (nombreImagen == null || nombreImagen.isBlank()) {
            return contextPath + IMAGEN_POR_DEFECTO;
        }
        String nombre = URLEncoder.encode(nombreImagen.trim(), StandardCharsets.UTF_8).replace("+", "%20");
        return contextPath + "/imagenes/" + carpeta + "/" + nombre;
    }

    /**
     * Envía la imagen subida en el formulario al Servidor Central y devuelve el
     * nombre con el que quedó guardada ("" si no se subió ningún archivo).
     */
    public static String guardar(Part filePart, String nombreBase, String carpeta) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return "";
        }
        String original = filePart.getSubmittedFileName() == null
                ? "" : Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        byte[] datos;
        try (InputStream in = filePart.getInputStream()) {
            datos = in.readAllBytes();
        }
        try {
            String nombre = ClienteWS.getPuerto().subirImagen(carpeta, nombreBase, original, datos);
            return nombre == null ? "" : nombre;
        } catch (EdextWSException e) {
            throw new IOException("El Servidor Central no pudo guardar la imagen: " + e.getMessage(), e);
        }
    }

    /** Pide al Servidor Central que borre una imagen (no falla si no existe). */
    public static void borrar(String carpeta, String nombreImagen) {
        if (nombreImagen == null || nombreImagen.isBlank()) {
            return;
        }
        try {
            ClienteWS.getPuerto().borrarImagen(carpeta, nombreImagen);
        } catch (Exception e) {
            System.err.println("No se pudo borrar la imagen " + carpeta + "/" + nombreImagen + ": " + e.getMessage());
        }
    }
}
