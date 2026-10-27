package Classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Enum_Estado_inscripcionTest {

    @Test
    public void testValues() {
        Enum_Estado_inscripcion[] valores = Enum_Estado_inscripcion.values();

        assertEquals(3, valores.length);
        assertEquals(Enum_Estado_inscripcion.INSCRIPTO, valores[0]);
        assertEquals(Enum_Estado_inscripcion.ACEPTADO, valores[1]);
        assertEquals(Enum_Estado_inscripcion.RECHAZADO, valores[2]);
    }

    @Test
    public void testValueOf() {
        assertEquals(Enum_Estado_inscripcion.ACEPTADO, Enum_Estado_inscripcion.valueOf("ACEPTADO"));
        assertThrows(IllegalArgumentException.class, () -> Enum_Estado_inscripcion.valueOf("OTRO"));
    }
}
