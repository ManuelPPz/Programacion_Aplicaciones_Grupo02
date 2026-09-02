package Manejadores;

import java.util.List;
import java.util.ArrayList;
import Classes.Curso;
import Classes.EdicionCurso; // <-- Asegúrate de importar EdicionCurso
import Classes.Instituto;
import Classes.UsuarioBase;
import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.Date;

public class ManejadorCursos {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");
    List<Curso> misCursos;
    
    //=================Codigo de Singleton=================
    private static ManejadorCursos instance;    
    public static ManejadorCursos GetInstance(){
        if(instance==null){
            instance = new ManejadorCursos();
        }
        return instance;
    }
    
    private ManejadorCursos() {
        misCursos = new ArrayList<>();
        CargarDeBaseDeDatos();
    }
    //=======================================================
    
    public void CargarDeBaseDeDatos() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // PASO 1: Cargar todos los cursos haciendo FETCH de la colección 'misEdiciones'
            List<Curso> resultados = em.createQuery(
                "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misEdiciones", Curso.class
            ).getResultList();

            // PASO 2: En la misma sesión, hacer FETCH de la colección 'previas' para los mismos cursos
            // Esto inicializa las previas en memoria sin lanzar la MultipleBagFetchException
            if (!resultados.isEmpty()) {
                resultados = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.previas WHERE c IN :cursos", Curso.class
                ).setParameter("cursos", resultados)
                 .getResultList();
            }

            em.getTransaction().commit();

            // Guardar la lista de cursos obtenida
            this.misCursos = resultados;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al cargar cursos desde la BD: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    public Curso CrearCurso(Instituto instituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, Date fAlta, List<String> previas, UsuarioBase ub){
        Curso returnCurso;
        List<Curso> auxPrevias = new ArrayList<>();
        for(int i= 0; i<previas.size(); i++){
            auxPrevias.add(BuscarCurso(previas.get(i)));
        }
        returnCurso = new Curso(instituto, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, fAlta, auxPrevias, ub);
        return returnCurso;
    }

    public void ModificarCurso(Curso c, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, Date fAlta, List<String> previas, UsuarioBase ub){
        List<Curso> auxPrevias = new ArrayList<>();
        for(int i= 0; i<previas.size(); i++){
            auxPrevias.add(BuscarCurso(previas.get(i)));
        }
        c.ModificarMisDatos(descripcion, duracion, cantHoras, cantCreditos, URL, fAlta, auxPrevias, ub);
    }
    
    public void Add(Curso c) throws Exception{
        misCursos.add(c);
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar el curso: " + e.getMessage());
        } finally{
            em.close();
        }
    }
    
    public Curso BuscarCurso(String nombre){
        for(int i = 0; i<misCursos.size(); i++){
            Curso c = misCursos.get(i);
            if(c.getNombre().equals(nombre)){
                return c;
            }
        }
        return null;
    }

    public DTCurso getDT(Curso c){
        String ins = (c.getInstituto() != null) ? c.getInstituto().getNombre() : "";
        
        // 1. Convertir Previas
        List<Curso> auxPrevias = c.getPrevias();
        List<String> auxPreviasStr = new ArrayList<>();
        if (auxPrevias != null) {
            for(Curso previa : auxPrevias){
                auxPreviasStr.add(previa.getNombre());
            }
        }

        // CORRECCIÓN 2: Mapear la lista de Ediciones de EdicionCurso -> String
        List<EdicionCurso> auxEdiciones = c.getEdiciones();
        List<String> auxEdicionesStr = new ArrayList<>();
        if (auxEdiciones != null) {
            for(EdicionCurso edicion : auxEdiciones){
                if (edicion != null) {
                    auxEdicionesStr.add(edicion.getNombre()); // O el método que devuelva el nombre/identificador
                }
            }
        }

        // List de programas (vacía por ahora si no la usas)
        List<String> auxProgramasStr = new ArrayList<>();

        // CORRECCIÓN 3: Reemplazar los 'null' por la lista procesada 'auxEdicionesStr'
        return new DTCurso(
            ins,
            c.getNombre(),
            c.getDescripcion(),
            c.getDuracion(),
            c.getCantHoras(),
            c.getCantCreditos(),
            c.getURL(),
            c.getFAlta(),
            auxPreviasStr,
            auxEdicionesStr,  // <-- Pasa las ediciones mapeadas
            auxProgramasStr   // <-- Pasa la lista de programas
        );
    }

    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList<>();
        for(int i = 0; i<misCursos.size(); i++){
            DTMaster dt = getDT(misCursos.get(i));
            auxList.add(dt);
        }
        return auxList;
    }

    public List<DTMaster> getDTLIst(String instituto){
        List<DTMaster> auxList = new ArrayList<>();
        for(int i = 0; i<misCursos.size(); i++){
            Curso c = misCursos.get(i);
            if(c.getInstituto() != null && c.getInstituto().getNombre().equals(instituto)){
                DTMaster dt = getDT(c);
                auxList.add(dt);
            }
        }
        return auxList;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}