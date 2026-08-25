/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author mateo
 */
public class DTCurso extends DTMaster{
    private String instituto;
    private String nombre;
    private String descripcion;
    private int duracion;
    private float cantHoras;
    private int cantCreditos;
    private String URL;
    private Date fechaAlta;
    private List<String> listPrevias;
    private List<String> listEdiCursos;
    private List<String> listProgCursos;
    public DTCurso(String nomInstituto, String nom, String descripcion, int duracion,float cantHoras,int cantCreditos, String URL, Date fecha, List<String> listPrevias, List<String> listEdiCursos, List<String> listProgCursos){
        this.instituto = nomInstituto;
        this.nombre = nom;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCreditos = cantCreditos;
        this.URL = URL;
        this.fechaAlta = fecha;
        this.listPrevias = listPrevias;
        this.listEdiCursos = listEdiCursos;
        this.listProgCursos = listProgCursos;
    }
    public String getInstituto(){return this.instituto;}
    public String getNombre(){return this.nombre;}
    public String getDescripcion(){return this.descripcion;}
    public int getDuracion(){return this.duracion;}
    public float getCantHoras(){return this.cantHoras;}
    public int getCantCreditos(){return this.cantCreditos;}
    public String getURL(){return this.URL;}
    public Date getFechaAlta(){return this.fechaAlta;}
    public List<String> getPrevias(){return this.listPrevias;}
    public List<String> getEdiCursos(){return this.listEdiCursos;}
    public List<String> getProgFormacion(){return this.listProgCursos;}
    
}
