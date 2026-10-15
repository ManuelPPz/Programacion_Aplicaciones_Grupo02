/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Classes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import java.io.Serializable;
import Logica.DTsClasses.Vigencia;
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
    
    @Column(name="F_Alta")
    private Date fAlta;
    
    // CORREGIDO: Se quitó el columnDefinition = "BLOB" para que se guarde como texto plano (VARCHAR)
    @Column(name="ImagePath")
    private String image;
    
    @ManyToOne
    @JoinColumn(name="Instituto")
    private Instituto miInstituto;
    
    @ManyToMany
    @JoinTable(
        name = "programa_curso",
        joinColumns = @JoinColumn(name = "programa_nombre"),
        inverseJoinColumns = @JoinColumn(name = "curso_nombre")
    )
    private List<Curso> cursos;
    
    // Se corrige mappedBy apuntando a la propiedad edicion de Edi_Usu
    @OneToMany(mappedBy = "id.miPrograma", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Prog_Usu> misUsuarios = new ArrayList<>();
    
    // Requerido por JPA/Hibernate
    public ProgramaDeFormacion() {}

    public ProgramaDeFormacion(String nombre, String descripcion, Vigencia vigenciaPrograma, Date fAlta, String imgPath, Instituto instituto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.vigenciaPrograma = vigenciaPrograma;
        this.fAlta = fAlta;
        this.cursos = new ArrayList<>();
        this.image = imgPath;
        this.miInstituto = instituto;
    }
    
    public void ModificarDatos(String descripcion, Vigencia vigenciaPrograma, Date fAlta, String imgPath){
        this.descripcion = descripcion;
        this.vigenciaPrograma = vigenciaPrograma;
        this.fAlta = fAlta;
        this.image = imgPath;
    }
    
    public void AddCurso(Curso c) {
        if (c != null) {
            if (this.cursos == null) {
                this.cursos = new ArrayList<>();
            }

            // Si la lista es un proxy perezoso no inicializado y no hay sesión activa,
            // evitamos el .contains() capturando la excepción.
            boolean yaExiste = false;
            try {
                yaExiste = this.cursos.contains(c);
            } catch (org.hibernate.LazyInitializationException e) {
                // Si la colección está desconectada, inicializamos localmente
                this.cursos = new ArrayList<>();
            }

            if (!yaExiste) {
                this.cursos.add(c);
                c.AddPrograma(this);
            }
        }
    }
    
    public void RemoveCurso(Curso c){
        if (c != null && this.cursos != null) {
            if (this.cursos.remove(c)) {
                c.RemovePrograma(this); // Sincroniza también al remover
            }
        }
    }
    
    public void AddUsuarioInscripto(Prog_Usu pu){
        misUsuarios.add(pu);
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
    
    public String getImage(){
        return this.image;
    }
    public Instituto getInstituo(){
        return this.miInstituto;
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