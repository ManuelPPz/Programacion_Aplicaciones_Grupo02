package Manejadores;

import Classes.Curso;
import Classes.Edi_Usu;
import Classes.EdicionCurso;
import Classes.Enum_Estado_inscripcion;
import Classes.Id_Valoracion;
import Classes.Usuario;
import Classes.ValoracionCurso;
import DTsClasses.DTValoracionCurso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import util.JPAUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Tests de ManejadorValoraciones (JPAUtil mockeado, sin base de datos). */
public class ManejadorValoracionesTest {

    private final Date fecha = new Date();
    private MockedStatic<JPAUtil> jpaUtil;
    private EntityManager em;
    private EntityTransaction tx;
    private TypedQuery<ValoracionCurso> query;
    private ManejadorValoraciones manejador;
    private Usuario ana;
    private Curso java;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        query = mock(TypedQuery.class, RETURNS_SELF);
        when(em.getTransaction()).thenReturn(tx);
        when(em.createQuery(anyString(), eq(ValoracionCurso.class))).thenReturn(query);
        jpaUtil = mockStatic(JPAUtil.class);
        jpaUtil.when(JPAUtil::getEntityManager).thenReturn(em);
        manejador = ManejadorValoraciones.GetInstance();

        ana = new Usuario("ana", "Ana", "Paz", "a@x.com", "p", fecha, null);
        java = curso("Java");
        when(em.find(Usuario.class, "ana")).thenReturn(ana);
        when(em.find(Curso.class, "Java")).thenReturn(java);
    }

    @AfterEach
    public void tearDown() {
        jpaUtil.close();
    }

    private Curso curso(String nombre) {
        return new Curso(null, nombre, "d", 1, 1f, 1, "u", fecha, new ArrayList<>(), null, new ArrayList<>(), null);
    }

    private void inscribir(Usuario u, String edicion, Curso c, Enum_Estado_inscripcion estado) {
        Edi_Usu eu = new Edi_Usu(u, new EdicionCurso(edicion, null, c, fecha, fecha, 10, fecha, new ArrayList<>(), null), fecha);
        eu.setEstado(estado);
        u.AddEdicionCurso(eu);
    }

    @Test
    public void testGetInstance() {
        assertSame(ManejadorValoraciones.GetInstance(), ManejadorValoraciones.GetInstance());
    }

    @Test
    public void testCursosParticipaSoloAceptadasYSinRepetir() {
        assertTrue(ManejadorValoraciones.cursosParticipa(null).isEmpty());
        assertTrue(ManejadorValoraciones.cursosParticipa(ana).isEmpty());
        inscribir(ana, "Java 1", java, Enum_Estado_inscripcion.ACEPTADO);
        inscribir(ana, "Java 2", java, Enum_Estado_inscripcion.ACEPTADO);
        inscribir(ana, "Web 1", curso("Web"), Enum_Estado_inscripcion.INSCRIPTO);
        inscribir(ana, "BD 1", curso("BD"), Enum_Estado_inscripcion.RECHAZADO);
        inscribir(ana, "Redes 1", curso("Redes"), Enum_Estado_inscripcion.ACEPTADO);
        ana.AddEdicionCurso(null);
        assertEquals(List.of("Java", "Redes"), ManejadorValoraciones.cursosParticipa(ana));
    }

    @Test
    public void testValorarOk() throws Exception {
        inscribir(ana, "Java 1", java, Enum_Estado_inscripcion.ACEPTADO);
        manejador.Valorar("ana", "Java", 4);
        verify(em).persist(argThat(o -> o instanceof ValoracionCurso v && v.getPuntaje() == 4
                && v.getUsuario() == ana && v.getCurso() == java
                && v.getId().equals(new Id_Valoracion("ana", "Java"))));
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    public void testValorarPuntajeFueraDeRango() {
        assertThrows(Exception.class, () -> manejador.Valorar("ana", "Java", 0));
        assertThrows(Exception.class, () -> manejador.Valorar("ana", "Java", 6));
        verify(em, never()).persist(any());
    }

    @Test
    public void testValorarErroresHacenRollback() {
        when(tx.isActive()).thenReturn(true);
        Exception e = assertThrows(Exception.class, () -> manejador.Valorar("nadie", "Java", 3));
        assertEquals("Solo los estudiantes pueden valorar cursos.", e.getMessage());
        assertThrows(Exception.class, () -> manejador.Valorar(null, "Java", 3));
        e = assertThrows(Exception.class, () -> manejador.Valorar("ana", "Nada", 3));
        assertEquals("No existe el curso 'Nada'.", e.getMessage());
        assertThrows(Exception.class, () -> manejador.Valorar("ana", null, 3));
        e = assertThrows(Exception.class, () -> manejador.Valorar("ana", "Java", 3));
        assertEquals("Solo podés valorar cursos en los que participaste.", e.getMessage());

        inscribir(ana, "Java 1", java, Enum_Estado_inscripcion.ACEPTADO);
        when(em.find(eq(ValoracionCurso.class), any())).thenReturn(new ValoracionCurso(ana, java, 2));
        e = assertThrows(Exception.class, () -> manejador.Valorar("ana", "Java", 3));
        assertEquals("Ya valoraste el curso 'Java'.", e.getMessage());

        verify(em, never()).persist(any());
        verify(tx, times(6)).rollback();
        verify(em, times(6)).close();
    }

    @Test
    public void testValorarSinTransaccionActivaNoHaceRollback() {
        when(tx.isActive()).thenReturn(false);
        assertThrows(Exception.class, () -> manejador.Valorar("nadie", "Java", 3));
        verify(tx, never()).rollback();
    }

    @Test
    public void testResumen() {
        Usuario beto = new Usuario("beto", "B", "B", "b@x.com", "p", fecha, null);
        Usuario caro = new Usuario("caro", "C", "C", "c@x.com", "p", fecha, null);
        when(query.getResultList()).thenReturn(List.of(
                new ValoracionCurso(ana, java, 5), new ValoracionCurso(beto, java, 3),
                new ValoracionCurso(caro, java, 5), new ValoracionCurso(caro, curso("X"), 9)));

        DTValoracionCurso dt = manejador.Resumen("Java", "beto");
        assertEquals("Java", dt.getCurso());
        assertEquals(3, dt.getTotal());
        assertArrayEquals(new int[]{0, 0, 1, 0, 2}, dt.getEstrellas());
        assertEquals(13f / 3, dt.getPromedio(), 0.0001);
        assertEquals(3, dt.getMiPuntaje());
        verify(query, atLeastOnce()).setParameter("curso", "Java");

        assertEquals(0, manejador.Resumen("Java", null).getMiPuntaje());
        assertEquals(0, manejador.Resumen("Java", "zoe").getMiPuntaje());
        verify(em, times(3)).close();
    }

    @Test
    public void testResumenSinValoraciones() {
        when(query.getResultList()).thenReturn(List.of());
        DTValoracionCurso dt = manejador.Resumen("Java", "ana");
        assertEquals(0, dt.getTotal());
        assertEquals(0f, dt.getPromedio());
        assertArrayEquals(new int[5], dt.getEstrellas());
    }
}
