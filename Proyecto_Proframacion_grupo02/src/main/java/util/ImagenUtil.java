package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Manejo de imágenes en el disco del Servidor Central.
 *
 * Las imágenes se guardan en <base>/uploads/<carpeta>/<nombre>, donde <base> es
 * la propiedad de sistema "edext.imagenes.dir" o, por defecto,
 * <user.home>/mi_proyecto_data (mismo lugar que usaba el servidor web).
 * En la BD solo se guarda el nombre del archivo.
 */
public final class ImagenUtil {

    public static final String CARPETA_PERFILES = "perfiles";
    public static final String CARPETA_CURSOS = "cursos";
    public static final String CARPETA_PROGRAMAS = "programas";
    public static final String CARPETA_EDICIONCURSOS = "edicioncursos";

    private ImagenUtil() {
    }

    public static File getBaseDir() {
        String base = System.getProperty("edext.imagenes.dir");
        if (base == null || base.isBlank()) {
            base = System.getProperty("user.home") + File.separator + "mi_proyecto_data";
        }
        return new File(base);
    }

    /** Carpeta física: <base>/uploads/<carpeta> (se crea si no existe). */
    public static File getDirectorio(String carpeta) {
        File dir = new File(new File(getBaseDir(), "uploads"), carpeta);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * Guarda los bytes de una imagen y devuelve el nombre final del archivo
     * ("" si no había datos).
     *
     * @param datos          contenido de la imagen
     * @param nombreOriginal nombre original del archivo (solo se usa su extensión)
     * @param nombreBase     base para el nombre final (nickname, nombre del curso...)
     * @param carpeta        una de las constantes CARPETA_*
     */
    public static String guardar(byte[] datos, String nombreOriginal, String nombreBase, String carpeta) throws IOException {
        if (datos == null || datos.length == 0) {
            return "";
        }
        String extension = "";
        if (nombreOriginal != null) {
            String soloNombre = new File(nombreOriginal).getName();
            int i = soloNombre.lastIndexOf('.');
            if (i > 0) {
                extension = soloNombre.substring(i).toLowerCase();
            }
        }
        String base = (nombreBase == null || nombreBase.isBlank()) ? "img" : nombreBase;
        String limpio = base.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "_");
        String nombreFinal = limpio + "_" + System.currentTimeMillis() + extension;

        File destino = new File(getDirectorio(carpeta), nombreFinal);
        Files.write(destino.toPath(), datos);
        return nombreFinal;
    }

    /** Copia un archivo local (p. ej. elegido con JFileChooser) a la carpeta de imágenes. */
    public static String guardar(File archivo, String nombreBase, String carpeta) throws IOException {
        if (archivo == null || !archivo.isFile()) {
            return "";
        }
        return guardar(Files.readAllBytes(archivo.toPath()), archivo.getName(), nombreBase, carpeta);
    }

    /** Devuelve el archivo de una imagen guardada, o null si no existe. */
    public static File getArchivo(String carpeta, String nombreImagen) {
        if (nombreImagen == null || nombreImagen.isBlank()) {
            return null;
        }
        // Evita salir de la carpeta con nombres tipo "../../algo"
        File f = new File(getDirectorio(carpeta), new File(nombreImagen).getName());
        return f.isFile() ? f : null;
    }

    /** Lee los bytes de una imagen guardada, o null si no existe. */
    public static byte[] leer(String carpeta, String nombreImagen) {
        File f = getArchivo(carpeta, nombreImagen);
        if (f == null) {
            return null;
        }
        try {
            return Files.readAllBytes(f.toPath());
        } catch (IOException e) {
            System.err.println("No se pudo leer la imagen " + f.getAbsolutePath() + ": " + e.getMessage());
            return null;
        }
    }

    public static void borrar(String carpeta, String nombreImagen) {
        File f = getArchivo(carpeta, nombreImagen);
        if (f != null) {
            f.delete();
        }
    }
}
