/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTsClasses;

import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author mateo
 */
//Esto sirve para los casteos en los servlets
@XmlSeeAlso({
    DTCategoria.class,
    DTCurso.class,
    DTEdi_Usu.class,
    DTEdicionCurso.class,
    DTInstituto.class,
    DTProgramaForm.class,
    DTUsuarioBase.class
})
public class DTMaster {
    public DTMaster(){
        
    }
}
