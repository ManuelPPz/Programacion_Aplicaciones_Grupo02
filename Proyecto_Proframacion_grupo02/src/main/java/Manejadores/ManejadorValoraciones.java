package Manejadores;

import Classes.Curso;
import Classes.Edi_Usu;
import Classes.Enum_Estado_inscripcion;
import Classes.Id_Valoracion;
import Classes.Usuario;
import Classes.ValoracionCurso;
import DTsClasses.DTValoracionCurso;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import util.JPAUtil;

/**
 * Caso de uso Valorar un Curso y resumen de puntajes (Requerimiento 7.6).
 *
 * Un estudiante participa en un curso si tiene una inscripción ACEPTADA en alguna de
 * sus ediciones; solo puede valorar esos cursos, con 1 a 5 estrellas y una única vez.
 */
public class ManejadorValoraciones {

    private static ManejadorValoraciones instance;

    public static ManejadorValoraciones GetInstance() {
        if (instance == null) {
            instance = new ManejadorValoraciones();
        }
        return instance;
    }

    private ManejadorValoraciones() {
    }

    /** Nombres de los cursos en los que participa el estudiante, sin repetir y en orden de inscripción. */
    public static List<String> cursosParticipa(Usuario u) {
        List<String> cursos = new ArrayList<>();
        if (u == null || u.getMisInscripciones() == null) {
            return cursos;
        }
        for (Edi_Usu eu : u.getMisInscripciones()) {
            if (eu != null && eu.getMiEstado() == Enum_Estado_inscripcion.ACEPTADO
                    && eu.getEdicion() != null && eu.getEdicion().getCurso() != null) {
                String curso = eu.getEdicion().getCurso().getNombre();
                if (curso != null && !cursos.contains(curso)) {
                    cursos.add(curso);
                }
            }
        }
        return cursos;
    }

    /** Registra la valoración; falla si no corresponde (sin participación, puntaje inválido o ya valorado). */
    public void Valorar(String nickname, String nomCurso, int puntaje) throws Exception {
        if (puntaje < ValoracionCurso.MIN_PUNTAJE || puntaje > ValoracionCurso.MAX_PUNTAJE) {
            throw new Exception("El puntaje debe estar entre " + ValoracionCurso.MIN_PUNTAJE
                    + " y " + ValoracionCurso.MAX_PUNTAJE + " estrellas.");
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario u = (nickname == null) ? null : em.find(Usuario.class, nickname);
            if (u == null) {
                throw new Exception("Solo los estudiantes pueden valorar cursos.");
            }
            Curso c = (nomCurso == null) ? null : em.find(Curso.class, nomCurso);
            if (c == null) {
                throw new Exception("No existe el curso '" + nomCurso + "'.");
            }
            if (!cursosParticipa(u).contains(c.getNombre())) {
                throw new Exception("Solo podés valorar cursos en los que participaste.");
            }
            if (em.find(ValoracionCurso.class, new Id_Valoracion(u.getNickname(), c.getNombre())) != null) {
                throw new Exception("Ya valoraste el curso '" + c.getNombre() + "'.");
            }
            em.persist(new ValoracionCurso(u, c, puntaje));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /** Resumen de puntajes del curso; {@code nickname} puede ser null (visitante). */
    public DTValoracionCurso Resumen(String nomCurso, String nickname) {
        int[] estrellas = new int[ValoracionCurso.MAX_PUNTAJE];
        int miPuntaje = 0;
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<ValoracionCurso> lista = em.createQuery(
                    "SELECT v FROM ValoracionCurso v WHERE v.curso.nombre = :curso", ValoracionCurso.class)
                    .setParameter("curso", nomCurso)
                    .getResultList();
            for (ValoracionCurso v : lista) {
                int p = v.getPuntaje();
                if (p >= ValoracionCurso.MIN_PUNTAJE && p <= ValoracionCurso.MAX_PUNTAJE) {
                    estrellas[p - 1]++;
                    if (nickname != null && v.getUsuario() != null && nickname.equals(v.getUsuario().getNickname())) {
                        miPuntaje = p;
                    }
                }
            }
        } finally {
            em.close();
        }
        return new DTValoracionCurso(nomCurso, estrellas, miPuntaje);
    }
}
