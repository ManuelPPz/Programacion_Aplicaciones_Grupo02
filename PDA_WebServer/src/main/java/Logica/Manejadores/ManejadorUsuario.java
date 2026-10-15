package Logica.Manejadores;

import java.util.List;
import java.util.ArrayList;

import Logica.Classes.Edi_Usu;
import Logica.Classes.Curso;
import Logica.Classes.EdicionCurso;
import Logica.Classes.Usuario;
import Logica.Classes.Instituto;
import Logica.Classes.Prog_Usu;
import Logica.Classes.ProgramaDeFormacion;
import Logica.Classes.UsuarioBase;
import Logica.Classes.Docente;

import java.util.Date;
import javax.swing.ImageIcon;

import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTUsuario;
import Logica.DTsClasses.DTMaster;

import jakarta.persistence.EntityManager;
import Logica.util.JPAUtil;
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

        UsuarioBase ub = BuscarUsuario(nick);
        if (ub != null) {
            if (docente && ub instanceof Docente d) {
                if ("".equals(password)) {
                    d.ModificarMisDatos(nombre, apellido, d.getPassword(), fNac, imgPath, instituto);
                } else {
                    d.ModificarMisDatos(nombre, apellido, password, fNac, imgPath, instituto);
                }
            } else if (ub instanceof Usuario u) {
                if ("".equals(password)) {
                    u.ModificarMisDatos(nombre, apellido, u.getPassword(), fNac, imgPath);
                } else {
                    u.ModificarMisDatos(nombre, apellido, password, fNac, imgPath);
                }

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

            if (ub instanceof Docente d && d.getInstituto() != null) {
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
                em.refresh(ubBD);

                if (ubBD.getSeguidos() != null) {
                    ubBD.getSeguidos().size();
                }

                if (ubBD instanceof Docente d) {
                    if (d.getInstituto() != null) {
                        d.getInstituto().getNombre();
                    }
                    if (d.getEdiciones() != null) {
                        d.getEdiciones().size();
                    }
                    if (d.getCursos() != null) {
                        for (Curso c : d.getCursos()) {
                            em.refresh(c);
                            if (c.getProgramas() != null) {
                                c.getProgramas().size();
                            }
                        }
                    }
                } else if (ubBD instanceof Usuario u) {
                    if (u.getMisInscripciones() != null) {
                        u.getMisInscripciones().size();
                        for (Edi_Usu eu : u.getMisInscripciones()) {
                            if (eu.getEdicion() != null) {
                                eu.getEdicion().getNombre();
                            }
                        }
                    }
                    if (u.getMisInscripcionesPro() != null) {
                        u.getMisInscripcionesPro().size();
                        for (Prog_Usu pu : u.getMisInscripcionesPro()) {
                            if (pu.getId() != null && pu.getId().getPrograma() != null) {
                                pu.getId().getPrograma().getNombre();
                            }
                        }
                    }
                }

                return ubBD;
            }
        } catch (Exception e) {
            System.err.println("Error al buscar usuario en BD: " + e.getMessage());
        } finally {
            em.close();
        }

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
        if (ub == null) {
            return null;
        }

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
                        EdicionCurso ec = eu.getEdicion();
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
            // Consultamos todos los usuarios base para evitar cruces de cast en las consultas específicas
            List<UsuarioBase> todos = em.createQuery(
                    "SELECT u FROM UsuarioBase u", UsuarioBase.class
            ).getResultList();

            if (todos != null) {
                for (UsuarioBase ub : todos) {
                    if (ub instanceof Docente d) {
                        if (d.getCursos() != null) {
                            d.getCursos().size();
                        }
                        if (d.getEdiciones() != null) {
                            d.getEdiciones().size();
                        }
                    } else if (ub instanceof Usuario u) {
                        if (u.getMisInscripciones() != null) {
                            u.getMisInscripciones().size();
                            for (Edi_Usu eu : u.getMisInscripciones()) {
                                if (eu.getEdicion() != null) {
                                    eu.getEdicion().getNombre();
                                }
                            }
                        }
                        if (u.getMisInscripcionesPro() != null) {
                            u.getMisInscripcionesPro().size();
                            for (Prog_Usu pu : u.getMisInscripcionesPro()) {
                                if (pu.getId() != null && pu.getId().getPrograma() != null) {
                                    pu.getId().getPrograma().getNombre();
                                }
                            }
                        }
                    }
                    auxList.add(ub);
                }
            }

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
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
        if (eu != null && eu.getId() != null && eu.getUsuario() != null) {
            eu.getUsuario().AddEdicionCurso(eu);
        }
    }

    public void InscribirUsuarioAPrograma(Prog_Usu pu) {
        if (pu != null && pu.getId() != null && pu.getId().getUsuario() != null) {
            pu.getId().getUsuario().AddPrograma(pu);
        }
    }

    public void SeguirUsuarios(UsuarioBase ub1, UsuarioBase ub2) {
        if (ub1 != null && ub2 != null) {
            EntityManager em = getEntityManager();
            try {
                em.getTransaction().begin();
                
                UsuarioBase user1 = em.find(UsuarioBase.class, ub1.getNickname());
                UsuarioBase user2 = em.find(UsuarioBase.class, ub2.getNickname());

                if (user1 != null && user2 != null) {
                    user1.SeguirUsuario(user2);
                    em.merge(user1);
                    
                    em.getTransaction().commit();
                } else {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                }
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
            }
        }
    }

    public void DejarDeSeguir(UsuarioBase ub1, UsuarioBase ub2) {
        if (ub1 != null && ub2 != null) {
            EntityManager em = getEntityManager();
            try {
                em.getTransaction().begin();
                
                UsuarioBase user1 = em.find(UsuarioBase.class, ub1.getNickname());
                UsuarioBase user2 = em.find(UsuarioBase.class, ub2.getNickname());

                if (user1 != null && user2 != null) {
                    user1.DejarDeSeguir(user2);
                    em.merge(user1);
                    
                    em.getTransaction().commit();
                } else {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                }
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
            }
        }
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