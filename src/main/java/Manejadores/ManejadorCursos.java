/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manejadores;
import java.util.List;
import java.util.ArrayList;
import Classes.Curso;
import Classes.Instituto;
import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import java.util.Date;
/**
 *
 * @author mateo
 */
public class ManejadorCursos {
    
    List<Curso> misCursos;
    
    //=================Codigo de Singleton=================
    private static ManejadorCursos instance;    
    public static ManejadorCursos GetInstance(){
        if(instance==null){
            instance = new ManejadorCursos();
        }
        return instance;
        
    }
    private ManejadorCursos(){  
        misCursos = new ArrayList();
    }
    //=======================================================
    private void CargarDeBaseDeDatos(){
        //Aca cargas misUsuarios con lo que esta en la base de datos
    }
    
    public Curso CrearCurso(Instituto instituto,String nombre,String descripcion,int duracion,float cantHoras,int cantCreditos,String URL,Date fAlta,List<String>previas){
        Curso returnCurso;
        List<Curso> auxPrevias = new ArrayList();
        for(int i= 0;i<previas.size();i++){
            auxPrevias.add(BuscarCurso(previas.get(i),instituto.getNombre()));
        }
        returnCurso = new Curso(instituto,nombre,descripcion,duracion,cantHoras,cantCreditos,URL,fAlta,auxPrevias);
        return returnCurso;
    }
    public void ModificarCurso(Curso c,String descripcion,int duracion,float cantHoras,int cantCreditos,String URL,Date fAlta,List<String>previas){
        List<Curso> auxPrevias = new ArrayList();
        for(int i= 0;i<previas.size();i++){
            String ins = c.getInstituto().getNombre();
            auxPrevias.add(BuscarCurso(previas.get(i),ins));
        }
        c.ModificarMisDatos(descripcion, duracion, cantHoras, cantCreditos, URL, fAlta,auxPrevias);
    }
    
    
    
    
    
    
    
    public void Add(Curso c){
        misCursos.add(c);
        //Aca se añade a la base de datos
    }
    
    public Curso BuscarCurso(String nombre,String instituto){
        for(int i = 0;i<misCursos.size();i++){
            Curso c = misCursos.get(i);
            Instituto in = c.getInstituto();
            if(c.getNombre().equals(nombre) && in.getNombre().equals(instituto)){
                return c;
            }
        }
        return null;
    }

    
    
    
    public DTCurso getDT(Curso c){
        DTCurso auxDT;
        String ins = c.getInstituto().getNombre();
        List<Curso>auxPrevias = c.getPrevias();
        List<String> auxPreviasStr = new ArrayList();
        for(int i=0;i<auxPrevias.size();i++){
            auxPreviasStr.add(auxPrevias.get(i).getNombre());
        }
        auxDT = new DTCurso(ins,c.getNombre(),c.getDescripcion(),c.getDuracion(),c.getCantHoras(),c.getCantCreditos(),c.getURL(),c.getFAlta(),auxPreviasStr,null,null);
        System.out.println("en getdt auxdt: "+auxDT.getDescripcion());
        System.out.println("en getdt c: "+c.getDescripcion());
        return auxDT;
    }
    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList();
        for(int i = 0;i<misCursos.size();i++){
            DTMaster dt = getDT(misCursos.get(i));
            auxList.add(dt);
        }
        return auxList;
    }
    public List<DTMaster> getDTLIst(String instituto){
        List<DTMaster> auxList = new ArrayList();
        for(int i = 0;i<misCursos.size();i++){
            Curso c = misCursos.get(i);
            if(c.getInstituto().getNombre().equals(instituto)){
                DTMaster dt = getDT(c);
                auxList.add(dt);
            }
        }
        return auxList;
    }
    
    
    
}
