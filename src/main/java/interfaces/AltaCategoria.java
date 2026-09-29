/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import java.util.ArrayList;
import java.util.List;
import Logica.IController;
import Logica.Fabric;
/**
 *
 * @author mateo
 */
public class AltaCategoria extends javax.swing.JInternalFrame {
    IController ico;

    public AltaCategoria() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setTitle("Alta Categoria");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Nombre:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 20));
        getContentPane().add(fieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 29, 192, 28));

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(this::btnAceptarActionPerformed);
        getContentPane().add(btnAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, -1, -1));

        btnCancelar.setForeground(new java.awt.Color(255, 102, 102));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);
        getContentPane().add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        String nombreIngresado = fieldNombre.getText().trim();

        if (!nombreIngresado.isEmpty()) {

            // Verificamos si existe en la BD
            System.out.println((ico.VerificarCategoria(nombreIngresado)));
            if (ico.VerificarCategoria(nombreIngresado)) {

                int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "La categoría '" + nombreIngresado + "' ya existe. ¿Desea cancelar?",
                    "Categoría Existente",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
                );

                if (respuesta == javax.swing.JOptionPane.YES_NO_OPTION) {
                    this.dispose(); 
                }

            } else {
                ico.AltaCategoria(nombreIngresado);

                javax.swing.JOptionPane.showMessageDialog(
                    this, 
                    "Categoría '" + nombreIngresado + "' registrada con éxito.", 
                    "Alta Exitosa", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );

                this.dispose();
            } 

        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                "Por favor, complete todos los campos obligatorios.", 
                "Campos Incompletos", 
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // Simplemente cierra la ventana interna sin cerrar el programa
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
