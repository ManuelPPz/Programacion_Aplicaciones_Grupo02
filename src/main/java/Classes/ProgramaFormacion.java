package Classes;

import DTsClasses.DTProgramaForm;
import DTsClasses.Vigencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Embedded;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "ProgramaFormacion")
public class ProgramaFormacion implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Nombre", unique = true, nullable = false)
    private String nombre;

    @Column(name = "Descripcion")
    private String descripcion;

    @Embedded
    private Vigencia vigenciaPrograma;

    @ManyToMany
    @JoinTable(
        name = "programa_curso",
        joinColumns = @JoinColumn(name = "programa_nombre"),
        inverseJoinColumns = @JoinColumn(name = "curso_nombre")
    )
    private List<Curso> cursos = new ArrayList<>();

    // Constructor vacío obligatorio para JPA
    public ProgramaFormacion() {}

    // Constructor desde DTO (opcional, pero muy útil)
    public ProgramaFormacion(DTProgramaForm dt, List<Curso> cursosEntidades) {
        this.nombre = dt.getNombre();
        this.descripcion = dt.getDescripcion();
        this.vigenciaPrograma = dt.getVigenciaProg();
        this.cursos = cursosEntidades;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Vigencia getVigenciaPrograma() {
        return vigenciaPrograma;
    }

    public void setVigenciaPrograma(Vigencia vigenciaPrograma) {
        this.vigenciaPrograma = vigenciaPrograma;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgramaFormacion)) {
            return false;
        }
        ProgramaFormacion other = (ProgramaFormacion) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.ProgramaFormacion[ nombre=" + nombre + " ]";
    }
}