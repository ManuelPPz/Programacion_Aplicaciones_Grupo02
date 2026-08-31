/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;
import Logica.IController;
import Logica.Fabric;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import DTsClasses.DTMaster;
import DTsClasses.DTCurso;
import DTsClasses.DTInstituto;
import DTsClasses.EnumDT;
/**
 *
 * @author sebas
 */
public class AltaCurso extends javax.swing.JInternalFrame {
    IController ico;
    
    
    /**
     * Creates new form AltaUsuario
     */
    public AltaCurso() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
        
       
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        boxInstituto = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaDescripcion = new javax.swing.JTextArea();
        spinnerDuracion = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fieldURL = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        boxPrevias = new javax.swing.JComboBox<>();
        buttonAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        spinnerFecha = new javax.swing.JSpinner();
        spinnerHoras = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        spinnerCreditos = new javax.swing.JSpinner();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Alta Curso");
        getContentPane().setLayout(null);

        jLabel1.setText("Instituto*");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(14, 26, 61, 16);

        jLabel2.setText("Nombre*");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(14, 54, 61, 16);

        jLabel3.setText("Descripcion*");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(14, 104, 90, 16);

        jLabel4.setText("Duracion*");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(14, 228, 60, 16);

        fieldNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldNombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldNombreFocusLost(evt);
            }
        });
        fieldNombre.addActionListener(this::fieldNombreActionPerformed);
        getContentPane().add(fieldNombre);
        fieldNombre.setBounds(99, 51, 254, 22);

        boxInstituto.addActionListener(this::boxInstitutoActionPerformed);
        getContentPane().add(boxInstituto);
        boxInstituto.setBounds(99, 23, 254, 22);
        List<DTMaster> auxListIns = ico.ListarClase(EnumDT.DT_INSTITUTO);

        boxInstituto.insertItemAt("SIN DATOS", 0);
        for(int i = 0;i<auxListIns.size();i++){
            DTInstituto dt = (DTInstituto)auxListIns.get(i);
            boxInstituto.insertItemAt(dt.getNombre(), i+1);
        }
        boxInstituto.setSelectedIndex(0);

        areaDescripcion.setColumns(20);
        areaDescripcion.setRows(5);
        jScrollPane2.setViewportView(areaDescripcion);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(99, 104, 254, 86);
        getContentPane().add(spinnerDuracion);
        spinnerDuracion.setBounds(73, 217, 48, 38);

        jLabel11.setText("Cant. Horas*");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(127, 228, 80, 16);

        jLabel13.setText("URL*");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(14, 276, 107, 16);

        fieldURL.addActionListener(this::fieldURLActionPerformed);
        getContentPane().add(fieldURL);
        fieldURL.setBounds(127, 273, 216, 22);

        jLabel14.setText("Previas");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(14, 349, 107, 16);

        boxPrevias.addActionListener(this::boxPreviasActionPerformed);
        RellenarPrevias();
        getContentPane().add(boxPrevias);
        boxPrevias.setBounds(127, 346, 144, 22);

        buttonAceptar.setText("Aceptar");
        buttonAceptar.addActionListener(this::buttonAceptarActionPerformed);
        getContentPane().add(buttonAceptar);
        buttonAceptar.setBounds(280, 390, 90, 23);

        btnCancelar.setForeground(new java.awt.Color(255, 102, 102));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);
        getContentPane().add(btnCancelar);
        btnCancelar.setBounds(170, 390, 90, 23);

        jLabel15.setText("Fecha de alta*");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(14, 309, 107, 16);

        spinnerFecha.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, new java.util.Date(), java.util.Calendar.DAY_OF_MONTH));
        spinnerFecha.setEditor(new javax.swing.JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy"));
        getContentPane().add(spinnerFecha);
        spinnerFecha.setBounds(127, 306, 144, 22);
        getContentPane().add(spinnerHoras);
        spinnerHoras.setBounds(200, 220, 48, 38);

        jLabel5.setText("Cant. Creditos*");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(253, 228, 100, 16);
        getContentPane().add(spinnerCreditos);
        spinnerCreditos.setBounds(340, 220, 48, 38);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNombreActionPerformed

    private void fieldURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldURLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldURLActionPerformed

    private void buttonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAceptarActionPerformed
//Logica de boton aceptar
    if (VerificarDatos()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Algunos campos deben ser completados", "Atencion", javax.swing.JOptionPane.ERROR_MESSAGE);
    } else {
        try {
            List<String> previas = new ArrayList<>();
            int tam = boxPrevias.getItemCount();
            for (int i = 1; i < tam; i++) {
                String text = (String) boxPrevias.getItemAt(i);
                if (text.contains("✓")) {
                    String auxSubString = text.substring(2);
                    previas.add(auxSubString);
                }
            }

            if (ico.VerificarCurso(fieldNombre.getText(), boxInstituto.getSelectedItem().toString())) {
                int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                        this,
                        "El programa '" + fieldNombre.getText() + "' ya existe. ¿Deseas modificar sus datos?",
                        "Programa Existente",
                        javax.swing.JOptionPane.YES_NO_OPTION,
                        javax.swing.JOptionPane.QUESTION_MESSAGE);

                if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
                    // El usuario quiere modificarlo
                    System.out.println(areaDescripcion.getText());
                    ico.AltaCurso(boxInstituto.getSelectedItem().toString(), fieldNombre.getText(), areaDescripcion.getText(), (int) spinnerDuracion.getValue(), (int) spinnerHoras.getValue(), (int) spinnerCreditos.getValue(), fieldURL.getText(), previas, (Date) spinnerFecha.getValue());

                    javax.swing.JOptionPane.showMessageDialog(this, "Programa actualizado con éxito.");
                    this.dispose();
                } else {
                    // El usuario canceló la operación
                    this.dispose();
                }
            } else {
                ico.AltaCurso(boxInstituto.getSelectedItem().toString(), fieldNombre.getText(), areaDescripcion.getText(), (int) spinnerDuracion.getValue(), (int) spinnerHoras.getValue(), (int) spinnerCreditos.getValue(), fieldURL.getText(), previas, (Date) spinnerFecha.getValue());
                javax.swing.JOptionPane.showMessageDialog(this, "Curso ingresado con exito!", "System", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al procesar el curso: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_buttonAceptarActionPerformed

    boolean VerificarDatos(){
        return boxInstituto.getSelectedIndex()==0 ||
                fieldNombre.getText().isEmpty() ||
                areaDescripcion.getText().isEmpty() || 
                (int) spinnerDuracion.getValue()==0 || 
                (int) spinnerHoras.getValue()==0 || 
                fieldURL.getText().isEmpty();
    }
    
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // Simplemente cierra la ventana interna sin cerrar el programa
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void boxPreviasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxPreviasActionPerformed
        if(boxPrevias.getSelectedIndex()!=0){
                String auxString = boxPrevias.getSelectedItem().toString();
            if(auxString.contains("✓")){
                String auxSubString = auxString.substring(2);
                int auxInt = boxPrevias.getSelectedIndex();
                boxPrevias.removeItemAt(auxInt);
                boxPrevias.insertItemAt(auxSubString, auxInt);
            }else{
                auxString = "✓ " + auxString;
                int auxInt = boxPrevias.getSelectedIndex();
                boxPrevias.removeItemAt(auxInt);
                boxPrevias.insertItemAt(auxString, auxInt);
                boxPrevias.setSelectedIndex(auxInt);
            }
        }
        
    }//GEN-LAST:event_boxPreviasActionPerformed

    private void boxInstitutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxInstitutoActionPerformed
        if(boxInstituto.getSelectedIndex()!=0){
            RellenarPrevias();
        }

    }//GEN-LAST:event_boxInstitutoActionPerformed

    private void fieldNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNombreFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNombreFocusGained

    private void fieldNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNombreFocusLost

    }//GEN-LAST:event_fieldNombreFocusLost
    public void RellenarPrevias(){
        boxPrevias.removeAllItems();
        List<DTMaster> auxListCur = ico.ListarCursos(boxInstituto.getSelectedItem().toString());
        if(!auxListCur.isEmpty()){
            boxPrevias.insertItemAt("SIN DATOS", 0);
            for(int i = 0;i<auxListCur.size();i++){
                DTCurso auxDT = (DTCurso)auxListCur.get(i);
                String aux = auxDT.getNombre();
                boxPrevias.insertItemAt(aux, i+1);
            }
            boxPrevias.setSelectedIndex(0);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaDescripcion;
    private javax.swing.JComboBox<String> boxInstituto;
    private javax.swing.JComboBox<String> boxPrevias;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton buttonAceptar;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JTextField fieldURL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JSpinner spinnerCreditos;
    private javax.swing.JSpinner spinnerDuracion;
    private javax.swing.JSpinner spinnerFecha;
    private javax.swing.JSpinner spinnerHoras;
    // End of variables declaration//GEN-END:variables
}
