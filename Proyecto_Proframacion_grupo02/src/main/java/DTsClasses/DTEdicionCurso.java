/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;

import java.util.Date;
import java.util.List;
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
    
    public DTEdicionCurso(String nomInstituto, String curso, String nombre, Date fInicio, Date fFin, int cupo, int cupoActual, List<String> listDocentes,Date fechaAlta, List<DTEdi_Usu> misInscripciones, List<String> misCategorias){
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
}
