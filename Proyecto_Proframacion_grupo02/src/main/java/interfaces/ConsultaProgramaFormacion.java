/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import DTsClasses.EnumDT;
import Logica.Fabric;
import Logica.IController;
import java.util.ArrayList;
import java.util.List;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mateo
 */
public class ConsultaProgramaFormacion extends javax.swing.JInternalFrame {
    IController ico;
    List<Object[]> rowsProg;
    public ConsultaProgramaFormacion() {
        rowsProg = new ArrayList<>();
        Fabric f = Fabric.GetInstance();
        ico  = f.GetIController();
        initComponents();
        
        List<DTMaster> listProgramas = ico.ListarClase(EnumDT.DT_PROGRAMA);
        if(!listProgramas.isEmpty()){
            listProgramas = OrdenarLista(listProgramas);
            IniciarRows(listProgramas);
            IniciarTable(EnumDT.DT_PROGRAMA);
        }
    }
    
    private void IniciarRows(List<DTMaster> list){
        
        for(int i = 0;i<list.size();i++){
            DTMaster dt = list.get(i);
            if(dt instanceof DTProgramaForm dti){
                if(i==0){
                    rowsProg = new ArrayList<>();
                }
                Object[] row = {dti.getNombre(),dti.getFechaAlta()};
                rowsProg.add(row);
            }
        }
    }
    
    private List<DTMaster> OrdenarLista(List<DTMaster> listaParam){
        List<DTMaster> auxDT = listaParam;
        for(int i = 0;i<auxDT.size()-1;i++){
            for(int j = 0;j<auxDT.size()-1-i;j++){
                if(auxDT.get(j) instanceof DTProgramaForm){
                    DTProgramaForm aux = (DTProgramaForm)auxDT.get(j);
                    DTProgramaForm auxJMas = (DTProgramaForm)auxDT.get(j+1);
                    if(aux.getNombre().toLowerCase().compareTo(auxJMas.getNombre().toLowerCase()) > 0){
                        DTMaster temp = auxDT.get(j);
                        auxDT.set(j, auxDT.get(j+1));
                        auxDT.set(j+1,temp);
                    }
                }
            }
        }
        return auxDT;
    }
    
    private void IniciarTable(EnumDT tipoDT){
        if(tipoDT==EnumDT.DT_PROGRAMA){
            DefaultTableModel modelo = (DefaultTableModel) tableProgramas.getModel();
            modelo.setRowCount(0);
            for(int i =0;i<rowsProg.size();i++){
                modelo.addRow(rowsProg.get(i));
            }
        }
    }
    
    public void FiltrarProgramas(){
        //Para filtrar los institutos
        DefaultTableModel modelo = (DefaultTableModel)tableProgramas.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tableProgramas.setRowSorter(sorter);
        
        String texto = fieldPrograma.getText();
        if (texto.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
        }
    }
    
    public void MostrarDatos(String nombre){
        DTMaster dt = ico.ConsultaProgramaFormacion(nombre);
        MiniInterfazDeConsultaPrograma micp = new MiniInterfazDeConsultaPrograma();
        this.getDesktopPane().add(micp);
        micp.setTitle("(Info) "+nombre);
        micp.setVisible(true);
        micp.toFront();
        
        micp.mostrarDatosPrograma(dt);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableProgramas = new javax.swing.JTable();
        fieldPrograma = new javax.swing.JTextField();

        setClosable(true);
        setPreferredSize(new java.awt.Dimension(357, 310));

        jLabel1.setText("Prog. de Formacion");

        tableProgramas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Fecha Alta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableProgramas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tableProgramas.getSelectedRow();
                if (fila != -1) {
                    String nombre = (String)tableProgramas.getValueAt(fila, 0);
                    MostrarDatos(nombre);
                }
            }
        });
        jScrollPane1.setViewportView(tableProgramas);
        if (tableProgramas.getColumnModel().getColumnCount() > 0) {
            tableProgramas.getColumnModel().getColumn(0).setResizable(false);
            tableProgramas.getColumnModel().getColumn(1).setResizable(false);
        }

        fieldPrograma.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                FiltrarProgramas();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                FiltrarProgramas();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(fieldPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(69, 69, 69))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fieldPrograma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableProgramas;
    // End of variables declaration//GEN-END:variables
}
