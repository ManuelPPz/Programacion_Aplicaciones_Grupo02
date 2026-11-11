/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.DTsClasses;

/**
 *
 * @author mateo
 */
public class DTValoracionCurso extends DTMaster{
    private String usuario;
    private String curso;
    private float valoracion;

    public String getUsuario() {
        return usuario;
    }

    public String getCurso() {
        return curso;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }
    
    public DTValoracionCurso(){}
    
    public DTValoracionCurso(String usuario, String curso, float valoracion) {
        this.usuario = usuario;
        this.curso = curso;
        this.valoracion = valoracion;
    }

    
}
