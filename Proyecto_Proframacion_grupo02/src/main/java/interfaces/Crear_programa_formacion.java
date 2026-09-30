/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import Logica.Fabric;
import Logica.IController;
import java.util.Calendar;
import java.util.Date;
import javax.swing.SpinnerDateModel;

/**
 *
 * @author mateo
 */
public class Crear_programa_formacion extends javax.swing.JInternalFrame {
    IController ico;

    /**
     * Creates new form Crear_programa_formacion
     */
    public Crear_programa_formacion() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        
        initComponents();
        Calendar calendar = Calendar.getInstance();
        
        SpinnerDateModel modelo =(SpinnerDateModel)spinDatePub.getModel();
        calendar.setTime((Date)spinDatePub.getValue());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date auxDate = calendar.getTime();
        
        modelo.setStart(auxDate);
        modelo.setValue(new Date());
        
        
        
        SpinnerDateModel modeloIni =(SpinnerDateModel)spinDateIni.getModel();
        modeloIni.setStart(auxDate);
        modeloIni.setValue((Date)spinDatePub.getValue());
            
        SpinnerDateModel modeloFin = (SpinnerDateModel)spinDateFin.getModel();
        modeloFin.setStart(auxDate);
        modeloFin.setValue((Date)spinDateIni.getValue());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spinDateIni = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        spinDateFin = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        acceptButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        spinDatePub = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaDescripcion = new javax.swing.JTextArea();

        setClosable(true);

        spinDateIni.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1789140907447L), null, null, java.util.Calendar.DAY_OF_MONTH));
        spinDateIni.setToolTipText("");
        spinDateIni.setEditor(new javax.swing.JSpinner.DateEditor(spinDateIni, "dd/MM/yyyy"));
        spinDateIni.setMaximumSize(new java.awt.Dimension(144, 22));
        spinDateIni.addChangeListener(this::spinDateIniStateChanged);

        jLabel4.setText("Fecha de inicio");

        spinDateFin.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1789140319724L), null, null, java.util.Calendar.DAY_OF_MONTH));
        spinDateFin.setToolTipText("");
        spinDateFin.setEditor(new javax.swing.JSpinner.DateEditor(spinDateFin, "dd/MM/yyyy"));
        spinDateFin.setMaximumSize(new java.awt.Dimension(144, 22));
        spinDateFin.setMinimumSize(new java.awt.Dimension(144, 22));
        spinDateFin.setPreferredSize(new java.awt.Dimension(144, 22));
        spinDateFin.addChangeListener(this::spinDateFinStateChanged);

        jLabel5.setText("Fecha de Finalizacion");

        acceptButton.setText("Aceptar");
        acceptButton.addActionListener(this::acceptButtonActionPerformed);

        jLabel1.setText("Nombre Programa");

        spinDatePub.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1789140925504L), null, null, java.util.Calendar.DAY_OF_MONTH));
        spinDatePub.setToolTipText("");
        spinDatePub.setEditor(new javax.swing.JSpinner.DateEditor(spinDatePub, "dd/MM/yyyy"));
        spinDatePub.setMaximumSize(new java.awt.Dimension(144, 22));
        spinDatePub.setMinimumSize(new java.awt.Dimension(144, 22));
        spinDatePub.setPreferredSize(new java.awt.Dimension(144, 22));
        spinDatePub.addChangeListener(this::spinDatePubStateChanged);

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("Ingrese datos del Programa");

        jLabel6.setText("Fecha de Alta");

        fieldNombre.setMaximumSize(new java.awt.Dimension(130, 22));
        fieldNombre.setMinimumSize(new java.awt.Dimension(130, 22));
        fieldNombre.setPreferredSize(new java.awt.Dimension(130, 22));

        jLabel3.setText("Descripcion");

        areaDescripcion.setColumns(20);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setRows(5);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setMaximumSize(new java.awt.Dimension(245, 130));
        areaDescripcion.setMinimumSize(new java.awt.Dimension(245, 130));
        areaDescripcion.setPreferredSize(new java.awt.Dimension(245, 130));
        jScrollPane1.setViewportView(areaDescripcion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spinDateFin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(spinDatePub, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spinDateIni, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(87, 87, 87))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(acceptButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fieldNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinDateIni, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinDateFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinDatePub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(acceptButton)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        String nombreProg = fieldNombre.getText().trim();
        String descripcionProg = areaDescripcion.getText().trim();
        Date fechaInicio = (Date) spinDateIni.getValue();
        Date fechaFin = (Date) spinDateFin.getValue();
        Date fAlta  = (Date)spinDatePub.getValue();
        
        if (nombreProg.isEmpty() || descripcionProg.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                "Error: Todos los campos son obligatorios", 
                "Campos Inválidos", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (VerificarDatos()) {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                "Error: Datos incoherentes en las fechas", 
                "Campos Inválidos", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            if (ico.VerificarPrograma(nombreProg)) {
                int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                    this, 
                    "El programa '" + nombreProg + "' ya existe. ¿Deseas modificar sus datos?", 
                    "Programa Existente", 
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
                );
            
                if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
                    // El usuario quiere modificarlo
                    ico.CrearProgramasDeFormacion(nombreProg, descripcionProg, fechaInicio, fechaFin, fAlta);
                    javax.swing.JOptionPane.showMessageDialog(this, "Programa actualizado con éxito.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } else {
                    // El usuario canceló la operación
                    javax.swing.JOptionPane.showMessageDialog(this, "Operación cancelada.");
                    this.dispose();
                }
            } else {
                ico.CrearProgramasDeFormacion(nombreProg, descripcionProg, fechaInicio, fechaFin, fAlta);
                javax.swing.JOptionPane.showMessageDialog(this, "¡Programa de formación creado con éxito!", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                "Error al procesar la solicitud: " + e.getMessage(), 
                "Error de Persistencia", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void spinDateIniStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinDateIniStateChanged
        Date fInicio = (Date)spinDateIni.getValue();
        Date fFin = (Date)spinDateFin.getValue();
        SpinnerDateModel modelo =(SpinnerDateModel)spinDateFin.getModel();
            modelo.setStart((Date)spinDateIni.getValue());
        if(fInicio.after(fFin)){
            modelo.setValue((Date)spinDateIni.getValue());
        }
    }//GEN-LAST:event_spinDateIniStateChanged

    private void spinDateFinStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinDateFinStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_spinDateFinStateChanged

    private void spinDatePubStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinDatePubStateChanged
        Date fAlta = (Date)spinDatePub.getValue();
        Date fInicio = (Date)spinDateIni.getValue();
        SpinnerDateModel modelo =(SpinnerDateModel)spinDateIni.getModel();
        modelo.setStart((Date)spinDatePub.getValue());
        if(fAlta.after(fInicio)){
            modelo.setValue((Date)spinDatePub.getValue());
        }
    }//GEN-LAST:event_spinDatePubStateChanged
    
    
    private boolean VerificarDatos() {
        Date fecha1 = (Date)spinDateIni.getValue();
        Date fecha2 = (Date)spinDateFin.getValue();
        Date fechaAlta = (Date)spinDatePub.getValue();
        
        return fecha1.after(fecha2) || fechaAlta.after(fecha1);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JTextArea areaDescripcion;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spinDateFin;
    private javax.swing.JSpinner spinDateIni;
    private javax.swing.JSpinner spinDatePub;
    // End of variables declaration//GEN-END:variables
}
