package Manejadores;

import Classes.Categoria;
import Classes.EdicionCurso;
import Classes.Instituto;
import Classes.Curso;
import Classes.Docente;
import Classes.UsuarioBase;
import Classes.Edi_Usu;
import Classes.Enum_Estado_inscripcion;
import Classes.Id_EdiUsu;
import Classes.Usuario;
import DTsClasses.DTEdi_Usu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import util.JPAUtil;

public class ManejadorEdicionCurso {

    private static ManejadorEdicionCurso instance;    
    
    public static ManejadorEdicionCurso GetInstance(){
        if(instance == null){
            instance = new ManejadorEdicionCurso();
        }
        return instance;
    }
    
    private ManejadorEdicionCurso(){  
    }

    public EdicionCurso CrearEdicion(Instituto instituto, Curso curso, String nombre, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> docentes, String imgPath){
        return new EdicionCurso(nombre, instituto, curso, fInicio, fFin, cupo, fAlta, docentes, imgPath);
    }
    
    public void ModificarDatos(EdicionCurso ec, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> misUsuarios, String imgPath){
        if (ec != null) {
            // Se agregó imgPath al llamado de modificación en la entidad
            ec.ModificarDatos(fInicio, fFin, cupo, fAlta, misUsuarios, imgPath);
        }
        
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(ec);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar curso en BD: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public void Add(EdicionCurso ec) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (ec.getMisDocentes() != null) {
                for (Docente d : ec.getMisDocentes()) {
                    if (d.getEdiciones() != null && !d.getEdiciones().contains(ec)) {
                        d.getEdiciones().add(ec);
                    }
                }
            }

            em.merge(ec);
            em.getTransaction().commit();
            System.out.println(">>> [DEBUG] Transacción COMMIT ejecutada con éxito.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar la edición de curso: " + e.getMessage());
        } finally {
            em.close();
        }
    }
   
    public EdicionCurso BuscarEdicion(String nombre) {
        EntityManager em = getEntityManager();
        EdicionCurso ec = null;
        try {
            ec = em.find(EdicionCurso.class, nombre);
        } catch (Exception e) {
            System.err.println("Error al buscar la edición de curso en el manejador: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); 
            }
        }
        return ec; 
    }
    
    public void AddUsuario(EdicionCurso ec, Docente ub){
        ec.AddUsuarios(ub);
    }
    
    public void AddUsuarioInscripto(Edi_Usu eu) throws Exception {
        if (eu == null || eu.getEdicion() == null || eu.getUsuario() == null) {
            throw new Exception("La inscripción debe contener un usuario y una edición válidos.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // 1. Obtener entidades limpias y gestionadas en la transacción actual
            EdicionCurso edicionManaged = em.find(EdicionCurso.class, eu.getEdicion().getNombre());
            Usuario usuarioManaged = em.find(Usuario.class, eu.getUsuario().getNickname());

            if (edicionManaged == null || usuarioManaged == null) {
                throw new Exception("Usuario o Edición no encontrados en la base de datos.");
            }

            // 2. Control de cupos
            int cupoMaximo = edicionManaged.getCupo();
            int cupoActual = edicionManaged.getCupoActual();

            if (cupoMaximo > 0) {
                if (cupoActual == 0 && (edicionManaged.getMisInscripciones() == null || edicionManaged.getMisInscripciones().isEmpty())) {
                    cupoActual = cupoMaximo;
                    edicionManaged.setCupoActual(cupoActual);
                }

                if (cupoActual <= 0) {
                    throw new Exception("No hay cupos disponibles para esta edición.");
                }
            }

            // 3. Verificar si ya existe la inscripción
            Id_EdiUsu idCompuesto = new Id_EdiUsu(usuarioManaged.getNickname(), edicionManaged.getNombre());
            Edi_Usu euExistente = em.find(Edi_Usu.class, idCompuesto);
            if (euExistente != null) {
                throw new Exception("El usuario ya se encuentra inscripto a esta edición.");
            }

            // 4. Instanciar la nueva entidad a guardar usando el constructor completo
            Date fechaInscripcion = (eu.getFIns() != null) ? eu.getFIns() : new Date();
            Edi_Usu nuevaInscripcion = new Edi_Usu(usuarioManaged, edicionManaged, fechaInscripcion);
            nuevaInscripcion.setEstado(eu.getMiEstado());
            nuevaInscripcion.setUrlVideo(eu.getUrlVideo());

            // 5. Persistir en la BD
            em.persist(nuevaInscripcion);

            // 6. Actualizar cupo y colecciones en memoria activa dentro de la transacción
            if (cupoMaximo > 0) {
                edicionManaged.setCupoActual(cupoActual - 1);
            }
            edicionManaged.AddUsuarioInscripto(nuevaInscripcion);
            usuarioManaged.AddEdicionCurso(nuevaInscripcion);

            em.getTransaction().commit();
            System.out.println(">>> [DEBUG] Inscripción registrada y cupo actualizado con éxito.");

        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new Exception("Error al guardar la inscripción: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<EdicionCurso> getList() {
        List<EdicionCurso> auxListEdi = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<EdicionCurso> query = em.createQuery(
                "SELECT DISTINCT e FROM EdicionCurso e LEFT JOIN FETCH e.miCurso", 
                EdicionCurso.class
            );
            auxListEdi = query.getResultList();

            for (EdicionCurso ec : auxListEdi) {
                if (ec.getCurso() != null && ec.getCurso().getCategorias() != null) {
                    ec.getCurso().getCategorias().size();
                }
                if (ec.getMisDocentes() != null) {
                    ec.getMisDocentes().size();
                }
                if (ec.getMisInscripciones() != null) {
                    ec.getMisInscripciones().size();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar las ediciones desde la BD: " + e.getMessage());
            auxListEdi = new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return auxListEdi;
    }
    
    public DTEdicionCurso getDT(EdicionCurso ec) {
        if (ec == null) return null;

        String ins = (ec.getInstituto() != null) ? ec.getInstituto().getNombre() : "";
        String cur = (ec.getCurso() != null) ? ec.getCurso().getNombre() : "";

        // Usamos List<?> para evitar ClassCastException si la entidad devuelve colecciones heterogéneas o genéricas
        List<?> auxUsuarios = ec.getMisDocentes();
        List<String> auxDocentes = new ArrayList<>();
        if (auxUsuarios != null) {
            for (Object obj : auxUsuarios) {
                if (obj instanceof Docente) {
                    Docente d = (Docente) obj;
                    if (d != null) {
                        auxDocentes.add(d.getNickname());
                    }
                }
            }
        }

        List<Edi_Usu> auxEdiUsu = ec.getMisInscripciones();
        List<DTEdi_Usu> auxDTEdiUsu = new ArrayList<>();
        if (auxEdiUsu != null) {
            for (Edi_Usu eu : auxEdiUsu) {
                if (eu != null) {
                    auxDTEdiUsu.add((DTEdi_Usu) eu.getMyDT());
                }
            }
        }

        List<String> auxCatStr = new ArrayList<>();
        if (ec.getCurso() != null && ec.getCurso().getCategorias() != null) {
            for (Categoria c : ec.getCurso().getCategorias()) {
                if (c != null) {
                    auxCatStr.add(c.getNombre());
                }
            }
        }

        return new DTEdicionCurso(
            ins, cur, ec.getNombre(), ec.getFInicio(), ec.getFFin(), 
            ec.getCupo(), ec.getCupoActual(), auxDocentes, ec.getFAlta(), 
            auxDTEdiUsu, auxCatStr, ec.getImage(), ec.getEstado()
        );
    }
    
    public List<DTMaster> getDTLIst(String curso) {
        List<DTMaster> auxList = new ArrayList<>();

        if (curso == null || curso.isBlank()) {
            return auxList;
        }

        String cursoLimpio = curso.trim();
        List<EdicionCurso> edicionesTodas = getList();

        if (edicionesTodas != null) {
            for (EdicionCurso ec : edicionesTodas) {
                if (ec != null && ec.getCurso() != null && ec.getCurso().getNombre() != null) {
                    if (ec.getCurso().getNombre().trim().equalsIgnoreCase(cursoLimpio)) {
                        DTMaster dt = getDT(ec);
                        auxList.add(dt);
                    }
                }
            }
        }

        return auxList;
    }
    
    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }
    
    public List<DTMaster> OrdenarInscripcionesPorPrioridad(List<Edi_Usu> eu, Curso c){      
        List<Edi_Usu> auxEuList = eu;
        auxEuList = OrdenarListaPrioritaria(auxEuList, c.getNombre());
        if(auxEuList != null){
            List<DTMaster> auxDt = new ArrayList<>();
            for(Edi_Usu auxEu : auxEuList){
                auxDt.add(auxEu.getMyDT());
            }
            return auxDt;
        }
        return null;
    }
    
    public List<Edi_Usu> getMisInscripciones(EdicionCurso eu){
        return eu.getMisInscripciones();
    }
    
    private List<Edi_Usu> OrdenarListaPrioritaria(List<Edi_Usu> listaParam, String curso) {
        if (listaParam == null || listaParam.isEmpty()) {
            return new ArrayList<>();
        }
        List<Edi_Usu> auxList = new ArrayList<>(listaParam);

        auxList.sort((eu1, eu2) -> {
            if (eu1 == null || eu1.getUsuario() == null) return 1;
            if (eu2 == null || eu2.getUsuario() == null) return -1;

            int rechazos1 = eu1.getUsuario().getCantMisInscripcionesRechazadas(curso);
            int rechazos2 = eu2.getUsuario().getCantMisInscripcionesRechazadas(curso);

            int rank1 = (int) (rechazos1 * 0.5);
            int rank2 = (int) (rechazos2 * 0.5);

            int rankCompare = Integer.compare(rank2, rank1);
            if (rankCompare != 0) {
                return rankCompare;
            }

            Date f1 = eu1.getFIns();
            Date f2 = eu2.getFIns();

            if (f1 == null && f2 == null) return 0;
            if (f1 == null) return 1;
            if (f2 == null) return -1;

            return f1.compareTo(f2);
        });

        return auxList;
    }
    
    /** Devuelve la inscripción del estudiante a la edición, o null si no existe. */
    public Edi_Usu BuscarInscripcion(String nickname, String nomEdicion) {
        if (nickname == null || nomEdicion == null) {
            return null;
        }
        EntityManager em = getEntityManager();
        try {
            return em.find(Edi_Usu.class, new Id_EdiUsu(nickname, nomEdicion));
        } catch (Exception e) {
            System.err.println("Error al buscar la inscripción: " + e.getMessage());
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /** Modifica la URL del video de motivación de una inscripción existente. */
    public void ModificarVideoInscripcion(String nickname, String nomEdicion, String urlVideo) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Edi_Usu eu = em.find(Edi_Usu.class, new Id_EdiUsu(nickname, nomEdicion));
            if (eu == null) {
                throw new Exception("El estudiante no está inscripto a esta edición.");
            }
            eu.setUrlVideo(urlVideo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Edición vigente de un curso: entre las ediciones que todavía no terminaron
     * (fecha de fin igual o posterior a {@code hoy}), la de fecha de alta más reciente.
     * Devuelve null si el curso no tiene ninguna.
     */
    public EdicionCurso BuscarEdicionVigente(String nomCurso, Date hoy) {
        if (nomCurso == null || nomCurso.isBlank()) {
            return null;
        }
        Date inicioDeHoy = inicioDelDia(hoy != null ? hoy : new Date());
        EdicionCurso vigente = null;
        for (EdicionCurso ec : getList()) {
            if (ec == null || ec.getCurso() == null || ec.getCurso().getNombre() == null
                    || !ec.getCurso().getNombre().trim().equalsIgnoreCase(nomCurso.trim())) {
                continue;
            }
            if (ec.getFFin() == null || ec.getFFin().before(inicioDeHoy)) {
                continue;
            }
            if (vigente == null || esMasReciente(ec, vigente)) {
                vigente = ec;
            }
        }
        return vigente;
    }

    private static boolean esMasReciente(EdicionCurso a, EdicionCurso b) {
        Date fa = a.getFAlta() != null ? a.getFAlta() : a.getFInicio();
        Date fb = b.getFAlta() != null ? b.getFAlta() : b.getFInicio();
        if (fa == null) {
            return false;
        }
        return fb == null || fa.after(fb);
    }

    private static Date inicioDelDia(Date d) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(d);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * El estudiante desiste de su inscripción: se borra y se libera el cupo.
     * Solo se puede desistir mientras la inscripción está pendiente (INSCRIPTO).
     */
    public void DesistirDeInscripcion(String nickname, String nomEdicion) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Edi_Usu eu = em.find(Edi_Usu.class, new Id_EdiUsu(nickname, nomEdicion));
            if (eu == null) {
                throw new Exception("El estudiante no está inscripto a esta edición.");
            }
            if (eu.getMiEstado() != Enum_Estado_inscripcion.INSCRIPTO) {
                throw new Exception("Solo se puede desistir de una inscripción pendiente (estado actual: "
                        + eu.getMiEstado() + ").");
            }
            EdicionCurso ec = eu.getEdicion();
            if (ec != null) {
                ec.getMisInscripciones().remove(eu);
                if (ec.getCupo() > 0) {
                    ec.setCupoActual(Math.min(ec.getCupo(), ec.getCupoActual() + 1));
                }
            }
            if (eu.getUsuario() != null && eu.getUsuario().getMisInscripciones() != null) {
                eu.getUsuario().getMisInscripciones().remove(eu);
            }
            em.remove(eu);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Guarda las calificaciones (1 a 12) de los estudiantes de la edición; las
     * listas van en paralelo (estudiantes.get(i) tiene la nota notas.get(i)).
     * Si finalizar es true, además cierra la edición. Es todo o nada.
     */
    public void IngresarNotas(String nomEdicion, List<String> estudiantes, List<Integer> notas, boolean finalizar)
            throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            EdicionCurso ec = em.find(EdicionCurso.class, nomEdicion);
            if (ec == null) {
                throw new Exception("No existe la edición '" + nomEdicion + "'.");
            }
            if (ec.isFinalizada()) {
                throw new Exception("La edición ya está finalizada; no se pueden modificar las calificaciones.");
            }
            int cant = (estudiantes == null || notas == null) ? 0 : Math.min(estudiantes.size(), notas.size());
            for (int i = 0; i < cant; i++) {
                Integer nota = notas.get(i);
                if (nota == null || nota < 1 || nota > 12) {
                    throw new Exception("La calificación de " + estudiantes.get(i) + " debe estar entre 1 y 12.");
                }
                Edi_Usu eu = em.find(Edi_Usu.class, new Id_EdiUsu(estudiantes.get(i), nomEdicion));
                if (eu == null) {
                    throw new Exception("El estudiante " + estudiantes.get(i) + " no está inscripto a esta edición.");
                }
                eu.setNota(nota);
            }
            if (finalizar) {
                ec.Finalizar();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Selección de estudiantes: el docente marca cada inscripción como ACEPTADO o
     * RECHAZADO. Cada elemento de cambios es {nickname (String), estado}. Es todo
     * o nada: si los aceptados quedan por encima del cupo de la edición no se
     * guarda ningún cambio.
     */
    public void SeleccionarEstudiantes(String nomEdicion, List<Object[]> cambios) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            EdicionCurso ec = (nomEdicion == null) ? null : em.find(EdicionCurso.class, nomEdicion);
            if (ec == null) {
                throw new Exception("No existe la edición '" + nomEdicion + "'.");
            }
            if (ec.isFinalizada()) {
                throw new Exception("La edición ya está finalizada; no se puede modificar la selección.");
            }
            if (cambios != null) {
                for (Object[] c : cambios) {
                    String nick = (String) c[0];
                    Enum_Estado_inscripcion estado = (Enum_Estado_inscripcion) c[1];
                    if (estado != Enum_Estado_inscripcion.ACEPTADO && estado != Enum_Estado_inscripcion.RECHAZADO) {
                        throw new Exception("El estado de " + nick + " debe ser Aceptada o Rechazada.");
                    }
                    Edi_Usu eu = em.find(Edi_Usu.class, new Id_EdiUsu(nick, nomEdicion));
                    if (eu == null) {
                        throw new Exception("El estudiante " + nick + " no está inscripto a esta edición.");
                    }
                    eu.setEstado(estado);
                }
            }
            int aceptados = 0;
            for (Edi_Usu eu : ec.getMisInscripciones()) {
                if (eu != null && eu.getMiEstado() == Enum_Estado_inscripcion.ACEPTADO) {
                    aceptados++;
                }
            }
            if (ec.getCupo() > 0 && aceptados > ec.getCupo()) {
                throw new Exception("Se aceptaron " + aceptados + " estudiantes y el cupo de la edición es "
                        + ec.getCupo() + ".");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void ModificarEstadoDeInscripcion(String nicknameUsuario, Enum_Estado_inscripcion estado, String nomEdicion) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            String jpql = "UPDATE Edi_Usu e SET e.estadoIns = :nuevoEstado WHERE e.id.miEdicionNombre = :nombreEdicion AND e.id.miUsuarioNickname = :nickname";

            int filasAfectadas = em.createQuery(jpql)
                    .setParameter("nuevoEstado", estado)
                    .setParameter("nombreEdicion", nomEdicion)
                    .setParameter("nickname", nicknameUsuario)
                    .executeUpdate();

            em.getTransaction().commit();
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