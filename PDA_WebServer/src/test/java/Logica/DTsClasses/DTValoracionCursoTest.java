package Logica.DTsClasses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests de los cálculos de presentación del resumen de valoraciones (Requerimiento 7.6). */
public class DTValoracionCursoTest {

    @Test
    public void testCantidadesYPorcentajes() {
        DTValoracionCurso v = new DTValoracionCurso("Java", 4, new int[]{1, 0, 0, 1, 2}, 3.75f, 0);
        assertEquals(1, v.cantidad(1));
        assertEquals(2, v.cantidad(5));
        assertEquals(0, v.cantidad(0));
        assertEquals(0, v.cantidad(6));
        assertEquals(50, v.porcentaje(5));
        assertEquals(25, v.porcentaje(1));
        assertFalse(v.isValorado());
        assertEquals("3,8", v.getPromedioTexto());

        int[][] filas = v.getFilas();
        assertEquals(5, filas.length);
        assertArrayEquals(new int[]{5, 2, 50}, filas[0]);
        assertArrayEquals(new int[]{1, 1, 25}, filas[4]);
    }

    @Test
    public void testSinValoraciones() {
        DTValoracionCurso v = new DTValoracionCurso("Java", 0, null, 0f, 0);
        assertEquals("-", v.getPromedioTexto());
        assertEquals(0, v.porcentaje(3));
        assertArrayEquals(new String[]{"empty", "empty", "empty", "empty", "empty"}, v.getIconos());
    }

    @Test
    public void testIconosRedondeaALaMediaEstrella() {
        assertArrayEquals(new String[]{"full", "full", "full", "empty", "empty"}, DTValoracionCurso.iconos(3.2f));
        assertArrayEquals(new String[]{"full", "full", "full", "half", "empty"}, DTValoracionCurso.iconos(3.3f));
        assertArrayEquals(new String[]{"full", "full", "full", "full", "full"}, DTValoracionCurso.iconos(5f));
        DTValoracionCurso v = new DTValoracionCurso("Java", 1, new int[]{0, 0, 0, 0, 1, 9}, 5f, 5);
        assertTrue(v.isValorado());
        assertEquals(5, v.getMiPuntaje());
        assertEquals(1, v.cantidad(5));
    }
}
