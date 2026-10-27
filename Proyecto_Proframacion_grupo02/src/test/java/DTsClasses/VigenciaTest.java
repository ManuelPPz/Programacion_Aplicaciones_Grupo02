package DTsClasses;

import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VigenciaTest {

    @Test
    public void testConstructorVacio() {
        Vigencia v = new Vigencia();

        assertNull(v.getFechaInicio());
        assertNull(v.getFechaFin());
    }

    @Test
    public void testConstructorCompleto() {
        Date inicio = new Date(1);
        Date fin = new Date(2);

        Vigencia v = new Vigencia(inicio, fin);

        assertSame(inicio, v.getFechaInicio());
        assertSame(fin, v.getFechaFin());
    }

    @Test
    public void testSetters() {
        Vigencia v = new Vigencia();
        Date inicio = new Date(10);
        Date fin = new Date(20);

        v.setFechaInicio(inicio);
        v.setFechaFin(fin);

        assertSame(inicio, v.getFechaInicio());
        assertSame(fin, v.getFechaFin());
    }
}
