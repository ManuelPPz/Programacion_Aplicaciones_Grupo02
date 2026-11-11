package Classes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/** Clave de una valoración: un estudiante valora un curso una única vez. */
@Embeddable
public class Id_Valoracion implements Serializable {

    @Column(name = "usuario_nickname")
    private String miUsuarioNickname;

    @Column(name = "curso_nombre")
    private String miCursoNombre;

    public Id_Valoracion() {
    }

    public Id_Valoracion(String miUsuarioNickname, String miCursoNombre) {
        this.miUsuarioNickname = miUsuarioNickname;
        this.miCursoNombre = miCursoNombre;
    }

    public String getMiUsuarioNickname() {
        return miUsuarioNickname;
    }

    public String getMiCursoNombre() {
        return miCursoNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Id_Valoracion that)) {
            return false;
        }
        return Objects.equals(miUsuarioNickname, that.miUsuarioNickname)
                && Objects.equals(miCursoNombre, that.miCursoNombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(miUsuarioNickname, miCursoNombre);
    }
}
