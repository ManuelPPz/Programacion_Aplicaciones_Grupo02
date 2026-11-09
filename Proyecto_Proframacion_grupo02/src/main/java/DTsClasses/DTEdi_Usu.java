/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import java.util.Date;
import Classes.Enum_Estado_inscripcion;
/**
 *
 * @author mateo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DTEdi_Usu extends DTMaster{

    /** Constructor sin argumentos requerido por JAXB (Web Services). */
    protected DTEdi_Usu() {
    }

    private String nickEstudiante;
    private String nomEdicion;
    private Date fInscripcion;
    private Enum_Estado_inscripcion estadoIns;

    public DTEdi_Usu(String nickEstudiante, String nomEdicion, Date fInscripcion, Enum_Estado_inscripcion estadoIns) {
        this.nickEstudiante = nickEstudiante;
        this.nomEdicion = nomEdicion;
        this.fInscripcion = fInscripcion;
        this.estadoIns = estadoIns;
    }

    public String getNickEstudiante() {
        return nickEstudiante;
    }

    public String getNomEdicion() {
        return nomEdicion;
    }

    public Date getfInscripcion() {
        return fInscripcion;
    }

    public Enum_Estado_inscripcion getEstadoIns() {
        return estadoIns;
    }
    
}
