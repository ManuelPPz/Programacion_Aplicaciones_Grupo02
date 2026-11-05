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
    private List<String> listCategorias;
    private String docente;
    private String imgPath;
    
    public DTCurso(){
        super();
    }
    public DTCurso(String nomInstituto, String nom, String descripcion, int duracion,float cantHoras,int cantCreditos, String URL, Date fecha, List<String> listPrevias, List<String> listEdiCursos, List<String> listProgCursos, List<String> categorias, String docente, String imgPath){
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
        this.listCategorias = categorias;
        this.docente = docente;
        this.imgPath=imgPath;
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
    public List<String> getCategorias(){return this.listCategorias;}
    public String getDocente(){return this.docente;}
    public String getImage(){return this.imgPath;}

    public void setInstituto(String instituto) {
        this.instituto = instituto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setCantHoras(float cantHoras) {
        this.cantHoras = cantHoras;
    }

    public void setCantCreditos(int cantCreditos) {
        this.cantCreditos = cantCreditos;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setListPrevias(List<String> listPrevias) {
        this.listPrevias = listPrevias;
    }

    public void setListEdiCursos(List<String> listEdiCursos) {
        this.listEdiCursos = listEdiCursos;
    }

    public void setListProgCursos(List<String> listProgCursos) {
        this.listProgCursos = listProgCursos;
    }

    public void setListCategorias(List<String> listCategorias) {
        this.listCategorias = listCategorias;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public List<String> getListPrevias() {
        return listPrevias;
    }

    public List<String> getListEdiCursos() {
        return listEdiCursos;
    }

    public List<String> getListProgCursos() {
        return listProgCursos;
    }

    public List<String> getListCategorias() {
        return listCategorias;
    }
    
    
}
