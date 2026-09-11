package Manejadores;

import Classes.Curso;
import java.util.List;
import java.util.ArrayList;

import Classes.Docente;
import Classes.Edi_Usu;
import Classes.EdicionCurso;
import Classes.Usuario;
import Classes.Instituto;
import Classes.Prog_Usu;
import Classes.ProgramaDeFormacion;
import Classes.UsuarioBase;

import java.util.Date;
import javax.swing.ImageIcon;

import DTsClasses.DTUsuarioBase;
import DTsClasses.DTDocente;
import DTsClasses.DTUsuario;
import DTsClasses.DTMaster;

import jakarta.persistence.EntityManager;
import util.JPAUtil;

import java.io.IOException;

/**
 * @author mateo
 */
public class ManejadorUsuario {

    private List<UsuarioBase> misUsuarios;

    // ================= Singleton =================
    private static ManejadorUsuario instance;

    public static ManejadorUsuario GetInstance() {
        if (instance == null) {
            instance = new ManejadorUsuario();
        }
        return instance;
    }

    private ManejadorUsuario() {
        misUsuarios = new ArrayList<>();
        CargarDeBaseDeDatos();
    }
    // =============================================

    public void CargarDeBaseDeDatos() {
        EntityManager em = getEntityManager();
        try {
            misUsuarios.clear();

            // 1. Cargar los docentes con sus institutos
            List<Docente> docentes = em.createQuery(
                "SELECT DISTINCT d FROM Docente d LEFT JOIN FETCH d.misInstitutos", Docente.class
            ).getResultList();

            // 2. Hidratar misCursos y misEdiciones dentro de la sesión activa
            for (Docente d : docentes) {
                if (d.getCursos() != null) {
                    d.getCursos().size();
                }
                if (d.getEdiciones() != null) {
                    d.getEdiciones().size();
                }
            }

            // 3. Cargar estudiantes haciendo JOIN FETCH sobre la propiedad 'misInscripciones'
            List<Usuario> estudiantes = em.createQuery(
                "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.misInscripciones", Usuario.class
            ).getResultList();

            misUsuarios.addAll(docentes);
            misUsuarios.addAll(estudiantes);
        }finally {
            em.close();
        }
    }

    public UsuarioBase CrearUsuario(String nick, String nombre, String apellido, String correo, boolean docente, Date fNac, List<Instituto> institutos, String imgPath) throws IOException {
        UsuarioBase returnUb;
        byte[] imgByte = null;
        if (imgPath != null && !imgPath.trim().isEmpty()) {
            imgByte = ConvertirImageIconToByte(imgPath);
        }

        if (docente) {
            returnUb = new Docente(nick, nombre, apellido, correo, fNac, imgByte, institutos);
        } else {
            returnUb = new Usuario(nick, nombre, apellido, correo, fNac, imgByte);
        }
        return returnUb;
    }

    public void ModificarDatosUsuario(String nick, String nombre, String apellido, String correo, boolean docente, Date fNac, List<Instituto> institutos, String imgPath) throws IOException {
        byte[] imgByte = null;
    if (imgPath != null && !imgPath.trim().isEmpty()) {
        imgByte = ConvertirImageIconToByte(imgPath);
    }

    UsuarioBase ub = BuscarUsuario(nick);
        if (ub != null) {
            if (docente && ub instanceof Docente d) {
                d.ModificarMisDatos(nombre, apellido, correo, fNac, imgByte, institutos);
            } else if (ub instanceof Usuario u) {
                u.ModificarMisDatos(nombre, apellido, correo, fNac, imgByte);
            }

            // Sincronizar los cambios con JPA
            EntityManager em = getEntityManager();
            try {
                em.getTransaction().begin();
                em.merge(ub);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("Error al actualizar usuario en BD: " + e.getMessage());
            } finally {
                em.close();
            }
        }
    }


    public void Add(UsuarioBase ub) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ub);

            if (ub instanceof Docente d && d.getInstitutos() != null) {
                for (Instituto inst : d.getInstitutos()) {
                    Instituto instMerged = em.find(Instituto.class, inst.getNombre());
                    if (instMerged != null) {
                        if (!instMerged.getDocentes().contains(d)) {
                            instMerged.getDocentes().add(d);
                        }
                        em.merge(instMerged);
                    }
                }
            }

            em.getTransaction().commit();
            misUsuarios.add(ub);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar el usuario: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public UsuarioBase BuscarUsuario(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return null;
        }

        String nickLimpio = nickname.trim();

        EntityManager em = getEntityManager();
        try {
            UsuarioBase ubBD = em.find(UsuarioBase.class, nickLimpio);
            if (ubBD != null) {
                // Refrescar para traer los últimos cambios
                em.refresh(ubBD);

                // HIDRATACIÓN DENTRO DE LA SESIÓN ABIERTA
                if (ubBD instanceof Docente d) {
                    // 1. Cargar Institutos
                    if (d.getInstitutos() != null) {
                        d.getInstitutos().size();
                    }
                    // 2. Cargar Ediciones (AQUÍ DABA EL ERROR)
                    if (d.getEdiciones() != null) {
                        d.getEdiciones().size();
                    }
                    // 3. Cargar Cursos y sus Programas
                    if (d.getCursos() != null) {
                        for (Curso c : d.getCursos()) {
                            em.refresh(c);
                            if (c.getProgramas() != null) {
                                c.getProgramas().size();
                            }
                        }
                    }
                } else if (ubBD instanceof Usuario u) {
                    // Cargar inscripciones para estudiantes
                    if (u.getMisInscripciones() != null) {
                        u.getMisInscripciones().size();
                    }
                }

                return ubBD;
            }
        } catch (Exception e) {
            System.err.println("Error al buscar usuario en BD: " + e.getMessage());
        } finally {
            em.close(); // Se cierra la sesión SOLO después de haber cargado todo en memoria
        }

        // Fallback a la memoria local si no está en BD
        for (UsuarioBase ub : misUsuarios) {
            if (nickLimpio.equalsIgnoreCase(ub.getNickname())) {
                return ub;
            }
        }
        return null;
    }

    public DTUsuarioBase getDT(UsuarioBase ub) {
        if (ub == null) return null;

        ImageIcon img = null;
        if (ub.getImage() != null && ub.getImage().length > 0) {
            img = ConvertirByteToImageIcon(ub.getImage());
        }

        if (ub instanceof Docente docente) {
            List<String> auxStr = new ArrayList<>();
        if (docente.getInstitutos() != null) {
            for (Instituto inst : docente.getInstitutos()) {
                if (inst != null && inst.getNombre() != null) {
                    auxStr.add(inst.getNombre());
                }
            }
        }

        List<String> auxCur = new ArrayList<>();
        List<String> auxProg = new ArrayList<>();

        if (docente.getCursos() != null) {
            for (Curso c : docente.getCursos()) {
                if (c != null && c.getNombre() != null) {
                        auxCur.add(c.getNombre());

                    // REVISIÓN Y LECTURA DE PROGRAMAS DIRECTA:
                    // Ya no filtramos por c.getDocente() porque el curso YA pertenece al docente (docente.getCursos())
                    if (c.getProgramas() != null) {
                        for (ProgramaDeFormacion pg : c.getProgramas()) {
                            if (pg != null && pg.getNombre() != null) {
                                String nomPg = pg.getNombre();
                                if (!auxProg.contains(nomPg)) {
                                    auxProg.add(nomPg);
                                }
                            }
                        }
                    }
                }
            }
        }

        List<String> auxEdi = new ArrayList<>();
        if (docente.getEdiciones() != null) {
            for (EdicionCurso ec : docente.getEdiciones()) {
                if (ec != null && ec.getNombre() != null) {
                    auxEdi.add(ec.getNombre());
                }
            }
        }

        return new DTDocente(ub.getNickname(), ub.getNombre(), ub.getApellido(), ub.getCorreo(), ub.getFNac(), auxStr, img, auxCur, auxEdi, auxProg);

        } else if (ub instanceof Usuario usuario) {
            List<String> auxEdi = new ArrayList<>();
            if (usuario.getMisInscripciones() != null) {
                for (Edi_Usu eu : usuario.getMisInscripciones()) {
                    if (eu != null && eu.getId() != null) {
                        EdicionCurso ec = eu.getId().getEdicion();
                        if (ec != null && ec.getNombre() != null) {
                            auxEdi.add(ec.getNombre());
                        }
                    }
                }
            }
            List<String> auxProg = new ArrayList<>();
            if (usuario.getMisInscripcionesPro()!= null) {
                for (Prog_Usu pu : usuario.getMisInscripcionesPro()) {
                    if (pu != null && pu.getId() != null) {
                        ProgramaDeFormacion pdf = pu.getId().getPrograma();
                        if (pdf != null && pdf.getNombre() != null) {
                            auxProg.add(pdf.getNombre());
                        }
                    }
                }
            }
            return new DTUsuario(ub.getNickname(), ub.getNombre(), ub.getApellido(), ub.getCorreo(), ub.getFNac(), img, auxEdi, auxProg);
        }
        return null;
    }

    public List<DTMaster> getDTList() {
        List<DTMaster> auxList = new ArrayList<>();
        for (UsuarioBase ub : misUsuarios) {
            auxList.add(getDT(ub));
        }
        return auxList;
    }

    public List<DTMaster> getDTList(String instituto) {
        List<DTMaster> auxList = new ArrayList<>();

        if (instituto == null || instituto.trim().isEmpty()) {
            return auxList;
        }

        String instBuscado = instituto.trim();

        for (UsuarioBase ub : misUsuarios) {
            DTMaster dt = getDT(ub);

            if (dt instanceof DTDocente auxDT) {
                List<String> auxIns = auxDT.getInstitutos();

                if (auxIns != null) {
                    for (String nomInst : auxIns) {
                        if (nomInst != null && nomInst.trim().equalsIgnoreCase(instBuscado)) {
                            auxList.add(dt);
                            break;
                        }
                    }
                }
            }
        }

        return auxList;
    }

    /*-----------------------------------Funciones para la lista de cursos del docente--------------------*/
    public void AddCurso(UsuarioBase ub, Curso c) {
        if (ub instanceof Docente docente) {
            docente.AddCurso(c);
        }
    }

    public void RemoveCurso(UsuarioBase ub, Curso c) {
        if (ub instanceof Docente docente) {
            docente.RemoveCurso(c);
        }
    }
    /*-----------------------------------------------------------------------------------------------------*/

    /*-----------------------------Funciones para la lista de ediciones de cursos de los usuarios---------------*/
    public void AddEdicion(UsuarioBase ub, EdicionCurso ec) {
        if (ub instanceof Docente docente) {
            docente.AddEdicion(ec);
        }
    }

    public void RemoveEdicion(UsuarioBase ub, EdicionCurso ec) {
        if (ub instanceof Docente docente) {
            docente.RemoveEdicion(ec);
        }
    }

    public void InscribirUsuarioAEdicion(Edi_Usu eu) {
        if (eu != null && eu.getId() != null && eu.getId().getUsuario() != null) {
            eu.getId().getUsuario().AddEdicionCurso(eu);
        }
    }
    /*-----------------------------------------------------------------------------------------------------*/
    /*-------------------------Funciones para la lista de programas de cursos de los usuarios--------------*/
    public void InscribirUsuarioAPrograma(Prog_Usu pu) {
        if (pu != null && pu.getId() != null && pu.getId().getUsuario() != null) {
            pu.getId().getUsuario().AddPrograma(pu);
        }
    }
    /*-----------------------------------------------------------------------------------------------------*/
    private byte[] ConvertirImageIconToByte(String imgPath) throws IOException {
        if (imgPath == null || imgPath.trim().isEmpty()) {
            return null;
        }

        java.io.File archivo = new java.io.File(imgPath);
        if (archivo.exists() && archivo.isFile()) {
            return java.nio.file.Files.readAllBytes(archivo.toPath());
        }

        return null;
    }

    private ImageIcon ConvertirByteToImageIcon(byte[] bytes) {
        return new ImageIcon(bytes);
    }

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }
}