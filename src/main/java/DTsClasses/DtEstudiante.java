/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author leoli
 */
public class DtEstudiante extends DtUsuario {
    private List<String> ediciones; // Ediciones a las que está inscripto

    public DtEstudiante(String nickname, String nombre, String apellido, String correo, LocalDate fechaNacimiento, String imagenPath, List<String> ediciones) {
        super(nickname, nombre, apellido, correo, fechaNacimiento, imagenPath);
        this.ediciones = ediciones;
    }

    public List<String> getEdiciones() { return ediciones; }
}
