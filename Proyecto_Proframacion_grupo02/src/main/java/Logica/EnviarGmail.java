
package Logica;
import DTsClasses.DTUsuarioBase;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
/**
 *
 * @author mateo
 */
public class EnviarGmail {
    String remitente = "edext.support@gmail.com";
    String clave = "agou qjwk ylat daeq";
    
    public void Enviar(String destinatario, String asunto, String mensajeTexto) {
        Properties props = new Properties();

        // Usar SSL directo en el puerto 465
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");

        // Configuración SSL (reemplaza a starttls)
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Forzar protocolos de cifrado soportados por Java 21 / Gmail
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");

        Session session = Session.getInstance(props, new Authenticator() {
        @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setContent(mensajeTexto, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Correo enviado correctamente.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public String CuerpoMensajeNuevoUsuario(DTUsuarioBase dt) {
        String gmailBody = "<h2>Hola " + dt.getNombre() + ",</h2>"
                         + "<h3>¡Tu registro a la plataforma fue exitoso!</h3>"
                         + "<p><b>Detalles de la cuenta:</b></p>"
                         + "<ul>"
                         + "  <li><b>Nickname:</b> " + dt.getNickname() + "</li>"
                         + "  <li><b>Contraseña:</b> " + dt.getPassword() + "</li>"
                         + "  <li><b>Link: </b>(Link de la pagina de edEXT_db)"
                         + "</ul>"
                         + "<p style='font-size: 20px;'>Saludos!</p>";
                         
        return gmailBody;
    }
    
}
