package Logica.ws;

import Logica.Classes.Enum_Estado_inscripcion;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTValoracionCurso;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.IController;
import Logica.ws.cliente.DtCambioEstado;
import Logica.ws.cliente.EdextWSException;
import Logica.ws.cliente.PublicadorWS;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * Implementación de IController del servidor web: no tiene lógica ni acceso a
 * la base de datos, cada operación se resuelve llamando al Web Service del
 * Servidor Central y convirtiendo la respuesta con ConversorDT.
 */
public class ControllerWS implements IController {

    private final Supplier<PublicadorWS> puerto;

    public ControllerWS() {
        this(ClienteWS::getPuerto);
    }

    /** Permite inyectar el proxy del Web Service (tests). */
    public ControllerWS(Supplier<PublicadorWS> puerto) {
        this.puerto = puerto;
    }

    private PublicadorWS ws() {
        return puerto.get();
    }

    /** Los errores de negocio del SC llegan como EdextWSException con el mensaje original. */
    private static Exception error(EdextWSException e) {
        return new Exception(e.getMessage(), e);
    }

    // ===================== Usuarios =====================

    @Override
    public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, String password,
            Date fechaNac, boolean docente, String instituto, String imgPath) throws Exception {
        try {
            ws().agregarUsuario(nickname, nombre, apellido, correo, password, ConversorDT.fecha(fechaNac),
                    docente, instituto, imgPath);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public DTUsuarioBase ConsultarUsuario(String nickname) {
        return ConversorDT.usuario(ws().consultarUsuario(nickname));
    }

    @Override
    public void ModificarUsuario(String nickname, String newNombre, String newApellido, String newPassword,
            boolean docente, Date newFechaNac, String instituto, String imgPath) {
        ws().modificarUsuario(nickname, newNombre, newApellido, newPassword, docente,
                ConversorDT.fecha(newFechaNac), instituto, imgPath);
    }

    @Override
    public void SeguirUsuario(String nickname1, String nickname2) {
        ws().seguirUsuario(nickname1, nickname2);
    }

    @Override
    public void DejarDeSeguir(String nickname1, String nickname2) {
        ws().dejarDeSeguir(nickname1, nickname2);
    }

    @Override
    public List<String> ListarSeguidos(String nickname) {
        return new ArrayList<>(ws().listarSeguidos(nickname));
    }

    @Override
    public List<String> ListarSeguidores(String nickname) {
        return new ArrayList<>(ws().listarSeguidores(nickname));
    }

    @Override
    public DTMaster ConsultaUsuarioAvanzada(String id, String password) {
        return ConversorDT.dt(ws().consultaUsuarioAvanzada(id, password));
    }

    @Override
    public boolean VerificarCorreo(String correo) {
        return ws().verificarCorreo(correo);
    }

    @Override
    public boolean VerificarNickname(String nickname) {
        return ws().verificarNickname(nickname);
    }

    @Override
    public String GenerateRandPassword() {
        return ws().generateRandPassword();
    }

    // ===================== Cursos =====================

    @Override
    public void AltaCurso(String nomInstituto, String nombre, String descripcion, int duracion, float cantHoras,
            int cantCreditos, String URL, List<String> previas, Date fechaIngreso, String docente,
            List<String> categorias, String imgPath) throws Exception {
        try {
            ws().altaCurso(nomInstituto, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, previas,
                    ConversorDT.fecha(fechaIngreso), docente, categorias, imgPath);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public DTMaster ConsultaCurso(String nomCurso) {
        return ConversorDT.dt(ws().consultaCurso(nomCurso));
    }

    @Override
    public boolean VerificarCurso(String nombre) {
        return ws().verificarCurso(nombre);
    }

    @Override
    public List<DTMaster> ListarCursos(String nomInstituto) {
        return ConversorDT.lista(ws().listarCursos(nomInstituto));
    }

    @Override
    public List<DTMaster> ListarCursosXCategoria(String categoria) {
        return ConversorDT.lista(ws().listarCursosXCategoria(categoria));
    }

    @Override
    public boolean RegistrarVisitaCurso(String nomCurso) {
        return ws().registrarVisitaCurso(nomCurso);
    }

    @Override
    public List<DTMaster> ListarCursosMasVisitados(int cantidad) {
        return ConversorDT.lista(ws().listarCursosMasVisitados(cantidad));
    }

    // ===================== Ediciones =====================

    @Override
    public void AltaEdicionCurso(String instituto, String nomCurso, String nomEdicion, Date fInicio, Date fFin,
            int cupo, List<String> docentes, Date fAlta, String imgPath) throws Exception {
        try {
            ws().altaEdicionCurso(instituto, nomCurso, nomEdicion, ConversorDT.fecha(fInicio),
                    ConversorDT.fecha(fFin), cupo, docentes, ConversorDT.fecha(fAlta), imgPath);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public DTMaster ConsultaEdicionCurso(String nomEdicion) {
        return ConversorDT.dt(ws().consultaEdicionCurso(nomEdicion));
    }

    @Override
    public boolean VerificarEdicion(String nombre) {
        return ws().verificarEdicion(nombre);
    }

    @Override
    public List<DTMaster> ListarEdiciones(String nomCurso) {
        return ConversorDT.lista(ws().listarEdiciones(nomCurso));
    }

    @Override
    public List<DTMaster> ListarEdicionesPrioritarias(String nomEdicion) {
        return ConversorDT.lista(ws().listarEdicionesPrioritarias(nomEdicion));
    }

    @Override
    public void InscripcionAEdicionCurso(String nomEdicion, String nickname, Date fIns) throws Exception {
        InscripcionAEdicionCurso(nomEdicion, nickname, fIns, null);
    }

    @Override
    public void InscripcionAEdicionCurso(String nomEdicion, String nickname, Date fIns, String urlVideo) throws Exception {
        try {
            ws().inscripcionAEdicionCurso(nomEdicion, nickname, ConversorDT.fecha(fIns), urlVideo);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public DTMaster ConsultarInscripcionEdicion(String nickname, String nomEdicion) {
        return ConversorDT.dt(ws().consultarInscripcionEdicion(nickname, nomEdicion));
    }

    @Override
    public void ModificarInscripcionEdicion(String nickname, String nomEdicion, String urlVideo) throws Exception {
        try {
            ws().modificarInscripcionEdicion(nickname, nomEdicion, urlVideo);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public void DesistirDeEdicionDeCurso(String nickname, String nomEdicion) throws Exception {
        try {
            ws().desistirDeEdicionDeCurso(nickname, nomEdicion);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public void IngresarResultadosDeEvaluaciones(String nomEdicion, List<String> estudiantes, List<Integer> notas)
            throws Exception {
        try {
            ws().ingresarResultadosDeEvaluaciones(nomEdicion, estudiantes, notas);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public void FinalizarEdicionDeCurso(String nomEdicion, List<String> estudiantes, List<Integer> notas)
            throws Exception {
        try {
            ws().finalizarEdicionDeCurso(nomEdicion, estudiantes, notas);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public DTMaster EdicionVigente(String nomCurso) {
        return ConversorDT.dt(ws().edicionVigente(nomCurso));
    }

    @Override
    public List<DTMaster> ListarInscripciones(String nicknameEstudiante) {
        return ConversorDT.lista(ws().listarInscripciones(nicknameEstudiante));
    }

    @Override
    public DTMaster ActaDeEstudio(String nickname, String nomPrograma) throws Exception {
        try {
            return ConversorDT.acta(ws().actaDeEstudio(nickname, nomPrograma));
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    // ===================== Valorar un Curso =====================

    @Override
    public void ValorarCurso(String nickname, String nomCurso, int puntaje) throws Exception {
        try {
            ws().valorarCurso(nickname, nomCurso, puntaje);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public DTValoracionCurso ValoracionesCurso(String nomCurso, String nickname) {
        return ConversorDT.valoracion(ws().valoracionesCurso(nomCurso, nickname));
    }

    @Override
    public List<String> CursosParticipa(String nickname) throws Exception {
        try {
            return ws().cursosParticipa(nickname);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    /** Cada elemento es {nickname (String), nuevo estado (Enum_Estado_inscripcion)}. */
    @Override
    public void AceptarEstudiantesAEdicionCurso(List<Object[]> estudiantesYCambios, String nomEdicion) throws Exception {
        List<DtCambioEstado> cambios = new ArrayList<>();
        if (estudiantesYCambios != null) {
            for (Object[] o : estudiantesYCambios) {
                if (o != null && o.length >= 2 && o[0] instanceof String nick
                        && o[1] instanceof Enum_Estado_inscripcion estado) {
                    DtCambioEstado c = new DtCambioEstado();
                    c.setNickname(nick);
                    c.setEstado(ConversorDT.estado(estado));
                    cambios.add(c);
                }
            }
        }
        try {
            ws().aceptarEstudiantesAEdicionCurso(cambios, nomEdicion);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    // ===================== Programas =====================

    @Override
    public void CrearProgramasDeFormacion(String nomPrograma, String descripcion, Date fInicio, Date fFin, Date fAlta,
            String imgPath, String instituto) throws Exception {
        try {
            ws().crearProgramasDeFormacion(nomPrograma, descripcion, ConversorDT.fecha(fInicio),
                    ConversorDT.fecha(fFin), ConversorDT.fecha(fAlta), imgPath, instituto);
        } catch (EdextWSException e) {
            throw error(e);
        }
    }

    @Override
    public void AgregarCursoAProgramas(String nomPrograma, List<String> cursos) {
        ws().agregarCursoAProgramas(nomPrograma, cursos);
    }

    @Override
    public DTMaster ConsultaProgramaFormacion(String nomPrograma) {
        return ConversorDT.dt(ws().consultaProgramaFormacion(nomPrograma));
    }

    @Override
    public void InscripcionUsuarioAProgramas(String nomPrograma, String nickname, Date fIns) {
        ws().inscripcionUsuarioAProgramas(nomPrograma, nickname, ConversorDT.fecha(fIns));
    }

    @Override
    public boolean VerificarPrograma(String nombre) {
        return ws().verificarPrograma(nombre);
    }

    @Override
    public boolean ExistePrograma(String nombreProg) {
        return VerificarPrograma(nombreProg);
    }

    @Override
    public List<DTMaster> ListarProgramaDeForm() {
        return ConversorDT.lista(ws().listarProgramaDeForm());
    }

    @Override
    public List<DTMaster> ListarProgramasDeCurso(String curso) {
        return ConversorDT.lista(ws().listarProgramasDeCurso(curso));
    }

    // ===================== Listados generales =====================

    @Override
    public List<DTMaster> ListarClase(EnumDT enumType) {
        return ConversorDT.lista(ws().listarClase(ConversorDT.enumDT(enumType)));
    }

    @Override
    public List<DTMaster> ListarDocentes(String nomInstituto) {
        return ConversorDT.lista(ws().listarDocentes(nomInstituto));
    }

    @Override
    public List<DTMaster> ListarCurEdiProg(EnumDT tipoBusqueda, String dato) {
        return ConversorDT.lista(ws().listarCurEdiProg(ConversorDT.enumDT(tipoBusqueda), dato));
    }

    @Override
    public boolean VerificarInstituto(String instituto) {
        return ws().verificarInstituto(instituto);
    }

    @Override
    public boolean VerificarCategoria(String nombre) {
        return ws().verificarCategoria(nombre);
    }

    // ===================== Solo disponibles en el Servidor Central (Swing) =====================

    @Override
    public void ActualizarPrograma(String nombreProg, String descripcionProg, Date fInicio, Date fFin) {
        throw new UnsupportedOperationException("ActualizarPrograma solo está disponible en el Servidor Central");
    }

    @Override
    public void AltaInstituto(String nomInstituto) throws Exception {
        throw new UnsupportedOperationException("AltaInstituto solo está disponible en el Servidor Central");
    }

    @Override
    public void AltaCategoria(String nombre) {
        throw new UnsupportedOperationException("AltaCategoria solo está disponible en el Servidor Central");
    }
}
