/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Manejadores;

import Classes.Curso;
import Classes.Instituto;
import Classes.UsuarioBase;
import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias avanzadas para la clase ManejadorCursos.
 * @author manuelpalumbo
 */
public class ManejadorCursosTest {
    
    private ManejadorCursos manejador;
    private ManejadorInstituto manejadorInstituto;

    public ManejadorCursosTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        manejador = ManejadorCursos.GetInstance();
        manejadorInstituto = ManejadorInstituto.GetInstance();
    }
    
    @AfterEach
    public void tearDown() {
        manejador = null;
        manejadorInstituto = null;
    }

    @Test
    public void testGetInstance() {
        System.out.println("GetInstance");
        ManejadorCursos result1 = ManejadorCursos.GetInstance();
        ManejadorCursos result2 = ManejadorCursos.GetInstance();
        
        assertNotNull(result1);
        assertSame(result1, result2, "ManejadorCursos debe ser una instancia única (Singleton)");
    }

    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        EntityManager em = ManejadorCursos.getEntityManager();
        assertNotNull(em);
        if (em.isOpen()) {
            em.close();
        }
    }

    @Test
    public void testCargarDeBaseDeDatos() {
        System.out.println("CargarDeBaseDeDatos");
        assertDoesNotThrow(() -> manejador.CargarDeBaseDeDatos());
    }

    @Test
    public void testCrearCursoConPreviasYNulas() {
        System.out.println("CrearCurso con previas y nulos");
        Instituto instituto = new Instituto("Instituto_Test_Crear");
        String nombre = "Curso_Test_" + System.currentTimeMillis();
        
        // Probar creación con lista de previas nula y con elementos inexistentes
        List<String> previasInexistentes = List.of("Previa_Falsa_123");
        Curso result = manejador.CrearCurso(instituto, nombre, "Desc", 5, 40.0F, 10, "http://test.com", new Date(), previasInexistentes, null);
        
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());
        
        // Probar con previas null explícitamente
        Curso resultNullPrevias = manejador.CrearCurso(instituto, nombre + "_2", "Desc", 5, 40.0F, 10, "http://test.com", new Date(), null, null);
        assertNotNull(resultNullPrevias);
    }

    @Test
    public void testModificarCurso() throws Exception {
        System.out.println("ModificarCurso");
        String nombreInst = "Instituto_Test_Mod_" + System.currentTimeMillis();
        Instituto instituto = manejadorInstituto.CreaInstituto(nombreInst);
        manejadorInstituto.Add(instituto);

        String nombre = "Curso_Modificar_" + System.currentTimeMillis();
        Curso c = manejador.CrearCurso(instituto, nombre, "Desc Inicial", 3, 20.0f, 5, "http://old.com", new Date(), null, null);
        manejador.Add(c);

        String nuevaDesc = "Desc Modificada";
        int nuevaDuracion = 8;
        float nuevasHoras = 60.0F;
        int nuevosCred = 12;
        String nuevaUrl = "http://new.com";
        Date nuevaFecha = new Date();
        List<String> previasValidas = List.of(nombre); // Puede usarse a sí mismo o lista vacía

        assertDoesNotThrow(() -> {
            manejador.ModificarCurso(c, nuevaDesc, nuevaDuracion, nuevasHoras, nuevosCred, nuevaUrl, nuevaFecha, previasValidas, null);
        });

        Curso cursoModificado = manejador.BuscarCurso(nombre);
        assertNotNull(cursoModificado);
        assertEquals(nuevaDesc, cursoModificado.getDescripcion());
        assertEquals(nuevaDuracion, cursoModificado.getDuracion());
    }

    @Test
    public void testAddYExcepcion() throws Exception {
        System.out.println("Add y control de excepciones");
        String nombreInst = "Instituto_Test_Add_" + System.currentTimeMillis();
        Instituto instituto = manejadorInstituto.CreaInstituto(nombreInst);
        manejadorInstituto.Add(instituto);

        String nombre = "Curso_Add_" + System.currentTimeMillis();
        Curso c = manejador.CrearCurso(instituto, nombre, "Desc", 2, 10.0f, 3, "http://add.com", new Date(), null, null);
        
        assertDoesNotThrow(() -> manejador.Add(c));
        
        Curso encontrado = manejador.BuscarCurso(nombre);
        assertNotNull(encontrado);
        assertEquals(nombre, encontrado.getNombre());

        // Forzar escenario de error en Add (ej: persistir un curso duplicado o con datos inválidos si aplica)
        // O evaluar comportamiento al pasar un curso repetido que dispare la excepción controlada de la BD
        assertThrows(Exception.class, () -> {
            manejador.Add(c); // Duplicado en BD
        });
    }

    @Test
    public void testBuscarCurso() throws Exception {
        System.out.println("BuscarCurso");
        String nombreInst = "Instituto_Test_Buscar_" + System.currentTimeMillis();
        Instituto instituto = manejadorInstituto.CreaInstituto(nombreInst);
        manejadorInstituto.Add(instituto);

        String nombre = "Curso_Buscar_" + System.currentTimeMillis();
        Curso c = manejador.CrearCurso(instituto, nombre, "Desc", 4, 30.0f, 8, "http://buscar.com", new Date(), null, null);
        manejador.Add(c);

        Curso result = manejador.BuscarCurso(nombre);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertNull(manejador.BuscarCurso("Curso_Inexistente_999"));
        assertNull(manejador.BuscarCurso(null));
    }

    @Test
    public void testGetDT() {
        System.out.println("getDT");
        Instituto instituto = new Instituto("Instituto_Test_DT");
        String nombre = "Curso_DT_" + System.currentTimeMillis();
        
        Curso c = manejador.CrearCurso(instituto, nombre, "Desc", 3, 15.0f, 4, "http://dt.com", new Date(), null, null);
        
        DTCurso result = manejador.getDT(c);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertNull(manejador.getDT(null));
    }

    @Test
    public void testGetDTList() throws Exception {
        System.out.println("getDTList");
        String nombreInst = "Instituto_Test_List_" + System.currentTimeMillis();
        Instituto instituto = manejadorInstituto.CreaInstituto(nombreInst);
        manejadorInstituto.Add(instituto);

        String nombre = "Curso_List_" + System.currentTimeMillis();
        Curso c = manejador.CrearCurso(instituto, nombre, "Desc", 1, 5.0f, 2, "http://list.com", new Date(), null, null);
        manejador.Add(c);

        List<DTMaster> result = manejador.getDTList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetDTLIstPorInstituto() throws Exception {
        System.out.println("getDTLIst con instituto");
        String nombreInst = "Instituto_Filtro_" + System.currentTimeMillis();
        Instituto instituto = manejadorInstituto.CreaInstituto(nombreInst);
        manejadorInstituto.Add(instituto);

        String nombreCurso = "Curso_Filtro_" + System.currentTimeMillis();
        Curso c = manejador.CrearCurso(instituto, nombreCurso, "Desc", 2, 10.0f, 3, "http://filtro.com", new Date(), null, null);
        manejador.Add(c);

        List<DTMaster> result = manejador.getDTLIst(nombreInst);
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Probar con instituto inexistente o nulo
        assertTrue(manejador.getDTLIst("Instituto_Falso_999").isEmpty());
        assertTrue(manejador.getDTLIst(null).isEmpty());
    }
}