package DTsClasses;

import Classes.Enum_Estado_inscripcion;
import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTEdi_UsuTest {

    @Test
    public void testConstructorYGetters() {
        Date f = new Date(1234);

        DTEdi_Usu dt = new DTEdi_Usu("nick", "Ed1", f, Enum_Estado_inscripcion.ACEPTADO);

        assertEquals("nick", dt.getNickEstudiante());
        assertEquals("Ed1", dt.getNomEdicion());
        assertSame(f, dt.getfInscripcion());
        assertEquals(Enum_Estado_inscripcion.ACEPTADO, dt.getEstadoIns());
        assertTrue(dt instanceof DTMaster);
    }
}
