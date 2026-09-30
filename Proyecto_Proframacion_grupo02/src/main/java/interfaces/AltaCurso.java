/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import DTsClasses.DTCurso;
import DTsClasses.DTDocente;
import DTsClasses.DTInstituto;
import DTsClasses.DTCategoria;
import DTsClasses.DTMaster;
import DTsClasses.EnumDT;
import Logica.Fabric;
import Logica.IController;
import com.toedter.calendar.JCalendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author mateo
 */
public class AltaCurso extends javax.swing.JInternalFrame {

    IController ico;
    List<Object[]> rowsDocente;
    List<Object[]> rowsCursos;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DefaultTableModel modeloDocente;
    TableRowSorter<DefaultTableModel> sorterDocente;
    
    DefaultTableModel modeloCurso;
    TableRowSorter<DefaultTableModel> sorterCurso;
    JCalendar popupCalendar;
    public AltaCurso() {
        rowsDocente = new ArrayList<>();
        rowsCursos = new ArrayList<>(); // Es recomendable inicializar ambas listas aquí

        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();

        // 1. Inicializar componentes visuales primero
        popupCalendar = new JCalendar();
        initComponents();

        // 2. Vincular modelos y sorters después de initComponents
        modeloDocente = (DefaultTableModel) tableDocentes.getModel();
        sorterDocente = new TableRowSorter<>(modeloDocente);

        modeloCurso = (DefaultTableModel) tableCursos.getModel();
        sorterCurso = new TableRowSorter<>(modeloCurso);

        // 3. Asignar listeners de filtrado al final, cuando sorterCurso y sorterDocente NO son null
        fieldCurso.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { FiltrarCurso(); }
            @Override
            public void removeUpdate(DocumentEvent e) { FiltrarCurso(); }
            @Override
            public void changedUpdate(DocumentEvent e) { }
        });

        fieldDocente.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { FiltrarDocente(); }
            @Override
            public void removeUpdate(DocumentEvent e) { FiltrarDocente(); }
            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
    }

    private void IniciarRows(List<DTMaster> list) {
        
        for (int i = 0; i < list.size(); i++) {
            DTMaster dt = list.get(i);
            if (dt instanceof DTDocente dti) {
                if (i == 0) {
                    rowsDocente = new ArrayList<>();
                }
                Object[] row = {dti.getNickname()};
                rowsDocente.add(row);
            } else if (dt instanceof DTCurso dti) {
                if (i == 0) {
                    rowsCursos = new ArrayList<>();
                }
                Object[] row = {dti.getNombre(), false};
                rowsCursos.add(row);
            }
        }
    }

    private List<DTMaster> OrdenarLista(List<DTMaster> listaParam) {
        List<DTMaster> auxDT = listaParam;
        for (int i = 0; i < auxDT.size() - 1; i++) {
            for (int j = 0; j < auxDT.size() - 1; j++) {
                if (auxDT.get(j) instanceof DTDocente) {
                    DTDocente aux = (DTDocente) auxDT.get(j);
                    DTDocente auxJMas = (DTDocente) auxDT.get(j + 1);
                    if (aux.getNombre().toLowerCase().compareTo(auxJMas.getNombre().toLowerCase()) > 0) {
                        DTDocente temp = aux;
                        auxDT.set(j, auxDT.get(j + 1));
                        auxDT.set(j + 1, temp);
                    }
                } else if (auxDT.get(j) instanceof DTCurso) {
                    DTCurso aux = (DTCurso) auxDT.get(j);
                    DTCurso auxJMas = (DTCurso) auxDT.get(j + 1);
                    if (aux.getNombre().toLowerCase().compareTo(auxJMas.getNombre().toLowerCase()) > 0) {
                        DTCurso temp = aux;
                        auxDT.set(j, auxDT.get(j + 1));
                        auxDT.set(j + 1, temp);
                    }
                }
            }
        }
        return auxDT;
    }

    private void IniciarTable(EnumDT tipoDT) {
        if (tipoDT == EnumDT.DT_USUARIO) {
            DefaultTableModel modelo = (DefaultTableModel) tableDocentes.getModel();
            modelo.setRowCount(0);
            for (int i = 0; i < rowsDocente.size(); i++) {
                modelo.addRow(rowsDocente.get(i));
            }
        } else if (tipoDT == EnumDT.DT_CURSO) {
            DefaultTableModel modelo = (DefaultTableModel) tableCursos.getModel();
            modelo.setRowCount(0);
            for (int i = 0; i < rowsCursos.size(); i++) {
                modelo.addRow(rowsCursos.get(i));
            }
        }
    }

    public void FiltrarDocente() {
        //Para filtrar los docentes
        if(fieldDocente.getForeground()==new Color(204,204,204)){
            return;
        }
        tableDocentes.setRowSorter(sorterDocente);

        String texto = fieldDocente.getText();
        if (texto.trim().length() == 0) {
            sorterDocente.setRowFilter(null);
        } else {
            sorterDocente.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
        }
    }

    public void FiltrarCurso() {
        //Para filtrar los cursos
        if(fieldCurso.getForeground()==new Color(204,204,204)){
            return;
        }
        tableCursos.setRowSorter(sorterCurso);

        String texto = fieldCurso.getText();
        if (texto.trim().length() == 0) {
            sorterCurso.setRowFilter(null);
        } else {
            sorterCurso.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
        }
    }
    public void ResetTable(EnumDT tipoDT){
        if (tipoDT == EnumDT.DT_USUARIO) {
            DefaultTableModel modelo = (DefaultTableModel) tableDocentes.getModel();
            modelo.setRowCount(0);
        } else if (tipoDT == EnumDT.DT_CURSO) {
            DefaultTableModel modelo = (DefaultTableModel) tableCursos.getModel();
            modelo.setRowCount(0);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fieldNombre = new javax.swing.JTextField();
        boxInstituto = new javax.swing.JComboBox<>();
        spinnerHoras = new javax.swing.JSpinner();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaDescripcion = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        spinnerCreditos = new javax.swing.JSpinner();
        spinnerDuracion = new javax.swing.JSpinner();
        fieldCurso = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableCursos = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fieldDocente = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableDocentes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        fieldURL = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        buttonAceptar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        fieldFecha = new javax.swing.JTextField();
        comboCategoria = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(423, 611));
        setMinimumSize(new java.awt.Dimension(423, 611));
        setName(""); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fieldNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldNombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldNombreFocusLost(evt);
            }
        });
        fieldNombre.addActionListener(this::fieldNombreActionPerformed);
        getContentPane().add(fieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 44, 254, -1));

        boxInstituto.addActionListener(this::boxInstitutoActionPerformed);
        getContentPane().add(boxInstituto, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 16, 254, -1));
        List<DTMaster> auxListIns = ico.ListarClase(EnumDT.DT_INSTITUTO);

        boxInstituto.insertItemAt("SIN DATOS", 0);
        for(int i = 0;i<auxListIns.size();i++){
            DTInstituto dt = (DTInstituto)auxListIns.get(i);
            boxInstituto.insertItemAt(dt.getNombre(), i+1);
        }
        boxInstituto.setSelectedIndex(0);

        spinnerHoras.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        getContentPane().add(spinnerHoras, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 184, 48, 38));

        areaDescripcion.setColumns(20);
        areaDescripcion.setRows(5);
        jScrollPane2.setViewportView(areaDescripcion);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 83, 254, -1));

        jLabel5.setText("Cant. Creditos*");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 192, 100, -1));

        spinnerCreditos.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        spinnerCreditos.setToolTipText("");
        getContentPane().add(spinnerCreditos, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 184, 48, 38));

        spinnerDuracion.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        getContentPane().add(spinnerDuracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 181, 48, 38));

        fieldCurso.setForeground(new java.awt.Color(204, 204, 204));
        fieldCurso.setText("Previas...");
        fieldCurso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldCursoFocusGained(evt);
            }
        });
        getContentPane().add(fieldCurso, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 358, 200, 23));

        tableCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Previas", "Seleccionado"
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
        jScrollPane7.setViewportView(tableCursos);

        getContentPane().add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 200, 138));

        jLabel11.setText("Cant. Horas*");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 192, 80, -1));

        jLabel13.setText("URL*");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 236, 72, -1));

        fieldDocente.setForeground(new java.awt.Color(204, 204, 204));
        fieldDocente.setText("Docente...");
        fieldDocente.setMinimumSize(new java.awt.Dimension(64, 10));
        fieldDocente.setPreferredSize(new java.awt.Dimension(68, 9));
        fieldDocente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldDocenteFocusGained(evt);
            }
        });
        fieldDocente.addActionListener(this::fieldDocenteActionPerformed);
        getContentPane().add(fieldDocente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 358, 120, 23));

        tableDocentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Docentes"
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
        jScrollPane8.setViewportView(tableDocentes);
        if (tableDocentes.getColumnModel().getColumnCount() > 0) {
            tableDocentes.getColumnModel().getColumn(0).setResizable(false);
        }

        getContentPane().add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 120, 138));

        jLabel1.setText("Instituto*");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 19, 61, -1));

        fieldURL.addActionListener(this::fieldURLActionPerformed);
        getContentPane().add(fieldURL, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 233, 247, -1));

        jLabel2.setText("Nombre*");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 47, 61, -1));

        buttonAceptar.setText("Aceptar");
        buttonAceptar.addActionListener(this::buttonAceptarActionPerformed);
        getContentPane().add(buttonAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 527, 90, -1));

        jLabel3.setText("Descripcion*");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 83, 90, -1));

        btnCancelar.setForeground(new java.awt.Color(255, 102, 102));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);
        getContentPane().add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 527, 90, -1));

        jLabel4.setText("Duracion*");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 192, 60, -1));

        jLabel15.setText("Categoria/s");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 101, 20));

        fieldFecha.setMaximumSize(new java.awt.Dimension(64, 22));
        fieldFecha.setPreferredSize(new java.awt.Dimension(64, 18));
        fieldFecha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldFechaMouseClicked(evt);
            }
        });
        getContentPane().add(fieldFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 275, 250, 20));

        comboCategoria.addActionListener(this::comboCategoriaActionPerformed);
        List<DTMaster> auxListCat = ico.ListarClase(EnumDT.DT_CATEGORIA);
        comboCategoria.insertItemAt("Seleccionar...", 0);
        for(int i = 0;i<auxListCat.size();i++){
            DTMaster dt = auxListCat.get(i);
            if(dt instanceof DTCategoria dtc){
                comboCategoria.insertItemAt(dtc.getNombre(), i+1);
            }

        }
        comboCategoria.setSelectedIndex(0);
        getContentPane().add(comboCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 200, 20));

        jLabel16.setText("Fecha de alta*");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 275, 107, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNombreFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNombreFocusGained

    private void fieldNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNombreFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNombreFocusLost

    private void fieldNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNombreActionPerformed

    private void boxInstitutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxInstitutoActionPerformed
        if (boxInstituto.getSelectedIndex() != 0) {
            String auxStr = (String) boxInstituto.getSelectedItem();
            List<DTMaster> listDocente = ico.ListarDocentes(auxStr);
            if (!listDocente.isEmpty()) {
                listDocente = OrdenarLista(listDocente);
                IniciarRows(listDocente);
                IniciarTable(EnumDT.DT_USUARIO);
            }else{
                ResetTable(EnumDT.DT_USUARIO);
            }
            List<DTMaster> listCursos = ico.ListarCursos(auxStr);
            if (!listCursos.isEmpty()) {
                listCursos = OrdenarLista(listCursos);
                IniciarRows(listCursos);
                IniciarTable(EnumDT.DT_CURSO);
            }else{
                ResetTable(EnumDT.DT_CURSO);
            }
        }
    }//GEN-LAST:event_boxInstitutoActionPerformed

    private void fieldURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldURLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldURLActionPerformed

    private void buttonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAceptarActionPerformed
        // Logica de boton aceptar
        if (VerificarDatos()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Algunos campos deben ser completados", "Atención", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<String> previas = new ArrayList<>();
            int tam = tableCursos.getRowCount();

            // Obtención segura de los datos de la tabla de cursos
            for (int i = 0; i < tam; i++) {
                Object valBool = tableCursos.getValueAt(i, 1);
                if (Boolean.TRUE.equals(valBool)) { // Evita NullPointerException o ClassCastException
                    Object valStr = tableCursos.getValueAt(i, 0);
                    if (valStr != null) {
                        previas.add(valStr.toString());
                    }
                }
            }
            //Obtencion segura de los datos del combo box categoria
            List<String> categorias = new ArrayList();
            int tamCat = comboCategoria.getItemCount();
            for(int i = 1;i<tamCat;i++){
                String text = (String) comboCategoria.getItemAt(i);
                if(text.contains("✓")){
                    String auxSubString = text.substring(2);
                    categorias.add(auxSubString);
                }
            }

            // Validación de selección de docente
            int fila = tableDocentes.getSelectedRow();
            if (fila == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar un docente", "Atención", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            String docenteSeleccionado = tableDocentes.getValueAt(fila, 0).toString();
            String nombreCurso = fieldNombre.getText().trim();

            // Obtención segura del valor de la fecha
            Date fecha = sdf.parse(fieldFecha.getText());

            // Verificación si el curso/programa ya existe
            if (ico.VerificarCurso(nombreCurso)) {
                int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                        this,
                        "El programa '" + nombreCurso + "' ya existe. ¿Deseas modificar sus datos?",
                        "Programa Existente",
                        javax.swing.JOptionPane.YES_NO_OPTION,
                        javax.swing.JOptionPane.QUESTION_MESSAGE);

                if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
                    // Nota: Verifica que tu método Modificar/AltaCurso en el backend reciba las operaciones UPDATE correctamente
                    ico.AltaCurso(
                        boxInstituto.getSelectedItem().toString(), 
                        nombreCurso, 
                        areaDescripcion.getText(), 
                        (int) spinnerDuracion.getValue(), 
                        (int) spinnerHoras.getValue(), 
                        (int) spinnerCreditos.getValue(), 
                        fieldURL.getText(), 
                        previas, 
                        fecha, 
                        docenteSeleccionado,
                        categorias
                    );
                    javax.swing.JOptionPane.showMessageDialog(this, "Programa actualizado con éxito.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
            } else {
                // Alta de nuevo curso
                ico.AltaCurso(
                    boxInstituto.getSelectedItem().toString(), 
                    nombreCurso, 
                    areaDescripcion.getText(), 
                    (int) spinnerDuracion.getValue(), 
                    (int) spinnerHoras.getValue(), 
                    (int) spinnerCreditos.getValue(), 
                    fieldURL.getText(), 
                    previas, 
                    fecha, 
                    docenteSeleccionado,
                    categorias
                );
                javax.swing.JOptionPane.showMessageDialog(this, "Curso ingresado con éxito!", "System", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }

        } catch (Exception e) {
            // Imprime la pila de llamadas en la consola para identificar exactamente la línea del error
            e.printStackTrace(); 
            javax.swing.JOptionPane.showMessageDialog(this, "Error al procesar el curso: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonAceptarActionPerformed
    boolean VerificarDatos() {
        return boxInstituto.getSelectedIndex() == 0
                || fieldNombre.getText().isEmpty()
                || areaDescripcion.getText().isEmpty()
                || (int) spinnerDuracion.getValue() == 0
                || (int) spinnerHoras.getValue() == 0
                || fieldURL.getText().isEmpty();
    }
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // Simplemente cierra la ventana interna sin cerrar el programa
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void fieldFechaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldFechaMouseClicked
        JPopupMenu popupMenu = new JPopupMenu();
        

        // Configurar el evento al seleccionar una fecha
        popupCalendar.getDayChooser().addPropertyChangeListener("day", evtc -> {
            // 1. Obtener el nuevo día seleccionado
            int nuevoDia = (Integer) evtc.getNewValue();

            // 2. Obtener el objeto Calendar actual y asignarle el nuevo día
            java.util.Calendar cal = popupCalendar.getCalendar();
            cal.set(java.util.Calendar.DAY_OF_MONTH, nuevoDia);

            Date fechaSeleccionada = cal.getTime();

            if (fechaSeleccionada != null) {
                fieldFecha.setText(sdf.format(fechaSeleccionada));
            }

            popupMenu.setVisible(false);
        });

        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(popupCalendar, BorderLayout.CENTER);

        // DESPLEGAR EL POPUP (Muestra el calendario justo debajo del JTextField)
        popupMenu.show(fieldFecha, 0, fieldFecha.getHeight());
    }//GEN-LAST:event_fieldFechaMouseClicked

    private void fieldDocenteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDocenteFocusGained
        fieldDocente.setText("");
        fieldDocente.setForeground(Color.black);
    }//GEN-LAST:event_fieldDocenteFocusGained

    private void fieldCursoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCursoFocusGained
        fieldCurso.setText("");
        fieldCurso.setForeground(Color.black);
    }//GEN-LAST:event_fieldCursoFocusGained

    private void fieldDocenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDocenteActionPerformed
        fieldDocente.setText("");
    }//GEN-LAST:event_fieldDocenteActionPerformed

    private void comboCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCategoriaActionPerformed
        if(comboCategoria.getSelectedIndex()!=0){
            String auxString = comboCategoria.getSelectedItem().toString();
            if(auxString.contains("✓")){
                String auxSubString = auxString.substring(2);
                int auxInt = comboCategoria.getSelectedIndex();
                comboCategoria.removeItemAt(auxInt);
                comboCategoria.insertItemAt(auxSubString, auxInt);
            }else{
                auxString = "✓ " + auxString;
                int auxInt = comboCategoria.getSelectedIndex();
                comboCategoria.removeItemAt(auxInt);
                comboCategoria.insertItemAt(auxString, auxInt);
                comboCategoria.setSelectedIndex(auxInt);
            }
        }
    }//GEN-LAST:event_comboCategoriaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaDescripcion;
    private javax.swing.JComboBox<String> boxInstituto;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton buttonAceptar;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.JTextField fieldCurso;
    private javax.swing.JTextField fieldDocente;
    private javax.swing.JTextField fieldFecha;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JTextField fieldURL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSpinner spinnerCreditos;
    private javax.swing.JSpinner spinnerDuracion;
    private javax.swing.JSpinner spinnerHoras;
    private javax.swing.JTable tableCursos;
    private javax.swing.JTable tableDocentes;
    // End of variables declaration//GEN-END:variables
}
