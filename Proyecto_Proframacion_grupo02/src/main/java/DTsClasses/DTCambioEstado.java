package DTsClasses;

import Classes.Enum_Estado_inscripcion;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * Cambio de estado de la inscripción de un estudiante a una edición.
 * Reemplaza al Object[]{nickname, estado} para poder enviarlo por Web Service.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DTCambioEstado extends DTMaster {

    private String nickname;
    private Enum_Estado_inscripcion estado;

    /** Constructor sin argumentos requerido por JAXB (Web Services). */
    protected DTCambioEstado() {
    }

    public DTCambioEstado(String nickname, Enum_Estado_inscripcion estado) {
        this.nickname = nickname;
        this.estado = estado;
    }

    public String getNickname() {
        return nickname;
    }

    public Enum_Estado_inscripcion getEstado() {
        return estado;
    }
}
