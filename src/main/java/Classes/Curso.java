/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "Curso")
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="Nickname", unique=true, nullable=false)
    private String nombre;
    @ManyToOne
    @JoinColumn(name="Instituto")
    private Instituto miInstituto;
    @Column(name="Descripcion")
    private String descripcion;
    @Column(name="Duracion")
    private int duracion;
    @Column(name="Cant. Horas")
    private float cantHoras;
    @Column(name="Cant. Creditos")
    private int cantCreditos;
    @Column(name="URL")
    private String URL;
    @Column(name="Fecha Alta")
    private Date fAlta;
    @ManyToMany
    @JoinTable(name = "Previa",joinColumns = @JoinColumn(name="Nombre"),inverseJoinColumns = @JoinColumn(name="Previa_Nombre"))
    private List<Curso> previas;
    @OneToMany(mappedBy="miCurso")
    private List<EdicionCurso> misEdiciones;
    @ManyToMany
    List<ProgramaDeFormacion> misProgramas;
    @ManyToOne
    @JoinColumn(name="Nombre_Doc")
    private Docente miDocente;
    
    
    public Instituto getInstituto() {return miInstituto;}
    public String getNombre() {return nombre;}
    public String getDescripcion(){return descripcion;}
    public int getDuracion() {return duracion;}
    public float getCantHoras(){return cantHoras;}
    public int getCantCreditos() {return cantCreditos;}
    public String getURL(){return URL;}
    public Date getFAlta(){return fAlta;}
    public List<Curso> getPrevias(){return previas;}
    public List<EdicionCurso> getEdiciones(){return misEdiciones;}
    public List<ProgramaDeFormacion> getProgramas(){return this.misProgramas;}
    public Curso(){
        
    }
    public Curso(Instituto instituto,String nombre,String descripcion,int duracion,float cantHoras,int cantCreditos,String URL,Date fAlta,List<Curso>previas, UsuarioBase ub){
        this.miInstituto = instituto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCreditos = cantCreditos;
        this.URL = URL;
        this.fAlta = fAlta;
        this.previas=previas;
        this.miDocente = (Docente)ub;
        this.misProgramas = new ArrayList();
        this.misEdiciones = new ArrayList();
    }

    
    public void ModificarMisDatos(String descripcion,int duracion,float cantHoras,int cantCreditos,String URL,Date fAlta,List<Curso>previas, UsuarioBase ub){
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCreditos = cantCreditos;
        this.URL = URL;
        this.fAlta = fAlta;
        this.previas = previas;
        this.miDocente = (Docente)ub;
    }
    public void AddEdicion(EdicionCurso ec){
        this.misEdiciones.add(ec);
    }
    public void RemoveEdicion(EdicionCurso ec){
        this.misEdiciones.remove(ec);
    }
    
    public void AddPrograma(ProgramaDeFormacion pdf){
        this.misProgramas.add(pdf);
    }
    public void RemovePrograma(ProgramaDeFormacion pdf){
        this.misProgramas.remove(pdf);
    }
    
    
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Docente getMiDocente(){
        return this.miDocente;
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
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.Curso[ nombre=" + nombre + " ]";
    }
    
}
