/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import bdSQL.ConexionBD;
import java.sql.Connection;
import DTsClasses.Vigencia;
import javax.swing.ImageIcon;
//Imports Manejadores
import Manejadores.*;
//Imports Clases
import Classes.UsuarioBase;
import Classes.Curso;
import Classes.Instituto;
import Classes.ProgramaFormacion;
//Imports DTs
import DTsClasses.DTCurso;
import DTsClasses.DTUsuarioBase;
import DTsClasses.DTInstituto;
import DTsClasses.DTMaster;
import DTsClasses.EnumDT;
import DTsClasses.DTProgramaForm;

/**
 *
 * @author mateo
 */
public class Controller implements IController{
    ManejadorUsuario manUsuario;
    ManejadorCursos manCursos;
    ManejadorInstituto manInstituto;
    public Controller(){
        manUsuario = ManejadorUsuario.GetInstance();
        manCursos = ManejadorCursos.GetInstance();
        manInstituto = ManejadorInstituto.GetInstance();
    }
    //Alta Usuario
    @Override
    public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, Date fechaNac, boolean docente, List<String> institutos, String imgPath){
        UsuarioBase auxUsuario = null;
        List<Instituto> auxInstituto = new ArrayList();
        if(institutos!=null){
            for(int i = 0;i<institutos.size();i++){
                auxInstituto.add(manInstituto.BuscarInstituto(institutos.get(i)));
            }
        }
        
        try {
            auxUsuario = manUsuario.CrearUsuario(nickname, nombre, apellido, correo, docente, fechaNac, auxInstituto, imgPath);
        } catch (IOException ex) {
            System.out.print("No se puedo ingresar el usuario");
        }
        manUsuario.Add(auxUsuario);
    }
    //ConsultaUsuario
    @Override
    public DTUsuarioBase ConsultarUsuario(String nickname){
        UsuarioBase auxUsuario = manUsuario.BuscarUsuario(nickname);
        if(auxUsuario!=null){
            DTUsuarioBase auxDT = manUsuario.getDT(auxUsuario);
            return auxDT;
        }
        return null;
    }
    
    //Modificar Datos Usuario
    @Override
    public void ModificarUsuario(String nickname, String newNombre, String newApellido, String newCorreo,boolean docente, Date newFechaNac, List<String> institutos, String imgPath){
        
        List<Instituto> auxInstituto = new ArrayList();
        if(institutos!=null){
            for(int i = 0;i<institutos.size();i++){
                auxInstituto.add(manInstituto.BuscarInstituto(institutos.get(i)));
            }
        }
        
        
        try {
            manUsuario.ModificarDatosUsuario(nickname, newNombre, newApellido, newCorreo, true, newFechaNac, auxInstituto, imgPath);
        } catch (IOException ex) {
            System.out.print("No se puedo modificar los datos");
        }
    }
    //Alta Curso
    @Override
    public void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, List<String> previas, Date fechaIngreso){
        Curso auxC = manCursos.BuscarCurso(nombre,nomInstituto);
        if(auxC==null){
            Instituto ins = new Instituto();
            ins.setNombre(nomInstituto);
            Curso c = manCursos.CrearCurso(ins, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, fechaIngreso,previas);
            manCursos.Add(c);
            System.out.println("alta curso c: "+c.getDescripcion());
            System.out.println("alta curso desc: "+descripcion);
        }else{
            manCursos.ModificarCurso(auxC, descripcion, duracion, cantHoras, cantCreditos, URL, fechaIngreso,previas);
        }
        
    }
    //Consulta Curso
    @Override
    public DTMaster ConsultaCurso(String nomCurso,String ins){
        
        Curso c = manCursos.BuscarCurso(nomCurso,ins);
        if(c!=null){
            DTCurso auxDT = manCursos.getDT(c);
            return auxDT;
        }
        return null;        
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
    public void CrearProgramasDeFormacion(DTProgramaForm dt) throws Exception{
       //Convertir el DT en una entidad de jpa
        ProgramaFormacion pf = new ProgramaFormacion();
        pf.setNombre(dt.getNombre());
        pf.setDescripcion(dt.getDescripcion());
        pf.setVigenciaPrograma(dt.getVigenciaProg());
        
        //guardar a traves del manejador
        ManejadorProgramaForm.getInstance().agregarPrograma(pf);
    }
    //Agregar programa
    @Override
    public void AgregarCursoAProgramas(String nomPrograma, String nomCurso){
        
    }
    //Consulta Programa de Formacion
    //Se modificara al crear el tipo de dato ProgramaFormacion retornando el tipo de dato "ProgramaFormacion"
    @Override
    public DTProgramaForm ConsultaProgramaFormacion(String nomPrograma){
        DTProgramaForm d = new DTProgramaForm("h", "o", null, null);
    // Implementación real futura:
    /*
    ManejadorProgramaFormacion mp = ManejadorProgramaFormacion.getInstance();
    ProgramaFormacion p = mp.buscarPrograma(nomPrograma);
    
    List<DTCurso> listDTCursos = new ArrayList<>();
    for (Curso c : p.getListaCursos()) {
        listDTCursos.add(new DTCurso(c.getInstituto().getNombre(), c.getNombre(), ...));
    }
    
    DTProgramaForm auxDTProg = new DTProgramaForm(p.getNombre(), p.getDescripcion(), p.getVigencia(), listDTCursos);
    return auxDTProg;
    */
    
    return d;
    }
    
    //MOMENTANEO PONER EN TRUE PARA PROBAR Y DEJAR EN FALSE HASTA QUE SE AGREGE METODO
    @Override
    public boolean ExistePrograma(String nombreProg){
        //Consulta cuantos Programas tienen ese nombre
        String sql = "SELECT COUNT(*) FROM ProgramaFormacion WHERE nombre = ?";
        //Abre y cierra la coneccion automaticamente
        try (Connection con = bdSQL.ConexionBD.getConexion(); java.sql.PreparedStatement ps = con.prepareStatement(sql)){
           //asigna el nombre a '?'
            ps.setString(1, nombreProg);
        //ejecuta la consulta y lee el resultado numerico
        try(java.sql.ResultSet rs = ps.executeQuery()){
           if(rs.next()){
           //devuelve true si es igual
           return rs.getInt(1) > 0;
                   }
           
        }
        
      } catch(java.sql.SQLException e){
          //imprime mensaje de error si algo falla
          System.err.println("Error al validar existencia: " + e.getMessage());
      }
    return false; //si no existe el nombre,,, cambiar a true para probar la otra ventana de crear programa >:)
    }
    @Override
    //Se usa para actualizar un programa existente 
    public void ActualizarPrograma(String nombreProg, String descripcionProg, Date fInicio, Date fFin){
    
    }
    
    //Alta Instituto
    @Override
    public void AltaInstituto(String nomInstituto){
        Instituto i = manInstituto.CreaInstituto(nomInstituto);
        manInstituto.Add(i);
    }
    
    
    //Otras Funciones
    //Verificar si existe curso con el nombre
    @Override
    public boolean VerificarCurso(String nombre,String instituto){
        Curso c = manCursos.BuscarCurso(nombre,instituto);
        return c!=null;
    }
    public boolean VerificarInstituto(String instituto){
        Instituto i = manInstituto.BuscarInstituto(instituto);
        return i!=null;
    }
    
    //Devoolver lista completa de DTs
    //Lista que no requiere de ninguna condicion
    @Override
    public List<DTMaster> ListarClase(EnumDT enumType){
        List<DTMaster> listReturn = new ArrayList();
        if(enumType==EnumDT.DT_INSTITUTO){
            listReturn = manInstituto.getDTList();
        }else if(enumType==EnumDT.DT_CURSO){
            listReturn = manCursos.getDTList();
        }else if(enumType==EnumDT.DT_USUARIO){
            listReturn = manUsuario.getDTList();
        }
        
        return listReturn;
    }
    
    @Override
    public List<DTMaster> ListarCursos(String nomInstituto){
            List<DTMaster> listReturn = manCursos.getDTLIst(nomInstituto);
            return listReturn;
    }
    
}
