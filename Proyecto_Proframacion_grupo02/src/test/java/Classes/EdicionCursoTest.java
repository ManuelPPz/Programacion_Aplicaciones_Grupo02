package Classes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EdicionCursoTest {

    private static void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    private EdicionCurso crear(int cupo, List<Docente> docentes) {
        return new EdicionCurso("Ed1", new Instituto("INCO"), new Curso(), new Date(1), new Date(2),
                cupo, new Date(3), docentes);
    }

    @Test
    public void testConstructorVacio() {
        EdicionCurso ec = new EdicionCurso();

        assertNull(ec.getNombre());
        assertNotNull(ec.getMisInscripciones());
        assertTrue(ec.getMisInscripciones().isEmpty());
    }

    @Test
    public void testConstructorCompletoYGetters() {
        Instituto ins = new Instituto("INCO");
        Curso curso = new Curso();
        Date fi = new Date(1);
        Date ff = new Date(2);
        Date fa = new Date(3);
        List<Docente> docentes = new ArrayList<>();

        EdicionCurso ec = new EdicionCurso("Ed1", ins, curso, fi, ff, 10, fa, docentes);

        assertEquals("Ed1", ec.getNombre());
        assertSame(ins, ec.getInstituto());
        assertSame(curso, ec.getCurso());
        assertSame(fi, ec.getFInicio());
        assertSame(ff, ec.getFFin());
        assertEquals(10, ec.getCupo());
        assertEquals(10, ec.getCupoActual());
        assertSame(fa, ec.getFAlta());
        assertSame(docentes, ec.getMisDocentes());
        assertTrue(ec.getMisInscripciones().isEmpty());
    }

    @Test
    public void testModificarDatos() {
        EdicionCurso ec = crear(10, new ArrayList<>());
        Date fi = new Date(10);
        Date ff = new Date(20);
        Date fa = new Date(30);
        List<Docente> nuevos = new ArrayList<>();

        ec.ModificarDatos(fi, ff, 25, fa, nuevos);

        assertSame(fi, ec.getFInicio());
        assertSame(ff, ec.getFFin());
        assertEquals(25, ec.getCupo());
        assertSame(fa, ec.getFAlta());
        assertSame(nuevos, ec.getMisDocentes());
    }

    @Test
    public void testAddUsuarioInscripto() {
        EdicionCurso ec = crear(10, new ArrayList<>());
        Edi_Usu eu = new Edi_Usu();

        ec.AddUsuarioInscripto(eu);

        assertEquals(1, ec.getMisInscripciones().size());
        assertSame(eu, ec.getMisInscripciones().get(0));
    }

    @Test
    public void testAddUsuariosConListaNula() {
        EdicionCurso ec = crear(10, null);
        Docente d = new Docente();

        ec.AddUsuarios(d);

        assertEquals(1, ec.getMisDocentes().size());
        assertSame(d, ec.getMisDocentes().get(0));
    }

    @Test
    public void testAddUsuariosConListaExistente() {
        EdicionCurso ec = crear(10, new ArrayList<>());

        ec.AddUsuarios(new Docente());
        ec.AddUsuarios(new Docente());

        assertEquals(2, ec.getMisDocentes().size());
    }

    @Test
    public void testSetNombre() {
        EdicionCurso ec = new EdicionCurso();
        ec.setNombre("Nueva");
        assertEquals("Nueva", ec.getNombre());
    }

    // ---------- getCupoActual ----------

    @Test
    public void testCupoActualInicialEsElCupo() {
        assertEquals(10, crear(10, new ArrayList<>()).getCupoActual());
    }

    @Test
    public void testCupoActualCeroSinInscripcionesDevuelveCupo() {
        EdicionCurso ec = crear(10, new ArrayList<>());
        ec.setCupoActual(0);

        assertEquals(10, ec.getCupoActual());
    }

    @Test
    public void testCupoActualCeroConListaNulaDevuelveCupo() throws Exception {
        EdicionCurso ec = crear(10, new ArrayList<>());
        ec.setCupoActual(0);
        setField(ec, "misUsuarios", null);

        assertEquals(10, ec.getCupoActual());
    }

    @Test
    public void testCupoActualCeroConInscripcionesDevuelveCero() {
        EdicionCurso ec = crear(10, new ArrayList<>());
        ec.setCupoActual(0);
        ec.AddUsuarioInscripto(new Edi_Usu());

        assertEquals(0, ec.getCupoActual());
    }

    @Test
    public void testCupoActualCeroConCupoCeroDevuelveCero() {
        EdicionCurso ec = crear(0, new ArrayList<>());

        assertEquals(0, ec.getCupoActual());
    }

    @Test
    public void testCupoActualPositivo() {
        EdicionCurso ec = crear(10, new ArrayList<>());
        ec.setCupoActual(4);

        assertEquals(4, ec.getCupoActual());
    }

    // ---------- equals / hashCode / toString ----------

    private EdicionCurso conNombre(String nombre) {
        EdicionCurso ec = new EdicionCurso();
        ec.setNombre(nombre);
        return ec;
    }

    @Test
    public void testEquals() {
        EdicionCurso a = conNombre("A");

        assertTrue(a.equals(conNombre("A")));
        assertFalse(a.equals(conNombre("B")));
        assertFalse(a.equals(new EdicionCurso()));
        assertFalse(new EdicionCurso().equals(a));
        assertTrue(new EdicionCurso().equals(new EdicionCurso()));
        assertFalse(a.equals("A"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, new EdicionCurso().hashCode());
        assertEquals("A".hashCode(), conNombre("A").hashCode());
    }

    @Test
    public void testToString() {
        assertTrue(conNombre("Ed1").toString().contains("Ed1"));
    }
}
