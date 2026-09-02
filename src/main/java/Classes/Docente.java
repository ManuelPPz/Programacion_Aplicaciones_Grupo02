/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

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

    @ManyToMany(mappedBy = "misDocentes", fetch = FetchType.EAGER)
    private List<Instituto> misInstitutos;
    
    @OneToMany(mappedBy="miDocente")
    private List<Curso> misCursos;

    public Docente() {
        super();
        misInstitutos = new ArrayList<>();
        misCursos = new ArrayList<>();
    }

    public Docente(String nick, String nombre, String apellido, String correo, Date fNac, byte[] img, List<Instituto> institutos) {
        super(nick, nombre, apellido, correo, fNac, img);
        this.setInstitutos(institutos);
        this.misCursos = new ArrayList<>();
    }

    public void ModificarMisDatos(String nom, String apellido, String correo, Date fNac, byte[] img, List<Instituto> institutos) {
        super.ModificarMisDatos(nom, apellido, correo, fNac, img);
        this.setInstitutos(institutos);
    }

    public void setInstitutos(List<Instituto> institutos) {
        this.misInstitutos = (institutos != null) ? institutos : new ArrayList<>();
        
        // Sincronización bidireccional:
        // Como 'Instituto' es el dueño de la relación (posee la tabla de unión),
        // debemos agregar este docente a la lista del instituto obligatoriamente.
        if (institutos != null) {
            for (Instituto inst : institutos) {
                if (inst.getDocentes() != null && !inst.getDocentes().contains(this)) {
                    inst.getDocentes().add(this);
                }
            }
        }
    }

    public List<Instituto> getInstitutos() {
        return this.misInstitutos;
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
    
}