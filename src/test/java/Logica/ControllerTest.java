package Logica;

import DTsClasses.DTMaster;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Controller.
 * @author manuelpalumbo
 */
public class ControllerTest {
    
    private Controller controller;

    public ControllerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        controller = new Controller();
    }
    
    @AfterEach
    public void tearDown() {
        controller = null;
    }

    @Test
    public void testAltaInstituto() throws Exception {
        System.out.println("AltaInstituto");
        String nomInstituto = "INCO_" + System.currentTimeMillis();
        controller.AltaInstituto(nomInstituto);
        assertTrue(controller.VerificarInstituto(nomInstituto));
    }

    // =========================================================================
    // PRUEBAS DE AGREGAR USUARIO
    // =========================================================================

    @Test
    public void testAgregarUsuarioEstudianteExitoso() throws Exception {
        System.out.println("AgregarUsuario - Estudiante Exitoso");
        String id = String.valueOf(System.currentTimeMillis());
        String nickname = "est_" + id;
        String correo = "est_" + id + "@test.com";
        
        List<String> institutos = new ArrayList<>();
        assertDoesNotThrow(() -> controller.AgregarUsuario(nickname, "Juan", "Perez", correo, new Date(), false, institutos, ""));
        
        DTUsuarioBase user = controller.ConsultarUsuario(nickname);
        assertNotNull(user);
    }

    @Test
    public void testAgregarUsuarioDocenteConInstituto() throws Exception {
        System.out.println("AgregarUsuario - Docente con Instituto");
        String id = String.valueOf(System.currentTimeMillis());
        
        String inst = "Inst_Doc_" + id;
        controller.AltaInstituto(inst);
        
        String nickname = "doc_" + id;
        String correo = "doc_" + id + "@test.com";
        List<String> institutos = new ArrayList<>();
        institutos.add(inst);
        
        assertDoesNotThrow(() -> controller.AgregarUsuario(nickname, "Carlos", "Ruiz", correo, new Date(), true, institutos, ""));
        
        DTUsuarioBase user = controller.ConsultarUsuario(nickname);
        assertNotNull(user);
    }

    @Test
    public void testAgregarUsuarioInstitutosNull() throws Exception {
        System.out.println("AgregarUsuario - Institutos Null");
        String id = String.valueOf(System.currentTimeMillis());
        String nickname = "null_inst_" + id;
        String correo = "null_inst_" + id + "@test.com";
        
        assertDoesNotThrow(() -> controller.AgregarUsuario(nickname, "Ana", "Gomez", correo, new Date(), false, null, ""));
        
        DTUsuarioBase user = controller.ConsultarUsuario(nickname);
        assertNotNull(user);
    }

    @Test
    public void testAgregarUsuarioConIOException() {
        System.out.println("AgregarUsuario - Captura IOException");
        String id = String.valueOf(System.currentTimeMillis());
        String nickname = "io_ex_" + id;
        String correo = "io_ex_" + id + "@test.com";
        
        assertDoesNotThrow(() -> controller.AgregarUsuario(nickname, "Pedro", "Picasso", correo, new Date(), false, new ArrayList<>(), "path_invalido_inexistente_12345/foto.png"));
    }

    // =========================================================================
    // PRUEBAS DE MODIFICAR USUARIO
    // =========================================================================

    @Test
    public void testModificarUsuarioExitosoConInstitutos() throws Exception {
        System.out.println("ModificarUsuario - Exitoso con Institutos");
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String nickname = "mod_inst_" + uniqueId;
        String correo = "mod_inst_" + uniqueId + "@test.com";
        
        String nomInst = "Inst_Mod_" + uniqueId;
        controller.AltaInstituto(nomInst);
        
        List<String> esp = new ArrayList<>();
        controller.AgregarUsuario(nickname, "Carlos", "Lopez", correo, new Date(), true, esp, "");
        
        List<String> institutosMod = new ArrayList<>();
        institutosMod.add(nomInst);
        
        assertDoesNotThrow(() -> {
            controller.ModificarUsuario(nickname, "Carlos Alberto", "Lopez Silva", correo, true, new Date(), institutosMod, "");
        });
        
        DTUsuarioBase userMod = controller.ConsultarUsuario(nickname);
        assertNotNull(userMod);
    }

    @Test
    public void testModificarUsuarioInstitutosNull() throws Exception {
        System.out.println("ModificarUsuario - Institutos Null");
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String nickname = "mod_null_" + uniqueId;
        String correo = "mod_null_" + uniqueId + "@test.com";
        
        List<String> esp = new ArrayList<>();
        controller.AgregarUsuario(nickname, "Ana", "Gomez", correo, new Date(), false, esp, "");
        
        assertDoesNotThrow(() -> {
            controller.ModificarUsuario(nickname, "Ana Maria", "Gomez", correo, false, new Date(), null, "");
        });
        
        DTUsuarioBase userMod = controller.ConsultarUsuario(nickname);
        assertNotNull(userMod);
    }

    @Test
    public void testModificarUsuarioConExcepcionImagen() throws Exception {
        System.out.println("ModificarUsuario - Captura IOException en Imagen");
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String nickname = "mod_img_" + uniqueId;
        String correo = "mod_img_" + uniqueId + "@test.com";
        
        List<String> esp = new ArrayList<>();
        controller.AgregarUsuario(nickname, "Pedro", "Picasso", correo, new Date(), false, esp, "");
        
        assertDoesNotThrow(() -> {
            controller.ModificarUsuario(nickname, "Pedro", "Picasso", correo, false, new Date(), new ArrayList<>(), "path_invalido_inexistente/foto.png");
        });
    }

    // =========================================================================
    // PRUEBAS DE ALTA CURSO
    // =========================================================================

    @Test
    public void testAltaCursoExitosoConDocente() throws Exception {
        System.out.println("AltaCurso - Exitoso con Docente");
        String id = String.valueOf(System.currentTimeMillis());
        
        String inst = "Inst_AC_" + id;
        controller.AltaInstituto(inst);
        
        String nickDoc = "doc_ac_" + id;
        controller.AgregarUsuario(nickDoc, "Profesor", "Titular", "prof_" + id + "@test.com", new Date(), true, new ArrayList<>(), inst);
        
        String nomCurso = "Curso_Nuevo_" + id;
        List<String> previas = new ArrayList<>();
        
        assertDoesNotThrow(() -> {
            controller.AltaCurso(inst, nomCurso, "Descripcion del curso", 3, 20.0F, 10, "http://curso.com", previas, new Date(), nickDoc);
        });
        
        assertTrue(controller.VerificarCurso(nomCurso));
    }

    @Test
    public void testAltaCursoModificarExistente() throws Exception {
        System.out.println("AltaCurso - Modificar Curso Existente");
        String id = String.valueOf(System.currentTimeMillis());
        
        String inst = "Inst_ModC_" + id;
        controller.AltaInstituto(inst);
        
        String nickDoc = "doc_modc_" + id;
        controller.AgregarUsuario(nickDoc, "Doc", "Mod", "docmodc_" + id + "@test.com", new Date(), true, new ArrayList<>(), inst);
        
        String nomCurso = "Curso_Repetido_" + id;
        List<String> previas = new ArrayList<>();
        
        // Primera vez (Crea el curso)
        controller.AltaCurso(inst, nomCurso, "Desc vieja", 2, 10.0F, 5, "http://url1.com", previas, new Date(), nickDoc);
        
        // Segunda vez con el mismo nombre (Debe entrar al 'else' y modificarlo)
        assertDoesNotThrow(() -> {
            controller.AltaCurso(inst, nomCurso, "Desc nueva", 4, 30.0F, 12, "http://url2.com", previas, new Date(), nickDoc);
        });
        
        assertTrue(controller.VerificarCurso(nomCurso));
    }

    @Test
    public void testAltaCursoPreviasNull() throws Exception {
        System.out.println("AltaCurso - Previas Null");
        String id = String.valueOf(System.currentTimeMillis());
        
        String inst = "Inst_PrevNull_" + id;
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_PrevNull_" + id;
        
        assertDoesNotThrow(() -> {
            controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://url.com", null, new Date(), "");
        });
        
        assertTrue(controller.VerificarCurso(nomCurso));
    }

    // =========================================================================
    // RESTO DE PRUEBAS
    // =========================================================================

    @Test
    public void testConsultarUsuario() throws Exception {
        System.out.println("ConsultarUsuario");
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String nickname = "consult_" + uniqueId;
        String correo = "consult_" + uniqueId + "@test.com";
        
        List<String> esp = new ArrayList<>();
        controller.AgregarUsuario(nickname, "Maria", "Gomez", correo, new Date(), false, esp, "");
        
        DTUsuarioBase result = controller.ConsultarUsuario(nickname);
        assertNotNull(result);
    }

    @Test
    public void testConsultaCurso() throws Exception {
        System.out.println("ConsultaCurso");
        String inst = "Inst_ConsCurso_" + System.currentTimeMillis();
        controller.AltaInstituto(inst);
        
        String nomCurso = "Curso_Cons_" + System.currentTimeMillis();
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Descripcion", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        DTMaster result = controller.ConsultaCurso(nomCurso);
        assertNotNull(result);
    }

    @Test
    public void testAltaEdicionCursoNuevaConDocentes() throws Exception {
        System.out.println("AltaEdicionCursoNuevaConDocentes");
        String id = String.valueOf(System.currentTimeMillis());
        
        String inst = "Inst_Ed_" + id;
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_Ed_" + id;
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        String nickDocente = "docente_" + id;
        List<String> especialidades = new ArrayList<>();
        controller.AgregarUsuario(nickDocente, "Doc", "Test", "doc_" + id + "@test.com", new Date(), true, especialidades, inst);
        
        List<String> docentes = new ArrayList<>();
        docentes.add(nickDocente);
        docentes.add("Docente_Inexistente");
        
        String nomEdicion = "Edicion_Nueva_" + id;
        assertDoesNotThrow(() -> {
            controller.AltaEdicionCurso(inst, nomCurso, nomEdicion, new Date(), new Date(), 30, docentes, new Date());
        });
        
        assertTrue(controller.VerificarEdicion(nomEdicion));
    }

    @Test
    public void testAltaEdicionCursoModificarExistente() throws Exception {
        System.out.println("AltaEdicionCursoModificarExistente");
        String id = String.valueOf(System.currentTimeMillis());
        
        String inst = "Inst_Ed_Mod_" + id;
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_Ed_Mod_" + id;
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        String nickDocente = "doc_mod_" + id;
        List<String> especialidades = new ArrayList<>();
        controller.AgregarUsuario(nickDocente, "Doc2", "Test2", "docmod_" + id + "@test.com", new Date(), true, especialidades, inst);
        
        List<String> docentes = new ArrayList<>();
        docentes.add(nickDocente);
        
        String nomEdicion = "Edicion_Existente_" + id;
        
        controller.AltaEdicionCurso(inst, nomCurso, nomEdicion, new Date(), new Date(), 30, null, new Date());
        
        assertDoesNotThrow(() -> {
            controller.AltaEdicionCurso(inst, nomCurso, nomEdicion, new Date(), new Date(), 40, docentes, new Date());
        });
        
        assertTrue(controller.VerificarEdicion(nomEdicion));
    }

    @Test
    public void testAltaEdicionCursoDocentesNull() throws Exception {
        System.out.println("AltaEdicionCursoDocentesNull");
        String id = String.valueOf(System.currentTimeMillis());
        
        String inst = "Inst_Ed_Null_" + id;
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_Ed_Null_" + id;
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        String nomEdicion = "Edicion_NullDoc_" + id;
        
        assertDoesNotThrow(() -> {
            controller.AltaEdicionCurso(inst, nomCurso, nomEdicion, new Date(), new Date(), 15, null, new Date());
        });
        
        assertTrue(controller.VerificarEdicion(nomEdicion));
    }

    @Test
    public void testConsultaEdicionCurso() throws Exception {
        System.out.println("ConsultaEdicionCurso");
        String inst = "Inst_CEd_" + System.currentTimeMillis();
        controller.AltaInstituto(inst);
        
        String nomCurso = "Curso_CEd_" + System.currentTimeMillis();
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        String nomEdicion = "Edicion_Cons_" + System.currentTimeMillis();
        controller.AltaEdicionCurso(inst, nomCurso, nomEdicion, new Date(), new Date(), 30, null, new Date());
        
        DTMaster result = controller.ConsultaEdicionCurso(nomEdicion);
        assertNotNull(result);
    }

    @Test
    public void testInscripcionAEdicionCurso() throws Exception {
        System.out.println("InscripcionAEdicionCurso");
        String id = String.valueOf(System.currentTimeMillis());
        String nick = "estudiante_" + id;
        List<String> esp = new ArrayList<>();
        controller.AgregarUsuario(nick, "Ana", "Ruiz", "ana_" + id + "@test.com", new Date(), false, esp, "");
        
        String inst = "Inst_Insc_" + id;
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_Insc_" + id;
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        assertDoesNotThrow(() -> controller.InscripcionAEdicionCurso(nomCurso, nick, new Date()));
    }

    @Test
    public void testCrearProgramasDeFormacion() throws Exception {
        System.out.println("CrearProgramasDeFormacion");
        String nomPrograma = "Prog_" + System.currentTimeMillis();
        controller.CrearProgramasDeFormacion(nomPrograma, "Descripcion", new Date(), new Date(), new Date());
        
        assertTrue(controller.VerificarPrograma(nomPrograma));
    }

    @Test
    public void testAgregarCursoAProgramasExitoso() throws Exception {
        System.out.println("AgregarCursoAProgramas - Exitoso");
        String id = String.valueOf(System.currentTimeMillis());
        
        String inst = "Inst_Prog_" + id;
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_Prog_" + id;
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc Curso", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        String nomProg = "Prog_AddC_" + id;
        controller.CrearProgramasDeFormacion(nomProg, "Desc Prog", new Date(), new Date(), new Date());
        
        List<String> cursos = new ArrayList<>();
        cursos.add(nomCurso);
        
        assertDoesNotThrow(() -> controller.AgregarCursoAProgramas(nomProg, cursos));
    }

    @Test
    public void testAgregarCursoAProgramasProgramaInexistente() {
        System.out.println("AgregarCursoAProgramas - Programa Inexistente");
        List<String> cursos = new ArrayList<>();
        cursos.add("Curso_Cualquiera");
        
        assertDoesNotThrow(() -> controller.AgregarCursoAProgramas("Prog_Inexistente_999", cursos));
    }

    @Test
    public void testAgregarCursoAProgramasListaCursosNull() throws Exception {
        System.out.println("AgregarCursoAProgramas - Cursos Null");
        String nomProg = "Prog_NullCursos_" + System.currentTimeMillis();
        controller.CrearProgramasDeFormacion(nomProg, "Desc", new Date(), new Date(), new Date());
        
        assertDoesNotThrow(() -> controller.AgregarCursoAProgramas(nomProg, null));
    }

    @Test
    public void testAgregarCursoAProgramasCursoInexistente() throws Exception {
        System.out.println("AgregarCursoAProgramas - Curso Inexistente");
        String nomProg = "Prog_CursoInex_" + System.currentTimeMillis();
        controller.CrearProgramasDeFormacion(nomProg, "Desc", new Date(), new Date(), new Date());
        
        List<String> cursos = new ArrayList<>();
        cursos.add("Curso_Inexistente_12345");
        
        assertDoesNotThrow(() -> controller.AgregarCursoAProgramas(nomProg, cursos));
    }

    @Test
    public void testConsultaProgramaFormacion() throws Exception {
        System.out.println("ConsultaProgramaFormacion");
        String nomProg = "Prog_Cons_" + System.currentTimeMillis();
        controller.CrearProgramasDeFormacion(nomProg, "Desc", new Date(), new Date(), new Date());
        
        DTMaster result = controller.ConsultaProgramaFormacion(nomProg);
        assertNotNull(result);
    }

    @Test
    public void testExistePrograma() throws Exception {
        System.out.println("ExistePrograma");
        String nomProg = "Prog_Exist_" + System.currentTimeMillis();
        controller.CrearProgramasDeFormacion(nomProg, "Desc", new Date(), new Date(), new Date());
        
        boolean result = controller.VerificarPrograma(nomProg);
        assertTrue(result);
    }

    @Test
    public void testActualizarPrograma() throws Exception {
        System.out.println("ActualizarPrograma");
        String nomProg = "Prog_Act_" + System.currentTimeMillis();
        controller.CrearProgramasDeFormacion(nomProg, "Desc Vieja", new Date(), new Date(), new Date());
        
        assertDoesNotThrow(() -> controller.ActualizarPrograma(nomProg, "Desc Nueva", new Date(), new Date()));
    }

    @Test
    public void testInscripcionUsuarioAProgramas() throws Exception {
        System.out.println("InscripcionUsuarioAProgramas");
        String id = String.valueOf(System.currentTimeMillis());
        String nick = "user_prog_" + id;
        List<String> esp = new ArrayList<>();
        controller.AgregarUsuario(nick, "Pedro", "Gomez", "pedro_" + id + "@test.com", new Date(), false, esp, "");
        
        String nomProg = "Prog_Insc_" + id;
        controller.CrearProgramasDeFormacion(nomProg, "Desc", new Date(), new Date(), new Date());
        
        assertDoesNotThrow(() -> controller.InscripcionUsuarioAProgramas(nomProg, nick, new Date()));
    }

    @Test
    public void testVerificarCurso() throws Exception {
        System.out.println("VerificarCurso");
        String inst = "Inst_VerC_" + System.currentTimeMillis();
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_Ver_" + System.currentTimeMillis();
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        assertTrue(controller.VerificarCurso(nomCurso));
        assertFalse(controller.VerificarCurso("Curso_Inexistente_123"));
    }

    @Test
    public void testVerificarInstituto() throws Exception {
        System.out.println("VerificarInstituto");
        String nomInst = "Inst_Verific_" + System.currentTimeMillis();
        controller.AltaInstituto(nomInst);
        
        assertTrue(controller.VerificarInstituto(nomInst));
        assertFalse(controller.VerificarInstituto("Inst_Inexistente_123"));
    }

    @Test
    public void testVerificarEdicion() throws Exception {
        System.out.println("VerificarEdicion");
        String inst = "Inst_VEd_" + System.currentTimeMillis();
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_VEd_" + System.currentTimeMillis();
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        String nomEdicion = "Ed_VEd_" + System.currentTimeMillis();
        controller.AltaEdicionCurso(inst, nomCurso, nomEdicion, new Date(), new Date(), 30, null, new Date());
        
        assertTrue(controller.VerificarEdicion(nomEdicion));
        assertFalse(controller.VerificarEdicion("Edicion_Inexistente_123"));
    }

    @Test
    public void testVerificarPrograma() throws Exception {
        System.out.println("VerificarPrograma");
        String nomProg = "Prog_Verif_" + System.currentTimeMillis();
        controller.CrearProgramasDeFormacion(nomProg, "Desc", new Date(), new Date(), new Date());
        
        assertTrue(controller.VerificarPrograma(nomProg));
        assertFalse(controller.VerificarPrograma("Prog_Inexistente_123"));
    }

    // =========================================================================
    // PRUEBAS DE LISTAR CLASE
    // =========================================================================

    @Test
    public void testListarClase() {
        System.out.println("ListarClase");
        List<DTMaster> result = controller.ListarClase(EnumDT.DT_USUARIO);
        assertNotNull(result);
    }

    @Test
    public void testListarClaseUsuariosConDatos() throws Exception {
        System.out.println("ListarClase - Usuarios Con Datos");
        String id1 = String.valueOf(System.currentTimeMillis());
        String id2 = String.valueOf(System.currentTimeMillis() + 1);

        controller.AgregarUsuario("u1_" + id1, "Nombre1", "Apellido1", "u1_" + id1 + "@test.com", new Date(), false, null, "");
        controller.AgregarUsuario("u2_" + id2, "Nombre2", "Apellido2", "u2_" + id2 + "@test.com", new Date(), false, null, "");

        List<DTMaster> result = controller.ListarClase(EnumDT.DT_USUARIO);
        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    public void testListarClaseTipoInexistente() {
        System.out.println("ListarClase - Tipo no Soportado u Otro Enum");
        List<DTMaster> result = controller.ListarClase(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================================================================

    @Test
    public void testListarCursos() throws Exception {
        System.out.println("ListarCursos");
        String inst = "Inst_ListC_" + System.currentTimeMillis();
        controller.AltaInstituto(inst);
        
        List<DTMaster> result = controller.ListarCursos(inst);
        assertNotNull(result);
    }

    @Test
    public void testListarEdiciones() throws Exception {
        System.out.println("ListarEdiciones");
        String inst = "Inst_ListE_" + System.currentTimeMillis();
        controller.AltaInstituto(inst);
        String nomCurso = "Curso_ListE_" + System.currentTimeMillis();
        List<String> prev = new ArrayList<>();
        controller.AltaCurso(inst, nomCurso, "Desc", 2, 10.0F, 5, "http://test.com", prev, new Date(), "");
        
        List<DTMaster> result = controller.ListarEdiciones(nomCurso);
        assertNotNull(result);
    }

    @Test
    public void testListarDocentes() throws Exception {
        System.out.println("ListarDocentes");
        String inst = "Inst_ListD_" + System.currentTimeMillis();
        controller.AltaInstituto(inst);
        
        List<DTMaster> result = controller.ListarDocentes(inst);
        assertNotNull(result);
    }

    @Test
    public void testListarProgramaDeForm() {
        System.out.println("ListarProgramaDeForm");
        List<DTMaster> result = controller.ListarProgramaDeForm();
        assertNotNull(result);
    }
}