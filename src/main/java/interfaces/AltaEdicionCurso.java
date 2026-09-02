/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;
import DTsClasses.DTCurso;
import DTsClasses.DTInstituto;
import DTsClasses.DTMaster;
import DTsClasses.EnumDT;
import interfaces.MiniInterfazDeAltaEdicion;
import Logica.Fabric;
import Logica.IController;
import java.text.SimpleDateFormat;
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
public class AltaEdicionCurso extends javax.swing.JInternalFrame {
    IController ico;
    List<Object[]> rowsIns;
    List<Object[]> rowsCurso;
    public AltaEdicionCurso() {
        rowsIns = new ArrayList();
        rowsCurso = new ArrayList();
        
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
        
        List<DTMaster> listInstituto = ico.ListarClase(EnumDT.DT_INSTITUTO);
        if(!listInstituto.isEmpty()){
            listInstituto = OrdenarLista(listInstituto);
            IniciarRows(listInstituto);
            IniciarTable(EnumDT.DT_INSTITUTO);
        }
        
    }
    private void IniciarRows(List<DTMaster> list){
        
        for(int i = 0;i<list.size();i++){
            DTMaster dt = list.get(i);
            if(dt instanceof DTInstituto dti){
                if(i==0){
                    rowsIns = new ArrayList();
                }
                Object[] row = {dti.getNombre()};
                rowsIns.add(row);
            }else if(dt instanceof DTCurso dti){
                if(i==0){
                    rowsCurso = new ArrayList();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fecha = sdf.format(dti.getFechaAlta());
                Object[] row = {dti.getNombre(), fecha};
                rowsCurso.add(row);
            }
        }
    }
    private List<DTMaster> OrdenarLista(List<DTMaster> listaParam){
        List<DTMaster> auxDT = listaParam;
        for(int i = 0;i<auxDT.size()-1;i++){
            for(int j = 0;j<auxDT.size()-1;j++){
                if(auxDT instanceof DTInstituto){
                    DTInstituto aux = (DTInstituto)auxDT.get(j);
                    DTInstituto auxJMas = (DTInstituto)auxDT.get(j+1);
                    if(aux.getNombre().toLowerCase().compareTo(auxJMas.getNombre().toLowerCase()) > 0){
                        DTInstituto temp = aux;
                        auxDT.set(j, auxDT.get(j+1));
                        auxDT.set(j+1,temp);
                    }
                }
                
            }
        }
        return auxDT;
    }
    private void IniciarTable(EnumDT tipoDT){
        if(tipoDT==EnumDT.DT_INSTITUTO){
            DefaultTableModel modelo = (DefaultTableModel) tableInstitutos.getModel();
            modelo.setRowCount(0);
            for(int i =0;i<rowsIns.size();i++){
                modelo.addRow(rowsIns.get(i));
            }
        }else if(tipoDT==EnumDT.DT_CURSO){
            DefaultTableModel modelo = (DefaultTableModel) tableCursos.getModel();
            modelo.setRowCount(0);
            for(int i =0;i<rowsCurso.size();i++){
                modelo.addRow(rowsCurso.get(i));
            }
        } 
    }
    private void FiltrarInstitutos(){
        //Para filtrar los institutos
        DefaultTableModel modelo = (DefaultTableModel)tableInstitutos.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tableInstitutos.setRowSorter(sorter);
        
        String texto = fieldInstituto.getText();
        if (texto.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
        }
    }
    private void FiltrarCursos(){
        //Para filtrar los cursos
        DefaultTableModel modelo = (DefaultTableModel)tableCursos.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tableCursos.setRowSorter(sorter);
        
        String texto = fieldCurso.getText();
        if (texto.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
        }
    }
    private void MostrarDatos(String nombre, String instituto){
        MiniInterfazDeAltaEdicion miae = new MiniInterfazDeAltaEdicion(instituto,nombre);
        this.getDesktopPane().add(miae);
        miae.setTitle("Alta Edicion de Curso");
        miae.setVisible(true);
        miae.toFront();
        //micc.ColocarDatos(dt);
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableInstitutos = new javax.swing.JTable();
        fieldInstituto = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableCursos = new javax.swing.JTable();
        fieldCurso = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setClosable(true);

        tableInstitutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableInstitutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tableInstitutos.getSelectedRow();
                if (fila != -1) {
                    String objNick = (String)tableInstitutos.getValueAt(fila, 0);

                    List<DTMaster> listCurso = ico.ListarCursos(objNick);
                    if(!listCurso.isEmpty()){
                        listCurso = OrdenarLista(listCurso);
                        IniciarRows(listCurso);
                        IniciarTable(EnumDT.DT_CURSO);
                        FiltrarCursos();
                    }else{
                        DefaultTableModel modelo = (DefaultTableModel) tableCursos.getModel();
                        modelo.setRowCount(0);
                    }

                }
            }
        });
        jScrollPane1.setViewportView(tableInstitutos);

        fieldInstituto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                FiltrarInstitutos();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                FiltrarInstitutos();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        tableCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Fecha de Alta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableCursos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tableCursos.getSelectedRow();
                int filaIns = tableInstitutos.getSelectedRow();
                if (fila != -1) {
                    //Logica de mostrar curso
                    String nombre = (String)tableCursos.getValueAt(fila, 0);
                    String objNick = (String)tableInstitutos.getValueAt(filaIns, 0);
                    MostrarDatos(nombre,objNick);
                }
            }
        });
        jScrollPane2.setViewportView(tableCursos);

        jLabel1.setText("Instituto");

        jLabel2.setText("Curso");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(fieldCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(fieldInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(56, 56, 56))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fieldCurso)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fieldCurso;
    private javax.swing.JTextField fieldInstituto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableCursos;
    private javax.swing.JTable tableInstitutos;
    // End of variables declaration//GEN-END:variables
}
