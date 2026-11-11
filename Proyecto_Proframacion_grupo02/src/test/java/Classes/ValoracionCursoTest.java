package Classes;

import DTsClasses.DTValoracionCurso;
import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests de ValoracionCurso, su clave Id_Valoracion y DTValoracionCurso. */
public class ValoracionCursoTest {

    private final Date fecha = new Date();

    private Curso curso(String nombre) {
        return new Curso(null, nombre, "d", 1, 1f, 1, "u", fecha, new ArrayList<>(), null, new ArrayList<>(), null);
    }

    @Test
    public void testConstructorYGetters() {
        Usuario u = new Usuario("ana", "Ana", "Paz", "a@x.com", "p", fecha, null);
        Curso c = curso("Java");
        ValoracionCurso v = new ValoracionCurso(u, c, 4);
        assertSame(u, v.getUsuario());
        assertSame(c, v.getCurso());
        assertEquals(4, v.getPuntaje());
        assertEquals("ana", v.getId().getMiUsuarioNickname());
        assertEquals("Java", v.getId().getMiCursoNombre());
    }

    @Test
    public void testEqualsYHashCode() {
        Usuario u = new Usuario("ana", "Ana", "Paz", "a@x.com", "p", fecha, null);
        ValoracionCurso a = new ValoracionCurso(u, curso("Java"), 4);
        ValoracionCurso b = new ValoracionCurso(u, curso("Java"), 2);
        ValoracionCurso otra = new ValoracionCurso(u, curso("Web"), 4);
        ValoracionCurso vacia = new ValoracionCurso();
        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, otra);
        assertNotEquals(a, "Java");
        assertNotEquals(vacia, a);
        assertEquals(0, vacia.hashCode());
        assertNull(vacia.getId());
    }

    @Test
    public void testIdValoracion() {
        Id_Valoracion id = new Id_Valoracion("ana", "Java");
        assertEquals(id, id);
        assertEquals(id, new Id_Valoracion("ana", "Java"));
        assertEquals(id.hashCode(), new Id_Valoracion("ana", "Java").hashCode());
        assertNotEquals(id, new Id_Valoracion("ana", "Web"));
        assertNotEquals(id, new Id_Valoracion("beto", "Java"));
        assertNotEquals(id, "ana");
        assertNull(new Id_Valoracion().getMiCursoNombre());
    }

    @Test
    public void testDTValoracionCurso() {
        int[] estrellas = {1, 0, 0, 0, 1};
        DTValoracionCurso dt = new DTValoracionCurso("Java", estrellas, 5);
        estrellas[0] = 99; // se copia el arreglo
        assertEquals(2, dt.getTotal());
        assertEquals(3f, dt.getPromedio());
        assertEquals(5, dt.getMiPuntaje());
        assertEquals("Java", dt.getCurso());
        dt.getEstrellas()[0] = 50;
        assertArrayEquals(new int[]{1, 0, 0, 0, 1}, dt.getEstrellas());

        DTValoracionCurso vacio = new DTValoracionCurso("X", null, 0);
        assertEquals(0, vacio.getTotal());
        assertEquals(0f, vacio.getPromedio());
        assertArrayEquals(new int[5], vacio.getEstrellas());
    }
}
