package webservices;

import DTsClasses.DTDocente;
import Logica.Fabric;
import Logica.IController;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Publica el Web Service de verdad (Endpoint.publish) en un puerto libre y
 * verifica el WSDL y una llamada SOAP real.
 */
public class ServidorWSTest {

    @AfterEach
    public void tearDown() {
        ServidorWS.detener();
    }

    private static int puertoLibre() throws IOException {
        try (ServerSocket s = new ServerSocket(0)) {
            return s.getLocalPort();
        }
    }

    private static String leer(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        in.transferTo(out);
        return out.toString(StandardCharsets.UTF_8);
    }

    private static String get(String url) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        try (InputStream in = c.getInputStream()) {
            return leer(in);
        }
    }

    private static String soap(String url, String cuerpo) throws IOException {
        String sobre = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                + "xmlns:ws=\"http://webservices.edext/\"><S:Body>" + cuerpo + "</S:Body></S:Envelope>";
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setRequestMethod("POST");
        c.setDoOutput(true);
        c.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        c.setRequestProperty("SOAPAction", "\"\"");
        try (OutputStream out = c.getOutputStream()) {
            out.write(sobre.getBytes(StandardCharsets.UTF_8));
        }
        InputStream in = c.getResponseCode() < 400 ? c.getInputStream() : c.getErrorStream();
        try (in) {
            return leer(in);
        }
    }

    @Test
    public void testPublicaWsdlYRespondeLlamadas() throws Exception {
        IController ico = mock(IController.class);
        DTDocente docente = new DTDocente("jdoe", "Juan", "Doe", "j@x.com", "secreta", new Date(0),
                "INCO", "jdoe.png", List.of("Java"), List.of(), List.of());
        when(ico.ConsultarUsuario("jdoe")).thenReturn(docente);
        when(ico.ListarSeguidores("jdoe")).thenReturn(List.of("ana", "beto"));

        String url = "http://localhost:" + puertoLibre() + "/edext";
        String wsdlUrl = ServidorWS.publicar(url, new PublicadorWS(ico));

        assertEquals(url + "?wsdl", wsdlUrl);
        assertTrue(ServidorWS.estaPublicado());
        String wsdl = get(wsdlUrl);
        assertTrue(wsdl.contains("PublicadorService"));
        assertTrue(wsdl.contains("ConsultarUsuario"));

        // Respuesta polimórfica: viaja como dtDocente y sin la contraseña
        String resp = soap(url, "<ws:ConsultarUsuario><nickname>jdoe</nickname></ws:ConsultarUsuario>");
        assertTrue(resp.contains("dtDocente"), resp);
        assertTrue(resp.contains("<instituto>INCO</instituto>"), resp);
        assertTrue(resp.contains("<imgPath>jdoe.png</imgPath>"), resp);
        assertFalse(resp.contains("secreta"), resp);

        String seg = soap(url, "<ws:ListarSeguidores><nickname>jdoe</nickname></ws:ListarSeguidores>");
        assertTrue(seg.contains(">ana<") && seg.contains(">beto<"), seg);
    }

    @Test
    public void testErroresLleganComoSoapFault() throws Exception {
        IController ico = mock(IController.class);
        String url = "http://localhost:" + puertoLibre() + "/edext";
        ServidorWS.publicar(url, new PublicadorWS(ico));

        String resp = soap(url, "<ws:ObtenerImagen><carpeta>../secreto</carpeta><nombre>x</nombre></ws:ObtenerImagen>");
        assertTrue(resp.contains("Fault"), resp);
        assertTrue(resp.contains("Carpeta de imágenes no válida"), resp);
        assertTrue(resp.contains("EdextFault"), resp);
    }

    @Test
    public void testRepublicarYDetener() throws Exception {
        IController ico = mock(IController.class);
        Fabric fabric = mock(Fabric.class);
        when(fabric.GetIController()).thenReturn(ico);
        try (MockedStatic<Fabric> f = mockStatic(Fabric.class)) {
            f.when(Fabric::GetInstance).thenReturn(fabric);
            ServidorWS.publicar("http://localhost:" + puertoLibre() + "/edext");
        }
        assertTrue(ServidorWS.estaPublicado());

        // Publicar de nuevo detiene el anterior
        String url2 = "http://localhost:" + puertoLibre() + "/edext";
        ServidorWS.publicar(url2, new PublicadorWS(ico));
        assertTrue(get(url2 + "?wsdl").contains("PublicadorService"));

        ServidorWS.detener();
        assertFalse(ServidorWS.estaPublicado());
        ServidorWS.detener(); // no falla si ya estaba detenido
    }

    @Test
    public void testUrlDePublicacion() {
        // Local: se escucha solo en esta PC
        assertEquals("http://localhost:9128/edext", ServidorWS.urlDePublicacion("http://localhost:9128/edext"));
        assertEquals("http://127.0.0.1:9128/edext", ServidorWS.urlDePublicacion(" http://127.0.0.1:9128/edext "));
        // IP o nombre de la PC: se escucha en todas las interfaces con el mismo puerto y ruta
        assertEquals("http://0.0.0.0:9128/edext", ServidorWS.urlDePublicacion("http://192.168.1.10:9128/edext"));
        assertEquals("http://0.0.0.0:8000/", ServidorWS.urlDePublicacion("http://servidor:8000"));
    }

    @Test
    public void testUrlDePublicacionInvalida() {
        assertThrows(IllegalArgumentException.class, () -> ServidorWS.urlDePublicacion(null));
        assertThrows(IllegalArgumentException.class, () -> ServidorWS.urlDePublicacion(" "));
        assertThrows(IllegalArgumentException.class, () -> ServidorWS.urlDePublicacion("https://x:1/ws"));
        assertThrows(IllegalArgumentException.class, () -> ServidorWS.urlDePublicacion("http://x/ws"));
        assertThrows(IllegalArgumentException.class, () -> ServidorWS.urlDePublicacion("http:///ws"));
    }

    @Test
    public void testPublicaConIpEnTodasLasInterfaces() throws Exception {
        IController ico = mock(IController.class);
        int puerto = puertoLibre();
        // Con un nombre de PC distinto de localhost, igual responde por localhost
        String wsdl = ServidorWS.publicar("http://mi-pc-sc:" + puerto + "/edext", new PublicadorWS(ico));
        assertEquals("http://mi-pc-sc:" + puerto + "/edext?wsdl", wsdl);
        assertTrue(get("http://127.0.0.1:" + puerto + "/edext?wsdl").contains("PublicadorService"));
    }
}
