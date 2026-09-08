/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "Prog_Usu")
public class Prog_Usu implements Serializable {

    @EmbeddedId
    private Id_ProgUsu id;
    @Temporal(TemporalType.DATE)
    @Column(name = "fInscripcion")
    private Date fInscripcion;
    
    public Prog_Usu(){}
    
    public Prog_Usu(Id_ProgUsu id, Date fIns){
        this.id = id;
        this.fInscripcion = fIns;
    }

    // Getters y Setters
    public Id_ProgUsu getId() {
        return id;
    }

    public void setId(Id_ProgUsu id) {
        this.id = id;
    }

    public Date getFIns() {
        return this.fInscripcion;
    }

    public void setFIns(Date fInscripcion) {
        this.fInscripcion = fInscripcion;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Id_ProgUsu)) {
            return false;
        }
        Prog_Usu other = (Prog_Usu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.Edi_Usu[ id=" + id + " ]";
    }
    
}
