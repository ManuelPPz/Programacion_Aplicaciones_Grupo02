package Logica;

import Classes.Categoria;
import Classes.Curso;
import Classes.Docente;
import Classes.EdicionCurso;
import Classes.Edi_Usu;
import Classes.Enum_Estado_inscripcion;
import Classes.Instituto;
import Classes.ProgramaDeFormacion;
import Classes.Usuario;
import Classes.UsuarioBase;
import DTsClasses.DTCurso;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import Manejadores.ManejadorCategoria;
import Manejadores.ManejadorCursos;
import Manejadores.ManejadorEdicionCurso;
import Manejadores.ManejadorInstituto;
import Manejadores.ManejadorProgramasDeFormacion;
import Manejadores.ManejadorUsuario;
import bdSQL.ConexionBD;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests de Controller. Los manejadores (singletons) se reemplazan por mocks
 * con mockStatic, asi no se toca la base de datos real.
 */
public class ControllerTest {

    private ManejadorUsuario manUsuario;
    private ManejadorCursos manCursos;
    private ManejadorInstituto manInstituto;
    private ManejadorEdicionCurso manEdicion;
    private ManejadorProgramasDeFormacion manProgramas;
    private ManejadorCategoria manCategoria;

    private MockedStatic<ManejadorUsuario> sUsuario;
    private MockedStatic<ManejadorCursos> sCursos;
    private MockedStatic<ManejadorInstituto> sInstituto;
    private MockedStatic<ManejadorEdicionCurso> sEdicion;
    private MockedStatic<ManejadorProgramasDeFormacion> sProgramas;
    private MockedStatic<ManejadorCategoria> sCategoria;

    private Controller instance;
    private final Date fecha = new Date();

    @BeforeEach
    public void setUp() {
        manUsuario = mock(ManejadorUsuario.class);
        manCursos = mock(ManejadorCursos.class);
        manInstituto = mock(ManejadorInstituto.class);
        manEdicion = mock(ManejadorEdicionCurso.class);
        manProgramas = mock(ManejadorProgramasDeFormacion.class);
        manCategoria = mock(ManejadorCategoria.class);

        sUsuario = mockStatic(ManejadorUsuario.class);
        sUsuario.when(ManejadorUsuario::GetInstance).thenReturn(manUsuario);
        sCursos = mockStatic(ManejadorCursos.class);
        sCursos.when(ManejadorCursos::GetInstance).thenReturn(manCursos);
        sInstituto = mockStatic(ManejadorInstituto.class);
        sInstituto.when(ManejadorInstituto::GetInstance).thenReturn(manInstituto);
        sEdicion = mockStatic(ManejadorEdicionCurso.class);
        sEdicion.when(ManejadorEdicionCurso::GetInstance).thenReturn(manEdicion);
        sProgramas = mockStatic(ManejadorProgramasDeFormacion.class);
        sProgramas.when(ManejadorProgramasDeFormacion::GetInstance).thenReturn(manProgramas);
        sCategoria = mockStatic(ManejadorCategoria.class);
        sCategoria.when(ManejadorCategoria::GetInstance).thenReturn(manCategoria);

        instance = new Controller();
    }

    @AfterEach
    public void tearDown() {
        sUsuario.close();
        sCursos.close();
        sInstituto.close();
        sEdicion.close();
        sProgramas.close();
        sCategoria.close();
    }

    // ---------------------------------------------------------------- Usuario

    @Test
    public void testAgregarUsuario() throws Exception {
        Instituto ins = mock(Instituto.class);
        UsuarioBase ub = mock(UsuarioBase.class);
        when(manInstituto.BuscarInstituto("ins")).thenReturn(ins);
        when(manUsuario.CrearUsuario("nick", "nom", "ape", "a@b.com", "pw", true, fecha, ins, "img")).thenReturn(ub);
        when(manUsuario.getDT(ub)).thenReturn(mock(DTUsuarioBase.class));

        // EnviarGmail se crea con "new": se mockea para no mandar mails reales
        try (MockedConstruction<EnviarGmail> mc = mockConstruction(EnviarGmail.class)) {
            instance.AgregarUsuario("nick", "nom", "ape", "a@b.com", "pw", fecha, true, "ins", "img");
            assertEquals(1, mc.constructed().size());
        }
        verify(manUsuario).Add(ub);
    }

    @Test
    public void testAgregarUsuarioIOException() throws Exception {
        Instituto ins = mock(Instituto.class);
        when(manInstituto.BuscarInstituto("ins")).thenReturn(ins);
        when(manUsuario.CrearUsuario("nick", "nom", "ape", "a@b.com", "pw", false, fecha, ins, "img"))
                .thenThrow(new IOException("fallo"));

        assertDoesNotThrow(() -> instance.AgregarUsuario("nick", "nom", "ape", "a@b.com", "pw", fecha, false, "ins", "img"));
        verify(manUsuario, never()).Add(any());
    }

    @Test
    public void testConsultarUsuarioExiste() {
        UsuarioBase ub = mock(UsuarioBase.class);
        DTUsuarioBase dt = mock(DTUsuarioBase.class);
        when(manUsuario.BuscarUsuario("nick")).thenReturn(ub);
        when(manUsuario.getDT(ub)).thenReturn(dt);

        assertSame(dt, instance.ConsultarUsuario("nick"));
    }

    @Test
    public void testConsultarUsuarioNoExiste() {
        when(manUsuario.BuscarUsuario("nick")).thenReturn(null);
        assertNull(instance.ConsultarUsuario("nick"));
    }

    @Test
    public void testModificarUsuario() throws Exception {
        Instituto ins = mock(Instituto.class);
        when(manInstituto.BuscarInstituto("ins")).thenReturn(ins);

        instance.ModificarUsuario("nick", "nom", "ape", "pw", false, fecha, "ins", "img");

        verify(manUsuario).ModificarDatosUsuario("nick", "nom", "ape", "pw", true, fecha, ins, "img");
    }

    @Test
    public void testModificarUsuarioIOException() throws Exception {
        Instituto ins = mock(Instituto.class);
        when(manInstituto.BuscarInstituto("ins")).thenReturn(ins);
        doThrow(new IOException("fallo")).when(manUsuario)
                .ModificarDatosUsuario("nick", "nom", "ape", "pw", true, fecha, ins, "img");

        assertDoesNotThrow(() -> instance.ModificarUsuario("nick", "nom", "ape", "pw", true, fecha, "ins", "img"));
    }

    @Test
    public void testSeguirUsuario() {
        UsuarioBase u1 = mock(UsuarioBase.class);
        UsuarioBase u2 = mock(UsuarioBase.class);
        when(manUsuario.BuscarUsuario("n1")).thenReturn(u1);
        when(manUsuario.BuscarUsuario("n2")).thenReturn(u2);

        instance.SeguirUsuario("n1", "n2");

        verify(manUsuario).SeguirUsuarios(u1, u2);
    }

    @Test
    public void testDejarDeSeguir() {
        UsuarioBase u1 = mock(UsuarioBase.class);
        UsuarioBase u2 = mock(UsuarioBase.class);
        when(manUsuario.BuscarUsuario("n1")).thenReturn(u1);
        when(manUsuario.BuscarUsuario("n2")).thenReturn(u2);

        instance.DejarDeSeguir("n1", "n2");

        verify(manUsuario).DejarDeSeguir(u1, u2);
    }

    // ------------------------------------------------------------------ Curso

    @Test
    public void testAltaCursoNuevo() throws Exception {
        UsuarioBase doc = mock(UsuarioBase.class);
        Categoria cat = mock(Categoria.class);
        Curso nuevo = mock(Curso.class);
        List<String> previas = new ArrayList<>();
        when(manCursos.BuscarCurso("curso")).thenReturn(null);
        when(manUsuario.BuscarUsuario("doc")).thenReturn(doc);
        when(manCategoria.BuscarCategoria("cat")).thenReturn(cat);
        when(manCursos.CrearCurso(any(Instituto.class), anyString(), anyString(), anyInt(), anyFloat(), anyInt(),
                anyString(), any(Date.class), anyList(), any(UsuarioBase.class), anyList())).thenReturn(nuevo);

        instance.AltaCurso("ins", "curso", "desc", 3, 10f, 5, "url", previas, fecha, "doc", Arrays.asList("cat"));

        verify(manUsuario).AddCurso(doc, nuevo);
        verify(manCursos).Add(nuevo);
        verify(manCursos, never()).ModificarCurso(any(), any(), anyInt(), anyFloat(), anyInt(), any(), any(), any(), any(), any());
    }

    @Test
    public void testAltaCursoExistente() throws Exception {
        UsuarioBase docNuevo = mock(UsuarioBase.class);
        Docente docViejo = mock(Docente.class);
        Curso existente = mock(Curso.class);
        when(existente.getMiDocente()).thenReturn(docViejo);
        when(manCursos.BuscarCurso("curso")).thenReturn(existente);
        when(manUsuario.BuscarUsuario("doc")).thenReturn(docNuevo);

        instance.AltaCurso("ins", "curso", "desc", 3, 10f, 5, "url", new ArrayList<>(), fecha, "doc", new ArrayList<>());

        verify(manUsuario).RemoveCurso(docViejo, existente);
        verify(manUsuario).AddCurso(docNuevo, existente);
        verify(manCursos).ModificarCurso(eq(existente), eq("desc"), eq(3), eq(10f), eq(5), eq("url"),
                eq(fecha), anyList(), eq(docNuevo), anyList());
        verify(manCursos, never()).Add(any());
    }

    @Test
    public void testConsultaCursoExiste() {
        Curso c = mock(Curso.class);
        DTCurso dt = mock(DTCurso.class);
        when(manCursos.BuscarCurso("curso")).thenReturn(c);
        when(manCursos.getDT(c)).thenReturn(dt);

        assertSame(dt, instance.ConsultaCurso("curso"));
    }

    @Test
    public void testConsultaCursoNoExiste() {
        when(manCursos.BuscarCurso("curso")).thenReturn(null);
        assertNull(instance.ConsultaCurso("curso"));
    }

    // ---------------------------------------------------------------- Edicion

    @Test
    public void testAltaEdicionCursoNuevaConDocentes() throws Exception {
        Instituto ins = mock(Instituto.class);
        Curso c = mock(Curso.class);
        List<EdicionCurso> ediciones = new ArrayList<>();
        when(c.getEdiciones()).thenReturn(ediciones);
        Docente d1 = mock(Docente.class);
        EdicionCurso ec = mock(EdicionCurso.class);
        when(manInstituto.BuscarInstituto("ins")).thenReturn(ins);
        when(manCursos.BuscarCurso("curso")).thenReturn(c);
        when(manEdicion.BuscarEdicion("ed")).thenReturn(null);
        when(manUsuario.BuscarUsuario("d1")).thenReturn(d1);
        when(manUsuario.BuscarUsuario("noExiste")).thenReturn(null);
        when(manEdicion.CrearEdicion(any(), any(), any(), any(), any(), anyInt(), any(), anyList())).thenReturn(ec);

        instance.AltaEdicionCurso("ins", "curso", "ed", fecha, fecha, 10, Arrays.asList("d1", "noExiste"), fecha);

        verify(manUsuario).AddEdicion(d1, ec);
        verify(manEdicion).Add(ec);
        assertTrue(ediciones.contains(ec));
    }

    @Test
    public void testAltaEdicionCursoNuevaSinCursoNiDocentes() throws Exception {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(manCursos.BuscarCurso("curso")).thenReturn(null);
        when(manEdicion.BuscarEdicion("ed")).thenReturn(null);
        when(manEdicion.CrearEdicion(any(), any(), any(), any(), any(), anyInt(), any(), anyList())).thenReturn(ec);

        instance.AltaEdicionCurso("ins", "curso", "ed", fecha, fecha, 10, null, fecha);

        verify(manEdicion).Add(ec);
        verify(manUsuario, never()).AddEdicion(any(), any());
    }

    @Test
    public void testAltaEdicionCursoExistente() throws Exception {
        EdicionCurso existente = mock(EdicionCurso.class);
        Docente viejo = mock(Docente.class);
        Docente nuevo = mock(Docente.class);
        when(existente.getMisDocentes()).thenReturn(new ArrayList<>(Arrays.asList(viejo)));
        when(manEdicion.BuscarEdicion("ed")).thenReturn(existente);
        when(manUsuario.BuscarUsuario("nuevo")).thenReturn(nuevo);
        when(manUsuario.BuscarUsuario("noExiste")).thenReturn(null);

        instance.AltaEdicionCurso("ins", "curso", "ed", fecha, fecha, 10, Arrays.asList("nuevo", "noExiste"), fecha);

        verify(manUsuario).RemoveEdicion(viejo, existente);
        verify(manUsuario).AddEdicion(nuevo, existente);
        verify(manEdicion).ModificarDatos(eq(existente), any(), any(), eq(10), any(), anyList());
        verify(manEdicion, never()).Add(any());
    }

    @Test
    public void testAltaEdicionCursoExistenteDocentesNull() throws Exception {
        EdicionCurso existente = mock(EdicionCurso.class);
        when(existente.getMisDocentes()).thenReturn(new ArrayList<>());
        when(manEdicion.BuscarEdicion("ed")).thenReturn(existente);

        instance.AltaEdicionCurso("ins", "curso", "ed", fecha, fecha, 10, null, fecha);

        verify(manEdicion).ModificarDatos(eq(existente), any(), any(), eq(10), any(), anyList());
    }

    @Test
    public void testConsultaEdicionCursoExiste() {
        EdicionCurso ec = mock(EdicionCurso.class);
        DTEdicionCurso dt = mock(DTEdicionCurso.class);
        when(manEdicion.BuscarEdicion("ed")).thenReturn(ec);
        when(manEdicion.getDT(ec)).thenReturn(dt);

        assertSame(dt, instance.ConsultaEdicionCurso("ed"));
    }

    @Test
    public void testConsultaEdicionCursoNoExiste() {
        when(manEdicion.BuscarEdicion("ed")).thenReturn(null);
        assertNull(instance.ConsultaEdicionCurso("ed"));
    }

    // ----------------------------------------------------------- Inscripcion

    @Test
    public void testInscripcionAEdicionCurso() throws Exception {
        when(manUsuario.BuscarUsuario("nick")).thenReturn(mock(Usuario.class));
        when(manEdicion.BuscarEdicion("ed")).thenReturn(mock(EdicionCurso.class));

        instance.InscripcionAEdicionCurso("ed", "nick", fecha);

        verify(manEdicion).AddUsuarioInscripto(any(Edi_Usu.class));
    }

    @Test
    public void testInscripcionAEdicionCursoUsuarioNull() throws Exception {
        when(manUsuario.BuscarUsuario("nick")).thenReturn(null);
        when(manEdicion.BuscarEdicion("ed")).thenReturn(mock(EdicionCurso.class));

        instance.InscripcionAEdicionCurso("ed", "nick", fecha);

        verify(manEdicion, never()).AddUsuarioInscripto(any());
    }

    @Test
    public void testInscripcionAEdicionCursoEdicionNull() throws Exception {
        when(manUsuario.BuscarUsuario("nick")).thenReturn(mock(Usuario.class));
        when(manEdicion.BuscarEdicion("ed")).thenReturn(null);

        instance.InscripcionAEdicionCurso("ed", "nick", fecha);

        verify(manEdicion, never()).AddUsuarioInscripto(any());
    }

    @Test
    public void testInscripcionAEdicionCursoExcepcion() throws Exception {
        when(manUsuario.BuscarUsuario("nick")).thenReturn(mock(Usuario.class));
        when(manEdicion.BuscarEdicion("ed")).thenReturn(mock(EdicionCurso.class));
        doThrow(new RuntimeException("fallo")).when(manEdicion).AddUsuarioInscripto(any(Edi_Usu.class));

        assertDoesNotThrow(() -> instance.InscripcionAEdicionCurso("ed", "nick", fecha));
    }

    @Test
    public void testAceptarEstudiantesAEdicionCurso() {
        Enum_Estado_inscripcion estado = Enum_Estado_inscripcion.values()[0];
        List<Object[]> lista = new ArrayList<>();
        lista.add(new Object[]{"est1", estado});
        lista.add(new Object[]{"est2", estado});

        instance.AceptarEstudiantesAEdicionCurso(lista, "ed");

        verify(manEdicion).ModificarEstadoDeInscripcion("est1", estado, "ed");
        verify(manEdicion).ModificarEstadoDeInscripcion("est2", estado, "ed");
    }

    // --------------------------------------------------------------- Programa

    @Test
    public void testCrearProgramasDeFormacionNuevo() throws Exception {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(manProgramas.BuscarPrograma("prog")).thenReturn(null);
        when(manProgramas.CrearPrograma(eq("prog"), eq("desc"), any(), eq(fecha))).thenReturn(pdf);

        instance.CrearProgramasDeFormacion("prog", "desc", fecha, fecha, fecha);

        verify(manProgramas).Add(pdf);
        verify(manProgramas, never()).ModificarDatos(any(), any(), any(), any());
    }

    @Test
    public void testCrearProgramasDeFormacionExistente() throws Exception {
        when(manProgramas.BuscarPrograma("prog")).thenReturn(mock(ProgramaDeFormacion.class));

        instance.CrearProgramasDeFormacion("prog", "desc", fecha, fecha, fecha);

        verify(manProgramas).ModificarDatos(eq("prog"), eq("desc"), any(), eq(fecha));
        verify(manProgramas, never()).Add(any());
    }

    @Test
    public void testAgregarCursoAProgramas() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        Curso c = mock(Curso.class);
        when(manProgramas.BuscarPrograma("prog")).thenReturn(pdf);
        when(manCursos.BuscarCurso("c1")).thenReturn(c);
        when(manCursos.BuscarCurso("noExiste")).thenReturn(null);

        instance.AgregarCursoAProgramas("prog", Arrays.asList("c1", "noExiste"));

        verify(manProgramas, times(1)).AddCurso(pdf, c);
        verify(c).AddPrograma(pdf);
    }

    @Test
    public void testAgregarCursoAProgramasProgramaNull() {
        when(manProgramas.BuscarPrograma("prog")).thenReturn(null);

        instance.AgregarCursoAProgramas("prog", Arrays.asList("c1"));

        verify(manProgramas, never()).AddCurso(any(), any());
    }

    @Test
    public void testAgregarCursoAProgramasCursosNull() {
        when(manProgramas.BuscarPrograma("prog")).thenReturn(mock(ProgramaDeFormacion.class));

        instance.AgregarCursoAProgramas("prog", null);

        verify(manProgramas, never()).AddCurso(any(), any());
    }

    @Test
    public void testConsultaProgramaFormacionExiste() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        DTProgramaForm dt = mock(DTProgramaForm.class);
        when(manProgramas.BuscarPrograma("prog")).thenReturn(pdf);
        doReturn(dt).when(manProgramas).getDT(pdf);

        assertSame(dt, instance.ConsultaProgramaFormacion("prog"));
    }

    @Test
    public void testConsultaProgramaFormacionNoExiste() {
        when(manProgramas.BuscarPrograma("prog")).thenReturn(null);
        assertNull(instance.ConsultaProgramaFormacion("prog"));
    }

    @Test
    public void testActualizarPrograma() {
        // El metodo esta vacio: solo se verifica que no falle
        assertDoesNotThrow(() -> instance.ActualizarPrograma("prog", "desc", fecha, fecha));
    }

    @Test
    public void testInscripcionUsuarioAProgramas() throws Exception {
        when(manUsuario.BuscarUsuario("nick")).thenReturn(mock(Usuario.class));
        when(manProgramas.BuscarPrograma("prog")).thenReturn(mock(ProgramaDeFormacion.class));

        instance.InscripcionUsuarioAProgramas("prog", "nick", fecha);

        verify(manUsuario).InscribirUsuarioAPrograma(any());
        verify(manProgramas).AddUsuarioInscripto(any());
    }

    @Test
    public void testInscripcionUsuarioAProgramasExcepcion() throws Exception {
        when(manUsuario.BuscarUsuario("nick")).thenReturn(mock(Usuario.class));
        when(manProgramas.BuscarPrograma("prog")).thenReturn(mock(ProgramaDeFormacion.class));
        doThrow(new RuntimeException("fallo")).when(manProgramas).AddUsuarioInscripto(any());

        assertDoesNotThrow(() -> instance.InscripcionUsuarioAProgramas("prog", "nick", fecha));
    }

    // ---------------------------------------------------- ExistePrograma (SQL)

    private boolean existeProgramaConConteo(Boolean hayFila, int conteo) throws Exception {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(hayFila);
        when(rs.getInt(1)).thenReturn(conteo);

        try (MockedStatic<ConexionBD> bd = mockStatic(ConexionBD.class)) {
            bd.when(ConexionBD::getConexion).thenReturn(con);
            return instance.ExistePrograma("prog");
        }
    }

    @Test
    public void testExisteProgramaTrue() throws Exception {
        assertTrue(existeProgramaConConteo(true, 1));
    }

    @Test
    public void testExisteProgramaConteoCero() throws Exception {
        assertFalse(existeProgramaConConteo(true, 0));
    }

    @Test
    public void testExisteProgramaSinFilas() throws Exception {
        assertFalse(existeProgramaConConteo(false, 0));
    }

    @Test
    public void testExisteProgramaSQLException() throws Exception {
        try (MockedStatic<ConexionBD> bd = mockStatic(ConexionBD.class)) {
            bd.when(ConexionBD::getConexion).thenThrow(new SQLException("fallo"));
            assertFalse(instance.ExistePrograma("prog"));
        }
    }

    // --------------------------------------------------------------- Instituto

    @Test
    public void testAltaInstituto() throws Exception {
        Instituto i = mock(Instituto.class);
        when(manInstituto.CreaInstituto("ins")).thenReturn(i);

        instance.AltaInstituto("ins");

        verify(manInstituto).Add(i);
    }

    // -------------------------------------------------------------- Verificar

    @Test
    public void testVerificarCurso() {
        when(manCursos.BuscarCurso("si")).thenReturn(mock(Curso.class));
        when(manCursos.BuscarCurso("no")).thenReturn(null);
        assertTrue(instance.VerificarCurso("si"));
        assertFalse(instance.VerificarCurso("no"));
    }

    @Test
    public void testVerificarInstituto() {
        when(manInstituto.BuscarInstituto("si")).thenReturn(mock(Instituto.class));
        when(manInstituto.BuscarInstituto("no")).thenReturn(null);
        assertTrue(instance.VerificarInstituto("si"));
        assertFalse(instance.VerificarInstituto("no"));
    }

    @Test
    public void testVerificarEdicion() {
        when(manEdicion.BuscarEdicion("si")).thenReturn(mock(EdicionCurso.class));
        when(manEdicion.BuscarEdicion("no")).thenReturn(null);
        assertTrue(instance.VerificarEdicion("si"));
        assertFalse(instance.VerificarEdicion("no"));
    }

    @Test
    public void testVerificarPrograma() {
        when(manProgramas.BuscarPrograma("si")).thenReturn(mock(ProgramaDeFormacion.class));
        when(manProgramas.BuscarPrograma("no")).thenReturn(null);
        assertTrue(instance.VerificarPrograma("si"));
        assertFalse(instance.VerificarPrograma("no"));
    }

    @Test
    public void testVerificarCategoria() {
        when(manCategoria.BuscarCategoria("si")).thenReturn(mock(Categoria.class));
        when(manCategoria.BuscarCategoria("no")).thenReturn(null);
        assertTrue(instance.VerificarCategoria("si"));
        assertFalse(instance.VerificarCategoria("no"));
    }

    @Test
    public void testVerificarCorreo() {
        when(manUsuario.VerificarUsuario("si@a.com")).thenReturn(mock(UsuarioBase.class));
        when(manUsuario.VerificarUsuario("no@a.com")).thenReturn(null);
        assertTrue(instance.VerificarCorreo("si@a.com"));
        assertFalse(instance.VerificarCorreo("no@a.com"));
    }

    @Test
    public void testVerificarNickname() {
        when(manUsuario.VerificarUsuario("si")).thenReturn(mock(UsuarioBase.class));
        when(manUsuario.VerificarUsuario("no")).thenReturn(null);
        assertTrue(instance.VerificarNickname("si"));
        assertFalse(instance.VerificarNickname("no"));
    }

    // ----------------------------------------------------------------- Listar

    @Test
    public void testListarClaseInstituto() {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manInstituto).getDTList();
        assertSame(lista, instance.ListarClase(EnumDT.DT_INSTITUTO));
    }

    @Test
    public void testListarClaseCurso() {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manCursos).getDTList();
        assertSame(lista, instance.ListarClase(EnumDT.DT_CURSO));
    }

    @Test
    public void testListarClaseUsuario() {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manUsuario).getDTList();
        assertSame(lista, instance.ListarClase(EnumDT.DT_USUARIO));
    }

    @Test
    public void testListarClasePrograma() {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manProgramas).getDTList();
        assertSame(lista, instance.ListarClase(EnumDT.DT_PROGRAMA));
    }

    @Test
    public void testListarClaseCategoria() throws Exception {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manCategoria).getDTList();
        assertSame(lista, instance.ListarClase(EnumDT.DT_CATEGORIA));
    }

    @Test
    public void testListarClaseCategoriaExcepcion() throws Exception {
        when(manCategoria.getDTList()).thenThrow(new Exception("fallo"));
        List<DTMaster> result = instance.ListarClase(EnumDT.DT_CATEGORIA);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testListarClaseNull() {
        assertTrue(instance.ListarClase(null).isEmpty());
    }

    @Test
    public void testListarCursos() {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manCursos).getDTLIst("ins");
        assertSame(lista, instance.ListarCursos("ins"));
    }

    @Test
    public void testListarEdiciones() {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manEdicion).getDTLIst("curso");
        assertSame(lista, instance.ListarEdiciones("curso"));
    }

    @Test
    public void testListarEdicionesPrioritarias() {
        EdicionCurso ec = mock(EdicionCurso.class);
        Curso c = mock(Curso.class);
        when(ec.getCurso()).thenReturn(c);
        List<Edi_Usu> inscripciones = new ArrayList<>();
        List<DTMaster> ordenada = new ArrayList<>();
        when(manEdicion.BuscarEdicion("ed")).thenReturn(ec);
        when(manEdicion.getMisInscripciones(ec)).thenReturn(inscripciones);
        doReturn(ordenada).when(manEdicion).OrdenarInscripcionesPorPrioridad(inscripciones, c);

        assertSame(ordenada, instance.ListarEdicionesPrioritarias("ed"));
    }

    @Test
    public void testListarDocentes() {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manUsuario).getDTList("ins");
        assertSame(lista, instance.ListarDocentes("ins"));
    }

    @Test
    public void testListarProgramaDeForm() {
        List<DTMaster> lista = new ArrayList<>();
        doReturn(lista).when(manProgramas).getDTList();
        assertSame(lista, instance.ListarProgramaDeForm());
    }

    @Test
    public void testListarInscripcionesEstudiante() {
        Usuario u = mock(Usuario.class);
        Edi_Usu eu = mock(Edi_Usu.class);
        DTMaster dt = mock(DTMaster.class);
        when(u.getMisInscripciones()).thenReturn(Collections.singletonList(eu));
        doReturn(dt).when(eu).getMyDT();
        when(manUsuario.BuscarUsuario("est")).thenReturn(u);

        List<DTMaster> result = instance.ListarInscripciones("est");

        assertEquals(1, result.size());
        assertSame(dt, result.get(0));
    }

    @Test
    public void testListarInscripcionesNoEsEstudiante() {
        when(manUsuario.BuscarUsuario("doc")).thenReturn(mock(Docente.class));
        assertTrue(instance.ListarInscripciones("doc").isEmpty());
    }

    @Test
    public void testListarInscripcionesNull() {
        when(manUsuario.BuscarUsuario("x")).thenReturn(null);
        assertTrue(instance.ListarInscripciones("x").isEmpty());
    }

    // ------------------------------------------------------------------ Otros

    @Test
    public void testGenerateRandPassword() {
        for (int i = 0; i < 50; i++) {
            String pass = instance.GenerateRandPassword();
            assertEquals(12, pass.length());
            assertTrue(pass.startsWith(".-"));
            assertTrue(pass.substring(2).matches("[A-Za-z0-9]{10}"));
        }
    }

    @Test
    public void testAltaCategoria() throws Exception {
        Categoria cat = mock(Categoria.class);
        when(manCategoria.CrearCategoria("cat")).thenReturn(cat);

        instance.AltaCategoria("cat");

        verify(manCategoria).Add(cat);
    }

    @Test
    public void testAltaCategoriaExcepcion() throws Exception {
        Categoria cat = mock(Categoria.class);
        when(manCategoria.CrearCategoria("cat")).thenReturn(cat);
        doThrow(new Exception("fallo")).when(manCategoria).Add(cat);

        assertDoesNotThrow(() -> instance.AltaCategoria("cat"));
    }

    @Test
    public void testConsultaUsuarioAvanzadaExiste() {
        UsuarioBase ub = mock(UsuarioBase.class);
        DTUsuarioBase dt = mock(DTUsuarioBase.class);
        when(manUsuario.BusquedaAvanzada("id", "pw")).thenReturn(ub);
        when(manUsuario.getDT(ub)).thenReturn(dt);

        assertSame(dt, instance.ConsultaUsuarioAvanzada("id", "pw"));
    }

    @Test
    public void testConsultaUsuarioAvanzadaNoExiste() {
        when(manUsuario.BusquedaAvanzada("id", "pw")).thenReturn(null);
        assertNull(instance.ConsultaUsuarioAvanzada("id", "pw"));
    }
}