package DTsClasses;

import Classes.Enum_Estado_inscripcion;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTEdicionCursoTest {

    @Test
    public void testConstructorYGetters() {
        Date fi = new Date(1);
        Date ff = new Date(2);
        Date fa = new Date(3);
        List<String> docentes = List.of("profe");
        List<DTEdi_Usu> inscripciones = List.of(new DTEdi_Usu("nick", "Ed1", fa, Enum_Estado_inscripcion.INSCRIPTO));
        List<String> categorias = List.of("Backend");

        DTEdicionCurso dt = new DTEdicionCurso("INCO", "Java", "Ed1", fi, ff, 10, 7,
                docentes, fa, inscripciones, categorias);

        assertEquals("INCO", dt.getInstituto());
        assertEquals("Java", dt.getCurso());
        assertEquals("Ed1", dt.getNombre());
        assertSame(fi, dt.getFInicio());
        assertSame(ff, dt.getFFin());
        assertEquals(10, dt.getCupo());
        assertSame(docentes, dt.getDocentes());
        assertSame(fa, dt.getFechaAlta());
        assertSame(inscripciones, dt.getMisInscripciones());
        assertSame(categorias, dt.getMisCategorias());
        assertTrue(dt instanceof DTMaster);
    }
}
