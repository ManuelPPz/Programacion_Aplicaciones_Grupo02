package Classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DocenteTest {

    private Docente crear(Instituto ins) {
        return new Docente("profe", "Nombre", "Apellido", "mail@mail.com", "pw", new Date(0), "img.png", ins);
    }

    @Test
    public void testConstructorVacio() {
        Docente d = new Docente();

        assertNull(d.getInstituto());
        assertNotNull(d.getCursos());
        assertTrue(d.getCursos().isEmpty());
        assertTrue(d.getEdiciones().isEmpty());
    }

    @Test
    public void testConstructorCompleto() {
        Instituto ins = new Instituto("INCO");
        Docente d = crear(ins);

        assertEquals("profe", d.getNickname());
        assertSame(ins, d.getInstituto());
        assertTrue(d.getCursos().isEmpty());
    }

    @Test
    public void testSetInstituto() {
        Docente d = new Docente();
        Instituto ins = new Instituto("FING");

        d.setInstituto(ins);

        assertSame(ins, d.getInstituto());
    }

    @Test
    public void testModificarMisDatos() {
        Docente d = crear(new Instituto("INCO"));
        Instituto nuevo = new Instituto("FING");
        Date f = new Date(9000);

        d.ModificarMisDatos("Otro", "Ape", "nuevaPw", f, "x.png", nuevo);

        assertEquals("Otro", d.getNombre());
        assertEquals("Ape", d.getApellido());
        assertEquals("nuevaPw", d.getPassword());
        assertSame(f, d.getFNac());
        assertEquals("x.png", d.getImage());
        assertSame(nuevo, d.getInstituto());
    }

    @Test
    public void testAddYRemoveCurso() {
        Docente d = new Docente();
        Curso c = new Curso();
        c.setNombre("Java");

        d.AddCurso(c);
        assertEquals(1, d.getCursos().size());
        assertSame(c, d.getCursos().get(0));

        d.RemoveCurso(c);
        assertTrue(d.getCursos().isEmpty());
    }

    @Test
    public void testAddYRemoveEdicion() {
        Docente d = new Docente();
        EdicionCurso ec = new EdicionCurso();
        ec.setNombre("Ed1");

        d.AddEdicion(ec);
        assertEquals(1, d.getEdiciones().size());
        assertEquals(1, d.getMisEdiciones().size());

        d.RemoveEdicion(ec);
        assertTrue(d.getEdiciones().isEmpty());
    }

    @Test
    public void testSetMisEdiciones() {
        Docente d = new Docente();
        List<EdicionCurso> lista = new ArrayList<>();
        lista.add(new EdicionCurso());

        d.setMisEdiciones(lista);

        assertSame(lista, d.getMisEdiciones());
        assertSame(lista, d.getEdiciones());
    }
}
