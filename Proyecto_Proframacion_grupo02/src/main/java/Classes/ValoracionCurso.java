/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import DTsClasses.DTMaster;
import DTsClasses.DTValoracionCurso;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "ValoracionCurso")
public class ValoracionCurso implements Serializable {

    @EmbeddedId
    private Id_Valoracion id;

    @ManyToOne
    @MapsId("miUsuarioNickname") // Se vincula con el campo miUsuarioNickname en Id_Valoracion
    @JoinColumn(name = "usuario_nickname")
    private Usuario usuario;
    
    @ManyToOne
    @MapsId("miCursoNombre") // Se vincula con el campo miEdicionNombre en Id_Valoracion
    @JoinColumn(name = "curso_nombre")
    private Curso curso;
    
    private float valoracion;
    
    
    public Id_Valoracion getId() {
        return id;
    }
    public Usuario getUsuario(){
        return this.usuario;
    }
    public Curso getCurso(){
        return this.curso;
    }
    public float getValoracion(){
        return this.valoracion;
    }
    
    public void setId(Id_Valoracion id) {
        this.id = id;
    }
    public void setValoracion(float val){
        this.valoracion=val;
    }
    
    public ValoracionCurso(){}
    
    public ValoracionCurso(Usuario u, Curso c, float valoracion){
        this.valoracion=valoracion;
        this.usuario=u;
        this.curso=c;
        this.id=new Id_Valoracion(u.getNickname(),c.getNombre());
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoracionCurso)) {
            return false;
        }
        ValoracionCurso other = (ValoracionCurso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classes.ValoracionCurso[ id=" + id + " ]";
    }
    
    public DTMaster getMyDT() {
        return new DTValoracionCurso(usuario.getNickname(), curso.getNombre(),valoracion);
    }
}
