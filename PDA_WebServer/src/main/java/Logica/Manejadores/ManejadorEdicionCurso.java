package Logica.Manejadores;

import Logica.Classes.Categoria;
import Logica.Classes.EdicionCurso;
import Logica.Classes.Instituto;
import Logica.Classes.Curso;
import Logica.Classes.Docente;
import Logica.Classes.UsuarioBase;
import Logica.Classes.Edi_Usu;
import Logica.Classes.Enum_Estado_inscripcion;
import Logica.Classes.Usuario;
import Logica.DTsClasses.DTEdi_Usu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import Logica.util.JPAUtil; // Import de la utilería centralizada

public class ManejadorEdicionCurso {

    
    //=================Codigo de Singleton=================
    private static ManejadorEdicionCurso instance;    
    public static ManejadorEdicionCurso GetInstance(){
        if(instance == null){
            instance = new ManejadorEdicionCurso();
        }
        return instance;
    }
    
    private ManejadorEdicionCurso(){  
    }
    //=======================================================

    
    public EdicionCurso CrearEdicion(Instituto instituto, Curso curso, String nombre, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> docentes){
        EdicionCurso returnEdicion;
        returnEdicion = new EdicionCurso(nombre, instituto, curso, fInicio, fFin, cupo, fAlta,docentes);
        return returnEdicion;
    }
    
    public void ModificarDatos(EdicionCurso ec, Date fInicio, Date fFin, int cupo, Date fAlta, List<Docente> misUsuarios){
        if (ec != null) {
            ec.ModificarDatos(fInicio, fFin, cupo, fAlta, misUsuarios);
        }
        
        // Sincronizar los cambios con JPA
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

            // 1. Asegurar la relación bidireccional en memoria
            if (ec.getMisDocentes() != null) {
                for (Docente d : ec.getMisDocentes()) {
                    if (d.getEdiciones() != null && !d.getEdiciones().contains(ec)) {
                        d.getEdiciones().add(ec);
                    }
                }
            }

            // 2. Usar merge(ec) en lugar de persist(ec).
            // merge() busca los docentes por su ID (nickname), los asocia y genera las inserciones 
            // en la tabla intermedia "Docente_EdicionCurso" automáticamente.
            EdicionCurso ecManaged = em.merge(ec);

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
        if (eu == null || eu.getId() == null) {
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // 1. Guardar/Actualizar la entidad de asociación en la BD
            Edi_Usu euManaged = em.merge(eu);

            // 2. Sincronizar el modelo en memoria RAM y actualizar el cupo
            EdicionCurso edicion = eu.getId().getEdicion();
            if (edicion != null) {
                // Validar que queden cupos disponibles
                if (edicion.getCupoActual() <= 0) {
                    throw new Exception("No hay cupos disponibles para esta edición.");
                }
                edicion.setCupoActual(edicion.getCupoActual() - 1);

                // Actualizar la entidad EdicionCurso en la base de datos
                em.merge(edicion);

                // Sincronizar en memoria RAM
                edicion.AddUsuarioInscripto(euManaged);
            }

            em.getTransaction().commit();
            System.out.println(">>> [DEBUG] Inscripción registrada y cupo actualizado con éxito.");

        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new Exception("Error al guardar la inscripción: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public List<EdicionCurso> getList() {
        List<EdicionCurso> auxListEdi = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Carga explicita de la entidad Curso asociada a cada EdicionCurso
            TypedQuery<EdicionCurso> query = em.createQuery(
                "SELECT DISTINCT e FROM EdicionCurso e LEFT JOIN FETCH e.miCurso", 
                EdicionCurso.class
            );
            auxListEdi = query.getResultList();

            // Forzamos la resolucion de colecciones dentro de la conexion abierta
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
                    // Normaliza espacios y mayúsculas para evitar falsos negativos
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
        auxEuList = OrdenarListaPrioritaria(auxEuList,c.getNombre());
        if(auxEuList!=null){
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
    
    private List<Edi_Usu> OrdenarListaPrioritaria(List<Edi_Usu> listaParam, String curso){
        List<Edi_Usu> auxList = listaParam;
        for(int i = 0;i<auxList.size()-1;i++){
            for(int j = 0;j<auxList.size()-1;j++){
                Edi_Usu aux = auxList.get(j);
                Edi_Usu auxJMas = auxList.get(j+1);
                
                int rankAux = (int) (aux.getId().getUsuario().getCantMisInscripcionesRechazadas(curso)*0.5);
                int rankJMas = (int) (auxJMas.getId().getUsuario().getCantMisInscripcionesRechazadas(curso)*0.5);
                
                //En caso de que la lista no muestre los datos de la manera correcta dar vuelta las consultas de los if(si funciona borrar este comentario)
                if(rankAux < rankJMas){
                    Edi_Usu temp = aux;
                    auxList.set(j, auxList.get(j+1));
                    auxList.set(j+1,temp);
                }else {
                    if(rankAux==rankJMas){
                        if(aux.getFIns().before(auxJMas.getFIns())){
                            Edi_Usu temp = aux;
                            auxList.set(j, auxList.get(j+1));
                            auxList.set(j+1,temp);
                        }
                    }
                }
            }
        }
        return auxList;
    }
    
    public void ModificarEstadoDeInscripcion(String nicknameUsuario, Enum_Estado_inscripcion estado, String nomEdicion) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            // Usamos JPQL navegando por los nombres de las propiedades en la entidad Java
            String jpql = "UPDATE Edi_Usu e SET e.estado = :nuevoEstado WHERE e.id.miEdicionNombre = :nombreEdicion AND e.id.miUsuarioNickname = :nickname";

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