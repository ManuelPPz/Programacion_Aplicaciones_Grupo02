package WebServer;
import Classes.Enum_Estado_inscripcion;
import DTsClasses.DTMaster;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import jakarta.jws.WebService;
import java.util.Date;
import java.util.List;
import Logica.IController;
import Logica.Fabric;


@WebService(endpointInterface = "WebServer.LabService")
public class LabServiceImpl implements LabService{
    IController ico;
    public LabServiceImpl(){
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
    }
    
    @Override
    public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, String password, Date fechaNac, boolean docente, String instituto, String imgPath) {
        try {
            ico.AgregarUsuario(nickname, nombre, apellido, correo, password, fechaNac, docente, instituto, imgPath);
        } catch (Exception ex) {
            System.getLogger(LabServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public DTUsuarioBase ConsultarUsuario(String nickname) {
        return ico.ConsultarUsuario(nickname);
    }

    @Override
    public void ModificarUsuario(String nickname, String newNombre, String newApellido, String newPassword, boolean docente, Date newFechaNac, String instituto, String imgPath) {
        ico.ModificarUsuario(nickname, newNombre, newApellido, newPassword, docente, newFechaNac, instituto, imgPath);
    }

    @Override
    public void SeguirUsuario(String nickname1, String nickname2) {
        ico.SeguirUsuario(nickname1, nickname2);
    }

    @Override
    public void DejarDeSeguir(String nickname1, String nickname2) {
        ico.DejarDeSeguir(nickname1, nickname2);
    }

    @Override
    public void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, List<String> previas, Date fechaIngreso, String docente, List<String> categorias, String imgPath) {
        try {
            ico.AltaCurso(nomInstituto, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, previas, fechaIngreso, docente, categorias, imgPath);
        } catch (Exception ex) {
            System.getLogger(LabServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public DTMaster ConsultaCurso(String nomCurso) {
        return ico.ConsultaCurso(nomCurso);
    }
    
    @Override
    public void ValorarCurso(String nomCurso, String nickname, float valoracion){
        ico.ValorarCurso(nomCurso, nickname, valoracion);
    }

    @Override
    public void AltaEdicionCurso(String instituto, String nomCurso, String nomEdicion, Date fInicio, Date fFin, int cupo, List<String> docentes, Date fAlta, String imgPath) {
        try {
            ico.AltaEdicionCurso(instituto, nomCurso, nomEdicion, fInicio, fFin, cupo, docentes, fAlta, imgPath);
        } catch (Exception ex) {
            System.getLogger(LabServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public DTMaster ConsultaEdicionCurso(String nomEdicion) {
        return ico.ConsultaEdicionCurso(nomEdicion);
    }

    @Override
    public void InscripcionAEdicionCurso(String nomEdicion, String nickname, Date fIns) {
        ico.InscripcionAEdicionCurso(nomEdicion, nickname, fIns);
    }
    @Override
    public void DesistirDeEdicionDeCurso(String nombreUsuario, String nomEdicion){
        ico.DesistirDeEdicionDeCurso(nombreUsuario, nomEdicion);
    }
    @Override
    public void IngresarEvaluacionesDeEstudiantes(String nomEdicion, List<String> estudiantes, List<Integer> notas){
        ico.IngresarResultadosDeEvaluaciones(nomEdicion, estudiantes, notas);
    }
    @Override
    public void FinalizarEdicionDeCurso(String nomEdicion, List<String> estudiantes, List<Integer> notas){
        ico.IngresarResultadosDeEvaluaciones(nomEdicion, estudiantes, notas);
        ico.FinalizarEdicionDeCurso(nomEdicion);
    }
/*
    @Override
    public void AceptarEstudiantesAEdicionCurso(List<Object[]> estudiantesYCambios, String nomEdicion) {
        ico.AceptarEstudiantesAEdicionCurso(estudiantesYCambios, nomEdicion);
    }
*/
    @Override
    public void CrearProgramasDeFormacion(String nomPrograma, String descripcion, Date fInicio, Date fFin, Date fAlta, String imgPath, String instituto) {
        try {
            ico.CrearProgramasDeFormacion(nomPrograma, descripcion, fInicio, fFin, fAlta, imgPath, instituto);
        } catch (Exception ex) {
            System.getLogger(LabServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public void AgregarCursoAProgramas(String nomPrograma, List<String> cursos) {
        ico.AgregarCursoAProgramas(nomPrograma, cursos);
    }

    @Override
    public DTMaster ConsultaProgramaFormacion(String nomPrograma) {
        return ico.ConsultaProgramaFormacion(nomPrograma);
    }

    @Override
    public boolean ExistePrograma(String nombreProg) {
        return ico.ExistePrograma(nombreProg);
    }

    @Override
    public void InscripcionUsuarioAProgramas(String nomPrograma, String nickname, Date fIns) {
        ico.InscripcionUsuarioAProgramas(nomPrograma, nickname, fIns);
    }

    @Override
    public boolean VerificarCurso(String nombre) {
        return ico.VerificarCurso(nombre);
    }

    @Override
    public boolean VerificarInstituto(String instituto) {
        return ico.VerificarInstituto(instituto);
    }

    @Override
    public boolean VerificarEdicion(String nombre) {
        return ico.VerificarEdicion(nombre);
    }

    @Override
    public boolean VerificarPrograma(String nombre) {
        return ico.VerificarPrograma(nombre);
    }

    @Override
    public boolean VerificarCategoria(String nombre) {
        return ico.VerificarCategoria(nombre);
    }

    @Override
    public boolean VerificarCorreo(String correo) {
        return ico.VerificarCorreo(correo);
    }

    @Override
    public boolean VerificarNickname(String nickname) {
        return ico.VerificarNickname(nickname);
    }

    @Override
    public List<DTMaster> ListarClase(EnumDT enumType) {
        return ico.ListarClase(enumType);
    }

    @Override
    public List<DTMaster> ListarCursos(String nomInstituto) {
        return ico.ListarCursos(nomInstituto);
    }
    
    @Override
    public List<DTMaster> ListarEdiciones(String nomCurso) {
        return ico.ListarEdiciones(nomCurso);
    }

    @Override
    public List<DTMaster> ListarEdicionesPrioritarias(String nomEdicion) {
        return ico.ListarEdicionesPrioritarias(nomEdicion);
    }

    @Override
    public List<DTMaster> ListarDocentes(String nomInstituto) {
        return ico.ListarDocentes(nomInstituto);
    }


    @Override
    public List<DTMaster> ListarInscripciones(String nickEstudiante) {
        return ico.ListarInscripciones(nickEstudiante);
    }
    
    @Override
    public Enum_Estado_inscripcion VerificarInscripcion(String nomUsuario, String nomEdicion){
        return ico.VerificarInscripcion(nomUsuario, nomEdicion);
    }

    @Override
    public DTMaster ConsultaUsuarioAvanzada(String id, String password) {
        return ico.ConsultaUsuarioAvanzada(id, password);
    }

    @Override
    public List<DTMaster> ListarProgramasDeCurso(String nomCurso) {
        return ico.ListarProgramasDeCurso(nomCurso);
    }

    @Override
    public List<DTMaster> ListarCurEdiProg(EnumDT tipoBusqueda, String dato) {
        return ico.ListarCurEdiProg(tipoBusqueda, dato);
    }
    
    @Override
    public DTMaster CantValPorEstrellas(String curso){
        return ico.CantValPorEstrellas(curso);
    }
    
}
