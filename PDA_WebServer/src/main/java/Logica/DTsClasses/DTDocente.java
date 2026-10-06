/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.DTsClasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author mateo
 */
public class DTDocente extends DTUsuarioBase{
    String instituto;
    List<String> cursos = new ArrayList();
    public DTDocente(String nickname,String nombre,String apellido,String correo, String password,Date fNac, String instituto, ImageIcon img, List<String> cursos, List<String> ediciones, List<String> programas){
        super(nickname,nombre,apellido,correo, password,fNac,img, ediciones, programas);
        this.instituto = instituto;
        this.cursos = cursos;
    }
    public String getInstituto(){
        return this.instituto;
    }
    public List<String> getCursos(){
        return this.cursos;
    }
}
