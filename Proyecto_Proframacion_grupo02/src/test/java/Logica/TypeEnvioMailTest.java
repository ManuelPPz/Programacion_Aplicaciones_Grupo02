package Logica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TypeEnvioMailTest {

    @Test
    public void testValues() {
        TypeEnvioMail[] valores = TypeEnvioMail.values();

        assertEquals(2, valores.length);
        assertArrayEquals(
                new TypeEnvioMail[]{TypeEnvioMail.ALTA_USUARIO, TypeEnvioMail.RECUPERAR_CONTRASENIA},
                valores);
    }

    @Test
    public void testValueOf() {
        assertEquals(TypeEnvioMail.ALTA_USUARIO, TypeEnvioMail.valueOf("ALTA_USUARIO"));
        assertEquals(TypeEnvioMail.RECUPERAR_CONTRASENIA, TypeEnvioMail.valueOf("RECUPERAR_CONTRASENIA"));
    }

    @Test
    public void testValueOfInvalido() {
        assertThrows(IllegalArgumentException.class, () -> TypeEnvioMail.valueOf("NO_EXISTE"));
    }
}