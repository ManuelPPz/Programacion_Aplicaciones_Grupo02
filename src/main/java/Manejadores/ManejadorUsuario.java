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
import javax.swing.ImageIcon;

import javax.imageio.ImageIO;

import DTsClasses.DTUsuarioBase;
import DTsClasses.DTDocente;
import DTsClasses.DTUsuario;
import DTsClasses.DTMaster;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    
    public UsuarioBase CrearUsuario(String nick, String nombre, String apellido, String correo, boolean docente,Date fNac, List<String>institutos, String imgPath) throws IOException{
        UsuarioBase returnUb;
        byte[] imgByte = null;
        if(!imgPath.isEmpty()){
            imgByte = ConvertirImageIconToByte(imgPath);
        }
        
        if(docente){
            returnUb = new Docente(nick, nombre, apellido, correo,/*falta meter institutos*/ fNac,imgByte);
        }else{
            returnUb = new Usuario(nick, nombre, apellido, correo, fNac,imgByte);
        }
        return returnUb;
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
    
}
