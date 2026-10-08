/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author mateo
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UsuarioBase")
public class UsuarioBase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="Nickname", unique=true, nullable=false)
    private String nickname;
    @Column(name="Nombre")
    private String nombre;
    @Column(name="Apellido")
    private String apellido;
    //Falta hacerlo que sea unico
    @Column(name="Correo", nullable=false, unique=true)
    private String correo;
    @Column(name="contrasenia")
    private String contrasenia;
    @Column(name="fNac")
    private Date fNac;
    @Column(name="ImagePath", columnDefinition = "BLOB")
    private String image;
    
    @ManyToMany
    @JoinTable(
        name = "Seguidos", 
        joinColumns = @JoinColumn(name="MyNickname"), 
        inverseJoinColumns = @JoinColumn(name="NicknameOtherUser")
    )
    private List<UsuarioBase> seguidos = new ArrayList<>();

    public String getNickname() {return nickname;}
    public String getNombre(){return this.nombre;}
    public String getApellido(){return this.apellido;}
    public String getCorreo(){return this.correo;}
    public Date getFNac(){return this.fNac;}
    public String getImage(){return this.image;}
    public String getPassword(){return this.contrasenia;}
    
    public UsuarioBase(){}
    public UsuarioBase(String nick, String nom, String apellido, String correo,String contrasenia, Date fNac,String img){
        this.nickname = nick;
        this.nombre = nom;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.fNac = fNac;
        this.image = img;
    }

    public void ModificarMisDatos(String nom, String apellido, String password, Date fNac,String img){
        this.nombre = nom;
        this.apellido = apellido;
        this.contrasenia = password;
        this.fNac = fNac;
        this.image = img;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nickname != null ? nickname.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioBase)) {
            return false;
        }
        UsuarioBase other = (UsuarioBase) object;
        if ((this.nickname == null && other.nickname != null) || (this.nickname != null && !this.nickname.equals(other.nickname))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.UsuarioBase[ id=" + nickname + " ]";
    }
    
    public void SeguirUsuario(UsuarioBase ub){
        seguidos.add(ub);
    }
    public void DejarDeSeguir(UsuarioBase ub){
        seguidos.remove(ub);
    }
    public List<UsuarioBase> getMisSeguidos(){
        return this.seguidos;
    }
    
}