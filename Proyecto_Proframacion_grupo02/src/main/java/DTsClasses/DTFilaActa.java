package DTsClasses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import java.util.Date;

/** Un curso del programa dentro del acta de estudio del estudiante. */
@XmlAccessorType(XmlAccessType.FIELD)
public class DTFilaActa {

    private String curso;
    private String edicion;      // null si no cursó el curso
    private Integer nota;        // null si no tiene calificación
    private String resultado;    // Aprobado, Reprobado, En curso, ...
    private boolean aprobado;
    private Date fechaResultado; // null si todavía no hay resultado

    /** Constructor sin argumentos requerido por JAXB (Web Services). */
    protected DTFilaActa() {
    }

    public DTFilaActa(String curso, String edicion, Integer nota, String resultado, boolean aprobado, Date fechaResultado) {
        this.curso = curso;
        this.edicion = edicion;
        this.nota = nota;
        this.resultado = resultado;
        this.aprobado = aprobado;
        this.fechaResultado = fechaResultado;
    }

    public String getCurso() { return curso; }
    public String getEdicion() { return edicion; }
    public Integer getNota() { return nota; }
    public String getResultado() { return resultado; }
    public boolean isAprobado() { return aprobado; }
    public Date getFechaResultado() { return fechaResultado; }
}
