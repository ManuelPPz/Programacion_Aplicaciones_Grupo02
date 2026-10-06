package Manejadores;

import Classes.Categoria;
import Classes.Curso;
import Classes.Prog_Usu;
import Classes.ProgramaDeFormacion;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import DTsClasses.Vigencia;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.JPAUtil;

public class ManejadorProgramasDeFormacion {
    
    //=================Codigo de Singleton=================
    private static ManejadorProgramasDeFormacion instance;    
    
    public static ManejadorProgramasDeFormacion GetInstance(){
        if(instance == null){
            instance = new ManejadorProgramasDeFormacion();
        }
        return instance;
    }
    
    
    public ProgramaDeFormacion CrearPrograma(String nombre, String descripcion, Vigencia vigencia, Date fAlta){
        ProgramaDeFormacion auxPDF = new ProgramaDeFormacion(nombre, descripcion, vigencia, fAlta);
        return auxPDF;
    }
    
    public void ModificarDatos(String nombre, String descripcion, Vigencia vigenciaPrograma, Date fAlta){
        ProgramaDeFormacion auxPDF = BuscarPrograma(nombre);
        if (auxPDF != null) {
            auxPDF.ModificarDatos(descripcion, vigenciaPrograma, fAlta);
            
            EntityManager em = JPAUtil.getEntityManager();
            try {
                em.getTransaction().begin();
                em.merge(auxPDF);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new RuntimeException("Error al actualizar el programa: " + e.getMessage(), e);
            } finally {
                em.close();
            }
        }
    }
    
    public void Add(ProgramaDeFormacion pdf){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pdf);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar el programa: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public void AddUsuarioInscripto(Prog_Usu pu) throws Exception {
        if (pu == null || pu.getId() == null) {
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // 1. Guardar/Actualizar la entidad de asociación en la BD
            Prog_Usu euManaged = em.merge(pu);

            // 2. Sincronizar el modelo en memoria RAM
            if (pu.getId().getPrograma() != null) {
                pu.getId().getPrograma().AddUsuarioInscripto(euManaged);
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
    
    public ProgramaDeFormacion BuscarPrograma(String nombre){
        EntityManager em = JPAUtil.getEntityManager();
        ProgramaDeFormacion pdf = null;
        try {
            pdf = em.find(ProgramaDeFormacion.class, nombre);
        } catch (Exception e) {
            System.err.println("Error al buscar la edición de curso en el manejador: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); 
            }
        }
        return pdf; 
    }
    
    public void AddCurso(ProgramaDeFormacion pdf, Curso c){
        if (pdf == null || c == null) return;
        
        // 1. Actualizar en memoria RAM local
        pdf.AddCurso(c);
        
        // 2. Persistir la relación ManyToMany en la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Unir o buscar los objetos dentro de la sesión activa de JPA
            ProgramaDeFormacion pdfMerged = em.find(ProgramaDeFormacion.class, pdf.getNombre());
            Curso cMerged = em.find(Curso.class, c.getNombre());
            
            if (pdfMerged != null && cMerged != null) {
                // Sincronizar bidireccionalmente en las entidades gestionadas por JPA
                pdfMerged.AddCurso(cMerged);
                cMerged.AddPrograma(pdfMerged);

                em.merge(pdfMerged);
                em.merge(cMerged);
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al asociar el curso al programa de formación: " + e.getMessage(), e);
        } finally {
            em.close();
        }
        // 2. CRUCIAL: Sincronizar AMBOS objetos en la memoria RAM local (fuera del EM)
        // Para que los Manejadores en memoria vean los cambios AL INSTANTE
        pdf.AddCurso(c);
        c.AddPrograma(pdf);
    }
    
        private ManejadorProgramasDeFormacion(){  
    }
    //=======================================================
    
    private List<ProgramaDeFormacion> getList(){
        List<ProgramaDeFormacion> auxList = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
     try {
            auxList.clear();
            
            // Cargar programas e hidratar la lista de cursos
            List<ProgramaDeFormacion> programas = em.createQuery(
                "SELECT DISTINCT p FROM ProgramaDeFormacion p LEFT JOIN FETCH p.cursos", 
                ProgramaDeFormacion.class
            ).getResultList();

            auxList.addAll(programas);
        } finally {
            em.close();
        }
     return auxList;
    }
    
    
    public DTProgramaForm getDT(ProgramaDeFormacion pdf){
        if (pdf == null) return null;
        
        String nom = pdf.getNombre();
        String desc = pdf.getDescripcion();
        Vigencia v = pdf.getVigencia();
        List<Curso> cursos = pdf.getCursos();
        Date fAlta = pdf.getFAlta();
        List<String> auxList = new ArrayList<>();
        
        List<String> auxListCat = new ArrayList<>();
        if (cursos != null) {
            for(int i = 0; i < cursos.size(); i++){
                if (cursos.get(i) != null) {
                    auxList.add(cursos.get(i).getNombre());
                    List<Categoria> auxCat = cursos.get(i).getCategorias();
                    if(auxCat!=null){
                        for(Categoria c : auxCat){
                            String nomCat = c.getNombre();
                            if(!auxListCat.contains(nomCat)){
                                auxListCat.add(nomCat);
                            }
                        }
                    }
                }
            }
        }
        return new DTProgramaForm(nom, desc, v, auxList, fAlta, auxListCat);
    }
    
    public List<DTMaster> getDTList(){
        List<ProgramaDeFormacion> auxListPdf = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if(auxListPdf!=null){
            for(int i = 0; i < auxListPdf.size(); i++){
                DTMaster dt = getDT(auxListPdf.get(i));
                if (dt != null) {
                    auxList.add(dt);
                }
            }
        }
        
        return auxList;
    }
}