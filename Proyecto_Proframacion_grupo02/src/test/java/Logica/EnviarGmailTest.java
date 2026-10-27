package Logica;

import DTsClasses.DTUsuarioBase;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests de EnviarGmail. Transport.send se mockea para no mandar mails reales.
 * Nota: mockStatic es por hilo, por eso EnviarAsincrono se prueba con un spy
 * (el hilo asincrono no veria el mock estatico).
 */
public class EnviarGmailTest {

    // ---------------------------------------------------------------- Enviar

    @Test
    public void testEnviarOk() throws Exception {
        EnviarGmail instance = new EnviarGmail();
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        try (MockedStatic<Transport> t = mockStatic(Transport.class)) {
            instance.Enviar("alumno@correo.com", "Asunto de prueba", "<p>Hola</p>");
            t.verify(() -> Transport.send(captor.capture()));
        }

        Message msg = captor.getValue();
        assertEquals("Asunto de prueba", msg.getSubject());
        assertEquals("alumno@correo.com", msg.getAllRecipients()[0].toString());
        assertTrue(msg.getFrom()[0].toString().contains("edext.support@gmail.com"));
        assertEquals("<p>Hola</p>", msg.getContent());
    }

    @Test
    public void testEnviarMessagingException() {
        EnviarGmail instance = new EnviarGmail();

        try (MockedStatic<Transport> t = mockStatic(Transport.class)) {
            t.when(() -> Transport.send(any(Message.class))).thenThrow(new MessagingException("fallo SMTP"));
            assertDoesNotThrow(() -> instance.Enviar("alumno@correo.com", "Asunto", "texto"));
            t.verify(() -> Transport.send(any(Message.class)));
        }
    }

    @Test
    public void testEnviarDireccionInvalida() {
        // InternetAddress.parse lanza AddressException (subclase de MessagingException)
        EnviarGmail instance = new EnviarGmail();

        try (MockedStatic<Transport> t = mockStatic(Transport.class)) {
            assertDoesNotThrow(() -> instance.Enviar("@@@ no es un mail", "Asunto", "texto"));
            t.verifyNoInteractions();
        }
    }

    @Test
    public void testEnviarExcepcionInesperada() {
        // destinatario null -> NullPointerException -> cae en el catch (Exception)
        EnviarGmail instance = new EnviarGmail();

        try (MockedStatic<Transport> t = mockStatic(Transport.class)) {
            assertDoesNotThrow(() -> instance.Enviar(null, "Asunto", "texto"));
            t.verifyNoInteractions();
        }
    }

    @Test
    public void testEnviarAutenticadorUsaClaveSinEspacios() throws Exception {
        // Cubre el Authenticator anonimo: se obtiene la Session del mensaje enviado
        EnviarGmail instance = new EnviarGmail();
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        try (MockedStatic<Transport> t = mockStatic(Transport.class)) {
            instance.Enviar("alumno@correo.com", "Asunto", "texto");
            t.verify(() -> Transport.send(captor.capture()));
        }

        Field f = Message.class.getDeclaredField("session");
        f.setAccessible(true);
        Session session = (Session) f.get(captor.getValue());

        PasswordAuthentication pa = session.requestPasswordAuthentication(null, 587, "smtp", "prompt", "user");
        assertNotNull(pa);
        assertEquals("edext.support@gmail.com", pa.getUserName());
        assertFalse(pa.getPassword().contains(" "));
        assertEquals(16, pa.getPassword().length());
    }

    // ------------------------------------------------------- EnviarAsincrono

    @Test
    public void testEnviarAsincronoLlamaAEnviar() {
        EnviarGmail spy = spy(new EnviarGmail());
        doNothing().when(spy).Enviar(anyString(), anyString(), anyString());

        spy.EnviarAsincrono("alumno@correo.com", "Asunto", "texto");

        verify(spy, timeout(3000)).Enviar("alumno@correo.com", "Asunto", "texto");
    }

    @Test
    public void testEnviarAsincronoCapturaExcepcion() throws Exception {
        EnviarGmail spy = spy(new EnviarGmail());
        CountDownLatch llamado = new CountDownLatch(1);
        doAnswer(inv -> {
            llamado.countDown();
            throw new RuntimeException("fallo en segundo plano");
        }).when(spy).Enviar(anyString(), anyString(), anyString());

        spy.EnviarAsincrono("alumno@correo.com", "Asunto", "texto");

        assertTrue(llamado.await(3, TimeUnit.SECONDS));
        Thread.sleep(300); // deja que el catch del hilo asincrono termine (para la cobertura)
    }

    // ------------------------------------------------ CuerpoMensajeNuevoUsuario

    @Test
    public void testCuerpoMensajeNuevoUsuario() {
        DTUsuarioBase dt = mock(DTUsuarioBase.class);
        when(dt.getNombre()).thenReturn("Ana");
        when(dt.getNickname()).thenReturn("ana99");
        when(dt.getPassword()).thenReturn("clave123");

        String body = new EnviarGmail().CuerpoMensajeNuevoUsuario(dt);

        assertTrue(body.contains("Hola Ana:"));
        assertTrue(body.contains("<b>Nickname:</b> ana99"));
        assertTrue(body.contains("<b>Contraseña:</b> clave123"));
        assertTrue(body.contains("Acceder a edEXT"));
    }
}