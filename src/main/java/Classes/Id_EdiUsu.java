/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mateo
 */
@Embeddable
public class Id_EdiUsu implements Serializable {

    @ManyToOne
    private Usuario miUsuario;
    @ManyToOne
    private EdicionCurso miEdicion;
    public Usuario getUsuario() {
        return miUsuario;
    }
    public EdicionCurso getEdicion(){
        return miEdicion;
    }
    public Id_EdiUsu(Usuario ub, EdicionCurso ec){
        this.miUsuario = ub;
        this.miEdicion = ec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id_EdiUsu)) return false;
        Id_EdiUsu that = (Id_EdiUsu) o;
        return Objects.equals(miUsuario, that.miUsuario) &&
               Objects.equals(miEdicion, that.miEdicion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(miUsuario, miEdicion);
    }
    
}
