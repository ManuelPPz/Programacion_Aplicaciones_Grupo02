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
    private String urlVideo;
    private Integer nota;

    public DTEdi_Usu(String nickEstudiante, String nomEdicion, Date fInscripcion, Enum_Estado_inscripcion estadoIns,
            String urlVideo, Integer nota) {
        this(nickEstudiante, nomEdicion, fInscripcion, estadoIns, urlVideo);
        this.nota = nota;
    }

    public DTEdi_Usu(String nickEstudiante, String nomEdicion, Date fInscripcion, Enum_Estado_inscripcion estadoIns,
            String urlVideo) {
        this(nickEstudiante, nomEdicion, fInscripcion, estadoIns);
        this.urlVideo = urlVideo;
    }

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
    

    /** URL del video de motivación, o null si no se adjuntó. */
    public String getUrlVideo() {
        return urlVideo;
    }

    /** Calificación (1 a 12), o null si todavía no se ingresó. */
    public Integer getNota() {
        return nota;
    }
}
