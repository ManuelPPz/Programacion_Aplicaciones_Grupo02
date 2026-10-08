package Logica.Classes;

import Logica.DTsClasses.DTEdi_Usu;
import Logica.DTsClasses.DTMaster;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Edi_Usu implements Serializable {

    @EmbeddedId
    private Id_EdiUsu id;
    
    @Column(name="Fecha_inscripcion")
    private Date fInscripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private Enum_Estado_inscripcion estadoIns = Enum_Estado_inscripcion.INSCRIPTO; // Valor por defecto
    
    public Id_EdiUsu getId() {
        return id;
    }
    public Date getFIns(){
        return this.fInscripcion;
    }
    
    public Edi_Usu(){}
    
    public Edi_Usu(Id_EdiUsu id, Date fIns){
        this.id = id;
        this.fInscripcion = fIns;
        this.estadoIns = Enum_Estado_inscripcion.INSCRIPTO; // Se inicializa explícitamente en INSCRIPTO
    }

    // Constructor sobrecargado opcional si en algún momento quieres pasar el estado
    public Edi_Usu(Id_EdiUsu id, Date fIns, Enum_Estado_inscripcion estado){
        this.id = id;
        this.fInscripcion = fIns;
        this.estadoIns = (estado != null) ? estado : Enum_Estado_inscripcion.INSCRIPTO;
    }

    public Enum_Estado_inscripcion getMiEstado(){ return this.estadoIns; }
    public void setEstado(Enum_Estado_inscripcion estado){ this.estadoIns = estado; }

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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Classes.Edi_Usu[ id=" + id + " ]";
    }

    public DTMaster getMyDT(){
        return new DTEdi_Usu(id.getUsuario().getNickname(), id.getEdicion().getNombre(), fInscripcion, estadoIns);
    }
}