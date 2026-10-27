package DTsClasses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTCategoriaTest {

    @Test
    public void testConstructorYGetter() {
        DTCategoria dt = new DTCategoria("Backend");

        assertEquals("Backend", dt.getNombre());
        assertTrue(dt instanceof DTMaster);
    }

    @Test
    public void testSetNombre() {
        DTCategoria dt = new DTCategoria("Backend");

        dt.setNombre("Frontend");

        assertEquals("Frontend", dt.getNombre());
    }
}
