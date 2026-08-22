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
public class DtDocente extends DtUsuario {
    private String nombreInstituto;
    private List<String> cursos;

    public DtDocente(String nickname, String nombre, String apellido, String correo, LocalDate fechaNacimiento, String imagenPath, String nombreInstituto, List<String> cursos) {
        super(nickname, nombre, apellido, correo, fechaNacimiento, imagenPath); 
        this.nombreInstituto = nombreInstituto;
        this.cursos = cursos;
    }

    public String getNombreInstituto() { return nombreInstituto; }
    public List<String> getCursos() { return cursos; }
}