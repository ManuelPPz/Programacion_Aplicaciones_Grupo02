package util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de ImagenUtil sobre una carpeta temporal (no toca ~/mi_proyecto_data).
 */
public class ImagenUtilTest {

    @TempDir
    Path tmp;

    private String propiedadAnterior;

    @BeforeEach
    public void setUp() {
        propiedadAnterior = System.getProperty("edext.imagenes.dir");
        System.setProperty("edext.imagenes.dir", tmp.toString());
    }

    @AfterEach
    public void tearDown() {
        if (propiedadAnterior == null) {
            System.clearProperty("edext.imagenes.dir");
        } else {
            System.setProperty("edext.imagenes.dir", propiedadAnterior);
        }
    }

    @Test
    public void testBaseDirPorPropiedadYPorDefecto() {
        assertEquals(tmp.toFile(), ImagenUtil.getBaseDir());
        System.setProperty("edext.imagenes.dir", "  ");
        assertEquals(new File(System.getProperty("user.home"), "mi_proyecto_data"), ImagenUtil.getBaseDir());
    }

    @Test
    public void testGetDirectorioLoCrea() {
        File dir = ImagenUtil.getDirectorio(ImagenUtil.CARPETA_CURSOS);
        assertTrue(dir.isDirectory());
        assertEquals(tmp.resolve("uploads").resolve("cursos").toFile(), dir);
    }

    @Test
    public void testGuardarLeerYBorrarBytes() {
        byte[] datos = {1, 2, 3};
        String nombre = assertDoesNotThrow(() ->
                ImagenUtil.guardar(datos, "C:/fotos/Mi Foto.PNG", "Juan Pérez", ImagenUtil.CARPETA_PERFILES));

        assertTrue(nombre.matches("juan_p_rez_\\d+\\.png"), nombre);
        assertArrayEquals(datos, ImagenUtil.leer(ImagenUtil.CARPETA_PERFILES, nombre));
        assertNotNull(ImagenUtil.getArchivo(ImagenUtil.CARPETA_PERFILES, nombre));

        ImagenUtil.borrar(ImagenUtil.CARPETA_PERFILES, nombre);
        assertNull(ImagenUtil.leer(ImagenUtil.CARPETA_PERFILES, nombre));
    }

    @Test
    public void testGuardarSinDatosNiNombres() throws Exception {
        assertEquals("", ImagenUtil.guardar((byte[]) null, "a.png", "x", ImagenUtil.CARPETA_PERFILES));
        assertEquals("", ImagenUtil.guardar(new byte[0], "a.png", "x", ImagenUtil.CARPETA_PERFILES));

        String sinExtension = ImagenUtil.guardar(new byte[]{9}, null, null, ImagenUtil.CARPETA_PROGRAMAS);
        assertTrue(sinExtension.matches("img_\\d+"), sinExtension);
        String sinPunto = ImagenUtil.guardar(new byte[]{9}, "archivo", " ", ImagenUtil.CARPETA_PROGRAMAS);
        assertTrue(sinPunto.matches("img_\\d+"), sinPunto);
    }

    @Test
    public void testGuardarDesdeArchivo() throws Exception {
        Path origen = tmp.resolve("origen.jpg");
        Files.write(origen, new byte[]{7, 7});

        String nombre = ImagenUtil.guardar(origen.toFile(), "curso", ImagenUtil.CARPETA_EDICIONCURSOS);
        assertTrue(nombre.endsWith(".jpg"));
        assertArrayEquals(new byte[]{7, 7}, ImagenUtil.leer(ImagenUtil.CARPETA_EDICIONCURSOS, nombre));

        assertEquals("", ImagenUtil.guardar((File) null, "x", ImagenUtil.CARPETA_CURSOS));
        assertEquals("", ImagenUtil.guardar(tmp.resolve("noExiste.png").toFile(), "x", ImagenUtil.CARPETA_CURSOS));
    }

    @Test
    public void testNombresInvalidosOInexistentes() throws Exception {
        assertNull(ImagenUtil.getArchivo(ImagenUtil.CARPETA_PERFILES, null));
        assertNull(ImagenUtil.getArchivo(ImagenUtil.CARPETA_PERFILES, " "));
        assertNull(ImagenUtil.leer(ImagenUtil.CARPETA_PERFILES, "noExiste.png"));
        ImagenUtil.borrar(ImagenUtil.CARPETA_PERFILES, null); // no falla

        // No permite salir de la carpeta de imágenes con "../"
        Files.write(tmp.resolve("secreto.txt"), new byte[]{1});
        assertNull(ImagenUtil.getArchivo(ImagenUtil.CARPETA_PERFILES, "../../secreto.txt"));
    }

    @Test
    public void testLeerDirectorioDevuelveNull() {
        // Un directorio con el nombre de la imagen no se considera archivo
        new File(ImagenUtil.getDirectorio(ImagenUtil.CARPETA_CURSOS), "carpeta.png").mkdirs();
        assertNull(ImagenUtil.leer(ImagenUtil.CARPETA_CURSOS, "carpeta.png"));
    }
}
