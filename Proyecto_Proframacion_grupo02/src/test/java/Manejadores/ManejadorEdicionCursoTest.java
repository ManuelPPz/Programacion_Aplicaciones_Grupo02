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
                    "Ed1", new Date(), new Date(), 10, new Date(), new ArrayList<>());

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

        manejador.ModificarDatos(ec, d, d, 5, d, docentes);

        verify(ec).ModificarDatos(d, d, 5, d, docentes);
        verify(tx).begin();
        verify(em).merge(ec);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testModificarDatosEcNulo() {
        manejador.ModificarDatos(null, new Date(), new Date(), 5, new Date(), new ArrayList<>());

        verify(em).merge(null);
        verify(em).close();
    }

    @Test
    public void testModificarDatosErrorHaceRollback() {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(em.merge(ec)).thenThrow(new RuntimeException("boom"));

        manejador.ModificarDatos(ec, new Date(), new Date(), 5, new Date(), new ArrayList<>());

        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testModificarDatosErrorSinTransaccionActiva() {
        EdicionCurso ec = mock(EdicionCurso.class);
        when(em.merge(ec)).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);

        manejador.ModificarDatos(ec, new Date(), new Date(), 5, new Date(), new ArrayList<>());

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
            assertEquals(11, args.size());
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
}