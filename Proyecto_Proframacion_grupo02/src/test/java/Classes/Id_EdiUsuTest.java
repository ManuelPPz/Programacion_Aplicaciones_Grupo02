package Classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Id_EdiUsuTest {

    @Test
    public void testConstructores() {
        Id_EdiUsu vacio = new Id_EdiUsu();
        assertNull(vacio.getMiUsuarioNickname());
        assertNull(vacio.getMiEdicionNombre());

        Id_EdiUsu id = new Id_EdiUsu("nick", "Ed1");
        assertEquals("nick", id.getMiUsuarioNickname());
        assertEquals("Ed1", id.getMiEdicionNombre());
    }

    @Test
    public void testSetters() {
        Id_EdiUsu id = new Id_EdiUsu();

        id.setMiUsuarioNickname("nick");
        id.setMiEdicionNombre("Ed1");

        assertEquals("nick", id.getMiUsuarioNickname());
        assertEquals("Ed1", id.getMiEdicionNombre());
    }

    @Test
    public void testEquals() {
        Id_EdiUsu a = new Id_EdiUsu("nick", "Ed1");

        assertTrue(a.equals(a));
        assertTrue(a.equals(new Id_EdiUsu("nick", "Ed1")));
        assertFalse(a.equals(new Id_EdiUsu("otro", "Ed1")));
        assertFalse(a.equals(new Id_EdiUsu("nick", "Ed2")));
        assertFalse(a.equals("x"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(new Id_EdiUsu("nick", "Ed1").hashCode(), new Id_EdiUsu("nick", "Ed1").hashCode());
    }
}
