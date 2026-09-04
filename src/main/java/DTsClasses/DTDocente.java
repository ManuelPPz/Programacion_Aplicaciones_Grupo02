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
public class DTDocente extends DTUsuarioBase{
    List<String> institutos = new ArrayList();
    List<String> cursos = new ArrayList();
    public DTDocente(String nickname,String nombre,String apellido,String correo,Date fNac, List<String> institutos, ImageIcon img, List<String> cursos, List<String> ediciones, List<String> programas){
        super(nickname,nombre,apellido,correo,fNac,img, ediciones, programas);
        this.institutos = institutos;
        this.cursos = cursos;
    }
    public List<String> getInstitutos(){
        return this.institutos;
    }
    public List<String> getCursos(){
        return this.cursos;
    }
}
