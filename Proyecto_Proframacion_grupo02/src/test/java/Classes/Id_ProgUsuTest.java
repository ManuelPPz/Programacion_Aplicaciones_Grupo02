package Classes;

import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Id_ProgUsuTest {

    private Usuario usuario(String nick) {
        return new Usuario(nick, "n", "a", "c@c.com", "pw", new Date(0), "img");
    }

    private ProgramaDeFormacion programa(String nombre) {
        return new ProgramaDeFormacion(nombre, "d", null, new Date());
    }

    @Test
    public void testConstructores() {
        Id_ProgUsu vacio = new Id_ProgUsu();
        assertNull(vacio.getUsuario());
        assertNull(vacio.getPrograma());

        Usuario u = usuario("nick");
        ProgramaDeFormacion p = programa("P");
        Id_ProgUsu id = new Id_ProgUsu(u, p);
        assertSame(u, id.getUsuario());
        assertSame(p, id.getPrograma());
    }

    @Test
    public void testSetters() {
        Id_ProgUsu id = new Id_ProgUsu();
        Usuario u = usuario("nick");
        ProgramaDeFormacion p = programa("P");

        id.setUsuario(u);
        id.setEdicion(p);

        assertSame(u, id.getUsuario());
        assertSame(p, id.getPrograma());
    }

    @Test
    public void testEquals() {
        Id_ProgUsu a = new Id_ProgUsu(usuario("nick"), programa("P"));

        assertTrue(a.equals(a));
        assertTrue(a.equals(new Id_ProgUsu(usuario("nick"), programa("P"))));
        assertFalse(a.equals(new Id_ProgUsu(usuario("otro"), programa("P"))));
        assertFalse(a.equals(new Id_ProgUsu(usuario("nick"), programa("Q"))));
        assertFalse(a.equals("x"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(new Id_ProgUsu(usuario("nick"), programa("P")).hashCode(),
                new Id_ProgUsu(usuario("nick"), programa("P")).hashCode());
    }
}
