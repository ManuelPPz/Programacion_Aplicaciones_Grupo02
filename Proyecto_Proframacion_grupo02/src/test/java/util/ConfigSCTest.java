package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    private String userDirAnterior;

    @BeforeEach
    public void setUp() throws Exception {
        configAnterior = System.getProperty(ConfigSC.PROPIEDAD_RUTA);
        imagenesAnterior = System.getProperty(ConfigSC.CLAVE_IMAGENES_DIR);
        userDirAnterior = System.getProperty("user.dir");
        // Aislar de archivos reales: la carpeta de trabajo es una carpeta vacía
        System.setProperty("user.dir", Files.createDirectories(tmp.resolve("otra/a/b/c/d/e")).toString());
        System.clearProperty(ConfigSC.PROPIEDAD_RUTA);
        System.clearProperty(ConfigSC.CLAVE_IMAGENES_DIR);
    }

    @AfterEach
    public void tearDown() {
        restaurar(ConfigSC.PROPIEDAD_RUTA, configAnterior);
        restaurar(ConfigSC.CLAVE_IMAGENES_DIR, imagenesAnterior);
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

    /** Estructura del repo: raiz/config.properties y raiz/Proyecto/target/sc.jar */
    private Path repoConConfig(String contenido) throws Exception {
        Path raiz = Files.createDirectories(tmp.resolve("repo"));
        Files.writeString(raiz.resolve("config.properties"), contenido);
        return Files.createDirectories(raiz.resolve("Proyecto_Proframacion_grupo02/target"));
    }

    @Test
    public void testValoresLeidos() throws Exception {
        ConfigSC c = de("ws.url= http://10.0.0.5:9128/edext \nedext.imagenes.dir=/datos/img\n");
        assertEquals("http://10.0.0.5:9128/edext", c.getWsUrl());
        assertEquals("/datos/img", c.getImagenesDir());
        assertFalse(c.encontrado());
        assertNull(c.getArchivo());
    }

    @Test
    public void testSinValoresNoHayDefaultsEnElCodigo() throws Exception {
        ConfigSC c = de("ws.url=\nedext.imagenes.dir=  \n");
        assertNull(c.getWsUrl());
        assertNull(c.getImagenesDir());
    }

    @Test
    public void testEncuentraElDeLaRaizDelRepoSubiendoDesdeElJar() throws Exception {
        Path target = repoConConfig("ws.url=http://raiz:1/ws\n");
        ConfigSC c = ConfigSC.cargar(null, target.toFile());
        assertTrue(c.encontrado());
        assertEquals("http://raiz:1/ws", c.getWsUrl());
        assertEquals(tmp.resolve("repo/config.properties").toFile().getAbsoluteFile(), c.getArchivo());

        // Ejecutando desde el IDE: target/classes (un nivel más abajo)
        File classes = Files.createDirectories(target.resolve("classes")).toFile();
        assertEquals("http://raiz:1/ws", ConfigSC.cargar(null, classes).getWsUrl());
    }

    @Test
    public void testElDeAlLadoDelJarTienePrioridad() throws Exception {
        Path target = repoConConfig("ws.url=http://raiz:1/ws\n");
        Files.writeString(target.resolve("config.properties"), "ws.url=http://junto:2/ws\n");
        assertEquals("http://junto:2/ws", ConfigSC.cargar(null, target.toFile()).getWsUrl());
    }

    @Test
    public void testPropiedadYVariableDeEntornoTienenPrioridad() throws Exception {
        Path target = repoConConfig("ws.url=http://raiz:1/ws\n");
        Path porVariable = tmp.resolve("variable.properties");
        Files.writeString(porVariable, "ws.url=http://variable:3/ws\n");
        assertEquals("http://variable:3/ws", ConfigSC.cargar(porVariable.toString(), target.toFile()).getWsUrl());

        Path porPropiedad = tmp.resolve("propiedad.properties");
        Files.writeString(porPropiedad, "ws.url=http://propiedad:4/ws\n");
        System.setProperty(ConfigSC.PROPIEDAD_RUTA, porPropiedad.toString());
        assertEquals("http://propiedad:4/ws", ConfigSC.cargar(porVariable.toString(), target.toFile()).getWsUrl());
    }

    @Test
    public void testCarpetaDeTrabajo() throws Exception {
        Path trabajo = Files.createDirectories(tmp.resolve("trabajo"));
        Files.writeString(trabajo.resolve("config.properties"), "ws.url=http://trabajo:5/ws\n");
        System.setProperty("user.dir", trabajo.toString());
        assertEquals("http://trabajo:5/ws", ConfigSC.cargar(" ", null).getWsUrl());
    }

    @Test
    public void testNoEncontradoYLimiteDeNiveles() throws Exception {
        Path raiz = Files.createDirectories(tmp.resolve("lejos"));
        Files.writeString(raiz.resolve("config.properties"), "ws.url=http://lejos:1/ws\n");
        File muyAbajo = Files.createDirectories(raiz.resolve("1/2/3/4/5")).toFile();
        ConfigSC c = ConfigSC.cargar(null, muyAbajo);
        assertFalse(c.encontrado());
        assertNull(c.getWsUrl());
    }

    @Test
    public void testArchivoQueEsCarpetaSeIgnora() throws Exception {
        Path target = repoConConfig("ws.url=http://raiz:1/ws\n");
        Files.createDirectories(target.resolve("config.properties")); // carpeta, no archivo
        assertEquals("http://raiz:1/ws", ConfigSC.cargar(null, target.toFile()).getWsUrl());
    }

    @Test
    public void testCandidatosYRutasBuscadas() {
        List<File> c = ConfigSC.candidatos(null, tmp.toFile());
        assertEquals(new File(tmp.toFile(), "config.properties"), c.get(0));
        assertTrue(c.size() > ConfigSC.NIVELES_HACIA_ARRIBA);
        assertTrue(ConfigSC.rutasBuscadas().contains("config.properties"));
        assertNotNull(ConfigSC.ubicacionDelPrograma());
    }

    @Test
    public void testAplicarImagenesDir() throws Exception {
        de("edext.imagenes.dir=/a\n").aplicarImagenesDir();
        assertEquals("/a", System.getProperty(ConfigSC.CLAVE_IMAGENES_DIR));

        // Si ya venía con -D, no se pisa
        de("edext.imagenes.dir=/b\n").aplicarImagenesDir();
        assertEquals("/a", System.getProperty(ConfigSC.CLAVE_IMAGENES_DIR));

        // Sin valor en el archivo, no se toca
        System.clearProperty(ConfigSC.CLAVE_IMAGENES_DIR);
        de("ws.url=x\n").aplicarImagenesDir();
        assertNull(System.getProperty(ConfigSC.CLAVE_IMAGENES_DIR));

        System.setProperty(ConfigSC.CLAVE_IMAGENES_DIR, "  ");
        de("edext.imagenes.dir=/c\n").aplicarImagenesDir();
        assertEquals("/c", System.getProperty(ConfigSC.CLAVE_IMAGENES_DIR));
    }
}
