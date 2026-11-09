package webservices;

import Classes.Enum_Estado_inscripcion;
import DTsClasses.DTCambioEstado;
import DTsClasses.DTMaster;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import Logica.Fabric;
import Logica.IController;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import util.ImagenUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests de PublicadorWS: cada operación delega en el IController (mockeado).
 */
public class PublicadorWSTest {

    @TempDir
    Path tmp;

    private IController ico;
    private PublicadorWS ws;
    private String dirAnterior;

    @BeforeEach
    public void setUp() {
        ico = mock(IController.class);
        ws = new PublicadorWS(ico);
        dirAnterior = System.getProperty("edext.imagenes.dir");
        System.setProperty("edext.imagenes.dir", tmp.toString());
    }

    @AfterEach
    public void tearDown() {
        if (dirAnterior == null) {
            System.clearProperty("edext.imagenes.dir");
        } else {
            System.setProperty("edext.imagenes.dir", dirAnterior);
        }
    }

    @Test
    public void testConstructorPorDefectoUsaFabric() {
        Fabric fabric = mock(Fabric.class);
        when(fabric.GetIController()).thenReturn(ico);
        try (MockedStatic<Fabric> f = mockStatic(Fabric.class)) {
            f.when(Fabric::GetInstance).thenReturn(fabric);
            PublicadorWS porDefecto = new PublicadorWS();
            porDefecto.VerificarNickname("x");
        }
        verify(ico).VerificarNickname("x");
    }

    // ===================== Usuarios =====================

    @Test
    public void testOperacionesDeUsuario() throws Exception {
        Date d = new Date();
        DTUsuarioBase dtu = mock(DTUsuarioBase.class);
        DTMaster dtm = mock(DTMaster.class);
        when(ico.ConsultarUsuario("u")).thenReturn(dtu);
        when(ico.ConsultaUsuarioAvanzada("u", "p")).thenReturn(dtm);
        when(ico.ListarSeguidos("u")).thenReturn(List.of("a"));
        when(ico.ListarSeguidores("u")).thenReturn(List.of("b"));
        when(ico.VerificarNickname("u")).thenReturn(true);
        when(ico.VerificarCorreo("c")).thenReturn(true);
        when(ico.GenerateRandPassword()).thenReturn("pw");

        ws.AgregarUsuario("u", "n", "a", "c", "p", d, true, "INCO", "img");
        ws.ModificarUsuario("u", "n", "a", "p", false, d, "", "img");
        ws.SeguirUsuario("u", "v");
        ws.DejarDeSeguir("u", "v");

        verify(ico).AgregarUsuario("u", "n", "a", "c", "p", d, true, "INCO", "img");
        verify(ico).ModificarUsuario("u", "n", "a", "p", false, d, "", "img");
        verify(ico).SeguirUsuario("u", "v");
        verify(ico).DejarDeSeguir("u", "v");
        assertSame(dtu, ws.ConsultarUsuario("u"));
        assertSame(dtm, ws.ConsultaUsuarioAvanzada("u", "p"));
        assertEquals(List.of("a"), ws.ListarSeguidos("u"));
        assertEquals(List.of("b"), ws.ListarSeguidores("u"));
        assertTrue(ws.VerificarNickname("u"));
        assertTrue(ws.VerificarCorreo("c"));
        assertEquals("pw", ws.GenerateRandPassword());
    }

    @Test
    public void testAgregarUsuarioConErrorDevuelveFault() throws Exception {
        doThrow(new Exception("nickname repetido")).when(ico)
                .AgregarUsuario(any(), any(), any(), any(), any(), any(), anyBoolean(), any(), any());
        EdextWSException e = assertThrows(EdextWSException.class,
                () -> ws.AgregarUsuario("u", "n", "a", "c", "p", new Date(), false, "", ""));
        assertEquals("nickname repetido", e.getMessage());
    }

    @Test
    public void testErrorSinMensajeUsaToString() throws Exception {
        doThrow(new IllegalStateException()).when(ico)
                .AgregarUsuario(any(), any(), any(), any(), any(), any(), anyBoolean(), any(), any());
        EdextWSException e = assertThrows(EdextWSException.class,
                () -> ws.AgregarUsuario("u", "n", "a", "c", "p", new Date(), false, "", ""));
        assertEquals("java.lang.IllegalStateException", e.getMessage());
    }

    // ===================== Cursos y ediciones =====================

    @Test
    public void testOperacionesDeCursos() throws Exception {
        Date d = new Date();
        List<String> previas = List.of("p");
        List<String> cats = List.of("c");
        List<DTMaster> lista = new ArrayList<>();
        DTMaster dt = mock(DTMaster.class);
        when(ico.ConsultaCurso("Java")).thenReturn(dt);
        when(ico.VerificarCurso("Java")).thenReturn(true);
        when(ico.ListarCursos("INCO")).thenReturn(lista);
        when(ico.ListarCursosXCategoria("c")).thenReturn(lista);

        ws.AltaCurso("INCO", "Java", "d", 1, 2f, 3, "url", previas, d, "doc", cats, "img");

        verify(ico).AltaCurso("INCO", "Java", "d", 1, 2f, 3, "url", previas, d, "doc", cats, "img");
        assertSame(dt, ws.ConsultaCurso("Java"));
        assertTrue(ws.VerificarCurso("Java"));
        assertSame(lista, ws.ListarCursos("INCO"));
        assertSame(lista, ws.ListarCursosXCategoria("c"));
    }

    @Test
    public void testAltaCursoConError() throws Exception {
        doThrow(new Exception("ya existe")).when(ico).AltaCurso(any(), any(), any(), anyInt(), anyFloat(),
                anyInt(), any(), any(), any(), any(), any(), any());
        assertThrows(EdextWSException.class,
                () -> ws.AltaCurso("I", "C", "d", 1, 1f, 1, "u", null, new Date(), "doc", null, null));
    }

    @Test
    public void testOperacionesDeEdiciones() throws Exception {
        Date d = new Date();
        List<String> docentes = List.of("doc");
        List<DTMaster> lista = new ArrayList<>();
        DTMaster dt = mock(DTMaster.class);
        when(ico.ConsultaEdicionCurso("E")).thenReturn(dt);
        when(ico.VerificarEdicion("E")).thenReturn(true);
        when(ico.ListarEdiciones("C")).thenReturn(lista);
        when(ico.ListarEdicionesPrioritarias("E")).thenReturn(lista);
        when(ico.ListarInscripciones("u")).thenReturn(lista);

        ws.AltaEdicionCurso("I", "C", "E", d, d, 10, docentes, d, "img");
        ws.InscripcionAEdicionCurso("E", "u", d);

        verify(ico).AltaEdicionCurso("I", "C", "E", d, d, 10, docentes, d, "img");
        verify(ico).InscripcionAEdicionCurso("E", "u", d);
        assertSame(dt, ws.ConsultaEdicionCurso("E"));
        assertTrue(ws.VerificarEdicion("E"));
        assertSame(lista, ws.ListarEdiciones("C"));
        assertSame(lista, ws.ListarEdicionesPrioritarias("E"));
        assertSame(lista, ws.ListarInscripciones("u"));
    }

    @Test
    public void testAltaEdicionConError() throws Exception {
        doThrow(new Exception("fechas")).when(ico).AltaEdicionCurso(any(), any(), any(), any(), any(),
                anyInt(), any(), any(), any());
        assertThrows(EdextWSException.class,
                () -> ws.AltaEdicionCurso("I", "C", "E", null, null, 1, null, null, null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAceptarEstudiantesConvierteLosDTs() {
        Enum_Estado_inscripcion estado = Enum_Estado_inscripcion.values()[0];
        List<DTCambioEstado> cambios = Arrays.asList(
                new DTCambioEstado("ana", estado),
                null,
                new DTCambioEstado(null, estado),
                new DTCambioEstado("beto", null));

        ws.AceptarEstudiantesAEdicionCurso(cambios, "E");

        ArgumentCaptor<List<Object[]>> captor = ArgumentCaptor.forClass(List.class);
        verify(ico).AceptarEstudiantesAEdicionCurso(captor.capture(), eq("E"));
        assertEquals(1, captor.getValue().size());
        assertEquals("ana", captor.getValue().get(0)[0]);
        assertSame(estado, captor.getValue().get(0)[1]);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAceptarEstudiantesSinCambios() {
        ws.AceptarEstudiantesAEdicionCurso(null, "E");
        ArgumentCaptor<List<Object[]>> captor = ArgumentCaptor.forClass(List.class);
        verify(ico).AceptarEstudiantesAEdicionCurso(captor.capture(), eq("E"));
        assertTrue(captor.getValue().isEmpty());
    }

    @Test
    public void testListarEstadosInscripcion() {
        List<String> estados = ws.ListarEstadosInscripcion();
        assertEquals(Enum_Estado_inscripcion.values().length, estados.size());
        assertEquals(Enum_Estado_inscripcion.values()[0].name(), estados.get(0));
    }

    // ===================== Programas, listados =====================

    @Test
    public void testOperacionesDeProgramasYListados() throws Exception {
        Date d = new Date();
        List<String> cursos = List.of("Java");
        List<DTMaster> lista = new ArrayList<>();
        DTMaster dt = mock(DTMaster.class);
        when(ico.ConsultaProgramaFormacion("P")).thenReturn(dt);
        when(ico.VerificarPrograma("P")).thenReturn(true);
        when(ico.ListarProgramaDeForm()).thenReturn(lista);
        when(ico.ListarProgramasDeCurso("Java")).thenReturn(lista);
        when(ico.ListarClase(EnumDT.DT_INSTITUTO)).thenReturn(lista);
        when(ico.ListarDocentes("INCO")).thenReturn(lista);
        when(ico.ListarCurEdiProg(EnumDT.DT_CATEGORIA, "c")).thenReturn(lista);
        when(ico.VerificarInstituto("INCO")).thenReturn(true);
        when(ico.VerificarCategoria("c")).thenReturn(true);

        ws.CrearProgramasDeFormacion("P", "d", d, d, d, "img", "INCO");
        ws.AgregarCursoAProgramas("P", cursos);
        ws.InscripcionUsuarioAProgramas("P", "u", d);

        verify(ico).CrearProgramasDeFormacion("P", "d", d, d, d, "img", "INCO");
        verify(ico).AgregarCursoAProgramas("P", cursos);
        verify(ico).InscripcionUsuarioAProgramas("P", "u", d);
        assertSame(dt, ws.ConsultaProgramaFormacion("P"));
        assertTrue(ws.VerificarPrograma("P"));
        assertSame(lista, ws.ListarProgramaDeForm());
        assertSame(lista, ws.ListarProgramasDeCurso("Java"));
        assertSame(lista, ws.ListarClase(EnumDT.DT_INSTITUTO));
        assertSame(lista, ws.ListarDocentes("INCO"));
        assertSame(lista, ws.ListarCurEdiProg(EnumDT.DT_CATEGORIA, "c"));
        assertTrue(ws.VerificarInstituto("INCO"));
        assertTrue(ws.VerificarCategoria("c"));
    }

    @Test
    public void testCrearProgramaConError() throws Exception {
        doThrow(new Exception("error")).when(ico).CrearProgramasDeFormacion(any(), any(), any(), any(), any(), any(), any());
        assertThrows(EdextWSException.class,
                () -> ws.CrearProgramasDeFormacion("P", "d", null, null, null, null, null));
    }

    // ===================== Imágenes =====================

    @Test
    public void testSubirObtenerYBorrarImagen() throws Exception {
        String nombre = ws.SubirImagen(ImagenUtil.CARPETA_CURSOS, "Java", "foto.png", new byte[]{1, 2});
        assertTrue(nombre.startsWith("java_") && nombre.endsWith(".png"), nombre);
        assertArrayEquals(new byte[]{1, 2}, ws.ObtenerImagen(ImagenUtil.CARPETA_CURSOS, nombre));

        ws.BorrarImagen(ImagenUtil.CARPETA_CURSOS, nombre);
        assertNull(ws.ObtenerImagen(ImagenUtil.CARPETA_CURSOS, nombre));
        assertEquals("", ws.SubirImagen(ImagenUtil.CARPETA_PERFILES, "u", "a.jpg", null));
    }

    @Test
    public void testCarpetaInvalida() {
        assertThrows(EdextWSException.class, () -> ws.SubirImagen("../../etc", "x", "a.png", new byte[]{1}));
        assertThrows(EdextWSException.class, () -> ws.ObtenerImagen(null, "a.png"));
        assertThrows(EdextWSException.class, () -> ws.BorrarImagen("otra", "a.png"));
    }

    @Test
    public void testSubirImagenConErrorDeDisco() throws Exception {
        // La "carpeta base" es un archivo: no se puede crear uploads/ dentro
        Path archivo = tmp.resolve("noEsCarpeta");
        java.nio.file.Files.write(archivo, new byte[]{0});
        System.setProperty("edext.imagenes.dir", archivo.toString());
        assertThrows(EdextWSException.class,
                () -> ws.SubirImagen(ImagenUtil.CARPETA_PERFILES, "u", "a.jpg", new byte[]{1}));
    }

    @Test
    public void testFaultInfo() {
        EdextWSException e = new EdextWSException("m");
        assertEquals("m", e.getFaultInfo().getMensaje());
        EdextFault f = new EdextFault();
        f.setMensaje("x");
        EdextWSException conCausa = new EdextWSException("x", f, new RuntimeException());
        assertSame(f, conCausa.getFaultInfo());
        assertNotNull(conCausa.getCause());
    }

    @Test
    public void testTendencias() {
        when(ico.RegistrarVisitaCurso("Java")).thenReturn(true);
        assertTrue(ws.RegistrarVisitaCurso("Java"));

        List<DTMaster> lista = new ArrayList<>();
        when(ico.ListarCursosMasVisitados(0)).thenReturn(lista);
        assertSame(lista, ws.ListarCursosMasVisitados(0));
    }
}
