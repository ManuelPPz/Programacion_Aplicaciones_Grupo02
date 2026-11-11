package Manejadores;


import DTsClasses.DTUnitValoracion;
import java.util.List;
import java.util.ArrayList;

import Classes.Categoria;
import Classes.Curso;
import Classes.EdicionCurso;
import Classes.Id_Valoracion;
import Classes.Instituto;
import Classes.ProgramaDeFormacion;
import Classes.Usuario;
import Classes.UsuarioBase;
import Classes.ValoracionCurso;

import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import DTsClasses.DTValoracionCurso;
import DTsClasses.ExtrasDT.DTUnitValoracion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import util.JPAUtil; // Import de la clase utilitaria

public class ManejadorCursos {
    //=================Codigo de Singleton=================
    private static ManejadorCursos instance;    
    public static ManejadorCursos GetInstance(){
        if(instance == null){
            instance = new ManejadorCursos();
        }
        return instance;
    }
    
    private ManejadorCursos() {
    }
    //=======================================================
    
    public Curso CrearCurso(Instituto instituto, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, Date fAlta, List<String> previas, UsuarioBase ub, List<Categoria> categorias, String imgPath){
        Curso returnCurso;
        List<Curso> auxPrevias = new ArrayList<>();
        if (previas != null) {
            for (int i = 0; i < previas.size(); i++) {
                Curso previa = BuscarCurso(previas.get(i));
                if (previa != null) {
                    auxPrevias.add(previa);
                }
            }
        }
        returnCurso = new Curso(instituto, nombre, descripcion, duracion, cantHoras, cantCreditos, URL, fAlta, auxPrevias, ub, categorias, imgPath);
        return returnCurso;
    }

    public void ModificarCurso(Curso c, String descripcion, int duracion, float cantHoras, int cantCreditos, String URL, Date fAlta, List<String> previas, UsuarioBase ub, List<Categoria> categorias, String imgPath){
        if (c == null) return;

        List<Curso> auxPrevias = new ArrayList<>();
        if (previas != null) {
            for (int i = 0; i < previas.size(); i++) {
                Curso previa = BuscarCurso(previas.get(i));
                if (previa != null) {
                    auxPrevias.add(previa);
                }
            }
        }
        
        c.ModificarMisDatos(descripcion, duracion, cantHoras, cantCreditos, URL, fAlta, auxPrevias, ub, categorias, imgPath);
        
        // Sincronizar los cambios con JPA
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(c);
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
    
    public void Add(Curso c) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            System.err.println("Causa raiz: " + (e.getCause() != null ? e.getCause().getMessage() : "Desconocida"));
            if (e.getCause() != null && e.getCause().getCause() != null) {
                System.err.println("Detalle SQL: " + e.getCause().getCause().getMessage());
            }

            throw new Exception("Error al guardar el curso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public Curso BuscarCurso(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        Curso c = null;
        try {
            List<Curso> resultados = em.createQuery(
                "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.previas WHERE c.nombre = :nombre", Curso.class)
                .setParameter("nombre", nombre)
                .getResultList();

            if (!resultados.isEmpty()) {
                c = resultados.get(0);

                em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misEdiciones WHERE c = :curso", Curso.class)
                    .setParameter("curso", c)
                    .getSingleResult();

                em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misProgramas WHERE c = :curso", Curso.class)
                    .setParameter("curso", c)
                    .getSingleResult();

                em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misCategorias WHERE c = :curso", Curso.class)
                    .setParameter("curso", c)
                    .getSingleResult();
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el curso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close(); 
        }
        return c; 
    }
    
    public List<Curso> getList(){
        List<Curso> auxListCur = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            List<Curso> resultados = em.createQuery(
                "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misEdiciones", Curso.class
            ).getResultList();

            if (!resultados.isEmpty()) {
                resultados = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.previas WHERE c IN :cursos", Curso.class
                ).setParameter("cursos", resultados)
                 .getResultList();
            }
            if (!resultados.isEmpty()) {
                resultados = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misProgramas WHERE c IN :cursos", Curso.class
                ).setParameter("cursos", resultados)
                 .getResultList();
            }
            if (!resultados.isEmpty()) {
                resultados = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.misCategorias WHERE c IN :cursos", Curso.class
                ).setParameter("cursos", resultados)
                 .getResultList();
            }

            em.getTransaction().commit();

            auxListCur = resultados;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al cargar cursos desde la BD: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return auxListCur;
    }

    public DTCurso getDT(Curso c){
        if (c == null) return null;

        String ins = (c.getInstituto() != null) ? c.getInstituto().getNombre() : "";
        
        List<Curso> auxPrevias = c.getPrevias();
        List<String> auxPreviasStr = new ArrayList<>();
        if (auxPrevias != null) {
            for(Curso previa : auxPrevias){
                if (previa != null) {
                    auxPreviasStr.add(previa.getNombre());
                }
            }
        }

        List<EdicionCurso> auxEdiciones = c.getEdiciones();
        List<String> auxEdicionesStr = new ArrayList<>();
        if (auxEdiciones != null) {
            for(EdicionCurso edicion : auxEdiciones){
                if (edicion != null) {
                    auxEdicionesStr.add(edicion.getNombre());
                }
            }
        }
        
        List<ProgramaDeFormacion> auxProgramas = c.getProgramas();
        List<String> auxProgramasStr = new ArrayList<>();
        if(auxProgramas != null){
            for(ProgramaDeFormacion programa : auxProgramas){
                if (programa != null) {
                    auxProgramasStr.add(programa.getNombre());
                }
            }
        }

        List<Categoria> auxCategoria = c.getCategorias();
        List<String> auxCategoriaStr = new ArrayList<>();
        if(auxCategoria != null){
            for(Categoria cat : auxCategoria){
                if(cat != null){
                    auxCategoriaStr.add(cat.getNombre());
                }
            }
        }
        
        List<ValoracionCurso> auxValoracion = c.getValoraciones();
        List<DTValoracionCurso> auxValoracionVal = new ArrayList<>();
        if(auxValoracion!=null){
            for(ValoracionCurso val : auxValoracion){
                if(val!=null){
                    auxValoracionVal.add((DTValoracionCurso) val.getMyDT());
                }
            }
        }
        
        
        

        String docenteNick = (c.getDocente() != null) ? c.getDocente().getNickname() : "";

        return new DTCurso(
            ins,
            c.getNombre(),
            c.getDescripcion(),
            c.getDuracion(),
            c.getCantHoras(),
            c.getCantCreditos(),
            c.getURL(),
            c.getFAlta(),
            auxPreviasStr,
            auxEdicionesStr,
            auxProgramasStr,
            auxCategoriaStr,
            docenteNick,
            c.getImage(),
            auxValoracionVal
        );
    }

    public List<DTMaster> getDTList(){
        List<Curso> auxListCur = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if(auxListCur != null){
            for(int i = 0; i < auxListCur.size(); i++){
                DTMaster dt = getDT(auxListCur.get(i));
                if (dt != null) {
                    auxList.add(dt);
                }
            }
        }
        
        return auxList;
    }

    public List<DTMaster> getDTListIns(String instituto){
        List<Curso> auxListCur = getList();
        List<DTMaster> auxList = new ArrayList<>();
        if(auxListCur != null){
            for(int i = 0; i < auxListCur.size(); i++){
                Curso c = auxListCur.get(i);
                if(c != null && c.getInstituto() != null && c.getInstituto().getNombre().equals(instituto)){
                    DTMaster dt = getDT(c);
                    if (dt != null) {
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
    
    public List<DTMaster> getDTListCat(String categoria){
        List<Curso> auxList = getList();
        List<DTMaster> auxReturnList = new ArrayList<>();
        for(int i = 0; i < auxList.size(); i++){
            for(Categoria c : auxList.get(i).getCategorias()){
                if(c.getNombre() != null && c.getNombre().equals(categoria)){
                    auxReturnList.add(getDT(auxList.get(i)));
                }
            }
        }
        return auxReturnList;
    }
    
    public void AddValoracionUsuario(ValoracionCurso vc) throws Exception{
        if (vc == null || vc.getCurso()== null || vc.getUsuario() == null) {
            throw new Exception("La inscripción debe contener un usuario y una edición válidos.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // 1. Obtener entidades limpias y gestionadas en la transacción actual
            Curso cursoManaged = em.find(Curso.class, vc.getCurso().getNombre());
            Usuario usuarioManaged = em.find(Usuario.class, vc.getUsuario().getNickname());

            if (cursoManaged == null || usuarioManaged == null) {
                throw new Exception("Usuario o Edición no encontrados en la base de datos.");
            }


            // 3. Verificar si ya existe la inscripción
            Id_Valoracion idCompuesto = new Id_Valoracion(usuarioManaged.getNickname(), cursoManaged.getNombre());
            ValoracionCurso euExistente = em.find(ValoracionCurso.class, idCompuesto);
            if (euExistente != null) {
                throw new Exception("El usuario ya se encuentra inscripto a esta edición.");
            }

            // 4. Instanciar la nueva entidad a guardar usando el constructor completo
            ValoracionCurso nuevaValoracion = new ValoracionCurso(usuarioManaged, cursoManaged, vc.getValoracion());

            // 5. Persistir en la BD
            em.persist(nuevaValoracion);

            cursoManaged.AddValoracion(nuevaValoracion);
            usuarioManaged.AddValoracion(nuevaValoracion);

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
    
    
    public DTUnitValoracion getValoracionCurso(Curso c){
        List<ValoracionCurso> auxListVal = c.getValoraciones();
        int[] auxArrayInts = CantValPorEstrellas(auxListVal); 
        float promedio = calcPromedioValoraciones(auxArrayInts,auxListVal.size());
        
        return new DTUnitValoracion(auxListVal.size(),auxArrayInts,promedio);
    }
    
    private int[] CantValPorEstrellas(List<ValoracionCurso> auxList){
        final int CANT_ESTRELLAS = 5;
        
        if(auxList==null){
            return null;
        }
        int[] estrellas = new int[CANT_ESTRELLAS]; 
        
        for(ValoracionCurso vc : auxList){
            if(vc!=null){
                int estrella = (int)vc.getValoracion();
                if(estrella>0 && estrella <=estrellas.length  ){
                    estrellas[estrella-1]++;
                }
            }
            
        }
        return estrellas;
    }
    
    float calcPromedioValoraciones(int[] arrayCantEstrellas, int cantTotalVal){
        float promedio = 0;
        for(int i = 0;i<arrayCantEstrellas.length;i++){
            int multiplicador = i; multiplicador++;
            int multiplicacion = arrayCantEstrellas[i]*multiplicador;
            promedio+=multiplicacion;
        }
        return promedio/cantTotalVal;
    }
    
}