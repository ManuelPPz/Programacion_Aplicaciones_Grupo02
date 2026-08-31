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
public class EdicionCurso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="Nombre", unique=true, nullable=false)
    private String nombre;
    @ManyToOne
    @JoinColumn(name="Instituto")
    private Instituto miInstituto;
    @ManyToOne
    @JoinColumn(name="Curso")
    private Curso miCurso;
    @Column(name="F. Inicio")
    private Date fInicio;
    @Column(name="F. Fin")
    private Date fFin;
    @Column(name="Cupo")
    private int cupo;
    @ManyToMany
    private List<UsuarioBase> misUsuarios;
    @Column(name="F. Alta")
    private Date fAlta;
    
    public String getNombre(){return nombre;}
    public Instituto getInstituto() {return miInstituto;}
    public Curso getCurso() {return miCurso;}
    public Date getFInicio() {return fInicio;}
    public Date getFFin() {return fFin;}
    public int getCupo() {return cupo;}
    public List<UsuarioBase> getMisUsuarios() {return misUsuarios;}
    public Date getFAlta() {return fAlta;}

    public EdicionCurso(String nombre, Instituto miInstituto, Curso miCurso, Date fInicio, Date fFin, int cupo, Date fAlta) {
        this.nombre = nombre;
        this.miInstituto = miInstituto;
        this.miCurso = miCurso;
        this.fInicio = fInicio;
        this.fFin = fFin;
        this.cupo = cupo;
        this.fAlta = fAlta;
        misUsuarios = new ArrayList();
    }
    
    public void AddUsuarios(UsuarioBase ub){
        misUsuarios.add(ub);
    }
    public void setNombre(String nombre) {
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
        if (!(object instanceof EdicionCurso)) {
            return false;
        }
        EdicionCurso other = (EdicionCurso) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.EdicionCurso[ id=" + nombre + " ]";
    }
    
}
