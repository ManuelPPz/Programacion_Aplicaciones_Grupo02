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
            manUsuario.AddCurso(auxUb, c);
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
    
// Alta Edicion Curso
@Override
public void AltaEdicionCurso(String instituto, String nomCurso, String nomEdicion, Date fInicio, Date fFin, int cupo, List<String> docentes, Date fAlta) throws Exception {
    
    Instituto ins = manInstituto.BuscarInstituto(instituto);
    Curso c = manCursos.BuscarCurso(nomCurso);
    EdicionCurso auxEc = manEdicion.BuscarEdicion(nomEdicion);
    
    if (auxEc == null) {
        List<Docente> auxListDocentes = new ArrayList<>();
        if (docentes != null) {
            for (String docStr : docentes) {
                if (docStr == null || docStr.trim().isEmpty()) continue;

                // 1. Extraer el nickname limpiando posibles formatos
                String nick = docStr.trim();
                if (nick.contains("(")) {
                    // Formato: "Nombre Apellido (nickname)"
                    nick = nick.substring(nick.indexOf("(") + 1, nick.indexOf(")")).trim();
                } else if (nick.contains(":")) {
                    // Formato: "cod: Nombre" o "Nickname: cod"
                    nick = nick.split(":")[0].trim();
                } else if (nick.contains("-")) {
                    // Formato: "cod - Nombre"
                    nick = nick.split("-")[0].trim();
                }

                // 2. Intentar buscar en el manejador directo en memoria
                UsuarioBase ub = manUsuario.BuscarUsuario(nick);
                
                // 3. Fallback: Búsqueda insensible a mayúsculas/minúsculas en el listado de DTs
                if (ub == null) {
                    for (DTMaster dt : manUsuario.getDTList()) {
                        if (dt instanceof DTUsuarioBase dtUser) {
                            if (dtUser.getNickname().equalsIgnoreCase(nick)) {
                                ub = manUsuario.BuscarUsuario(dtUser.getNickname());
                                break;
                            }
                        }
                    }
                }

                // 4. Validar que sea Instancia de Docente
                if (ub instanceof Docente d) {
                    auxListDocentes.add(d);
                } else {
                    System.err.println("[WARN] No se pudo encontrar el docente con nickname/id: '" + nick + "' (Entrada original: '" + docStr + "')");
                }
            }
        }

        // Impresión de depuración
        System.out.println("Docentes a asociar en la edición: " + auxListDocentes.size());

        // Crear la edición con la lista validada de docentes
        EdicionCurso ec = manEdicion.CrearEdicion(ins, c, nomEdicion, fInicio, fFin, cupo, fAlta, auxListDocentes);
        
        // Persistir en base de datos mediante el manejador
        manEdicion.Add(ec);

        // Vincular edición con el curso en memoria
        if (c != null && c.getEdiciones() != null && !c.getEdiciones().contains(ec)) {
            c.getEdiciones().add(ec);
        }
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
    public void InscripcionAEdicionCurso(String nomCurso, String nickname, Date fIns){
        Usuario u = (Usuario)manUsuario.BuscarUsuario(nickname);
        EdicionCurso ec = manEdicion.BuscarEdicion(nomCurso);
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