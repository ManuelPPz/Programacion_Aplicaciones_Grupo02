/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manejadores;
import Classes.Curso;
import Classes.ProgramaDeFormacion;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import DTsClasses.Vigencia;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author mateo
 */
public class ManejadorProgramasDeFormacion {
    List<ProgramaDeFormacion> misProgramas;
    //=================Codigo de Singleton=================
    private static ManejadorProgramasDeFormacion instance;    
    public static ManejadorProgramasDeFormacion GetInstance(){
        if(instance==null){
            instance = new ManejadorProgramasDeFormacion();
        }
        return instance;
        
    }
    private ManejadorProgramasDeFormacion(){  
        misProgramas = new ArrayList();
    }
    //=======================================================
    private void CargarDeBaseDeDatos(){
        //Aca cargas misUsuarios con lo que esta en la base de datos
    }
    public ProgramaDeFormacion CrearPrograma(String nombre,String descripcion,Vigencia vigencia, Date fAlta){
        ProgramaDeFormacion auxPDF;
        auxPDF = new ProgramaDeFormacion(nombre,descripcion,vigencia, fAlta);
        return auxPDF;
    }
    
    public void ModificarDatos(String nombre, String descripcion, Vigencia vigenciaPrograma, Date fAlta){
        ProgramaDeFormacion auxPDF = BuscarPrograma(nombre);
        auxPDF.ModificarDatos(descripcion, vigenciaPrograma, fAlta);
    }
    
    public void Add(ProgramaDeFormacion pdf){
        misProgramas.add(pdf);
        //Agregar tambien a la base de datos
    }
    
    public ProgramaDeFormacion BuscarPrograma(String nombre){
        for(int i = 0;i<misProgramas.size();i++){
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
        DTProgramaForm auxDT;
        String nom = pdf.getNombre();
        String desc = pdf.getDescripcion();
        Vigencia v = pdf.getVigencia();
        List<Curso> cursos = pdf.getCursos();
        Date fAlta = pdf.getFAlta();
        List<String> auxList = new ArrayList();
        for(int i=0;i<cursos.size();i++){
            String auxStr = cursos.get(i).getNombre();
            auxList.add(auxStr);
        }
        auxDT = new DTProgramaForm(nom,desc,v,auxList,fAlta);
        return auxDT;
    }
    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList();
        for(int i = 0;i<misProgramas.size();i++){
            DTMaster dt = getDT(misProgramas.get(i));
            auxList.add(dt);
        }
        return auxList;
    }
}
