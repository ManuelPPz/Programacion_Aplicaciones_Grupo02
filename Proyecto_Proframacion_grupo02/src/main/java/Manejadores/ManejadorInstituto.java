package Manejadores;

import java.util.List;
import java.util.ArrayList;
import Classes.Instituto;
import DTsClasses.DTMaster;
import DTsClasses.DTInstituto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import util.JPAUtil; // Import de la utilería centralizada

public class ManejadorInstituto {
<<<<<<< HEAD
    private List<Instituto> misInstitutos;
=======
>>>>>>> v2.0.1
    private static ManejadorInstituto instance;

    // Constructora privada para respetar el patrón Singleton
    private ManejadorInstituto(){
<<<<<<< HEAD
        misInstitutos = obtenerTodosLosInstitutos();
=======
>>>>>>> v2.0.1
    }

    public static ManejadorInstituto GetInstance(){
        if(instance == null){
            instance = new ManejadorInstituto();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }

    public Instituto CreaInstituto(String instituto){
        return new Instituto(instituto);
    }

    public void Add(Instituto c) throws Exception{
<<<<<<< HEAD
        misInstitutos.add(c);
=======
>>>>>>> v2.0.1
        EntityManager em = JPAUtil.getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar el instituto: " + e.getMessage());
        } finally{
            em.close();
        }
    }

    public Instituto BuscarInstituto(String instituto){
<<<<<<< HEAD
        for(int i = 0; i < misInstitutos.size(); i++){
            Instituto in = misInstitutos.get(i);
            if(in.getNombre().equals(instituto)){
                return in;
            }
        }
        return null;
    }
=======
        EntityManager em = getEntityManager();
        Instituto i = null;
        try {
            i = em.find(Instituto.class, instituto);
        } catch (Exception e) {
            System.err.println("Error al buscar la edición de curso en el manejador: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); 
            }
        }
        return i; 
    }
    
>>>>>>> v2.0.1

    public DTInstituto getDT(Instituto in){
        if (in == null) return null;
        return new DTInstituto(in.getNombre());
    }

    public List<DTMaster> getDTList(){
<<<<<<< HEAD
        List<DTMaster> auxList = new ArrayList<>();
        for(int i = 0; i < misInstitutos.size(); i++){
            DTMaster dt = getDT(misInstitutos.get(i));
            auxList.add(dt);
        }
        return auxList;
    }

    public List<Instituto> obtenerTodosLosInstitutos() {
=======
        List<Instituto> auxListIns = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if(auxListIns!=null){
            for(int i = 0; i < auxListIns.size(); i++){
                DTMaster dt = getDT(auxListIns.get(i));
                auxList.add(dt);
            }
        }
        
        return auxList;
    }

    public List<Instituto> getList() {
>>>>>>> v2.0.1
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Instituto> query = em.createQuery("SELECT i FROM Instituto i", Instituto.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}