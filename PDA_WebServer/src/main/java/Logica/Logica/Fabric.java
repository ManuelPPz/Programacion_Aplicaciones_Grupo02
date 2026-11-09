/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Logica;

/**
 *
 * @author mateo
 */
public class Fabric {
    private static Fabric instance;
    public Fabric(){
    }
    
    public static Fabric GetInstance(){
        if(instance==null){
            instance = new Fabric();
        }
        return instance;
    }
    
   /** El web no tiene lógica propia: todo pasa por el Web Service del Servidor Central. */
   public IController GetIController(){
       return new Logica.ws.ControllerWS();
   }
}
