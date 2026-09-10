/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package interfaces;

import DTsClasses.DTMaster;
import Logica.Fabric;
import Logica.IController;
import javax.swing.JList;

/**
 *
 * @author mateo
 */
public class InfoExtraUsuario extends javax.swing.JPanel {

    IController ico;
    public InfoExtraUsuario() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
    }
    public JList getListEdiciones(){
        return this.listEdiciones;
    }
    public JList getListProgramas(){
        return this.listProgramas;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listProgramas = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listEdiciones = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();

        setLayout(null);

        listProgramas.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listProgramas.addListSelectionListener(this::listProgramasValueChanged);
        jScrollPane1.setViewportView(listProgramas);

        add(jScrollPane1);
        jScrollPane1.setBounds(277, 34, 154, 146);

        jLabel2.setText("Prog. de Formacion Inscriptos");
        add(jLabel2);
        jLabel2.setBounds(270, 10, 210, 16);

        listEdiciones.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listEdiciones.addListSelectionListener(this::listEdicionesValueChanged);
        jScrollPane2.setViewportView(listEdiciones);

        add(jScrollPane2);
        jScrollPane2.setBounds(58, 34, 154, 146);

        jLabel3.setText("Edi. de Curso Inscriptas");
        add(jLabel3);
        jLabel3.setBounds(60, 10, 180, 18);
    }// </editor-fold>//GEN-END:initComponents

    private void listEdicionesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listEdicionesValueChanged
        //Consulta Ediciones
        if (!evt.getValueIsAdjusting()) {

            String nombre = listEdiciones.getSelectedValue();

            if (nombre != null) {
                DTMaster dt = ico.ConsultaEdicionCurso(nombre);
                MiniInterfazDeConsultaEdicion miec = new MiniInterfazDeConsultaEdicion();

                javax.swing.JDesktopPane desktop = (javax.swing.JDesktopPane) 
                    javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JDesktopPane.class, this);

                if (desktop != null) {
                    desktop.add(miec);
                    miec.setTitle("(Info) " + nombre);
                    miec.ColocarDatos(dt);
                    miec.setVisible(true);
                    miec.toFront();
                }
            }
        }
    }//GEN-LAST:event_listEdicionesValueChanged

    private void listProgramasValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listProgramasValueChanged
        //Consulta Programas de Formacion
        /*
        Posible Solucion
        if (!evt.getValueIsAdjusting()) {

            String nombre = listProgramas.getSelectedValue();

            if (nombre != null) {
                DTMaster dt = ico.ConsultaProgramaFormacion(nombre);
                MiniInterfazDeProgramas mip = new MiniInterfazDeProgramas();

                javax.swing.JDesktopPane desktop = (javax.swing.JDesktopPane) 
                    javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JDesktopPane.class, this);

                if (desktop != null) {
                    desktop.add(mip);
                    mip.setTitle("(Info) " + nombre);
                    mip.ColocarDatos(dt);
                    mip.setVisible(true);
                    mip.toFront();
                }
            }
        }
        
        */
    }//GEN-LAST:event_listProgramasValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listEdiciones;
    private javax.swing.JList<String> listProgramas;
    // End of variables declaration//GEN-END:variables
}
