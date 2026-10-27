package Classes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProgramaDeFormacionTest {

    private static void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    private ProgramaDeFormacion programa(String nombre) {
        return new ProgramaDeFormacion(nombre, "desc", null, new Date());
    }

    private Curso curso(String nombre) {
        Curso c = new Curso();
        c.setNombre(nombre);
        return c;
    }

    @Test
    public void testConstructorVacio() {
        ProgramaDeFormacion p = new ProgramaDeFormacion();

        assertNull(p.getNombre());
        assertNull(p.getCursos());
    }

    @Test
    public void testConstructorCompletoYGetters() {
        Date f = new Date(1234);
        ProgramaDeFormacion p = new ProgramaDeFormacion("P", "desc", null, f);

        assertEquals("P", p.getNombre());
        assertEquals("desc", p.getDescripcion());
        assertNull(p.getVigencia());
        assertSame(f, p.getFAlta());
        assertNotNull(p.getCursos());
        assertTrue(p.getCursos().isEmpty());
    }

    @Test
    public void testModificarDatos() {
        ProgramaDeFormacion p = programa("P");
        Date f = new Date(9999);

        p.ModificarDatos("nueva", null, f);

        assertEquals("nueva", p.getDescripcion());
        assertNull(p.getVigencia());
        assertSame(f, p.getFAlta());
        assertEquals("P", p.getNombre());
    }

    @Test
    public void testSetId() {
        ProgramaDeFormacion p = new ProgramaDeFormacion();
        p.setId("Nuevo");
        assertEquals("Nuevo", p.getNombre());
    }

    // ---------- AddCurso ----------

    @Test
    public void testAddCursoNuloNoHaceNada() {
        ProgramaDeFormacion p = programa("P");

        p.AddCurso(null);

        assertTrue(p.getCursos().isEmpty());
    }

    @Test
    public void testAddCursoSincronizaAmbosLados() {
        ProgramaDeFormacion p = programa("P");
        Curso c = curso("Java");

        p.AddCurso(c);
        p.AddCurso(c); // duplicado

        assertEquals(1, p.getCursos().size());
        assertEquals(1, c.getProgramas().size());
        assertSame(p, c.getProgramas().get(0));
    }

    @Test
    public void testAddCursoConListaNula() {
        ProgramaDeFormacion p = new ProgramaDeFormacion();
        Curso c = curso("Java");

        p.AddCurso(c);

        assertEquals(1, p.getCursos().size());
    }

    @Test
    public void testAddCursoConListaPerezosaNoInicializada() throws Exception {
        ProgramaDeFormacion p = programa("P");
        ArrayList<Curso> perezosa = new ArrayList<Curso>() {
            @Override
            public boolean contains(Object o) {
                throw new LazyInitializationException("lazy");
            }
        };
        setField(p, "cursos", perezosa);
        Curso c = curso("Java");

        p.AddCurso(c);

        assertNotSame(perezosa, p.getCursos());
        assertEquals(1, p.getCursos().size());
        assertSame(c, p.getCursos().get(0));
    }

    // ---------- RemoveCurso ----------

    @Test
    public void testRemoveCurso() {
        ProgramaDeFormacion p = programa("P");
        Curso c = curso("Java");
        p.AddCurso(c);

        p.RemoveCurso(c);

        assertTrue(p.getCursos().isEmpty());
        assertTrue(c.getProgramas().isEmpty());
    }

    @Test
    public void testRemoveCursoNoExistente() {
        ProgramaDeFormacion p = programa("P");
        Curso c = curso("Java");

        p.RemoveCurso(c);
        p.RemoveCurso(null);

        assertTrue(p.getCursos().isEmpty());
    }

    @Test
    public void testRemoveCursoConListaNula() {
        ProgramaDeFormacion p = new ProgramaDeFormacion();

        p.RemoveCurso(curso("Java")); // no debe lanzar excepcion

        assertNull(p.getCursos());
    }

    @Test
    public void testAddUsuarioInscripto() throws Exception {
        ProgramaDeFormacion p = programa("P");

        p.AddUsuarioInscripto(new Prog_Usu());
        p.AddUsuarioInscripto(new Prog_Usu());

        Field f = ProgramaDeFormacion.class.getDeclaredField("misUsuarios");
        f.setAccessible(true);
        assertEquals(2, ((java.util.List<?>) f.get(p)).size());
    }

    // ---------- equals / hashCode / toString ----------

    @Test
    public void testEquals() {
        ProgramaDeFormacion a = programa("A");

        assertTrue(a.equals(programa("A")));
        assertFalse(a.equals(programa("B")));
        assertFalse(a.equals(new ProgramaDeFormacion()));
        assertFalse(new ProgramaDeFormacion().equals(a));
        assertTrue(new ProgramaDeFormacion().equals(new ProgramaDeFormacion()));
        assertFalse(a.equals("A"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, new ProgramaDeFormacion().hashCode());
        assertEquals("A".hashCode(), programa("A").hashCode());
    }

    @Test
    public void testToString() {
        assertTrue(programa("Prog1").toString().contains("Prog1"));
    }
}
