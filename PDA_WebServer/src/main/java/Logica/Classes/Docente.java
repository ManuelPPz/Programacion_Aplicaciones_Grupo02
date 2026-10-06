/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import java.util.List;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "Docente")
@PrimaryKeyJoinColumn(name = "nickname")
public class Docente extends UsuarioBase {
    
    @ManyToOne
    @JoinColumn(name="Instituto")
    private Instituto miInstituto;
    
    // En la clase Docente.java
    @OneToMany(mappedBy = "miDocente", fetch = FetchType.EAGER)
    private List<Curso> misCursos = new ArrayList<>();
    
    @ManyToMany(mappedBy = "misDocentes", fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT) // Para evitar conflictos de List con EAGER
    private List<EdicionCurso> misEdiciones = new ArrayList<>();
            
    
    public Docente() {
        super();
        misCursos = new ArrayList<>();
    }

    public Docente(String nick, String nombre, String apellido, String correo, String contrasenia, Date fNac, byte[] img, Instituto instituto) {
        super(nick, nombre, apellido, correo, contrasenia, fNac, img);
        this.miInstituto = instituto;
        this.misCursos = new ArrayList<>();
    }

    public void ModificarMisDatos(String nom, String apellido, String correo, Date fNac, byte[] img, Instituto instituto) {
        super.ModificarMisDatos(nom, apellido, correo, fNac, img);
        this.miInstituto = instituto;
    }


    public Instituto getInstituto() {
        return this.miInstituto;
    }
    public void setInstituto(Instituto i){
        this.miInstituto = i;
    }
    
    public void AddCurso(Curso c){
        misCursos.add(c);
    }
    public void RemoveCurso(Curso c){
        misCursos.remove(c);
    } 
    public List<Curso> getCursos(){
        return this.misCursos;
    }
    
    
    public void AddEdicion(EdicionCurso ec){
        misEdiciones.add(ec);
    }
    public void RemoveEdicion(EdicionCurso ec){
        misEdiciones.remove(ec);
    }
    public List<EdicionCurso> getEdiciones(){
        return this.misEdiciones;
    }
    public List<EdicionCurso> getMisEdiciones() {
    return misEdiciones;
}

public void setMisEdiciones(List<EdicionCurso> misEdiciones) {
    this.misEdiciones = misEdiciones;
}
    
}