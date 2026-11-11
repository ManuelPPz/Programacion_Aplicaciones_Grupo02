package Classes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class Id_Valoracion implements Serializable {

    @Column(name = "usuario_nickname")
    private String miUsuarioNickname;

    @Column(name = "edicion_nombre")
    private String miCursoNombre;
    
    public Id_Valoracion() {}
    
    public Id_Valoracion(String miUsuarioNickname, String miCursoNombre) {
        this.miUsuarioNickname = miUsuarioNickname;
        this.miCursoNombre = miCursoNombre;
    }



    public String getMiUsuarioNickname() {
        return miUsuarioNickname;
    }

    public void setMiUsuarioNickname(String miUsuarioNickname) {
        this.miUsuarioNickname = miUsuarioNickname;
    }

    public String getMiCursoNombre() {
        return miCursoNombre;
    }

    public void setMiCursoNombre(String miCursoNombre) {
        this.miCursoNombre = miCursoNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id_Valoracion)) return false;
        Id_Valoracion that = (Id_Valoracion) o;
        return Objects.equals(miUsuarioNickname, that.miUsuarioNickname) &&
               Objects.equals(miCursoNombre, that.miCursoNombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(miUsuarioNickname, miCursoNombre);
    }
    
}
