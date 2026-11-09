package Logica.ws;

import Logica.ws.cliente.PublicadorService;
import Logica.ws.cliente.PublicadorWS;
import jakarta.xml.ws.BindingProvider;
import java.util.Map;

/**
 * Acceso al Web Service del Servidor Central.
 *
 * La URL del servicio se configura en web.xml (context-param "edext.ws.url");
 * ConfigWSListener la carga al arrancar la aplicación. El WSDL se toma de la
 * copia incluida en el WAR (/wsdl/PublicadorService.wsdl), así que no hace falta
 * que el Servidor Central esté levantado para desplegar el web.
 */
public final class ClienteWS {

    public static final String PARAM_URL = "edext.ws.url";
    public static final String URL_POR_DEFECTO = "http://localhost:9128/edext";

    /** Tiempo máximo para conectar / esperar respuesta del Servidor Central (ms). */
    static final int TIMEOUT_CONEXION_MS = 5000;
    static final int TIMEOUT_RESPUESTA_MS = 60000;

    private static volatile String url = URL_POR_DEFECTO;
    private static PublicadorService servicio;

    /** Un proxy por hilo: los proxies de JAX-WS no son seguros para uso concurrente. */
    private static final ThreadLocal<PublicadorWS> PUERTOS = new ThreadLocal<>();
    private static volatile int version = 0;
    private static final ThreadLocal<Integer> VERSION_PUERTO = new ThreadLocal<>();

    private ClienteWS() {
    }

    /** Cambia la URL del Servidor Central (se aplica a los próximos pedidos). */
    public static synchronized void configurar(String nuevaUrl) {
        url = (nuevaUrl == null || nuevaUrl.isBlank()) ? URL_POR_DEFECTO : nuevaUrl.trim();
        version++;
    }

    public static String getUrl() {
        return url;
    }

    private static synchronized PublicadorService getServicio() {
        if (servicio == null) {
            servicio = new PublicadorService();
        }
        return servicio;
    }

    /** Proxy del Web Service para el hilo actual, apuntando a la URL configurada. */
    public static PublicadorWS getPuerto() {
        PublicadorWS puerto = PUERTOS.get();
        Integer v = VERSION_PUERTO.get();
        if (puerto == null || v == null || v != version) {
            puerto = getServicio().getPublicadorPort();
            Map<String, Object> ctx = ((BindingProvider) puerto).getRequestContext();
            ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
            ctx.put("com.sun.xml.ws.connect.timeout", TIMEOUT_CONEXION_MS);
            ctx.put("com.sun.xml.ws.request.timeout", TIMEOUT_RESPUESTA_MS);
            PUERTOS.set(puerto);
            VERSION_PUERTO.set(version);
        }
        return puerto;
    }
}
