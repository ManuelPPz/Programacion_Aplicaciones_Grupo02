/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;

import java.util.Date;
import java.util.List;
import Classes.Enum_Estado_Edicion;
/**
 *
 * @author mateo
 */
public class DTEdicionCurso extends DTMaster{
    private String instituto;
    private String curso;
    private String nombre;
    private Date fInicio;
    private Date fFin;
    private int cupo;
    private int cupoActual;
    private List<String> listDocentes;
    private Date fechaAlta;
    private List<DTEdi_Usu> misIscripciones;
    private List<String> misCategorias;
    private String imgPath;
    private Enum_Estado_Edicion eee;
    public DTEdicionCurso(){
        super();
    }

    public Date getfInicio() {
        return fInicio;
    }

    public Date getfFin() {
        return fFin;
    }

    public int getCupoActual() {
        return cupoActual;
    }

    public List<String> getListDocentes() {
        return listDocentes;
    }

    public List<DTEdi_Usu> getMisIscripciones() {
        return misIscripciones;
    }

    public String getImage(){
        return this.imgPath;
    }
    
    public Enum_Estado_Edicion getEstado(){
        return this.eee;
    }
    public DTEdicionCurso(String nomInstituto, String curso, String nombre, Date fInicio, Date fFin, int cupo, int cupoActual, List<String> listDocentes,Date fechaAlta, List<DTEdi_Usu> misInscripciones, List<String> misCategorias, String imgPath, Enum_Estado_Edicion eee){
        this.instituto = nomInstituto;
        this.curso = curso;
        this.nombre = nombre;
        this.fInicio = fInicio;
        this.fFin = fFin;
        this.cupo = cupo;
        this.cupoActual = cupoActual;
        this.listDocentes = listDocentes;
        this.fechaAlta = fechaAlta;
        this.misIscripciones = misInscripciones;
        this.misCategorias = misCategorias;
        this.imgPath=imgPath;
        this.eee=eee;
    }
    public String getInstituto(){return this.instituto;}
    public String getCurso(){return this.curso;}
    public String getNombre(){return this.nombre;}
    public Date getFInicio(){return this.fInicio;}
    public Date getFFin(){return this.fFin;}
    public int getCupo(){return this.cupo;}
    public int getCupoActual;
    public List<String> getDocentes(){return this.listDocentes;}
    public Date getFechaAlta(){return this.fechaAlta;}
    public List<DTEdi_Usu> getMisInscripciones(){return this.misIscripciones;}
    public List<String> getMisCategorias(){return this.misCategorias;}

    public void setInstituto(String instituto) {
        this.instituto = instituto;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setfInicio(Date fInicio) {
        this.fInicio = fInicio;
    }

    public void setfFin(Date fFin) {
        this.fFin = fFin;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public void setCupoActual(int cupoActual) {
        this.cupoActual = cupoActual;
    }

    public void setListDocentes(List<String> listDocentes) {
        this.listDocentes = listDocentes;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setMisIscripciones(List<DTEdi_Usu> misIscripciones) {
        this.misIscripciones = misIscripciones;
    }

    public void setMisCategorias(List<String> misCategorias) {
        this.misCategorias = misCategorias;
    }

    public void setGetCupoActual(int getCupoActual) {
        this.getCupoActual = getCupoActual;
    }
    
    public void setEstado(Enum_Estado_Edicion eee){
        this.eee=eee;
    }
    
    
    
}
