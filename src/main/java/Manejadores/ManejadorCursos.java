package Manejadores;

import java.util.List;
import java.util.ArrayList;
import Classes.Curso;
import Classes.EdicionCurso; 
import Classes.Instituto;
import Classes.ProgramaDeFormacion;
import Classes.UsuarioBase;
import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import util.JPAUtil;

public class ManejadorCursos {
    private List<Curso> misCursos;
    
    //=================Codigo de Singleton=================
    private static ManejadorCursos instance;    
    public static ManejadorCursos GetInstance(){
        if(instance == null){
            instance = new ManejadorCursos();
        }
        return instance;
    }
    
    private ManejadorCursos() {
        misCursos = new ArrayList<>();
        CargarDeBaseDeDatos();
    }
    //=======================================================
    
    public static EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }

    public static void close() {
        JPAUtil.close();
    }
    
    public void CargarDeBaseDeDatos() {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // PASO 1: Cargar todos los cursos haciendo FETCH de la colección 'misEdiciones'
            List<Curso> resultados = em.createQuery(
                "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misEdiciones", Curso.class
            ).getResultList();

            // PASO 2: En la misma sesión, hacer FETCH de la colección 'previas' para los mismos cursos
            if (!resultados.isEmpty()) {
                resultados = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.previas WHERE c IN :cursos", Curso.class
                ).setParameter("cursos", resultados)
                 .getResultList();
            }
            if (!resultados.isEmpty()) {
                resultados = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misProgramas WHERE c IN :cursos", Curso.class
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
        if (previas != null) {
            for(int i = 0; i < previas.size(); i++){
                Curso cPrevia = BuscarCurso(previas.get(i));
                if (cPrevia != null) {
                    auxPrevias.add(cPrevia);
                }
            }
        }
        returnCurso = new Curso(instituto, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, fAlta, auxPrevias, ub);
        return returnCurso;
    }

    public void ModificarCurso(Curso c, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, Date fAlta, List<String> previas, UsuarioBase ub){
        List<Curso> auxPrevias = new ArrayList<>();
        if (previas != null) {
            for(int i = 0; i < previas.size(); i++){
                Curso cPrevia = BuscarCurso(previas.get(i));
                if (cPrevia != null) {
                    auxPrevias.add(cPrevia);
                }
            }
        }
        c.ModificarMisDatos(descripcion, duracion, cantHoras, cantCreditos, URL, fAlta, auxPrevias, ub);
        
        // Sincronizar los cambios con JPA
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar curso en BD: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public void Add(Curso c) throws Exception{
        misCursos.add(c);
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar el curso: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public Curso BuscarCurso(String nombre){
        if (nombre == null) return null;
        for(int i = 0; i < misCursos.size(); i++){
            Curso c = misCursos.get(i);
            if(c != null && nombre.equals(c.getNombre())){
                return c;
            }
        }
        return null;
    }

    public DTCurso getDT(Curso c){
        if (c == null) return null;

        String ins = (c.getInstituto() != null) ? c.getInstituto().getNombre() : "";
        
        // PROTECCIÓN CONTRA NULOS: Evita el NullPointerException en el unboxing
        int duracionSegura = (c.getDuracion() != null) ? c.getDuracion() : 0;
        float cantHorasSegura = (c.getCantHoras() != null) ? c.getCantHoras() : 0.0f;
        int cantCreditosSeguro = (c.getCantCreditos() != null) ? c.getCantCreditos() : 0;
        
        List<Curso> auxPrevias = c.getPrevias();
        List<String> auxPreviasStr = new ArrayList<>();
        if (auxPrevias != null) {
            for(Curso previa : auxPrevias){
                if (previa != null && previa.getNombre() != null) {
                    auxPreviasStr.add(previa.getNombre());
                }
            }
        }

        List<EdicionCurso> auxEdiciones = c.getEdiciones();
        List<String> auxEdicionesStr = new ArrayList<>();
        if (auxEdiciones != null) {
            for(EdicionCurso edicion : auxEdiciones){
                if (edicion != null && edicion.getNombre() != null) {
                    auxEdicionesStr.add(edicion.getNombre());
                }
            }
        }
        
        List<ProgramaDeFormacion> auxProgramas = c.getProgramas();
        List<String> auxProgramasStr = new ArrayList<>();
        if(auxProgramas != null){
            for(ProgramaDeFormacion programa : auxProgramas){
                if (programa != null && programa.getNombre() != null) {
                    auxProgramasStr.add(programa.getNombre());
                }
            }
        }
        
        return new DTCurso(
            ins,
            c.getNombre(),
            c.getDescripcion(),
            duracionSegura,
            cantHorasSegura,
            cantCreditosSeguro,
            c.getURL(),
            c.getFAlta(),
            auxPreviasStr,
            auxEdicionesStr,
            auxProgramasStr
        );
    }

    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList<>();
        for(int i = 0; i < misCursos.size(); i++){
            DTMaster dt = getDT(misCursos.get(i));
            if (dt != null) {
                auxList.add(dt);
            }
        }
        return auxList;
    }

    public List<DTMaster> getDTLIst(String instituto){
        List<DTMaster> auxList = new ArrayList<>();
        if (instituto == null) return auxList;
        
        for(int i = 0; i < misCursos.size(); i++){
            Curso c = misCursos.get(i);
            if(c != null && c.getInstituto() != null && instituto.equals(c.getInstituto().getNombre())){
                DTMaster dt = getDT(c);
                if (dt != null) {
                    auxList.add(dt);
                }
            }
        }
        return auxList;
    }
}