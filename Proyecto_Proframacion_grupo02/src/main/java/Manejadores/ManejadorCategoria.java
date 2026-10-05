/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manejadores;

import Classes.Categoria;
import DTsClasses.DTCategoria;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import util.JPAUtil;

/**
 *
 * @author mateo
 */
public class ManejadorCategoria {
    //=================Codigo de Singleton=================
    private static ManejadorCategoria instance;    
    public static ManejadorCategoria GetInstance(){
        if(instance == null){
            instance = new ManejadorCategoria();
        }
        return instance;
    }
    
    private ManejadorCategoria() {
    }
    //=======================================================
    
    public Categoria CrearCategoria(String nombre){
        return new Categoria(nombre);
    }
    public void Add(Categoria c) throws Exception{
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar la categoria: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    public List<Categoria> getList() throws Exception{
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // "Categoria" es el nombre de la clase/entidad Java
            List<Categoria> categorias = em.createQuery("SELECT c FROM Categoria c", Categoria.class)
                                           .getResultList();
            return categorias;
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de categorías: " + e.getMessage());
        } finally {
            em.close(); // Siempre cerrar el EntityManager para liberar conexiones
        }
    }
    
    public Categoria BuscarCategoria(String nombre){
        EntityManager em = JPAUtil.getEntityManager();
        Categoria c = null;
        try {
            c = em.find(Categoria.class, nombre);
        } catch (Exception e) {
            System.err.println("Error al buscar la categoría, error en el manejador ");
        } finally {
            em.close(); 
        }
        return c; 
    }
    
    public DTCategoria getDT(Categoria c){
        return new DTCategoria(c.getNombre());
    }
    public List<DTMaster> getDTList() throws Exception{
        List<Categoria> auxListC = getList();
<<<<<<< HEAD
        System.out.println(auxListC.size());
        List<DTMaster> auxList = new ArrayList<>();
        for(int i = 0; i < auxListC.size(); i++){
            Categoria c = auxListC.get(i);
            if(c.getNombre()!= null){
                DTMaster dt = getDT(c);
                auxList.add(dt);
            }
        }
=======
        List<DTMaster> auxList = new ArrayList<>();
        if(auxListC!=null){
            for(int i = 0; i < auxListC.size(); i++){
                Categoria c = auxListC.get(i);
                if(c.getNombre()!= null){
                    DTMaster dt = getDT(c);
                    auxList.add(dt);
                }
            }
        }
        
>>>>>>> v2.0.1
        return auxList;
    }
}
