/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DTDocente extends DTUsuarioBase{

    /** Constructor sin argumentos requerido por JAXB (Web Services). */
    protected DTDocente() {
    }

    String instituto;
    List<String> cursos = new ArrayList();
    public DTDocente(String nickname,String nombre,String apellido,String correo, String password,Date fNac, String instituto, String img, List<String> cursos, List<String> ediciones, List<String> programas){
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
