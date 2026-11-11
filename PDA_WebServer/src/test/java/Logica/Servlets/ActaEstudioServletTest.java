package Logica.Servlets;

import Logica.DTsClasses.DTActaEstudio;
import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTFilaActa;
import Logica.DTsClasses.DTUsuario;
import Logica.Logica.IController;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Tests del caso de uso Tramitar Acta de Estudio, con el Servidor Central mockeado. */
public class ActaEstudioServletTest {

    private IController ic;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private HttpSession session;
    private ActaEstudioServlet servlet;
    private final ByteArrayOutputStream cuerpo = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() throws Exception {
        ic = mock(IController.class);
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(req.getContextPath()).thenReturn("/app");
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(new DTUsuario("ana", "Ana", "Paz", "a@x.com",
                null, new Date(), null, new ArrayList<>(), new ArrayList<>()));
        when(resp.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override public void write(int b) { cuerpo.write(b); }
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(WriteListener l) { }
        });
        servlet = new ActaEstudioServlet() {
            @Override
            protected IController control() {
                return ic;
            }
        };
    }

    @Test
    public void testDescargaElPdf() throws Exception {
        when(req.getParameter("programa")).thenReturn(" Prog ");
        when(ic.ActaDeEstudio("ana", "Prog")).thenReturn(new DTActaEstudio("ana", "Ana", "Paz", "Prog", "INCO",
                new Date(), List.of(new DTFilaActa("Java", "J1", 8, "Aprobado", true, new Date()))));

        servlet.doGet(req, resp);

        verify(resp).setContentType("application/pdf");
        verify(resp).setHeader("Content-Disposition", "attachment; filename=\"Acta_Prog_ana.pdf\"");
        verify(resp, never()).sendRedirect(anyString());
        assertTrue(new String(cuerpo.toByteArray(), 0, 5, StandardCharsets.ISO_8859_1).startsWith("%PDF-"));
    }

    @Test
    public void testErrorDelServidorCentralVuelveAlPerfil() throws Exception {
        when(req.getParameter("programa")).thenReturn("Prog");
        when(ic.ActaDeEstudio(any(), any())).thenThrow(new Exception("no inscripto"));
        servlet.doGet(req, resp);
        verify(resp).sendRedirect("/app/ConsultaUsuarioIndividualServlet?nickname=ana&errorActa=no+inscripto");
    }

    @Test
    public void testSinProgramaVuelveAlPerfil() throws Exception {
        servlet.doGet(req, resp);
        verify(resp).sendRedirect(startsWith("/app/ConsultaUsuarioIndividualServlet?nickname=ana&errorActa="));
        verifyNoInteractions(ic);
    }

    @Test
    public void testSoloEstudiantes() throws Exception {
        when(session.getAttribute("usuarioLogueado")).thenReturn(new DTDocente("house", "G", "H", "h@x.com",
                null, new Date(), "INCO", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        servlet.doGet(req, resp);
        when(req.getSession(false)).thenReturn(null);
        servlet.doGet(req, resp);
        verify(resp, times(2)).sendRedirect("/app/IniciarSesionServlet");
        verifyNoInteractions(ic);
    }
}
