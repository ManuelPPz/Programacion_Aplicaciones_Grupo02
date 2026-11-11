package Logica.Servlets;

import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTUsuario;
import Logica.DTsClasses.DTValoracionCurso;
import Logica.Logica.IController;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Tests del caso de uso Valorar un Curso, con el SC mockeado. */
public class ValorarCursoServletTest {

    private IController ic;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private ValorarCursoServlet servlet;
    private DTValoracionCurso resumen;

    @BeforeEach
    public void setUp() throws Exception {
        ic = mock(IController.class);
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        resumen = new DTValoracionCurso("Java", 1, new int[]{0, 0, 0, 1, 0}, 4f, 0);

        when(req.getContextPath()).thenReturn("/app");
        when(req.getSession(false)).thenReturn(session);
        when(req.getRequestDispatcher(ValorarCursoServlet.VISTA)).thenReturn(dispatcher);
        when(session.getAttribute("usuarioLogueado")).thenReturn(new DTUsuario("ana", "A", "P", "a@x.com", null,
                new Date(), null, new ArrayList<>(), new ArrayList<>()));
        when(ic.CursosParticipa("ana")).thenReturn(List.of("Java", "Web"));
        when(ic.ValoracionesCurso("Java", "ana")).thenReturn(resumen);

        servlet = new ValorarCursoServlet() {
            @Override
            protected IController control() {
                return ic;
            }
        };
    }

    @Test
    public void testSoloEstudiantes() throws Exception {
        when(session.getAttribute("usuarioLogueado")).thenReturn(new DTDocente("house", "N", "A", "x@x.com", null,
                new Date(), "INCO", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        servlet.doGet(req, resp);
        servlet.doPost(req, resp);
        when(req.getSession(false)).thenReturn(null);
        servlet.doGet(req, resp);
        verify(resp, times(3)).sendRedirect("/app/IniciarSesionServlet");
        verifyNoInteractions(ic);
    }

    @Test
    public void testGetSinCursoListaLosCursos() throws Exception {
        servlet.doGet(req, resp);
        verify(req).setAttribute("cursos", List.of("Java", "Web"));
        verify(req, never()).setAttribute(eq("resumen"), any());
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void testGetConCursoMuestraElResumen() throws Exception {
        when(req.getParameter("curso")).thenReturn(" Java ");
        servlet.doGet(req, resp);
        verify(req).setAttribute("cursoSeleccionado", "Java");
        verify(req).setAttribute("resumen", resumen);
    }

    @Test
    public void testGetCursoEnElQueNoParticipa() throws Exception {
        when(req.getParameter("curso")).thenReturn("BD");
        servlet.doGet(req, resp);
        verify(req).setAttribute("mensajeInfo", "No participás en el curso 'BD'.");
        verify(ic, never()).ValoracionesCurso(any(), any());
    }

    @Test
    public void testGetErrorDelServidorCentral() throws Exception {
        when(ic.CursosParticipa("ana")).thenThrow(new Exception("caído"));
        servlet.doGet(req, resp);
        verify(req).setAttribute("mensajeError", "Error al cargar datos: caído");
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void testPostValoraYRecargaElResumen() throws Exception {
        when(req.getParameter("curso")).thenReturn("Java");
        when(req.getParameter("puntaje")).thenReturn("4");
        servlet.doPost(req, resp);
        verify(ic).ValorarCurso("ana", "Java", 4);
        verify(req).setAttribute(eq("mensajeExito"), contains("Java"));
        verify(req).setAttribute("resumen", resumen);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void testPostErrores() throws Exception {
        servlet.doPost(req, resp);
        verify(req).setAttribute("mensajeError", "No se pudo registrar la valoración: Elegí un curso para valorar.");

        when(req.getParameter("curso")).thenReturn("Java");
        when(req.getParameter("puntaje")).thenReturn("muchas");
        servlet.doPost(req, resp);
        verify(req).setAttribute("mensajeError", "No se pudo registrar la valoración: Elegí de 1 a 5 estrellas.");

        when(req.getParameter("puntaje")).thenReturn("5");
        doThrow(new Exception("Ya valoraste el curso 'Java'.")).when(ic).ValorarCurso("ana", "Java", 5);
        servlet.doPost(req, resp);
        verify(req).setAttribute("mensajeError", "No se pudo registrar la valoración: Ya valoraste el curso 'Java'.");
        verify(dispatcher, times(3)).forward(req, resp);
    }
}
