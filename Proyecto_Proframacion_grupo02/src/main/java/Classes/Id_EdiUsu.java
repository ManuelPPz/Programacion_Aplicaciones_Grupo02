package Classes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Id_EdiUsu implements Serializable {

    @Column(name = "usuario_nickname")
    private String miUsuarioNickname;

    @Column(name = "edicion_nombre")
    private String miEdicionNombre;

    public Id_EdiUsu() {}

    public Id_EdiUsu(String miUsuarioNickname, String miEdicionNombre) {
        this.miUsuarioNickname = miUsuarioNickname;
        this.miEdicionNombre = miEdicionNombre;
    }

    public String getMiUsuarioNickname() {
        return miUsuarioNickname;
    }

    public void setMiUsuarioNickname(String miUsuarioNickname) {
        this.miUsuarioNickname = miUsuarioNickname;
    }

    public String getMiEdicionNombre() {
        return miEdicionNombre;
    }

    public void setMiEdicionNombre(String miEdicionNombre) {
        this.miEdicionNombre = miEdicionNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id_EdiUsu)) return false;
        Id_EdiUsu that = (Id_EdiUsu) o;
        return Objects.equals(miUsuarioNickname, that.miUsuarioNickname) &&
               Objects.equals(miEdicionNombre, that.miEdicionNombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(miUsuarioNickname, miEdicionNombre);
    }
}