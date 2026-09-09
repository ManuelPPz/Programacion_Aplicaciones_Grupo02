package interfaces;
import DTsClasses.DTCurso;
import DTsClasses.DTEdicionCurso;
import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
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
public class InscripcionProgramaDeFormacion extends javax.swing.JInternalFrame {
    IController ico;
    List<Object[]> rowsProgramas;
    List<Object[]> rowsUsuario;
    public InscripcionProgramaDeFormacion() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
        rowsProgramas = new ArrayList<>();
        rowsUsuario = new ArrayList<>();
        //Tener lista de cursos en la plataforma
        List<DTMaster> auxPrograma = ico.ListarClase(EnumDT.DT_PROGRAMA);     
        if(auxPrograma!=null){
            auxPrograma = OrdenarLista(auxPrograma);
            IniciarRows(auxPrograma);
            IniciarTable(EnumDT.DT_PROGRAMA);
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
            if(dt instanceof DTProgramaForm dti){
                if(i==0){
                    rowsProgramas = new ArrayList();
                }
                if(dti instanceof DTProgramaForm){
                    Object[] row = {dti.getNombre(),dti.getVigenciaProg().getFechaInicio(),dti.getVigenciaProg().getFechaFin()};
                    rowsProgramas.add(row);
                }
            }else if(dt instanceof DTUsuarioBase dti){
                if(i==0){
                    rowsUsuario = new ArrayList();
                }
                if(dti instanceof DTUsuario){
                    Object[] row = {dti.getNickname(),dti.getNombre(), dti.getApellido()};
                    rowsUsuario.add(row);
                }
            }
        }
    }
    private List<DTMaster> OrdenarLista(List<DTMaster> listaParam){
        List<DTMaster> auxDT = listaParam;
        for(int i = 0;i<auxDT.size()-1;i++){
            for(int j = 0;j<auxDT.size()-1;j++){
                if(auxDT instanceof DTProgramaForm){
                    DTProgramaForm aux = (DTProgramaForm)auxDT.get(j);
                    DTCurso auxJMas = (DTCurso)auxDT.get(j+1);
                    if(aux.getNombre().toLowerCase().compareTo(auxJMas.getNombre().toLowerCase()) > 0){
                        DTProgramaForm temp = aux;
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
        if(enumType == EnumDT.DT_PROGRAMA){
            DefaultTableModel modelo = (DefaultTableModel) tableProgramas.getModel();
            modelo.setRowCount(0);
            for(int i =0;i<rowsProgramas.size();i++){
                modelo.addRow(rowsProgramas.get(i));
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
        DefaultTableModel modeloEdi = (DefaultTableModel) tableProgramas.getModel();
        DefaultTableModel modeloUsu = (DefaultTableModel) tableUsuarios.getModel();
        ico.InscripcionUsuarioAProgramas((String)modeloEdi.getValueAt(filaEdi, 0), (String)modeloUsu.getValueAt(filaUsu, 0), (Date)spinDateIns.getValue());
        javax.swing.JOptionPane.showMessageDialog(this, 
                    "Usuario "+(String)modeloUsu.getValueAt(filaUsu, 0)+" Inscripto con exito.", 
                    "Atención", 
                    javax.swing.JOptionPane.WARNING_MESSAGE); 
        this.dispose();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tableProgramas = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableUsuarios = new javax.swing.JTable();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        spinDateIns = new javax.swing.JSpinner();

        setPreferredSize(new java.awt.Dimension(412, 500));

        tableProgramas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tableProgramas);
        if (tableProgramas.getColumnModel().getColumnCount() > 0) {
            tableProgramas.getColumnModel().getColumn(0).setResizable(false);
            tableProgramas.getColumnModel().getColumn(1).setResizable(false);
            tableProgramas.getColumnModel().getColumn(2).setResizable(false);
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(spinDateIns, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAceptar)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(spinDateIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAceptar)
                            .addComponent(btnCancelar))))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        int filaProg = tableProgramas.getSelectedRow();
        int filaUsu = tableUsuarios.getSelectedRow();
        if(filaProg!=-1){
            if(filaUsu!=-1){
                    Inscribir(filaProg,filaUsu);
                }else{
                    javax.swing.JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar un usuario para continuar.", 
                    "Atención", 
                    javax.swing.JOptionPane.WARNING_MESSAGE); 
                }
        }else{
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un Programa para continuar.", 
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner spinDateIns;
    private javax.swing.JTable tableProgramas;
    private javax.swing.JTable tableUsuarios;
    // End of variables declaration//GEN-END:variables
}
