/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manejadores;
import java.util.List;
import java.util.ArrayList;
import Classes.Instituto;
import DTsClasses.DTMaster;
import DTsClasses.DTInstituto;
/**
 *
 * @author mateo
 */
public class ManejadorInstituto {
    List<Instituto> misInstitutos;
    private static ManejadorInstituto instance;
    public ManejadorInstituto(){
        misInstitutos = new ArrayList();
    }
    public static ManejadorInstituto GetInstance(){
        if(instance==null){
            instance = new ManejadorInstituto();
        }
        return instance;
    }
    
    
    
    public Instituto CreaInstituto(String instituto){
        return new Instituto(instituto);
    }
    
    public void Add(Instituto c){
        misInstitutos.add(c);
        //Aca se añade a la base de datos
    }
    
    public Instituto BuscarInstituto(String instituto){
        for(int i = 0;i<misInstitutos.size();i++){
            Instituto in = misInstitutos.get(i);
            if(in.getNombre().equals(instituto)){
                return in;
            }
        }
        return null;
    }

    
    
    
    public DTInstituto getDT(Instituto in){
        DTInstituto auxDT;
        auxDT = new DTInstituto(in.getNombre());
        return auxDT;
    }
    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList();
        for(int i = 0;i<misInstitutos.size();i++){
            DTMaster dt = getDT(misInstitutos.get(i));
            auxList.add(dt);
        }
        return auxList;
    }
}
