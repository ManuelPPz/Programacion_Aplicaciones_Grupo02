package interfaces;
import DTsClasses.DTCurso;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import DTsClasses.DTUsuario;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import Logica.Fabric;
import Logica.IController;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author mateo
 */
public class InscripcionEdicionCurso extends javax.swing.JInternalFrame {
    IController ico;
    List<Object[]> rowsCursos;
    List<Object[]> rowsEdiciones;
    List<Object[]> rowsUsuario;
    public InscripcionEdicionCurso() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
        //Tener lista de cursos en la plataforma
        List<DTMaster> auxInsCur = ico.ListarClase(EnumDT.DT_CURSO);     
        if(auxInsCur!=null){
            auxInsCur = OrdenarLista(auxInsCur);
            IniciarRows(auxInsCur);
            IniciarTable(EnumDT.DT_CURSO);
        }
        //Tener lista de usuarios en la plataforma
        List<DTMaster> auxUsuarios = ico.ListarClase(EnumDT.DT_USUARIO);
        if(auxUsuarios!=null){
            auxUsuarios = OrdenarLista(auxUsuarios);
            IniciarRows(auxUsuarios);
            IniciarTable(EnumDT.DT_USUARIO);
        }
        
        
        
    }
    
    private void IniciarRows(List<DTMaster> listCur){
        for(int i = 0;i<listCur.size();i++){
            DTMaster dt = listCur.get(i);
            if(dt instanceof DTCurso dti){
                if(i==0){
                    rowsCursos = new ArrayList();
                }
                if(dti instanceof DTCurso){
                    Object[] row = {dti.getInstituto(),dti.getNombre()};
                    rowsCursos.add(row);
                }
            }else if(dt instanceof DTEdicionCurso dti){
                if(i==0){
                    rowsEdiciones = new ArrayList();
                }
                if(dti instanceof DTEdicionCurso){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date fAlta = dti.getFechaAlta();
                    String fechaA = sdf.format(fAlta);
                    Date fFin = dti.getFFin();
                    String fechaF = sdf.format(fFin);
                    Date fNow = new Date();
                    if(fNow.before(fFin)){
                        Object[] row = {dti.getNombre(),fechaA,fechaF};
                        rowsEdiciones.add(row);
                    }                   
                }
            }else if(dt instanceof DTUsuarioBase dti){
                if(i==0){
                    rowsUsuario = new ArrayList();
                }
                if(dti instanceof DTUsuario){
                    Object[] row = {dti.getNickname(),dti.getNombre(), dti.getApellido()};
                    rowsCursos.add(row);
                }
            }
        }
    }
    private List<DTMaster> OrdenarLista(List<DTMaster> listaParam){
        List<DTMaster> auxDT = listaParam;
        for(int i = 0;i<auxDT.size()-1;i++){
            for(int j = 0;j<auxDT.size()-1;j++){
                if(auxDT instanceof DTCurso){
                    DTCurso aux = (DTCurso)auxDT.get(j);
                    DTCurso auxJMas = (DTCurso)auxDT.get(j+1);
                    if(aux.getInstituto().toLowerCase().compareTo(auxJMas.getInstituto().toLowerCase()) > 0){
                        DTCurso temp = aux;
                        auxDT.set(j, auxDT.get(j+1));
                        auxDT.set(j+1,temp);  
                    }
                }else if(auxDT instanceof DTEdicionCurso){
                    DTEdicionCurso aux = (DTEdicionCurso)auxDT.get(j);
                    DTEdicionCurso auxJMas = (DTEdicionCurso)auxDT.get(j+1);
                    if(aux.getNombre().toLowerCase().compareTo(auxJMas.getNombre().toLowerCase()) > 0){
                        DTEdicionCurso temp = aux;
                        auxDT.set(j, auxDT.get(j+1));
                        auxDT.set(j+1,temp);  
                    }
                }else if(auxDT instanceof DTUsuarioBase){
                    if(auxDT instanceof DTUsuario){
                        DTUsuario aux = (DTUsuario)auxDT.get(j);
                        DTUsuario auxJMas = (DTUsuario)auxDT.get(j+1);
                        if(aux.getNickname().toLowerCase().compareTo(auxJMas.getNickname().toLowerCase()) > 0){
                            DTUsuario temp = aux;
                            auxDT.set(j, auxDT.get(j+1));
                            auxDT.set(j+1,temp);  
                        }
                    }
                }
            }
        }
        return auxDT;
    }
    
    private void IniciarTable(EnumDT enumType){
        if(enumType == EnumDT.DT_CURSO){
            DefaultTableModel modelo = (DefaultTableModel) tableInsCur.getModel();
            modelo.setRowCount(0);
            for(int i =0;i<rowsCursos.size();i++){
                modelo.addRow(rowsCursos.get(i));
            }
        }else if(enumType == EnumDT.DT_EDICION){
            DefaultTableModel modelo = (DefaultTableModel) tableEdiciones.getModel();
            modelo.setRowCount(0);
            for(int i =0;i<rowsEdiciones.size();i++){
                modelo.addRow(rowsEdiciones.get(i));
            }
        }else if(enumType == EnumDT.DT_USUARIO){
            DefaultTableModel modelo = (DefaultTableModel) tableUsuarios.getModel();
            modelo.setRowCount(0);
            for(int i =0;i<rowsUsuario.size();i++){
                modelo.addRow(rowsUsuario.get(i));
            }
        }
        
    }
    private void Inscribir(int filaEdi, int filaUsu){
        DefaultTableModel modeloEdi = (DefaultTableModel) tableEdiciones.getModel();
        DefaultTableModel modeloUsu = (DefaultTableModel) tableUsuarios.getModel();
        ico.InscripcionAEdicionCurso((String)modeloEdi.getValueAt(filaEdi, 0), (String)modeloUsu.getValueAt(filaUsu, 0), (Date)spinDateIns.getValue());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableInsCur = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableEdiciones = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableUsuarios = new javax.swing.JTable();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        spinDateIns = new javax.swing.JSpinner();

        setPreferredSize(new java.awt.Dimension(412, 500));

        tableInsCur.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Instituto", "Curso"
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
        tableInsCur.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tableInsCur.getSelectedRow();
                if (fila != -1) {
                    String objNom = (String)tableInsCur.getValueAt(fila, 1);

                    List<DTMaster> listEdi = ico.ListarEdiciones(objNom);
                    if(!listEdi.isEmpty()){
                        listEdi = OrdenarLista(listEdi);
                        IniciarRows(listEdi);
                        IniciarTable(EnumDT.DT_EDICION);
                    }else{
                        DefaultTableModel modelo = (DefaultTableModel) tableEdiciones.getModel();
                        modelo.setRowCount(0);
                    }

                }
            }
        });
        jScrollPane1.setViewportView(tableInsCur);
        if (tableInsCur.getColumnModel().getColumnCount() > 0) {
            tableInsCur.getColumnModel().getColumn(0).setResizable(false);
            tableInsCur.getColumnModel().getColumn(1).setResizable(false);
        }

        tableEdiciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Inicio", "Fin"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableEdiciones);
        if (tableEdiciones.getColumnModel().getColumnCount() > 0) {
            tableEdiciones.getColumnModel().getColumn(0).setResizable(false);
            tableEdiciones.getColumnModel().getColumn(1).setResizable(false);
            tableEdiciones.getColumnModel().getColumn(2).setResizable(false);
        }

        tableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Nombre", "Apellido"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableUsuarios);
        if (tableUsuarios.getColumnModel().getColumnCount() > 0) {
            tableUsuarios.getColumnModel().getColumn(0).setResizable(false);
            tableUsuarios.getColumnModel().getColumn(1).setResizable(false);
            tableUsuarios.getColumnModel().getColumn(2).setResizable(false);
        }

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(this::btnAceptarActionPerformed);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        spinDateIns.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, new java.util.Date(), java.util.Calendar.DAY_OF_MONTH));
        spinDateIns.setEditor(new javax.swing.JSpinner.DateEditor(spinDateIns, "dd/MM/yyyy"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAceptar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spinDateIns, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(spinDateIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar)
                    .addComponent(btnCancelar))
                .addGap(128, 128, 128))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        int filaCur = tableInsCur.getSelectedRow();
        int filaEdi = tableEdiciones.getSelectedRow();
        int filaUsu = tableUsuarios.getSelectedRow();
        if(filaCur!=-1){
            if(filaEdi!=-1){
                if(filaUsu!=-1){
                    Inscribir(filaEdi,filaUsu);
                }else{
                    javax.swing.JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar un usuario para continuar.", 
                    "Atención", 
                    javax.swing.JOptionPane.WARNING_MESSAGE); 
                }
            }else{
                javax.swing.JOptionPane.showMessageDialog(this, 
                "Debe seleccionar una Edicion de curso para continuar.", 
                "Atención", 
                javax.swing.JOptionPane.WARNING_MESSAGE); 
            }
        }else{
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un Curso para continuar.", 
                "Atención", 
                javax.swing.JOptionPane.WARNING_MESSAGE);        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // Cierra y destruye la ventana interna actual
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner spinDateIns;
    private javax.swing.JTable tableEdiciones;
    private javax.swing.JTable tableInsCur;
    private javax.swing.JTable tableUsuarios;
    // End of variables declaration//GEN-END:variables
}
