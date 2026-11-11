package Logica.util;

import Logica.DTsClasses.DTActaEstudio;
import Logica.DTsClasses.DTFilaActa;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests de la generación del PDF del acta de estudio. */
public class ActaPdfTest {

    private static DTActaEstudio acta(List<DTFilaActa> filas) {
        return new DTActaEstudio("ana", "Ana", "Paz", "Programación Web", "INCO", new Date(), filas);
    }

    @Test
    public void testGeneraUnPdf() throws Exception {
        List<DTFilaActa> filas = List.of(
                new DTFilaActa("Java", "Java 2026", 9, "Aprobado", true, new Date()),
                new DTFilaActa("Web", "Web 2026", 3, "Reprobado", false, new Date()),
                new DTFilaActa("BD", null, null, "No cursado", false, null));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ActaPdf.escribir(acta(filas), "no es una imagen".getBytes(), out);

        assertTrue(out.size() > 1000);
        assertTrue(new String(out.toByteArray(), 0, 5, StandardCharsets.ISO_8859_1).startsWith("%PDF-"));
    }

    @Test
    public void testProgramaSinCursos() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ActaPdf.escribir(new DTActaEstudio("ana", "Ana", "Paz", "P", null, null, new ArrayList<>()), null, out);
        assertTrue(new String(out.toByteArray(), 0, 5, StandardCharsets.ISO_8859_1).startsWith("%PDF-"));
    }

    @Test
    public void testResumenYNombreDeArchivo() {
        DTActaEstudio a = acta(List.of(new DTFilaActa("Java", "J", 12, "Aprobado", true, new Date())));
        assertTrue(a.isProgramaAprobado());
        assertEquals(1, a.getCantAprobados());
        assertFalse(acta(new ArrayList<>()).isProgramaAprobado());
        assertEquals("Acta_Programacion_Web_ana.pdf", ActaPdf.nombreArchivo(a));
    }
}
