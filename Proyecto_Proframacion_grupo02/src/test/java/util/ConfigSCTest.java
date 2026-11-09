package util;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigSCTest {

    @TempDir
    Path tmp;

    private String configAnterior;
    private String imagenesAnterior;
    private String homeAnterior;
    private String userDirAnterior;

    @BeforeEach
    public void setUp() {
        configAnterior = System.getProperty("edext.config");
        imagenesAnterior = System.getProperty("edext.imagenes.dir");
        homeAnterior = System.getProperty("user.home");
        userDirAnterior = System.getProperty("user.dir");
        // Aislar de archivos reales del usuario
        System.setProperty("user.home", tmp.resolve("home").toString());
        System.setProperty("user.dir", tmp.resolve("trabajo").toString());
        System.clearProperty("edext.config");
        System.clearProperty("edext.imagenes.dir");
    }

    @AfterEach
    public void tearDown() {
        restaurar("edext.config", configAnterior);
        restaurar("edext.imagenes.dir", imagenesAnterior);
        restaurar("user.home", homeAnterior);
        restaurar("user.dir", userDirAnterior);
    }

    private static void restaurar(String clave, String valor) {
        if (valor == null) {
            System.clearProperty(clave);
        } else {
            System.setProperty(clave, valor);
        }
    }

    private static ConfigSC de(String texto) throws Exception {
        return ConfigSC.desde(new ByteArrayInputStream(texto.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testValoresLeidos() throws Exception {
        ConfigSC c = de("ws.url= http://10.0.0.5:9128/edext \nedext.imagenes.dir=/datos/img\n");
        assertEquals("http://10.0.0.5:9128/edext", c.getWsUrl());
        assertEquals("/datos/img", c.getImagenesDir());
        assertEquals("stream", c.getOrigen());
    }

    @Test
    public void testValoresPorDefecto() throws Exception {
        ConfigSC c = de("ws.url=\nedext.imagenes.dir=  \n");
        assertEquals(ConfigSC.WS_URL_POR_DEFECTO, c.getWsUrl());
        assertNull(c.getImagenesDir());
    }

    @Test
    public void testSinArchivoExternoUsaElDelJar() {
        ConfigSC c = ConfigSC.cargar();
        assertEquals("config.properties del jar", c.getOrigen());
        assertEquals("http://localhost:9128/edext", c.getWsUrl());
        assertNull(c.getImagenesDir());
    }

    @Test
    public void testArchivoIndicadoConPropiedad() throws Exception {
        Path f = tmp.resolve("mi.properties");
        Files.writeString(f, "ws.url=http://192.168.1.20:8888/ws\n");
        System.setProperty("edext.config", f.toString());

        ConfigSC c = ConfigSC.cargar();
        assertEquals("http://192.168.1.20:8888/ws", c.getWsUrl());
        assertEquals(f.toFile().getAbsolutePath(), c.getOrigen());
    }

    @Test
    public void testArchivoEnCarpetaDeTrabajoYEnHome() throws Exception {
        Path home = Files.createDirectories(tmp.resolve("home").resolve(".edext"));
        Files.writeString(home.resolve("config.properties"), "ws.url=http://home:1/ws\n");
        assertEquals("http://home:1/ws", ConfigSC.cargar().getWsUrl());

        // La carpeta de trabajo tiene prioridad sobre ~/.edext
        Path trabajo = Files.createDirectories(tmp.resolve("trabajo"));
        Files.writeString(trabajo.resolve("config.properties"), "ws.url=http://trabajo:2/ws\n");
        assertEquals("http://trabajo:2/ws", ConfigSC.cargar().getWsUrl());
    }

    @Test
    public void testArchivoIlegibleSigueConElSiguiente() throws Exception {
        // Una carpeta con el nombre del archivo no es un archivo: se ignora
        System.setProperty("edext.config", " ");
        Files.createDirectories(tmp.resolve("trabajo").resolve("config.properties"));
        assertEquals("config.properties del jar", ConfigSC.cargar().getOrigen());
    }

    @Test
    public void testAplicarImagenesDir() throws Exception {
        de("edext.imagenes.dir=/a\n").aplicarImagenesDir();
        assertEquals("/a", System.getProperty("edext.imagenes.dir"));

        // Si ya venía con -D, no se pisa
        de("edext.imagenes.dir=/b\n").aplicarImagenesDir();
        assertEquals("/a", System.getProperty("edext.imagenes.dir"));

        // Sin valor en el archivo, no se toca
        System.clearProperty("edext.imagenes.dir");
        de("ws.url=x\n").aplicarImagenesDir();
        assertNull(System.getProperty("edext.imagenes.dir"));

        System.setProperty("edext.imagenes.dir", "  ");
        de("edext.imagenes.dir=/c\n").aplicarImagenesDir();
        assertEquals("/c", System.getProperty("edext.imagenes.dir"));
    }
}
