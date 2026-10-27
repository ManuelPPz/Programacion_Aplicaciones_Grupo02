package Classes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CursoTest {

    private static void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    private Curso curso(String nombre) {
        Curso c = new Curso();
        c.setNombre(nombre);
        return c;
    }

    @Test
    public void testConstructorVacio() {
        Curso c = new Curso();

        assertNull(c.getNombre());
        assertNotNull(c.getPrevias());
        assertNotNull(c.getEdiciones());
        assertNotNull(c.getProgramas());
    }

    @Test
    public void testConstructorCompletoYGetters() {
        Instituto ins = new Instituto("INCO");
        Docente docente = new Docente();
        Date f = new Date();
        List<Curso> previas = new ArrayList<>();
        previas.add(curso("Base"));
        List<Categoria> cats = new ArrayList<>();
        cats.add(new Categoria("Backend"));

        Curso c = new Curso(ins, "Java", "desc", 10, 20.5f, 5, "http://url", f, previas, docente, cats);

        assertSame(ins, c.getInstituto());
        assertEquals("Java", c.getNombre());
        assertEquals("desc", c.getDescripcion());
        assertEquals(10, c.getDuracion());
        assertEquals(20.5f, c.getCantHoras());
        assertEquals(5, c.getCantCreditos());
        assertEquals("http://url", c.getURL());
        assertSame(f, c.getFAlta());
        assertSame(previas, c.getPrevias());
        assertSame(docente, c.getDocente());
        assertSame(docente, c.getMiDocente());
        assertSame(cats, c.getCategorias());
        assertTrue(c.getProgramas().isEmpty());
        assertTrue(c.getEdiciones().isEmpty());
    }

    @Test
    public void testConstructorConPreviasNulas() {
        Curso c = new Curso(null, "Java", "d", 1, 1f, 1, "u", new Date(), null, null, null);

        assertNotNull(c.getPrevias());
        assertTrue(c.getPrevias().isEmpty());
        assertNull(c.getDocente());
    }

    @Test
    public void testModificarMisDatos() {
        Curso c = new Curso(null, "Java", "d", 1, 1f, 1, "u", new Date(), null, null, null);
        Docente docente = new Docente();
        Date f = new Date(5000);
        List<Curso> previas = new ArrayList<>();
        List<Categoria> cats = new ArrayList<>();

        c.ModificarMisDatos("nueva", 20, 30f, 8, "nuevaUrl", f, previas, docente, cats);

        assertEquals("nueva", c.getDescripcion());
        assertEquals(20, c.getDuracion());
        assertEquals(30f, c.getCantHoras());
        assertEquals(8, c.getCantCreditos());
        assertEquals("nuevaUrl", c.getURL());
        assertSame(f, c.getFAlta());
        assertSame(previas, c.getPrevias());
        assertSame(docente, c.getDocente());
        assertSame(cats, c.getCategorias());
        assertEquals("Java", c.getNombre());
    }

    @Test
    public void testSetters() {
        Curso c = new Curso();

        c.setNombre("Java");
        c.setDuracion(7);
        c.setCantHoras(12.5f);
        c.setCantCreditos(4);

        assertEquals("Java", c.getNombre());
        assertEquals(7, c.getDuracion());
        assertEquals(12.5f, c.getCantHoras());
        assertEquals(4, c.getCantCreditos());
    }

    @Test
    public void testAddYRemoveEdicion() {
        Curso c = new Curso();
        EdicionCurso ec = new EdicionCurso();

        c.AddEdicion(ec);
        assertEquals(1, c.getEdiciones().size());

        c.RemoveEdicion(ec);
        assertTrue(c.getEdiciones().isEmpty());
    }

    @Test
    public void testAddYRemoveEdicionConListaNula() throws Exception {
        Curso c = new Curso();
        setField(c, "misEdiciones", null);

        c.RemoveEdicion(new EdicionCurso()); // no debe lanzar excepcion
        c.AddEdicion(new EdicionCurso());

        assertEquals(1, c.getEdiciones().size());
    }

    @Test
    public void testAddPrograma() {
        Curso c = new Curso();
        ProgramaDeFormacion p = new ProgramaDeFormacion("P", "d", null, new Date());

        c.AddPrograma(p);
        c.AddPrograma(p); // duplicado: no se agrega

        assertEquals(1, c.getProgramas().size());
    }

    @Test
    public void testAddYRemoveProgramaConListaNula() throws Exception {
        Curso c = new Curso();
        ProgramaDeFormacion p = new ProgramaDeFormacion("P", "d", null, new Date());
        setField(c, "misProgramas", null);

        c.RemovePrograma(p); // no debe lanzar excepcion
        c.AddPrograma(p);

        assertEquals(1, c.getProgramas().size());

        c.RemovePrograma(p);
        assertTrue(c.getProgramas().isEmpty());
    }

    @Test
    public void testEquals() {
        Curso a = curso("A");

        assertTrue(a.equals(curso("A")));
        assertFalse(a.equals(curso("B")));
        assertFalse(a.equals(new Curso()));
        assertFalse(new Curso().equals(a));
        assertTrue(new Curso().equals(new Curso()));
        assertFalse(a.equals("A"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, new Curso().hashCode());
        assertEquals("A".hashCode(), curso("A").hashCode());
    }

    @Test
    public void testToString() {
        assertTrue(curso("Java").toString().contains("Java"));
    }
}
