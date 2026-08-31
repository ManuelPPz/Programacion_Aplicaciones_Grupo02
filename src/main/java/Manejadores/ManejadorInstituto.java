/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manejadores;
import java.util.List;
import java.util.ArrayList;
import Classes.Instituto;
import DTsClasses.DTMaster;
import DTsClasses.DTInstituto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
/**
 *
 * @author mateo
 */
public class ManejadorInstituto {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");
    List<Instituto> misInstitutos;
    private static ManejadorInstituto instance;
    public ManejadorInstituto(){
        misInstitutos = new ArrayList();
        misInstitutos = obtenerTodosLosInstitutos();
    }
    public static ManejadorInstituto GetInstance(){
        if(instance==null){
            instance = new ManejadorInstituto();
        }
        return instance;
    }
    
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    
    
    public Instituto CreaInstituto(String instituto){
        return new Instituto(instituto);
    }
    
    public void Add(Instituto c) throws Exception{
        misInstitutos.add(c);
        //Aca se añade a la base de datos
            EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(c); //Insertar objeto en la bd
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
    
    public Instituto BuscarInstituto(String instituto){
        for(int i = 0;i<misInstitutos.size();i++){
            Instituto in = misInstitutos.get(i);
            if(in.getNombre().equals(instituto)){
                return in;
            }
        }
        return null;
    }

    
    
    
    public DTInstituto getDT(Instituto in){
        DTInstituto auxDT;
        auxDT = new DTInstituto(in.getNombre());
        return auxDT;
    }
    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList();
        for(int i = 0;i<misInstitutos.size();i++){
            DTMaster dt = getDT(misInstitutos.get(i));
            auxList.add(dt);
        }
        return auxList;
    }
    public List<Instituto> obtenerTodosLosInstitutos() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Instituto> query = em.createQuery("SELECT i FROM Instituto i", Instituto.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
