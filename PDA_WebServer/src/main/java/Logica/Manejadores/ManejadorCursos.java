package Logica.Manejadores;

import Classes.Categoria;
import java.util.List;
import java.util.ArrayList;
import Classes.Curso;
import Classes.EdicionCurso; 
import Classes.Instituto;
import Classes.ProgramaDeFormacion;
import Classes.UsuarioBase;
import Logica.DTsClasses.DTCurso;
import Logica.DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import Logica.util.JPAUtil; // Import de la clase utilitaria

public class ManejadorCursos {
    
    //=================Codigo de Singleton=================
    private static ManejadorCursos instance;    
    public static ManejadorCursos GetInstance(){
        if(instance == null){
            instance = new ManejadorCursos();
        }
        return instance;
    }
    
    private ManejadorCursos() {
    }
    //=======================================================
    
    public Curso CrearCurso(Instituto instituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, Date fAlta, List<String> previas, UsuarioBase ub, List<Categoria> categorias){
        Curso returnCurso;
        List<Curso> auxPrevias = new ArrayList<>();
        for(int i = 0; i < previas.size(); i++){
            auxPrevias.add(BuscarCurso(previas.get(i)));
        }
        returnCurso = new Curso(instituto, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, fAlta, auxPrevias, ub, categorias);
        return returnCurso;
    }

    public void ModificarCurso(Curso c, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, Date fAlta, List<String> previas, UsuarioBase ub, List<Categoria> categorias){
        List<Curso> auxPrevias = new ArrayList<>();
        for(int i = 0; i < previas.size(); i++){
            auxPrevias.add(BuscarCurso(previas.get(i)));
        }
        c.ModificarMisDatos(descripcion, duracion, cantHoras, cantCreditos, URL, fAlta, auxPrevias, ub, categorias);
        
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
                System.err.println("Error al actualizar usuario en BD: " + e.getMessage());
            } finally {
                em.close();
            }
    }
    
    public void Add(Curso c) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            // Imprime el error real de la base de datos en la consola del IDE
            System.err.println("Causa raiz: " + (e.getCause() != null ? e.getCause().getMessage() : "Desconocida"));
            if (e.getCause() != null && e.getCause().getCause() != null) {
                System.err.println("Detalle SQL: " + e.getCause().getCause().getMessage());
            }

            throw new Exception("Error al guardar el curso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public Curso BuscarCurso(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        Curso c = null;
        try {
            // Paso 1: Cargar el curso haciendo FETCH JOIN de 'previas'
            List<Curso> resultados = em.createQuery(
                "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.previas WHERE c.nombre = :nombre", Curso.class)
                .setParameter("nombre", nombre)
                .getResultList();

            if (!resultados.isEmpty()) {
                c = resultados.get(0);

                // Paso 2: Inicializar 'misEdiciones' en la misma sesión
                em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misEdiciones WHERE c = :curso", Curso.class)
                    .setParameter("curso", c)
                    .getSingleResult();

                // Paso 3: Inicializar 'misProgramas'
                em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misProgramas WHERE c = :curso", Curso.class)
                    .setParameter("curso", c)
                    .getSingleResult();

                // Paso 4: Inicializar 'misCategorias'
                em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misCategorias WHERE c = :curso", Curso.class)
                    .setParameter("curso", c)
                    .getSingleResult();
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el curso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close(); 
        }
        return c; 
    }
    
    public List<Curso> getList(){
        List<Curso> auxListCur = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
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
            if (!resultados.isEmpty()) {
                resultados = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misCategorias WHERE c IN :cursos", Curso.class
                ).setParameter("cursos", resultados)
                 .getResultList();
            }

            em.getTransaction().commit();

            // Guardar la lista de cursos obtenida
            auxListCur = resultados;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al cargar cursos desde la BD: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return auxListCur;
    }

    public DTCurso getDT(Curso c){
        String ins = (c.getInstituto() != null) ? c.getInstituto().getNombre() : "";
        
        List<Curso> auxPrevias = c.getPrevias();
        List<String> auxPreviasStr = new ArrayList<>();
        if (auxPrevias != null) {
            for(Curso previa : auxPrevias){
                auxPreviasStr.add(previa.getNombre());
            }
        }

        List<EdicionCurso> auxEdiciones = c.getEdiciones();
        List<String> auxEdicionesStr = new ArrayList<>();
        if (auxEdiciones != null) {
            for(EdicionCurso edicion : auxEdiciones){
                if (edicion != null) {
                    auxEdicionesStr.add(edicion.getNombre());
                }
            }
        }
        
        List<ProgramaDeFormacion> auxProgramas = c.getProgramas();
        List<String> auxProgramasStr = new ArrayList<>();
        if(auxProgramas!=null){
            for(ProgramaDeFormacion programa : auxProgramas){
                if (programa != null) {
                    auxProgramasStr.add(programa.getNombre());
                }
            }
        }
        List<Categoria> auxCategoria = c.getCategorias();
        List<String> auxCategoriaStr = new ArrayList<>();
        if(auxCategoria!=null){
            for(Categoria cat : auxCategoria){
                if(cat!=null){
                    auxCategoriaStr.add(cat.getNombre());
                }
            }
        }
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
            auxEdicionesStr,
            auxProgramasStr,
            auxCategoriaStr,
            c.getDocente().getNickname()
        );
    }

    public List<DTMaster> getDTList(){
        List<Curso> auxListCur = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if(auxListCur!=null){
            for(int i = 0; i < auxListCur.size(); i++){
                DTMaster dt = getDT(auxListCur.get(i));
                auxList.add(dt);
            }
        }
        
        return auxList;
    }

    public List<DTMaster> getDTLIst(String instituto){
        List<Curso> auxListCur = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if(auxListCur!=null){
            for(int i = 0; i < auxListCur.size(); i++){
                Curso c = auxListCur.get(i);
                if(c.getInstituto() != null && c.getInstituto().getNombre().equals(instituto)){
                    DTMaster dt = getDT(c);
                    auxList.add(dt);
                }
            }
        }
        
        return auxList;
    }

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }
}