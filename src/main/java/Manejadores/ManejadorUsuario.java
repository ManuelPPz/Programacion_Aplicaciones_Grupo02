/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manejadores;
import java.util.List;
import java.util.ArrayList;

import Classes.Docente;
import Classes.Usuario;
import Classes.Instituto;
import Classes.UsuarioBase;

import java.util.Date;
import javax.swing.ImageIcon;

import javax.imageio.ImageIO;

import DTsClasses.DTUsuarioBase;
import DTsClasses.DTDocente;
import DTsClasses.DTUsuario;
import DTsClasses.DTMaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
/**
 *
 * @author mateo
 */
public class ManejadorUsuario {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");
    List<UsuarioBase> misUsuarios;
    
    
    //=================Codigo de Singleton=================
    private static ManejadorUsuario instance;
    public static ManejadorUsuario GetInstance(){
        if(instance==null){
            instance = new ManejadorUsuario();
        }
        return instance;
    }
    private ManejadorUsuario(){
        misUsuarios = new ArrayList();
    }
    //======================================================
    
    private void CargarDeBaseDeDatos(){
        //Aca cargas misUsuarios con lo que esta en la base de datos
    }
    
    
    public UsuarioBase CrearUsuario(String nick, String nombre, String apellido, String correo, boolean docente,Date fNac, List<Instituto>institutos, String imgPath) throws IOException{
        UsuarioBase returnUb;
        byte[] imgByte = null;
        if(!imgPath.isEmpty()){
            imgByte = ConvertirImageIconToByte(imgPath);
        }
        
        if(docente){
            returnUb = new Docente(nick, nombre, apellido, correo, fNac,imgByte, institutos);
        }else{
            returnUb = new Usuario(nick, nombre, apellido, correo, fNac,imgByte);
        }
        return returnUb;
    }
    
    
    public void ModificarDatosUsuario(String nick, String nombre, String apellido, String correo, boolean docente,Date fNac, List<Instituto>institutos, String imgPath) throws IOException{
        byte[] imgByte = null;
        if(!imgPath.isEmpty()){
            imgByte = ConvertirImageIconToByte(imgPath);
        }
        
        UsuarioBase ubModificar;
        if(docente){
            Docente d = (Docente)BuscarUsuario(nick);
            d.ModificarMisDatos(nombre, apellido, correo, fNac, imgByte,institutos);
        }else{
            Usuario u = (Usuario)BuscarUsuario(nick);
            u.ModificarMisDatos(nombre, apellido, correo, fNac, imgByte);
        }
    }
    
    
    public void Add(UsuarioBase ub)throws Exception{
        misUsuarios.add(ub);
        //Aca se añade a la base de datos
            EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(ub); //Insertar objeto en la bd
            em.getTransaction().commit();
        } catch (Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new Exception("Error al guardar el programa" + e.getMessage());
        }finally{
            em.close();
        }
    }
    

    public UsuarioBase BuscarUsuario(String nickname){
        for(int i = 0;i<misUsuarios.size();i++){
            if(misUsuarios.get(i).getNickname().equals(nickname)){
                return misUsuarios.get(i);
            }
        }
        return null;
    }
    
    
    
    /*public List<String> MisUsuarios(){
        List<String> auxList = new ArrayList();
        for(int i = 0;i<misUsuarios.size();i++){
            auxList.add(misUsuarios.get(i).getNickname());
        }
        return auxList;
    }*/
    public DTUsuarioBase getDT(UsuarioBase ub){
        DTUsuarioBase auxDT = null;
        ImageIcon img = null;
        if(ub.getImage()!=null){
            img = ConvertirByteToImageIcon(ub.getImage());
        }
        
        if(ub instanceof Docente){
           auxDT = new DTDocente(ub.getNickname(),ub.getNombre(),ub.getApellido(),ub.getCorreo(),ub.getFNac(),null,img);
        }else if(ub instanceof Usuario){
           auxDT = new DTUsuario(ub.getNickname(),ub.getNombre(),ub.getApellido(),ub.getCorreo(),ub.getFNac(),img);
        }
        return auxDT;
    }
    public List<DTMaster> getDTList(){
        List<DTMaster> auxList = new ArrayList();
        for(int i = 0;i<misUsuarios.size();i++){
            DTMaster dt = getDT(misUsuarios.get(i));
            auxList.add(dt);
        }
        return auxList;
    }
    
    
    
    
    
    //Funcion que convierte un ImageIcon en byte para persistencia
    private byte[] ConvertirImageIconToByte(String imgPath) throws IOException{
        ImageIcon img = new ImageIcon(imgPath);
        String formato = "png";
        if(imgPath.endsWith(".png")){
            formato = "png";
        }else if(imgPath.endsWith(".jpg") || imgPath.endsWith(".jpeg")){
            formato = "png";
        }
        BufferedImage bi = new BufferedImage(img.getIconWidth(),img.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        img.paintIcon(null,g,0,0);
        g.dispose();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi,formato,baos);
        byte[] bytes = baos.toByteArray();

        return bytes;
    }
    
    private ImageIcon ConvertirByteToImageIcon(byte[] bytes){
        return new ImageIcon(bytes);
    }

}
