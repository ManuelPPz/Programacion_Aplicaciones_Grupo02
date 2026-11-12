package Logica.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Busca el config.properties compartido con el Servidor Central (clave ws.url).
 *
 * Dónde se busca (gana el primero que exista):
 *   1. la ruta indicada con -Dedext.config=/ruta/config.properties
 *   2. la ruta de la variable de entorno EDEXT_CONFIG
 *   3. ~/.edEXT/config.properties del usuario que ejecuta Tomcat (Requerimiento 7.8:
 *      la ubicación normal; se cambia sin volver a generar web.war)
 *   4. la carpeta donde Tomcat desplegó la aplicación y las de arriba, hasta 4
 *      niveles (con NetBeans llega a la raíz del repo)
 *   5. la carpeta de Tomcat (catalina.base) y la de arriba
 * Si no se encuentra, se usa el context-param edext.ws.url del web.xml.
 */
public final class ConfigWeb {

    public static final String NOMBRE_ARCHIVO = "config.properties";
    public static final String CLAVE_WS_URL = "ws.url";
    public static final String PROPIEDAD_RUTA = "edext.config";
    public static final String VARIABLE_RUTA = "EDEXT_CONFIG";
    public static final String CARPETA_EDEXT = ".edEXT";
    static final int NIVELES_HACIA_ARRIBA = 4;

    private ConfigWeb() {
    }

    /** Resultado de la búsqueda: la URL y de dónde salió. */
    public static final class Resultado {

        private final String url;
        private final String origen;

        Resultado(String url, String origen) {
            this.url = url;
            this.origen = origen;
        }

        public String getUrl() {
            return url;
        }

        public String getOrigen() {
            return origen;
        }
    }

    /**
     * @param carpetaApp   carpeta donde está desplegada la aplicación (getRealPath("/")), puede ser null
     * @param variableEntorno valor de EDEXT_CONFIG, puede ser null
     * @param urlWebXml    valor del context-param edext.ws.url, puede ser null
     */
    public static Resultado resolver(String carpetaApp, String variableEntorno, String urlWebXml) {
        for (File f : candidatos(carpetaApp, variableEntorno)) {
            if (f.isFile()) {
                String url = leerUrl(f);
                if (url != null) {
                    return new Resultado(url, f.getAbsolutePath());
                }
            }
        }
        if (urlWebXml != null && !urlWebXml.isBlank()) {
            return new Resultado(urlWebXml.trim(), "web.xml (context-param " + ClienteWS.PARAM_URL + ")");
        }
        return new Resultado(null, "ninguno: no se encontró " + NOMBRE_ARCHIVO + " ni " + ClienteWS.PARAM_URL
                + " en web.xml");
    }

    static List<File> candidatos(String carpetaApp, String variableEntorno) {
        Set<File> lista = new LinkedHashSet<>();
        String explicito = System.getProperty(PROPIEDAD_RUTA);
        if (explicito != null && !explicito.isBlank()) {
            lista.add(new File(explicito.trim()));
        }
        if (variableEntorno != null && !variableEntorno.isBlank()) {
            lista.add(new File(variableEntorno.trim()));
        }
        lista.add(archivoEdext());
        if (carpetaApp != null && !carpetaApp.isBlank()) {
            agregarSubiendo(lista, new File(carpetaApp), NIVELES_HACIA_ARRIBA);
        }
        String catalinaBase = System.getProperty("catalina.base");
        if (catalinaBase != null && !catalinaBase.isBlank()) {
            agregarSubiendo(lista, new File(catalinaBase), 1);
        }
        return new ArrayList<>(lista);
    }

    /** ~/.edEXT/config.properties */
    public static File archivoEdext() {
        return new File(new File(System.getProperty("user.home"), CARPETA_EDEXT), NOMBRE_ARCHIVO);
    }

    private static void agregarSubiendo(Set<File> lista, File desde, int niveles) {
        File dir = desde.getAbsoluteFile();
        for (int i = 0; dir != null && i <= niveles; i++) {
            lista.add(new File(dir, NOMBRE_ARCHIVO));
            dir = dir.getParentFile();
        }
    }

    private static String leerUrl(File f) {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(f)) {
            p.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("No se pudo leer " + f.getAbsolutePath() + ": " + e.getMessage());
            return null;
        }
        String url = p.getProperty(CLAVE_WS_URL);
        return (url == null || url.isBlank()) ? null : url.trim();
    }
}
