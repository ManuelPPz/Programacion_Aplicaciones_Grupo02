package Logica.ws;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Al desplegar la aplicación configura la URL del Web Service del Servidor
 * Central: primero desde el config.properties compartido (ver ConfigWeb) y, si no
 * existe, desde el context-param "edext.ws.url" del web.xml.
 */
@WebListener
public class ConfigWSListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        ConfigWeb.Resultado r = ConfigWeb.resolver(ctx.getRealPath("/"),
                System.getenv(ConfigWeb.VARIABLE_RUTA), ctx.getInitParameter(ClienteWS.PARAM_URL));
        ClienteWS.configurar(r.getUrl());
        if (r.getUrl() != null) {
            ctx.log("edEXT: Servidor Central en " + r.getUrl() + " (configuración: " + r.getOrigen() + ")");
        } else {
            ctx.log("edEXT: ATENCIÓN, URL del Servidor Central sin configurar (" + r.getOrigen() + ")");
        }
    }
}
