/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.DTsClasses;
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
    private String imgPath;
    public DTProgramaForm(){
        super();
    }

    public Vigencia getVigenciaPrograma() {
        return vigenciaPrograma;
    }
    
    // constructor
    public DTProgramaForm(String nomPrograma, String descripcion, Vigencia vigenciaProg, List<String> cursos, Date fechaAlta, List<String> misCategorias, String imgPath) {
        this.nombre = nomPrograma;
        this.descripcion = descripcion;
        this.vigenciaPrograma = vigenciaProg;
        this.cursos = cursos;
        this.fechaAlta = fechaAlta;
        this.misCategorias = misCategorias;
        this.imgPath=imgPath;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    public String getImage(){
        return this.imgPath;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setVigenciaPrograma(Vigencia vigenciaPrograma) {
        this.vigenciaPrograma = vigenciaPrograma;
    }

    public void setCursos(List<String> cursos) {
        this.cursos = cursos;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setMisCategorias(List<String> misCategorias) {
        this.misCategorias = misCategorias;
    }
    
    
}