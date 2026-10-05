/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import DTsClasses.DTCurso;
import DTsClasses.DTDocente;
import DTsClasses.DTMaster;
import Logica.Fabric;
import Logica.IController;
import com.toedter.calendar.JCalendar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author mateo
 */
public class MiniInterfazDeAltaEdicion extends javax.swing.JInternalFrame {
    IController ico;
    String instituto;
    String curso;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    JCalendar calendarIni;
    JCalendar calendarFin;
    JCalendar calendarPub;
    
    public MiniInterfazDeAltaEdicion(String instituto, String curso) {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        calendarIni = new JCalendar();
        calendarFin = new JCalendar();
        calendarPub = new JCalendar();

        initComponents();

        spinnerCupo.setVisible(false);
        TableColumn colNombre = tableDocentes.getColumnModel().getColumn(0);
        colNombre.setPreferredWidth(150);
        TableColumn colSeleccionado = tableDocentes.getColumnModel().getColumn(1);
        colSeleccionado.setPreferredWidth(50);
        this.instituto = instituto;
        this.curso = curso;
        fieldInstituto.setText(instituto);
        fieldCurso.setText(curso);

        DTCurso auxDt = (DTCurso) ico.ConsultaCurso(curso);

        // 1. INICIALIZAR FECHA DE PUBLICACIÓN
        Date fAlta = auxDt.getFechaAlta();
        calendarPub.setMinSelectableDate(fAlta);
        calendarPub.setDate(fAlta);
        fieldFechaPub.setText(sdf.format(fAlta));

        // 2. INICIALIZAR FECHA DE INICIO (Mínimo = Fecha Pub)
        calendarIni.setMinSelectableDate(fAlta);
        calendarIni.setDate(fAlta);
        fieldDateIni.setText(sdf.format(fAlta));

        // 3. INICIALIZAR FECHA DE FIN (Inicio + Duración del curso)
        Calendar calAux = Calendar.getInstance();
        calAux.setTime(fAlta); // Partir de la fecha de inicio
        calAux.add(Calendar.WEEK_OF_YEAR, auxDt.getDuracion());
        Date fFin = calAux.getTime();

        calendarFin.setMinSelectableDate(fAlta);
        calendarFin.setDate(fFin);
        fieldDateFin.setText(sdf.format(fFin));

        // 4. CONFIGURAR LISTENERS UNA SOLA VEZ
        setupCalendarListeners();

        LlenarTablas();
    }

    private void setupCalendarListeners() {
        // LISTENER PUBLICACIÓN
        calendarPub.getDayChooser().addPropertyChangeListener("day", evtc -> {
            int nuevoDia = (Integer) evtc.getNewValue();
            Calendar cal = calendarPub.getCalendar();
            cal.set(Calendar.DAY_OF_MONTH, nuevoDia);
            Date fPub = cal.getTime();

            fieldFechaPub.setText(sdf.format(fPub));

            // Ajustar el mínimo de Fecha Inicio
            calendarIni.setMinSelectableDate(fPub);
            if (calendarIni.getDate().before(fPub)) {
                calendarIni.setDate(fPub);
                fieldDateIni.setText(sdf.format(fPub));
            }
        });

        // LISTENER INICIO
        calendarIni.getDayChooser().addPropertyChangeListener("day", evtc -> {
            int nuevoDia = (Integer) evtc.getNewValue();
            Calendar cal = calendarIni.getCalendar();
            cal.set(Calendar.DAY_OF_MONTH, nuevoDia);
            Date fIni = cal.getTime();

            fieldDateIni.setText(sdf.format(fIni));

            // Ajustar el mínimo de Fecha Fin
            calendarFin.setMinSelectableDate(fIni);
            if (calendarFin.getDate().before(fIni)) {
                calendarFin.setDate(fIni);
                fieldDateFin.setText(sdf.format(fIni));
            }
        });

        // LISTENER FIN
        calendarFin.getDayChooser().addPropertyChangeListener("day", evtc -> {
            int nuevoDia = (Integer) evtc.getNewValue();
            Calendar cal = calendarFin.getCalendar();
            cal.set(Calendar.DAY_OF_MONTH, nuevoDia);
            Date fFin = cal.getTime();

            fieldDateFin.setText(sdf.format(fFin));
        });
    }
    
    
    private void LlenarTablas() {
        DefaultTableModel modelo = (DefaultTableModel) tableDocentes.getModel();
        modelo.setRowCount(0); // Limpiar filas previas

        List<DTMaster> auxList = ico.ListarDocentes(instituto);

        if (auxList != null && !auxList.isEmpty()) {
            for (DTMaster dt : auxList) {
                if (dt instanceof DTDocente dTDocente) {
                    // Muestra Nickname - Nombre completo
                    String etiqueta = dTDocente.getNombre()+ " (" + dTDocente.getNickname()+ ")";
                    Object[] fila = new Object[]{ etiqueta, Boolean.FALSE };
                    modelo.addRow(fila);
                }
            }
        } else {
            System.out.println("No se encontraron docentes para el instituto: " + instituto);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        fieldInstituto = new javax.swing.JTextField();
        fieldCurso = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        checkCupo = new javax.swing.JCheckBox();
        spinnerCupo = new javax.swing.JSpinner();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableDocentes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nombreField = new javax.swing.JTextField();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        fieldFechaPub = new javax.swing.JTextField();
        fieldDateIni = new javax.swing.JTextField();
        fieldDateFin = new javax.swing.JTextField();

        setClosable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Inicio*");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 138, -1, 26));

        fieldInstituto.setEditable(false);
        fieldInstituto.setBackground(new java.awt.Color(255, 255, 255));
        fieldInstituto.setText("jTextField1");
        getContentPane().add(fieldInstituto, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 24, 204, 24));

        fieldCurso.setEditable(false);
        fieldCurso.setBackground(new java.awt.Color(255, 255, 255));
        fieldCurso.setText("jTextField2");
        getContentPane().add(fieldCurso, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 61, 204, 23));

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Fin*");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(186, 138, -1, -1));

        checkCupo.setText("Cupo Definido");
        checkCupo.addChangeListener(this::checkCupoStateChanged);
        getContentPane().add(checkCupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, -1));
        getContentPane().add(spinnerCupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, -1, -1));

        tableDocentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Docentes", "Seleccionado"
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
        jScrollPane7.setViewportView(tableDocentes);

        getContentPane().add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 200, 138));

        jLabel1.setText("Instituto:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 24, 53, 24));

        jLabel2.setText("Curso:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 60, 53, 24));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Fecha de publicacion*");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, 26));

        jLabel6.setText("Nombre*");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 96, 53, 24));
        getContentPane().add(nombreField, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 96, 204, 24));

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(this::btnAceptarActionPerformed);
        getContentPane().add(btnAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 450, -1, -1));

        btnCancelar.setForeground(new java.awt.Color(255, 102, 102));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);
        getContentPane().add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 450, -1, -1));

        fieldFechaPub.setMaximumSize(new java.awt.Dimension(64, 22));
        fieldFechaPub.setPreferredSize(new java.awt.Dimension(64, 18));
        fieldFechaPub.addMouseListener(new java.awt.event.MouseAdapter() {
<<<<<<< HEAD
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldFechaPubMouseClicked(evt);
            }
        });
        getContentPane().add(fieldFechaPub, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 380, 80, 20));
=======
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fieldFechaPubMousePressed(evt);
            }
        });
        getContentPane().add(fieldFechaPub, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 380, 90, 20));
>>>>>>> v2.0.1

        fieldDateIni.setMaximumSize(new java.awt.Dimension(64, 22));
        fieldDateIni.setPreferredSize(new java.awt.Dimension(64, 18));
        fieldDateIni.addMouseListener(new java.awt.event.MouseAdapter() {
<<<<<<< HEAD
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldDateIniMouseClicked(evt);
=======
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fieldDateIniMousePressed(evt);
>>>>>>> v2.0.1
            }
        });
        getContentPane().add(fieldDateIni, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 138, 80, 20));

        fieldDateFin.setMaximumSize(new java.awt.Dimension(64, 22));
        fieldDateFin.setPreferredSize(new java.awt.Dimension(64, 18));
        fieldDateFin.addMouseListener(new java.awt.event.MouseAdapter() {
<<<<<<< HEAD
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldDateFinMouseClicked(evt);
=======
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fieldDateFinMousePressed(evt);
>>>>>>> v2.0.1
            }
        });
        getContentPane().add(fieldDateFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 138, 80, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkCupoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_checkCupoStateChanged
        spinnerCupo.setVisible(checkCupo.isSelected());
        Dimension dSpinner = new Dimension(30,22);
        spinnerCupo.setModel(new SpinnerNumberModel(1, 1, null, 1));
        spinnerCupo.setPreferredSize(dSpinner);
        spinnerCupo.setMinimumSize(dSpinner);
        spinnerCupo.setMaximumSize(dSpinner);
        
        Dimension tamFijo = new Dimension(64,22);
        fieldInstituto.setPreferredSize(tamFijo);
        fieldInstituto.setMinimumSize(tamFijo);
        fieldInstituto.setMaximumSize(tamFijo);
        
        fieldCurso.setPreferredSize(tamFijo);
        fieldCurso.setMinimumSize(tamFijo);
        fieldCurso.setMaximumSize(tamFijo);
    }//GEN-LAST:event_checkCupoStateChanged

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        if (VerificarDatos()) {
        JOptionPane.showMessageDialog(this, "Algunos campos deben ser completados o son inválidos", "Atencion", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    String nombre = nombreField.getText().trim();
    Date fIni = calendarIni.getDate();
    Date fFin = calendarFin.getDate();
    Date fPub = calendarPub.getDate();
    
    boolean auxCheckCupo = checkCupo.isSelected();
    int auxCupo = auxCheckCupo ? (int) spinnerCupo.getValue() : 0;

        List<String> auxDocentes = new ArrayList<>();
        
        for (int i = 0; i < tableDocentes.getRowCount(); i++) {
            Object isSelected = tableDocentes.getValueAt(i, 1);
            if (isSelected != null && (Boolean) isSelected) {
                String docenteNick = (String) tableDocentes.getValueAt(i, 0);
                // 1. Extraer el nickname limpiando posibles formatos
                String nick = docenteNick.trim();
                if (nick.contains("(")) {
                    // Formato: "Nombre Apellido (nickname)"
                    nick = nick.substring(nick.indexOf("(") + 1, nick.indexOf(")")).trim();
                } else if (nick.contains(":")) {
                    // Formato: "cod: Nombre" o "Nickname: cod"
                    nick = nick.split(":")[0].trim();
                } else if (nick.contains("-")) {
                    // Formato: "cod - Nombre"
                    nick = nick.split("-")[0].trim();
                }
                auxDocentes.add(nick);
            }
        }

    try {
        if (ico.VerificarEdicion(nombre)) {
            int respuesta = JOptionPane.showConfirmDialog(
                this, 
                "La edición '" + nombre + "' ya existe. ¿Deseas modificar sus datos?", 
                "Edición Existente", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (respuesta == JOptionPane.YES_OPTION) {
                ico.AltaEdicionCurso(instituto, curso, nombre, fIni, fFin, auxCupo, auxDocentes, fPub);
                JOptionPane.showMessageDialog(this, "Edición actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        } else {
            ico.AltaEdicionCurso(instituto, curso, nombre, fIni, fFin, auxCupo, auxDocentes, fPub);
            JOptionPane.showMessageDialog(this, "Edición dada de alta con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnAceptarActionPerformed
    boolean VerificarDatos(){
        Date fecha1 = calendarIni.getDate();
        Date fecha2 = calendarFin.getDate();
        Date fechaPub = calendarPub.getDate();
        Boolean auxBool = false;
        for(int i = 0;i<tableDocentes.getRowCount();i++){
            auxBool = (Boolean)tableDocentes.getValueAt(i, 1);
            if(auxBool){
                break;
            }
            
        }
        return nombreField.getText().isEmpty() ||
                fecha1.after(fecha2) ||
                !auxBool ||
                fechaPub.after(fecha1);
                
    }
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

<<<<<<< HEAD
    private void fieldFechaPubMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldFechaPubMouseClicked
=======
    private void fieldDateIniMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldDateIniMousePressed
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(calendarIni, BorderLayout.CENTER);

        java.beans.PropertyChangeListener closeListener = new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evtc) {
                popupMenu.setVisible(false);
                calendarIni.getDayChooser().removePropertyChangeListener("day", this);
            }
        };
        calendarIni.getDayChooser().addPropertyChangeListener("day", closeListener);

        popupMenu.show(fieldDateIni, 0, fieldDateIni.getHeight());
    }//GEN-LAST:event_fieldDateIniMousePressed

    private void fieldDateFinMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldDateFinMousePressed
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(calendarFin, BorderLayout.CENTER);

        java.beans.PropertyChangeListener closeListener = new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evtc) {
                popupMenu.setVisible(false);
                calendarFin.getDayChooser().removePropertyChangeListener("day", this);
            }
        };
        calendarFin.getDayChooser().addPropertyChangeListener("day", closeListener);

        popupMenu.show(fieldDateFin, 0, fieldDateFin.getHeight());
    }//GEN-LAST:event_fieldDateFinMousePressed

    private void fieldFechaPubMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldFechaPubMousePressed
>>>>>>> v2.0.1
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(calendarPub, BorderLayout.CENTER);

        // Listener temporal para cerrar el popup al hacer clic en un día
        java.beans.PropertyChangeListener closeListener = new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evtc) {
                popupMenu.setVisible(false);
                calendarPub.getDayChooser().removePropertyChangeListener("day", this);
            }
        };
        calendarPub.getDayChooser().addPropertyChangeListener("day", closeListener);

        popupMenu.show(fieldFechaPub, 0, fieldFechaPub.getHeight());
<<<<<<< HEAD
    }//GEN-LAST:event_fieldFechaPubMouseClicked

    private void fieldDateIniMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldDateIniMouseClicked
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(calendarIni, BorderLayout.CENTER);

        java.beans.PropertyChangeListener closeListener = new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evtc) {
                popupMenu.setVisible(false);
                calendarIni.getDayChooser().removePropertyChangeListener("day", this);
            }
        };
        calendarIni.getDayChooser().addPropertyChangeListener("day", closeListener);

        popupMenu.show(fieldDateIni, 0, fieldDateIni.getHeight());
    }//GEN-LAST:event_fieldDateIniMouseClicked

    private void fieldDateFinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldDateFinMouseClicked
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(calendarFin, BorderLayout.CENTER);

        java.beans.PropertyChangeListener closeListener = new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evtc) {
                popupMenu.setVisible(false);
                calendarFin.getDayChooser().removePropertyChangeListener("day", this);
            }
        };
        calendarFin.getDayChooser().addPropertyChangeListener("day", closeListener);

        popupMenu.show(fieldDateFin, 0, fieldDateFin.getHeight());
    }//GEN-LAST:event_fieldDateFinMouseClicked
=======
    }//GEN-LAST:event_fieldFechaPubMousePressed
>>>>>>> v2.0.1


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JCheckBox checkCupo;
    private javax.swing.JTextField fieldCurso;
    private javax.swing.JTextField fieldDateFin;
    private javax.swing.JTextField fieldDateIni;
    private javax.swing.JTextField fieldFechaPub;
    private javax.swing.JTextField fieldInstituto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextField nombreField;
    private javax.swing.JSpinner spinnerCupo;
    private javax.swing.JTable tableDocentes;
    // End of variables declaration//GEN-END:variables
}
