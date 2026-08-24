/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;

import java.util.Date;
import javax.swing.ImageIcon;

/**
 *
 * @author mateo
 */
public class DTUsuarioBase extends DTMaster{
    String nickname;
    String nombre;
    String apellido;
    String correo;
    Date fNac;
    ImageIcon img;
    
    public DTUsuarioBase(String nickname,String nombre,String apellido,String correo,Date fNac, ImageIcon img){
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.fNac = fNac;
        this.img = img;
    }
    public String getNickname(){return nickname;}
    public String getNombre(){return nombre;}
    public String getApellido(){return apellido;}
    public String getCorreo(){return correo;}
    public Date getFNac(){return fNac;}
    public ImageIcon getImg(){return img;}
}
