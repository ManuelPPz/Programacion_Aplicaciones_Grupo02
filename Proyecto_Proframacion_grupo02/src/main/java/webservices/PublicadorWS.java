package webservices;

import Classes.Enum_Estado_inscripcion;
import DTsClasses.DTCambioEstado;
import DTsClasses.DTMaster;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import Logica.Fabric;
import Logica.IController;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import util.ImagenUtil;

/**
 * Web Service (JAX-WS) del Servidor Central. Es la única puerta de entrada del
 * servidor web a la lógica: cada operación delega en el IController.
 */
@WebService(serviceName = "PublicadorService", portName = "PublicadorPort",
        targetNamespace = "http://webservices.edext/")
public class PublicadorWS {

    private static final Set<String> CARPETAS_IMAGENES = Set.of(
            ImagenUtil.CARPETA_PERFILES, ImagenUtil.CARPETA_CURSOS,
            ImagenUtil.CARPETA_PROGRAMAS, ImagenUtil.CARPETA_EDICIONCURSOS);

    private final IController ico;

    public PublicadorWS() {
        this(Fabric.GetInstance().GetIController());
    }

    /** Permite inyectar el controlador (tests). */
    PublicadorWS(IController ico) {
        this.ico = ico;
    }

    private static EdextWSException error(Exception e) {
        String msg = (e.getMessage() == null || e.getMessage().isBlank()) ? e.toString() : e.getMessage();
        return new EdextWSException(msg, e);
    }

    // ===================== Usuarios =====================

    @WebMethod
    public void AgregarUsuario(@WebParam(name = "nickname") String nickname, @WebParam(name = "nombre") String nombre,
            @WebParam(name = "apellido") String apellido, @WebParam(name = "correo") String correo,
            @WebParam(name = "password") String password, @WebParam(name = "fechaNac") Date fechaNac,
            @WebParam(name = "docente") boolean docente, @WebParam(name = "instituto") String instituto,
            @WebParam(name = "imgPath") String imgPath) throws EdextWSException {
        try {
            ico.AgregarUsuario(nickname, nombre, apellido, correo, password, fechaNac, docente, instituto, imgPath);
        } catch (Exception e) {
            throw error(e);
        }
    }

    @WebMethod
    public DTUsuarioBase ConsultarUsuario(@WebParam(name = "nickname") String nickname) {
        return ico.ConsultarUsuario(nickname);
    }

    @WebMethod
    public void ModificarUsuario(@WebParam(name = "nickname") String nickname, @WebParam(name = "nombre") String nombre,
            @WebParam(name = "apellido") String apellido, @WebParam(name = "password") String password,
            @WebParam(name = "docente") boolean docente, @WebParam(name = "fechaNac") Date fechaNac,
            @WebParam(name = "instituto") String instituto, @WebParam(name = "imgPath") String imgPath) {
        ico.ModificarUsuario(nickname, nombre, apellido, password, docente, fechaNac, instituto, imgPath);
    }

    /** Login: id puede ser nickname o correo. Devuelve null si las credenciales no son válidas. */
    @WebMethod
    public DTMaster ConsultaUsuarioAvanzada(@WebParam(name = "id") String id, @WebParam(name = "password") String password) {
        return ico.ConsultaUsuarioAvanzada(id, password);
    }

    @WebMethod
    public void SeguirUsuario(@WebParam(name = "seguidor") String seguidor, @WebParam(name = "seguido") String seguido) {
        ico.SeguirUsuario(seguidor, seguido);
    }

    @WebMethod
    public void DejarDeSeguir(@WebParam(name = "seguidor") String seguidor, @WebParam(name = "seguido") String seguido) {
        ico.DejarDeSeguir(seguidor, seguido);
    }

    @WebMethod
    public List<String> ListarSeguidos(@WebParam(name = "nickname") String nickname) {
        return ico.ListarSeguidos(nickname);
    }

    @WebMethod
    public List<String> ListarSeguidores(@WebParam(name = "nickname") String nickname) {
        return ico.ListarSeguidores(nickname);
    }

    @WebMethod
    public boolean VerificarNickname(@WebParam(name = "nickname") String nickname) {
        return ico.VerificarNickname(nickname);
    }

    @WebMethod
    public boolean VerificarCorreo(@WebParam(name = "correo") String correo) {
        return ico.VerificarCorreo(correo);
    }

    @WebMethod
    public String GenerateRandPassword() {
        return ico.GenerateRandPassword();
    }

    // ===================== Cursos =====================

    @WebMethod
    public void AltaCurso(@WebParam(name = "instituto") String instituto, @WebParam(name = "nombre") String nombre,
            @WebParam(name = "descripcion") String descripcion, @WebParam(name = "duracion") int duracion,
            @WebParam(name = "cantHoras") float cantHoras, @WebParam(name = "cantCreditos") int cantCreditos,
            @WebParam(name = "url") String url, @WebParam(name = "previas") List<String> previas,
            @WebParam(name = "fechaAlta") Date fechaAlta, @WebParam(name = "docente") String docente,
            @WebParam(name = "categorias") List<String> categorias, @WebParam(name = "imgPath") String imgPath)
            throws EdextWSException {
        try {
            ico.AltaCurso(instituto, nombre, descripcion, duracion, cantHoras, cantCreditos, url,
                    previas, fechaAlta, docente, categorias, imgPath);
        } catch (Exception e) {
            throw error(e);
        }
    }

    @WebMethod
    public DTMaster ConsultaCurso(@WebParam(name = "nombre") String nombre) {
        return ico.ConsultaCurso(nombre);
    }

    @WebMethod
    public boolean VerificarCurso(@WebParam(name = "nombre") String nombre) {
        return ico.VerificarCurso(nombre);
    }

    @WebMethod
    public List<DTMaster> ListarCursos(@WebParam(name = "instituto") String instituto) {
        return ico.ListarCursos(instituto);
    }

    @WebMethod
    public List<DTMaster> ListarCursosXCategoria(@WebParam(name = "categoria") String categoria) {
        return ico.ListarCursosXCategoria(categoria);
    }

    // ===================== Ediciones =====================

    @WebMethod
    public void AltaEdicionCurso(@WebParam(name = "instituto") String instituto, @WebParam(name = "curso") String curso,
            @WebParam(name = "nombre") String nombre, @WebParam(name = "fechaInicio") Date fechaInicio,
            @WebParam(name = "fechaFin") Date fechaFin, @WebParam(name = "cupo") int cupo,
            @WebParam(name = "docentes") List<String> docentes, @WebParam(name = "fechaAlta") Date fechaAlta,
            @WebParam(name = "imgPath") String imgPath) throws EdextWSException {
        try {
            ico.AltaEdicionCurso(instituto, curso, nombre, fechaInicio, fechaFin, cupo, docentes, fechaAlta, imgPath);
        } catch (Exception e) {
            throw error(e);
        }
    }

    @WebMethod
    public DTMaster ConsultaEdicionCurso(@WebParam(name = "nombre") String nombre) {
        return ico.ConsultaEdicionCurso(nombre);
    }

    @WebMethod
    public boolean VerificarEdicion(@WebParam(name = "nombre") String nombre) {
        return ico.VerificarEdicion(nombre);
    }

    @WebMethod
    public List<DTMaster> ListarEdiciones(@WebParam(name = "curso") String curso) {
        return ico.ListarEdiciones(curso);
    }

    @WebMethod
    public List<DTMaster> ListarEdicionesPrioritarias(@WebParam(name = "edicion") String edicion) {
        return ico.ListarEdicionesPrioritarias(edicion);
    }

    @WebMethod
    public void InscripcionAEdicionCurso(@WebParam(name = "edicion") String edicion,
            @WebParam(name = "nickname") String nickname, @WebParam(name = "fecha") Date fecha) {
        ico.InscripcionAEdicionCurso(edicion, nickname, fecha);
    }

    @WebMethod
    public List<DTMaster> ListarInscripciones(@WebParam(name = "nickname") String nickname) {
        return ico.ListarInscripciones(nickname);
    }

    /** Cambia el estado de inscripción (aceptado/rechazado/...) de varios estudiantes a una edición. */
    @WebMethod
    public void AceptarEstudiantesAEdicionCurso(@WebParam(name = "cambios") List<DTCambioEstado> cambios,
            @WebParam(name = "edicion") String edicion) {
        List<Object[]> lista = new ArrayList<>();
        if (cambios != null) {
            for (DTCambioEstado c : cambios) {
                if (c != null && c.getNickname() != null && c.getEstado() != null) {
                    lista.add(new Object[]{c.getNickname(), c.getEstado()});
                }
            }
        }
        ico.AceptarEstudiantesAEdicionCurso(lista, edicion);
    }

    /** Lista los estados de inscripción posibles (útil para los combos del web). */
    @WebMethod
    public List<String> ListarEstadosInscripcion() {
        List<String> estados = new ArrayList<>();
        for (Enum_Estado_inscripcion e : Enum_Estado_inscripcion.values()) {
            estados.add(e.name());
        }
        return estados;
    }

    // ===================== Programas de formación =====================

    @WebMethod
    public void CrearProgramasDeFormacion(@WebParam(name = "nombre") String nombre,
            @WebParam(name = "descripcion") String descripcion, @WebParam(name = "fechaInicio") Date fechaInicio,
            @WebParam(name = "fechaFin") Date fechaFin, @WebParam(name = "fechaAlta") Date fechaAlta,
            @WebParam(name = "imgPath") String imgPath, @WebParam(name = "instituto") String instituto)
            throws EdextWSException {
        try {
            ico.CrearProgramasDeFormacion(nombre, descripcion, fechaInicio, fechaFin, fechaAlta, imgPath, instituto);
        } catch (Exception e) {
            throw error(e);
        }
    }

    @WebMethod
    public void AgregarCursoAProgramas(@WebParam(name = "programa") String programa,
            @WebParam(name = "cursos") List<String> cursos) {
        ico.AgregarCursoAProgramas(programa, cursos);
    }

    @WebMethod
    public DTMaster ConsultaProgramaFormacion(@WebParam(name = "nombre") String nombre) {
        return ico.ConsultaProgramaFormacion(nombre);
    }

    @WebMethod
    public void InscripcionUsuarioAProgramas(@WebParam(name = "programa") String programa,
            @WebParam(name = "nickname") String nickname, @WebParam(name = "fecha") Date fecha) {
        ico.InscripcionUsuarioAProgramas(programa, nickname, fecha);
    }

    @WebMethod
    public boolean VerificarPrograma(@WebParam(name = "nombre") String nombre) {
        return ico.VerificarPrograma(nombre);
    }

    @WebMethod
    public List<DTMaster> ListarProgramaDeForm() {
        return ico.ListarProgramaDeForm();
    }

    @WebMethod
    public List<DTMaster> ListarProgramasDeCurso(@WebParam(name = "curso") String curso) {
        return ico.ListarProgramasDeCurso(curso);
    }

    // ===================== Institutos, categorías, docentes, búsqueda =====================

    @WebMethod
    public List<DTMaster> ListarClase(@WebParam(name = "tipo") EnumDT tipo) {
        return ico.ListarClase(tipo);
    }

    @WebMethod
    public List<DTMaster> ListarDocentes(@WebParam(name = "instituto") String instituto) {
        return ico.ListarDocentes(instituto);
    }

    @WebMethod
    public List<DTMaster> ListarCurEdiProg(@WebParam(name = "tipo") EnumDT tipo, @WebParam(name = "dato") String dato) {
        return ico.ListarCurEdiProg(tipo, dato);
    }

    @WebMethod
    public boolean VerificarInstituto(@WebParam(name = "nombre") String nombre) {
        return ico.VerificarInstituto(nombre);
    }

    @WebMethod
    public boolean VerificarCategoria(@WebParam(name = "nombre") String nombre) {
        return ico.VerificarCategoria(nombre);
    }

    // ===================== Imágenes (se guardan en la PC del Servidor Central) =====================

    /**
     * Guarda una imagen en el Servidor Central y devuelve el nombre con el que
     * quedó guardada ("" si no se mandaron datos). Ese nombre es el que se pasa
     * luego como imgPath en las altas/modificaciones.
     *
     * @param carpeta        perfiles, cursos, programas o edicioncursos
     * @param nombreBase     base para el nombre del archivo (nickname, nombre del curso...)
     * @param nombreOriginal nombre original del archivo (para la extensión)
     * @param datos          contenido de la imagen
     */
    @WebMethod
    public String SubirImagen(@WebParam(name = "carpeta") String carpeta, @WebParam(name = "nombreBase") String nombreBase,
            @WebParam(name = "nombreOriginal") String nombreOriginal, @WebParam(name = "datos") byte[] datos)
            throws EdextWSException {
        validarCarpeta(carpeta);
        try {
            return ImagenUtil.guardar(datos, nombreOriginal, nombreBase, carpeta);
        } catch (Exception e) {
            throw error(e);
        }
    }

    /** Devuelve el contenido de una imagen guardada, o null si no existe. */
    @WebMethod
    public byte[] ObtenerImagen(@WebParam(name = "carpeta") String carpeta, @WebParam(name = "nombre") String nombre)
            throws EdextWSException {
        validarCarpeta(carpeta);
        return ImagenUtil.leer(carpeta, nombre);
    }

    /** Borra una imagen guardada (no falla si no existe). */
    @WebMethod
    public void BorrarImagen(@WebParam(name = "carpeta") String carpeta, @WebParam(name = "nombre") String nombre)
            throws EdextWSException {
        validarCarpeta(carpeta);
        ImagenUtil.borrar(carpeta, nombre);
    }

    private static void validarCarpeta(String carpeta) throws EdextWSException {
        if (carpeta == null || !CARPETAS_IMAGENES.contains(carpeta)) {
            throw new EdextWSException("Carpeta de imágenes no válida: " + carpeta);
        }
    }
}
