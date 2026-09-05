/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
/**
 *
 * @author mateo
 */
@Entity
public class Usuario extends UsuarioBase{
    @ManyToMany(mappedBy="miUsuario")
    private List<Edi_Usu> misInscripciones;
    public Usuario(){
        super();
    }
    public Usuario(String nick, String nombre, String apellido, String correo, Date fNac,byte[] img){
        super(nick, nombre, apellido, correo, fNac,img);
    }
    
    @Override
    public void ModificarMisDatos(String nom, String apellido, String correo, Date fNac,byte[] img){
        super.ModificarMisDatos(nom, apellido, correo, fNac, img);
    }
    
    public void AddEdicionCurso(Edi_Usu ec){
        misInscripciones.add(ec);
    }
    public List<Edi_Usu> getMisEdiciones(){return this.misInscripciones;}
}
