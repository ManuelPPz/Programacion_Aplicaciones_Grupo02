package webservices;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/** Datos del error que viajan dentro del SOAP fault de {@link EdextWSException}. */
@XmlAccessorType(XmlAccessType.FIELD)
public class EdextFault {

    private String mensaje;

    public EdextFault() {
    }

    public EdextFault(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
