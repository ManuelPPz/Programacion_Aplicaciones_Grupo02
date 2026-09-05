package Manejadores;

import Classes.Curso;
import java.util.List;
import java.util.ArrayList;

import Classes.Docente;
import Classes.Edi_Usu;
import Classes.EdicionCurso;
import Classes.Usuario;
import Classes.Instituto;
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
        } finally {
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
        if (nickname == null) return null;
        for (UsuarioBase ub : misUsuarios) {
            if (nickname.equals(ub.getNickname())) {
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
                        if (c.getDocente() != null && c.getDocente().getNickname().equals(docente.getNickname())) {
                            if (c.getProgramas() != null) {
                                for (ProgramaDeFormacion pg : c.getProgramas()) {
                                    if (pg != null && pg.getNombre() != null) {
                                        auxProg.add(pg.getNombre());
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
            return new DTUsuario(ub.getNickname(), ub.getNombre(), ub.getApellido(), ub.getCorreo(), ub.getFNac(), img, auxEdi, new ArrayList<>());
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

    /*-----------------------------Funciones para la lista de ediciones de cursos del docente---------------*/
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