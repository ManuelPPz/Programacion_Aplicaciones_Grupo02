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
public class Usuario extends UsuarioBase{
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
}
