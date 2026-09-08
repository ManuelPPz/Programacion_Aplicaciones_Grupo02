package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Edi_Usu")
public class Edi_Usu implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Id_EdiUsu id;

    @Temporal(TemporalType.DATE)
    @Column(name = "fInscripcion")
    private Date fInscripcion;

    // 1. Constructor por defecto (OBLIGATORIO para JPA)
    public Edi_Usu() {}

    // 2. Constructor con parámetros
    public Edi_Usu(Id_EdiUsu id, Date fIns) {
        this.id = id;
        this.fInscripcion = fIns;
    }

    // Getters y Setters
    public Id_EdiUsu getId() {
        return id;
    }

    public void setId(Id_EdiUsu id) {
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