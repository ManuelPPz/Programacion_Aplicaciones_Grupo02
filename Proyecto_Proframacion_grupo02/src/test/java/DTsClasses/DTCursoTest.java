package DTsClasses;

import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTCursoTest {

    @Test
    public void testConstructorYGetters() {
        Date f = new Date(1234);
        List<String> previas = List.of("Base");
        List<String> ediciones = List.of("Ed1", "Ed2");
        List<String> programas = List.of("P1");
        List<String> categorias = List.of("Backend");

        DTCurso dt = new DTCurso("INCO", "Java", "desc", 10, 20.5f, 5, "http://url", f,
                previas, ediciones, programas, categorias, "profe");

        assertEquals("INCO", dt.getInstituto());
        assertEquals("Java", dt.getNombre());
        assertEquals("desc", dt.getDescripcion());
        assertEquals(10, dt.getDuracion());
        assertEquals(20.5f, dt.getCantHoras());
        assertEquals(5, dt.getCantCreditos());
        assertEquals("http://url", dt.getURL());
        assertSame(f, dt.getFechaAlta());
        assertSame(previas, dt.getPrevias());
        assertSame(ediciones, dt.getEdiCursos());
        assertSame(programas, dt.getProgFormacion());
        assertSame(categorias, dt.getCategorias());
        assertEquals("profe", dt.getDocente());
        assertTrue(dt instanceof DTMaster);
    }
}
