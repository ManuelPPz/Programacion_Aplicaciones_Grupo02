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
import javax.swing.ImageIcon;

/**
 *
 * @author mateo
 */
public interface IController {
    //Alta Usuario
    public abstract void AgregarUsuario(String nickname, String nombre, String apellido, String correo, Date fechaNac, boolean docente, List<String> institutos, String imgPath)throws Exception;
    //Consultar Usuario, la funcion deberia devolver el tipo de dato usuario
    //Se modificara al crear el tipo de dato usuario retornando el tipo de dato "Usuario"
    public abstract DTUsuarioBase ConsultarUsuario(String nickname);
    //Modificar Datos Usuario
    public abstract void ModificarUsuario(String nickname, String newNombre, String newApellido, String newCorreo,boolean docente, Date newFechaNac, List<String> institutos, String imgPath);
    //Alta Curso
    //La fecha del curso se toma dentro de la funcion
    //Preguntar al profe si los cursos previos son el tipo de dato o un int
    public abstract void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, List<String> previas, Date fechaIngreso)throws Exception;
    //Consulta Curso
    //Se modificara al crear el tipo de dato curso retornando el tipo de dato "Curso"
    public abstract DTMaster ConsultaCurso(String nomCurso);
    //Alta Edicion Curso
    public abstract void AltaEdicionCurso(String instituto, String nomCurso, String nomEdicion,Date fInicio, Date fFin,int cupo, List<String>docentes, Date fAlta);
    //Consulta Edicion Curso
    //Se modificara al crear el tipo de dato EdicionCurso retornando el tipo de dato "EdicionCurso"
    public abstract DTMaster ConsultaEdicionCurso(String nomEdicion);
    //Inscripcion a Edicion Curso
    public abstract void InscripcionAEdicionCurso(String nomCurso, String nickname);
    //Crear Programa de Formacion
    //El tipo de dato FechaType sera añadido cuando se cree el tipo de dato "FechaType" o alguno con nombre parecido
    public abstract void CrearProgramasDeFormacion(DTProgramaForm dt)throws Exception;
    //Agregar programa
    public abstract void AgregarCursoAProgramas(String nomPrograma, String nomCurso);
    //Consulta Programa de Formacion
    //Se modificara al crear el tipo de dato ProgramaFormacion retornando el tipo de dato "ProgramaFormacion"
    public abstract DTProgramaForm ConsultaProgramaFormacion(String nomPrograma);
    //Alta Instituto
    public abstract void AltaInstituto(String nomInstituto)throws Exception;
    
    
    //Otras Funciones
    
    //Verificar existencia de curso
    public abstract boolean VerificarCurso(String nombre);
    //Verificar existencia de instituto
    public abstract boolean VerificarInstituto(String instituto);
    //Verificar existencia de edicion de curso
    public abstract boolean VerificarEdicion(String nombre);
    //Devoolver lista completa de DTs
    public abstract List<DTMaster> ListarClase(EnumDT enumType);
    
    //Devolver lista de cursos x instituto
    public abstract List<DTMaster> ListarCursos(String nomInstituto);
    //Devolver lista de ediciones x curso
    public abstract List<DTMaster>ListarEdiciones(String nomCurso);
    //Devolver lista de docentes x instituto
    public abstract List<DTMaster>ListarDocentes(String nomInstituto);
    //verifica existencia de programa
    public abstract boolean ExistePrograma(String nombreProg);
    public abstract void ActualizarPrograma(String nombreProg, String descripcionProg, Date fInicio, Date fFin);
    
    
}
