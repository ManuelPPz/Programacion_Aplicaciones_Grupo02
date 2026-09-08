package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mateoa
 */
@Entity
@Table(name = "EdicionCurso")
public class EdicionCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Nombre", unique = true, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "Instituto_Nombre")
    private Instituto miInstituto;

    @ManyToOne
    @JoinColumn(name = "Curso_Nombre")
    private Curso miCurso;

    @Temporal(TemporalType.DATE) 
    @Column(name = "fInicio")
    private Date fInicio;

    @Temporal(TemporalType.DATE) 
    @Column(name = "fFin")
    private Date fFin;

    @Column(name = "Cupo")
    private int cupo;

    // Se corrige mappedBy apuntando a la propiedad edicion de Edi_Usu
    @OneToMany(mappedBy = "id.miEdicion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Edi_Usu> misUsuarios = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "Docente_EdicionCurso",
        joinColumns = @JoinColumn(name = "ediciones_Nombre", referencedColumnName = "Nombre"),
        inverseJoinColumns = @JoinColumn(name = "docentes_Nickname", referencedColumnName = "Nickname")
    )
    private List<Docente> misDocentes = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "F_Alta")
    private Date fAlta;

    // Constructores
    public EdicionCurso() { 
        this.misUsuarios = new ArrayList<>();
        this.misDocentes = new ArrayList<>();
    }

    public EdicionCurso(String nombre, Instituto miInstituto, Curso miCurso, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> docentes) {
        this.nombre = nombre;
        this.miInstituto = miInstituto;
        this.miCurso = miCurso;
        this.fInicio = fInicio;
        this.fFin = fFin;
        this.cupo = cupo;
        this.fAlta = fAlta;
        this.misUsuarios = new ArrayList<>();
        this.misDocentes = docentes != null ? docentes : new ArrayList<>();
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Instituto getInstituto() { return miInstituto; }
    public void setInstituto(Instituto miInstituto) { this.miInstituto = miInstituto; }

    public Curso getCurso() { return miCurso; }
    public void setCurso(Curso miCurso) { this.miCurso = miCurso; }

    public Date getFInicio() { return fInicio; }
    public void setFInicio(Date fInicio) { this.fInicio = fInicio; }

    public Date getFFin() { return fFin; }
    public void setFFin(Date fFin) { this.fFin = fFin; }

    public int getCupo() { return cupo; }
    public void setCupo(int cupo) { this.cupo = cupo; }

    public List<Docente> getMisDocentes() { return misDocentes; }
    public void setMisDocentes(List<Docente> misDocentes) { this.misDocentes = misDocentes; }

    public List<Edi_Usu> getMisUsuarios() { return misUsuarios; }
    
    // Alias por si lo llamas como getMisInscripciones() en otros manejadores
    public List<Edi_Usu> getMisInscripciones() { return misUsuarios; }

    public Date getFAlta() { return fAlta; }
    public void setFAlta(Date fAlta) { this.fAlta = fAlta; }

    // Métodos de Dominio
    public void ModificarDatos(Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> newDocentes) {
        this.fInicio = fInicio;
        this.fFin = fFin;
        this.cupo = cupo;
        this.fAlta = fAlta;
        this.misDocentes = newDocentes;
    }

    public void AddUsuarioInscripto(Edi_Usu eu) {
        if (this.misUsuarios == null) {
            this.misUsuarios = new ArrayList<>();
        }
        if (!this.misUsuarios.contains(eu)) {
            this.misUsuarios.add(eu);
        }
    }

    public void AddUsuarios(Docente ub) {
        if (this.misDocentes == null) {
            this.misDocentes = new ArrayList<>();
        }
        if (!this.misDocentes.contains(ub)) {
            this.misDocentes.add(ub);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdicionCurso)) {
            return false;
        }
        EdicionCurso other = (EdicionCurso) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.EdicionCurso[ id=" + nombre + " ]";
    }
}