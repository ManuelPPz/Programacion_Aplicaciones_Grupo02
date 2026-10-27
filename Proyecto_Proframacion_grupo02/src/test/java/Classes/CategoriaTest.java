package Classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriaTest {

    @Test
    public void testConstructores() {
        Categoria vacia = new Categoria();
        assertNull(vacia.getNombre());

        Categoria c = new Categoria("Backend");
        assertEquals("Backend", c.getNombre());
    }

    @Test
    public void testSetNombre() {
        Categoria c = new Categoria();
        c.setNombre("Frontend");
        assertEquals("Frontend", c.getNombre());
    }

    @Test
    public void testEquals() {
        Categoria a = new Categoria("A");
        assertTrue(a.equals(new Categoria("A")));
        assertFalse(a.equals(new Categoria("B")));
        assertFalse(a.equals(new Categoria()));
        assertFalse(new Categoria().equals(a));
        assertTrue(new Categoria().equals(new Categoria()));
        assertFalse(a.equals("A"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, new Categoria().hashCode());
        assertEquals(new Categoria("A").hashCode(), new Categoria("A").hashCode());
        assertEquals("A".hashCode(), new Categoria("A").hashCode());
    }

    @Test
    public void testToString() {
        assertTrue(new Categoria("A").toString().contains("A"));
    }
}
