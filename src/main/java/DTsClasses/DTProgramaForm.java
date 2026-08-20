/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author manuelpalumbo
 */
public class DTProgramaForm {
    private String nombre;
    private String descripcion;
    private Vigencia vigenciaPrograma;
    private List<DTCurso>cursos;
    //constructor
    public DTProgramaForm(String nomPrograma, String descripcion, Vigencia vigenciaProg, List<DTCurso>cursos){
    this.nombre = nomPrograma;
    this.descripcion = descripcion;
    this.vigenciaPrograma = vigenciaProg;
    this.cursos = cursos;
    
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public Vigencia getVigenciaProg(){
        return vigenciaPrograma;
    }
    
    public List<DTCurso> getCursos(){
        return cursos;
    }
    
    
}

