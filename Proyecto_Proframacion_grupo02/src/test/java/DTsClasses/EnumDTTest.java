package DTsClasses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnumDTTest {

    @Test
    public void testValues() {
        EnumDT[] valores = EnumDT.values();

        assertEquals(6, valores.length);
        assertEquals(EnumDT.DT_INSTITUTO, valores[0]);
        assertEquals(EnumDT.DT_CATEGORIA, valores[5]);
    }

    @Test
    public void testValueOf() {
        assertEquals(EnumDT.DT_CURSO, EnumDT.valueOf("DT_CURSO"));
        assertThrows(IllegalArgumentException.class, () -> EnumDT.valueOf("OTRO"));
    }
}
