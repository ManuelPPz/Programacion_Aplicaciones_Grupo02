/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import javax.swing.ImageIcon;

/**
 *
 * @author mateo
 */
@Entity
public class Docente extends UsuarioBase{
    public Docente(){
        super();
    }
    public Docente(String nick, String nombre, String apellido, String correo, Date fNac, byte[] img){
        super(nick, nombre, apellido, correo, fNac,img);
    }
    
}
