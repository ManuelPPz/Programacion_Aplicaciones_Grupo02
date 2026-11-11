package Logica.DTsClasses;

import java.util.Date;

/** Un curso del programa dentro del acta de estudio del estudiante. */
public class DTFilaActa {

    private final String curso;
    private final String edicion;
    private final Integer nota;
    private final String resultado;
    private final boolean aprobado;
    private final Date fechaResultado;

    public DTFilaActa(String curso, String edicion, Integer nota, String resultado, boolean aprobado, Date fechaResultado) {
        this.curso = curso;
        this.edicion = edicion;
        this.nota = nota;
        this.resultado = resultado;
        this.aprobado = aprobado;
        this.fechaResultado = fechaResultado;
    }

    public String getCurso() { return curso; }
    /** Edición cursada, o null si no cursó el curso. */
    public String getEdicion() { return edicion; }
    /** Calificación (1 a 12), o null si no tiene. */
    public Integer getNota() { return nota; }
    public String getResultado() { return resultado; }
    public boolean isAprobado() { return aprobado; }
    /** Fecha del resultado (cierre de la edición), o null si todavía no hay. */
    public Date getFechaResultado() { return fechaResultado; }
}
