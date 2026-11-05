package Logica.util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImagenUtil {

    // Único lugar donde se define la carpeta base
    private static final String BASE_DIR =
            System.getProperty("user.home") + File.separator + "mi_proyecto_data";

    public static final String CARPETA_PERFILES = "perfiles";
    public static final String CARPETA_CURSOS   = "cursos";
    public static final String CARPETA_PROGRAMAS = "programas";
    public static final String CARPETA_EDICIONCURSOS = "edicioncursos";

    /** Carpeta física: <user.home>/mi_proyecto_data/uploads/<carpeta> */
    public static File getDirectorio(String carpeta) {
        File dir = new File(BASE_DIR + File.separator + "uploads" + File.separator + carpeta);
        if (!dir.exists()) {
            boolean creados = dir.mkdirs();
            System.out.println(">>> [DEBUG ImagenUtil] Creando directorio: " + dir.getAbsolutePath() + " -> " + creados);
        }
        return dir;
    }

    /** URL de imagen con ?v=<lastModified> para evitar caché. */
    public static String versionedUrl(String carpeta, String nombreImagen, String contextPath) {
        if (nombreImagen == null || nombreImagen.isBlank()) {
            return contextPath + "/Images/ImagenUserDefault.png";
        }
        
        File file = new File(getDirectorio(carpeta), nombreImagen);
        
        // Si la imagen no existe en la carpeta física, se retorna directamente la imagen por defecto
        if (!file.exists() || !file.isFile()) {
            return contextPath + "/Images/ImagenUserDefault.png";
        }

        String relativePath = "/" + carpeta + "/" + nombreImagen;
        return contextPath + "/imagenes" + relativePath + "?v=" + file.lastModified();
    }

    /** Guarda la imagen y devuelve el nombre final ("" si no había archivo). */
    public static String guardar(Part filePart, String nombreBase, String carpeta) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            System.out.println(">>> [DEBUG ImagenUtil] filePart es nulo o de tamaño 0.");
            return "";
        }

        String original = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        int i = original.lastIndexOf('.');
        String extension = (i > 0) ? original.substring(i) : "";

        String limpio = nombreBase.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "_");
        String nombreFinal = limpio + "_" + System.currentTimeMillis() + extension;

        File archivoDestino = new File(getDirectorio(carpeta), nombreFinal);

        // Se usa Files.copy con InputStream para garantizar escritura física directa
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println(">>> [DEBUG ImagenUtil] Imagen guardada en: " + archivoDestino.getAbsolutePath());
        } catch (Exception e) {
            System.err.println(">>> [ERROR ImagenUtil] No se pudo guardar la imagen: " + e.getMessage());
            e.printStackTrace();
            return "";
        }

        return nombreFinal;
    }
   
    public static File getBaseDir() { 
        return new File(BASE_DIR); 
    }
   
    public static void borrar(String carpeta, String nombreImagen) {
        if (nombreImagen == null || nombreImagen.isBlank()) {
            return;
        }
        File f = new File(getDirectorio(carpeta), nombreImagen);
        if (f.isFile()) {
            f.delete();
        }
    }
}