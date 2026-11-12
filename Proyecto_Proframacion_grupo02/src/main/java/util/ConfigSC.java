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
 * config.properties (no hay URLs ni datos de conexión fijos en el código).
 *
 * Dónde se busca (gana el primero que exista):
 *   1. la ruta indicada con -Dedext.config=/ruta/config.properties
 *   2. la ruta de la variable de entorno EDEXT_CONFIG
 *   3. ~/.edEXT/config.properties (Requerimiento 7.8: la ubicación normal)
 *   4. la carpeta del .jar (o de las clases compiladas) y las carpetas de arriba,
 *      hasta 4 niveles (por ejemplo el config.properties de la raíz del repo)
 *   5. la carpeta desde donde se ejecuta el programa y las de arriba
 * Si no hay ninguno, {@link #cargarOCrear()} crea ~/.edEXT/config.properties con
 * los valores por defecto.
 *
 * Claves:
 *   ws.url                        URL donde se publica el Web Service (obligatoria)
 *   db.url, db.usuario, db.password conexión a MySQL (si faltan, se usa persistence.xml)
 *   edext.imagenes.dir            carpeta base de las imágenes (opcional)
 */
public final class ConfigSC {

    public static final String CLAVE_WS_URL = "ws.url";
    public static final String CLAVE_IMAGENES_DIR = "edext.imagenes.dir";
    public static final String NOMBRE_ARCHIVO = "config.properties";
    public static final String PROPIEDAD_RUTA = "edext.config";
    public static final String VARIABLE_RUTA = "EDEXT_CONFIG";
    public static final String CARPETA_EDEXT = ".edEXT";
    public static final String CLAVE_DB_URL = "db.url";
    public static final String CLAVE_DB_USUARIO = "db.usuario";
    public static final String CLAVE_DB_PASSWORD = "db.password";
    /** Plantilla (en el .jar) con la que se crea el archivo si no existe. */
    static final String PLANTILLA = "/config-default.properties";

    private static ConfigSC actual;
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

    /**
     * Busca el config.properties y, si no existe en ningún lado, crea
     * ~/.edEXT/config.properties a partir de la plantilla y lo lee.
     */
    public static ConfigSC cargarOCrear() {
        return cargarOCrear(System.getenv(VARIABLE_RUTA), ubicacionDelPrograma());
    }

    /** Versión testeable de {@link #cargarOCrear()}. */
    static ConfigSC cargarOCrear(String variableEntorno, File carpetaPrograma) {
        ConfigSC c = cargar(variableEntorno, carpetaPrograma);
        if (!c.encontrado()) {
            File destino = archivoEdext();
            try {
                crearDesdePlantilla(destino);
                System.out.println("Se creó la configuración por defecto en " + destino.getAbsolutePath());
                c = cargar(variableEntorno, carpetaPrograma);
            } catch (IOException e) {
                System.err.println("No se pudo crear " + destino.getAbsolutePath() + ": " + e.getMessage());
            }
        }
        return c;
    }

    /** Copia la plantilla de configuración a {@code destino} (creando la carpeta). */
    static void crearDesdePlantilla(File destino) throws IOException {
        File carpeta = destino.getAbsoluteFile().getParentFile();
        if (carpeta != null && !carpeta.isDirectory() && !carpeta.mkdirs()) {
            throw new IOException("no se pudo crear la carpeta " + carpeta.getAbsolutePath());
        }
        try (InputStream in = ConfigSC.class.getResourceAsStream(PLANTILLA)) {
            if (in == null) {
                throw new IOException("falta la plantilla " + PLANTILLA);
            }
            java.nio.file.Files.copy(in, destino.toPath());
        }
    }

    /** Configuración en uso (la carga la primera vez que se pide). */
    public static synchronized ConfigSC actual() {
        if (actual == null) {
            actual = cargar();
        }
        return actual;
    }

    /** Fija la configuración en uso (la llama el main al arrancar). */
    public static synchronized void usar(ConfigSC c) {
        actual = c;
    }

    /** Carpeta ~/.edEXT del usuario que ejecuta el programa. */
    public static File carpetaEdext() {
        return new File(System.getProperty("user.home"), CARPETA_EDEXT);
    }

    /** ~/.edEXT/config.properties */
    public static File archivoEdext() {
        return new File(carpetaEdext(), NOMBRE_ARCHIVO);
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
        lista.add(archivoEdext());
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
     * Datos de conexión a la base para pasarle a JPA; solo incluye las claves
     * presentes, el resto sale de persistence.xml.
     */
    public java.util.Map<String, String> propiedadesJPA() {
        java.util.Map<String, String> m = new java.util.HashMap<>();
        poner(m, "jakarta.persistence.jdbc.url", CLAVE_DB_URL);
        poner(m, "jakarta.persistence.jdbc.user", CLAVE_DB_USUARIO);
        poner(m, "jakarta.persistence.jdbc.password", CLAVE_DB_PASSWORD);
        return m;
    }

    private void poner(java.util.Map<String, String> m, String claveJpa, String clave) {
        String v = props.getProperty(clave);
        if (v != null && !v.isBlank()) {
            m.put(claveJpa, v.trim());
        }
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
