/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author manuelpalumbo
 */
public class DTProgramaForm extends DTMaster {
    private String nombre;
    private String descripcion;
    private Vigencia vigenciaPrograma;
    private List<String> cursos;
    private Date fechaAlta;
    private List<String> misCategorias;
    
    // constructor
    public DTProgramaForm(String nomPrograma, String descripcion, Vigencia vigenciaProg, List<String> cursos, Date fechaAlta, List<String> misCategorias) {
        this.nombre = nomPrograma;
        this.descripcion = descripcion;
        this.vigenciaPrograma = vigenciaProg;
        this.cursos = cursos;
        this.fechaAlta = fechaAlta;
        this.misCategorias = misCategorias;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public Vigencia getVigenciaProg() {
        return vigenciaPrograma;
    }
    
    public List<String> getCursos() {
        return cursos;
    }
    
    public Date getFechaAlta() {
        return fechaAlta;
    }
    public List<String> getMisCategorias(){
        return this.misCategorias;
    }
}