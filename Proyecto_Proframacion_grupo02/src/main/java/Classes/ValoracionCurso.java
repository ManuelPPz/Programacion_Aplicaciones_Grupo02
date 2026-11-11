package Classes;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Puntaje (1 a 5 estrellas) que un estudiante le asigna a un curso en el que participó.
 *
 * @author mateo
 */
@Entity
@Table(name = "ValoracionCurso")
public class ValoracionCurso implements Serializable {

    public static final int MIN_PUNTAJE = 1;
    public static final int MAX_PUNTAJE = 5;

    @EmbeddedId
    private Id_Valoracion id;

    @ManyToOne
    @MapsId("miUsuarioNickname")
    @JoinColumn(name = "usuario_nickname")
    private Usuario usuario;

    @ManyToOne
    @MapsId("miCursoNombre")
    @JoinColumn(name = "curso_nombre")
    private Curso curso;

    @Column(name = "puntaje", nullable = false)
    private int puntaje;

    public ValoracionCurso() {
    }

    public ValoracionCurso(Usuario usuario, Curso curso, int puntaje) {
        this.usuario = usuario;
        this.curso = curso;
        this.puntaje = puntaje;
        this.id = new Id_Valoracion(usuario.getNickname(), curso.getNombre());
    }

    public Id_Valoracion getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Curso getCurso() {
        return curso;
    }

    public int getPuntaje() {
        return puntaje;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof ValoracionCurso that && id != null && id.equals(that.id));
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
