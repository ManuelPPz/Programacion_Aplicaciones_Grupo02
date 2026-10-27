package DTsClasses;

import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTProgramaFormTest {

    @Test
    public void testConstructorYGetters() {
        Vigencia vigencia = new Vigencia(new Date(1), new Date(2));
        List<String> cursos = List.of("Java", "SQL");
        Date fa = new Date(3);
        List<String> categorias = List.of("Backend");

        DTProgramaForm dt = new DTProgramaForm("P1", "desc", vigencia, cursos, fa, categorias);

        assertEquals("P1", dt.getNombre());
        assertEquals("desc", dt.getDescripcion());
        assertSame(vigencia, dt.getVigenciaProg());
        assertSame(cursos, dt.getCursos());
        assertSame(fa, dt.getFechaAlta());
        assertSame(categorias, dt.getMisCategorias());
        assertTrue(dt instanceof DTMaster);
    }
}
