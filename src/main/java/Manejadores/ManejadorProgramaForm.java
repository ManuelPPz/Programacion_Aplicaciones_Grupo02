/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manejadores;

/**
 *
 * @author manuelpalumbo
 */

import Classes.ProgramaFormacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
public class ManejadorProgramaForm {
    private static ManejadorProgramaForm instance;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");
    
    public static ManejadorProgramaForm getInstance(){
        if(instance == null){
            instance = new ManejadorProgramaForm();
        }
        return instance;
    }
    
    private ManejadorProgramaForm(){
        
    }
    
    public void agregarPrograma(ProgramaFormacion pf) throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(pf); //Insertar objeto en la bd
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
}
