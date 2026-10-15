/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Classes;

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

    //==============Constructores=========================
    public Instituto() {
    }

    public Instituto(String nombre) {
        this.nombre = nombre;
    }
    //====================================================

    //===============Getters y Setters====================
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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