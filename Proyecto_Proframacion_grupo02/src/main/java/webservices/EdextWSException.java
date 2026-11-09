package webservices;

import jakarta.xml.ws.WebFault;

/**
 * Error de negocio devuelto por el Web Service. Llega al cliente como SOAP
 * fault con el mensaje original (ver {@link EdextFault}).
 */
@WebFault(name = "EdextFault", targetNamespace = "http://webservices.edext/")
public class EdextWSException extends Exception {

    private static final long serialVersionUID = 1L;

    private final EdextFault faultInfo;

    public EdextWSException(String mensaje) {
        this(mensaje, new EdextFault(mensaje));
    }

    public EdextWSException(String mensaje, Throwable causa) {
        this(mensaje, new EdextFault(mensaje), causa);
    }

    /** Constructor requerido por JAX-WS (mensaje + bean del fault). */
    public EdextWSException(String mensaje, EdextFault faultInfo) {
        super(mensaje);
        this.faultInfo = faultInfo;
    }

    /** Constructor requerido por JAX-WS (mensaje + bean del fault + causa). */
    public EdextWSException(String mensaje, EdextFault faultInfo, Throwable causa) {
        super(mensaje, causa);
        this.faultInfo = faultInfo;
    }

    /** Bean con los datos del error que viaja en el SOAP fault. */
    public EdextFault getFaultInfo() {
        return faultInfo;
    }
}
