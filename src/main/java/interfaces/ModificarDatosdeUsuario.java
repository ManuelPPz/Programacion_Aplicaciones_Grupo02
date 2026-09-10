/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import DTsClasses.DTDocente;
import DTsClasses.DTInstituto;
import DTsClasses.DTMaster;
import DTsClasses.DTUsuarioBase;
import DTsClasses.EnumDT;
import Logica.Fabric;
import Logica.IController;
/**
 *
 * @author leoli
 */
public class ModificarDatosdeUsuario extends javax.swing.JInternalFrame {
    IController ico;
    List<Object[]> rows;

    //Datos del usuario que se esta editando actualmente
    String nicknameActual;
    String correoActual;
    boolean esDocenteActual;

    /**
     * Creates new form ModificarDatosdeUsuario
     */
    public ModificarDatosdeUsuario() {
        initComponents();
        rows = new ArrayList();
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
        this.setClosable(true);
        this.setMaximizable(true);
        this.setResizable(true);
        this.setTitle("Modificar Datos de Usuario");

        CargarUsuarios();
        panelEdicion.setVisible(false);
        tableUsuarios.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int fila = tableUsuarios.getSelectedRow();
            if (fila != -1) {
                // Obtiene el Nickname de la columna 0
                String nick = (String) tableUsuarios.getValueAt(fila, 0);
                // Carga los datos del usuario y hace visible el panelEdicion
                MostrarDatosParaEditar(nick);
            }
        }
    });
    }
     private void CargarUsuarios(){
        List<DTMaster> listUsuario = ico.ListarClase(EnumDT.DT_USUARIO);
        rows = new ArrayList();
        for(int i = 0;i<listUsuario.size();i++){
            DTMaster dt = listUsuario.get(i);
            if(dt instanceof DTUsuarioBase dti){
                Object[] row = {dti.getNickname(), dti.getCorreo(), dti.getNombre(), dti.getApellido(), (dti instanceof DTDocente)};
                rows.add(row);
            }
        }
        IniciarTable();
    }
    private void IniciarTable(){
        DefaultTableModel modelo = (DefaultTableModel) tableUsuarios.getModel();
        modelo.setRowCount(0);
        for(int i = 0;i<rows.size();i++){
            modelo.addRow(rows.get(i));
        }
    }
    private void FiltrarUsuarios(){
        if(fieldBusqueda.getText().isEmpty()){
            IniciarTable();
        }else{
            DefaultTableModel modelo = (DefaultTableModel) tableUsuarios.getModel();
            modelo.setRowCount(0);
            //Index==0 nickname, Index==1 correo
            int columna = boxTipoBusqueda.getSelectedIndex();
            String inputField = fieldBusqueda.getText().toLowerCase();
            for(int i = 0;i<rows.size();i++){
                Object[] fila = rows.get(i);
                String valor = ((String) fila[columna]).toLowerCase();
                if(valor.startsWith(inputField)){
                    modelo.insertRow(0, fila);
                }else if(valor.contains(inputField)){
                    modelo.addRow(fila);
                }
            }
        }
    }
    private void CargarInstitutosParaEdicion(List<String> institutosDocente){
        comboInstituto.removeAllItems();
        List<DTMaster> listIns = ico.ListarClase(EnumDT.DT_INSTITUTO);
        for(int i = 0;i<listIns.size();i++){
            DTInstituto dt = (DTInstituto) listIns.get(i);
            String nom = dt.getNombre();
            if(institutosDocente!=null && institutosDocente.contains(nom)){
                comboInstituto.addItem("✓ " + nom);
            }else{
                comboInstituto.addItem(nom);
            }
        }
    }
    private void MostrarDatosParaEditar(String nickname){
        DTUsuarioBase dt = ico.ConsultarUsuario(nickname);
        if(dt==null){
            return;
        }
        nicknameActual = dt.getNickname();
        correoActual = dt.getCorreo();

        labelNickname.setText(dt.getNickname());
        labelCorreo.setText(dt.getCorreo());
        txtNombre.setText(dt.getNombre());
        txtApellido.setText(dt.getApellido());
        spinnerFecha.setValue(dt.getFNac());
        fieldPath.setText("");

        if(dt instanceof DTDocente docenteDT){
            esDocenteActual = true;
            lblTipo.setText("Docente");
            lblInstituto.setVisible(true);
            comboInstituto.setVisible(true);
            CargarInstitutosParaEdicion(docenteDT.getInstitutos());
        }else{
            esDocenteActual = false;
            lblTipo.setText("Estudiante");
            lblInstituto.setVisible(false);
            comboInstituto.setVisible(false);
        }

        panelEdicion.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        boxTipoBusqueda = new javax.swing.JComboBox<>();
        fieldBusqueda = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUsuarios = new javax.swing.JTable();
        panelEdicion = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblInstituto = new javax.swing.JLabel();
        labelNickname = new javax.swing.JLabel();
        labelCorreo = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        spinnerFecha = new javax.swing.JSpinner();
        comboInstituto = new javax.swing.JComboBox<>();
        fieldPath = new javax.swing.JTextField();
        buttonChooser = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Modificar Datos de Usuario");

        jLabel1.setText("Buscar por:");

        boxTipoBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nickname ", "Correo" }));

        fieldBusqueda.setColumns(15);

        tableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nickname", "Correo", "Nombre", "Apellido", "Docente"
            }
        ));
        jScrollPane1.setViewportView(tableUsuarios);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(27, 27, 27)
                        .addComponent(boxTipoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(boxTipoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        panelEdicion.setBorder(javax.swing.BorderFactory.createTitledBorder("Editar datos de usuario"));

        jLabel2.setText("Nickname");

        jLabel3.setText("Correo");

        jLabel4.setText("Nombre");

        jLabel5.setText("Fecha Nac");

        jLabel6.setText("Nueva Imagen");

        jLabel7.setText("Tipo:");

        jLabel8.setText("Apellido:");

        lblInstituto.setText("Instituto:");

        labelNickname.setText("jLabel10");

        labelCorreo.setText("jLabel10");

        lblTipo.setText("jLabel10");

        txtNombre.setMaximumSize(new java.awt.Dimension(64, 23));
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        txtNombre.setColumns(6);

        txtApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoActionPerformed(evt);
            }
        });
        txtNombre.setColumns(6);

        spinnerFecha.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), new java.util.Date(652369140000L), new java.util.Date(), java.util.Calendar.DAY_OF_MONTH));
        spinnerFecha.setEditor(new javax.swing.JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy"));

        comboInstituto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboInstituto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboInstitutoActionPerformed(evt);
            }
        });

        buttonChooser.setText("...");
        buttonChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooserActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(255, 102, 102));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEdicionLayout = new javax.swing.GroupLayout(panelEdicion);
        panelEdicion.setLayout(panelEdicionLayout);
        panelEdicionLayout.setHorizontalGroup(
            panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEdicionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEdicionLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(fieldPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonChooser))
                    .addGroup(panelEdicionLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(240, 240, 240)
                        .addComponent(btnCancelar)
                        .addGap(18, 18, 18)
                        .addComponent(btnAceptar))
                    .addGroup(panelEdicionLayout.createSequentialGroup()
                        .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(36, 36, 36)
                        .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelEdicionLayout.createSequentialGroup()
                                .addComponent(spinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblInstituto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelEdicionLayout.createSequentialGroup()
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelCorreo)
                            .addGroup(panelEdicionLayout.createSequentialGroup()
                                .addComponent(labelNickname)
                                .addGap(74, 74, 74)
                                .addComponent(jLabel7)
                                .addGap(62, 62, 62)
                                .addComponent(lblTipo))))))
        );
        panelEdicionLayout.setVerticalGroup(
            panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEdicionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(labelNickname)
                    .addComponent(lblTipo))
                .addGap(18, 18, 18)
                .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labelCorreo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(spinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInstituto)
                    .addComponent(comboInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(fieldPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonChooser))
                .addGap(18, 18, 18)
                .addGroup(panelEdicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnAceptar))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEdicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelEdicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoActionPerformed

    private void buttonChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooserActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes JPG y PNG", "jpg", "png"));
        int resultado = chooser.showOpenDialog(this);
        if(resultado == JFileChooser.APPROVE_OPTION){
            File archivo = chooser.getSelectedFile();
            fieldPath.setText(archivo.getAbsolutePath());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonChooserActionPerformed

    private void comboInstitutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboInstitutoActionPerformed
       if(comboInstituto.getSelectedItem()==null){
            return;
        }
        String auxString = comboInstituto.getSelectedItem().toString();
        int auxInt = comboInstituto.getSelectedIndex();
        if(auxString.contains("✓")){
            String auxSubString = auxString.substring(2);
            comboInstituto.removeItemAt(auxInt);
            comboInstituto.insertItemAt(auxSubString, auxInt);
        }else{
            auxString = "✓ " + auxString;
            comboInstituto.removeItemAt(auxInt);
            comboInstituto.insertItemAt(auxString, auxInt);
            comboInstituto.setSelectedIndex(auxInt);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_comboInstitutoActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        if(nicknameActual==null){
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un usuario de la tabla.",
                "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();

        if(nombre.isEmpty() || apellido.isEmpty()){
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos obligatorios.",
                "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(esDocenteActual && comboInstituto.getItemCount()>0){
            boolean algunoSeleccionado = false;
            for(int i = 0;i<comboInstituto.getItemCount();i++){
                if(((String)comboInstituto.getItemAt(i)).contains("✓")){
                    algunoSeleccionado = true;
                    break;
                }
            }
            if(!algunoSeleccionado){
                JOptionPane.showMessageDialog(this,
                    "El docente debe pertenecer al menos a un instituto.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        Date fecha = (Date) spinnerFecha.getValue();
        String imgPath = fieldPath.getText();

        try{
            List<String> institutos = null;
            if(esDocenteActual){
                institutos = new ArrayList();
                for(int i = 0;i<comboInstituto.getItemCount();i++){
                    String item = (String) comboInstituto.getItemAt(i);
                    if(item.contains("✓")){
                        institutos.add(item.substring(2));
                    }
                }
            }

            ico.ModificarUsuario(nicknameActual, nombre, apellido, correoActual, esDocenteActual, fecha, institutos, imgPath);

            JOptionPane.showMessageDialog(this,
                "Los datos del usuario '" + nicknameActual + "' se actualizaron con éxito.",
                "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            CargarUsuarios();
            panelEdicion.setVisible(false);
            nicknameActual = null;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al modificar los datos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
// TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxTipoBusqueda;
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton buttonChooser;
    private javax.swing.JComboBox<String> comboInstituto;
    private javax.swing.JTextField fieldBusqueda;
    private javax.swing.JTextField fieldPath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCorreo;
    private javax.swing.JLabel labelNickname;
    private javax.swing.JLabel lblInstituto;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JPanel panelEdicion;
    private javax.swing.JSpinner spinnerFecha;
    private javax.swing.JTable tableUsuarios;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
