/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;
import java.time.LocalDate;
/**
 *
 * @author leoli
 */
public abstract class Usuario {
    protected String nick;
    protected String nombre;
    protected String apellido;
    protected String mail;
    protected LocalDate fecha_nacimiento;
    

    public Usuario(String nick, String nombre, String mail, LocalDate fecha_nacimiento) {
        this.nick = nick;
        this.nombre = nombre;
        this.mail = mail;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    // En Java no se declara destructor (el Garbage Collector gestiona la memoria)

    public String getNick() {
        return nick;
    }

    public String getNombre() {
        return nombre;
    }
     public String getApellido() {
        return apellido;
    }
     
    public String getMail() {
        return mail;
    }

    public LocalDate getLocalDate() {
        return fecha_nacimiento;
    }
}

