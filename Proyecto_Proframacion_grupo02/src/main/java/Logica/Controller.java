/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;
import Manejadores.ManejadorInstituto;
import Manejadores.ManejadorCursos;
import Manejadores.ManejadorProgramasDeFormacion;
import Manejadores.ManejadorEdicionCurso;
import Manejadores.ManejadorUsuario;
import Manejadores.ManejadorCategoria;

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
//Imports Clases
import Classes.UsuarioBase;
import Classes.Curso;
import Classes.Docente;
import Classes.Edi_Usu;
import Classes.Instituto;
import Classes.EdicionCurso;
import Classes.Id_EdiUsu;
import Classes.Id_ProgUsu;
import Classes.Prog_Usu;
import Classes.Usuario;
import Classes.ProgramaDeFormacion;
import Classes.Categoria;
import Classes.Enum_Estado_inscripcion;
//Imports DTs
import DTsClasses.DTCurso;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTUsuarioBase;
import DTsClasses.DTInstituto;
import DTsClasses.DTMaster;
import DTsClasses.EnumDT;
import DTsClasses.DTProgramaForm;
import java.util.Random;


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
    ManejadorCategoria manCategoria;
    
    public Controller(){
        manUsuario = ManejadorUsuario.GetInstance();
        manCursos = ManejadorCursos.GetInstance();
        manInstituto = ManejadorInstituto.GetInstance();
        manEdicion = ManejadorEdicionCurso.GetInstance();
        manProgramas = ManejadorProgramasDeFormacion.GetInstance();
        manCategoria = ManejadorCategoria.GetInstance();
    }
    
    //Alta Usuario
    @Override
    public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, String password, Date fechaNac, boolean docente, String instituto, String imgPath)throws Exception {
        UsuarioBase auxUsuario = null;
        Instituto auxInstituto = manInstituto.BuscarInstituto(instituto);
        //String password = GenerateRandPassword();
        try {
            auxUsuario = manUsuario.CrearUsuario(nickname, nombre, apellido, correo, password, docente, fechaNac, auxInstituto, imgPath);
        } catch (IOException ex) {
            System.getLogger("No se pudo crear el usuario(Error en Controller.AgregarUsuario())");
        }
        manUsuario.Add(auxUsuario);
        EnviarGmail eg = new EnviarGmail();
        eg.EnviarAsincrono(correo, "Bienvenido a la plataforma de edEXT", eg.CuerpoMensajeNuevoUsuario(manUsuario.getDT(auxUsuario)));
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
    public void ModificarUsuario(String nickname, String newNombre, String newApellido, String newPassword,boolean docente, Date newFechaNac, String instituto, String imgPath){
        
        Instituto auxInstituto = manInstituto.BuscarInstituto(instituto);
        try {
            manUsuario.ModificarDatosUsuario(nickname, newNombre, newApellido, newPassword, true, newFechaNac, auxInstituto, imgPath);
        } catch (IOException ex) {
            System.getLogger("No se pudo modificar el usuario(Error en Controller.ModificarUsuario())");
        }
    }
    
    @Override
    public void SeguirUsuario(String nickname1, String nickname2){
        UsuarioBase ub1 = manUsuario.BuscarUsuario(nickname1);
        UsuarioBase ub2 = manUsuario.BuscarUsuario(nickname2);
        manUsuario.SeguirUsuarios(ub1, ub2);
    }
    @Override
    public void DejarDeSeguir(String nickname1, String nickname2){
        UsuarioBase ub1 = manUsuario.BuscarUsuario(nickname1);
        UsuarioBase ub2 = manUsuario.BuscarUsuario(nickname2);
        manUsuario.DejarDeSeguir(ub1, ub2);
    }
    
    
    //Alta Curso
    @Override
    public void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, List<String> previas, Date fechaIngreso, String docente, List<String> categorias) throws Exception {
        Curso auxC = manCursos.BuscarCurso(nombre);
        UsuarioBase auxUb = manUsuario.BuscarUsuario(docente);
        List<Categoria> auxCategorias = new ArrayList<>();
        for(int i = 0;i<categorias.size();i++){
            auxCategorias.add(manCategoria.BuscarCategoria(categorias.get(i)));
        }
        if(auxC==null){
            Instituto ins = new Instituto();
            ins.setNombre(nomInstituto);     
            Curso c = manCursos.CrearCurso(ins, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, fechaIngreso,previas, auxUb, auxCategorias);
            manUsuario.AddCurso(auxUb, c);
            manCursos.Add(c);
        }else{
            manUsuario.RemoveCurso(auxC.getMiDocente(), auxC);
            manUsuario.AddCurso(auxUb, auxC);
            manCursos.ModificarCurso(auxC, descripcion, duracion, cantHoras, cantCreditos, URL, fechaIngreso,previas, auxUb, auxCategorias);
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
        System.out.println("Los docentes son: "+docentes);
        if(auxEc==null){
            // 1. Crear la entidad Edición
            List<Docente> auxListDocentes = new ArrayList<>();
            if (docentes != null) {
                for(int i = 0; i < docentes.size(); i++){
                    Docente ub = (Docente)manUsuario.BuscarUsuario(docentes.get(i));
                    if (ub != null) {
                        
                        auxListDocentes.add(ub);
                    }
                }
            }
            EdicionCurso ec = manEdicion.CrearEdicion(ins, c, nomEdicion, fInicio, fFin, cupo, fAlta,auxListDocentes);
            
            // 2. Guardar la edición en su manejador / BD
            
            for(Docente d : auxListDocentes){
                manUsuario.AddEdicion(d, ec);
            }
            // 3. Vincular en memoria la nueva Edición al Curso padre
            if (c != null) {
                if (c.getEdiciones() == null) {
                    // Inicialización en caso de que sea null
                }
                c.getEdiciones().add(ec);
            }
            manEdicion.Add(ec);
           
        }else{
            List<Docente> auxListDocentes = new ArrayList<>();
            if (docentes != null) {
                for(int i = 0; i < docentes.size(); i++){
                    Docente ub = (Docente)manUsuario.BuscarUsuario(docentes.get(i));
                    if (ub != null) {
                        auxListDocentes.add(ub);
                    }
                }
            }
            if(auxListDocentes!=null){
                for(Docente d : auxEc.getMisDocentes()){
                    manUsuario.RemoveEdicion(d, auxEc);
                }
                for(Docente d : auxListDocentes){
                    manUsuario.AddEdicion(d, auxEc);
                }
            }
            manEdicion.ModificarDatos(auxEc, fInicio, fFin, cupo, fAlta, auxListDocentes);
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
        Id_EdiUsu ieu = new Id_EdiUsu(u, ec);
        Edi_Usu eu = new Edi_Usu(ieu, fIns);

        manUsuario.InscribirUsuarioAEdicion(eu);

        try {
            manEdicion.AddUsuarioInscripto(eu); // Linea 220
        } catch (Exception e) {
            System.err.println("Error al agregar usuario inscripto: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //En esta funcion la lista de Object[] recibe el nickname del estudiate como indice="0" y el cambio con indice="1" que puede ser Enum_Estado_inscripcion - ACEPTADO, RECHAZADO O INSCRIPTO
    //si y solo si el profe confirma que esto se puede en todo caso solo se pasaran los estudiantes que fueron aceptados y rechazados
    @Override
    public void AceptarEstudiantesAEdicionCurso(List<Object[]> estudiantesYCambios, String nomEdicion){
        for(Object[] o : estudiantesYCambios){
            manEdicion.ModificarEstadoDeInscripcion((String)o[0], (Enum_Estado_inscripcion)o[1], nomEdicion);
        }
        
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
        if (pdf != null && cursos != null) {
            for (String nomCurso : cursos) {
                Curso c = manCursos.BuscarCurso(nomCurso);
                if (c != null) {
                    // 1. Agrega el curso al programa
                    manProgramas.AddCurso(pdf, c); 

                    // 2. CRUCIAL: Vincula el programa dentro del curso en memoria RAM
                    c.AddPrograma(pdf); 
                }
            }
        }
    }
    
    //Consulta Programa de Formacion
    @Override
    public DTMaster ConsultaProgramaFormacion(String nomPrograma){
        ProgramaDeFormacion pdf = manProgramas.BuscarPrograma(nomPrograma);
        if(pdf!=null){
            DTMaster auxDT = manProgramas.getDT(pdf);
            return auxDT;
        }
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
    
    @Override
    public void InscripcionUsuarioAProgramas(String nomPrograma, String nickname, Date fIns) {
        Usuario u = (Usuario) manUsuario.BuscarUsuario(nickname);
        ProgramaDeFormacion pdf = manProgramas.BuscarPrograma(nomPrograma);
        Id_ProgUsu ipu = new Id_ProgUsu(u, pdf);
        Prog_Usu pu = new Prog_Usu(ipu, fIns);

        manUsuario.InscribirUsuarioAPrograma(pu);

        try {
            manProgramas.AddUsuarioInscripto(pu);
        } catch (Exception e) {
            System.err.println("Error al agregar usuario inscripto: " + e.getMessage());
            e.printStackTrace();
        }

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
    @Override
    public boolean VerificarCategoria(String nombre){
        Categoria c = manCategoria.BuscarCategoria(nombre);
        return c!=null;
    }
    //Retornara true si el correo ya esta en la base de datos
    @Override
    public boolean VerificarCorreo(String correo){
        return manUsuario.VerificarUsuario(correo)!=null;
    }
    //Retornanra true si el nickname ya esta en la base de datos
    @Override
    public boolean VerificarNickname(String nickname){
        return manUsuario.VerificarUsuario(nickname)!=null;
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
        }else if(enumType==EnumDT.DT_CATEGORIA){
            try {
                listReturn = manCategoria.getDTList();
            } catch (Exception ex) {
                System.getLogger(Controller.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
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
    public List<DTMaster> ListarEdicionesPrioritarias(String nomEdicion){
        EdicionCurso eu = manEdicion.BuscarEdicion(nomEdicion);
        List<Edi_Usu> auxList = manEdicion.getMisInscripciones(eu);
        return manEdicion.OrdenarInscripcionesPorPrioridad(auxList, eu.getCurso());
    }
    
    @Override
    public List<DTMaster>ListarDocentes(String nomInstituto){
        List<DTMaster> listReturn = manUsuario.getDTList(nomInstituto);
        return listReturn;
    }
    @Override
    public List<DTMaster>ListarProgramaDeForm(){
        List<DTMaster> listReturn = manProgramas.getDTList();
        return listReturn;
    }
    @Override
    public List<DTMaster>ListarInscripciones(String nickEstudiante){
        UsuarioBase ub = manUsuario.BuscarUsuario(nickEstudiante);
        List<DTMaster> auxDT = new ArrayList<>();
        if(ub instanceof Usuario u){
            List<Edi_Usu> auxEdiUsu = u.getMisInscripciones();
            
            for(Edi_Usu eu : auxEdiUsu){
                auxDT.add(eu.getMyDT());
                
            }
        }
        return auxDT;
    }
    @Override
    public String GenerateRandPassword(){
        Random random = new Random();
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String numeros = "0123456789";
        int tamPassword = 10;
        String finalPass = ".-";
        for(int i = 0;i<tamPassword;i++){
            int randChar = random.nextInt(2);
            if(randChar==0){
                int letraNum = random.nextInt(letras.length());
                char letra = letras.charAt(letraNum);
                finalPass += letra;
            }else if(randChar == 1){
                int letraNum = random.nextInt(numeros.length());
                char letra = numeros.charAt(letraNum);
                finalPass += letra;
            }
        }
        return finalPass;
    }
    
    @Override
    public void AltaCategoria(String nombre){
        Categoria c = manCategoria.CrearCategoria(nombre);
        try {
            manCategoria.Add(c);
        } catch (Exception ex) {
            System.getLogger("No se pudo crear la categoria(Error en Controller.AltaCategoria())");
        }
    }
    public DTMaster ConsultaUsuarioAvanzada(String id, String password){
        UsuarioBase ub;
        ub = manUsuario.BusquedaAvanzada(id, password);
        if(ub!=null){
            DTUsuarioBase dt = manUsuario.getDT(ub);
            return dt;
        }
        
        return null;
    }

   
}
