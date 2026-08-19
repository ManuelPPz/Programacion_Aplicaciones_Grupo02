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
public class DTCurso {
    public String instituto;
    public String nombre;
    public String descripcion;
    public int duracion;
    public int cantHoras;
    public int cantCreditos;
    public String URL;
    public Date fechaAlta;
    public List<String> listPrevias;
    public List<String> listEdiCursos;
    public List<String> listProgCursos;
    public DTCurso(String nomInstituto, String nom, String descripcion, int duracion,int cantHoras,int cantCreditos, String URL, Date fecha, List<String> listPrevias, List<String> listEdiCursos, List<String> listProgCursos){
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
}
