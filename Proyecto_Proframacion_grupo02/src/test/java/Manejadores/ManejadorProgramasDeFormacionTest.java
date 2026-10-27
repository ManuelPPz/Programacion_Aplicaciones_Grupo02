package Manejadores;

import Classes.Categoria;
import Classes.Curso;
import Classes.Prog_Usu;
import Classes.ProgramaDeFormacion;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios de ManejadorProgramasDeFormacion (JPAUtil mockeado, sin base de datos).
 */
public class ManejadorProgramasDeFormacionTest {

    private MockedStatic<JPAUtil> jpaUtil;
    private EntityManager em;
    private EntityTransaction tx;
    private ManejadorProgramasDeFormacion manejador;

    @BeforeEach
    public void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);
        when(em.isOpen()).thenReturn(true);

        jpaUtil = mockStatic(JPAUtil.class);
        jpaUtil.when(JPAUtil::getEntityManager).thenReturn(em);

        manejador = ManejadorProgramasDeFormacion.GetInstance();
    }

    @AfterEach
    public void tearDown() {
        jpaUtil.close();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(ManejadorProgramasDeFormacion.GetInstance());
        assertSame(ManejadorProgramasDeFormacion.GetInstance(), ManejadorProgramasDeFormacion.GetInstance());
    }

    @Test
    public void testCrearPrograma() {
        try (MockedConstruction<ProgramaDeFormacion> construido = mockConstruction(ProgramaDeFormacion.class)) {
            ProgramaDeFormacion result = manejador.CrearPrograma("P", "desc", null, new Date());

            assertNotNull(result);
            assertSame(construido.constructed().get(0), result);
        }
    }

    // ===================== ModificarDatos =====================

    @Test
    public void testModificarDatosOk() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(em.find(ProgramaDeFormacion.class, "P")).thenReturn(pdf);
        Date d = new Date();

        manejador.ModificarDatos("P", "nueva", null, d);

        verify(pdf).ModificarDatos("nueva", null, d);
        verify(em).merge(pdf);
        verify(tx).commit();
    }

    @Test
    public void testModificarDatosProgramaInexistente() {
        when(em.find(ProgramaDeFormacion.class, "P")).thenReturn(null);

        manejador.ModificarDatos("P", "nueva", null, new Date());

        verify(em, never()).merge(any());
    }

    @Test
    public void testModificarDatosErrorHaceRollback() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(em.find(ProgramaDeFormacion.class, "P")).thenReturn(pdf);
        when(em.merge(pdf)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> manejador.ModificarDatos("P", "d", null, new Date()));
        verify(tx).rollback();
    }

    @Test
    public void testModificarDatosErrorSinTransaccionActiva() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(em.find(ProgramaDeFormacion.class, "P")).thenReturn(pdf);
        when(em.merge(pdf)).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> manejador.ModificarDatos("P", "d", null, new Date()));
        verify(tx, never()).rollback();
    }

    // ===================== Add =====================

    @Test
    public void testAddOk() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);

        manejador.Add(pdf);

        verify(em).persist(pdf);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testAddErrorHaceRollback() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        doThrow(new RuntimeException("boom")).when(em).persist(pdf);

        assertThrows(RuntimeException.class, () -> manejador.Add(pdf));
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddErrorSinTransaccionActiva() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        doThrow(new RuntimeException("boom")).when(em).persist(pdf);
        when(tx.isActive()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> manejador.Add(pdf));
        verify(tx, never()).rollback();
    }

    // ===================== AddUsuarioInscripto =====================

    @Test
    public void testAddUsuarioInscriptoNulo() throws Exception {
        manejador.AddUsuarioInscripto(null);
        verifyNoInteractions(em);
    }

    @Test
    public void testAddUsuarioInscriptoSinId() throws Exception {
        Prog_Usu pu = mock(Prog_Usu.class);
        when(pu.getId()).thenReturn(null);

        manejador.AddUsuarioInscripto(pu);

        verifyNoInteractions(em);
    }

    @Test
    public void testAddUsuarioInscriptoOk() throws Exception {
        Prog_Usu pu = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        ProgramaDeFormacion programa = pu.getId().getPrograma();
        Prog_Usu managed = mock(Prog_Usu.class);
        when(em.merge(pu)).thenReturn(managed);

        manejador.AddUsuarioInscripto(pu);

        verify(programa).AddUsuarioInscripto(managed);
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testAddUsuarioInscriptoSinPrograma() throws Exception {
        Prog_Usu pu = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        when(pu.getId().getPrograma()).thenReturn(null);
        when(em.merge(pu)).thenReturn(mock(Prog_Usu.class));

        manejador.AddUsuarioInscripto(pu);

        verify(tx).commit();
    }

    @Test
    public void testAddUsuarioInscriptoErrorHaceRollback() {
        Prog_Usu pu = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        when(em.merge(pu)).thenThrow(new RuntimeException("boom"));

        Exception ex = assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(pu));

        assertTrue(ex.getMessage().contains("Error al guardar la inscripción"));
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddUsuarioInscriptoErrorSinTransaccionActiva() {
        Prog_Usu pu = mock(Prog_Usu.class, RETURNS_DEEP_STUBS);
        when(em.merge(pu)).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);

        assertThrows(Exception.class, () -> manejador.AddUsuarioInscripto(pu));
        verify(tx, never()).rollback();
    }

    // ===================== BuscarPrograma =====================

    @Test
    public void testBuscarProgramaEncontrado() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(em.find(ProgramaDeFormacion.class, "P")).thenReturn(pdf);

        assertSame(pdf, manejador.BuscarPrograma("P"));
        verify(em).close();
    }

    @Test
    public void testBuscarProgramaConExcepcion() {
        when(em.find(ProgramaDeFormacion.class, "P")).thenThrow(new RuntimeException("boom"));

        assertNull(manejador.BuscarPrograma("P"));
        verify(em).close();
    }

    @Test
    public void testBuscarProgramaEmCerrado() {
        when(em.isOpen()).thenReturn(false);

        assertNull(manejador.BuscarPrograma("P"));
        verify(em, never()).close();
    }

    // ===================== AddCurso =====================

    @Test
    public void testAddCursoParametrosNulos() {
        manejador.AddCurso(null, mock(Curso.class));
        manejador.AddCurso(mock(ProgramaDeFormacion.class), null);

        verifyNoInteractions(em);
    }

    @Test
    public void testAddCursoOk() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(pdf.getNombre()).thenReturn("P");
        Curso c = mock(Curso.class);
        when(c.getNombre()).thenReturn("C");
        ProgramaDeFormacion pdfMerged = mock(ProgramaDeFormacion.class);
        Curso cMerged = mock(Curso.class);
        when(em.find(ProgramaDeFormacion.class, "P")).thenReturn(pdfMerged);
        when(em.find(Curso.class, "C")).thenReturn(cMerged);

        manejador.AddCurso(pdf, c);

        verify(pdfMerged).AddCurso(cMerged);
        verify(em).merge(pdfMerged);
        verify(em).merge(cMerged);
        verify(tx).commit();
        verify(pdf).AddCurso(c);
        verify(em).close();
    }

    @Test
    public void testAddCursoEntidadesNoEncontradas() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(pdf.getNombre()).thenReturn("P");
        Curso c = mock(Curso.class);
        when(c.getNombre()).thenReturn("C");
        when(em.find(ProgramaDeFormacion.class, "P")).thenReturn(null);

        manejador.AddCurso(pdf, c);

        verify(em, never()).merge(any());
        verify(tx).commit();
        verify(pdf).AddCurso(c);
    }

    @Test
    public void testAddCursoErrorHaceRollback() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(pdf.getNombre()).thenReturn("P");
        Curso c = mock(Curso.class);
        when(em.find(ProgramaDeFormacion.class, "P")).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> manejador.AddCurso(pdf, c));
        verify(tx).rollback();
        verify(em).close();
    }

    @Test
    public void testAddCursoErrorSinTransaccionActivaYEmCerrado() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(pdf.getNombre()).thenReturn("P");
        Curso c = mock(Curso.class);
        when(em.find(ProgramaDeFormacion.class, "P")).thenThrow(new RuntimeException("boom"));
        when(tx.isActive()).thenReturn(false);
        when(em.isOpen()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> manejador.AddCurso(pdf, c));
        verify(tx, never()).rollback();
        verify(em, never()).close();
    }

    // ===================== getDT =====================

    private Categoria categoria(String nombre) {
        Categoria cat = mock(Categoria.class);
        when(cat.getNombre()).thenReturn(nombre);
        return cat;
    }

    private Curso curso(String nombre, List<Categoria> categorias) {
        Curso c = mock(Curso.class);
        when(c.getNombre()).thenReturn(nombre);
        when(c.getCategorias()).thenReturn(categorias);
        return c;
    }

    @Test
    public void testGetDTNulo() {
        assertNull(manejador.getDT(null));
    }

    @Test
    public void testGetDTConCursosYCategorias() {
        Date fecha = new Date();
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(pdf.getNombre()).thenReturn("P");
        when(pdf.getDescripcion()).thenReturn("D");
        when(pdf.getVigencia()).thenReturn(null);
        when(pdf.getFAlta()).thenReturn(fecha);

        // Pre-crear categorías y cursos para evitar anidamiento de when(...)
        Categoria catA = categoria("A");
        Categoria catB = categoria("B");

        Curso c1 = curso("C1", Arrays.asList(catA, catB));
        Curso c2 = curso("C2", List.of(catA));
        Curso c3 = curso("C3", null);

        when(pdf.getCursos()).thenReturn(Arrays.asList(c1, null, c2, c3));

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTProgramaForm> dt = mockConstruction(DTProgramaForm.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            assertNotNull(manejador.getDT(pdf));

            assertEquals("P", args.get(0));
            assertEquals("D", args.get(1));
            assertEquals(List.of("C1", "C2", "C3"), args.get(3));
            assertSame(fecha, args.get(4));
            assertEquals(List.of("A", "B"), args.get(5));
        }
    }

    @Test
    public void testGetDTSinCursos() {
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(pdf.getCursos()).thenReturn(null);

        final List<Object> args = new ArrayList<>();
        try (MockedConstruction<DTProgramaForm> dt = mockConstruction(DTProgramaForm.class,
                (m, ctx) -> args.addAll(ctx.arguments()))) {
            assertNotNull(manejador.getDT(pdf));

            assertEquals(new ArrayList<String>(), args.get(3));
            assertEquals(new ArrayList<String>(), args.get(5));
        }
    }

    // ===================== getDTList (usa getList privado) =====================

    @SuppressWarnings("unchecked")
    private TypedQuery<ProgramaDeFormacion> queryProgramas() {
        TypedQuery<ProgramaDeFormacion> q = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(ProgramaDeFormacion.class))).thenReturn(q);
        return q;
    }

    @Test
    public void testGetDTListConProgramas() {
        TypedQuery<ProgramaDeFormacion> q = queryProgramas();
        ProgramaDeFormacion pdf = mock(ProgramaDeFormacion.class);
        when(q.getResultList()).thenReturn(Arrays.asList(pdf, null));

        try (MockedConstruction<DTProgramaForm> dt = mockConstruction(DTProgramaForm.class)) {
            List<DTMaster> result = manejador.getDTList();

            assertEquals(1, result.size());
            assertSame(dt.constructed().get(0), result.get(0));
        }
        verify(em).close();
    }

    @Test
    public void testGetDTListVacia() {
        TypedQuery<ProgramaDeFormacion> q = queryProgramas();
        when(q.getResultList()).thenReturn(new ArrayList<>());

        assertTrue(manejador.getDTList().isEmpty());
    }

    @Test
    public void testGetDTListConExcepcionCierraEm() {
        when(em.createQuery(anyString(), eq(ProgramaDeFormacion.class))).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> manejador.getDTList());
        verify(em).close();
    }
}