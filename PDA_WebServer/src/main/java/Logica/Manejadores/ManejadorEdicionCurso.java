package Logica.Manejadores;

import Logica.Classes.Categoria;
import Logica.Classes.EdicionCurso;
import Logica.Classes.Instituto;
import Logica.Classes.Curso;
import Logica.Classes.Docente;
import Logica.Classes.UsuarioBase;
import Logica.Classes.Edi_Usu;
import Logica.Classes.Enum_Estado_inscripcion;
import Logica.Classes.Id_EdiUsu;
import Logica.Classes.Usuario;
import Logica.DTsClasses.DTEdi_Usu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import Logica.util.JPAUtil;

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

    public EdicionCurso CrearEdicion(Instituto instituto, Curso curso, String nombre, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> docentes){
        return new EdicionCurso(nombre, instituto, curso, fInicio, fFin, cupo, fAlta, docentes);
    }
    
    public void ModificarDatos(EdicionCurso ec, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> misUsuarios){
        if (ec != null) {
            ec.ModificarDatos(fInicio, fFin, cupo, fAlta, misUsuarios);
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
            // Si el cupo total es mayor a 0, se valida que queden cupos disponibles.
            // Si cupoActual es 0 pero no hay inscripciones previas, se inicializa al cupo máximo.
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
            // Copia la fecha fInscripcion recibida en 'eu'
            Date fechaInscripcion = (eu.getFIns() != null) ? eu.getFIns() : new Date();
            Edi_Usu nuevaInscripcion = new Edi_Usu(usuarioManaged, edicionManaged, fechaInscripcion);
            nuevaInscripcion.setEstado(eu.getMiEstado());

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

        List<Docente> auxUsuarios = ec.getMisDocentes();
        List<String> auxDocentes = new ArrayList<>();
        if (auxUsuarios != null) {
            for (Docente d : auxUsuarios) {
                if (d != null) {
                    auxDocentes.add(d.getNickname());
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
            auxDTEdiUsu, auxCatStr
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
    
    public void ModificarEstadoDeInscripcion(String nicknameUsuario, Enum_Estado_inscripcion estado, String nomEdicion) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            // Corregido: e.estadoIns en lugar de e.estado
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