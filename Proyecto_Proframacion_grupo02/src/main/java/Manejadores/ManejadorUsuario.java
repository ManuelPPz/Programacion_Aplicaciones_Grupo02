package Manejadores;

import java.util.List;
import java.util.ArrayList;

import Classes.Edi_Usu;
import Classes.Curso;
import Classes.EdicionCurso;
import Classes.Usuario;
import Classes.Instituto;
import Classes.Prog_Usu;
import Classes.ProgramaDeFormacion;
import Classes.UsuarioBase;
import Classes.Docente;

import java.util.Date;
import javax.swing.ImageIcon;

import DTsClasses.DTUsuarioBase;
import DTsClasses.DTDocente;
import DTsClasses.DTUsuario;
import DTsClasses.DTMaster;

import jakarta.persistence.EntityManager;
import util.JPAUtil;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

import java.io.IOException;

/**
 * @author mateo
 */
public class ManejadorUsuario {

    // ================= Singleton =================
    private static ManejadorUsuario instance;

    public static ManejadorUsuario GetInstance() {
        if (instance == null) {
            instance = new ManejadorUsuario();
        }
        return instance;
    }

    private ManejadorUsuario() {
    }
    // =============================================

    public UsuarioBase CrearUsuario(String nick, String nombre, String apellido, String correo, String contrasenia, boolean docente, Date fNac, Instituto instituto, String imgPath) throws IOException {
        UsuarioBase returnUb;

        if (docente) {
            returnUb = new Docente(nick, nombre, apellido, correo, contrasenia, fNac, imgPath, instituto);
        } else {
            returnUb = new Usuario(nick, nombre, apellido, correo, contrasenia, fNac, imgPath);
        }
        return returnUb;
    }

    public void ModificarDatosUsuario(String nick, String nombre, String apellido, String password, boolean docente, Date fNac, Instituto instituto, String imgPath) throws IOException {
        byte[] imgByte = null;
        if (imgPath != null && !imgPath.trim().isEmpty()) {
            imgByte = ConvertirImageIconToByte(imgPath);
        }

        UsuarioBase ub = BuscarUsuario(nick);
        if (ub != null) {
            if (docente && ub instanceof Docente d) {
                d.ModificarMisDatos(nombre, apellido, password, fNac, imgPath, instituto);
            } else if (ub instanceof Usuario u) {
                u.ModificarMisDatos(nombre, apellido, password, fNac, imgPath);
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

    // En ManejadorUsuario.java
    public void Add(UsuarioBase ub) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            if (ub instanceof Docente d && d.getInstituto() != null) {
                // Se debe asociar el objeto gestionado por el EntityManager actual
                Instituto instPersistente = em.find(Instituto.class, d.getInstituto().getNombre());
                d.setInstituto(instPersistente);
            }

            em.persist(ub);
            em.getTransaction().commit();
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
                    // 1. Cargar Instituto
                    if (d.getInstituto() != null) {
                        d.getInstituto().getNombre();
                    }
                    // 2. Cargar Ediciones
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
            em.close(); // Se cierra la sesión solo después de haber cargado todo en memoria
        }

        // Fallback a la memoria local si no está en BD
        List<UsuarioBase> auxList = getList();
        if (auxList != null) {
            for (UsuarioBase ub : auxList) {
                if (nickLimpio.equalsIgnoreCase(ub.getNickname())) {
                    return ub;
                }
            }
        }
        return null;
    }

    public DTUsuarioBase getDT(UsuarioBase ub) {
        if (ub == null) return null;


        if (ub instanceof Docente docente) {
            String auxInsStr = (docente.getInstituto() != null) ? docente.getInstituto().getNombre() : "";

            List<String> auxCur = new ArrayList<>();
            List<String> auxProg = new ArrayList<>();

            if (docente.getCursos() != null) {
                for (Curso c : docente.getCursos()) {
                    if (c != null && c.getNombre() != null) {
                        auxCur.add(c.getNombre());

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

            return new DTDocente(ub.getNickname(), ub.getNombre(), ub.getApellido(), ub.getCorreo(), ub.getPassword(), ub.getFNac(), auxInsStr, ub.getImage(), auxCur, auxEdi, auxProg);

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
            if (usuario.getMisInscripcionesPro() != null) {
                for (Prog_Usu pu : usuario.getMisInscripcionesPro()) {
                    if (pu != null && pu.getId() != null) {
                        ProgramaDeFormacion pdf = pu.getId().getPrograma();
                        if (pdf != null && pdf.getNombre() != null) {
                            auxProg.add(pdf.getNombre());
                        }
                    }
                }
            }
            return new DTUsuario(ub.getNickname(), ub.getNombre(), ub.getApellido(), ub.getCorreo(), ub.getPassword(), ub.getFNac(), ub.getImage(), auxEdi, auxProg);
        }
        return null;
    }

    public List<UsuarioBase> getList() {
        List<UsuarioBase> auxList = new ArrayList<>();
        EntityManager em = getEntityManager();
        try {
            // Usamos 'd.miInstituto' que coincide exactamente con el atributo de Docente
            List<Docente> docentes = em.createQuery(
                "SELECT DISTINCT d FROM Docente d LEFT JOIN FETCH d.miInstituto", Docente.class
            ).getResultList();

            // Cargar colecciones para evitar LazyInitializationException fuera del em
            for (Docente d : docentes) {
                if (d.getCursos() != null) {
                    d.getCursos().size();
                }
                if (d.getEdiciones() != null) {
                    d.getEdiciones().size();
                }
            }

            // Cargar estudiantes
            List<Usuario> estudiantes = em.createQuery(
                "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.misInscripciones", Usuario.class
            ).getResultList();

            auxList.addAll(docentes);
            auxList.addAll(estudiantes);
        } finally {
            em.close();
        }
        return auxList;
    }

    public List<DTMaster> getDTList() {
        List<UsuarioBase> auxListUsu = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if (auxListUsu != null) {
            for (UsuarioBase ub : auxListUsu) {
                auxList.add(getDT(ub));
            }
        }
        return auxList;
    }

    public List<DTMaster> getDTList(String instituto) {
        List<UsuarioBase> auxListUsu = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if (auxListUsu != null) {
            if (instituto == null || instituto.trim().isEmpty()) {
                return auxList;
            }

            String instBuscado = instituto.trim();

            for (UsuarioBase ub : auxListUsu) {
                DTMaster dt = getDT(ub);

                if (dt instanceof DTDocente auxDT) {
                    String auxIns = auxDT.getInstituto();

                    if (auxIns != null && auxIns.trim().equalsIgnoreCase(instBuscado)) {
                        auxList.add(dt);
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

    /*-------------------------Funciones para la lista de programas de cursos de los usuarios--------------*/
    public void InscribirUsuarioAPrograma(Prog_Usu pu) {
        if (pu != null && pu.getId() != null && pu.getId().getUsuario() != null) {
            pu.getId().getUsuario().AddPrograma(pu);
        }
    }

    /*-------------------------Funciones para la lista de usuarios seguidos--------------------------------*/
    public void SeguirUsuarios(UsuarioBase ub1, UsuarioBase ub2) {
        ub1.SeguirUsuario(ub2);
    }

    public void DejarDeSeguir(UsuarioBase ub1, UsuarioBase ub2) {
        ub1.DejarDeSeguir(ub2);
    }

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


    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }

    public UsuarioBase BusquedaAvanzada(String nicknameOCorreo, String password) {
        if (nicknameOCorreo == null || nicknameOCorreo.trim().isEmpty() || password == null) {
            return null;
        }

        String valorLimpio = nicknameOCorreo.trim();
        EntityManager em = getEntityManager();

        try {
            String jpql = "SELECT u FROM UsuarioBase u WHERE (u.nickname = :valor OR u.correo = :valor) AND u.contrasenia = :pass";

            TypedQuery<UsuarioBase> query = em.createQuery(jpql, UsuarioBase.class);
            query.setParameter("valor", valorLimpio);
            query.setParameter("pass", password);

            List<UsuarioBase> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                return resultados.get(0);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar usuario en BD: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        List<UsuarioBase> misUsuarios = getList();
        if (misUsuarios != null) {
            for (UsuarioBase ub : misUsuarios) {
                boolean coincideIdentificador = valorLimpio.equals(ub.getNickname()) || valorLimpio.equals(ub.getCorreo());
                boolean coincidePass = password.equals(ub.getPassword());

                if (coincideIdentificador && coincidePass) {
                    return ub;
                }
            }
        }

        return null;
    }

    public UsuarioBase VerificarUsuario(String nomCorreo) {
        EntityManager em = getEntityManager();

        try {
            String jpql = "SELECT u FROM UsuarioBase u WHERE u.nickname = :criterio OR u.correo = :criterio";
            return em.createQuery(jpql, UsuarioBase.class).setParameter("criterio", nomCorreo).getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}