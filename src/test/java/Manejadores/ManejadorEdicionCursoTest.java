/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to edit this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Manejadores;

import Classes.Curso;
import Classes.Docente;
import Classes.Edi_Usu;
import Classes.EdicionCurso;
import Classes.Instituto;
import Classes.UsuarioBase;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.JPAUtil;

/**
 * Pruebas unitarias optimizadas para ManejadorEdicionCurso.
 * @author manuelpalumbo
 */
public class ManejadorEdicionCursoTest {
    
    private ManejadorEdicionCurso instance;

    public ManejadorEdicionCursoTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        instance = ManejadorEdicionCurso.GetInstance();
    }
    
    @AfterEach
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testGetInstance() {
        System.out.println("GetInstance");
        ManejadorEdicionCurso result1 = ManejadorEdicionCurso.GetInstance();
        ManejadorEdicionCurso result2 = ManejadorEdicionCurso.GetInstance();
        
        assertNotNull(result1);
        assertSame(result1, result2);
    }

   @Test
    public void testCargarDeBaseDeDatos() throws Exception {
        System.out.println("CargarDeBaseDeDatos");
        
        ManejadorEdicionCurso instance = ManejadorEdicionCurso.GetInstance();
        assertNotNull(instance, "La instancia del manejador no debe ser nula");
        
        // 1. Forzar la llamada al método privado (necesario porque es un Singleton)
        java.lang.reflect.Method metodoCargar = ManejadorEdicionCurso.class.getDeclaredMethod("CargarDeBaseDeDatos");
        metodoCargar.setAccessible(true);
        
        // 2. Asegurar que el método se ejecute correctamente y no filtre excepciones
        assertDoesNotThrow(() -> metodoCargar.invoke(instance), 
            "El método CargarDeBaseDeDatos lanzó una excepción no controlada.");
        
        // 3. Extraer la lista privada 'misEdiciones' para verificar su estado
        java.lang.reflect.Field listaField = ManejadorEdicionCurso.class.getDeclaredField("misEdiciones");
        listaField.setAccessible(true);
        List<?> misEdiciones = (List<?>) listaField.get(instance);
        
        // 4. Validar que la lista se haya inicializado (ya sea con datos de la BD o vacía por el catch)
        assertNotNull(misEdiciones, "La lista 'misEdiciones' quedó en null tras cargar la base de datos.");
    }

    @Test
    public void testCrearEdicion() {
        System.out.println("CrearEdicion");
        Instituto instituto = new Instituto("Instituto_Test");
        Curso curso = ManejadorCursos.GetInstance().CrearCurso(instituto, "Curso_Test", "Desc", 3, 10.0f, 2, "http://url.com", new Date(), null, null);
        String nombre = "Edicion_Crear_" + System.currentTimeMillis();
        Date fInicio = new Date();
        Date fFin = new Date();
        int cupo = 25;
        Date fAlta = new Date();
        List<Docente> docentes = new ArrayList<>();

        EdicionCurso result = instance.CrearEdicion(instituto, curso, nombre, fInicio, fFin, cupo, fAlta, docentes);
        
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());
        assertEquals(cupo, result.getCupo());
    }

    @Test
    public void testModificarDatos() throws Exception {
        System.out.println("ModificarDatos");
        
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Instituto instituto = new Instituto("Instituto_Mod_" + System.currentTimeMillis());
        em.persist(instituto);
        Curso curso = ManejadorCursos.GetInstance().CrearCurso(instituto, "Curso_Mod_" + System.currentTimeMillis(), "Desc", 3, 10.0f, 2, "http://url.com", new Date(), null, null);
        em.persist(curso);
        em.getTransaction().commit();
        em.close();

        String nombreEdicion = "Edicion_Mod_" + System.currentTimeMillis();
        EdicionCurso ec = instance.CrearEdicion(instituto, curso, nombreEdicion, new Date(), new Date(), 20, new Date(), new ArrayList<>());
        instance.Add(ec);

        Date fInicio = new Date();
        Date fFin = new Date();
        int cupo = 50;
        Date fAlta = new Date();
        List<Docente> misUsuarios = new ArrayList<>();

        assertDoesNotThrow(() -> {
            instance.ModificarDatos(ec, fInicio, fFin, cupo, fAlta, misUsuarios);
        });

        EdicionCurso editada = instance.BuscarEdicion(nombreEdicion);
        assertNotNull(editada);
        assertEquals(50, editada.getCupo());

        assertDoesNotThrow(() -> {
            instance.ModificarDatos(null, fInicio, fFin, cupo, fAlta, misUsuarios);
        });
    }

    @Test
    public void testAdd() throws Exception {
        System.out.println("Add");
        
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Instituto instituto = new Instituto("Instituto_Add_" + System.currentTimeMillis());
        em.persist(instituto);
        Curso curso = ManejadorCursos.GetInstance().CrearCurso(instituto, "Curso_Add_" + System.currentTimeMillis(), "Desc", 3, 10.0f, 2, "http://url.com", new Date(), null, null);
        em.persist(curso);
        
        String uniqueEmail = "doc_add_" + System.currentTimeMillis() + "@t.com";
        Docente docente = new Docente("doc_add_" + System.currentTimeMillis(), "Nom", "Ap", uniqueEmail, new Date(), new byte[0], new ArrayList<>());
        em.persist(docente);
        em.getTransaction().commit();
        em.close();

        List<Docente> listaDocentes = new ArrayList<>();
        listaDocentes.add(docente);

        String nombreEdicion = "Edicion_Add_" + System.currentTimeMillis();
        EdicionCurso ec = instance.CrearEdicion(instituto, curso, nombreEdicion, new Date(), new Date(), 30, new Date(), listaDocentes);

        assertDoesNotThrow(() -> instance.Add(ec));
        
        EdicionCurso encontrada = instance.BuscarEdicion(nombreEdicion);
        assertNotNull(encontrada);
        assertEquals(nombreEdicion, encontrada.getNombre());
    }

    @Test
    public void testBuscarEdicion() throws Exception {
        System.out.println("BuscarEdicion");
        
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Instituto instituto = new Instituto("Instituto_Bus_" + System.currentTimeMillis());
        em.persist(instituto);
        Curso curso = ManejadorCursos.GetInstance().CrearCurso(instituto, "Curso_Bus_" + System.currentTimeMillis(), "Desc", 3, 10.0f, 2, "http://url.com", new Date(), null, null);
        em.persist(curso);
        em.getTransaction().commit();
        em.close();

        EdicionCurso ec = instance.CrearEdicion(instituto, curso, "Edicion_Bus_" + System.currentTimeMillis(), new Date(), new Date(), 30, new Date(), new ArrayList<>());
        instance.Add(ec);

        assertNotNull(instance.BuscarEdicion(ec.getNombre()));
        assertNull(instance.BuscarEdicion("Edicion_Inexistente_XYZ"));
    }

    @Test
    public void testAddUsuario() {
        System.out.println("AddUsuario");
        Instituto instituto = new Instituto("Instituto_Usr");
        Curso curso = ManejadorCursos.GetInstance().CrearCurso(instituto, "Curso_Usr", "Desc", 3, 10.0f, 2, "http://url.com", new Date(), null, null);
        EdicionCurso ec = instance.CrearEdicion(instituto, curso, "Edicion_Usr", new Date(), new Date(), 30, new Date(), new ArrayList<>());
        
        Docente docente = new Docente();

        assertDoesNotThrow(() -> instance.AddUsuario(ec, docente));
        assertTrue(ec.getMisDocentes().contains(docente));
    }

@Test
    public void testAddUsuarioInscripto() throws Exception {
        System.out.println("AddUsuarioInscripto");
        
        // 1. Validar nulo general
        assertThrows(Exception.class, () -> instance.AddUsuarioInscripto(null));
        
        // 2. Validar ID nulo
        Edi_Usu euVacio = new Edi_Usu();
        assertThrows(Exception.class, () -> instance.AddUsuarioInscripto(euVacio));

        // Preparar entidades iniciales (Instituto, Curso, Edición)
        String sufijo = String.valueOf(System.currentTimeMillis());
        Instituto instituto = new Instituto("Inst_" + sufijo);
        Curso curso = ManejadorCursos.GetInstance().CrearCurso(instituto, "Curso_" + sufijo, "Desc", 3, 10.0f, 2, "http", new Date(), null, null);
        EdicionCurso edicion = instance.CrearEdicion(instituto, curso, "Edicion_" + sufijo, new Date(), new Date(), 30, new Date(), new ArrayList<>());
        
        // ===== CONSTRUCCIÓN DINÁMICA DE LA CLAVE COMPUESTA =====
        Field idField = Edi_Usu.class.getDeclaredField("id");
        idField.setAccessible(true);
        Class<?> idType = idField.getType();
        Object idInst = idType.getDeclaredConstructor().newInstance();
        
        Field userFieldInId = null;
        Class<?> userFieldType = null;

        for (Field f : idType.getDeclaredFields()) {
            f.setAccessible(true);
            if (EdicionCurso.class.isAssignableFrom(f.getType())) {
                f.set(idInst, edicion);
            } else if (f.getType().getSimpleName().toLowerCase().contains("usuario")) {
                userFieldInId = f;
                userFieldType = f.getType();
            }
        }

        // ===== AUTO-LLENADO DEL USUARIO PARA PASAR LOS 'NOT NULL' DE LA BD =====
        Object usuario = userFieldType.getDeclaredConstructor().newInstance();
        String nicknameStr = "usr_" + sufijo;
        
        Class<?> claseActual = usuario.getClass();
        while (claseActual != null && claseActual != Object.class) {
            for (Field f : claseActual.getDeclaredFields()) {
                f.setAccessible(true);
                try {
                    if (f.getName().toLowerCase().contains("nick") || f.isAnnotationPresent(jakarta.persistence.Id.class)) {
                        f.set(usuario, nicknameStr);
                    } else if (f.getName().toLowerCase().contains("mail") || f.getName().toLowerCase().contains("correo")) {
                        f.set(usuario, "mail_" + sufijo + "@test.com");
                    } else if (f.getType() == String.class) {
                        f.set(usuario, "dummy_str"); // Evitar nulls en nombre, apellido, password, etc.
                    } else if (f.getType() == Date.class) {
                        f.set(usuario, new Date());  // Evitar nulls en fechas
                    } else if (f.getType() == byte[].class) {
                        f.set(usuario, new byte[0]); // Evitar nulls en imágenes/bytes
                    } else if (f.getType() == int.class || f.getType() == Integer.class) {
                        f.set(usuario, 1);
                    } else if (f.getType() == List.class) {
                        f.set(usuario, new ArrayList<>()); // Evitar nulls en colecciones
                    }
                } catch (Exception ignored) {}
            }
            claseActual = claseActual.getSuperclass();
        }

        // Asignar el usuario ya configurado al ID
        if (userFieldInId != null) {
            userFieldInId.set(idInst, usuario);
        }

        // 3. Validar entidades NO encontradas en la BD 
        // (¡Nota! Esto imprimirá el e.printStackTrace() en consola, pero el test lo atrapará exitosamente)
        Edi_Usu euNoDb = new Edi_Usu();
        idField.set(euNoDb, idInst);
        assertThrows(Exception.class, () -> instance.AddUsuarioInscripto(euNoDb));

        // 4. Persistir entidades en la BD para probar el flujo exitoso
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(instituto);
        em.persist(curso);
        em.persist(edicion);
        em.persist(usuario); // ¡Ahora pasará sin problemas porque llenamos todos los campos requeridos!
        em.getTransaction().commit();
        em.close();

        // 5. Probar el flujo exitoso completo
        Edi_Usu euExito = new Edi_Usu();
        idField.set(euExito, idInst);
        
        // Si Edi_Usu requiere una fecha obligatoria como FechaInscripcion, la llenamos también
        for (Field f : Edi_Usu.class.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.getType() == Date.class) {
                try { f.set(euExito, new Date()); } catch (Exception ignored) {}
            }
        }
        
        // Tiene que pasar sin lanzar ninguna excepción
        assertDoesNotThrow(() -> instance.AddUsuarioInscripto(euExito));
    }

    @Test
    public void testGetDTLIst() throws Exception {
        System.out.println("getDTLIst");
        String nombreCurso = "Curso_List_" + System.currentTimeMillis();
        
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Instituto instituto = new Instituto("Instituto_List_" + System.currentTimeMillis());
        em.persist(instituto);
        Curso curso = ManejadorCursos.GetInstance().CrearCurso(instituto, nombreCurso, "Desc", 3, 10.0f, 2, "http://url.com", new Date(), null, null);
        em.persist(curso);
        em.getTransaction().commit();
        em.close();

        EdicionCurso ec = instance.CrearEdicion(instituto, curso, "Edicion_List_" + System.currentTimeMillis(), new Date(), new Date(), 30, new Date(), new ArrayList<>());
        instance.Add(ec);

        List<DTMaster> result = instance.getDTLIst(nombreCurso);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(instance.getDTLIst("Curso_Inexistente_List").isEmpty());
    }
}