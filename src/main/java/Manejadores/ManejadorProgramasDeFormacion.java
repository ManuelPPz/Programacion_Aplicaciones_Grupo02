package Manejadores;

import Classes.Curso;
import Classes.ProgramaDeFormacion;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import DTsClasses.Vigencia;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.JPAUtil; // Import de la utilería centralizada

public class ManejadorProgramasDeFormacion {
    private List<ProgramaDeFormacion> misProgramas;
    
    //=================Codigo de Singleton=================
    private static ManejadorProgramasDeFormacion instance;    
    
    public static ManejadorProgramasDeFormacion GetInstance(){
        if(instance == null){
            instance = new ManejadorProgramasDeFormacion();
        }
        return instance;
    }
    
    private ManejadorProgramasDeFormacion(){  
        misProgramas = new ArrayList<>();
        CargarDeBaseDeDatos();
    }
    //=======================================================
    
    private void CargarDeBaseDeDatos(){
        EntityManager em = JPAUtil.getEntityManager();
      try {
        misProgramas.clear();
        
        // Carga los programas de formación forzando la hidratación de sus cursos
        List<ProgramaDeFormacion> programas = em.createQuery(
            "SELECT DISTINCT p FROM ProgramaDeFormacion p LEFT JOIN FETCH p.cursos", 
            ProgramaDeFormacion.class
        ).getResultList();

        misProgramas.addAll(programas);
    } finally {
        em.close();
    }
    }
    
    public ProgramaDeFormacion CrearPrograma(String nombre, String descripcion, Vigencia vigencia, Date fAlta){
        ProgramaDeFormacion auxPDF = new ProgramaDeFormacion(nombre, descripcion, vigencia, fAlta);
        return auxPDF;
    }
    
    public void ModificarDatos(String nombre, String descripcion, Vigencia vigenciaPrograma, Date fAlta){
        ProgramaDeFormacion auxPDF = BuscarPrograma(nombre);
        if (auxPDF != null) {
            auxPDF.ModificarDatos(descripcion, vigenciaPrograma, fAlta);
        }
    }
    
    public void Add(ProgramaDeFormacion pdf){
        misProgramas.add(pdf);
        
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
    
    public ProgramaDeFormacion BuscarPrograma(String nombre){
        for(int i = 0; i < misProgramas.size(); i++){
            ProgramaDeFormacion ec = misProgramas.get(i);
            if(ec.getNombre().equals(nombre)){
                return ec;
            }
        }
        return null;
    }
    
    public void AddCurso(ProgramaDeFormacion pdf, Curso c){
        pdf.AddCurso(c);
    }
    
    public DTProgramaForm getDT(ProgramaDeFormacion pdf){
        if (pdf == null) return null;
        
        String nom = pdf.getNombre();
        String desc = pdf.getDescripcion();
        Vigencia v = pdf.getVigencia();
        List<Curso> cursos = pdf.getCursos();
        Date fAlta = pdf.getFAlta();
        List<String> auxList = new ArrayList<>();
        
        if (cursos != null) {
            for(int i = 0; i < cursos.size(); i++){
                if (cursos.get(i) != null) {
                    auxList.add(cursos.get(i).getNombre());
                }
            }
        }
        return new DTProgramaForm(nom, desc, v, auxList, fAlta);
    }
    
    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList<>();
        for(int i = 0; i < misProgramas.size(); i++){
            DTMaster dt = getDT(misProgramas.get(i));
            if (dt != null) {
                auxList.add(dt);
            }
        }
        return auxList;
    }
}