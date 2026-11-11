package Logica.Servlets;

import Logica.Logica.IController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Tests de la verificación AJAX de nickname y correo (Requerimiento 7.5). */
public class VerificarDisponibilidadServletTest {

    private IController ic;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private StringWriter salida;
    private VerificarDisponibilidadServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        ic = mock(IController.class);
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        salida = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(salida));
        servlet = new VerificarDisponibilidadServlet() {
            @Override
            protected IController control() {
                return ic;
            }
        };
    }

    private String pedir(String tipo, String valor) throws Exception {
        salida.getBuffer().setLength(0);
        when(req.getParameter("tipo")).thenReturn(tipo);
        when(req.getParameter("valor")).thenReturn(valor);
        servlet.doGet(req, resp);
        return salida.toString();
    }

    @Test
    public void testNicknameDisponible() throws Exception {
        when(ic.VerificarNickname("kairoh")).thenReturn(false);
        String json = pedir("nickname", " kairoh ");
        assertTrue(json.startsWith("{\"disponible\":true,"));
        assertTrue(json.endsWith("\"sugerencia\":null}"));
        verify(resp).setContentType("application/json;charset=UTF-8");
    }

    @Test
    public void testNicknameOcupadoSugiereOtro() throws Exception {
        when(ic.VerificarNickname("kairoh")).thenReturn(true);
        when(ic.VerificarNickname("kairoh2")).thenReturn(true);
        when(ic.VerificarNickname("kairoh3")).thenReturn(false);
        String json = pedir("nickname", "kairoh");
        assertTrue(json.startsWith("{\"disponible\":false,"));
        assertTrue(json.contains("ya está en uso"));
        assertTrue(json.endsWith("\"sugerencia\":\"kairoh3\"}"));
    }

    @Test
    public void testNicknameSinSugerencias() throws Exception {
        when(ic.VerificarNickname(anyString())).thenReturn(true);
        assertTrue(pedir("nickname", "x").endsWith("\"sugerencia\":null}"));
        verify(ic, times(1 + VerificarDisponibilidadServlet.MAX_SUGERENCIAS)).VerificarNickname(anyString());
    }

    @Test
    public void testNicknameInvalido() throws Exception {
        assertTrue(pedir("nickname", "  ").contains("obligatorio"));
        assertTrue(pedir("nickname", "con espacio").contains("espacios"));
        assertTrue(pedir("nickname", null).contains("obligatorio"));
        verifyNoInteractions(ic);
    }

    @Test
    public void testCorreo() throws Exception {
        when(ic.VerificarCorreo("a@b.com")).thenReturn(true);
        when(ic.VerificarCorreo("libre@b.com")).thenReturn(false);
        assertTrue(pedir("correo", "a@b.com").contains("\"disponible\":false"));
        assertTrue(pedir("correo", "libre@b.com").contains("\"disponible\":true"));
        assertTrue(pedir("correo", "sin-arroba").contains("formato"));
        assertTrue(pedir("correo", "").contains("obligatorio"));
    }

    @Test
    public void testTipoInvalidoYErrorDelServidor() throws Exception {
        pedir("otro", "x");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        when(ic.VerificarCorreo(anyString())).thenThrow(new RuntimeException("SC caído"));
        assertTrue(pedir("correo", "a@b.com").contains("No se pudo verificar"));
        verify(resp).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    @Test
    public void testJsonEscapado() {
        assertEquals("{\"disponible\":false,\"mensaje\":\"a\\\"b\\\\c\\n\\u003c\",\"sugerencia\":null}",
                VerificarDisponibilidadServlet.json(false, "a\"b\\c\n<", null));
    }
}
