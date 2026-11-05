/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.DTsClasses;

import jakarta.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author mateo
 */
@XmlSeeAlso({
    DTDocente.class,
    DTUsuario.class
})
public class DTUsuarioBase extends DTMaster{
    String nickname;
    String nombre;
    String apellido;
    String correo;
    String password;
    Date fNac;
    String imgPath;
    List<String> ediciones; 
    List<String> programas;
    List<String> seguidos;
    public DTUsuarioBase(){
        super();
    }
    public DTUsuarioBase(String nickname,String nombre,String apellido,String correo, String password,Date fNac, String img, List<String> ediciones, List<String> programas, List<String> seguidos){
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.fNac = fNac;
        this.imgPath = img;
        this.ediciones = ediciones;
        this.programas = programas;
        this.seguidos=seguidos;
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
    public List<String> getSeguidos(){return this.seguidos;}

    public Date getfNac() {
        return fNac;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setfNac(Date fNac) {
        this.fNac = fNac;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setEdiciones(List<String> ediciones) {
        this.ediciones = ediciones;
    }

    public void setProgramas(List<String> programas) {
        this.programas = programas;
    }
    
    public void setSeguidor(List<String> seguidos){
        this.seguidos=seguidos;
    }
    
    
}
