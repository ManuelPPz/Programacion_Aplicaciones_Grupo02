/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DTUsuarioBase extends DTMaster{

    /** Constructor sin argumentos requerido por JAXB (Web Services). */
    protected DTUsuarioBase() {
    }

    String nickname;
    String nombre;
    String apellido;
    String correo;
    @XmlTransient // la contraseña no viaja por el Web Service
    String password;
    Date fNac;
    String imgPath;
    List<String> ediciones; 
    List<String> programas;
    
    public DTUsuarioBase(String nickname,String nombre,String apellido,String correo, String password,Date fNac, String img, List<String> ediciones, List<String> programas){
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.fNac = fNac;
        this.imgPath = img;
        this.ediciones = ediciones;
        this.programas = programas;
    }
    public String getNickname(){return nickname;}
    public String getNombre(){return nombre;}
    public String getApellido(){return apellido;}
    public String getCorreo(){return correo;}
    public Date getFNac(){return fNac;}
    public String getImg(){return imgPath;}
    public List<String> getEdiciones(){return this.ediciones;}
    public List<String> getProgramas(){return this.programas;}
    public String getPassword(){return this.password;}
}
