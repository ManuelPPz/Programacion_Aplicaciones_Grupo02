package DTsClasses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Acta de estudio de un estudiante para un programa de formación (Requerimiento 7.4). */
@XmlAccessorType(XmlAccessType.FIELD)
public class DTActaEstudio extends DTMaster {

    private String nickname;
    private String nombre;
    private String apellido;
    private String programa;
    private String instituto;
    private Date fechaEmision;
    private List<DTFilaActa> filas = new ArrayList<>();

    /** Constructor sin argumentos requerido por JAXB (Web Services). */
    protected DTActaEstudio() {
    }

    public DTActaEstudio(String nickname, String nombre, String apellido, String programa, String instituto,
            Date fechaEmision, List<DTFilaActa> filas) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.programa = programa;
        this.instituto = instituto;
        this.fechaEmision = fechaEmision;
        this.filas = (filas != null) ? filas : new ArrayList<>();
    }

    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getPrograma() { return programa; }
    public String getInstituto() { return instituto; }
    public Date getFechaEmision() { return fechaEmision; }
    public List<DTFilaActa> getFilas() { return filas; }
}
