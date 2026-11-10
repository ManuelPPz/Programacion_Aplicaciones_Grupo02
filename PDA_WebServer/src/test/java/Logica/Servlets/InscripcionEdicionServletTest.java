package Logica.Servlets;

import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTEdi_Usu;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuario;
import Logica.DTsClasses.EnumDT;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Tests del caso de uso Inscripción a edición de curso (con video), con el Servidor Central mockeado. */
public class InscripcionEdicionServletTest {

    private IController ic;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private InscripcionEdicionServlet servlet;
    private DTUsuario estudiante;
    private DTEdicionCurso vigente;

    @BeforeEach
    public void setUp() {
        ic = mock(IController.class);
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        // DTs reales: con Java 26 Mockito no puede mockear clases (solo interfaces).
        estudiante = new DTUsuario("roro", "Rodrigo", "Pérez", "roro@x.com", null, new Date(), null,
                new ArrayList<>(), new ArrayList<>());
        vigente = new DTEdicionCurso("INCO", "Java", "Java 2026", new Date(), new Date(), 30, 30,
                new ArrayList<>(), new Date(), new ArrayList<>(), new ArrayList<>(), null);

        when(req.getContextPath()).thenReturn("/app");
        when(req.getSession(false)).thenReturn(session);
        when(req.getSession()).thenReturn(session);
        when(req.getRequestDispatcher(InscripcionEdicionServlet.JSP)).thenReturn(dispatcher);
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);

        servlet = new InscripcionEdicionServlet() {
            @Override
            protected IController control() {
                return ic;
            }
        };
    }

    private void parametros(String filtro, String valor, String curso) {
        when(req.getParameter("filtro")).thenReturn(filtro);
        when(req.getParameter("valor")).thenReturn(valor);
        when(req.getParameter("curso")).thenReturn(curso);
    }

    // ----------------------------------------------------------------- GET

    @Test
    public void testSinSesionRedirigeAlLogin() throws Exception {
        when(req.getSession(false)).thenReturn(null);
        servlet.doGet(req, resp);
        verify(resp).sendRedirect("/app/IniciarSesionServlet");
        verifyNoInteractions(ic);
    }

    @Test
    public void testDocenteVeAvisoYNoUsaElServidorCentral() throws Exception {
        when(session.getAttribute("usuarioLogueado")).thenReturn(new DTDocente("house", "Gregory", "House", "h@x.com",
                null, new Date(), "INCO", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        servlet.doPost(req, resp);
        verify(req).setAttribute("soloEstudiantes", Boolean.TRUE);
        verify(dispatcher).forward(req, resp);
        verifyNoInteractions(ic);
    }

    @Test
    public void testGetSinFiltroSoloCargaInstitutosYCategorias() throws Exception {
        parametros(null, null, null);
        servlet.doGet(req, resp);
        verify(req).setAttribute("filtro", "instituto");
        verify(ic).ListarClase(EnumDT.DT_INSTITUTO);
        verify(ic).ListarClase(EnumDT.DT_CATEGORIA);
        verify(ic, never()).ListarCursos(any());
        verify(ic, never()).EdicionVigente(any());
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void testGetPorInstitutoListaCursos() throws Exception {
        parametros("instituto", "INCO", " ");
        List<DTMaster> cursos = new ArrayList<>();
        when(ic.ListarCursos("INCO")).thenReturn(cursos);
        servlet.doGet(req, resp);
        verify(req).setAttribute("cursos", cursos);
        verify(ic, never()).ListarCursosXCategoria(any());
    }

    @Test
    public void testGetPorCategoriaConCursoMuestraVigenteEInscripcion() throws Exception {
        parametros("categoria", "Programación", "Java");
        DTMaster ins = new DTEdi_Usu("roro", "Java 2026", new Date(), null, "https://v");
        when(ic.EdicionVigente("Java")).thenReturn(vigente);
        when(ic.ConsultarInscripcionEdicion("roro", "Java 2026")).thenReturn(ins);
        servlet.doGet(req, resp);
        verify(ic).ListarCursosXCategoria("Programación");
        verify(req).setAttribute("edicionVigente", vigente);
        verify(req).setAttribute("inscripcion", ins);
    }

    @Test
    public void testGetCursoSinVigente() throws Exception {
        parametros("instituto", "INCO", "Java");
        when(ic.EdicionVigente("Java")).thenReturn(null);
        servlet.doGet(req, resp);
        verify(req, never()).setAttribute(eq("edicionVigente"), any());
        verify(ic, never()).ConsultarInscripcionEdicion(any(), any());
    }

    @Test
    public void testGetPasaLosMensajesDeLaSesionAlRequest() throws Exception {
        parametros(null, null, null);
        when(session.getAttribute("inscripcionExito")).thenReturn("ok");
        servlet.doGet(req, resp);
        verify(req).setAttribute("exito", "ok");
        verify(session).removeAttribute("inscripcionExito");
    }

    // ---------------------------------------------------------------- POST

    private void post(String accion, String edicion, String video) {
        parametros("instituto", "INCO", "Java");
        when(req.getParameter("accion")).thenReturn(accion);
        when(req.getParameter("edicion")).thenReturn(edicion);
        when(req.getParameter("urlVideo")).thenReturn(video);
        when(ic.EdicionVigente("Java")).thenReturn(vigente);
    }

    @Test
    public void testPostInscribeConVideoYRedirige() throws Exception {
        post("inscribir", "Java 2026", " https://youtu.be/x ");
        servlet.doPost(req, resp);
        verify(ic).InscripcionAEdicionCurso("Java 2026", "roro", null, "https://youtu.be/x");
        verify(session).setAttribute(eq("inscripcionExito"), anyString());
        verify(resp).sendRedirect("/app/InscripcionEdicionServlet?filtro=instituto&valor=INCO&curso=Java");
    }

    @Test
    public void testPostInscribeSinVideo() throws Exception {
        post("inscribir", "Java 2026", "");
        servlet.doPost(req, resp);
        verify(ic).InscripcionAEdicionCurso("Java 2026", "roro", null, null);
    }

    @Test
    public void testPostModifica() throws Exception {
        post("modificar", "Java 2026", "https://v");
        servlet.doPost(req, resp);
        verify(ic).ModificarInscripcionEdicion("roro", "Java 2026", "https://v");
        verify(ic, never()).InscripcionAEdicionCurso(any(), any(), any(), any());
    }

    @Test
    public void testPostErrorDelServidorCentralQuedaEnSesion() throws Exception {
        post("inscribir", "Java 2026", "mala");
        doThrow(new Exception("La URL del video no es válida")).when(ic)
                .InscripcionAEdicionCurso(any(), any(), any(), any());
        servlet.doPost(req, resp);
        verify(session).setAttribute("inscripcionError", "La URL del video no es válida");
        verify(session).setAttribute("inscripcionUrlVideo", "mala");
        verify(resp).sendRedirect(anyString());
    }

    @Test
    public void testPostEdicionQueNoEsLaVigente() throws Exception {
        post("inscribir", "Java 2020", null);
        servlet.doPost(req, resp);
        verify(ic, never()).InscripcionAEdicionCurso(any(), any(), any(), any());
        verify(session).setAttribute(eq("inscripcionError"), contains("no es la edición vigente"));
    }

    @Test
    public void testPostSinCurso() throws Exception {
        post("inscribir", null, null);
        when(req.getParameter("curso")).thenReturn(null);
        servlet.doPost(req, resp);
        verify(session).setAttribute(eq("inscripcionError"), contains("Debe elegir"));
    }

    @Test
    public void testPostErrorSinMensaje() throws Exception {
        post("modificar", "Java 2026", null);
        doThrow(new Exception()).when(ic).ModificarInscripcionEdicion(any(), any(), any());
        servlet.doPost(req, resp);
        verify(session).setAttribute("inscripcionError", "No se pudo completar la operación.");
    }

    @Test
    public void testConsultaCodificaParametros() {
        assertEquals("?filtro=categoria&valor=Dise%C3%B1o+UX", InscripcionEdicionServlet.consulta("categoria", "Diseño UX", null));
        assertEquals("", InscripcionEdicionServlet.consulta(null, " ", null));
    }
}
