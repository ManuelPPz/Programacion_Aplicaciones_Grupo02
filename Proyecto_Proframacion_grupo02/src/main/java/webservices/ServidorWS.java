package webservices;

import jakarta.xml.ws.Endpoint;
import java.net.URI;
import java.util.Set;

/**
 * Publica el PublicadorWS usando el servidor HTTP incluido en la JDK
 * (Endpoint.publish). La URL viene del config.properties (clave ws.url).
 */
public final class ServidorWS {

    private static final Set<String> LOCALES = Set.of("localhost", "127.0.0.1", "0.0.0.0", "[::1]");

    private static Endpoint endpoint;

    private ServidorWS() {
    }

    /**
     * URL en la que realmente se escucha. ws.url es la dirección con la que los
     * clientes (servidor web, móvil) llegan al Servidor Central, por ejemplo
     * http://192.168.1.10:9128/edext; para que funcione aunque la PC tenga varias
     * interfaces de red se escucha en todas (0.0.0.0) con el mismo puerto y ruta.
     * Si la URL es local (localhost) se escucha solo en esta PC.
     */
    public static String urlDePublicacion(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Falta la URL del Web Service (ws.url)");
        }
        URI uri = URI.create(url.trim());
        if (!"http".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null || uri.getPort() == -1) {
            throw new IllegalArgumentException(
                    "ws.url debe tener la forma http://<ip o nombre>:<puerto>/<ruta> y se recibió: " + url);
        }
        String host = LOCALES.contains(uri.getHost().toLowerCase()) ? uri.getHost() : "0.0.0.0";
        String ruta = (uri.getRawPath() == null || uri.getRawPath().isEmpty()) ? "/" : uri.getRawPath();
        return "http://" + host + ":" + uri.getPort() + ruta;
    }

    /**
     * Publica el Web Service. Si ya había uno publicado, lo detiene antes.
     *
     * @return la URL del WSDL (con la dirección configurada en ws.url)
     */
    public static synchronized String publicar(String url, PublicadorWS servicio) {
        String escucha = urlDePublicacion(url);
        detener();
        endpoint = Endpoint.publish(escucha, servicio);
        return url.trim() + "?wsdl";
    }

    public static String publicar(String url) {
        return publicar(url, new PublicadorWS());
    }

    public static synchronized boolean estaPublicado() {
        return endpoint != null && endpoint.isPublished();
    }

    public static synchronized void detener() {
        if (endpoint != null) {
            endpoint.stop();
            endpoint = null;
        }
    }
}
