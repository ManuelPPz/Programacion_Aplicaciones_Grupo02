/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mateo
 */
@Embeddable
public class Id_ProgUsu implements Serializable {
    @ManyToOne
    private Usuario miUsuario;
    @ManyToOne
    private ProgramaDeFormacion miPrograma;
    
    public Usuario getUsuario() {
        return miUsuario;
    }
    public ProgramaDeFormacion getPrograma(){
        return miPrograma;
    }
    public Id_ProgUsu(Usuario ub, ProgramaDeFormacion pdf){
        this.miUsuario = ub;
        this.miPrograma = pdf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id_ProgUsu)) return false;
        Id_ProgUsu that = (Id_ProgUsu) o;
        return Objects.equals(miUsuario, that.miUsuario) &&
               Objects.equals(miPrograma, that.miPrograma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(miUsuario, miPrograma);
    }
    
}
