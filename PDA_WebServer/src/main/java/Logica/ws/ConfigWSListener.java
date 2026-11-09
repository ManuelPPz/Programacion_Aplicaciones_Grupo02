package Logica.ws;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Al desplegar la aplicación lee de web.xml la URL del Web Service del
 * Servidor Central (context-param "edext.ws.url").
 */
@WebListener
public class ConfigWSListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String url = sce.getServletContext().getInitParameter(ClienteWS.PARAM_URL);
        ClienteWS.configurar(url);
        sce.getServletContext().log("edEXT: Servidor Central en " + ClienteWS.getUrl());
    }
}
