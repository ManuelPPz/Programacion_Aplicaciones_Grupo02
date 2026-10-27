package Manejadores;

import Classes.Instituto;
import DTsClasses.DTInstituto;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Tests unitarios de ManejadorInstituto (JPAUtil mockeado, sin base de datos).
 */
public class ManejadorInstitutoTest {

    private MockedStatic<JPAUtil> jpaUtil;
    private EntityManager em;
    private EntityTransaction tx;
    private ManejadorInstituto manejador;

    @BeforeEach
    public void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);
        when(em.isOpen()).thenReturn(true);

        jpaUtil = mockStatic(JPAUtil.class);
        jpaUtil.when(JPAUtil::getEntityManager).thenReturn(em);

        manejador = ManejadorInstituto.GetInstance();
    }

    @AfterEach
    public void tearDown() {
        jpaUtil.close();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(ManejadorInstituto.GetInstance());
        assertSame(ManejadorInstituto.GetInstance(), ManejadorInstituto.GetInstance());
    }

    @Test
    public void testCreaInstituto() {
        try (MockedConstruction<Instituto> construido = mockConstruction(Instituto.class)) {
            Instituto result = manejador.CreaInstituto("INCO");

            assertNotNull(result);
            assertSame(construido.constructed().get(0), result);
        }
    }

    // ===================== Add =====================

    @Test
    public void testAddOk() throws Exception {
        Instituto i = mock(Instituto.class);

        manejador.Add(i);

        verify(tx).begin();
        verify(em).persist(i);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testAddErrorHaceRollback() {
        Instituto i = mock(Instituto.class);
        doThrow(new RuntimeException("boom")).when(em).persist(i);

        Exception ex = assertThrows(Exception.class, () -> manejador.Add(i));

        assertTrue(ex.getMessage().contains("Error al guardar el instituto"));
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddErrorSinTransaccionActiva() {
        Instituto i = mock(Instituto.class);
        doThrow(new RuntimeException("boom")).when(em).persist(i);
        when(tx.isActive()).thenReturn(false);

        assertThrows(Exception.class, () -> manejador.Add(i));
        verify(tx, never()).rollback();
    }

    // ===================== BuscarInstituto =====================

    @Test
    public void testBuscarInstitutoEncontrado() {
        Instituto i = mock(Instituto.class);
        when(em.find(Instituto.class, "INCO")).thenReturn(i);

        assertSame(i, manejador.BuscarInstituto("INCO"));
        verify(em).close();
    }

    @Test
    public void testBuscarInstitutoConExcepcion() {
        when(em.find(Instituto.class, "INCO")).thenThrow(new RuntimeException("boom"));

        assertNull(manejador.BuscarInstituto("INCO"));
        verify(em).close();
    }

    @Test
    public void testBuscarInstitutoEmCerrado() {
        when(em.isOpen()).thenReturn(false);

        assertNull(manejador.BuscarInstituto("INCO"));
        verify(em, never()).close();
    }

    // ===================== getDT =====================

    @Test
    public void testGetDTNulo() {
        assertNull(manejador.getDT(null));
    }

    @Test
    public void testGetDT() {
        Instituto i = mock(Instituto.class);
        when(i.getNombre()).thenReturn("INCO");

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTInstituto> dt = mockConstruction(DTInstituto.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            DTInstituto result = manejador.getDT(i);

            assertNotNull(result);
            assertEquals(List.of("INCO"), args);
        }
    }

    // ===================== getList =====================

    @SuppressWarnings("unchecked")
    private TypedQuery<Instituto> queryInstitutos() {
        TypedQuery<Instituto> q = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Instituto.class))).thenReturn(q);
        return q;
    }

    @Test
    public void testGetList() {
        TypedQuery<Instituto> q = queryInstitutos();
        Instituto i = mock(Instituto.class);
        when(q.getResultList()).thenReturn(List.of(i));

        List<Instituto> result = manejador.getList();

        assertEquals(List.of(i), result);
        verify(em).close();
    }

    @Test
    public void testGetListConExcepcionCierraEm() {
        when(em.createQuery(anyString(), eq(Instituto.class))).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> manejador.getList());
        verify(em).close();
    }

    // ===================== getDTList =====================

    @Test
    public void testGetDTListConInstitutos() {
        ManejadorInstituto spy = spy(manejador);
        Instituto i1 = mock(Instituto.class);
        Instituto i2 = mock(Instituto.class);
        DTInstituto dt = mock(DTInstituto.class);
        doReturn(Arrays.asList(i1, i2)).when(spy).getList();
        doReturn(dt).when(spy).getDT(any(Instituto.class));

        List<DTMaster> result = spy.getDTList();

        assertEquals(2, result.size());
        assertSame(dt, result.get(0));
    }

    @Test
    public void testGetDTListListaNula() {
        ManejadorInstituto spy = spy(manejador);
        doReturn(null).when(spy).getList();

        List<DTMaster> result = spy.getDTList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}