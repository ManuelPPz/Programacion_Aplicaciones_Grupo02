package Manejadores;
import Classes.EdicionCurso;
import Classes.Instituto;
import Classes.Curso;
import Classes.Docente;
import Classes.UsuarioBase;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
public class ManejadorEdicionCurso {
    List<EdicionCurso> misEdiciones;
    //=================Codigo de Singleton=================
    private static ManejadorEdicionCurso instance;    
    public static ManejadorEdicionCurso GetInstance(){
        if(instance==null){
            instance = new ManejadorEdicionCurso();
        }
        return instance;
        
    }
    private ManejadorEdicionCurso(){  
        misEdiciones = new ArrayList();
    }
    //=======================================================
    private void CargarDeBaseDeDatos(){
        //Aca cargas misUsuarios con lo que esta en la base de datos
    }
    public EdicionCurso CrearEdicion(Instituto instituto, Curso curso, String nombre, Date fInicio,Date fFin, int cupo, Date fAlta){
        EdicionCurso returnEdicion;
        returnEdicion = new EdicionCurso(nombre, instituto,curso,fInicio,fFin,cupo,fAlta);
        return returnEdicion;
    }
    public void ModificarDatos(String nombre,Date fInicio,Date fFin, int cupo, Date fAlta,List<UsuarioBase> misUsuarios){
        EdicionCurso ec = BuscarEdicion(nombre);
        ec.ModificarDatos(fInicio, fFin, cupo, fAlta, misUsuarios);
    }
    public void Add(EdicionCurso ec){
        misEdiciones.add(ec);
        //Aca se añade a la base de datos
    }
    
    public EdicionCurso BuscarEdicion(String nombre){
        for(int i = 0;i<misEdiciones.size();i++){
            EdicionCurso ec = misEdiciones.get(i);
            if(ec.getNombre().equals(nombre)){
                return ec;
            }
        }
        return null;
    }
    public void AddUsuario(EdicionCurso ec, UsuarioBase ub){
        ec.AddUsuarios(ub);
    }
    
    public DTEdicionCurso getDT(EdicionCurso ec){
        DTEdicionCurso auxDT;
        String ins = ec.getInstituto().getNombre();
        String cur = ec.getCurso().getNombre();
        List<UsuarioBase>auxUsuarios = ec.getMisUsuarios();
        List<String> auxDocentes = new ArrayList();
        for(int i=0;i<auxUsuarios.size();i++){
            UsuarioBase ub = auxUsuarios.get(i);
            if(ub instanceof Docente d){
                auxDocentes.add(d.getNombre());
            }
            
        }
        auxDT = new DTEdicionCurso(ins,cur,ec.getNombre(),ec.getFInicio(),ec.getFFin(), ec.getCupo(),auxDocentes,ec.getFAlta());
        return auxDT;
    }
    
    public List<DTMaster> getDTLIst(String curso){
        List<DTMaster> auxList = new ArrayList();
        for(int i = 0;i<misEdiciones.size();i++){
            EdicionCurso ec = misEdiciones.get(i);
            if(ec.getCurso().getNombre().equals(curso)){
                DTMaster dt = getDT(ec);
                auxList.add(dt);
            }
        }
        return auxList;
    }
}
