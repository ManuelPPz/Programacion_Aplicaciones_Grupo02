package Logica.Servlets;

import Logica.Classes.Enum_Estado_inscripcion;
import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTEdi_Usu;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTUsuario;
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
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Tests del caso de uso Seleccionar estudiantes para una edición de curso, con el SC mockeado. */
public class SeleccionarEstudiantesEdicionServletTest {

    private IController ic;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private SeleccionarEstudiantesEdicionServlet servlet;
    private DTEdicionCurso vigente;

    @BeforeEach
    public void setUp() {
        ic = mock(IController.class);
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        List<DTEdi_Usu> inscripciones = new ArrayList<>(List.of(
                new DTEdi_Usu("ana", "Java 2026", new Date(), Enum_Estado_inscripcion.ACEPTADO, "https://youtu.be/abc123XYZ"),
                new DTEdi_Usu("beto", "Java 2026", new Date(), Enum_Estado_inscripcion.INSCRIPTO)));
        vigente = new DTEdicionCurso("INCO", "Java", "Java 2026", new Date(), new Date(), 2, 0,
                new ArrayList<>(List.of("house")), new Date(), inscripciones, new ArrayList<>(), null);

        when(req.getContextPath()).thenReturn("/app");
        when(req.getSession(false)).thenReturn(session);
        when(req.getRequestDispatcher(SeleccionarEstudiantesEdicionServlet.VISTA)).thenReturn(dispatcher);
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente("house"));
        when(req.getParameter("instituto")).thenReturn("INCO");
        when(req.getParameter("curso")).thenReturn("Java");
        when(ic.EdicionVigente("Java")).thenReturn(vigente);

        servlet = new SeleccionarEstudiantesEdicionServlet() {
            @Override
            protected IController control() {
                return ic;
            }
        };
    }

    private static DTDocente docente(String nick) {
        return new DTDocente(nick, "N", "A", "x@x.com", null, new Date(), "INCO", null,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testSinSesionDeDocenteRedirigeAlLogin() throws Exception {
        when(session.getAttribute("usuarioLogueado")).thenReturn(new DTUsuario("roro", "R", "P", "r@x.com", null,
                new Date(), null, new ArrayList<>(), new ArrayList<>()));
        servlet.doGet(req, resp);
        servlet.doPost(req, resp);
        verify(resp, times(2)).sendRedirect("/app/IniciarSesionServlet");
        verifyNoInteractions(ic);
    }

    @Test
    public void testGetMuestraLaEdicionVigente() throws Exception {
        servlet.doGet(req, resp);
        verify(req).setAttribute("dtEdicion", vigente);
        verify(req).setAttribute("cantAceptados", 1);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void testGetDocenteQueNoParticipa() throws Exception {
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente("otro"));
        servlet.doGet(req, resp);
        verify(req, never()).setAttribute(eq("dtEdicion"), any());
        verify(req).setAttribute(eq("mensajeInfo"), contains("No participás"));
    }

    @Test
    public void testGetCursoSinEdicionVigente() throws Exception {
        when(ic.EdicionVigente("Java")).thenReturn(null);
        servlet.doGet(req, resp);
        verify(req).setAttribute(eq("mensajeInfo"), contains("no tiene una edición vigente"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPostRegistraLaSeleccion() throws Exception {
        when(req.getParameter("nomEdicion")).thenReturn("Java 2026");
        when(req.getParameterValues("estudiantes[]")).thenReturn(new String[]{"ana", "beto"});
        when(req.getParameter("estado_ana")).thenReturn("ACEPTADO");
        when(req.getParameter("estado_beto")).thenReturn("rechazado");

        servlet.doPost(req, resp);

        ArgumentCaptor<List<Object[]>> captor = ArgumentCaptor.forClass(List.class);
        verify(ic).AceptarEstudiantesAEdicionCurso(captor.capture(), eq("Java 2026"));
        assertEquals(2, captor.getValue().size());
        assertSame(Enum_Estado_inscripcion.RECHAZADO, captor.getValue().get(1)[1]);
        verify(req).setAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    public void testPostMuestraElErrorDelCupo() throws Exception {
        when(req.getParameter("nomEdicion")).thenReturn("Java 2026");
        when(req.getParameterValues("estudiantes[]")).thenReturn(new String[]{"ana"});
        when(req.getParameter("estado_ana")).thenReturn("ACEPTADO");
        doThrow(new Exception("supera el cupo")).when(ic).AceptarEstudiantesAEdicionCurso(any(), any());

        servlet.doPost(req, resp);

        verify(req).setAttribute(eq("mensajeError"), contains("supera el cupo"));
    }

    @Test
    public void testPostValidaciones() throws Exception {
        // Edición que no es la vigente
        when(req.getParameter("nomEdicion")).thenReturn("Java 2020");
        servlet.doPost(req, resp);
        // Sin cambios marcados / estado inválido
        when(req.getParameter("nomEdicion")).thenReturn("Java 2026");
        when(req.getParameterValues("estudiantes[]")).thenReturn(new String[]{"beto"});
        servlet.doPost(req, resp);
        when(req.getParameter("estado_beto")).thenReturn("INSCRIPTO");
        servlet.doPost(req, resp);
        // Docente que no participa
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente("otro"));
        servlet.doPost(req, resp);

        verify(ic, never()).AceptarEstudiantesAEdicionCurso(any(), any());
        verify(req).setAttribute(eq("mensajeError"), contains("no es la edición vigente"));
        verify(req).setAttribute(eq("mensajeError"), contains("ninguna inscripción"));
        verify(req).setAttribute(eq("mensajeError"), contains("Estado no válido"));
        verify(req).setAttribute(eq("mensajeError"), contains("No participás"));
    }
}
