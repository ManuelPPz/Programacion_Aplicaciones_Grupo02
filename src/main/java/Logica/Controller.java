/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;
import java.time.LocalDate;
/**
 *
 * @author mateo
 */
public class Controller implements IController{
    public Controller(){
    }
    //Alta Usuario
    public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, LocalDate fechaNac, boolean docente, String instituto){
    }   //Consultar Usuario, la funcion deberia devolver el tipo de dato usuario
    //Se modificara al crear el tipo de dato usuario retornando el tipo de dato "Usuario"
    public void ConsultarUsuario(String nickname){
        
    }
    //Modificar Datos Usuario
    public void ModificarUsuario(String nickname, String newNombre, String newApellido, String newCorreo, LocalDate newFechaNac){
        
    }
    //Alta Curso
    //La fecha del curso se toma dentro de la funcion
    //Preguntar al profe si los cursos previos son el tipo de dato o un int
    public void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, int cantPrevias){
        
    }
    //Consulta Curso
    //Se modificara al crear el tipo de dato curso retornando el tipo de dato "Curso"
    public void ConsultaCurso(String nomCurso){
        
    }
    //Alta Edicion Curso
    //La coleccion de docentes sera añadida cuando se cree el tipo de dato "Docente"
    //El tipo de dato FechaType sera añadido cuando se cree el tipo de dato "FechaType" o alguno con nombre parecido
    public void AltaEdicionCurso(String nomCurso, String nomEdicion/*, FechaType (dia incio, dia final)*/,int cupo/*Collection<Usuario> docentes*/){
        
    }
    //Consulta Edicion Curso
    //Se modificara al crear el tipo de dato EdicionCurso retornando el tipo de dato "EdicionCurso"
    public void ConsultaEdicionCurso(String nomEdicion){
        
    }
    //Inscripcion a Edicion Curso
    public void InscripcionAEdicionCurso(String nomCurso, String nickname){
        
    }
    //Crear Programa de Formacion
    //El tipo de dato FechaType sera añadido cuando se cree el tipo de dato "FechaType" o alguno con nombre parecido
    public void CrearProgramasDeFormacion(String nomPrograma, String descripcion/*, FechaType (dia incio, dia final)*/){
        
    }
    //Agregar programa
    public void AgregarCursoAProgramas(String nomPrograma, String nomCurso){
        
    }
    //Consulta Programa de Formacion
    //Se modificara al crear el tipo de dato ProgramaFormacion retornando el tipo de dato "ProgramaFormacion"
    public void ConsultaProgramaFormacion(String nomPrograma){
        
    }
    //Alta Instituto
    public void AltaInstituto(String nomInstituto){
        
    }
    
    
}
