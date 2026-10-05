package Logica.Manejadores;

import Classes.EdicionCurso;
import Classes.Instituto;
import Classes.Curso;
import Classes.Docente;
import Classes.UsuarioBase;
import Classes.Edi_Usu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import Logica.util.JPAUtil; // Import de la utilería centralizada

public class ManejadorEdicionCurso {

    
    //=================Codigo de Singleton=================
    private static ManejadorEdicionCurso instance;    
    public static ManejadorEdicionCurso GetInstance(){
        if(instance == null){
            instance = new ManejadorEdicionCurso();
        }
        return instance;
    }
    
    private ManejadorEdicionCurso(){  
    }
    //=======================================================

    
    public EdicionCurso CrearEdicion(Instituto instituto, Curso curso, String nombre, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> docentes){
        EdicionCurso returnEdicion;
        returnEdicion = new EdicionCurso(nombre, instituto, curso, fInicio, fFin, cupo, fAlta,docentes);
        return returnEdicion;
    }
    
    public void ModificarDatos(EdicionCurso ec, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> misUsuarios){
        if (ec != null) {
            ec.ModificarDatos(fInicio, fFin, cupo, fAlta, misUsuarios);
        }
        
        // Sincronizar los cambios con JPA
            EntityManager em = getEntityManager();
            try {
                em.getTransaction().begin();
                em.merge(ec);
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
    
    public void Add(EdicionCurso ec) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // 1. Asegurar la relación bidireccional en memoria
            if (ec.getMisDocentes() != null) {
                for (Docente d : ec.getMisDocentes()) {
                    if (d.getEdiciones() != null && !d.getEdiciones().contains(ec)) {
                        d.getEdiciones().add(ec);
                    }
                }
            }

            // 2. Usar merge(ec) en lugar de persist(ec).
            // merge() busca los docentes por su ID (nickname), los asocia y genera las inserciones 
            // en la tabla intermedia "Docente_EdicionCurso" automáticamente.
            EdicionCurso ecManaged = em.merge(ec);

            em.getTransaction().commit();
            System.out.println(">>> [DEBUG] Transacción COMMIT ejecutada con éxito.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar la edición de curso: " + e.getMessage());
        } finally {
            em.close();
        }
    }
   
    public EdicionCurso BuscarEdicion(String nombre) {
        EntityManager em = getEntityManager();
        EdicionCurso ec = null;
        try {
            ec = em.find(EdicionCurso.class, nombre);
        } catch (Exception e) {
            System.err.println("Error al buscar la edición de curso en el manejador: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); 
            }
        }
        return ec; 
    }
    
    public void AddUsuario(EdicionCurso ec, Docente ub){
        ec.AddUsuarios(ub);
    }
    public void AddUsuarioInscripto(Edi_Usu eu) throws Exception {
        if (eu == null || eu.getId() == null) {
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // 1. Guardar/Actualizar la entidad de asociación en la BD
            Edi_Usu euManaged = em.merge(eu);

            // 2. Sincronizar el modelo en memoria RAM
            if (eu.getId().getEdicion() != null) {
                eu.getId().getEdicion().AddUsuarioInscripto(euManaged);
            }

            em.getTransaction().commit();
            System.out.println(">>> [DEBUG] Inscripción registrada con éxito.");

        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new Exception("Error al guardar la inscripción: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public List<EdicionCurso> getList(){
        List<EdicionCurso> auxListEdi = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<EdicionCurso> query = em.createQuery("SELECT e FROM EdicionCurso e", EdicionCurso.class);
            auxListEdi = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al cargar las ediciones desde la BD: " + e.getMessage());
            auxListEdi = new ArrayList<>();
        } finally {
            em.close();
        }
        return auxListEdi;
    }
    
    
    public DTEdicionCurso getDT(EdicionCurso ec){
        DTEdicionCurso auxDT;
        String ins = (ec.getInstituto() != null) ? ec.getInstituto().getNombre() : "";
        String cur = (ec.getCurso() != null) ? ec.getCurso().getNombre() : "";
        List<Docente> auxUsuarios = ec.getMisDocentes();
        List<String> auxDocentes = new ArrayList<>();
        if (auxUsuarios != null) {
            for(int i = 0; i < auxUsuarios.size(); i++){
                UsuarioBase ub = auxUsuarios.get(i);
                if(ub instanceof Docente d){
                    auxDocentes.add(d.getNickname());
                }
            }
        }
        auxDT = new DTEdicionCurso(ins, cur, ec.getNombre(), ec.getFInicio(), ec.getFFin(), ec.getCupo(), auxDocentes, ec.getFAlta());
        return auxDT;
    }
    
    public List<DTMaster> getDTLIst(String curso){
        List<EdicionCurso> auxListEdi = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if(auxListEdi!=null){
            for(int i = 0; i < auxListEdi.size(); i++){
                EdicionCurso ec = auxListEdi.get(i);
                if(ec.getCurso() != null && ec.getCurso().getNombre().equals(curso)){
                    DTMaster dt = getDT(ec);
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