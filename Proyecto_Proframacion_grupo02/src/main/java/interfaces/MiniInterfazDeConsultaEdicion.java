/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import DTsClasses.DTCurso;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import DTsClasses.DTUsuarioBase;
import Logica.Fabric;
import Logica.IController;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mateo
 */
public class MiniInterfazDeConsultaEdicion extends javax.swing.JInternalFrame {
    IController ico;
    DefaultListModel<String> modeloDocentes;
    DTEdicionCurso dtc = null;
    
    public MiniInterfazDeConsultaEdicion() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
    }
    
    public void ColocarDatos(DTMaster dtEdicion){
        dtc = (DTEdicionCurso)dtEdicion;
        fieldInstituto.setText(dtc.getInstituto());
        fieldCurso.setText(dtc.getCurso());
        fieldNombre.setText(dtc.getNombre());
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        bannerFIni.setText(formateador.format(dtc.getFInicio()));
        bannerFFin.setText(formateador.format(dtc.getFFin()));
        bannerFPub.setText(formateador.format(dtc.getFechaAlta()));
        bannerCupo.setText(String.valueOf(dtc.getCupo()));
        modeloDocentes = new DefaultListModel();
        listDocentes.setModel(modeloDocentes);
        LLenarTablasYListas();
        
    }
    private void LLenarTablasYListas(){
        if (dtc != null) {
            // 1. Cargar Ediciones de Curso
            if (modeloDocentes != null) {
                modeloDocentes.clear(); // Limpiamos elementos previos
                List<String> listEdi = dtc.getDocentes();
                if (listEdi != null) {
                    for (String ed : listEdi) {
                        modeloDocentes.addElement(ed);
                    }
                }
            }          
        } else {
            System.out.println("El DTCurso proporcionado es null.");
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fieldInstituto = new javax.swing.JTextField();
        fieldCurso = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        bannerCupo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listDocentes = new javax.swing.JList<>();
        bannerFIni = new javax.swing.JTextField();
        bannerFFin = new javax.swing.JTextField();
        bannerFPub = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setClosable(true);

        jLabel6.setText("Nombre*");

        fieldNombre.setEditable(false);
        fieldNombre.setBackground(new java.awt.Color(255, 255, 255));
        fieldNombre.setFocusable(false);
        fieldNombre.setColumns(10);

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Inicio*");

        fieldInstituto.setEditable(false);
        fieldInstituto.setBackground(new java.awt.Color(255, 255, 255));
        fieldInstituto.setText("jTextField1");
        fieldInstituto.setFocusable(false);
        fieldInstituto.setColumns(10);

        fieldCurso.setEditable(false);
        fieldCurso.setBackground(new java.awt.Color(255, 255, 255));
        fieldCurso.setText("jTextField2");
        fieldCurso.setFocusable(false);
        fieldCurso.setColumns(10);

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Fin*");

        jLabel1.setText("Instituto:");

        jLabel2.setText("Curso:");

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Fecha de publicacion*");

        bannerCupo.setColumns(5);
        bannerCupo.setEditable(false);
        bannerCupo.setBackground(new java.awt.Color(255, 255, 255));
        bannerCupo.setFocusable(false);

        jLabel3.setText("Cupo:");

        listDocentes.addListSelectionListener(this::listDocentesValueChanged);
        jScrollPane1.setViewportView(listDocentes);

        bannerFIni.setEditable(false);
        bannerFIni.setBackground(new java.awt.Color(255, 255, 255));
        bannerFIni.setFocusable(false);
        bannerFIni.setColumns(8);

        bannerFFin.setEditable(false);
        bannerFFin.setBackground(new java.awt.Color(255, 255, 255));
        bannerFFin.setFocusable(false);
        bannerFFin.setColumns(8);

        bannerFPub.setEditable(false);
        bannerFPub.setBackground(new java.awt.Color(255, 255, 255));
        bannerFPub.setFocusable(false);
        bannerFPub.setColumns(8);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Docentes");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                            .addComponent(fieldNombre)
                            .addComponent(fieldInstituto)
                            .addComponent(fieldCurso)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bannerCupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel16)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(bannerFPub))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addGap(32, 32, 32)
                            .addComponent(bannerFIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel14)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bannerFFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(32, Short.MAX_VALUE))
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
                    .addComponent(fieldNombre))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(bannerFIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bannerFFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bannerFPub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bannerCupo)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listDocentesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listDocentesValueChanged
        if (!evt.getValueIsAdjusting()) {

            String nickname = listDocentes.getSelectedValue();

            if (nickname != null) {
                DTMaster dt = ico.ConsultarUsuario(nickname);
                MiniInterfazDeConsultaUsuario micu = new MiniInterfazDeConsultaUsuario();

                javax.swing.JDesktopPane desktop = (javax.swing.JDesktopPane) 
                    javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JDesktopPane.class, this);

                if (desktop != null) {
                    desktop.add(micu);
                    micu.setTitle("(Info) " + nickname);
                    micu.ColocarDatos((DTUsuarioBase)dt);
                    micu.setVisible(true);
                    micu.toFront();
                }
            }
        }
    }//GEN-LAST:event_listDocentesValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bannerCupo;
    private javax.swing.JTextField bannerFFin;
    private javax.swing.JTextField bannerFIni;
    private javax.swing.JTextField bannerFPub;
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnAceptar1;
    private javax.swing.JTextField fieldCurso;
    private javax.swing.JTextField fieldInstituto;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> listDocentes;
    // End of variables declaration//GEN-END:variables
}
