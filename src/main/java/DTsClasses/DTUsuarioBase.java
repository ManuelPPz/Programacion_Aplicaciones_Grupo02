/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;

import java.util.Date;

/**
 *
 * @author mateo
 */
public class DTUsuarioBase {
    String nickname;
    String nombre;
    String apellido;
    String correo;
    Date fNac;
    
    public DTUsuarioBase(String nickname,String nombre,String apellido,String correo,Date fNac){
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.fNac = fNac;
    }
}
