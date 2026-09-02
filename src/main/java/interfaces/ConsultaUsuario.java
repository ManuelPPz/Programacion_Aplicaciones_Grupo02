/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import javax.swing.table.DefaultTableModel;
import DTsClasses.DTUsuarioBase;
import DTsClasses.DTDocente;
import Logica.Fabric;
import Logica.IController;
import java.util.List;
import java.util.ArrayList;

import DTsClasses.DTUsuario;
import DTsClasses.DTMaster;
import DTsClasses.EnumDT;
import javax.swing.RowFilter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author manuelpalumbo
 */
public class ConsultaUsuario extends javax.swing.JInternalFrame {
    IController ico;
    List<Object[]> rows;
    public ConsultaUsuario() {
        rows = new ArrayList();
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
        this.setClosable(true);   
        List<DTMaster> listUsuario = ico.ListarClase(EnumDT.DT_USUARIO);
        

        listUsuario = OrdenarLista(listUsuario);
        IniciarRows(listUsuario);
        IniciarTable();
    }
    
    private void IniciarRows(List<DTMaster> listUsu){
        for(int i = 0;i<listUsu.size();i++){
            DTMaster dt = listUsu.get(i);
            if(dt instanceof DTUsuarioBase dti){
                if(i==0){
                    rows = new ArrayList();
                }
                if(dti instanceof DTDocente){
                    Object[] row = {dti.getNickname(),dti.getCorreo(),dti.getNombre(),dti.getApellido(), true};
                    rows.add(row);
                }else{
                    Object[] row = {dti.getNickname(),dti.getCorreo(),dti.getNombre(),dti.getApellido(), false};
                    rows.add(row);
                }
                
                
            }
        }
    }
    
    private List<DTMaster> OrdenarLista(List<DTMaster> listaParam){
        List<DTMaster> auxDT = listaParam;
        for(int i = 0;i<auxDT.size()-1;i++){
            for(int j = 0;j<auxDT.size()-1;j++){
                if(auxDT instanceof DTUsuarioBase){
                    DTUsuarioBase aux = (DTUsuarioBase)auxDT.get(j);
                    DTUsuarioBase auxJMas = (DTUsuarioBase)auxDT.get(j+1);
                    if(aux.getNickname().toLowerCase().compareTo(auxJMas.getNickname().toLowerCase()) > 0){
                        DTUsuarioBase temp = aux;
                        auxDT.set(j, auxDT.get(j+1));
                        auxDT.set(j+1,temp);  
                    }
                }
            }
        }
        return auxDT;
    }
    
    
    private void IniciarTable(){
        DefaultTableModel modelo = (DefaultTableModel) tableUsuario.getModel();
        modelo.setRowCount(0);
        for(int i =0;i<rows.size();i++){
            modelo.addRow(rows.get(i));
        }
    }

    public void MostrarDataCompleta(String nick){
        DTUsuarioBase dt = ico.ConsultarUsuario(nick);
        MiniInterfazDeConsultaUsuario micu = new MiniInterfazDeConsultaUsuario();
        this.getDesktopPane().add(micu);
        micu.setVisible(true);
        micu.toFront();
        micu.ColocarDatos(dt);
        
    }
    
    
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        tableUsuario = new javax.swing.JTable();
        fieldBusqueda = new javax.swing.JTextField();
        boxTipoBusqueda = new javax.swing.JComboBox<>();
        labelText = new javax.swing.JLabel();

        tableUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nikname", "Correo", "Nombre", "Apellido", "Docente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableUsuario.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tableUsuario.getSelectedRow();
                if (fila != -1) {
                    String objNick = (String)tableUsuario.getValueAt(fila, 0);
                    MostrarDataCompleta(objNick);
                }
            }
        });
        jScrollPane3.setViewportView(tableUsuario);
        if (tableUsuario.getColumnModel().getColumnCount() > 0) {
            tableUsuario.getColumnModel().getColumn(0).setResizable(false);
            tableUsuario.getColumnModel().getColumn(1).setResizable(false);
            tableUsuario.getColumnModel().getColumn(2).setResizable(false);
            tableUsuario.getColumnModel().getColumn(3).setResizable(false);
            tableUsuario.getColumnModel().getColumn(4).setResizable(false);
        }

        fieldBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                FiltrarUsuarios();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                FiltrarUsuarios();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        boxTipoBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nickname", "Correo" }));

        labelText.setText("Buscar por:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(boxTipoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxTipoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelText)
                    .addComponent(fieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void FiltrarUsuarios(){
        int valorPorCualBuscar = boxTipoBusqueda.getSelectedIndex();
        DefaultTableModel modelo = (DefaultTableModel)tableUsuario.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tableUsuario.setRowSorter(sorter);
        
        String texto = fieldBusqueda.getText();
        if (texto.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, valorPorCualBuscar));
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxTipoBusqueda;
    private javax.swing.JTextField fieldBusqueda;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelText;
    private javax.swing.JTable tableUsuario;
    // End of variables declaration//GEN-END:variables
}
