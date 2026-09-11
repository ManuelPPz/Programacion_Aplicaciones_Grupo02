package Manejadores;

import Classes.EdicionCurso;
import Classes.Instituto;
import Classes.Curso;
import Classes.Docente;
import Classes.UsuarioBase;
import Classes.Edi_Usu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import util.JPAUtil; // Import de la utilería centralizada

public class ManejadorEdicionCurso {

    private List<EdicionCurso> misEdiciones;
    
    //=================Codigo de Singleton=================
    private static ManejadorEdicionCurso instance;    
    public static ManejadorEdicionCurso GetInstance(){
        if(instance == null){
            instance = new ManejadorEdicionCurso();
        }
        return instance;
    }
    
    private ManejadorEdicionCurso(){  
        misEdiciones = new ArrayList<>();
        CargarDeBaseDeDatos();
    }
    //=======================================================
    
    private void CargarDeBaseDeDatos(){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<EdicionCurso> query = em.createQuery("SELECT e FROM EdicionCurso e", EdicionCurso.class);
            misEdiciones = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al cargar las ediciones desde la BD: " + e.getMessage());
            misEdiciones = new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    public EdicionCurso CrearEdicion(Instituto instituto, Curso curso, String nombre, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> docentes){
        EdicionCurso returnEdicion;
        returnEdicion = new EdicionCurso(nombre, instituto, curso, fInicio, fFin, cupo, fAlta,docentes);
        return returnEdicion;
    }
    
    public void ModificarDatos(EdicionCurso ec, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> misUsuarios){
        if (ec != null) {
            ec.ModificarDatos(fInicio, fFin, cupo, fAlta, misUsuarios);
        }
    }
    
    public void Add(EdicionCurso ec) throws Exception {
        misEdiciones.add(ec);
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
   
    public EdicionCurso BuscarEdicion(String nombre){
        for(int i = 0; i < misEdiciones.size(); i++){
            EdicionCurso ec = misEdiciones.get(i);
            if(ec.getNombre().equals(nombre)){
                return ec;
            }
        }
        return null;
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
        List<DTMaster> auxList = new ArrayList<>();
        for(int i = 0; i < misEdiciones.size(); i++){
            EdicionCurso ec = misEdiciones.get(i);
            if(ec.getCurso() != null && ec.getCurso().getNombre().equals(curso)){
                DTMaster dt = getDT(ec);
                auxList.add(dt);
            }
        }
        return auxList;
    }
    
    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }
}