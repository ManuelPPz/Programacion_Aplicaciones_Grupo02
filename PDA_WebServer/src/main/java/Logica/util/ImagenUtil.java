package Logica.util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
            dir.mkdirs();
        }
        return dir;
    }

    /** URL de imagen con ?v=<lastModified> para evitar caché. */
    public static String versionedUrl(String carpeta, String nombreImagen, String contextPath) {
        String relativePath = "/uploads/" + carpeta + "/" + nombreImagen;
        File file = new File(getDirectorio(carpeta), nombreImagen);
        String url = contextPath + "/imagenes" + relativePath;
        return file.exists() ? url + "?v=" + file.lastModified() : url;
    }

    /** Guarda la imagen y devuelve el nombre final ("" si no había archivo). */
    public static String guardar(Part filePart, String nombreBase, String carpeta) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return "";
        }
        String original = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        int i = original.lastIndexOf('.');
        String extension = (i > 0) ? original.substring(i) : "";

        String limpio = nombreBase.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "_");
        String nombreFinal = limpio + "_" + System.currentTimeMillis() + extension;

        filePart.write(new File(getDirectorio(carpeta), nombreFinal).getAbsolutePath());
        return nombreFinal;
    }
   
    public static File getBaseDir() {return new File(BASE_DIR);}
    
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