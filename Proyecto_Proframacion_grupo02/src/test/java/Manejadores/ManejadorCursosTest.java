package Manejadores;

import Classes.Categoria;
import Classes.Curso;
import Classes.EdicionCurso;
import Classes.Instituto;
import Classes.ProgramaDeFormacion;
import Classes.UsuarioBase;
import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
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
 * Tests unitarios de ManejadorCursos.
 * JPAUtil se mockea de forma estatica, por lo que no se necesita base de datos.
 */
public class ManejadorCursosTest {

    private MockedStatic<JPAUtil> jpaUtil;
    private EntityManager em;
    private EntityTransaction tx;
    private TypedQuery<Curso> query;
    private ManejadorCursos manejador;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        query = mock(TypedQuery.class, RETURNS_SELF);

        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);
        when(em.createQuery(anyString(), eq(Curso.class))).thenReturn(query);

        jpaUtil = mockStatic(JPAUtil.class);
        jpaUtil.when(JPAUtil::getEntityManager).thenReturn(em);

        manejador = ManejadorCursos.GetInstance();
    }

    @AfterEach
    public void tearDown() {
        jpaUtil.close();
    }

    // ===================== GetInstance =====================

    @Test
    public void testGetInstance() {
        ManejadorCursos a = ManejadorCursos.GetInstance();
        ManejadorCursos b = ManejadorCursos.GetInstance();
        assertNotNull(a);
        assertSame(a, b);
    }

    // ===================== CrearCurso =====================

    @Test
    public void testCrearCurso() {
        Curso previa = mock(Curso.class);
        when(query.getResultList()).thenReturn(List.of(previa));

        Instituto instituto = mock(Instituto.class);
        UsuarioBase ub = mock(UsuarioBase.class);
        List<Categoria> categorias = new ArrayList<>();
        Date fecha = new Date();

        try (MockedConstruction<Curso> construido = mockConstruction(Curso.class)) {
            Curso result = manejador.CrearCurso(instituto, "Java", "desc", 10, 20.5f, 5,
                    "http://url", fecha, List.of("Previa1"), ub, categorias);

            assertNotNull(result);
            assertEquals(1, construido.constructed().size());
            assertSame(construido.constructed().get(0), result);
        }
    }

    @Test
    public void testCrearCursoSinPreviasPasaListaVacia() {
        Instituto instituto = mock(Instituto.class);
        UsuarioBase ub = mock(UsuarioBase.class);

        final List<Object> argumentos = new ArrayList<>();
        try (MockedConstruction<Curso> construido = mockConstruction(Curso.class,
                (mock, ctx) -> argumentos.addAll(ctx.arguments()))) {
            Curso result = manejador.CrearCurso(instituto, "Java", "desc", 10, 20.5f, 5,
                    "http://url", new Date(), new ArrayList<>(), ub, new ArrayList<>());

            assertNotNull(result);
            assertEquals(11, argumentos.size());
            assertSame(instituto, argumentos.get(0));
            assertEquals("Java", argumentos.get(1));
            assertEquals(new ArrayList<Curso>(), argumentos.get(8));
            assertSame(ub, argumentos.get(9));
        }
    }

    @Test
    public void testCrearCursoConPreviasBuscaCadaUna() {
        Curso previa = mock(Curso.class);
        when(query.getResultList()).thenReturn(List.of(previa));

        final List<Object> argumentos = new ArrayList<>();
        try (MockedConstruction<Curso> construido = mockConstruction(Curso.class,
                (mock, ctx) -> argumentos.addAll(ctx.arguments()))) {
            manejador.CrearCurso(mock(Instituto.class), "Java", "desc", 10, 20.5f, 5,
                    "http://url", new Date(), Arrays.asList("A", "B"),
                    mock(UsuarioBase.class), new ArrayList<>());

            assertEquals(List.of(previa, previa), argumentos.get(8));
        }
    }

    // ===================== ModificarCurso =====================

    @Test
    public void testModificarCursoOk() {
        Curso c = mock(Curso.class);
        Curso previa = mock(Curso.class);
        when(query.getResultList()).thenReturn(List.of(previa));
        UsuarioBase ub = mock(UsuarioBase.class);
        List<Categoria> categorias = new ArrayList<>();
        Date fecha = new Date();

        manejador.ModificarCurso(c, "nueva", 5, 10f, 3, "url", fecha, List.of("P1"), ub, categorias);

        verify(c).ModificarMisDatos("nueva", 5, 10f, 3, "url", fecha, List.of(previa), ub, categorias);
        verify(tx).begin();
        verify(em).merge(c);
        verify(tx).commit();
        verify(em, atLeastOnce()).close();
    }

    @Test
    public void testModificarCursoErrorHaceRollback() {
        Curso c = mock(Curso.class);
        when(em.merge(c)).thenThrow(new RuntimeException("fallo merge"));

        manejador.ModificarCurso(c, "d", 1, 1f, 1, "u", new Date(), new ArrayList<>(),
                mock(UsuarioBase.class), new ArrayList<>());

        verify(tx).rollback();
        verify(tx, never()).commit();
        verify(em).close();
    }

    @Test
    public void testModificarCursoErrorSinTransaccionActiva() {
        Curso c = mock(Curso.class);
        when(em.merge(c)).thenThrow(new RuntimeException("fallo merge"));
        when(tx.isActive()).thenReturn(false);

        manejador.ModificarCurso(c, "d", 1, 1f, 1, "u", new Date(), new ArrayList<>(),
                mock(UsuarioBase.class), new ArrayList<>());

        verify(tx, never()).rollback();
        verify(em).close();
    }

    // ===================== Add =====================

    @Test
    public void testAddOk() throws Exception {
        Curso c = mock(Curso.class);

        manejador.Add(c);

        verify(tx).begin();
        verify(em).persist(c);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testAddErrorSinCausa() {
        Curso c = mock(Curso.class);
        doThrow(new RuntimeException("sin causa")).when(em).persist(c);

        Exception ex = assertThrows(Exception.class, () -> manejador.Add(c));

        assertTrue(ex.getMessage().contains("Error al guardar el curso"));
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddErrorConCausa() {
        Curso c = mock(Curso.class);
        doThrow(new RuntimeException("top", new RuntimeException("causa")))
                .when(em).persist(c);

        assertThrows(Exception.class, () -> manejador.Add(c));
        verify(tx).rollback();
    }

    @Test
    public void testAddErrorConCausaYCausaRaiz() {
        Curso c = mock(Curso.class);
        doThrow(new RuntimeException("top", new RuntimeException("causa", new RuntimeException("sql"))))
                .when(em).persist(c);
        when(tx.isActive()).thenReturn(false);

        assertThrows(Exception.class, () -> manejador.Add(c));
        verify(tx, never()).rollback();
        verify(em).close();
    }

    // ===================== BuscarCurso =====================

    @Test
    public void testBuscarCursoEncontrado() {
        Curso c = mock(Curso.class);
        when(query.getResultList()).thenReturn(List.of(c));
        when(query.getSingleResult()).thenReturn(c);

        Curso result = manejador.BuscarCurso("Java");

        assertSame(c, result);
        // 1 query inicial + misEdiciones + misProgramas + misCategorias
        verify(em, times(4)).createQuery(anyString(), eq(Curso.class));
        verify(em).close();
    }

    @Test
    public void testBuscarCursoNoEncontrado() {
        when(query.getResultList()).thenReturn(new ArrayList<>());

        Curso result = manejador.BuscarCurso("Inexistente");

        assertNull(result);
        verify(em, times(1)).createQuery(anyString(), eq(Curso.class));
        verify(em).close();
    }

    @Test
    public void testBuscarCursoConExcepcion() {
        when(em.createQuery(anyString(), eq(Curso.class))).thenThrow(new RuntimeException("boom"));

        Curso result = manejador.BuscarCurso("Java");

        assertNull(result);
        verify(em).close();
    }

    // ===================== getList =====================

    @Test
    public void testGetListConResultados() {
        Curso c = mock(Curso.class);
        when(query.getResultList()).thenReturn(List.of(c));

        List<Curso> result = manejador.getList();

        assertEquals(1, result.size());
        assertSame(c, result.get(0));
        verify(em, times(4)).createQuery(anyString(), eq(Curso.class));
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testGetListVacia() {
        when(query.getResultList()).thenReturn(new ArrayList<>());

        List<Curso> result = manejador.getList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(em, times(1)).createQuery(anyString(), eq(Curso.class));
        verify(tx).commit();
    }

    @Test
    public void testGetListConExcepcionHaceRollback() {
        when(em.createQuery(anyString(), eq(Curso.class))).thenThrow(new RuntimeException("boom"));

        List<Curso> result = manejador.getList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testGetListConExcepcionSinTransaccionActiva() {
        when(em.createQuery(anyString(), eq(Curso.class))).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);

        List<Curso> result = manejador.getList();

        assertTrue(result.isEmpty());
        verify(tx, never()).rollback();
    }

    // ===================== getDT =====================

    private Curso cursoCompleto() {
        Curso c = mock(Curso.class, RETURNS_DEEP_STUBS);
        when(c.getInstituto().getNombre()).thenReturn("INCO");
        when(c.getNombre()).thenReturn("Java");
        when(c.getDescripcion()).thenReturn("desc");
        when(c.getURL()).thenReturn("url");
        when(c.getDocente().getNickname()).thenReturn("profe");

        Curso previa = mock(Curso.class);
        when(previa.getNombre()).thenReturn("Base");
        when(c.getPrevias()).thenReturn(List.of(previa));

        EdicionCurso ed = mock(EdicionCurso.class);
        when(ed.getNombre()).thenReturn("Java 2026");
        when(c.getEdiciones()).thenReturn(Arrays.asList(ed, null));

        ProgramaDeFormacion prog = mock(ProgramaDeFormacion.class);
        when(prog.getNombre()).thenReturn("Programa A");
        when(c.getProgramas()).thenReturn(Arrays.asList(prog, null));

        Categoria cat = mock(Categoria.class);
        when(cat.getNombre()).thenReturn("Backend");
        when(c.getCategorias()).thenReturn(Arrays.asList(cat, null));
        return c;
    }

    @Test
    public void testGetDTCompleto() {
        Curso c = cursoCompleto();
        final List<Object> args = new ArrayList<>();

        try (MockedConstruction<DTCurso> dt = mockConstruction(DTCurso.class,
                (mock, ctx) -> args.addAll(ctx.arguments()))) {
            DTCurso result = manejador.getDT(c);

            assertNotNull(result);
            assertEquals(13, args.size());
            assertEquals("INCO", args.get(0));
            assertEquals("Java", args.get(1));
            assertEquals("desc", args.get(2));
            assertEquals("url", args.get(6));
            assertEquals(List.of("Base"), args.get(8));
            assertEquals(List.of("Java 2026"), args.get(9));
            assertEquals(List.of("Programa A"), args.get(10));
            assertEquals(List.of("Backend"), args.get(11));
            assertEquals("profe", args.get(12));
        }
    }

    @Test
    public void testGetDTConColeccionesNulasYSinInstituto() {
        Curso c = mock(Curso.class, RETURNS_DEEP_STUBS);
        when(c.getInstituto()).thenReturn(null);
        when(c.getPrevias()).thenReturn(null);
        when(c.getEdiciones()).thenReturn(null);
        when(c.getProgramas()).thenReturn(null);
        when(c.getCategorias()).thenReturn(null);
        when(c.getDocente().getNickname()).thenReturn("profe");

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTCurso> dt = mockConstruction(DTCurso.class,
                (mock, ctx) -> args.addAll(ctx.arguments()))) {
            DTCurso result = manejador.getDT(c);

            assertNotNull(result);
            assertEquals("", args.get(0));
            assertEquals(new ArrayList<String>(), args.get(8));
            assertEquals(new ArrayList<String>(), args.get(9));
            assertEquals(new ArrayList<String>(), args.get(10));
            assertEquals(new ArrayList<String>(), args.get(11));
        }
    }

    // ===================== getDTList / getDTLIst =====================

    @Test
    public void testGetDTListConCursos() {
        ManejadorCursos spy = spy(manejador);
        Curso c1 = mock(Curso.class);
        Curso c2 = mock(Curso.class);
        DTCurso dt = mock(DTCurso.class);
        doReturn(Arrays.asList(c1, c2)).when(spy).getList();
        doReturn(dt).when(spy).getDT(any(Curso.class));

        List<DTMaster> result = spy.getDTList();

        assertEquals(2, result.size());
        assertSame(dt, result.get(0));
    }

    @Test
    public void testGetDTListConListaNula() {
        ManejadorCursos spy = spy(manejador);
        doReturn(null).when(spy).getList();

        List<DTMaster> result = spy.getDTList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetDTLIstFiltraPorInstituto() {
        ManejadorCursos spy = spy(manejador);

        Curso delInstituto = mock(Curso.class, RETURNS_DEEP_STUBS);
        when(delInstituto.getInstituto().getNombre()).thenReturn("INCO");
        Curso deOtro = mock(Curso.class, RETURNS_DEEP_STUBS);
        when(deOtro.getInstituto().getNombre()).thenReturn("OTRO");
        Curso sinInstituto = mock(Curso.class);
        when(sinInstituto.getInstituto()).thenReturn(null);

        DTCurso dt = mock(DTCurso.class);
        doReturn(Arrays.asList(delInstituto, deOtro, sinInstituto)).when(spy).getList();
        doReturn(dt).when(spy).getDT(any(Curso.class));

        List<DTMaster> result = spy.getDTLIst("INCO");

        assertEquals(1, result.size());
        assertSame(dt, result.get(0));
        verify(spy, times(1)).getDT(any(Curso.class));
    }

    @Test
    public void testGetDTLIstConListaNula() {
        ManejadorCursos spy = spy(manejador);
        doReturn(null).when(spy).getList();

        List<DTMaster> result = spy.getDTLIst("INCO");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}