/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Classes.ProgramaFormacion;
import DTsClasses.DTProgramaForm;
import Manejadores.ManejadorProgramaForm;

/**
 *
 * @author manuelpalumbo
 */
public class ControladorPrograma {
    public void altaProgramaFormacion(DTProgramaForm dt) throws Exception{
       //Convertir el DT en una entidad de jpa
        ProgramaFormacion pf = new ProgramaFormacion();
        pf.setNombre(dt.getNombre());
        pf.setDescripcion(dt.getDescripcion());
        pf.setVigenciaPrograma(dt.getVigenciaProg());
        
        //guardar a traves del manejador
        ManejadorProgramaForm.getInstance().agregarPrograma(pf);
    }
}
