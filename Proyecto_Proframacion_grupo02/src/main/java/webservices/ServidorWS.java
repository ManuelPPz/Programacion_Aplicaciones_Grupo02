package webservices;

import jakarta.xml.ws.Endpoint;

/**
 * Publica el PublicadorWS usando el servidor HTTP incluido en la JDK
 * (Endpoint.publish). La URL viene del config.properties (clave ws.url).
 */
public final class ServidorWS {

    private static Endpoint endpoint;

    private ServidorWS() {
    }

    /**
     * Publica el Web Service en la URL indicada. Si ya había uno publicado,
     * lo detiene antes.
     *
     * @return la URL del WSDL
     */
    public static synchronized String publicar(String url, PublicadorWS servicio) {
        detener();
        endpoint = Endpoint.publish(url, servicio);
        return url + "?wsdl";
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
