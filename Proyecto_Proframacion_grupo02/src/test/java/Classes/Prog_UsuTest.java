package Classes;

import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Prog_UsuTest {

    private Id_ProgUsu id(String nick, String programa) {
        return new Id_ProgUsu(
                new Usuario(nick, "n", "a", "c@c.com", "pw", new Date(0), "img"),
                new ProgramaDeFormacion(programa, "d", null, new Date()));
    }

    @Test
    public void testConstructores() {
        Prog_Usu vacio = new Prog_Usu();
        assertNull(vacio.getId());
        assertNull(vacio.getFIns());

        Id_ProgUsu id = id("nick", "P");
        Date f = new Date(1234);
        Prog_Usu pu = new Prog_Usu(id, f);
        assertSame(id, pu.getId());
        assertSame(f, pu.getFIns());
    }

    @Test
    public void testSetters() {
        Prog_Usu pu = new Prog_Usu();
        Id_ProgUsu id = id("nick", "P");
        Date f = new Date(99);

        pu.setId(id);
        pu.setFIns(f);

        assertSame(id, pu.getId());
        assertSame(f, pu.getFIns());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, new Prog_Usu().hashCode());

        Id_ProgUsu id = id("nick", "P");
        assertEquals(id.hashCode(), new Prog_Usu(id, new Date()).hashCode());
    }

    @Test
    public void testEqualsConObjetosDeOtroTipo() {
        Prog_Usu pu = new Prog_Usu(id("nick", "P"), new Date());

        assertFalse(pu.equals("x"));
        assertFalse(pu.equals(null));
    }

    @Test
    public void testToString() {
        assertTrue(new Prog_Usu(id("nick", "P"), new Date()).toString().contains("id="));
    }
}
