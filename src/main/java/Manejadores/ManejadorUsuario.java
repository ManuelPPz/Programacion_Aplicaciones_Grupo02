package Manejadores;

import Classes.Curso;
import java.util.List;
import java.util.ArrayList;

import Classes.Docente;
import Classes.Usuario;
import Classes.Instituto;
import Classes.UsuarioBase;

import java.util.Date;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

import DTsClasses.DTUsuarioBase;
import DTsClasses.DTDocente;
import DTsClasses.DTUsuario;
import DTsClasses.DTMaster;

import jakarta.persistence.EntityManager;
import util.JPAUtil; // Import de la utilería centralizada

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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

            // 1. Cargar docentes forzando la hidratación de sus institutos
            List<Docente> docentes = em.createQuery(
                "SELECT DISTINCT d FROM Docente d LEFT JOIN FETCH d.misInstitutos", Docente.class
            ).getResultList();

            // 2. Cargar estudiantes
            List<Usuario> estudiantes = em.createQuery(
                "SELECT u FROM Usuario u", Usuario.class
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
            em.persist(ub); // Persiste el Usuario/Docente

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
            if (ub.getNickname().equalsIgnoreCase(nickname.trim())) {
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
            return new DTDocente(
                ub.getNickname(),
                ub.getNombre(),
                ub.getApellido(),
                ub.getCorreo(),
                ub.getFNac(),
                auxStr,
                img
            );
        } else {
            return new DTUsuario(
                ub.getNickname(),
                ub.getNombre(),
                ub.getApellido(),
                ub.getCorreo(),
                ub.getFNac(),
                img
            );
        }
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

    public void AddCurso(UsuarioBase ub, Curso c){
        if(ub instanceof Docente docente){
            docente.AddCurso(c);
        }
    }

    public void RemoveCurso(UsuarioBase ub, Curso c){
        if(ub instanceof Docente docente){
            docente.RemoveCurso(c);
        }
    }

    private byte[] ConvertirImageIconToByte(String imgPath) throws IOException {
        ImageIcon img = new ImageIcon(imgPath);
        String formato = "png";
        if (imgPath.endsWith(".jpg") || imgPath.endsWith(".jpeg")) {
            formato = "jpg";
        }

        BufferedImage bi = new BufferedImage(
            Math.max(1, img.getIconWidth()), 
            Math.max(1, img.getIconHeight()), 
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = bi.createGraphics();
        img.paintIcon(null, g, 0, 0);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, formato, baos);
        return baos.toByteArray();
    }

    private ImageIcon ConvertirByteToImageIcon(byte[] bytes) {
        return new ImageIcon(bytes);
    }

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }
}