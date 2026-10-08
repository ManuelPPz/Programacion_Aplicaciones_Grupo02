package Logica.Logica;

import Logica.DTsClasses.DTUsuarioBase;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author mateo
 */
    public class EnviarGmail {

        // Asegúrate de que la Contraseña de Aplicación de 16 caracteres generada en Google sea válida
        private final String remitente = "edext.support@gmail.com";
        private final String clave = "agou qjwk ylat daeq"; 

        public void Enviar(String destinatario, String asunto, String mensajeTexto) {
        // 1. Solución para el ClassLoader de Tomcat + Jakarta Activation
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(EnviarGmail.class.getClassLoader());

            // Registrar manualmente los MIME handlers de Jakarta Activation si no se detectan
            jakarta.activation.CommandMap commandMap = jakarta.activation.CommandMap.getDefaultCommandMap();
            if (commandMap instanceof jakarta.activation.MailcapCommandMap) {
                jakarta.activation.MailcapCommandMap mailcap = (jakarta.activation.MailcapCommandMap) commandMap;
                mailcap.addMailcap("text/html;; x-java-content-handler=org.eclipse.angus.mail.handlers.text_html");
                mailcap.addMailcap("text/plain;; x-java-content-handler=org.eclipse.angus.mail.handlers.text_plain");
                mailcap.addMailcap("multipart/*;; x-java-content-handler=org.eclipse.angus.mail.handlers.multipart_mixed");
            }

            Properties props = new Properties();

            // Configuración SMTP estándar de Gmail con TLS (Puerto 587)
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
            props.put("mail.debug", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remitente, clave.replace(" ", ""));
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente, "Soporte edEXT"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario.trim()));
            message.setSubject(asunto);
            message.setContent(mensajeTexto, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Correo enviado correctamente a: " + destinatario);

        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo SMTP: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado en EnviarGmail: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Restaurar el ClassLoader original
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    public void EnviarAsincrono(String destinatario, String asunto, String mensajeTexto) {
        CompletableFuture.runAsync(() -> {
            try {
                Enviar(destinatario, asunto, mensajeTexto);
            } catch (Exception e) {
                System.err.println("Error al enviar el correo en segundo plano: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public String CuerpoMensajeNuevoUsuario(DTUsuarioBase dt) {
        String gmailBody = "<h2>Hola " + dt.getNombre() + ":</h2>"
                 + "<h3>¡Tu registro en la plataforma ha sido exitoso!</h3>"
                 + "<p><b>Detalles de la cuenta:</b></p>"
                 + "<ul>"
                 + "  <li><b>Nickname:</b> " + dt.getNickname() + "</li>"
                 + "  <li><b>Contraseña:</b> " + dt.getPassword() + "</li>"
                 + "</ul>"
                 + "<p>¡Saludos!</p>"
                 + "<p>Somos un equipo de estudiantes probando nuestro proyecto académico de programación.</p>"
                 + "<p>Si recibiste este correo por error, se debe a que estuvimos realizando pruebas con direcciones de prueba. Pedimos disculpas por cualquier malestar o molestia ocasionada.</p>"
                 + "<p><b>¡Muchas gracias!</b></p>";
        return gmailBody;
    }
}