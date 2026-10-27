package Classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstitutoTest {

    @Test
    public void testConstructores() {
        assertNull(new Instituto().getNombre());
        assertEquals("INCO", new Instituto("INCO").getNombre());
    }

    @Test
    public void testSetNombre() {
        Instituto i = new Instituto();
        i.setNombre("FING");
        assertEquals("FING", i.getNombre());
    }

    @Test
    public void testEquals() {
        Instituto a = new Instituto("A");
        assertTrue(a.equals(new Instituto("A")));
        assertFalse(a.equals(new Instituto("B")));
        assertFalse(a.equals(new Instituto()));
        assertFalse(new Instituto().equals(a));
        assertTrue(new Instituto().equals(new Instituto()));
        assertFalse(a.equals("A"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, new Instituto().hashCode());
        assertEquals("A".hashCode(), new Instituto("A").hashCode());
    }

    @Test
    public void testToString() {
        assertTrue(new Instituto("A").toString().contains("A"));
    }
}
