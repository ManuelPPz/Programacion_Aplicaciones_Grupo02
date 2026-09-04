/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Edi_Usu implements Serializable {

    @EmbeddedId
    private Id_EdiUsu id;
    @Column(name="Fecha inscripcion")
    private Date fInscripcion;
    public Id_EdiUsu getId() {
        return id;
    }
    public Date getFIns(){
        return this.fInscripcion;
    }
    public Edi_Usu(Id_EdiUsu id, Date fIns){
        this.id = id;
        this.fInscripcion = fIns;
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
        if (!(object instanceof Edi_Usu)) {
            return false;
        }
        Edi_Usu other = (Edi_Usu) object;
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
