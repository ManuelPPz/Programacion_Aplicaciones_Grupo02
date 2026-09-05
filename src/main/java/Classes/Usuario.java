/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mateo
 */
@Entity
public class Usuario extends UsuarioBase {

    @OneToMany(mappedBy = "id.miUsuario")
    private List<Edi_Usu> misInscripciones = new ArrayList<>();

    public Usuario() {
        super();
    }

    public Usuario(String nick, String nombre, String apellido, String correo, Date fNac, byte[] img) {
        super(nick, nombre, apellido, correo, fNac, img);
    }

    @Override
    public void ModificarMisDatos(String nom, String apellido, String correo, Date fNac, byte[] img) {
        super.ModificarMisDatos(nom, apellido, correo, fNac, img);
    }

    public void AddEdicionCurso(Edi_Usu ec) {
        if (misInscripciones == null) {
            misInscripciones = new ArrayList<>();
        }
        misInscripciones.add(ec);
    }

    public List<Edi_Usu> getMisInscripciones() {
        return this.misInscripciones;
    }

    public List<Edi_Usu> getMisEdiciones() {
        return this.misInscripciones;
    }
}