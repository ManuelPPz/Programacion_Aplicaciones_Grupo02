package Logica.Filtros;

import Logica.Logica.IController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class VisitaCursoFilterTest {

    private IController ico;
    private VisitaCursoFilter filtro;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private FilterChain chain;

    @BeforeEach
    public void setUp() {
        ico = mock(IController.class);
        filtro = new VisitaCursoFilter(() -> ico);
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    public void testRegistraVisitaConParametroCurso() throws Exception {
        when(req.getParameter("curso")).thenReturn(" Java ");
        filtro.doFilter(req, resp, chain);
        verify(ico).RegistrarVisitaCurso("Java");
        verify(chain).doFilter(req, resp);
    }

    @Test
    public void testRegistraVisitaConParametroNombre() throws Exception {
        when(req.getParameter("curso")).thenReturn("");
        when(req.getParameter("nombre")).thenReturn("Python");
        filtro.doFilter(req, resp, chain);
        verify(ico).RegistrarVisitaCurso("Python");
        verify(chain).doFilter(req, resp);
    }

    @Test
    public void testSinCursoNoRegistra() throws Exception {
        filtro.doFilter(req, resp, chain);
        verify(ico, never()).RegistrarVisitaCurso(anyString());
        verify(chain).doFilter(req, resp);
    }

    @Test
    public void testSiFallaElServidorCentralLaConsultaSigue() throws Exception {
        when(req.getParameter("curso")).thenReturn("Java");
        when(ico.RegistrarVisitaCurso("Java")).thenThrow(new RuntimeException("caido"));
        assertDoesNotThrow(() -> filtro.doFilter(req, resp, chain));
        verify(chain).doFilter(req, resp);
    }

    @Test
    public void testConstructorPorDefecto() {
        assertNotNull(new VisitaCursoFilter());
    }
}
