/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;
import DTsClasses.DTDocente;
import DTsClasses.DTMaster;
import Logica.Fabric;
import Logica.IController;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
/**
 *
 * @author mateo
 */
public class MiniInterfazDeAltaEdicion extends javax.swing.JInternalFrame {
    IController ico;
    String instituto;
    String curso;
    
    public MiniInterfazDeAltaEdicion(String instituto, String curso) {
        Fabric f  = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
        spinnerCupo.setVisible(false);
        TableColumn colNombre = tableDocentes.getColumnModel().getColumn(0);
        colNombre.setPreferredWidth(150);
        TableColumn colSeleccionado = tableDocentes.getColumnModel().getColumn(1);
        colSeleccionado.setPreferredWidth(50);
        this.instituto = instituto;
        this.curso = curso;
        fieldInstituto.setText(instituto);
        fieldCurso.setText(curso);
        LlenarTablas();
    }
    
    
private void LlenarTablas() {
    DefaultTableModel modelo = (DefaultTableModel) tableDocentes.getModel();
    modelo.setRowCount(0); // Limpiar filas previas

    List<DTMaster> auxList = ico.ListarDocentes(instituto);

    if (auxList != null && !auxList.isEmpty()) {
        for (DTMaster dt : auxList) {
            if (dt instanceof DTDocente dTDocente) {
                // Muestra Nickname - Nombre completo
                String etiqueta = dTDocente.getNickname() + " (" + dTDocente.getNombre() + ")";
                Object[] fila = new Object[]{ etiqueta, Boolean.FALSE };
                modelo.addRow(fila);
            }
        }
    } else {
        System.out.println("No se encontraron docentes para el instituto: " + instituto);
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nombreField = new javax.swing.JTextField();
        spinDateIni = new javax.swing.JSpinner();
        jLabel12 = new javax.swing.JLabel();
        spinDateFin = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        checkCupo = new javax.swing.JCheckBox();
        spinnerCupo = new javax.swing.JSpinner();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableDocentes = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        spinDatePub = new javax.swing.JSpinner();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        fieldInstituto = new javax.swing.JTextField();
        fieldCurso = new javax.swing.JTextField();

        jLabel5.setText("jLabel5");

        setClosable(true);

        jLabel1.setText("Instituto:");

        jLabel2.setText("Curso:");

        jLabel6.setText("Nombre*");

        spinDateIni.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, new java.util.Date(), java.util.Calendar.DAY_OF_MONTH));
        spinDateIni.setEditor(new javax.swing.JSpinner.DateEditor(spinDateIni, "dd/MM/yyyy"));

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Inicio*");

        spinDateFin.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, new java.util.Date(), java.util.Calendar.DAY_OF_MONTH));
        spinDateFin.setEditor(new javax.swing.JSpinner.DateEditor(spinDateFin, "dd/MM/yyyy"));

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Fin*");

        checkCupo.setText("Cupo Definido");
        checkCupo.addChangeListener(this::checkCupoStateChanged);

        tableDocentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Docentes", "Seleccionado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tableDocentes);
        if (tableDocentes.getColumnModel().getColumnCount() > 0) {
            tableDocentes.getColumnModel().getColumn(0).setResizable(false);
            tableDocentes.getColumnModel().getColumn(1).setResizable(false);
        }

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Fecha de publicacion*");

        spinDatePub.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, new java.util.Date(), java.util.Calendar.DAY_OF_MONTH));
        spinDatePub.setEditor(new javax.swing.JSpinner.DateEditor(spinDatePub, "dd/MM/yyyy"));

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(this::btnAceptarActionPerformed);

        btnCancelar.setForeground(new java.awt.Color(255, 102, 102));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        fieldInstituto.setEditable(false);
        fieldInstituto.setBackground(new java.awt.Color(255, 255, 255));
        fieldInstituto.setText("jTextField1");

        fieldCurso.setEditable(false);
        fieldCurso.setBackground(new java.awt.Color(255, 255, 255));
        fieldCurso.setText("jTextField2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkCupo)
                        .addGap(18, 18, 18)
                        .addComponent(spinnerCupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(spinDateIni, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel14)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(spinDateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(nombreField)
                                .addComponent(fieldInstituto, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                                .addComponent(fieldCurso))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinDatePub, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(89, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAceptar)
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fieldInstituto)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(fieldCurso)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(nombreField))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinDateIni, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(jLabel14)
                    .addComponent(spinDateFin))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkCupo)
                    .addComponent(spinnerCupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinDatePub, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnAceptar))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkCupoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_checkCupoStateChanged
        spinnerCupo.setVisible(checkCupo.isSelected());
    }//GEN-LAST:event_checkCupoStateChanged

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
      String nombre = nombreField.getText();
    Date fIni = (Date) spinDateIni.getValue();
    Date fFin = (Date) spinDateFin.getValue();
    boolean auxCheckCupo = checkCupo.isSelected();
    int auxCupo = auxCheckCupo ? (int) spinnerCupo.getValue() : 0;
    Date fPub = (Date) spinDatePub.getValue();

    List<String> auxDocentes = new ArrayList<>();
    
    for (int i = 0; i < tableDocentes.getRowCount(); i++) {
        Object isSelected = tableDocentes.getValueAt(i, 1);
        if (isSelected != null && (Boolean) isSelected) {
            String docenteNick = (String) tableDocentes.getValueAt(i, 0);
            auxDocentes.add(docenteNick);
        }
    }

    try {
        ico.AltaEdicionCurso(instituto, curso, nombre, fIni, fFin, auxCupo, auxDocentes, fPub);
        JOptionPane.showMessageDialog(this, "Edición dada de alta con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // Simplemente cierra la ventana interna sin cerrar el programa
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JCheckBox checkCupo;
    private javax.swing.JTextField fieldCurso;
    private javax.swing.JTextField fieldInstituto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextField nombreField;
    private javax.swing.JSpinner spinDateFin;
    private javax.swing.JSpinner spinDateIni;
    private javax.swing.JSpinner spinDatePub;
    private javax.swing.JSpinner spinnerCupo;
    private javax.swing.JTable tableDocentes;
    // End of variables declaration//GEN-END:variables
}
