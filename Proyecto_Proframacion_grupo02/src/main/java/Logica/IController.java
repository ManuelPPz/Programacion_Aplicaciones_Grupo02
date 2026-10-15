/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Logica;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import DTsClasses.DTCurso;
import DTsClasses.DTInstituto;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import DTsClasses.Vigencia;
import javax.swing.ImageIcon;

/**
 *
 * @author mateo
 */
public interface IController {
    //Alta Usuario
    public abstract void AgregarUsuario(String nickname, String nombre, String apellido, String correo, String password, Date fechaNac, boolean docente, String instituto, String imgPath)throws Exception;
    
    //Consultar Usuario, la funcion deberia devolver el tipo de dato usuario
    //Se modificara al crear el tipo de dato usuario retornando el tipo de dato "Usuario"
    public abstract DTUsuarioBase ConsultarUsuario(String nickname);
    
    //Modificar Datos Usuario
    public abstract void ModificarUsuario(String nickname, String newNombre, String newApellido, String newPassword,boolean docente, Date newFechaNac, String instituto, String imgPath);
    
    //Seguir a un usuario
    public abstract void SeguirUsuario(String nickname1, String nickname2);
    //Dejar de Seguir a usuario
    public abstract void DejarDeSeguir(String nickname1, String nickname2);
    
    //Alta Curso
    public abstract void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, List<String> previas, Date fechaIngreso, String docente, List<String> categorias) throws Exception;
    
    //Consulta Curso
    //Se modificara al crear el tipo de dato curso retornando el tipo de dato "Curso"
    public abstract DTMaster ConsultaCurso(String nomCurso);
    
    //Alta Edicion Curso
    public abstract void AltaEdicionCurso(String instituto, String nomCurso, String nomEdicion,Date fInicio, Date fFin,int cupo, List<String>docentes, Date fAlta)throws Exception;
    
    //Consulta Edicion Curso
    //Se modificara al crear el tipo de dato EdicionCurso retornando el tipo de dato "EdicionCurso"
    public abstract DTMaster ConsultaEdicionCurso(String nomEdicion);
    
    //Inscripcion a Edicion Curso
    public abstract void InscripcionAEdicionCurso(String nomCurso, String nickname, Date fIns);
    
    //
    public abstract void AceptarEstudiantesAEdicionCurso(List<Object[]> estudiantesYCambios, String nomEdicion);
    //Crear Programa de Formacion
    //El tipo de dato FechaType sera añadido cuando se cree el tipo de dato "FechaType" o alguno con nombre parecido
    public abstract void CrearProgramasDeFormacion(String nomPrograma, String descripcion, Date fInicio, Date fFin,Date fAlta)throws Exception;
    
    //Agregar programa
    public abstract void AgregarCursoAProgramas(String nomPrograma, List<String> cursos);
    
    //Consulta Programa de Formacion
    //Se modificara al crear el tipo de dato ProgramaFormacion retornando el tipo de dato "ProgramaFormacion"
    public abstract DTMaster ConsultaProgramaFormacion(String nomPrograma);
    
    //Inscripcion de usuario a programa de formacion
    public abstract void InscripcionUsuarioAProgramas(String nomPrograma, String nickname, Date fIns);
    
    public abstract boolean ExistePrograma(String nombreProg);
    public abstract void ActualizarPrograma(String nombreProg, String descripcionProg, Date fInicio, Date fFin);
    
    //Alta Instituto
    public abstract void AltaInstituto(String nomInstituto)throws Exception;
    
    
    //Otras Funciones
    
    //Verificar existencia de curso
    public abstract boolean VerificarCurso(String nombre);
    //Verificar existencia de instituto
    public abstract boolean VerificarInstituto(String instituto);
    //Verificar existencia de edicion de curso
    public abstract boolean VerificarEdicion(String nombre);
    //Verificar existencia de programas de formacion
    public abstract boolean VerificarPrograma(String nombre);
    //Verificar existencia de categoria
    public abstract boolean VerificarCategoria(String nombre);
    //Verificar correo repetido
    public abstract boolean VerificarCorreo(String correo);
    //Verificar Nickname repetido
    public abstract boolean VerificarNickname(String nickname);
    //Consulta usuario avanzada
    public abstract DTMaster ConsultaUsuarioAvanzada(String id, String password);//id puede ser el nickname o correo, la funcion compruba por ambas posibilidades

    
    //Devoolver lista completa de DTs
    public abstract List<DTMaster> ListarClase(EnumDT enumType);
    
    //Devolver lista de cursos x instituto
    public abstract List<DTMaster> ListarCursos(String nomInstituto);
    //Devolver lista de ediciones x curso
    public abstract List<DTMaster>ListarEdiciones(String nomCurso);
    //Devolver lista de ediciones por prioridad para docente
    public abstract List<DTMaster>ListarEdicionesPrioritarias(String nomEdicion);
    //Devolver lista de docentes x instituto
    public abstract List<DTMaster>ListarDocentes(String nomInstituto);
    //Devolver lista de inscripciones a ediciones de curso de un usuario
    public abstract List<DTMaster> ListarInscripciones(String nicknameEstudiante);
    
    public abstract List<DTMaster>ListarProgramaDeForm();
    public abstract String GenerateRandPassword();
    
    public abstract void AltaCategoria(String nombre);
}