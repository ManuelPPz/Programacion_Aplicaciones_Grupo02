/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
public class DTDocente extends DTUsuarioBase{
    List<String> institutos = new ArrayList();
    
    public DTDocente(String nickname,String nombre,String apellido,String correo,Date fNac, List<String> institutos){
        super(nickname,nombre,apellido,correo,fNac);
        this.institutos = institutos;
    }
}
