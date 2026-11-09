package Logica.ws;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigWebTest {

    @TempDir
    Path tmp;

    private String configAnterior;
    private String catalinaAnterior;

    @BeforeEach
    public void setUp() {
        configAnterior = System.getProperty(ConfigWeb.PROPIEDAD_RUTA);
        catalinaAnterior = System.getProperty("catalina.base");
        System.clearProperty(ConfigWeb.PROPIEDAD_RUTA);
        System.clearProperty("catalina.base");
    }

    @AfterEach
    public void tearDown() {
        restaurar(ConfigWeb.PROPIEDAD_RUTA, configAnterior);
        restaurar("catalina.base", catalinaAnterior);
    }

    private static void restaurar(String k, String v) {
        if (v == null) {
            System.clearProperty(k);
        } else {
            System.setProperty(k, v);
        }
    }

    /** Como despliega NetBeans: raiz/PDA_WebServer/target/PDA_WebServer-1.0-SNAPSHOT */
    private String appDesplegadaEnRepo(String contenidoRaiz) throws Exception {
        Path raiz = Files.createDirectories(tmp.resolve("repo"));
        if (contenidoRaiz != null) {
            Files.writeString(raiz.resolve("config.properties"), contenidoRaiz);
        }
        return Files.createDirectories(raiz.resolve("PDA_WebServer/target/PDA_WebServer-1.0-SNAPSHOT")).toString();
    }

    @Test
    public void testUsaElConfigDeLaRaizDelRepo() throws Exception {
        String app = appDesplegadaEnRepo("ws.url= http://192.168.1.10:9128/edext \n");
        ConfigWeb.Resultado r = ConfigWeb.resolver(app, null, "http://webxml:1/ws");
        assertEquals("http://192.168.1.10:9128/edext", r.getUrl());
        assertEquals(tmp.resolve("repo/config.properties").toFile().getAbsolutePath(), r.getOrigen());
    }

    @Test
    public void testSinArchivoUsaWebXml() throws Exception {
        String app = appDesplegadaEnRepo(null);
        ConfigWeb.Resultado r = ConfigWeb.resolver(app, null, " http://webxml:1/ws ");
        assertEquals("http://webxml:1/ws", r.getUrl());
        assertTrue(r.getOrigen().contains("web.xml"));
    }

    @Test
    public void testArchivoSinClaveUsaWebXml() throws Exception {
        String app = appDesplegadaEnRepo("edext.imagenes.dir=/x\nws.url=  \n");
        assertEquals("http://webxml:1/ws", ConfigWeb.resolver(app, null, "http://webxml:1/ws").getUrl());
    }

    @Test
    public void testSinNadaNoHayUrl() throws Exception {
        ConfigWeb.Resultado r = ConfigWeb.resolver(appDesplegadaEnRepo(null), null, " ");
        assertNull(r.getUrl());
        assertTrue(r.getOrigen().startsWith("ninguno"));
        assertNull(ConfigWeb.resolver(null, " ", null).getUrl());
    }

    @Test
    public void testCarpetaDeTomcatEnOtraPC() throws Exception {
        Path tomcat = Files.createDirectories(tmp.resolve("tomcat"));
        Files.writeString(tomcat.resolve("config.properties"), "ws.url=http://sc:9128/edext\n");
        String app = Files.createDirectories(tomcat.resolve("webapps/PDA_WebServer")).toString();
        // subiendo desde webapps/PDA_WebServer se llega a la carpeta de Tomcat
        assertEquals("http://sc:9128/edext", ConfigWeb.resolver(app, null, null).getUrl());
        // y también por catalina.base aunque la app esté desplegada en otro lado
        System.setProperty("catalina.base", tomcat.toString());
        assertEquals("http://sc:9128/edext", ConfigWeb.resolver(null, null, null).getUrl());
    }

    @Test
    public void testPrioridadPropiedadYVariable() throws Exception {
        String app = appDesplegadaEnRepo("ws.url=http://raiz:1/ws\n");
        Path var = tmp.resolve("var.properties");
        Files.writeString(var, "ws.url=http://variable:2/ws\n");
        assertEquals("http://variable:2/ws", ConfigWeb.resolver(app, var.toString(), null).getUrl());
        Path prop = tmp.resolve("prop.properties");
        Files.writeString(prop, "ws.url=http://propiedad:3/ws\n");
        System.setProperty(ConfigWeb.PROPIEDAD_RUTA, prop.toString());
        assertEquals("http://propiedad:3/ws", ConfigWeb.resolver(app, var.toString(), null).getUrl());
    }

    @Test
    public void testCandidatosYCarpetaEnLugarDeArchivo() throws Exception {
        String app = appDesplegadaEnRepo("ws.url=http://raiz:1/ws\n");
        Files.createDirectories(Path.of(app, "config.properties")); // carpeta: se ignora
        assertEquals("http://raiz:1/ws", ConfigWeb.resolver(app, null, null).getUrl());
        assertEquals(new File(app, "config.properties").getAbsoluteFile(), ConfigWeb.candidatos(app, null).get(0));
    }
}
