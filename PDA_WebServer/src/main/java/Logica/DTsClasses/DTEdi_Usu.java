/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.DTsClasses;

import java.util.Date;
import Logica.Classes.Enum_Estado_inscripcion;
/**
 *
 * @author mateo
 */
public class DTEdi_Usu extends DTMaster{
    private String nickEstudiante;
    private String nomEdicion;
    private Date fInscripcion;
    private Enum_Estado_inscripcion estadoIns;
    private int nota;
    
    public DTEdi_Usu(){
        super();
    }
    public DTEdi_Usu(String nickEstudiante, String nomEdicion, Date fInscripcion, Enum_Estado_inscripcion estadoIns, int nota) {
        this.nickEstudiante = nickEstudiante;
        this.nomEdicion = nomEdicion;
        this.fInscripcion = fInscripcion;
        this.estadoIns = estadoIns;
        this.nota=nota;
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
    
    public int getNota(){
        return nota;
    }

    public Enum_Estado_inscripcion getEstadoIns() {
        return estadoIns;
    }

    public void setNickEstudiante(String nickEstudiante) {
        this.nickEstudiante = nickEstudiante;
    }

    public void setNomEdicion(String nomEdicion) {
        this.nomEdicion = nomEdicion;
    }

    public void setfInscripcion(Date fInscripcion) {
        this.fInscripcion = fInscripcion;
    }

    public void setEstadoIns(Enum_Estado_inscripcion estadoIns) {
        this.estadoIns = estadoIns;
    }
    
    public void setNota(int nota){
        this.nota=nota;
    }
    
}
