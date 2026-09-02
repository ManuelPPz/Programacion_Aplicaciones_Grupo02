/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import Logica.IController;
import Logica.Fabric;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import DTsClasses.DTMaster;
import DTsClasses.DTCurso;
import DTsClasses.DTDocente;
import DTsClasses.DTInstituto;
import DTsClasses.EnumDT;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author sebas xD
 */
public class AltaCurso extends javax.swing.JInternalFrame {

    IController ico;
    List<Object[]> rowsDocente;
    List<Object[]> rowsCursos;

    /**
     * Creates new form AltaUsuario
     */
    public AltaCurso() {
        rowsDocente = new ArrayList<>();
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
    }

    private void IniciarRows(List<DTMaster> list) {

        for (int i = 0; i < list.size(); i++) {
            DTMaster dt = list.get(i);
            if (dt instanceof DTDocente dti) {
                if (i == 0) {
                    rowsDocente = new ArrayList<>();
                }
                Object[] row = {dti.getNombre()};
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
        DefaultTableModel modelo = (DefaultTableModel) tableDocentes.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tableDocentes.setRowSorter(sorter);

        String texto = fieldDocente.getText();
        if (texto.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
        }
    }

    public void FiltrarCurso() {
        //Para filtrar los cursos
        DefaultTableModel modelo = (DefaultTableModel) tableCursos.getModel();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        boxInstituto = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaDescripcion = new javax.swing.JTextArea();
        spinnerDuracion = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fieldURL = new javax.swing.JTextField();
        buttonAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        spinnerFecha = new javax.swing.JSpinner();
        spinnerHoras = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        spinnerCreditos = new javax.swing.JSpinner();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableCursos = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableDocentes = new javax.swing.JTable();
        fieldDocente = new javax.swing.JTextField();
        fieldCurso = new javax.swing.JTextField();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Alta Curso");

        jLabel1.setText("Instituto*");

        jLabel2.setText("Nombre*");

        jLabel3.setText("Descripcion*");

        jLabel4.setText("Duracion*");

        fieldNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldNombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldNombreFocusLost(evt);
            }
        });
        fieldNombre.addActionListener(this::fieldNombreActionPerformed);

        boxInstituto.addActionListener(this::boxInstitutoActionPerformed);

        areaDescripcion.setColumns(20);
        areaDescripcion.setRows(5);
        jScrollPane2.setViewportView(areaDescripcion);

        jLabel11.setText("Cant. Horas*");

        jLabel13.setText("URL*");

        fieldURL.addActionListener(this::fieldURLActionPerformed);

        buttonAceptar.setText("Aceptar");
        buttonAceptar.addActionListener(this::buttonAceptarActionPerformed);

        btnCancelar.setForeground(new java.awt.Color(255, 102, 102));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        jLabel15.setText("Fecha de alta*");

        spinnerFecha.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, new java.util.Date(), java.util.Calendar.DAY_OF_MONTH));
        spinnerFecha.setEditor(new javax.swing.JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy"));

        jLabel5.setText("Cant. Creditos*");

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
        if (tableCursos.getColumnModel().getColumnCount() > 0) {
            tableCursos.getColumnModel().getColumn(0).setResizable(false);
            tableCursos.getColumnModel().getColumn(1).setResizable(false);
        }

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

        fieldDocente.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                FiltrarDocente();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                FiltrarDocente();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        fieldCurso.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                FiltrarCurso();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                FiltrarCurso();
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(59, 59, 59)
                                        .addComponent(spinnerDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(73, 73, 73)
                                        .addComponent(spinnerHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(87, 87, 87)
                                        .addComponent(spinnerCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(spinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(buttonAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(boxInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(fieldCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(fieldURL, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fieldDocente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(314, 314, 314))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addComponent(boxInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spinnerDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinnerHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel11))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel5))
                            .addComponent(spinnerCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(fieldURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel15))
                    .addComponent(spinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldDocente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCancelar)
                            .addComponent(buttonAceptar))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        List<DTMaster> auxListIns = ico.ListarClase(EnumDT.DT_INSTITUTO);

        boxInstituto.insertItemAt("SIN DATOS", 0);
        for (int i = 0; i < auxListIns.size(); i++) {
            DTInstituto dt = (DTInstituto) auxListIns.get(i);
            boxInstituto.insertItemAt(dt.getNombre(), i + 1);
        }
        boxInstituto.setSelectedIndex(0);

        pack();
    }// </editor-fold>                          

    private void fieldNombreActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void fieldURLActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void buttonAceptarActionPerformed(java.awt.event.ActionEvent evt) {
        // Logica de boton aceptar
        if (VerificarDatos()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Algunos campos deben ser completados", "Atencion", javax.swing.JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                List<String> previas = new ArrayList<>();
                int tam = tableCursos.getRowCount();
                for (int i = 0; i < tam; i++) {
                    boolean auxBool = (Boolean) tableCursos.getValueAt(i, 1);
                    if (auxBool) {
                        String auxStr = (String) tableCursos.getValueAt(i, 0);
                        previas.add(auxStr);
                    }
                }

                int fila = tableDocentes.getSelectedRow();
                if (fila == -1) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar un docente", "Atencion", javax.swing.JOptionPane.ERROR_MESSAGE);
                } else {
                    String auxStr = (String) tableDocentes.getValueAt(fila, 0);
                    
                    if (ico.VerificarCurso(fieldNombre.getText())) {
                        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                                this,
                                "El programa '" + fieldNombre.getText() + "' ya existe. ¿Deseas modificar sus datos?",
                                "Programa Existente",
                                javax.swing.JOptionPane.YES_NO_OPTION,
                                javax.swing.JOptionPane.QUESTION_MESSAGE);

                        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
                            // El usuario quiere modificarlo
                            System.out.println(areaDescripcion.getText());
                            ico.AltaCurso(boxInstituto.getSelectedItem().toString(), fieldNombre.getText(), areaDescripcion.getText(), (int) spinnerDuracion.getValue(), (int) spinnerHoras.getValue(), (int) spinnerCreditos.getValue(), fieldURL.getText(), previas, (Date) spinnerFecha.getValue(), auxStr);
                            javax.swing.JOptionPane.showMessageDialog(this, "Programa actualizado con éxito.");
                            this.dispose();
                        } else {
                            // El usuario canceló la operación
                            this.dispose();
                        }
                    } else {
                        ico.AltaCurso(boxInstituto.getSelectedItem().toString(), fieldNombre.getText(), areaDescripcion.getText(), (int) spinnerDuracion.getValue(), (int) spinnerHoras.getValue(), (int) spinnerCreditos.getValue(), fieldURL.getText(), previas, (Date) spinnerFecha.getValue(), auxStr);
                        javax.swing.JOptionPane.showMessageDialog(this, "Curso ingresado con exito!", "System", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    }
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al procesar el curso: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    boolean VerificarDatos() {
        return boxInstituto.getSelectedIndex() == 0
                || fieldNombre.getText().isEmpty()
                || areaDescripcion.getText().isEmpty()
                || (int) spinnerDuracion.getValue() == 0
                || (int) spinnerHoras.getValue() == 0
                || fieldURL.getText().isEmpty();
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        // Simplemente cierra la ventana interna sin cerrar el programa
        this.dispose();
    }

    private void boxInstitutoActionPerformed(java.awt.event.ActionEvent evt) {
        if (boxInstituto.getSelectedIndex() != 0) {
            String auxStr = (String) boxInstituto.getSelectedItem();
            List<DTMaster> listDocente = ico.ListarDocentes(auxStr);
            if (!listDocente.isEmpty()) {
                listDocente = OrdenarLista(listDocente);
                IniciarRows(listDocente);
                IniciarTable(EnumDT.DT_USUARIO);
            }
            List<DTMaster> listCursos = ico.ListarCursos(auxStr);
            if (!listCursos.isEmpty()) {
                listCursos = OrdenarLista(listCursos);
                IniciarRows(listCursos);
                IniciarTable(EnumDT.DT_CURSO);
            }
        }

    }

    private void fieldNombreFocusGained(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void fieldNombreFocusLost(java.awt.event.FocusEvent evt) {

    }


    // Variables declaration - do not modify                     
    private javax.swing.JTextArea areaDescripcion;
    private javax.swing.JComboBox<String> boxInstituto;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton buttonAceptar;
    private javax.swing.JTextField fieldCurso;
    private javax.swing.JTextField fieldDocente;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JTextField fieldURL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JSpinner spinnerCreditos;
    private javax.swing.JSpinner spinnerDuracion;
    private javax.swing.JSpinner spinnerFecha;
    private javax.swing.JSpinner spinnerHoras;
    private javax.swing.JTable tableCursos;
    private javax.swing.JTable tableDocentes;
    // End of variables declaration                   
}