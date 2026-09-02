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
import Classes.Docente;
import Classes.Instituto;
import Classes.ProgramaFormacion;
import Classes.EdicionCurso;
import Classes.Usuario;
import Classes.ProgramaDeFormacion;
//Imports DTs
import DTsClasses.DTCurso;
import DTsClasses.DTEdicionCurso;
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
    ManejadorEdicionCurso manEdicion;
    ManejadorProgramasDeFormacion manProgramas;
    
    public Controller(){
        manUsuario = ManejadorUsuario.GetInstance();
        manCursos = ManejadorCursos.GetInstance();
        manInstituto = ManejadorInstituto.GetInstance();
        manEdicion = ManejadorEdicionCurso.GetInstance();
        manProgramas = ManejadorProgramasDeFormacion.GetInstance();
    }
    
    //Alta Usuario
    @Override
    public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, Date fechaNac, boolean docente, List<String> institutos, String imgPath)throws Exception {
        UsuarioBase auxUsuario = null;
        List<Instituto> auxInstituto = new ArrayList<>();
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
        
        List<Instituto> auxInstituto = new ArrayList<>();
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
    public void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, List<String> previas, Date fechaIngreso, String docente) throws Exception {
        Curso auxC = manCursos.BuscarCurso(nombre);
        UsuarioBase auxUb = manUsuario.BuscarUsuario(docente);
        if(auxC==null){
            Instituto ins = new Instituto();
            ins.setNombre(nomInstituto);
            Curso c = manCursos.CrearCurso(ins, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, fechaIngreso,previas, auxUb);
            
            manCursos.Add(c);
        }else{
            manUsuario.RemoveCurso(auxC.getMiDocente(), auxC);
            manUsuario.AddCurso(auxUb, auxC);
            manCursos.ModificarCurso(auxC, descripcion, duracion, cantHoras, cantCreditos, URL, fechaIngreso,previas, auxUb);
        }
        
    }
    
    //Consulta Curso
    @Override
    public DTMaster ConsultaCurso(String nomCurso){
        
        Curso c = manCursos.BuscarCurso(nomCurso);
        if(c!=null){
            DTCurso auxDT = manCursos.getDT(c);
            return auxDT;
        }
        return null;        
    }
    
    //Alta Edicion Curso
    @Override
    public void AltaEdicionCurso(String instituto, String nomCurso, String nomEdicion, Date fInicio, Date fFin, int cupo, List<String> docentes, Date fAlta) throws Exception {
        Instituto ins = manInstituto.BuscarInstituto(instituto);
        Curso c = manCursos.BuscarCurso(nomCurso);
        EdicionCurso auxEc = manEdicion.BuscarEdicion(nomEdicion);
        
        if(auxEc==null){
            // 1. Crear la entidad Edición
            EdicionCurso ec = manEdicion.CrearEdicion(ins, c, nomEdicion, fInicio, fFin, cupo, fAlta);
            
            // 2. Guardar la edición en su manejador / BD
            manEdicion.Add(ec);
            
            // 3. Vincular en memoria la nueva Edición al Curso padre
            if (c != null) {
                if (c.getEdiciones() == null) {
                    // Inicialización en caso de que sea null
                }
                c.getEdiciones().add(ec);
            }
            
            // 4. Asociar docentes a la edición
            if (docentes != null) {
                for(int i = 0; i < docentes.size(); i++){
                    UsuarioBase ub = manUsuario.BuscarUsuario(docentes.get(i));
                    if (ub != null) {
                        manEdicion.AddUsuario(ec, ub);
                    }
                }
            }
        }else{
            List<UsuarioBase> auxList = auxEc.getMisUsuarios();
            List<UsuarioBase> newList = new ArrayList<>();
            boolean repetir = true;
            int index = 0;
            int indexDocente = 0;
            
            while(repetir && index < auxList.size()){
                UsuarioBase ub = auxList.get(index);
                if(ub instanceof Usuario){
                    newList.add(ub);
                }else{
                    if (indexDocente < docentes.size()) {
                        String auxStr = docentes.get(indexDocente);
                        UsuarioBase auxD = manUsuario.BuscarUsuario(auxStr);
                        if (auxD != null) newList.add(auxD);
                        indexDocente++;
                    }
                }
                index++;
            }
            manEdicion.ModificarDatos(nomEdicion, fInicio, fFin, cupo, fAlta, newList);
        }
        
    }
    
    //Consulta Edicion Curso
    @Override
    public DTMaster ConsultaEdicionCurso(String nomEdicion){
        EdicionCurso ec = manEdicion.BuscarEdicion(nomEdicion);
        if(ec!=null){
            DTEdicionCurso auxDT = manEdicion.getDT(ec);
            return auxDT;
        }
        return null; 
    }
    
    //Inscripcion a Edicion Curso
    @Override
    public void InscripcionAEdicionCurso(String nomCurso, String nickname){
        
    }
    
    //Crear Programa de Formacion (Versión por parámetros sueltos)
    @Override
    public void CrearProgramasDeFormacion(String nomPrograma, String descripcion, Date fInicio, Date fFin,Date fAlta)throws Exception{
        ProgramaDeFormacion auxProg = manProgramas.BuscarPrograma(nomPrograma);
        Vigencia v = new Vigencia(fInicio, fFin);
        if(auxProg==null){
            ProgramaDeFormacion pdf = manProgramas.CrearPrograma(nomPrograma, descripcion, v, fAlta);
            manProgramas.Add(pdf);
        }else{
            manProgramas.ModificarDatos(nomPrograma, descripcion, v, fAlta);
        }
    }
   
    //Agregar Curso/s a programas de formacion
    @Override
    public void AgregarCursoAProgramas(String nomPrograma, List<String> cursos){
        ProgramaDeFormacion pdf = manProgramas.BuscarPrograma(nomPrograma);
        for(int i = 0;i<cursos.size();i++){
            Curso c = manCursos.BuscarCurso(cursos.get(i));
            manProgramas.AddCurso(pdf, c);
        }
    }
    
    //Consulta Programa de Formacion
    @Override
    public DTProgramaForm ConsultaProgramaFormacion(String nomPrograma){
        return null;
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
    public void AltaInstituto(String nomInstituto)throws Exception{
        Instituto i = manInstituto.CreaInstituto(nomInstituto);
        manInstituto.Add(i);
    }
    
    
    //Otras Funciones
    //Verificar si existe curso con el nombre
    @Override
    public boolean VerificarCurso(String nombre){
        Curso c = manCursos.BuscarCurso(nombre);
        return c!=null;
    }
    @Override
    public boolean VerificarInstituto(String instituto){
        Instituto i = manInstituto.BuscarInstituto(instituto);
        return i!=null;
    }
    @Override
    public boolean VerificarEdicion(String nombre){
        EdicionCurso ec = manEdicion.BuscarEdicion(nombre);
        return ec!=null;
    }
    @Override
    public boolean VerificarPrograma(String nombre){
        ProgramaDeFormacion pdf = manProgramas.BuscarPrograma(nombre);
        return pdf!=null;
    }
    
    //Devoolver lista completa de DTs
    //Lista que no requiere de ninguna condicion
    @Override
    public List<DTMaster> ListarClase(EnumDT enumType){
        List<DTMaster> listReturn = new ArrayList<>();
        if(enumType==EnumDT.DT_INSTITUTO){
            listReturn = manInstituto.getDTList();
        }else if(enumType==EnumDT.DT_CURSO){
            listReturn = manCursos.getDTList();
        }else if(enumType==EnumDT.DT_USUARIO){
            listReturn = manUsuario.getDTList();
        }else if(enumType==EnumDT.DT_PROGRAMA){
            listReturn = manProgramas.getDTList();
        }
        
        return listReturn;
    }
    
    @Override
    public List<DTMaster> ListarCursos(String nomInstituto){
        List<DTMaster> listReturn = manCursos.getDTLIst(nomInstituto);
        return listReturn;
    }
    @Override
    public List<DTMaster>ListarEdiciones(String nomCurso){
        List<DTMaster> listReturn = manEdicion.getDTLIst(nomCurso);
        return listReturn;
    }
    @Override
    public List<DTMaster>ListarDocentes(String nomInstituto){
        List<DTMaster> listReturn = manUsuario.getDTList(nomInstituto);
        return listReturn;
    }
    
}