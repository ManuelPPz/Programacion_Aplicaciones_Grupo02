package Manejadores;

import Classes.Categoria;
import Classes.Curso;
import Classes.Docente;
import Classes.Edi_Usu;
import Classes.EdicionCurso;
import Classes.Enum_Estado_inscripcion;
import Classes.Instituto;
import Classes.Usuario;
import DTsClasses.DTEdi_Usu;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import util.JPAUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios de ManejadorEdicionCurso (JPAUtil mockeado, sin base de datos).
 */
public class ManejadorEdicionCursoTest {

    private MockedStatic<JPAUtil> jpaUtil;
    private EntityManager em;
    private EntityTransaction tx;
    private Query updateQuery;
    private ManejadorEdicionCurso manejador;

    @BeforeEach
    public void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        updateQuery = mock(Query.class, RETURNS_SELF);

        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);
        when(em.isOpen()).thenReturn(true);
        when(em.createQuery(anyString())).thenReturn(updateQuery);
        when(updateQuery.executeUpdate()).thenReturn(1);

        jpaUtil = mockStatic(JPAUtil.class);
        jpaUtil.when(JPAUtil::getEntityManager).thenReturn(em);

        manejador = ManejadorEdicionCurso.GetInstance();
    }

    @AfterEach
    public void tearDown() {
        jpaUtil.close();
    }

    // ===================== GetInstance =====================

    @Test
    public void testGetInstance() {
        assertNotNull(ManejadorEdicionCurso.GetInstance());
        assertSame(ManejadorEdicionCurso.GetInstance(), ManejadorEdicionCurso.GetInstance());
    }

    // ===================== CrearEdicion =====================

    @Test
    public void testCrearEdicion() {
        try (MockedConstruction<EdicionCurso> construido = mockConstruction(EdicionCurso.class)) {
            EdicionCurso result = manejador.CrearEdicion(mock(Instituto.class), mock(Curso.class),
                    "Ed1", new Date(), new Date(), 10, new Date(), new ArrayList<>(), null);

            assertNotNull(result);
            assertEquals(1, construido.constructed().size());
            assertSame(construido.constructed().get(0), result);
        }
    }

    // ===================== ModificarDatos =====================

    @Test
    public void testModificarDatosOk() {
        EdicionCurso ec = mock(EdicionCurso.class);
        Date d = new Date();
        List<Docente> docentes = new ArrayList<>();

        manejador.ModificarDatos(ec, d, d, 5, d, docentes, null);

        verify(ec).ModificarDatos(d, d, 5, d, docentes, null);
        verify(tx).begin();
        verify(em).merge(ec);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testModificarDatosEcNulo() {
        manejador.ModificarDatos(null, new Date(), new Date(), 5, new Date(), new ArrayList<>(), null);

        verify(em).merge(null);
        verify(em).close();
    }

    @Test
    public void testModificarDatosErrorHaceRollback() {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(em.merge(ec)).thenThrow(new RuntimeException("boom"));

        manejador.ModificarDatos(ec, new Date(), new Date(), 5, new Date(), new ArrayList<>(), null);

        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testModificarDatosErrorSinTransaccionActiva() {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(em.merge(ec)).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);

        manejador.ModificarDatos(ec, new Date(), new Date(), 5, new Date(), new ArrayList<>(), null);

        verify(tx, never()).rollback();
    }

    // ===================== Add =====================

    @Test
    public void testAddDocentesNulos() throws Exception {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(ec.getMisDocentes()).thenReturn(null);

        manejador.Add(ec);

        verify(em).merge(ec);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testAddConDocentes() throws Exception {
        EdicionCurso ec = mock(EdicionCurso.class);

        Docente sinLista = mock(Docente.class);
        when(sinLista.getEdiciones()).thenReturn(null);

        List<EdicionCurso> yaContiene = new ArrayList<>();
        yaContiene.add(ec);
        Docente conEdicion = mock(Docente.class);
        when(conEdicion.getEdiciones()).thenReturn(yaContiene);

        List<EdicionCurso> vacia = new ArrayList<>();
        Docente sinEdicion = mock(Docente.class);
        when(sinEdicion.getEdiciones()).thenReturn(vacia);

        when(ec.getMisDocentes()).thenReturn(Arrays.asList(sinLista, conEdicion, sinEdicion));

        manejador.Add(ec);

        assertEquals(1, yaContiene.size());
        assertEquals(List.of(ec), vacia);
        verify(em).merge(ec);
        verify(tx).commit();
    }

    @Test
    public void testAddErrorHaceRollbackYLanzaExcepcion() {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(em.merge(ec)).thenThrow(new RuntimeException("boom"));

        Exception ex = assertThrows(Exception.class, () -> manejador.Add(ec));

        assertTrue(ex.getMessage().contains("Error al guardar la edición de curso"));
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddErrorSinTransaccionActiva() {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(em.merge(ec)).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);

        assertThrows(Exception.class, () -> manejador.Add(ec));
        verify(tx, never()).rollback();
    }

    // ===================== BuscarEdicion =====================

    @Test
    public void testBuscarEdicionEncontrada() {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(em.find(EdicionCurso.class, "Ed1")).thenReturn(ec);

        assertSame(ec, manejador.BuscarEdicion("Ed1"));
        verify(em).close();
    }

    @Test
    public void testBuscarEdicionConExcepcion() {
        when(em.find(EdicionCurso.class, "Ed1")).thenThrow(new RuntimeException("boom"));

        assertNull(manejador.BuscarEdicion("Ed1"));
        verify(em).close();
    }

    @Test
    public void testBuscarEdicionEmCerrado() {
        when(em.isOpen()).thenReturn(false);

        assertNull(manejador.BuscarEdicion("Ed1"));
        verify(em, never()).close();
    }

    // ===================== AddUsuario =====================

    @Test
    public void testAddUsuario() {
        EdicionCurso ec = mock(EdicionCurso.class);
        Docente d = mock(Docente.class);

        manejador.AddUsuario(ec, d);

        verify(ec).AddUsuarios(d);
    }

    // ===================== AddUsuarioInscripto =====================

    private Edi_Usu euEntrada(Date fIns) {
        Edi_Usu eu = mock(Edi_Usu.class, RETURNS_DEEP_STUBS);
        when(eu.getEdicion().getNombre()).thenReturn("Ed1");
        when(eu.getUsuario().getNickname()).thenReturn("nick");
        when(eu.getFIns()).thenReturn(fIns);
        return eu;
    }

    private EdicionCurso edicionManaged(int cupo, int cupoActual) {
        EdicionCurso ed = mock(EdicionCurso.class);
        when(ed.getNombre()).thenReturn("Ed1");
        when(ed.getCupo()).thenReturn(cupo);
        when(ed.getCupoActual()).thenReturn(cupoActual);
        when(em.find(EdicionCurso.class, "Ed1")).thenReturn(ed);
        return ed;
    }

    private Usuario usuarioManaged() {
        Usuario us = mock(Usuario.class);
        when(us.getNickname()).thenReturn("nick");
        when(em.find(Usuario.class, "nick")).thenReturn(us);
        return us;
    }

    @Test
    public void testAddUsuarioInscriptoEntradaNula() {
        Exception ex = assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(null));
        assertTrue(ex.getMessage().contains("usuario y una edición válidos"));
    }

    @Test
    public void testAddUsuarioInscriptoSinEdicion() {
        Edi_Usu eu = mock(Edi_Usu.class);
        when(eu.getEdicion()).thenReturn(null);

        assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(eu));
    }

    @Test
    public void testAddUsuarioInscriptoSinUsuario() {
        Edi_Usu eu = mock(Edi_Usu.class, RETURNS_DEEP_STUBS);
        when(eu.getUsuario()).thenReturn(null);

        assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(eu));
    }

    @Test
    public void testAddUsuarioInscriptoEntidadesNoEncontradas() {
        Edi_Usu eu = euEntrada(new Date());
        when(em.find(EdicionCurso.class, "Ed1")).thenReturn(null);
        when(em.find(Usuario.class, "nick")).thenReturn(null);

        Exception ex = assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(eu));

        assertTrue(ex.getMessage().contains("Error al guardar la inscripción"));
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddUsuarioInscriptoSinCupos() {
        Edi_Usu eu = euEntrada(new Date());
        EdicionCurso ed = edicionManaged(5, 0);
        when(ed.getMisInscripciones()).thenReturn(List.of(mock(Edi_Usu.class)));
        usuarioManaged();

        Exception ex = assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(eu));

        assertTrue(ex.getMessage().contains("No hay cupos"));
        verify(em, never()).persist(any());
    }

    @Test
    public void testAddUsuarioInscriptoYaInscripto() {
        Edi_Usu eu = euEntrada(new Date());
        edicionManaged(5, 3);
        usuarioManaged();
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(mock(Edi_Usu.class));

        Exception ex = assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(eu));

        assertTrue(ex.getMessage().contains("ya se encuentra inscripto"));
        verify(tx).rollback();
    }

    @Test
    public void testAddUsuarioInscriptoOkConCupo() throws Exception {
        Date fecha = new Date();
        Edi_Usu eu = euEntrada(fecha);
        EdicionCurso ed = edicionManaged(5, 3);
        Usuario us = usuarioManaged();
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(null);

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<Edi_Usu> construido = mockConstruction(Edi_Usu.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            manejador.AddUsuarioInscripto(eu);

            assertSame(us, args.get(0));
            assertSame(ed, args.get(1));
            assertSame(fecha, args.get(2));
            Edi_Usu nueva = construido.constructed().get(0);
            verify(em).persist(nueva);
            verify(ed).setCupoActual(2);
            verify(ed).AddUsuarioInscripto(nueva);
            verify(us).AddEdicionCurso(nueva);
        }
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testAddUsuarioInscriptoInicializaCupoSinInscripcionesPrevias() throws Exception {
        Edi_Usu eu = euEntrada(null); // fIns nulo -> usa new Date()
        EdicionCurso ed = edicionManaged(5, 0);
        when(ed.getMisInscripciones()).thenReturn(null);
        usuarioManaged();

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<Edi_Usu> construido = mockConstruction(Edi_Usu.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            manejador.AddUsuarioInscripto(eu);

            assertNotNull(args.get(2));
            assertTrue(args.get(2) instanceof Date);
        }
        verify(ed).setCupoActual(5);
        verify(ed).setCupoActual(4);
    }

    @Test
    public void testAddUsuarioInscriptoInicializaCupoConListaVacia() throws Exception {
        Edi_Usu eu = euEntrada(new Date());
        EdicionCurso ed = edicionManaged(2, 0);
        when(ed.getMisInscripciones()).thenReturn(new ArrayList<>());
        usuarioManaged();

        try (MockedConstruction<Edi_Usu> construido = mockConstruction(Edi_Usu.class)) {
            manejador.AddUsuarioInscripto(eu);
        }
        verify(ed).setCupoActual(2);
        verify(ed).setCupoActual(1);
    }

    @Test
    public void testAddUsuarioInscriptoCupoIlimitado() throws Exception {
        Edi_Usu eu = euEntrada(new Date());
        EdicionCurso ed = edicionManaged(0, 0);
        usuarioManaged();

        try (MockedConstruction<Edi_Usu> construido = mockConstruction(Edi_Usu.class)) {
            manejador.AddUsuarioInscripto(eu);
        }
        verify(ed, never()).setCupoActual(anyInt());
        verify(tx).commit();
    }

    @Test
    public void testAddUsuarioInscriptoErrorSinTransaccionActivaYEmCerrado() {
        Edi_Usu eu = euEntrada(new Date());
        when(em.find(EdicionCurso.class, "Ed1")).thenReturn(null);
        when(tx.isActive()).thenReturn(false);
        when(em.isOpen()).thenReturn(false);

        assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(eu));

        verify(tx, never()).rollback();
        verify(em, never()).close();
    }

    // ===================== getList =====================

    @SuppressWarnings("unchecked")
    private TypedQuery<EdicionCurso> queryEdiciones() {
        TypedQuery<EdicionCurso> q = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(EdicionCurso.class))).thenReturn(q);
        return q;
    }

    @Test
    public void testGetListConResultados() {
        TypedQuery<EdicionCurso> q = queryEdiciones();

        EdicionCurso completa = mock(EdicionCurso.class, RETURNS_DEEP_STUBS);
        when(completa.getCurso().getCategorias()).thenReturn(new ArrayList<>());
        when(completa.getMisDocentes()).thenReturn(new ArrayList<>());
        when(completa.getMisInscripciones()).thenReturn(new ArrayList<>());

        EdicionCurso vacia = mock(EdicionCurso.class);
        when(vacia.getCurso()).thenReturn(null);
        when(vacia.getMisDocentes()).thenReturn(null);
        when(vacia.getMisInscripciones()).thenReturn(null);

        EdicionCurso sinCategorias = mock(EdicionCurso.class, RETURNS_DEEP_STUBS);
        when(sinCategorias.getCurso().getCategorias()).thenReturn(null);
        when(sinCategorias.getMisDocentes()).thenReturn(null);
        when(sinCategorias.getMisInscripciones()).thenReturn(null);

        when(q.getResultList()).thenReturn(Arrays.asList(completa, vacia, sinCategorias));

        List<EdicionCurso> result = manejador.getList();

        assertEquals(3, result.size());
        verify(em).close();
    }

    @Test
    public void testGetListConExcepcion() {
        when(em.createQuery(anyString(), eq(EdicionCurso.class))).thenThrow(new RuntimeException("boom"));

        List<EdicionCurso> result = manejador.getList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetListEmCerrado() {
        TypedQuery<EdicionCurso> q = queryEdiciones();
        when(q.getResultList()).thenReturn(new ArrayList<>());
        when(em.isOpen()).thenReturn(false);

        assertTrue(manejador.getList().isEmpty());
        verify(em, never()).close();
    }

    // ===================== getDT =====================

    @Test
    public void testGetDTNulo() {
        assertNull(manejador.getDT(null));
    }

    @Test
    public void testGetDTCompleto() {
        EdicionCurso ec = mock(EdicionCurso.class, RETURNS_DEEP_STUBS);
        when(ec.getInstituto().getNombre()).thenReturn("INCO");
        when(ec.getCurso().getNombre()).thenReturn("Java");
        when(ec.getNombre()).thenReturn("Ed1");
        when(ec.getCupo()).thenReturn(10);
        when(ec.getCupoActual()).thenReturn(7);

        Docente d = mock(Docente.class);
        when(d.getNickname()).thenReturn("profe");
        when(ec.getMisDocentes()).thenReturn(Arrays.asList(d, null));

        DTEdi_Usu dtEu = mock(DTEdi_Usu.class);
        Edi_Usu eu = mock(Edi_Usu.class);
        when(eu.getMyDT()).thenReturn(dtEu);
        when(ec.getMisInscripciones()).thenReturn(Arrays.asList(eu, null));

        Categoria cat = mock(Categoria.class);
        when(cat.getNombre()).thenReturn("Backend");
        when(ec.getCurso().getCategorias()).thenReturn(Arrays.asList(cat, null));

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTEdicionCurso> dt = mockConstruction(DTEdicionCurso.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            DTEdicionCurso result = manejador.getDT(ec);

            assertNotNull(result);
            assertEquals(13, args.size());
            assertEquals("INCO", args.get(0));
            assertEquals("Java", args.get(1));
            assertEquals("Ed1", args.get(2));
            assertEquals(10, args.get(5));
            assertEquals(7, args.get(6));
            assertEquals(List.of("profe"), args.get(7));
            assertEquals(List.of(dtEu), args.get(9));
            assertEquals(List.of("Backend"), args.get(10));
        }
    }

    @Test
    public void testGetDTSinRelacionesNiColecciones() {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(ec.getInstituto()).thenReturn(null);
        when(ec.getCurso()).thenReturn(null);
        when(ec.getMisDocentes()).thenReturn(null);
        when(ec.getMisInscripciones()).thenReturn(null);

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTEdicionCurso> dt = mockConstruction(DTEdicionCurso.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            assertNotNull(manejador.getDT(ec));

            assertEquals("", args.get(0));
            assertEquals("", args.get(1));
            assertEquals(new ArrayList<String>(), args.get(7));
            assertEquals(new ArrayList<DTEdi_Usu>(), args.get(9));
            assertEquals(new ArrayList<String>(), args.get(10));
        }
    }

    @Test
    public void testGetDTCursoSinCategorias() {
        EdicionCurso ec = mock(EdicionCurso.class, RETURNS_DEEP_STUBS);
        when(ec.getCurso().getCategorias()).thenReturn(null);
        when(ec.getMisDocentes()).thenReturn(null);
        when(ec.getMisInscripciones()).thenReturn(null);

        try (MockedConstruction<DTEdicionCurso> dt = mockConstruction(DTEdicionCurso.class)) {
            assertNotNull(manejador.getDT(ec));
        }
    }

    // ===================== getDTLIst =====================

    private EdicionCurso edicionDeCurso(String nombreCurso) {
        EdicionCurso ec = mock(EdicionCurso.class, RETURNS_DEEP_STUBS);
        when(ec.getCurso().getNombre()).thenReturn(nombreCurso);
        return ec;
    }

    @Test
    public void testGetDTLIstParametroNuloOVacio() {
        assertTrue(manejador.getDTLIst(null).isEmpty());
        assertTrue(manejador.getDTLIst("   ").isEmpty());
    }

    @Test
    public void testGetDTLIstFiltraPorCurso() {
        ManejadorEdicionCurso spy = spy(manejador);

        EdicionCurso coincide = edicionDeCurso("  Java ");
        EdicionCurso otro = edicionDeCurso("Python");
        EdicionCurso sinCurso = mock(EdicionCurso.class);
        when(sinCurso.getCurso()).thenReturn(null);
        EdicionCurso cursoSinNombre = edicionDeCurso(null);

        DTEdicionCurso dt = mock(DTEdicionCurso.class);
        doReturn(Arrays.asList(coincide, otro, sinCurso, cursoSinNombre, null)).when(spy).getList();
        doReturn(dt).when(spy).getDT(any(EdicionCurso.class));

        List<DTMaster> result = spy.getDTLIst(" java");

        assertEquals(1, result.size());
        assertSame(dt, result.get(0));
    }

    @Test
    public void testGetDTLIstListaNula() {
        ManejadorEdicionCurso spy = spy(manejador);
        doReturn(null).when(spy).getList();

        assertTrue(spy.getDTLIst("Java").isEmpty());
    }

    // ===================== OrdenarInscripcionesPorPrioridad =====================

    private Edi_Usu inscripcion(int rechazos, Date fecha, DTMaster dt) {
        Edi_Usu eu = mock(Edi_Usu.class, RETURNS_DEEP_STUBS);
        when(eu.getUsuario().getCantMisInscripcionesRechazadas("Java")).thenReturn(rechazos);
        when(eu.getFIns()).thenReturn(fecha);
        when(eu.getMyDT()).thenReturn(dt);
        return eu;
    }

    private Curso cursoJava() {
        Curso c = mock(Curso.class);
        when(c.getNombre()).thenReturn("Java");
        return c;
    }

    @Test
    public void testOrdenarListaNulaOVacia() {
        assertTrue(manejador.OrdenarInscripcionesPorPrioridad(null, cursoJava()).isEmpty());
        assertTrue(manejador.OrdenarInscripcionesPorPrioridad(new ArrayList<>(), cursoJava()).isEmpty());
    }

    @Test
    public void testOrdenarPorRechazosDescendente() {
        DTMaster dt0 = mock(DTMaster.class);
        DTMaster dt4 = mock(DTMaster.class);
        DTMaster dt2 = mock(DTMaster.class);
        Date f = new Date();

        Edi_Usu sinRechazos = inscripcion(0, f, dt0);
        Edi_Usu muchos = inscripcion(4, f, dt4);   // rank 2
        Edi_Usu algunos = inscripcion(2, f, dt2);  // rank 1

        List<DTMaster> result = manejador.OrdenarInscripcionesPorPrioridad(
                Arrays.asList(sinRechazos, muchos, algunos), cursoJava());

        assertEquals(List.of(dt4, dt2, dt0), result);
    }

    @Test
    public void testOrdenarMismoRankPorFecha() {
        DTMaster dtViejo = mock(DTMaster.class);
        DTMaster dtNuevo = mock(DTMaster.class);
        DTMaster dtNull1 = mock(DTMaster.class);
        DTMaster dtNull2 = mock(DTMaster.class);

        Edi_Usu nuevo = inscripcion(0, new Date(2000), dtNuevo);
        Edi_Usu nulo1 = inscripcion(0, null, dtNull1);
        Edi_Usu viejo = inscripcion(0, new Date(1000), dtViejo);
        Edi_Usu nulo2 = inscripcion(0, null, dtNull2);

        List<DTMaster> result = manejador.OrdenarInscripcionesPorPrioridad(
                Arrays.asList(nuevo, nulo1, viejo, nulo2), cursoJava());

        assertEquals(4, result.size());
        assertSame(dtViejo, result.get(0));
        assertSame(dtNuevo, result.get(1));
        assertTrue(result.subList(2, 4).containsAll(List.of(dtNull1, dtNull2)));
    }

    @Test
    public void testOrdenarConInscripcionesSinUsuario() {
        DTMaster dtA = mock(DTMaster.class);
        DTMaster dtB = mock(DTMaster.class);
        DTMaster dtSin1 = mock(DTMaster.class);
        DTMaster dtSin2 = mock(DTMaster.class);

        Edi_Usu a = inscripcion(0, new Date(1000), dtA);
        Edi_Usu sin1 = mock(Edi_Usu.class);
        when(sin1.getUsuario()).thenReturn(null);
        when(sin1.getMyDT()).thenReturn(dtSin1);
        Edi_Usu b = inscripcion(0, new Date(2000), dtB);
        Edi_Usu sin2 = mock(Edi_Usu.class);
        when(sin2.getUsuario()).thenReturn(null);
        when(sin2.getMyDT()).thenReturn(dtSin2);

        List<DTMaster> result = manejador.OrdenarInscripcionesPorPrioridad(
                Arrays.asList(a, sin1, b, sin2), cursoJava());

        assertEquals(4, result.size());
        assertEquals(List.of(dtA, dtB), result.subList(0, 2));
        assertTrue(result.subList(2, 4).containsAll(List.of(dtSin1, dtSin2)));
    }

    // ===================== getMisInscripciones =====================

    @Test
    public void testGetMisInscripciones() {
        EdicionCurso ec = mock(EdicionCurso.class);
        List<Edi_Usu> lista = new ArrayList<>();
        when(ec.getMisInscripciones()).thenReturn(lista);

        assertSame(lista, manejador.getMisInscripciones(ec));
    }

    // ===================== ModificarEstadoDeInscripcion =====================

    @Test
    public void testModificarEstadoDeInscripcionOk() {
        Enum_Estado_inscripcion estado = null;

        manejador.ModificarEstadoDeInscripcion("nick", estado, "Ed1");

        verify(updateQuery).setParameter("nuevoEstado", estado);
        verify(updateQuery).setParameter("nombreEdicion", "Ed1");
        verify(updateQuery).setParameter("nickname", "nick");
        verify(updateQuery).executeUpdate();
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testModificarEstadoDeInscripcionErrorHaceRollback() {
        when(updateQuery.executeUpdate()).thenThrow(new RuntimeException("boom"));

        manejador.ModificarEstadoDeInscripcion("nick", null, "Ed1");

        verify(tx).rollback();
        verify(tx, never()).commit();
        verify(em).close();
    }

    @Test
    public void testModificarEstadoDeInscripcionErrorSinTransaccionActiva() {
        when(updateQuery.executeUpdate()).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);

        manejador.ModificarEstadoDeInscripcion("nick", null, "Ed1");

        verify(tx, never()).rollback();
    }

    // ===================== Inscripción con video =====================

    @Test
    public void testAddUsuarioInscriptoCopiaUrlVideo() throws Exception {
        Edi_Usu eu = euEntrada(new Date());
        when(eu.getUrlVideo()).thenReturn("https://youtu.be/x");
        edicionManaged(5, 3);
        usuarioManaged();
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(null);

        try (MockedConstruction<Edi_Usu> construido = mockConstruction(Edi_Usu.class)) {
            manejador.AddUsuarioInscripto(eu);
            verify(construido.constructed().get(0)).setUrlVideo("https://youtu.be/x");
        }
    }

    @Test
    public void testBuscarInscripcion() {
        Edi_Usu eu = mock(Edi_Usu.class);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(eu);

        assertSame(eu, manejador.BuscarInscripcion("nick", "Ed1"));
        assertNull(manejador.BuscarInscripcion(null, "Ed1"));
        assertNull(manejador.BuscarInscripcion("nick", null));
        verify(em).close();
    }

    @Test
    public void testBuscarInscripcionError() {
        when(em.find(eq(Edi_Usu.class), any())).thenThrow(new RuntimeException("boom"));
        assertNull(manejador.BuscarInscripcion("nick", "Ed1"));
        verify(em).close();
    }

    @Test
    public void testModificarVideoInscripcionOk() throws Exception {
        Edi_Usu eu = mock(Edi_Usu.class);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(eu);

        manejador.ModificarVideoInscripcion("nick", "Ed1", "https://v");

        verify(eu).setUrlVideo("https://v");
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testModificarVideoInscripcionNoExiste() {
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(null);

        Exception ex = assertThrows(Exception.class, () -> manejador.ModificarVideoInscripcion("nick", "Ed1", null));

        assertTrue(ex.getMessage().contains("no está inscripto"));
        verify(tx).rollback();
        verify(tx, never()).commit();
        verify(em).close();
    }

    private static Date dia(int offsetDias) {
        return new Date(System.currentTimeMillis() + offsetDias * 24L * 60 * 60 * 1000);
    }

    private EdicionCurso edicionDeCurso(String curso, Date fInicio, Date fFin, Date fAlta) {
        EdicionCurso ec = mock(EdicionCurso.class, RETURNS_DEEP_STUBS);
        when(ec.getCurso().getNombre()).thenReturn(curso);
        when(ec.getFInicio()).thenReturn(fInicio);
        when(ec.getFFin()).thenReturn(fFin);
        when(ec.getFAlta()).thenReturn(fAlta);
        return ec;
    }

    @Test
    public void testBuscarEdicionVigenteEligeLaMasRecienteNoTerminada() {
        EdicionCurso terminada = edicionDeCurso("Java", dia(-60), dia(-1), dia(-1));
        EdicionCurso vieja = edicionDeCurso("Java", dia(-10), dia(30), dia(-90));
        EdicionCurso nueva = edicionDeCurso("Java", dia(20), dia(80), dia(-5));
        EdicionCurso otroCurso = edicionDeCurso("Python", dia(5), dia(50), dia(0));
        EdicionCurso sinFin = edicionDeCurso("Java", dia(5), null, dia(0));
        EdicionCurso sinCurso = mock(EdicionCurso.class);
        when(sinCurso.getCurso()).thenReturn(null);
        when(queryEdiciones().getResultList()).thenReturn(
                Arrays.asList(sinCurso, terminada, vieja, otroCurso, nueva, sinFin));

        assertSame(nueva, manejador.BuscarEdicionVigente(" java ", new Date()));
    }

    @Test
    public void testBuscarEdicionVigenteTerminaHoyCuenta() {
        EdicionCurso hoy = edicionDeCurso("Java", dia(-30), new Date(), null);
        when(queryEdiciones().getResultList()).thenReturn(List.of(hoy));
        assertSame(hoy, manejador.BuscarEdicionVigente("Java", null));
    }

    @Test
    public void testBuscarEdicionVigenteSinFechaAltaUsaInicio() {
        EdicionCurso a = edicionDeCurso("Java", dia(1), dia(40), null);
        EdicionCurso b = edicionDeCurso("Java", dia(10), dia(40), null);
        EdicionCurso sinFechas = edicionDeCurso("Java", null, dia(40), null);
        when(queryEdiciones().getResultList()).thenReturn(List.of(sinFechas, a, b));
        assertSame(b, manejador.BuscarEdicionVigente("Java", new Date()));
    }

    @Test
    public void testBuscarEdicionVigenteSinResultado() {
        EdicionCurso terminada = edicionDeCurso("Java", dia(-9), dia(-2), dia(-9));
        when(queryEdiciones().getResultList()).thenReturn(List.of(terminada));
        assertNull(manejador.BuscarEdicionVigente("Java", new Date()));
        assertNull(manejador.BuscarEdicionVigente(null, new Date()));
        assertNull(manejador.BuscarEdicionVigente("  ", new Date()));
    }

    // ===================== Desistir / calificaciones =====================

    private Edi_Usu inscripcionReal(String nick, EdicionCurso ec, Enum_Estado_inscripcion estado) {
        Usuario u = mock(Usuario.class);
        when(u.getNickname()).thenReturn(nick);
        when(u.getMisInscripciones()).thenReturn(new ArrayList<>());
        Edi_Usu eu = new Edi_Usu(u, ec, new Date());
        eu.setEstado(estado);
        return eu;
    }

    @Test
    public void testDesistirDeInscripcionLiberaCupo() throws Exception {
        EdicionCurso ec = new EdicionCurso("Ed1", null, null, dia(5), dia(30), 10, new Date(), new ArrayList<>(), null);
        ec.setCupoActual(9);
        Edi_Usu eu = inscripcionReal("nick", ec, Enum_Estado_inscripcion.INSCRIPTO);
        ec.AddUsuarioInscripto(eu);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(eu);

        manejador.DesistirDeInscripcion("nick", "Ed1");

        assertEquals(10, ec.getCupoActual());
        assertTrue(ec.getMisInscripciones().isEmpty());
        verify(em).remove(eu);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testDesistirDeInscripcionNoPendiente() {
        EdicionCurso ec = new EdicionCurso("Ed1", null, null, dia(5), dia(30), 10, new Date(), new ArrayList<>(), null);
        Edi_Usu eu = inscripcionReal("nick", ec, Enum_Estado_inscripcion.ACEPTADO);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(eu);

        Exception ex = assertThrows(Exception.class, () -> manejador.DesistirDeInscripcion("nick", "Ed1"));

        assertTrue(ex.getMessage().contains("pendiente"));
        verify(em, never()).remove(any());
        verify(tx).rollback();
    }

    @Test
    public void testDesistirDeInscripcionNoExiste() {
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(null);
        Exception ex = assertThrows(Exception.class, () -> manejador.DesistirDeInscripcion("nick", "Ed1"));
        assertTrue(ex.getMessage().contains("no está inscripto"));
        verify(em).close();
    }

    private EdicionCurso edicionConInscriptos(int cupo, Edi_Usu... out) {
        EdicionCurso ec = new EdicionCurso("Ed1", null, null, dia(5), dia(30), cupo, new Date(), new ArrayList<>(), null);
        String[] nicks = {"a", "b", "c"};
        for (int i = 0; i < out.length; i++) {
            out[i] = inscripcionReal(nicks[i], ec, Enum_Estado_inscripcion.INSCRIPTO);
            ec.AddUsuarioInscripto(out[i]);
        }
        when(em.find(EdicionCurso.class, "Ed1")).thenReturn(ec);
        return ec;
    }

    @Test
    public void testSeleccionarEstudiantesDentroDelCupo() throws Exception {
        Edi_Usu[] ins = new Edi_Usu[3];
        edicionConInscriptos(2, ins);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(ins[0], ins[1], ins[2]);

        manejador.SeleccionarEstudiantes("Ed1", List.of(
                new Object[]{"a", Enum_Estado_inscripcion.ACEPTADO},
                new Object[]{"b", Enum_Estado_inscripcion.ACEPTADO},
                new Object[]{"c", Enum_Estado_inscripcion.RECHAZADO}));

        assertEquals(Enum_Estado_inscripcion.ACEPTADO, ins[0].getMiEstado());
        assertEquals(Enum_Estado_inscripcion.ACEPTADO, ins[1].getMiEstado());
        assertEquals(Enum_Estado_inscripcion.RECHAZADO, ins[2].getMiEstado());
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testSeleccionarEstudiantesSuperaCupo() {
        Edi_Usu[] ins = new Edi_Usu[3];
        edicionConInscriptos(2, ins);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(ins[0], ins[1], ins[2]);

        Exception ex = assertThrows(Exception.class, () -> manejador.SeleccionarEstudiantes("Ed1", List.of(
                new Object[]{"a", Enum_Estado_inscripcion.ACEPTADO},
                new Object[]{"b", Enum_Estado_inscripcion.ACEPTADO},
                new Object[]{"c", Enum_Estado_inscripcion.ACEPTADO})));

        assertTrue(ex.getMessage().contains("cupo"));
        verify(tx).rollback();
        verify(tx, never()).commit();
    }

    @Test
    public void testSeleccionarEstudiantesSinCupoDefinidoNoLimita() throws Exception {
        Edi_Usu[] ins = new Edi_Usu[2];
        edicionConInscriptos(0, ins);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(ins[0], ins[1]);

        manejador.SeleccionarEstudiantes("Ed1", List.of(
                new Object[]{"a", Enum_Estado_inscripcion.ACEPTADO},
                new Object[]{"b", Enum_Estado_inscripcion.ACEPTADO}));

        verify(tx).commit();
    }

    @Test
    public void testSeleccionarEstudiantesValidaciones() {
        Edi_Usu[] ins = new Edi_Usu[1];
        EdicionCurso ec = edicionConInscriptos(5, ins);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(null);

        assertTrue(assertThrows(Exception.class, () -> manejador.SeleccionarEstudiantes("Ed1",
                List.<Object[]>of(new Object[]{"a", Enum_Estado_inscripcion.INSCRIPTO}))).getMessage().contains("Aceptada o Rechazada"));
        assertTrue(assertThrows(Exception.class, () -> manejador.SeleccionarEstudiantes("Ed1",
                List.<Object[]>of(new Object[]{"zz", Enum_Estado_inscripcion.ACEPTADO}))).getMessage().contains("no está inscripto"));
        assertTrue(assertThrows(Exception.class,
                () -> manejador.SeleccionarEstudiantes("NoExiste", null)).getMessage().contains("No existe"));
        assertTrue(assertThrows(Exception.class,
                () -> manejador.SeleccionarEstudiantes(null, null)).getMessage().contains("No existe"));

        ec.Finalizar();
        assertTrue(assertThrows(Exception.class,
                () -> manejador.SeleccionarEstudiantes("Ed1", null)).getMessage().contains("finalizada"));
        verify(tx, never()).commit();
    }

    @Test
    public void testIngresarNotasYFinalizar() throws Exception {
        EdicionCurso ec = new EdicionCurso("Ed1", null, null, dia(-5), dia(30), 0, new Date(), new ArrayList<>(), null);
        Edi_Usu a = inscripcionReal("a", ec, Enum_Estado_inscripcion.ACEPTADO);
        Edi_Usu b = inscripcionReal("b", ec, Enum_Estado_inscripcion.ACEPTADO);
        when(em.find(EdicionCurso.class, "Ed1")).thenReturn(ec);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(a, b);

        manejador.IngresarNotas("Ed1", List.of("a", "b"), List.of(7, 12), false);
        assertEquals(7, a.getNota());
        assertEquals(12, b.getNota());
        assertFalse(ec.isFinalizada());

        manejador.IngresarNotas("Ed1", List.of(), List.of(), true);
        assertTrue(ec.isFinalizada());
        verify(tx, times(2)).commit();
    }

    @Test
    public void testIngresarNotasValidaciones() {
        EdicionCurso ec = new EdicionCurso("Ed1", null, null, dia(-5), dia(30), 0, new Date(), new ArrayList<>(), null);
        when(em.find(EdicionCurso.class, "Ed1")).thenReturn(ec);
        when(em.find(eq(Edi_Usu.class), any())).thenReturn(null);

        assertTrue(assertThrows(Exception.class,
                () -> manejador.IngresarNotas("Ed1", List.of("a"), List.of(13), false)).getMessage().contains("entre 1 y 12"));
        assertTrue(assertThrows(Exception.class,
                () -> manejador.IngresarNotas("Ed1", List.of("a"), Arrays.asList((Integer) null), false)).getMessage().contains("entre 1 y 12"));
        assertTrue(assertThrows(Exception.class,
                () -> manejador.IngresarNotas("Ed1", List.of("a"), List.of(5), false)).getMessage().contains("no está inscripto"));
        assertTrue(assertThrows(Exception.class,
                () -> manejador.IngresarNotas("NoExiste", null, null, false)).getMessage().contains("No existe"));

        ec.Finalizar();
        assertTrue(assertThrows(Exception.class,
                () -> manejador.IngresarNotas("Ed1", null, null, false)).getMessage().contains("finalizada"));
        verify(tx, never()).commit();
    }

    @Test
    public void testGetDTIncluyeEstadoYNota() {
        EdicionCurso ec = new EdicionCurso("Ed1", null, null, dia(-5), dia(30), 0, new Date(), new ArrayList<>(), null);
        Edi_Usu eu = inscripcionReal("a", ec, Enum_Estado_inscripcion.ACEPTADO);
        eu.setNota(9);
        ec.AddUsuarioInscripto(eu);

        DTEdicionCurso dt = manejador.getDT(ec);

        assertEquals(Classes.Enum_Estado_Edicion.EN_CURSO, dt.getEstado());
        assertEquals(9, dt.getMisInscripciones().get(0).getNota());
    }
}
