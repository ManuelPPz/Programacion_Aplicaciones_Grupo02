package DTsClasses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTInstitutoTest {

    @Test
    public void testConstructorYGetter() {
        DTInstituto dt = new DTInstituto("INCO");

        assertEquals("INCO", dt.getNombre());
        assertTrue(dt instanceof DTMaster);
    }
}
