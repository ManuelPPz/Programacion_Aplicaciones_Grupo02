/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author mateo
 */
@Entity
public class Instituto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String nombre;

    // Cambios clave: FetchType.EAGER para cargar la relación automáticamente
    // e inicializar la colección = new ArrayList<>()
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Ins_Doc",
        joinColumns = @JoinColumn(name = "Instituto"),
        inverseJoinColumns = @JoinColumn(name = "Docente")
    )
    private List<Docente> misDocentes = new ArrayList<>();

    //==============Constructores=========================
    public Instituto() {
        this.misDocentes = new ArrayList<>();
    }

    public Instituto(String nombre) {
        this.nombre = nombre;
        this.misDocentes = new ArrayList<>();
    }
    //====================================================

    //===============Getters y Setters====================
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Docente> getDocentes() {
        if (this.misDocentes == null) {
            this.misDocentes = new ArrayList<>();
        }
        return this.misDocentes;
    }

    public void setDocentes(List<Docente> docentes) {
        this.misDocentes = (docentes != null) ? docentes : new ArrayList<>();
    }

    //===============Método Auxiliar=======================
    // Permite agregar docentes de forma bidireccional y segura
    public void addDocente(Docente doc) {
        if (doc != null) {
            if (this.misDocentes == null) {
                this.misDocentes = new ArrayList<>();
            }
            if (!this.misDocentes.contains(doc)) {
                this.misDocentes.add(doc);
            }
        }
    }
    //====================================================

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Instituto)) {
            return false;
        }
        Instituto other = (Instituto) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.Instituto[ id=" + nombre + " ]";
    }
}