/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;
import DTsClasses.DTCurso;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 *
 * @author mateo
 */
public class Controller implements IController{
    public Controller(){
    }
    //Alta Usuario
    @Override
    public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, Date fechaNac, boolean docente, String instituto){
    }   //Consultar Usuario, la funcion deberia devolver el tipo de dato usuario
    //Se modificara al crear el tipo de dato usuario retornando el tipo de dato "Usuario"
    @Override
    public void ConsultarUsuario(String nickname){
        
    }
    //Modificar Datos Usuario
    @Override
    public void ModificarUsuario(String nickname, String newNombre, String newApellido, String newCorreo, Date newFechaNac){
        
    }
    //Alta Curso
    @Override
    public void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, List<String> previas, Date fechaIngreso){
        /*
        Posible solucion
        Curso c = new Curso(nomInstituto,nombre,descripcion,duracion,cantHoras,cantCreditos, URL,previas,fechaIngreso);
        ManejadorCursos.Add(c);
        */
    }
    //Consulta Curso
    //Se modificara al crear el tipo de dato curso retornando el tipo de dato "Curso"
    @Override
    public DTCurso ConsultaCurso(String nomCurso){
        List<String> auxListPrev = new ArrayList();
        auxListPrev.add("COE");
        auxListPrev.add("ADI");
        
        List<String> auxListEdi = new ArrayList();
        auxListEdi.add("Edi. 2026");
        auxListEdi.add("Edi. 2027");
        
        List<String> auxListProg = new ArrayList();
        auxListProg.add("Prog. Ado.");
        
        
        Date auxFecha;
        try{
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            auxFecha = formato.parse("18/08/2021");
        }catch(ParseException e){
            auxFecha = new Date();
        }
        DTCurso auxDTCurso = new DTCurso("CURE",nomCurso,"Es un curso",1,3,15,"ev.Utec.com",auxFecha,auxListPrev,auxListEdi,auxListProg);
        //Falta implementar buscar un curso
        /*Posible solucion:
        Curso c = ManejadorCurso.BuscarCurso(nomCurso);
        DTCurso auxDTCurso = new DTCurso(c.miInstituto.nombre,c.nombre,c.descripcion,c.duracion,c.cantHoras,c.cantCreditos,c.URL,c.fechaAlta,c.listPrevias,c.listEdiciones,c.listProgramas);
        return auxDTCurso;
        */
        return auxDTCurso;
    }
    //Alta Edicion Curso
    //La coleccion de docentes sera añadida cuando se cree el tipo de dato "Docente"
    //El tipo de dato FechaType sera añadido cuando se cree el tipo de dato "FechaType" o alguno con nombre parecido
    @Override
    public void AltaEdicionCurso(String nomCurso, String nomEdicion/*, FechaType (dia incio, dia final)*/,int cupo/*Collection<Usuario> docentes*/){
        
    }
    //Consulta Edicion Curso
    //Se modificara al crear el tipo de dato EdicionCurso retornando el tipo de dato "EdicionCurso"
    @Override
    public void ConsultaEdicionCurso(String nomEdicion){
        
    }
    //Inscripcion a Edicion Curso
    @Override
    public void InscripcionAEdicionCurso(String nomCurso, String nickname){
        
    }
    //Crear Programa de Formacion
    //El tipo de dato FechaType sera añadido cuando se cree el tipo de dato "FechaType" o alguno con nombre parecido
    @Override
    public void CrearProgramasDeFormacion(String nomPrograma, String descripcion/*, FechaType (dia incio, dia final)*/){
        
    }
    //Agregar programa
    @Override
    public void AgregarCursoAProgramas(String nomPrograma, String nomCurso){
        
    }
    //Consulta Programa de Formacion
    //Se modificara al crear el tipo de dato ProgramaFormacion retornando el tipo de dato "ProgramaFormacion"
    @Override
    public void ConsultaProgramaFormacion(String nomPrograma){
        
    }
    
    //MOMENTANEO PONER EN TRUE PARA PROBAR Y DEJAR EN FALSE HASTA QUE SE AGREGE METODO
    @Override
    public boolean ExistePrograma(String nombreProg){
    return true;
    }
    @Override
    //Se usa para actualizar un programa existente 
    public void ActualizarPrograma(String nombreProg, String descripcionProg, Date fInicio, Date fFin){
    
    }
    
    //Alta Instituto
    @Override
    public void AltaInstituto(String nomInstituto){
        
    }
    
    
    //Otras Funciones
    //Verificar si existe curso con el nombre
    @Override
    public boolean VerificarCurso(String nombre){
        //Falta realizar
        /*Posible solucion
        Curso c = ManjeadorCursos.BuscarCurso(nombre);
        return c!=null;
        */
        return nombre.equals("PA");
    }
    
    //Devoolver lista de insitutos
    @Override
    public List<String> ListarInstitutos(){
        //Falta realizar
        /*Posible solucion:
        List<String> listReturn = ManejadorInstituto.GetNameMyCursos();
        */
        
        List<String> listReturn = new ArrayList();
        listReturn.add("CURE");
        listReturn.add("UDELAR");
        listReturn.add("UM");
        listReturn.add("ORT");
        return listReturn;
    }
    //Dovelver lista de cursos
    @Override
    public List<String> ListarCursos(){
        //Falta realizar
        /*Posible solucion:
        List<String> listReturn = ManjeadorCurso.GetNameMyCursos();
        */
        List<String> listReturn = new ArrayList();
        listReturn.add("Prog. Avanzada");
        listReturn.add("Prog. Aplicaciones");
        listReturn.add("COE");
        listReturn.add("ADI");
        return listReturn;
    }
    @Override
    public List<String> ListarCursos(String nomInstituto){
        //Falta Realizar
        /*Posible solucion:
        List<String> listReturn = ManjeadorCurso.GetNameMyCursos(nomInstituto);
        */
        List<String> listReturn = new ArrayList();
        listReturn.add("Prog. Aplicaciones");
        listReturn.add("ADI");
        return listReturn;
    }
    
}
