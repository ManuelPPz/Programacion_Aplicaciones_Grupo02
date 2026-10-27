package Classes;

import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioBaseTest {

    private UsuarioBase crear(String nick) {
        return new UsuarioBase(nick, "Nombre", "Apellido", "mail@mail.com", "pw", new Date(0), "img.png");
    }

    @Test
    public void testConstructorVacio() {
        UsuarioBase ub = new UsuarioBase();

        assertNull(ub.getNickname());
        assertNotNull(ub.getMisSeguidos());
        assertTrue(ub.getMisSeguidos().isEmpty());
    }

    @Test
    public void testConstructorCompletoYGetters() {
        Date f = new Date(0);
        UsuarioBase ub = new UsuarioBase("nick", "Nombre", "Apellido", "mail@mail.com", "pw", f, "img.png");

        assertEquals("nick", ub.getNickname());
        assertEquals("Nombre", ub.getNombre());
        assertEquals("Apellido", ub.getApellido());
        assertEquals("mail@mail.com", ub.getCorreo());
        assertEquals("pw", ub.getPassword());
        assertSame(f, ub.getFNac());
        assertEquals("img.png", ub.getImage());
    }

    @Test
    public void testModificarMisDatos() {
        UsuarioBase ub = crear("nick");
        Date f = new Date(1000);

        ub.ModificarMisDatos("Nuevo", "Apellido2", "nuevaPw", f, "otra.png");

        assertEquals("Nuevo", ub.getNombre());
        assertEquals("Apellido2", ub.getApellido());
        assertEquals("nuevaPw", ub.getPassword());
        assertSame(f, ub.getFNac());
        assertEquals("otra.png", ub.getImage());
        assertEquals("nick", ub.getNickname());
    }

    @Test
    public void testSeguirYDejarDeSeguir() {
        UsuarioBase a = crear("a");
        UsuarioBase b = crear("b");

        a.SeguirUsuario(b);
        assertEquals(1, a.getMisSeguidos().size());
        assertSame(b, a.getMisSeguidos().get(0));

        a.DejarDeSeguir(b);
        assertTrue(a.getMisSeguidos().isEmpty());
    }

    @Test
    public void testEquals() {
        UsuarioBase a = crear("a");

        assertTrue(a.equals(crear("a")));
        assertFalse(a.equals(crear("b")));
        assertFalse(a.equals(new UsuarioBase()));
        assertFalse(new UsuarioBase().equals(a));
        assertTrue(new UsuarioBase().equals(new UsuarioBase()));
        assertFalse(a.equals("a"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, new UsuarioBase().hashCode());
        assertEquals("a".hashCode(), crear("a").hashCode());
    }

    @Test
    public void testToString() {
        assertTrue(crear("nick").toString().contains("nick"));
    }
}
