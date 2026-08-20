/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;
import java.util.Date;
/**
 *
 * @author manuelpalumbo
 */
public class Vigencia {
    Date fechaInicio;
    Date fechaFin;
    public Vigencia(Date fechaInicio, Date fechaFin){
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    }
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }
    
}
