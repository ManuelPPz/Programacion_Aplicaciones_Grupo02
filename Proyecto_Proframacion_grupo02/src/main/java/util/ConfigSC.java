package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Configuración del Servidor Central, leída del archivo de propiedades
 * config.properties (no hay URLs fijas en el código).
 *
 * Dónde se busca (gana el primero que exista):
 *   1. la ruta indicada con -Dedext.config=/ruta/config.properties
 *   2. la ruta de la variable de entorno EDEXT_CONFIG
 *   3. la carpeta del .jar (o de las clases compiladas) y las carpetas de arriba,
 *      hasta 4 niveles: así se encuentra el config.properties de la raíz del repo
 *      y también uno copiado al lado del .jar en otra PC
 *   4. la carpeta desde donde se ejecuta el programa y las de arriba
 *
 * Claves:
 *   ws.url             URL donde se publica el Web Service (obligatoria)
 *   edext.imagenes.dir carpeta base de las imágenes (opcional)
 */
public final class ConfigSC {

    public static final String CLAVE_WS_URL = "ws.url";
    public static final String CLAVE_IMAGENES_DIR = "edext.imagenes.dir";
    public static final String NOMBRE_ARCHIVO = "config.properties";
    public static final String PROPIEDAD_RUTA = "edext.config";
    public static final String VARIABLE_RUTA = "EDEXT_CONFIG";
    static final int NIVELES_HACIA_ARRIBA = 4;

    private final Properties props = new Properties();
    private File archivo;

    private ConfigSC() {
    }

    /** Busca y lee el config.properties. Si no lo encuentra, getArchivo() es null. */
    public static ConfigSC cargar() {
        return cargar(System.getenv(VARIABLE_RUTA), ubicacionDelPrograma());
    }

    /** Versión testeable: recibe la variable de entorno y la carpeta del programa. */
    static ConfigSC cargar(String variableEntorno, File carpetaPrograma) {
        ConfigSC c = new ConfigSC();
        for (File f : candidatos(variableEntorno, carpetaPrograma)) {
            if (f.isFile()) {
                try (InputStream in = new FileInputStream(f)) {
                    c.leer(in);
                    c.archivo = f.getAbsoluteFile();
                    return c;
                } catch (IOException e) {
                    System.err.println("No se pudo leer " + f.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
        return c;
    }

    /** Crea una configuración a partir de un stream (útil para tests). */
    public static ConfigSC desde(InputStream in) throws IOException {
        ConfigSC c = new ConfigSC();
        c.leer(in);
        return c;
    }

    /** Lista ordenada de rutas donde se busca el archivo. */
    static List<File> candidatos(String variableEntorno, File carpetaPrograma) {
        Set<File> lista = new LinkedHashSet<>();
        String explicito = System.getProperty(PROPIEDAD_RUTA);
        if (explicito != null && !explicito.isBlank()) {
            lista.add(new File(explicito.trim()));
        }
        if (variableEntorno != null && !variableEntorno.isBlank()) {
            lista.add(new File(variableEntorno.trim()));
        }
        agregarSubiendo(lista, carpetaPrograma);
        agregarSubiendo(lista, new File(System.getProperty("user.dir")));
        return new ArrayList<>(lista);
    }

    private static void agregarSubiendo(Set<File> lista, File desde) {
        File dir = (desde == null) ? null : desde.getAbsoluteFile();
        for (int i = 0; dir != null && i <= NIVELES_HACIA_ARRIBA; i++) {
            lista.add(new File(dir, NOMBRE_ARCHIVO));
            dir = dir.getParentFile();
        }
    }

    /** Carpeta que contiene el .jar (o la carpeta de clases si se ejecuta desde el IDE). */
    static File ubicacionDelPrograma() {
        try {
            File f = new File(ConfigSC.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return f.isFile() ? f.getParentFile() : f;
        } catch (URISyntaxException | RuntimeException e) {
            return null;
        }
    }

    private void leer(InputStream in) throws IOException {
        props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    /** true si se encontró un config.properties. */
    public boolean encontrado() {
        return archivo != null;
    }

    /** Archivo leído, o null si no se encontró ninguno. */
    public File getArchivo() {
        return archivo;
    }

    /** URL del Web Service, o null si no está configurada. */
    public String getWsUrl() {
        String url = props.getProperty(CLAVE_WS_URL);
        return (url == null || url.isBlank()) ? null : url.trim();
    }

    public String getImagenesDir() {
        String dir = props.getProperty(CLAVE_IMAGENES_DIR);
        return (dir == null || dir.isBlank()) ? null : dir.trim();
    }

    /**
     * Aplica la carpeta de imágenes como propiedad de sistema para que la use
     * ImagenUtil, salvo que ya se haya pasado con -Dedext.imagenes.dir.
     */
    public void aplicarImagenesDir() {
        String dir = getImagenesDir();
        String actual = System.getProperty(CLAVE_IMAGENES_DIR);
        if (dir != null && (actual == null || actual.isBlank())) {
            System.setProperty(CLAVE_IMAGENES_DIR, dir);
        }
    }

    /** Texto de ayuda con las rutas donde se buscó el archivo. */
    public static String rutasBuscadas() {
        StringBuilder sb = new StringBuilder();
        for (File f : candidatos(System.getenv(VARIABLE_RUTA), ubicacionDelPrograma())) {
            sb.append("  ").append(f.getAbsolutePath()).append('\n');
        }
        return sb.toString();
    }
}
