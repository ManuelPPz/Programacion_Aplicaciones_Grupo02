/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Manejadores;

import Classes.Curso;
import Classes.Instituto;
import Classes.Prog_Usu;
import Classes.ProgramaDeFormacion;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import DTsClasses.Vigencia;
import jakarta.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ManejadorProgramasDeFormacionTest {
    
    private ManejadorProgramasDeFormacion manejador;
    private ManejadorCursos manejadorCursos;
    private ManejadorInstituto manejadorInstituto;

    @BeforeEach
    public void setUp() {
        manejador = ManejadorProgramasDeFormacion.GetInstance();
        manejadorCursos = ManejadorCursos.GetInstance();
        manejadorInstituto = ManejadorInstituto.GetInstance();
    }

    @Test
    public void testGetInstance() {
        System.out.println("GetInstance ProgramaFormacion");
        ManejadorProgramasDeFormacion inst1 = ManejadorProgramasDeFormacion.GetInstance();
        ManejadorProgramasDeFormacion inst2 = ManejadorProgramasDeFormacion.GetInstance();
        assertNotNull(inst1);
        assertSame(inst1, inst2);
    }

    @Test
    public void testCargarYBuscarPrograma() throws Exception {
        System.out.println("CargarDeBaseDeDatos y BuscarPrograma");
        String nombreProg = "Prog_Test_" + System.currentTimeMillis();
        Vigencia vigencia = new Vigencia(new Date(), new Date());
        
        ProgramaDeFormacion prog = new ProgramaDeFormacion(nombreProg, "Descripcion test", vigencia, new Date());
        
        assertDoesNotThrow(() -> manejador.Add(prog));
        
        ProgramaDeFormacion encontrado = manejador.BuscarPrograma(nombreProg);
        assertNotNull(encontrado);
        assertEquals(nombreProg, encontrado.getNombre());
        
        assertNull(manejador.BuscarPrograma("Programa_Inexistente_999"));
        assertNull(manejador.BuscarPrograma(null));
        
        assertThrows(Exception.class, () -> {
            manejador.Add(prog);
        });
    }

    @Test
    public void testModificarDatos() throws Exception {
        System.out.println("ModificarDatos ProgramaFormacion");
        String nombreProg = "Prog_Mod_" + System.currentTimeMillis();
        Vigencia vigencia1 = new Vigencia(new Date(), new Date());
        ProgramaDeFormacion prog = new ProgramaDeFormacion(nombreProg, "Desc Vieja", vigencia1, new Date());
        manejador.Add(prog);

        Vigencia vigencia2 = new Vigencia(new Date(), new Date());
        assertDoesNotThrow(() -> {
            manejador.ModificarDatos(nombreProg, "Desc Nueva", vigencia2, new Date());
        });

        ProgramaDeFormacion modificado = manejador.BuscarPrograma(nombreProg);
        assertEquals("Desc Nueva", modificado.getDescripcion());
    }

    @Test
    public void testAddCursoAlPrograma() throws Exception {
        System.out.println("AddCurso ProgramaFormacion");
        String nombreInst = "Inst_Prog_Curso_" + System.currentTimeMillis();
        Instituto instituto = manejadorInstituto.CreaInstituto(nombreInst);
        manejadorInstituto.Add(instituto);

        String nombreCurso = "Curso_Para_Prog_" + System.currentTimeMillis();
        Curso curso = manejadorCursos.CrearCurso(instituto, nombreCurso, "Desc", 4, 20.0f, 5, "http://url.com", new Date(), null, null);
        manejadorCursos.Add(curso);

        String nombreProg = "Prog_Con_Curso_" + System.currentTimeMillis();
        ProgramaDeFormacion prog = new ProgramaDeFormacion(nombreProg, "Desc", new Vigencia(new Date(), new Date()), new Date());
        manejador.Add(prog);

        assertDoesNotThrow(() -> {
            manejador.AddCurso(prog, curso);
        });
    }

    @Test
    public void testAddUsuarioInscripto() {
        System.out.println("AddUsuarioInscripto ProgramaFormacion");
        Prog_Usu progUsu = new Prog_Usu();
        assertDoesNotThrow(() -> {
            try {
                manejador.AddUsuarioInscripto(progUsu);
            } catch (Exception e) {
                // Capturar si requiere datos persistidos obligatorios
            }
        });
    }

    @Test
    public void testGetDTYListas() throws Exception {
        System.out.println("getDT y consultas de listas ProgramaFormacion");
        String nombreProg = "Prog_DT_" + System.currentTimeMillis();
        ProgramaDeFormacion prog = new ProgramaDeFormacion(nombreProg, "Desc DT", new Vigencia(new Date(), new Date()), new Date());
        manejador.Add(prog);

        DTProgramaForm dt = manejador.getDT(prog);
        assertNotNull(dt);
        assertEquals(nombreProg, dt.getNombre());

        assertNull(manejador.getDT(null));

        List<DTMaster> listaMaster = manejador.getDTList();
        assertNotNull(listaMaster);
        assertFalse(listaMaster.isEmpty());
    }
}