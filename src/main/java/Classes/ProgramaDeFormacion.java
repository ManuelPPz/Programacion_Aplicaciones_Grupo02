/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import java.io.Serializable;
import DTsClasses.Vigencia;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
@Entity
public class ProgramaDeFormacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String nombre;
    @Column(name="Descripcion")
    private String descripcion;
    @Embedded
    private Vigencia vigenciaPrograma;
    @Column(name="F. Alta")
    private Date fAlta;
    @ManyToMany
    @JoinTable(
        name = "programa_curso",
        joinColumns = @JoinColumn(name = "programa_nombre"),
        inverseJoinColumns = @JoinColumn(name = "curso_nombre")
    )
    private List<Curso> cursos;
    // Requerido por JPA/Hibernate
public ProgramaDeFormacion() {}

    public ProgramaDeFormacion(String nombre, String descripcion, Vigencia vigenciaPrograma, Date fAlta) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.vigenciaPrograma = vigenciaPrograma;
        this.fAlta = fAlta;
        cursos  = new ArrayList<>();
    }
    
    public void ModificarDatos(String descripcion, Vigencia vigenciaPrograma, Date fAlta){
        this.descripcion = descripcion;
        this.vigenciaPrograma = vigenciaPrograma;
        this.fAlta = fAlta;
    }
    
    
    public void AddCurso(Curso c){
        cursos.add(c);
    }
    public void RemoveCurso(Curso c){
        cursos.remove(c);
    }
    
    
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public Vigencia getVigencia(){
        return vigenciaPrograma;
    }
    public List<Curso> getCursos(){
        return cursos;
    }
    public Date getFAlta(){
        return fAlta;
    }
    

    public void setId(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramaDeFormacion)) {
            return false;
        }
        ProgramaDeFormacion other = (ProgramaDeFormacion) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.ProgramaDeFormacion[ id=" + nombre + " ]";
    }
    
}
