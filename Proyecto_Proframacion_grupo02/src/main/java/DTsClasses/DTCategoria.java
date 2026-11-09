/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
/**
 *
 * @author mateo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DTCategoria extends DTMaster{

    /** Constructor sin argumentos requerido por JAXB (Web Services). */
    protected DTCategoria() {
    }

    private String nombre;

    public DTCategoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
