/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Manejadores;

import Classes.Instituto;
import DTsClasses.DTInstituto;
import DTsClasses.DTMaster;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ManejadorInstituto.
 * @author manuelpalumbo
 */
public class ManejadorInstitutoTest {
    
    private ManejadorInstituto manejador;

    public ManejadorInstitutoTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        manejador = ManejadorInstituto.GetInstance();
    }
    
    @AfterEach
    public void tearDown() {
        manejador = null;
    }

    /**
     * Test of GetInstance method, of class ManejadorInstituto.
     */
    @Test
    public void testGetInstance() {
        System.out.println("GetInstance");
        ManejadorInstituto result1 = ManejadorInstituto.GetInstance();
        ManejadorInstituto result2 = ManejadorInstituto.GetInstance();
        
        assertNotNull(result1);
        assertSame(result1, result2, "El manejador debe ser una instancia única (Singleton)");
    }

    /**
     * Test of CreaInstituto method, of class ManejadorInstituto.
     */
    @Test
    public void testCreaInstituto() {
        System.out.println("CreaInstituto");
        String nombreInst = "Instituto_Test_" + System.currentTimeMillis();
        
        Instituto inst = manejador.CreaInstituto(nombreInst);
        
        assertNotNull(inst);
        assertEquals(nombreInst, inst.getNombre());
    }

    /**
     * Test of Add method, of class ManejadorInstituto.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("Add");
        String nombreInst = "Instituto_Add_" + System.currentTimeMillis();
        Instituto inst = new Instituto(nombreInst);
        
        assertDoesNotThrow(() -> manejador.Add(inst));
        
        Instituto encontrado = manejador.BuscarInstituto(nombreInst);
        assertNotNull(encontrado);
        assertEquals(nombreInst, encontrado.getNombre());
    }

    /**
     * Test of BuscarInstituto method, of class ManejadorInstituto.
     */
    @Test
    public void testBuscarInstituto() throws Exception {
        System.out.println("BuscarInstituto");
        String nombreInst = "Instituto_Buscar_" + System.currentTimeMillis();
        Instituto inst = manejador.CreaInstituto(nombreInst);
        manejador.Add(inst);
        
        Instituto resultado = manejador.BuscarInstituto(nombreInst);
        assertNotNull(resultado);
        assertEquals(nombreInst, resultado.getNombre());
        
        // Buscar uno que no exista
        Instituto noExistente = manejador.BuscarInstituto("Instituto_Inexistente_999");
        assertNull(noExistente);
    }

    /**
     * Test of getDT method, of class ManejadorInstituto.
     */
    @Test
    public void testGetDT() {
        System.out.println("getDT");
        String nombreInst = "Instituto_DT_" + System.currentTimeMillis();
        Instituto inst = new Instituto(nombreInst);
        
        DTInstituto dt = manejador.getDT(inst);
        
        assertNotNull(dt);
        assertEquals(nombreInst, dt.getNombre());
    }

    /**
     * Test of getDT method con valor null.
     */
    @Test
    public void testGetDTNull() {
        System.out.println("getDT - Null");
        DTInstituto dt = manejador.getDT(null);
        assertNull(dt);
    }

    /**
     * Test of getDTList method, of class ManejadorInstituto.
     */
    @Test
    public void testGetDTList() throws Exception {
        System.out.println("getDTList");
        String nombreInst = "Instituto_List_" + System.currentTimeMillis();
        Instituto inst = manejador.CreaInstituto(nombreInst);
        manejador.Add(inst);
        
        List<DTMaster> lista = manejador.getDTList();
        
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    /**
     * Test of obtenerTodosLosInstitutos method, of class ManejadorInstituto.
     */
    @Test
    public void testObtenerTodosLosInstitutos() throws Exception {
        System.out.println("obtenerTodosLosInstitutos");
        String nombreInst = "Instituto_Todos_" + System.currentTimeMillis();
        Instituto inst = manejador.CreaInstituto(nombreInst);
        manejador.Add(inst);
        
        List<Instituto> lista = manejador.obtenerTodosLosInstitutos();
        
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}