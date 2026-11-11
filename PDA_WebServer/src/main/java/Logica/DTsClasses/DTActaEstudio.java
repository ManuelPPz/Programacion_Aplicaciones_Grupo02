package Logica.DTsClasses;

import java.util.Date;
import java.util.List;

/** Acta de estudio de un estudiante para un programa de formación (Requerimiento 7.4). */
public class DTActaEstudio extends DTMaster {

    private final String nickname;
    private final String nombre;
    private final String apellido;
    private final String programa;
    private final String instituto;
    private final Date fechaEmision;
    private final List<DTFilaActa> filas;

    public DTActaEstudio(String nickname, String nombre, String apellido, String programa, String instituto,
            Date fechaEmision, List<DTFilaActa> filas) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.programa = programa;
        this.instituto = instituto;
        this.fechaEmision = fechaEmision;
        this.filas = filas;
    }

    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getPrograma() { return programa; }
    public String getInstituto() { return instituto; }
    public Date getFechaEmision() { return fechaEmision; }
    public List<DTFilaActa> getFilas() { return filas; }

    public int getCantAprobados() {
        int n = 0;
        for (DTFilaActa f : filas) {
            if (f.isAprobado()) {
                n++;
            }
        }
        return n;
    }

    /** true si aprobó todos los cursos del programa (y el programa tiene cursos). */
    public boolean isProgramaAprobado() {
        return !filas.isEmpty() && getCantAprobados() == filas.size();
    }
}
