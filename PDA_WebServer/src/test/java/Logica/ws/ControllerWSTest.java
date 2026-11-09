package Logica.ws;

import Logica.Classes.Enum_Estado_inscripcion;
import Logica.DTsClasses.DTCategoria;
import Logica.DTsClasses.DTCurso;
import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTInstituto;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTProgramaForm;
import Logica.DTsClasses.DTUsuario;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;
import Logica.ws.cliente.DtCambioEstado;
import Logica.ws.cliente.DtCategoria;
import Logica.ws.cliente.DtCurso;
import Logica.ws.cliente.DtDocente;
import Logica.ws.cliente.DtEdiUsu;
import Logica.ws.cliente.DtEdicionCurso;
import Logica.ws.cliente.DtInstituto;
import Logica.ws.cliente.DtMaster;
import Logica.ws.cliente.DtProgramaForm;
import Logica.ws.cliente.DtUsuario;
import Logica.ws.cliente.EdextFault;
import Logica.ws.cliente.EdextWSException;
import Logica.ws.cliente.EnumEstadoInscripcion;
import Logica.ws.cliente.PublicadorWS;
import Logica.ws.cliente.Vigencia;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ControllerWS + ConversorDT con el Web Service mockeado: se verifica que cada
 * operación llame al servicio y que las respuestas se conviertan a los DTs del web.
 */
public class ControllerWSTest {

    private PublicadorWS ws;
    private ControllerWS ctrl;
    private final Date fecha = new Date(1_000_000_000_000L);

    @BeforeEach
    public void setUp() {
        ws = mock(PublicadorWS.class);
        ctrl = new ControllerWS(() -> ws);
    }

    private XMLGregorianCalendar xml(Date d) throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    }

    private DtDocente docente() throws Exception {
        DtDocente d = new DtDocente();
        d.setNickname("house");
        d.setNombre("Gregory");
        d.setApellido("House");
        d.setCorreo("g@x.com");
        d.setFNac(xml(fecha));
        d.setImgPath("house.jpg");
        d.setInstituto("INCO");
        d.getCursos().add("Java");
        d.getEdiciones().add("Java 2025");
        return d;
    }

    // ===================== Conversión =====================

    @Test
    public void testFechas() throws Exception {
        assertNull(ConversorDT.fecha((XMLGregorianCalendar) null));
        assertNull(ConversorDT.fecha((Date) null));
        assertEquals(fecha, ConversorDT.fecha(ConversorDT.fecha(fecha)));
    }

    @Test
    public void testEnums() {
        assertNull(ConversorDT.enumDT(null));
        for (EnumDT e : EnumDT.values()) {
            assertEquals(e.name(), ConversorDT.enumDT(e).name());
        }
        assertNull(ConversorDT.estado((EnumEstadoInscripcion) null));
        assertNull(ConversorDT.estado((Enum_Estado_inscripcion) null));
        for (Enum_Estado_inscripcion e : Enum_Estado_inscripcion.values()) {
            assertEquals(e, ConversorDT.estado(ConversorDT.estado(e)));
        }
    }

    @Test
    public void testConvierteDocenteYUsuario() throws Exception {
        DTMaster dt = ConversorDT.dt(docente());
        DTDocente d = assertInstanceOf(DTDocente.class, dt);
        assertEquals("house", d.getNickname());
        assertEquals("INCO", d.getInstituto());
        assertEquals(List.of("Java"), d.getCursos());
        assertEquals(List.of("Java 2025"), d.getEdiciones());
        assertEquals(fecha, d.getFNac());
        assertEquals("house.jpg", d.getImg());
        assertNull(d.getPassword());

        DtUsuario u = new DtUsuario();
        u.setNickname("roro");
        u.getProgramas().add("P");
        DTUsuario du = assertInstanceOf(DTUsuario.class, ConversorDT.dt(u));
        assertEquals("roro", du.getNickname());
        assertEquals(List.of("P"), du.getProgramas());
        assertNull(du.getFNac());
        assertNull(ConversorDT.usuario(null));
    }

    @Test
    public void testConvierteCursoEdicionPrograma() throws Exception {
        DtCurso c = new DtCurso();
        c.setInstituto("INCO");
        c.setNombre("Java");
        c.setDuracion(4);
        c.setCantHoras(2.5f);
        c.setCantCreditos(3);
        c.setURL("u");
        c.setFechaAlta(xml(fecha));
        c.getListPrevias().add("Prev");
        c.getListCategorias().add("Cat");
        c.setDocente("house");
        c.setImgPath("c.png");
        DTCurso dc = assertInstanceOf(DTCurso.class, ConversorDT.dt(c));
        assertEquals("Java", dc.getNombre());
        assertEquals(2.5f, dc.getCantHoras());
        assertEquals(List.of("Prev"), dc.getPrevias());
        assertEquals(List.of("Cat"), dc.getCategorias());
        assertEquals(fecha, dc.getFechaAlta());
        assertEquals("c.png", dc.getImage());

        DtEdiUsu ins = new DtEdiUsu();
        ins.setNickEstudiante("roro");
        ins.setNomEdicion("E");
        ins.setFInscripcion(xml(fecha));
        ins.setEstadoIns(EnumEstadoInscripcion.ACEPTADO);
        DtEdicionCurso e = new DtEdicionCurso();
        e.setNombre("E");
        e.setCurso("Java");
        e.setCupo(10);
        e.setCupoActual(2);
        e.setFInicio(xml(fecha));
        e.getListDocentes().add("house");
        e.getMisIscripciones().add(ins);
        e.getMisIscripciones().add(null);
        DTEdicionCurso de = assertInstanceOf(DTEdicionCurso.class, ConversorDT.dt(e));
        assertEquals(2, de.getCupoActual());
        assertEquals(fecha, de.getFInicio());
        assertNull(de.getFFin());
        assertEquals(List.of("house"), de.getDocentes());
        assertEquals(1, de.getMisInscripciones().size());
        assertEquals(Enum_Estado_inscripcion.ACEPTADO, de.getMisInscripciones().get(0).getEstadoIns());
        assertEquals(fecha, de.getMisInscripciones().get(0).getfInscripcion());
        assertSame(DTsClassesEdiUsu(), de.getMisInscripciones().get(0).getClass());

        DtProgramaForm p = new DtProgramaForm();
        p.setNombre("P");
        Vigencia v = new Vigencia();
        v.setFechaInicio(xml(fecha));
        v.setFechaFin(xml(fecha));
        p.setVigenciaPrograma(v);
        p.getCursos().add("Java");
        DTProgramaForm dp = assertInstanceOf(DTProgramaForm.class, ConversorDT.dt(p));
        assertEquals(fecha, dp.getVigenciaProg().getFechaFin());
        assertEquals(List.of("Java"), dp.getCursos());

        DtProgramaForm sinVigencia = new DtProgramaForm();
        assertNull(ConversorDT.programa(sinVigencia).getVigenciaProg());
    }

    private static Class<?> DTsClassesEdiUsu() {
        return Logica.DTsClasses.DTEdi_Usu.class;
    }

    @Test
    public void testConvierteSimplesYListas() throws Exception {
        DtInstituto i = new DtInstituto();
        i.setNombre("INCO");
        DtCategoria c = new DtCategoria();
        c.setNombre("Cat");
        DtEdiUsu ins = new DtEdiUsu();
        ins.setNickEstudiante("x");

        List<DTMaster> l = ConversorDT.lista(Arrays.asList(i, null, c, ins, new DtCambioEstado()));
        assertEquals(4, l.size());
        assertEquals("INCO", assertInstanceOf(DTInstituto.class, l.get(0)).getNombre());
        assertEquals("Cat", assertInstanceOf(DTCategoria.class, l.get(1)).getNombre());
        assertEquals("x", ((Logica.DTsClasses.DTEdi_Usu) l.get(2)).getNickEstudiante());
        assertSame(DTMaster.class, l.get(3).getClass());
        assertTrue(ConversorDT.lista(null).isEmpty());
        assertNull(ConversorDT.dt(null));
    }

    // ===================== ControllerWS =====================

    @Test
    public void testUsuarios() throws Exception {
        when(ws.consultarUsuario("house")).thenReturn(docente());
        when(ws.consultaUsuarioAvanzada("house", "1234")).thenReturn(docente());
        when(ws.listarSeguidos("a")).thenReturn(List.of("b"));
        when(ws.listarSeguidores("a")).thenReturn(List.of("c"));
        when(ws.verificarCorreo("c")).thenReturn(true);
        when(ws.verificarNickname("n")).thenReturn(true);
        when(ws.generateRandPassword()).thenReturn("pw");

        ctrl.AgregarUsuario("n", "No", "Ap", "c", "p", fecha, true, "INCO", "img");
        ctrl.ModificarUsuario("n", "No", "Ap", "", false, fecha, "", "img");
        ctrl.SeguirUsuario("a", "b");
        ctrl.DejarDeSeguir("a", "b");

        verify(ws).agregarUsuario(eq("n"), eq("No"), eq("Ap"), eq("c"), eq("p"), any(), eq(true), eq("INCO"), eq("img"));
        verify(ws).modificarUsuario(eq("n"), eq("No"), eq("Ap"), eq(""), eq(false), any(), eq(""), eq("img"));
        verify(ws).seguirUsuario("a", "b");
        verify(ws).dejarDeSeguir("a", "b");
        assertInstanceOf(DTDocente.class, ctrl.ConsultarUsuario("house"));
        assertInstanceOf(DTUsuarioBase.class, ctrl.ConsultaUsuarioAvanzada("house", "1234"));
        assertNull(ctrl.ConsultaUsuarioAvanzada("x", "y"));
        assertEquals(List.of("b"), ctrl.ListarSeguidos("a"));
        assertEquals(List.of("c"), ctrl.ListarSeguidores("a"));
        assertTrue(ctrl.VerificarCorreo("c"));
        assertTrue(ctrl.VerificarNickname("n"));
        assertEquals("pw", ctrl.GenerateRandPassword());
    }

    @Test
    public void testErroresDeNegocioConservanElMensaje() throws Exception {
        EdextWSException fault = new EdextWSException("El nickname ya existe", new EdextFault());
        doThrow(fault).when(ws).agregarUsuario(any(), any(), any(), any(), any(), any(), anyBoolean(), any(), any());
        doThrow(fault).when(ws).altaCurso(any(), any(), any(), anyInt(), anyFloat(), anyInt(), any(), any(), any(), any(), any(), any());
        doThrow(fault).when(ws).altaEdicionCurso(any(), any(), any(), any(), any(), anyInt(), any(), any(), any());
        doThrow(fault).when(ws).crearProgramasDeFormacion(any(), any(), any(), any(), any(), any(), any());

        assertEquals("El nickname ya existe", assertThrows(Exception.class,
                () -> ctrl.AgregarUsuario("n", "a", "b", "c", "d", fecha, false, "", "")).getMessage());
        assertThrows(Exception.class, () -> ctrl.AltaCurso("I", "C", "d", 1, 1f, 1, "u", null, fecha, "doc", null, null));
        assertThrows(Exception.class, () -> ctrl.AltaEdicionCurso("I", "C", "E", fecha, fecha, 1, null, fecha, null));
        assertThrows(Exception.class, () -> ctrl.CrearProgramasDeFormacion("P", "d", fecha, fecha, fecha, null, null));
    }

    @Test
    public void testCursosEdicionesProgramas() throws Exception {
        List<DtMaster> cursos = new ArrayList<>(List.of(new DtCurso()));
        when(ws.consultaCurso("Java")).thenReturn(new DtCurso());
        when(ws.verificarCurso("Java")).thenReturn(true);
        when(ws.listarCursos("INCO")).thenReturn(cursos);
        when(ws.listarCursosXCategoria("Cat")).thenReturn(cursos);
        when(ws.consultaEdicionCurso("E")).thenReturn(new DtEdicionCurso());
        when(ws.verificarEdicion("E")).thenReturn(true);
        when(ws.listarEdiciones("Java")).thenReturn(new ArrayList<>(List.of(new DtEdicionCurso())));
        when(ws.listarEdicionesPrioritarias("E")).thenReturn(new ArrayList<>());
        when(ws.listarInscripciones("roro")).thenReturn(new ArrayList<>(List.of(new DtEdiUsu())));
        when(ws.consultaProgramaFormacion("P")).thenReturn(new DtProgramaForm());
        when(ws.verificarPrograma("P")).thenReturn(true);
        when(ws.listarProgramaDeForm()).thenReturn(new ArrayList<>(List.of(new DtProgramaForm())));
        when(ws.listarProgramasDeCurso("Java")).thenReturn(new ArrayList<>());
        when(ws.listarClase(Logica.ws.cliente.EnumDT.DT_INSTITUTO)).thenReturn(new ArrayList<>(List.of(new DtInstituto())));
        when(ws.listarDocentes("INCO")).thenReturn(new ArrayList<>(List.of(docente())));
        when(ws.listarCurEdiProg(Logica.ws.cliente.EnumDT.DT_CATEGORIA, "Cat")).thenReturn(cursos);
        when(ws.verificarInstituto("INCO")).thenReturn(true);
        when(ws.verificarCategoria("Cat")).thenReturn(true);

        ctrl.AltaCurso("INCO", "Java", "d", 1, 2f, 3, "u", List.of("p"), fecha, "house", List.of("c"), "img");
        ctrl.AltaEdicionCurso("INCO", "Java", "E", fecha, fecha, 10, List.of("house"), fecha, "img");
        ctrl.InscripcionAEdicionCurso("E", "roro", fecha);
        ctrl.CrearProgramasDeFormacion("P", "d", fecha, fecha, fecha, "img", "INCO");
        ctrl.AgregarCursoAProgramas("P", List.of("Java"));
        ctrl.InscripcionUsuarioAProgramas("P", "roro", fecha);

        verify(ws).altaCurso(eq("INCO"), eq("Java"), eq("d"), eq(1), eq(2f), eq(3), eq("u"), eq(List.of("p")),
                any(), eq("house"), eq(List.of("c")), eq("img"));
        verify(ws).altaEdicionCurso(eq("INCO"), eq("Java"), eq("E"), any(), any(), eq(10), eq(List.of("house")), any(), eq("img"));
        verify(ws).inscripcionAEdicionCurso(eq("E"), eq("roro"), any());
        verify(ws).crearProgramasDeFormacion(eq("P"), eq("d"), any(), any(), any(), eq("img"), eq("INCO"));
        verify(ws).agregarCursoAProgramas("P", List.of("Java"));
        verify(ws).inscripcionUsuarioAProgramas(eq("P"), eq("roro"), any());

        assertInstanceOf(DTCurso.class, ctrl.ConsultaCurso("Java"));
        assertTrue(ctrl.VerificarCurso("Java"));
        assertEquals(1, ctrl.ListarCursos("INCO").size());
        assertEquals(1, ctrl.ListarCursosXCategoria("Cat").size());
        assertInstanceOf(DTEdicionCurso.class, ctrl.ConsultaEdicionCurso("E"));
        assertTrue(ctrl.VerificarEdicion("E"));
        assertEquals(1, ctrl.ListarEdiciones("Java").size());
        assertTrue(ctrl.ListarEdicionesPrioritarias("E").isEmpty());
        assertEquals(1, ctrl.ListarInscripciones("roro").size());
        assertInstanceOf(DTProgramaForm.class, ctrl.ConsultaProgramaFormacion("P"));
        assertTrue(ctrl.VerificarPrograma("P"));
        assertTrue(ctrl.ExistePrograma("P"));
        assertEquals(1, ctrl.ListarProgramaDeForm().size());
        assertTrue(ctrl.ListarProgramasDeCurso("Java").isEmpty());
        assertInstanceOf(DTInstituto.class, ctrl.ListarClase(EnumDT.DT_INSTITUTO).get(0));
        assertInstanceOf(DTDocente.class, ctrl.ListarDocentes("INCO").get(0));
        assertEquals(1, ctrl.ListarCurEdiProg(EnumDT.DT_CATEGORIA, "Cat").size());
        assertTrue(ctrl.VerificarInstituto("INCO"));
        assertTrue(ctrl.VerificarCategoria("Cat"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAceptarEstudiantesArmaLosDTCambioEstado() {
        List<Object[]> cambios = Arrays.asList(
                new Object[]{"ana", Enum_Estado_inscripcion.ACEPTADO},
                null,
                new Object[]{"solo"},
                new Object[]{42, Enum_Estado_inscripcion.RECHAZADO},
                new Object[]{"beto", "no es un estado"},
                new Object[]{"caro", Enum_Estado_inscripcion.RECHAZADO});

        ctrl.AceptarEstudiantesAEdicionCurso(cambios, "E");

        ArgumentCaptor<List<DtCambioEstado>> captor = ArgumentCaptor.forClass(List.class);
        verify(ws).aceptarEstudiantesAEdicionCurso(captor.capture(), eq("E"));
        List<DtCambioEstado> enviados = captor.getValue();
        assertEquals(2, enviados.size());
        assertEquals("ana", enviados.get(0).getNickname());
        assertEquals(EnumEstadoInscripcion.ACEPTADO, enviados.get(0).getEstado());
        assertEquals(EnumEstadoInscripcion.RECHAZADO, enviados.get(1).getEstado());

        ctrl.AceptarEstudiantesAEdicionCurso(null, "F");
        verify(ws).aceptarEstudiantesAEdicionCurso(eq(new ArrayList<>()), eq("F"));
    }

    @Test
    public void testOperacionesSoloDelServidorCentral() {
        assertThrows(UnsupportedOperationException.class, () -> ctrl.ActualizarPrograma("P", "d", fecha, fecha));
        assertThrows(UnsupportedOperationException.class, () -> ctrl.AltaInstituto("I"));
        assertThrows(UnsupportedOperationException.class, () -> ctrl.AltaCategoria("C"));
    }

    @Test
    public void testClienteWSConfiguracion() {
        String anterior = ClienteWS.getUrl();
        try {
            ClienteWS.configurar("  http://10.0.0.5:9128/edext  ");
            assertEquals("http://10.0.0.5:9128/edext", ClienteWS.getUrl());
            ClienteWS.configurar(" ");
            assertEquals(ClienteWS.URL_POR_DEFECTO, ClienteWS.getUrl());
            ClienteWS.configurar(null);
            assertEquals(ClienteWS.URL_POR_DEFECTO, ClienteWS.getUrl());
        } finally {
            ClienteWS.configurar(anterior);
        }
    }

    @Test
    public void testClienteWSApuntaALaUrlConfigurada() {
        String anterior = ClienteWS.getUrl();
        try {
            ClienteWS.configurar("http://192.168.1.10:9128/edext");
            PublicadorWS p1 = ClienteWS.getPuerto();
            assertSame(p1, ClienteWS.getPuerto()); // se reutiliza en el mismo hilo
            assertEquals("http://192.168.1.10:9128/edext", ((jakarta.xml.ws.BindingProvider) p1)
                    .getRequestContext().get(jakarta.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY));

            ClienteWS.configurar("http://otra:1/ws"); // al cambiar la URL se crea uno nuevo
            PublicadorWS p2 = ClienteWS.getPuerto();
            assertNotSame(p1, p2);
            assertEquals("http://otra:1/ws", ((jakarta.xml.ws.BindingProvider) p2)
                    .getRequestContext().get(jakarta.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
        } finally {
            ClienteWS.configurar(anterior);
        }
    }
}
