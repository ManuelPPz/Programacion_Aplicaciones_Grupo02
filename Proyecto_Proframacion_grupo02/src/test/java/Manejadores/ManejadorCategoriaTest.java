package Manejadores;

import Classes.Categoria;
import DTsClasses.DTCategoria;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import util.JPAUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests de ManejadorCategoria. JPAUtil.getEntityManager() se mockea para no
 * tocar la base de datos real.
 */
public class ManejadorCategoriaTest {

    private MockedStatic<JPAUtil> jpa;
    private EntityManager em;
    private EntityTransaction tx;
    private TypedQuery<Categoria> query;
    private ManejadorCategoria instance;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        query = mock(TypedQuery.class);
        when(em.getTransaction()).thenReturn(tx);
        when(em.createQuery(anyString(), eq(Categoria.class))).thenReturn(query);

        jpa = mockStatic(JPAUtil.class);
        jpa.when(JPAUtil::getEntityManager).thenReturn(em);

        instance = ManejadorCategoria.GetInstance();
    }

    @AfterEach
    public void tearDown() {
        jpa.close();
    }

    private Categoria categoria(String nombre) {
        Categoria c = mock(Categoria.class);
        when(c.getNombre()).thenReturn(nombre);
        return c;
    }

    // ----------------------------------------------------------- Singleton

    @Test
    public void testGetInstance() {
        ManejadorCategoria a = ManejadorCategoria.GetInstance();
        ManejadorCategoria b = ManejadorCategoria.GetInstance();

        assertNotNull(a);
        assertSame(a, b);
    }

    // ------------------------------------------------------- CrearCategoria

    @Test
    public void testCrearCategoria() {
        Categoria c = instance.CrearCategoria("Programacion");

        assertNotNull(c);
        assertEquals("Programacion", c.getNombre());
    }

    // ------------------------------------------------------------------ Add

    @Test
    public void testAddOk() throws Exception {
        Categoria c = categoria("Redes");

        instance.Add(c);

        verify(tx).begin();
        verify(em).persist(c);
        verify(tx).commit();
        verify(tx, never()).rollback();
        verify(em).close();
    }

    @Test
    public void testAddErrorConTransaccionActiva() {
        Categoria c = categoria("Redes");
        doThrow(new RuntimeException("duplicada")).when(em).persist(c);
        when(tx.isActive()).thenReturn(true);

        Exception ex = assertThrows(Exception.class, () -> instance.Add(c));

        assertEquals("Error al guardar la categoria: duplicada", ex.getMessage());
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddErrorSinTransaccionActiva() {
        Categoria c = categoria("Redes");
        doThrow(new RuntimeException("fallo")).when(em).persist(c);
        when(tx.isActive()).thenReturn(false);

        assertThrows(Exception.class, () -> instance.Add(c));

        verify(tx, never()).rollback();
        verify(em).close();
    }

    // --------------------------------------------------------------- getList

    @Test
    public void testGetListOk() throws Exception {
        List<Categoria> esperadas = Arrays.asList(categoria("A"), categoria("B"));
        when(query.getResultList()).thenReturn(esperadas);

        List<Categoria> result = instance.getList();

        assertSame(esperadas, result);
        verify(em).createQuery("SELECT c FROM Categoria c", Categoria.class);
        verify(em).close();
    }

    @Test
    public void testGetListError() {
        when(em.createQuery(anyString(), eq(Categoria.class))).thenThrow(new RuntimeException("sin conexion"));

        Exception ex = assertThrows(Exception.class, () -> instance.getList());

        assertEquals("Error al obtener la lista de categorías: sin conexion", ex.getMessage());
        verify(em).close();
    }

    // ------------------------------------------------------- BuscarCategoria

    @Test
    public void testBuscarCategoriaExiste() {
        Categoria c = categoria("Redes");
        when(em.find(Categoria.class, "Redes")).thenReturn(c);

        assertSame(c, instance.BuscarCategoria("Redes"));
        verify(em).close();
    }

    @Test
    public void testBuscarCategoriaNoExiste() {
        when(em.find(Categoria.class, "Nada")).thenReturn(null);

        assertNull(instance.BuscarCategoria("Nada"));
        verify(em).close();
    }

    @Test
    public void testBuscarCategoriaError() {
        when(em.find(Categoria.class, "Redes")).thenThrow(new RuntimeException("fallo"));

        assertNull(instance.BuscarCategoria("Redes"));
        verify(em).close();
    }

    // ----------------------------------------------------------------- getDT

    @Test
    public void testGetDT() {
        DTCategoria dt = instance.getDT(categoria("Redes"));

        assertNotNull(dt);
    }

    // ------------------------------------------------------------- getDTList

    @Test
    public void testGetDTListIgnoraCategoriasSinNombre() throws Exception {
        List<Categoria> categorias = Arrays.asList(categoria("A"), categoria(null), categoria("B"));
        when(query.getResultList()).thenReturn(categorias);

        List<DTMaster> result = instance.getDTList();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetDTListListaNull() throws Exception {
        when(query.getResultList()).thenReturn(null);

        List<DTMaster> result = instance.getDTList();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetDTListVacia() throws Exception {
        when(query.getResultList()).thenReturn(Arrays.asList());

        assertTrue(instance.getDTList().isEmpty());
    }

    @Test
    public void testGetDTListPropagaErrorDeGetList() {
        when(em.createQuery(anyString(), eq(Categoria.class))).thenThrow(new RuntimeException("sin conexion"));

        assertThrows(Exception.class, () -> instance.getDTList());
    }
}