/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
/**
 *
 * @author mateo
 */
public class DTUsuario extends DTUsuarioBase{
    public DTUsuario(String nickname,String nombre,String apellido,String correo,Date fNac, ImageIcon img, List<String> ediciones, List<String> programas){
        super(nickname,nombre,apellido,correo,fNac,img, ediciones, programas);
    }
}
