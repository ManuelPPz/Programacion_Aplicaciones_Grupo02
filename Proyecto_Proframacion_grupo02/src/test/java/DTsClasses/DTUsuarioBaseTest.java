package DTsClasses;

import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTUsuarioBaseTest {

    @Test
    public void testConstructorYGetters() {
        Date f = new Date(1234);
        List<String> ediciones = List.of("Ed1");
        List<String> programas = List.of("P1");

        DTUsuarioBase dt = new DTUsuarioBase("nick", "Nombre", "Apellido", "mail@mail.com", "pw",
                f, "img.png", ediciones, programas);

        assertEquals("nick", dt.getNickname());
        assertEquals("Nombre", dt.getNombre());
        assertEquals("Apellido", dt.getApellido());
        assertEquals("mail@mail.com", dt.getCorreo());
        assertEquals("pw", dt.getPassword());
        assertSame(f, dt.getFNac());
        assertEquals("img.png", dt.getImg());
        assertSame(ediciones, dt.getEdiciones());
        assertSame(programas, dt.getProgramas());
        assertTrue(dt instanceof DTMaster);
    }
}
