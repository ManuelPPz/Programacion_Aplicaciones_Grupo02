package Classes;

import DTsClasses.DTEdi_Usu;
import DTsClasses.DTMaster;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "edi_usu")
public class Edi_Usu implements Serializable {

    @EmbeddedId
    private Id_EdiUsu id;

    @ManyToOne
    @MapsId("miUsuarioNickname") // Se vincula con el campo miUsuarioNickname en Id_EdiUsu
    @JoinColumn(name = "usuario_nickname")
    private Usuario usuario;

    @ManyToOne
    @MapsId("miEdicionNombre") // Se vincula con el campo miEdicionNombre en Id_EdiUsu
    @JoinColumn(name = "edicion_nombre")
    private EdicionCurso edicion;

    @Column(name = "Fecha_inscripcion")
    @Temporal(TemporalType.DATE)
    private Date fInscripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private Enum_Estado_inscripcion estadoIns = Enum_Estado_inscripcion.INSCRIPTO;

    public Edi_Usu() {}

    public Edi_Usu(Usuario usuario, EdicionCurso edicion, Date fIns) {
        this.usuario = usuario;
        this.edicion = edicion;
        this.fInscripcion = fIns;
        this.estadoIns = Enum_Estado_inscripcion.INSCRIPTO;
        // Construimos el ID usando las claves de los objetos
        this.id = new Id_EdiUsu(
            usuario != null ? usuario.getNickname() : null,
            edicion != null ? edicion.getNombre() : null
        );
    }

    public Id_EdiUsu getId() { return id; }
    public void setId(Id_EdiUsu id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public EdicionCurso getEdicion() { return edicion; }
    public void setEdicion(EdicionCurso edicion) { this.edicion = edicion; }

    public Date getFIns() { return this.fInscripcion; }
    public Enum_Estado_inscripcion getMiEstado() { return this.estadoIns; }
    public void setEstado(Enum_Estado_inscripcion estado) { this.estadoIns = estado; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Edi_Usu)) return false;
        Edi_Usu other = (Edi_Usu) object;
        return Objects.equals(this.id, other.id);
    }

    public DTMaster getMyDT() {
        return new DTEdi_Usu(
            usuario != null ? usuario.getNickname() : "",
            edicion != null ? edicion.getNombre() : "",
            fInscripcion,
            estadoIns
        );
    }
}