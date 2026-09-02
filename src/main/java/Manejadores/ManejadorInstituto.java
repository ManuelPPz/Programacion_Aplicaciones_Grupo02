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
    private List<Instituto> misInstitutos;
    private static ManejadorInstituto instance;

    // Constructora privada para respetar el patrón Singleton
    private ManejadorInstituto(){
        misInstitutos = obtenerTodosLosInstitutos();
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
        misInstitutos.add(c);
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
        for(int i = 0; i < misInstitutos.size(); i++){
            Instituto in = misInstitutos.get(i);
            if(in.getNombre().equals(instituto)){
                return in;
            }
        }
        return null;
    }

    public DTInstituto getDT(Instituto in){
        if (in == null) return null;
        return new DTInstituto(in.getNombre());
    }

    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList<>();
        for(int i = 0; i < misInstitutos.size(); i++){
            DTMaster dt = getDT(misInstitutos.get(i));
            auxList.add(dt);
        }
        return auxList;
    }

    public List<Instituto> obtenerTodosLosInstitutos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Instituto> query = em.createQuery("SELECT i FROM Instituto i", Instituto.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}