/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manejadores;
import Classes.UsuarioBase;
import java.util.List;
import java.util.ArrayList;
import Classes.Docente;
import Classes.Usuario;
import java.util.Date;

import DTsClasses.DTUsuarioBase;
import DTsClasses.DTDocente;
import DTsClasses.DTUsuario;
/**
 *
 * @author mateo
 */
public class ManejadorUsuario {
    
    List<UsuarioBase> misUsuarios;
    private static ManejadorUsuario instance;
    public ManejadorUsuario(){
        misUsuarios = new ArrayList();
    }
    
    private void CargarDeBaseDeDatos(){
        //Aca cargas misUsuarios con lo que esta en la base de datos
    }
    
    public static ManejadorUsuario GetInstance(){
        if(instance==null){
            instance = new ManejadorUsuario();
        }
        return instance;
    }
    
    public UsuarioBase CrearUsuario(String nick, String nombre, String apellido, String correo, boolean docente,Date fNac/*falta lista de institutos*/){
        UsuarioBase returnUb;
        if(docente){
            returnUb = new Docente(nick, nombre, apellido, correo, fNac/*falta lista de institutos*/);
        }else{
            returnUb = new Usuario(nick, nombre, apellido, correo, fNac);
        }
        return returnUb;
    }
    public void Add(UsuarioBase ub){
        misUsuarios.add(ub);
        //Aca se añade a la base de datos
    }
    public List<String> MisUsuarios(){
        List<String> auxList = new ArrayList();
        for(int i = 0;i<misUsuarios.size();i++){
            auxList.add(misUsuarios.get(i).getNickname());
        }
        return auxList;
    }
    public UsuarioBase BuscarUsuario(String nickname){
        for(int i = 0;i<misUsuarios.size();i++){
            if(misUsuarios.get(i).getNickname().equals(nickname)){
                return misUsuarios.get(i);
            }
        }
        return null;
    }
    
    public DTUsuarioBase getDT(UsuarioBase ub){
        if(ub instanceof Docente){
            DTUsuarioBase auxDT = new DTDocente(ub.getNickname(),ub.getNombre(),ub.getApellido(),ub.getCorreo(),ub.getFNac(),null);
            return auxDT;
        }else if(ub instanceof Usuario){
            
        }
        return null;
    }
    
}
