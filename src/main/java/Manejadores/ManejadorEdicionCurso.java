package Manejadores;
import Classes.EdicionCurso;
import Classes.Instituto;
import Classes.Curso;
import Classes.Docente;
import Classes.UsuarioBase;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;


public class ManejadorEdicionCurso {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");
    List<EdicionCurso> misEdiciones;
    //=================Codigo de Singleton=================
    private static ManejadorEdicionCurso instance;    
    public static ManejadorEdicionCurso GetInstance(){
        if(instance==null){
            instance = new ManejadorEdicionCurso();
        }
        return instance;
        
    }
    private ManejadorEdicionCurso(){  
        misEdiciones = new ArrayList();
        CargarDeBaseDeDatos();
    }
    //=======================================================
    private void CargarDeBaseDeDatos(){
        //Aca cargas misUsuarios con lo que esta en la base de datos
        EntityManager em = getEntityManager();
    try {
        // Consulta JPQL para seleccionar todas las ediciones de curso de la BD
        TypedQuery<EdicionCurso> query = em.createQuery("SELECT e FROM EdicionCurso e", EdicionCurso.class);
        misEdiciones = query.getResultList();
    } catch (Exception e) {
        System.err.println("Error al cargar las ediciones desde la BD: " + e.getMessage());
        misEdiciones = new ArrayList<>(); // Inicializa vacía si falla la carga
    } finally {
        em.close(); // cerrar el EntityManager
    }
    }
    public EdicionCurso CrearEdicion(Instituto instituto, Curso curso, String nombre, Date fInicio,Date fFin, int cupo, Date fAlta){
        EdicionCurso returnEdicion;
        returnEdicion = new EdicionCurso(nombre, instituto,curso,fInicio,fFin,cupo,fAlta);
        return returnEdicion;
    }
    public void Add(EdicionCurso ec) throws Exception{
        misEdiciones.add(ec);
        //Aca se añade a la base de datos
            EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(ec); //Insertar objeto en la bd
            em.getTransaction().commit();
        } catch (Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar el programa" + e.getMessage());
        }finally{
            em.close();
        }
    }
    
    public EdicionCurso BuscarEdicion(String nombre){
        for(int i = 0;i<misEdiciones.size();i++){
            EdicionCurso ec = misEdiciones.get(i);
            if(ec.getNombre().equals(nombre)){
                return ec;
            }
        }
        return null;
    }
    public void AddUsuario(EdicionCurso ec, UsuarioBase ub){
        ec.AddUsuarios(ub);
    }
    
    public DTEdicionCurso getDT(EdicionCurso ec){
        DTEdicionCurso auxDT;
        String ins = ec.getInstituto().getNombre();
        String cur = ec.getCurso().getNombre();
        List<UsuarioBase>auxUsuarios = ec.getMisUsuarios();
        List<String> auxDocentes = new ArrayList();
        for(int i=0;i<auxUsuarios.size();i++){
            UsuarioBase ub = auxUsuarios.get(i);
            if(ub instanceof Docente d){
                auxDocentes.add(d.getNombre());
            }
            
        }
        auxDT = new DTEdicionCurso(ins,cur,ec.getNombre(),ec.getFInicio(),ec.getFFin(), ec.getCupo(),auxDocentes,ec.getFAlta());
        return auxDT;
    }
    
    public List<DTMaster> getDTLIst(String curso){
        List<DTMaster> auxList = new ArrayList();
        for(int i = 0;i<misEdiciones.size();i++){
            EdicionCurso ec = misEdiciones.get(i);
            if(ec.getCurso().getNombre().equals(curso)){
                DTMaster dt = getDT(ec);
                auxList.add(dt);
            }
        }
        return auxList;
    }
    
    private EntityManager getEntityManager() {
    return emf.createEntityManager();
    }
}
