/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Logica;
import java.time.LocalDate;
/**
 *
 * @author mateo
 */
public interface IController {
    //Alta Usuario
    public abstract void AgregarUsuario(String nickname, String nombre, String apellido, String correo, LocalDate fechaNac, boolean docente, String instituto);
    //Consultar Usuario, la funcion deberia devolver el tipo de dato usuario
    //Se modificara al crear el tipo de dato usuario retornando el tipo de dato "Usuario"
    public abstract void ConsultarUsuario(String nickname);
    //Modificar Datos Usuario
    public abstract void ModificarUsuario(String nickname, String newNombre, String newApellido, String newCorreo, LocalDate newFechaNac);
    //Alta Curso
    //La fecha del curso se toma dentro de la funcion
    //Preguntar al profe si los cursos previos son el tipo de dato o un int
    public abstract void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, int cantPrevias);
    //Consulta Curso
    //Se modificara al crear el tipo de dato curso retornando el tipo de dato "Curso"
    public abstract void ConsultaCurso(String nomCurso);
    //Alta Edicion Curso
    //La coleccion de docentes sera añadida cuando se cree el tipo de dato "Docente"
    //El tipo de dato FechaType sera añadido cuando se cree el tipo de dato "FechaType" o alguno con nombre parecido
    public abstract void AltaEdicionCurso(String nomCurso, String nomEdicion/*, FechaType (dia incio, dia final)*/,int cupo/*Collection<Usuario> docentes*/);
    //Consulta Edicion Curso
    //Se modificara al crear el tipo de dato EdicionCurso retornando el tipo de dato "EdicionCurso"
    public abstract void ConsultaEdicionCurso(String nomEdicion);
    //Inscripcion a Edicion Curso
    public abstract void InscripcionAEdicionCurso(String nomCurso, String nickname);
    //Crear Programa de Formacion
    //El tipo de dato FechaType sera añadido cuando se cree el tipo de dato "FechaType" o alguno con nombre parecido
    public abstract void CrearProgramasDeFormacion(String nomPrograma, String descripcion/*, FechaType (dia incio, dia final)*/);
    //Agregar programa
    public abstract void AgregarCursoAProgramas(String nomPrograma, String nomCurso);
    //Consulta Programa de Formacion
    //Se modificara al crear el tipo de dato ProgramaFormacion retornando el tipo de dato "ProgramaFormacion"
    public abstract void ConsultaProgramaFormacion(String nomPrograma);
    //Alta Instituto
    public abstract void AltaInstituto(String nomInstituto);
}
