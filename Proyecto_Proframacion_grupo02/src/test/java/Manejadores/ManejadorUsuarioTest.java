package Manejadores;

import Classes.Docente;
import Classes.Edi_Usu;
import Classes.EdicionCurso;
import Classes.Curso;
import Classes.Instituto;
import Classes.Prog_Usu;
import Classes.ProgramaDeFormacion;
import Classes.Usuario;
import Classes.UsuarioBase;
import DTsClasses.DTDocente;
import DTsClasses.DTMaster;
import DTsClasses.DTUsuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import util.JPAUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios de ManejadorUsuario (JPAUtil mockeado, sin base de datos).
 */
public class ManejadorUsuarioTest {

    private MockedStatic<JPAUtil> jpaUtil;
    private EntityManager em;
    private EntityTransaction tx;
    private ManejadorUsuario manejador;

    @BeforeEach
    public void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);
        when(em.isOpen()).thenReturn(true);

        jpaUtil = mockStatic(JPAUtil.class);
        jpaUtil.when(JPAUtil::getEntityManager).thenReturn(em);

        manejador = ManejadorUsuario.GetInstance();
    }

    @AfterEach
    public void tearDown() {
        jpaUtil.close();
    }

    @SuppressWarnings("unchecked")
    private <T> TypedQuery<T> queryDe(Class<T> clazz) {
        TypedQuery<T> q = mock(TypedQuery.class, RETURNS_SELF);
        when(em.createQuery(anyString(), eq(clazz))).thenReturn(q);
        return q;
    }

    private UsuarioBase usuarioBase(String nick, String correo, String pass) {
        UsuarioBase ub = mock(UsuarioBase.class);
        when(ub.getNickname()).thenReturn(nick);
        when(ub.getCorreo()).thenReturn(correo);
        when(ub.getPassword()).thenReturn(pass);
        return ub;
    }

    // ===================== GetInstance / CrearUsuario =====================

    @Test
    public void testGetInstance() {
        assertNotNull(ManejadorUsuario.GetInstance());
        assertSame(ManejadorUsuario.GetInstance(), ManejadorUsuario.GetInstance());
    }

    @Test
    public void testCrearUsuarioDocente() throws Exception {
        try (MockedConstruction<Docente> construido = mockConstruction(Docente.class)) {
            UsuarioBase result = manejador.CrearUsuario("n", "nom", "ape", "c@c.com", "pw", true,
                    new Date(), mock(Instituto.class), "img");

            assertTrue(result instanceof Docente);
            assertSame(construido.constructed().get(0), result);
        }
    }

    @Test
    public void testCrearUsuarioEstudiante() throws Exception {
        try (MockedConstruction<Usuario> construido = mockConstruction(Usuario.class)) {
            UsuarioBase result = manejador.CrearUsuario("n", "nom", "ape", "c@c.com", "pw", false,
                    new Date(), null, "img");

            assertTrue(result instanceof Usuario);
            assertSame(construido.constructed().get(0), result);
        }
    }

    // ===================== ModificarDatosUsuario =====================

    @Test
    public void testModificarDatosUsuarioInexistente() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        doReturn(null).when(spy).BuscarUsuario("nick");

        spy.ModificarDatosUsuario("nick", "n", "a", "pw", false, new Date(), null, "img");

        verify(em, never()).merge(any());
    }

    @Test
    public void testModificarDatosDocenteConPasswordNueva() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        Docente d = mock(Docente.class);
        Instituto ins = mock(Instituto.class);
        Date f = new Date();
        doReturn(d).when(spy).BuscarUsuario("nick");

        spy.ModificarDatosUsuario("nick", "n", "a", "nueva", true, f, ins, "img");

        verify(d).ModificarMisDatos("n", "a", "nueva", f, "img", ins);
        verify(em).merge(d);
        verify(tx).commit();
    }

    @Test
    public void testModificarDatosDocenteConservaPasswordVacia() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        Docente d = mock(Docente.class);
        when(d.getPassword()).thenReturn("vieja");
        Date f = new Date();
        doReturn(d).when(spy).BuscarUsuario("nick");

        spy.ModificarDatosUsuario("nick", "n", "a", "", true, f, null, "img");

        verify(d).ModificarMisDatos("n", "a", "vieja", f, "img", null);
    }

    @Test
    public void testModificarDatosUsuarioConPasswordNueva() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        Usuario u = mock(Usuario.class);
        Date f = new Date();
        doReturn(u).when(spy).BuscarUsuario("nick");

        spy.ModificarDatosUsuario("nick", "n", "a", "nueva", false, f, null, "img");

        verify(u).ModificarMisDatos("n", "a", "nueva", f, "img");
        verify(em).merge(u);
    }

    @Test
    public void testModificarDatosUsuarioConservaPasswordVacia() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        Usuario u = mock(Usuario.class);
        when(u.getPassword()).thenReturn("vieja");
        Date f = new Date();
        doReturn(u).when(spy).BuscarUsuario("nick");

        spy.ModificarDatosUsuario("nick", "n", "a", "", false, f, null, "img");

        verify(u).ModificarMisDatos("n", "a", "vieja", f, "img");
    }

    @Test
    public void testModificarDatosFlagDocenteConUsuarioEstudiante() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        Usuario u = mock(Usuario.class);
        Date f = new Date();
        doReturn(u).when(spy).BuscarUsuario("nick");

        spy.ModificarDatosUsuario("nick", "n", "a", "pw", true, f, null, "img");

        verify(u).ModificarMisDatos("n", "a", "pw", f, "img");
    }

    @Test
    public void testModificarDatosUsuarioBaseGenerico() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        UsuarioBase ub = mock(UsuarioBase.class);
        doReturn(ub).when(spy).BuscarUsuario("nick");

        spy.ModificarDatosUsuario("nick", "n", "a", "pw", false, new Date(), null, "img");

        verify(em).merge(ub);
    }

    @Test
    public void testModificarDatosErrorHaceRollback() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        UsuarioBase ub = mock(UsuarioBase.class);
        doReturn(ub).when(spy).BuscarUsuario("nick");
        when(em.merge(ub)).thenThrow(new RuntimeException("boom"));

        spy.ModificarDatosUsuario("nick", "n", "a", "pw", false, new Date(), null, "img");

        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testModificarDatosErrorSinTransaccionActiva() throws Exception {
        ManejadorUsuario spy = spy(manejador);
        UsuarioBase ub = mock(UsuarioBase.class);
        doReturn(ub).when(spy).BuscarUsuario("nick");
        when(em.merge(ub)).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);

        spy.ModificarDatosUsuario("nick", "n", "a", "pw", false, new Date(), null, "img");

        verify(tx, never()).rollback();
    }

    // ===================== Add =====================

    @Test
    public void testAddDocenteConInstituto() throws Exception {
        Docente d = mock(Docente.class, RETURNS_DEEP_STUBS);
        when(d.getInstituto().getNombre()).thenReturn("INCO");
        Instituto persistente = mock(Instituto.class);
        when(em.find(Instituto.class, "INCO")).thenReturn(persistente);

        manejador.Add(d);

        verify(d).setInstituto(persistente);
        verify(em).persist(d);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testAddDocenteSinInstituto() throws Exception {
        Docente d = mock(Docente.class);
        when(d.getInstituto()).thenReturn(null);

        manejador.Add(d);

        verify(em, never()).find(eq(Instituto.class), any());
        verify(em).persist(d);
    }

    @Test
    public void testAddUsuarioEstudiante() throws Exception {
        Usuario u = mock(Usuario.class);

        manejador.Add(u);

        verify(em).persist(u);
        verify(tx).commit();
    }

    @Test
    public void testAddErrorHaceRollback() {
        Usuario u = mock(Usuario.class);
        doThrow(new RuntimeException("boom")).when(em).persist(u);

        Exception ex = assertThrows(Exception.class, () -> manejador.Add(u));

        assertTrue(ex.getMessage().contains("Error al guardar el usuario"));
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddErrorSinTransaccionActiva() {
        Usuario u = mock(Usuario.class);
        doThrow(new RuntimeException("boom")).when(em).persist(u);
        when(tx.isActive()).thenReturn(false);

        assertThrows(Exception.class, () -> manejador.Add(u));
        verify(tx, never()).rollback();
    }

    // ===================== BuscarUsuario =====================

    @Test
    public void testBuscarUsuarioNicknameNuloOVacio() {
        assertNull(manejador.BuscarUsuario(null));
        assertNull(manejador.BuscarUsuario("   "));
        verifyNoInteractions(em);
    }

    @Test
    public void testBuscarUsuarioDocenteHidratado() {
        Docente d = mock(Docente.class);
        when(d.getInstituto()).thenReturn(mock(Instituto.class));
        Curso c = mock(Curso.class);
        when(c.getProgramas()).thenReturn(List.of(mock(ProgramaDeFormacion.class)));
        when(d.getCursos()).thenReturn(List.of(c));
        when(em.find(UsuarioBase.class, "nick")).thenReturn(d);

        UsuarioBase result = manejador.BuscarUsuario(" nick ");

        assertSame(d, result);
        verify(em).refresh(d);
        verify(em).refresh(c);
        verify(em).close();
    }

    @Test
    public void testBuscarUsuarioDocenteConColeccionesNulas() {
        Docente d = mock(Docente.class);
        when(d.getInstituto()).thenReturn(null);
        when(d.getEdiciones()).thenReturn(null);
        when(d.getCursos()).thenReturn(null);
        when(em.find(UsuarioBase.class, "nick")).thenReturn(d);

        assertSame(d, manejador.BuscarUsuario("nick"));
    }

    @Test
    public void testBuscarUsuarioDocenteCursoSinProgramas() {
        Docente d = mock(Docente.class);
        Curso c = mock(Curso.class);
        when(c.getProgramas()).thenReturn(null);
        when(d.getCursos()).thenReturn(List.of(c));
        when(em.find(UsuarioBase.class, "nick")).thenReturn(d);

        assertSame(d, manejador.BuscarUsuario("nick"));
    }

    @Test
    public void testBuscarUsuarioEstudiante() {
        Usuario u = mock(Usuario.class);
        when(u.getMisInscripciones()).thenReturn(new ArrayList<>());
        when(em.find(UsuarioBase.class, "nick")).thenReturn(u);

        assertSame(u, manejador.BuscarUsuario("nick"));
    }

    @Test
    public void testBuscarUsuarioEstudianteSinInscripciones() {
        Usuario u = mock(Usuario.class);
        when(u.getMisInscripciones()).thenReturn(null);
        when(em.find(UsuarioBase.class, "nick")).thenReturn(u);

        assertSame(u, manejador.BuscarUsuario("nick"));
    }

    @Test
    public void testBuscarUsuarioTipoGenerico() {
        UsuarioBase ub = mock(UsuarioBase.class);
        when(em.find(UsuarioBase.class, "nick")).thenReturn(ub);

        assertSame(ub, manejador.BuscarUsuario("nick"));
    }

    @Test
    public void testBuscarUsuarioFallbackEncuentraEnLista() {
        ManejadorUsuario spy = spy(manejador);
        when(em.find(UsuarioBase.class, "nick")).thenReturn(null);
        UsuarioBase otro = usuarioBase("otro", "o@o.com", "x");
        UsuarioBase buscado = usuarioBase("NICK", "n@n.com", "x");
        doReturn(Arrays.asList(otro, buscado)).when(spy).getList();

        assertSame(buscado, spy.BuscarUsuario("nick"));
    }

    @Test
    public void testBuscarUsuarioFallbackNoEncuentra() {
        ManejadorUsuario spy = spy(manejador);
        when(em.find(UsuarioBase.class, "nick")).thenReturn(null);
        doReturn(List.of(usuarioBase("otro", "o@o.com", "x"))).when(spy).getList();

        assertNull(spy.BuscarUsuario("nick"));
    }

    @Test
    public void testBuscarUsuarioFallbackListaNula() {
        ManejadorUsuario spy = spy(manejador);
        when(em.find(UsuarioBase.class, "nick")).thenReturn(null);
        doReturn(null).when(spy).getList();

        assertNull(spy.BuscarUsuario("nick"));
    }

    @Test
    public void testBuscarUsuarioConExcepcionUsaFallback() {
        ManejadorUsuario spy = spy(manejador);
        when(em.find(UsuarioBase.class, "nick")).thenThrow(new RuntimeException("boom"));
        UsuarioBase buscado = usuarioBase("nick", "n@n.com", "x");
        doReturn(List.of(buscado)).when(spy).getList();

        assertSame(buscado, spy.BuscarUsuario("nick"));
        verify(em).close();
    }

    // ===================== getDT =====================

    @Test
    public void testGetDTNulo() {
        assertNull(manejador.getDT(null));
    }

    @Test
    public void testGetDTTipoDesconocido() {
        assertNull(manejador.getDT(mock(UsuarioBase.class)));
    }

    private ProgramaDeFormacion programa(String nombre) {
        ProgramaDeFormacion p = mock(ProgramaDeFormacion.class);
        when(p.getNombre()).thenReturn(nombre);
        return p;
    }

    private Curso curso(String nombre, List<ProgramaDeFormacion> programas) {
        Curso c = mock(Curso.class);
        when(c.getNombre()).thenReturn(nombre);
        when(c.getProgramas()).thenReturn(programas);
        return c;
    }

    private EdicionCurso edicion(String nombre) {
        EdicionCurso e = mock(EdicionCurso.class);
        when(e.getNombre()).thenReturn(nombre);
        return e;
    }

    @Test
    public void testGetDTDocenteCompleto() {
        Docente d = mock(Docente.class, RETURNS_DEEP_STUBS);
        when(d.getInstituto().getNombre()).thenReturn("INCO");
        when(d.getNickname()).thenReturn("profe");

        // Pre-crear objetos mock para evitar el anidamiento de when(...)
        ProgramaDeFormacion p1 = programa("P1");
        ProgramaDeFormacion pNull = programa(null);

        Curso cJava = curso("Java", Arrays.asList(p1, null, pNull, p1));
        Curso cVacio = curso(null, null);
        Curso cSql = curso("SQL", null);

        EdicionCurso e1 = edicion("E1");
        EdicionCurso eNull = edicion(null);

        when(d.getCursos()).thenReturn(Arrays.asList(cJava, null, cVacio, cSql));
        when(d.getEdiciones()).thenReturn(Arrays.asList(e1, null, eNull));

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTDocente> dt = mockConstruction(DTDocente.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            assertNotNull(manejador.getDT(d));

            assertEquals(11, args.size());
            assertEquals("profe", args.get(0));
            assertEquals("INCO", args.get(6));
            assertEquals(List.of("Java", "SQL"), args.get(8));
            assertEquals(List.of("E1"), args.get(9));
            assertEquals(List.of("P1"), args.get(10));
        }
    }

    @Test
    public void testGetDTDocenteSinRelaciones() {
        Docente d = mock(Docente.class);
        when(d.getInstituto()).thenReturn(null);
        when(d.getCursos()).thenReturn(null);
        when(d.getEdiciones()).thenReturn(null);

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTDocente> dt = mockConstruction(DTDocente.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            assertNotNull(manejador.getDT(d));

            assertEquals("", args.get(6));
            assertEquals(new ArrayList<String>(), args.get(8));
            assertEquals(new ArrayList<String>(), args.get(9));
            assertEquals(new ArrayList<String>(), args.get(10));
        }
    }

    @Test
    public void testGetDTUsuarioCompleto() {
        Usuario u = mock(Usuario.class);
        when(u.getNickname()).thenReturn("alumno");

        // Pre-crear ediciones y programas
        EdicionCurso e1 = edicion("E1");
        EdicionCurso eNull = edicion(null);

        ProgramaDeFormacion p1 = programa("P1");
        ProgramaDeFormacion pNull = programa(null);

        Edi_Usu euOk = mock(Edi_Usu.class, RETURNS_DEEP_STUBS);
        when(euOk.getEdicion()).thenReturn(e1);
        Edi_Usu euSinId = mock(Edi_Usu.class);
        when(euSinId.getId()).thenReturn(null);
        Edi_Usu euSinEdicion = mock(Edi_Usu.class, RETURNS_DEEP_STUBS);
        when(euSinEdicion.getEdicion()).thenReturn(null);
        Edi_Usu euSinNombre = mock(Edi_Usu.class, RETURNS_DEEP_STUBS);
        when(euSinNombre.getEdicion()).thenReturn(eNull);
        when(u.getMisInscripciones()).thenReturn(Arrays.asList(euOk, null, euSinId, euSinEdicion, euSinNombre));

        Prog_Usu puOk = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        when(puOk.getId().getPrograma()).thenReturn(p1);
        Prog_Usu puSinId = mock(Prog_Usu.class);
        when(puSinId.getId()).thenReturn(null);
        Prog_Usu puSinPrograma = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        when(puSinPrograma.getId().getPrograma()).thenReturn(null);
        Prog_Usu puSinNombre = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        when(puSinNombre.getId().getPrograma()).thenReturn(pNull);
        when(u.getMisInscripcionesPro()).thenReturn(Arrays.asList(puOk, null, puSinId, puSinPrograma, puSinNombre));

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTUsuario> dt = mockConstruction(DTUsuario.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            assertNotNull(manejador.getDT(u));

            assertEquals(9, args.size());
            assertEquals("alumno", args.get(0));
            assertEquals(List.of("E1"), args.get(7));
            assertEquals(List.of("P1"), args.get(8));
        }
    }

    @Test
    public void testGetDTUsuarioSinInscripciones() {
        Usuario u = mock(Usuario.class);
        when(u.getMisInscripciones()).thenReturn(null);
        when(u.getMisInscripcionesPro()).thenReturn(null);

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTUsuario> dt = mockConstruction(DTUsuario.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            assertNotNull(manejador.getDT(u));

            assertEquals(new ArrayList<String>(), args.get(7));
            assertEquals(new ArrayList<String>(), args.get(8));
        }
    }

    // ===================== getList =====================

    @Test
    public void testGetList() {
        TypedQuery<Docente> qDoc = queryDe(Docente.class);
        TypedQuery<Usuario> qUsu = queryDe(Usuario.class);

        Docente conColecciones = mock(Docente.class);
        when(conColecciones.getCursos()).thenReturn(new ArrayList<>());
        when(conColecciones.getEdiciones()).thenReturn(new ArrayList<>());
        Docente sinColecciones = mock(Docente.class);
        when(sinColecciones.getCursos()).thenReturn(null);
        when(sinColecciones.getEdiciones()).thenReturn(null);
        Usuario estudiante = mock(Usuario.class);

        when(qDoc.getResultList()).thenReturn(Arrays.asList(conColecciones, sinColecciones));
        when(qUsu.getResultList()).thenReturn(List.of(estudiante));

        List<UsuarioBase> result = manejador.getList();

        assertEquals(3, result.size());
        assertSame(estudiante, result.get(2));
        verify(em).close();
    }

    @Test
    public void testGetListConExcepcionCierraEm() {
        when(em.createQuery(anyString(), eq(Docente.class))).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> manejador.getList());
        verify(em).close();
    }

    // ===================== getDTList =====================

    @Test
    public void testGetDTListSinFiltro() {
        ManejadorUsuario spy = spy(manejador);
        UsuarioBase a = mock(UsuarioBase.class);
        UsuarioBase b = mock(UsuarioBase.class);
        DTUsuario dt = mock(DTUsuario.class);
        doReturn(Arrays.asList(a, b)).when(spy).getList();
        doReturn(dt).when(spy).getDT(a);
        doReturn(dt).when(spy).getDT(b);

        assertEquals(2, spy.getDTList().size());
    }

    @Test
    public void testGetDTListSinFiltroListaNula() {
        ManejadorUsuario spy = spy(manejador);
        doReturn(null).when(spy).getList();

        assertTrue(spy.getDTList().isEmpty());
    }

    @Test
    public void testGetDTListPorInstitutoListaNula() {
        ManejadorUsuario spy = spy(manejador);
        doReturn(null).when(spy).getList();

        assertTrue(spy.getDTList("INCO").isEmpty());
    }

    @Test
    public void testGetDTListPorInstitutoNuloOVacio() {
        ManejadorUsuario spy = spy(manejador);
        doReturn(new ArrayList<UsuarioBase>()).when(spy).getList();

        assertTrue(spy.getDTList((String) null).isEmpty());
        assertTrue(spy.getDTList("   ").isEmpty());
    }

    @Test
    public void testGetDTListPorInstituto() {
        ManejadorUsuario spy = spy(manejador);
        UsuarioBase coincide = mock(UsuarioBase.class);
        UsuarioBase otroInstituto = mock(UsuarioBase.class);
        UsuarioBase sinInstituto = mock(UsuarioBase.class);
        UsuarioBase estudiante = mock(UsuarioBase.class);

        DTDocente dtCoincide = mock(DTDocente.class);
        when(dtCoincide.getInstituto()).thenReturn(" inco ");
        DTDocente dtOtro = mock(DTDocente.class);
        when(dtOtro.getInstituto()).thenReturn("OTRO");
        DTDocente dtSin = mock(DTDocente.class);
        when(dtSin.getInstituto()).thenReturn(null);
        DTUsuario dtEstudiante = mock(DTUsuario.class);

        doReturn(Arrays.asList(coincide, otroInstituto, sinInstituto, estudiante)).when(spy).getList();
        doReturn(dtCoincide).when(spy).getDT(coincide);
        doReturn(dtOtro).when(spy).getDT(otroInstituto);
        doReturn(dtSin).when(spy).getDT(sinInstituto);
        doReturn(dtEstudiante).when(spy).getDT(estudiante);

        List<DTMaster> result = spy.getDTList("INCO");

        assertEquals(1, result.size());
        assertSame(dtCoincide, result.get(0));
    }

    // ===================== Operaciones sobre Docente =====================

    @Test
    public void testAddYRemoveCurso() {
        Docente d = mock(Docente.class);
        Curso c = mock(Curso.class);

        manejador.AddCurso(d, c);
        manejador.RemoveCurso(d, c);
        manejador.AddCurso(mock(Usuario.class), c);
        manejador.RemoveCurso(mock(Usuario.class), c);

        verify(d).AddCurso(c);
        verify(d).RemoveCurso(c);
    }

    @Test
    public void testAddYRemoveEdicion() {
        Docente d = mock(Docente.class);
        EdicionCurso ec = mock(EdicionCurso.class);

        manejador.AddEdicion(d, ec);
        manejador.RemoveEdicion(d, ec);
        manejador.AddEdicion(mock(Usuario.class), ec);
        manejador.RemoveEdicion(mock(Usuario.class), ec);

        verify(d).AddEdicion(ec);
        verify(d).RemoveEdicion(ec);
    }

    // ===================== Inscripciones =====================

    @Test
    public void testInscribirUsuarioAEdicionOk() {
        Edi_Usu eu = mock(Edi_Usu.class, RETURNS_DEEP_STUBS);
        var usuario = eu.getUsuario();

        manejador.InscribirUsuarioAEdicion(eu);

        verify(usuario).AddEdicionCurso(eu);
    }

    @Test
    public void testInscribirUsuarioAEdicionCasosInvalidos() {
        manejador.InscribirUsuarioAEdicion(null);

        Edi_Usu sinId = mock(Edi_Usu.class);
        when(sinId.getId()).thenReturn(null);
        manejador.InscribirUsuarioAEdicion(sinId);

        Edi_Usu sinUsuario = mock(Edi_Usu.class, RETURNS_DEEP_STUBS);
        when(sinUsuario.getUsuario()).thenReturn(null);
        manejador.InscribirUsuarioAEdicion(sinUsuario);

        verify(sinId, never()).getUsuario();
    }

    @Test
    public void testInscribirUsuarioAProgramaOk() {
        Prog_Usu pu = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        var usuario = pu.getId().getUsuario();

        manejador.InscribirUsuarioAPrograma(pu);

        verify(usuario).AddPrograma(pu);
    }

    @Test
    public void testInscribirUsuarioAProgramaCasosInvalidos() {
        manejador.InscribirUsuarioAPrograma(null);

        Prog_Usu sinId = mock(Prog_Usu.class);
        when(sinId.getId()).thenReturn(null);
        manejador.InscribirUsuarioAPrograma(sinId);

        Prog_Usu sinUsuario = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        when(sinUsuario.getId().getUsuario()).thenReturn(null);
        manejador.InscribirUsuarioAPrograma(sinUsuario);
    }

    // ===================== Seguir / DejarDeSeguir =====================

    @Test
    public void testSeguirYDejarDeSeguir() {
        UsuarioBase ub1 = mock(UsuarioBase.class);
        UsuarioBase ub2 = mock(UsuarioBase.class);

        manejador.SeguirUsuarios(ub1, ub2);
        manejador.DejarDeSeguir(ub1, ub2);

        verify(ub1).SeguirUsuario(ub2);
        verify(ub1).DejarDeSeguir(ub2);
    }

    // ===================== ConvertirImageIconToByte (privado) =====================

    private byte[] convertir(String path) throws Exception {
        Method m = ManejadorUsuario.class.getDeclaredMethod("ConvertirImageIconToByte", String.class);
        m.setAccessible(true);
        return (byte[]) m.invoke(manejador, path);
    }

    @Test
    public void testConvertirImageIconToByte(@TempDir Path dir) throws Exception {
        assertNull(convertir(null));
        assertNull(convertir("   "));
        assertNull(convertir(dir.resolve("noexiste.png").toString()));
        assertNull(convertir(dir.toString())); // es un directorio, no un archivo

        Path archivo = dir.resolve("img.png");
        Files.write(archivo, new byte[]{1, 2, 3});
        assertArrayEquals(new byte[]{1, 2, 3}, convertir(archivo.toString()));
    }

    // ===================== BusquedaAvanzada =====================

    @Test
    public void testBusquedaAvanzadaParametrosInvalidos() {
        assertNull(manejador.BusquedaAvanzada(null, "pw"));
        assertNull(manejador.BusquedaAvanzada("  ", "pw"));
        assertNull(manejador.BusquedaAvanzada("nick", null));
    }

    @Test
    public void testBusquedaAvanzadaEncuentraEnBD() {
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        UsuarioBase ub = mock(UsuarioBase.class);
        when(q.getResultList()).thenReturn(List.of(ub));

        assertSame(ub, manejador.BusquedaAvanzada(" nick ", "pw"));
        verify(q).setParameter("valor", "nick");
        verify(q).setParameter("pass", "pw");
        verify(em).close();
    }

    @Test
    public void testBusquedaAvanzadaFallbackPorNickname() {
        ManejadorUsuario spy = spy(manejador);
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        when(q.getResultList()).thenReturn(new ArrayList<>());
        UsuarioBase otro = usuarioBase("otro", "o@o.com", "pw");
        UsuarioBase buscado = usuarioBase("nick", "n@n.com", "pw");
        doReturn(Arrays.asList(otro, buscado)).when(spy).getList();

        assertSame(buscado, spy.BusquedaAvanzada("nick", "pw"));
    }

    @Test
    public void testBusquedaAvanzadaFallbackPorCorreo() {
        ManejadorUsuario spy = spy(manejador);
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        when(q.getResultList()).thenReturn(new ArrayList<>());
        UsuarioBase buscado = usuarioBase("nick", "n@n.com", "pw");
        doReturn(List.of(buscado)).when(spy).getList();

        assertSame(buscado, spy.BusquedaAvanzada("n@n.com", "pw"));
    }

    @Test
    public void testBusquedaAvanzadaFallbackPasswordIncorrecta() {
        ManejadorUsuario spy = spy(manejador);
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        when(q.getResultList()).thenReturn(new ArrayList<>());
        doReturn(List.of(usuarioBase("nick", "n@n.com", "otra"))).when(spy).getList();

        assertNull(spy.BusquedaAvanzada("nick", "pw"));
    }

    @Test
    public void testBusquedaAvanzadaFallbackListaNula() {
        ManejadorUsuario spy = spy(manejador);
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        when(q.getResultList()).thenReturn(new ArrayList<>());
        doReturn(null).when(spy).getList();

        assertNull(spy.BusquedaAvanzada("nick", "pw"));
    }

    @Test
    public void testBusquedaAvanzadaConExcepcionUsaFallback() {
        ManejadorUsuario spy = spy(manejador);
        when(em.createQuery(anyString(), eq(UsuarioBase.class))).thenThrow(new RuntimeException("boom"));
        UsuarioBase buscado = usuarioBase("nick", "n@n.com", "pw");
        doReturn(List.of(buscado)).when(spy).getList();

        assertSame(buscado, spy.BusquedaAvanzada("nick", "pw"));
    }

    @Test
    public void testBusquedaAvanzadaEmCerrado() {
        ManejadorUsuario spy = spy(manejador);
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        when(q.getResultList()).thenReturn(new ArrayList<>());
        when(em.isOpen()).thenReturn(false);
        doReturn(new ArrayList<UsuarioBase>()).when(spy).getList();

        assertNull(spy.BusquedaAvanzada("nick", "pw"));
        verify(em, never()).close();
    }

    // ===================== VerificarUsuario =====================

    @Test
    public void testVerificarUsuarioEncontrado() {
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        UsuarioBase ub = mock(UsuarioBase.class);
        when(q.getSingleResult()).thenReturn(ub);

        assertSame(ub, manejador.VerificarUsuario("nick"));
        verify(em).close();
    }

    @Test
    public void testVerificarUsuarioNoEncontrado() {
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        when(q.getSingleResult()).thenThrow(new NoResultException());

        assertNull(manejador.VerificarUsuario("nick"));
        verify(em).close();
    }

    @Test
    public void testVerificarUsuarioNoUnico() {
        TypedQuery<UsuarioBase> q = queryDe(UsuarioBase.class);
        when(q.getSingleResult()).thenThrow(new NonUniqueResultException());

        assertNull(manejador.VerificarUsuario("nick"));
        verify(em).close();
    }
}