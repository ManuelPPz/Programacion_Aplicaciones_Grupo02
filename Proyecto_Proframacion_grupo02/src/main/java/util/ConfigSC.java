package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Configuración del Servidor Central, leída de un archivo .properties.
 *
 * Orden de búsqueda (gana el primero que exista):
 *   1. el archivo indicado con -Dedext.config=/ruta/config.properties
 *   2. config.properties en la carpeta desde donde se ejecuta el SC
 *   3. ~/.edext/config.properties
 *   4. config.properties dentro del jar (valores por defecto)
 *
 * Claves:
 *   ws.url             URL donde se publica el Web Service
 *   edext.imagenes.dir carpeta base de las imágenes (opcional)
 */
public final class ConfigSC {

    public static final String CLAVE_WS_URL = "ws.url";
    public static final String CLAVE_IMAGENES_DIR = "edext.imagenes.dir";
    public static final String WS_URL_POR_DEFECTO = "http://localhost:9128/edext";
    public static final String NOMBRE_ARCHIVO = "config.properties";

    private final Properties props = new Properties();
    private String origen = "valores por defecto";

    private ConfigSC() {
    }

    /** Carga la configuración buscando el archivo en el orden documentado. */
    public static ConfigSC cargar() {
        ConfigSC c = new ConfigSC();
        for (File f : candidatos()) {
            if (f != null && f.isFile()) {
                try (InputStream in = new FileInputStream(f)) {
                    c.leer(in);
                    c.origen = f.getAbsolutePath();
                    return c;
                } catch (IOException e) {
                    System.err.println("No se pudo leer " + f.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
        try (InputStream in = ConfigSC.class.getResourceAsStream("/" + NOMBRE_ARCHIVO)) {
            if (in != null) {
                c.leer(in);
                c.origen = "config.properties del jar";
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer el config.properties del jar: " + e.getMessage());
        }
        return c;
    }

    /** Crea una configuración a partir de un stream (útil para tests). */
    public static ConfigSC desde(InputStream in) throws IOException {
        ConfigSC c = new ConfigSC();
        c.leer(in);
        c.origen = "stream";
        return c;
    }

    private static File[] candidatos() {
        String explicito = System.getProperty("edext.config");
        return new File[]{
            (explicito == null || explicito.isBlank()) ? null : new File(explicito),
            new File(System.getProperty("user.dir"), NOMBRE_ARCHIVO),
            new File(new File(System.getProperty("user.home"), ".edext"), NOMBRE_ARCHIVO)
        };
    }

    private void leer(InputStream in) throws IOException {
        props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    public String getWsUrl() {
        String url = props.getProperty(CLAVE_WS_URL);
        return (url == null || url.isBlank()) ? WS_URL_POR_DEFECTO : url.trim();
    }

    public String getImagenesDir() {
        String dir = props.getProperty(CLAVE_IMAGENES_DIR);
        return (dir == null || dir.isBlank()) ? null : dir.trim();
    }

    public String getOrigen() {
        return origen;
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
}
