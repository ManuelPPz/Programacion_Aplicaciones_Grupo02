package WebServer;
import Classes.Enum_Estado_inscripcion;
import DTsClasses.DTMaster;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.List;

@WebService(targetNamespace = "http://WebServer/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface LabService{
   @WebMethod
   void AgregarUsuario(String nickname, String nombre, String apellido, String correo, String password, Date fechaNac, boolean docente, String instituto, String imgPath);
   @WebMethod
   DTUsuarioBase ConsultarUsuario(String nickname);
   @WebMethod
   void ModificarUsuario(String nickname, String newNombre, String newApellido, String newPassword,boolean docente, Date newFechaNac, String instituto, String imgPath);
   @WebMethod
   void SeguirUsuario(String nickname1, String nickname2);
   @WebMethod
   void DejarDeSeguir(String nickname1, String nickname2);
   @WebMethod
   void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, List<String> previas, Date fechaIngreso, String docente, List<String> categorias, String imgPath);
   @WebMethod
   DTMaster ConsultaCurso(String nomCurso);
   @WebMethod
   void AltaEdicionCurso(String instituto, String nomCurso, String nomEdicion, Date fInicio, Date fFin, int cupo, List<String> docentes, Date fAlta, String imgPath);
   @WebMethod
   DTMaster ConsultaEdicionCurso(String nomEdicion);
   @WebMethod
   void InscripcionAEdicionCurso(String nomEdicion, String nickname, Date fIns);
   @WebMethod
   void DesistirDeEdicionDeCurso(String nombreUsuario, String nomEdicion);
   @WebMethod
   void IngresarEvaluacionesDeEstudiantes(String nomEdicion, List<String> estudiantes, List<Integer> notas);
   @WebMethod
   void FinalizarEdicionDeCurso(String nomEdicion, List<String> estudiantes, List<Integer> notas);
   /*
   @WebMethod
   void AceptarEstudiantesAEdicionCurso(List<Object[]> estudiantesYCambios, String nomEdicion);
*/
   @WebMethod
   void CrearProgramasDeFormacion(String nomPrograma, String descripcion, Date fInicio, Date fFin,Date fAlta, String imgPath, String instituto);
   @WebMethod
   void AgregarCursoAProgramas(String nomPrograma, List<String> cursos);
   @WebMethod
   DTMaster ConsultaProgramaFormacion(String nomPrograma);
   @WebMethod
   boolean ExistePrograma(String nombreProg);
   @WebMethod
   void InscripcionUsuarioAProgramas(String nomPrograma, String nickname, Date fIns);
   @WebMethod
   boolean VerificarCurso(String nombre);
   @WebMethod
   boolean VerificarInstituto(String instituto);
   @WebMethod
   boolean VerificarEdicion(String nombre);
   @WebMethod
   boolean VerificarPrograma(String nombre);
   @WebMethod
   boolean VerificarCategoria(String nombre);
   @WebMethod
   boolean VerificarCorreo(String correo);
   @WebMethod
   boolean VerificarNickname(String nickname);
   @WebMethod
   List<DTMaster> ListarClase(EnumDT enumType);
   @WebMethod
   List<DTMaster> ListarCursos(String nomInstituto);
   @WebMethod
   List<DTMaster>ListarEdiciones(String nomCurso);
   @WebMethod
   List<DTMaster> ListarEdicionesPrioritarias(String nomEdicion);
   @WebMethod
   List<DTMaster>ListarDocentes(String nomInstituto);
   @WebMethod
   List<DTMaster>ListarInscripciones(String nickEstudiante);
   @WebMethod
   DTMaster ConsultaUsuarioAvanzada(String id, String password);
   @WebMethod
   List<DTMaster> ListarProgramasDeCurso(String nomCurso);
   @WebMethod
   List<DTMaster> ListarCurEdiProg(EnumDT tipoBusqueda, String dato);
   @WebMethod
   Enum_Estado_inscripcion VerificarInscripcion(String nomUsuario, String nomEdicion);
}
