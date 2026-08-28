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
    private Date fechaInicio;
    private Date fechaFin;

    // 1. CONSTRUCTOR VACÍO OBLIGATORIO PARA JPB/HIBERNATE
    public Vigencia() {
    }

    // 2. Constructor con parámetros existente
    public Vigencia(Date fechaInicio, Date fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    // Setters (requeridos por JPA para reconstruir el objeto)
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}