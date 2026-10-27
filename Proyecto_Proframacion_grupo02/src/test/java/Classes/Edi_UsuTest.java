package Classes;

import DTsClasses.DTEdi_Usu;
import DTsClasses.DTMaster;
import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Edi_UsuTest {

    private Usuario usuario(String nick) {
        return new Usuario(nick, "n", "a", "c@c.com", "pw", new Date(0), "img");
    }

    private EdicionCurso edicion(String nombre) {
        return new EdicionCurso(nombre, null, null, new Date(), new Date(), 5, new Date(), new ArrayList<>());
    }

    @Test
    public void testConstructorVacio() {
        Edi_Usu eu = new Edi_Usu();

        assertNull(eu.getId());
        assertNull(eu.getUsuario());
        assertNull(eu.getEdicion());
        assertNull(eu.getFIns());
        assertEquals(Enum_Estado_inscripcion.INSCRIPTO, eu.getMiEstado());
    }

    @Test
    public void testConstructorCompleto() {
        Usuario u = usuario("nick");
        EdicionCurso ed = edicion("Ed1");
        Date f = new Date(1234);

        Edi_Usu eu = new Edi_Usu(u, ed, f);

        assertSame(u, eu.getUsuario());
        assertSame(ed, eu.getEdicion());
        assertSame(f, eu.getFIns());
        assertEquals(Enum_Estado_inscripcion.INSCRIPTO, eu.getMiEstado());
        assertEquals("nick", eu.getId().getMiUsuarioNickname());
        assertEquals("Ed1", eu.getId().getMiEdicionNombre());
    }

    @Test
    public void testConstructorConNulos() {
        Edi_Usu eu = new Edi_Usu(null, null, null);

        assertNotNull(eu.getId());
        assertNull(eu.getId().getMiUsuarioNickname());
        assertNull(eu.getId().getMiEdicionNombre());
    }

    @Test
    public void testSetters() {
        Edi_Usu eu = new Edi_Usu();
        Id_EdiUsu id = new Id_EdiUsu("nick", "Ed1");
        Usuario u = usuario("nick");
        EdicionCurso ed = edicion("Ed1");

        eu.setId(id);
        eu.setUsuario(u);
        eu.setEdicion(ed);
        eu.setEstado(Enum_Estado_inscripcion.ACEPTADO);

        assertSame(id, eu.getId());
        assertSame(u, eu.getUsuario());
        assertSame(ed, eu.getEdicion());
        assertEquals(Enum_Estado_inscripcion.ACEPTADO, eu.getMiEstado());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, new Edi_Usu().hashCode());

        Edi_Usu eu = new Edi_Usu(usuario("nick"), edicion("Ed1"), new Date());
        assertEquals(eu.getId().hashCode(), eu.hashCode());
    }

    @Test
    public void testEquals() {
        Edi_Usu a = new Edi_Usu(usuario("nick"), edicion("Ed1"), new Date());
        Edi_Usu igual = new Edi_Usu(usuario("nick"), edicion("Ed1"), new Date(5));
        Edi_Usu distinto = new Edi_Usu(usuario("otro"), edicion("Ed1"), new Date());

        assertTrue(a.equals(igual));
        assertFalse(a.equals(distinto));
        assertTrue(new Edi_Usu().equals(new Edi_Usu()));
        assertFalse(a.equals("x"));
        assertFalse(a.equals(null));
    }

    @Test
    public void testGetMyDTCompleto() {
        Edi_Usu eu = new Edi_Usu(usuario("nick"), edicion("Ed1"), new Date());

        DTMaster dt = eu.getMyDT();

        assertTrue(dt instanceof DTEdi_Usu);
    }

    @Test
    public void testGetMyDTSinUsuarioNiEdicion() {
        Edi_Usu eu = new Edi_Usu();

        DTMaster dt = eu.getMyDT();

        assertTrue(dt instanceof DTEdi_Usu);
    }
}
