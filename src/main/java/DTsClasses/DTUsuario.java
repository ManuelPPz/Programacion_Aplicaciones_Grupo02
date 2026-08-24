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
public class DTUsuario extends DTUsuarioBase{
    public DTUsuario(String nickname,String nombre,String apellido,String correo,Date fNac, ImageIcon img){
        super(nickname,nombre,apellido,correo,fNac,img);
    }
}
