/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;
import DTsClasses.DTCurso;
import DTsClasses.DTProgramaForm;
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
import Manejadores.ManejadorUsuario;
//Imports Clases
import Classes.UsuarioBase;
//imports DTs
import DTsClasses.DTCurso;
import DTsClasses.DTUsuarioBase;
//Se debe quitar despues de probar consulta usuario
import DTsClasses.DTDocente;
import DTsClasses.DTInstituto;
import DTsClasses.DTUsuario;
import java.io.IOException;
import DTsClasses.DTMaster;
import DTsClasses.EnumDT;

/**
 *
 * @author mateo
 */
public class Controller implements IController{
    ManejadorUsuario manUsuario;
    public Controller(){
        manUsuario = ManejadorUsuario.GetInstance();
    }
    //Alta Usuario
    @Override
    public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, Date fechaNac, boolean docente, List<String> institutos, String imgPath){
        UsuarioBase auxUsuario = null;
        try {
            auxUsuario = manUsuario.CrearUsuario(nickname, nombre, apellido, correo, docente, fechaNac, institutos, imgPath);
        } catch (IOException ex) {
            System.out.print("Ocurrio un error en el sistema");
        }
        manUsuario.Add(auxUsuario);
    }   
    //Consultar Usuario, la funcion deberia devolver el tipo de dato usuario
    //Se modificara al crear el tipo de dato usuario retornando el tipo de dato "Usuario"
    @Override
    public DTUsuarioBase ConsultarUsuario(String nickname){
        UsuarioBase auxUsuario = manUsuario.BuscarUsuario(nickname);
        DTUsuarioBase auxDT = manUsuario.getDT(auxUsuario);
        return auxDT;
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
    public DTMaster ConsultaCurso(String nomCurso){
        if("Prog. de Aplicaciones".equals(nomCurso)){
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
            DTMaster auxDTCurso = new DTCurso("CURE",nomCurso,"Es un curso",1,3,15,"https://ev1.utec.edu.uy/moodle/course/view.php?id=17424",auxFecha,auxListPrev,auxListEdi,auxListProg);
            //Falta implementar buscar un curso
            /*Posible solucion:
            Curso c = ManejadorCurso.BuscarCurso(nomCurso);
            DTCurso auxDTCurso = new DTCurso(c.miInstituto.nombre,c.nombre,c.descripcion,c.duracion,c.cantHoras,c.cantCreditos,c.URL,c.fechaAlta,c.listPrevias,c.listEdiciones,c.listProgramas);
            return auxDTCurso;
            */
            return auxDTCurso;
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
        String sql = "SELECT COUNT(*) FROM Programa_Formacion WHERE nombre = ?";
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
    return true; //si no existe el nombre,,, cambiar a true para probar la otra ventana de crear programa >:)
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
    
    //Devoolver lista completa de DTs
    //Lista que no requiere de ninguna condicion
    @Override
    public List<DTMaster> ListarClase(EnumDT enumType){
        //Falta realizar
        /*Posible solucion:
        List<String> listReturn = ManejadorInstituto.GetNameMyCursos();
        */
        List<DTMaster> listReturn = new ArrayList();
        if(enumType==EnumDT.DT_INSTITUTO){
            listReturn.add(new DTInstituto("CURE"));
            listReturn.add(new DTInstituto("ORT"));
            listReturn.add(new DTInstituto("UDELAR"));
            listReturn.add(new DTInstituto("UM"));
            return listReturn;
        }else if(enumType==EnumDT.DT_CURSO){
            //Falta realizar
            /*Posible solucion:
            List<String> listReturn = ManjeadorCurso.GetNameMyCursos();
            */
            listReturn.add(new DTCurso("CURE", "Prog. de Aplicaciones", "Java, UI, Netbeans y mas", 4,40,16, "https://ev1.utec.edu.uy/moodle/course/view.php?id=17424", new Date(), null, null, null));
            listReturn.add(new DTCurso("CURE", "Prog. de Avanzada", "C++", 5,40,20, "https://ev1.utec.edu.uy/moodle/course/view.php?id=17424", new Date(), null, null, null));
            listReturn.add(new DTCurso("CURE", "COE", "Aburrida", 2,5,3, "https://ev1.utec.edu.uy/moodle/course/view.php?id=17424", new Date(), null, null, null));
            listReturn.add(new DTCurso("CURE", "ADI", "Solo Caffa", 6,41,16, "https://ev1.utec.edu.uy/moodle/course/view.php?id=17424", new Date(), null, null, null));
        }else if(enumType==EnumDT.DT_USUARIO){
            listReturn = manUsuario.getDTList();
        }
        
        return listReturn;
    }
    
    @Override
    public List<DTMaster> ListarCursos(String nomInstituto){
        //Falta Realizar
        /*Posible solucion:
            List<String> listReturn = ManjeadorCurso.GetNameMyCursos(nomInstituto);
            *//*
            if(!"CURE".equals(nomInstituto)){
                return null;
            }
            */
            List<DTMaster> listReturn = new ArrayList();
            listReturn.add(new DTCurso("CURE", "Prog. de Aplicaciones", "Java, UI, Netbeans y mas", 4,40,16, "https://ev1.utec.edu.uy/moodle/course/view.php?id=17424", new Date(), null, null, null));
            listReturn.add(new DTCurso("CURE", "ING. De Software", "La da gatto", 4,44,20, "https://ev1.utec.edu.uy/moodle/course/view.php?id=17419", new Date(), null, null, null));
            return listReturn;
    }
    
}
