/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import DTsClasses.EnumDT;
import Logica.IController;
import Logica.Fabric;
import java.awt.HeadlessException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author manuelpalumbo
 */
public class AgregarCursoProgramaForm extends javax.swing.JInternalFrame {
    IController ico;
    List<Object[]> rowsProg;
    List<Object[]> rowsCurso;
    
    public AgregarCursoProgramaForm() {
        rowsProg = new ArrayList<>();
        rowsCurso = new ArrayList<>();
        Fabric f = Fabric.GetInstance();
        ico  = f.GetIController();
        initComponents();
        
        List<DTMaster> listProgramas = ico.ListarClase(EnumDT.DT_PROGRAMA);
        if(!listProgramas.isEmpty()){
            listProgramas = OrdenarLista(listProgramas);
            IniciarRows(listProgramas);
            IniciarTable(EnumDT.DT_PROGRAMA);
        }
        List<DTMaster> listCursos = ico.ListarClase(EnumDT.DT_CURSO);
        if(!listCursos.isEmpty()){
            listCursos = OrdenarLista(listCursos);
            IniciarRows(listCursos);
            IniciarTable(EnumDT.DT_CURSO);
        }
    }
    
    private void IniciarRows(List<DTMaster> list){
        
        for(int i = 0;i<list.size();i++){
            DTMaster dt = list.get(i);
            if(dt instanceof DTProgramaForm dti){
                if(i==0){
                    rowsProg = new ArrayList<>();
                }
                Object[] row = {dti.getNombre()};
                rowsProg.add(row);
            }else if(dt instanceof DTCurso dti){
                if(i==0){
                    rowsCurso = new ArrayList<>();
                }
                Object[] row = {dti.getNombre(), false};
                rowsCurso.add(row);
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
                }else if(auxDT.get(j) instanceof DTCurso){
                    DTCurso aux = (DTCurso)auxDT.get(j);
                    DTCurso auxJMas = (DTCurso)auxDT.get(j+1);
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
        }else if(tipoDT==EnumDT.DT_CURSO){
            DefaultTableModel modelo = (DefaultTableModel) tableCursos.getModel();
            modelo.setRowCount(0);
            for(int i =0;i<rowsCurso.size();i++){
                modelo.addRow(rowsCurso.get(i));
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
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        btAceptar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        fieldPrograma = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableCursos = new javax.swing.JTable();
        fieldCurso = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableProgramas = new javax.swing.JTable();

        setClosable(true);
        setTitle("Agregar Curso");

        btAceptar.setText("Aceptar");
        btAceptar.addActionListener(this::btAceptarActionPerformed);

        btCancelar.setText("Cancelar");
        btCancelar.addActionListener(this::btCancelarActionPerformed);

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

        tableCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Seleccionada"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableCursos);

        fieldCurso = new javax.swing.JTextField();
        fieldCurso.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                FiltrarCursos();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                FiltrarCursos();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        jLabel1.setText("Prog. de Formacion");

        jLabel2.setText("Curso");

        tableProgramas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tableProgramas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btAceptar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(fieldPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fieldCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldCurso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAceptar)
                    .addComponent(btCancelar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        this.dispose();
    }                                          

    private void btAceptarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        int fila = -1;
        fila = tableProgramas.getSelectedRow();
        List<String> auxList = new ArrayList<>();
        for(int i = 0;i<tableCursos.getRowCount();i++){
            boolean auxBool = (Boolean)tableCursos.getValueAt(i, 1);
            if(auxBool){
                String auxStr = (String)tableCursos.getValueAt(i, 0);
                auxList.add(auxStr);
            }
        }
        if(fila!=-1 && !auxList.isEmpty()){
            ico.AgregarCursoAProgramas((String)tableProgramas.getValueAt(fila, 0), auxList);
            javax.swing.JOptionPane.showMessageDialog(this, 
            "Curso/s registrado/s con éxito para " + (String)tableProgramas.getValueAt(fila, 0) + ".", 
            "Agregacion Exitosa", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
            // Cerrar la ventana interna
            this.dispose();
        }else{
            javax.swing.JOptionPane.showMessageDialog(this, 
            "Debe seleccionar al menos un curso y seleccionar un programa.", 
            "Campos Incompletos", 
            javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }                                         


    // Variables declaration - do not modify                     
    private javax.swing.JButton btAceptar;
    private javax.swing.JButton btCancelar;
    private javax.swing.JTextField fieldCurso;
    private javax.swing.JTextField fieldPrograma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableCursos;
    private javax.swing.JTable tableProgramas;
    // End of variables declaration                   
}