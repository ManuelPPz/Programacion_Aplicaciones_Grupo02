/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import Logica.Fabric;
import Logica.IController;
import com.toedter.calendar.JCalendar;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.SpinnerDateModel;
import javax.swing.JPopupMenu;

/**
 *
 * @author mateo
 */
public class Crear_programa_formacion extends javax.swing.JInternalFrame {
    IController ico;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JCalendar calendarIni;
        JCalendar calendarFin;
        JCalendar calendarPub;

        
    public Crear_programa_formacion() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        
        calendarIni = new JCalendar();
        calendarFin = new JCalendar();
        calendarPub = new JCalendar();
        
        
        initComponents();
        
        // 1. INICIALIZAR FECHA DE PUBLICACIÓN
        Date fAlta = new Date();
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
        Date fFin = calAux.getTime();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        acceptButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaDescripcion = new javax.swing.JTextArea();
        fieldDateIni = new javax.swing.JTextField();
        fieldDateFin = new javax.swing.JTextField();
        fieldFechaPub = new javax.swing.JTextField();

        setClosable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setText("Fecha de inicio");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 195, 113, 20));

        jLabel5.setText("Fecha de Finalizacion");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 226, -1, 20));

        acceptButton.setText("Aceptar");
        acceptButton.addActionListener(this::acceptButtonActionPerformed);
        getContentPane().add(acceptButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(284, 291, -1, -1));

        jLabel1.setText("Nombre Programa");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 25, -1, 22));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("Ingrese datos del Programa");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 0, -1, -1));

        jLabel6.setText("Fecha de Alta");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 254, 113, -1));

        fieldNombre.setMaximumSize(new java.awt.Dimension(130, 22));
        fieldNombre.setMinimumSize(new java.awt.Dimension(130, 22));
        fieldNombre.setPreferredSize(new java.awt.Dimension(130, 22));
        getContentPane().add(fieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 25, -1, -1));

        jLabel3.setText("Descripcion");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 53, 99, -1));

        areaDescripcion.setColumns(20);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setRows(5);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setMaximumSize(new java.awt.Dimension(245, 130));
        areaDescripcion.setMinimumSize(new java.awt.Dimension(245, 130));
        areaDescripcion.setPreferredSize(new java.awt.Dimension(245, 130));
        jScrollPane1.setViewportView(areaDescripcion);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 53, 245, 130));

        fieldDateIni.setMaximumSize(new java.awt.Dimension(64, 22));
        fieldDateIni.setPreferredSize(new java.awt.Dimension(64, 18));
        fieldDateIni.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldDateIniMouseClicked(evt);
            }
        });
        getContentPane().add(fieldDateIni, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 195, 150, 20));

        fieldDateFin.setMaximumSize(new java.awt.Dimension(64, 22));
        fieldDateFin.setPreferredSize(new java.awt.Dimension(64, 18));
        fieldDateFin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldDateFinMouseClicked(evt);
            }
        });
        getContentPane().add(fieldDateFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 226, 150, 20));

        fieldFechaPub.setMaximumSize(new java.awt.Dimension(64, 22));
        fieldFechaPub.setPreferredSize(new java.awt.Dimension(64, 18));
        fieldFechaPub.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldFechaPubMouseClicked(evt);
            }
        });
        getContentPane().add(fieldFechaPub, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 150, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        String nombreProg = fieldNombre.getText().trim();
        String descripcionProg = areaDescripcion.getText().trim();
        Date fechaInicio = calendarIni.getDate();
        Date fechaFin = calendarFin.getDate();
        Date fAlta  = calendarPub.getDate();
        
        if (nombreProg.isEmpty() || descripcionProg.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                "Error: Todos los campos son obligatorios", 
                "Campos Inválidos", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (VerificarDatos()) {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                "Error: Datos incoherentes en las fechas", 
                "Campos Inválidos", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            if (ico.VerificarPrograma(nombreProg)) {
                int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                    this, 
                    "El programa '" + nombreProg + "' ya existe. ¿Deseas modificar sus datos?", 
                    "Programa Existente", 
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
                );
            
                if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
                    // El usuario quiere modificarlo
                    ico.CrearProgramasDeFormacion(nombreProg, descripcionProg, fechaInicio, fechaFin, fAlta);
                    javax.swing.JOptionPane.showMessageDialog(this, "Programa actualizado con éxito.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } else {
                    // El usuario canceló la operación
                    javax.swing.JOptionPane.showMessageDialog(this, "Operación cancelada.");
                    this.dispose();
                }
            } else {
                ico.CrearProgramasDeFormacion(nombreProg, descripcionProg, fechaInicio, fechaFin, fAlta);
                javax.swing.JOptionPane.showMessageDialog(this, "¡Programa de formación creado con éxito!", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                "Error al procesar la solicitud: " + e.getMessage(), 
                "Error de Persistencia", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_acceptButtonActionPerformed

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

    private void fieldFechaPubMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldFechaPubMouseClicked
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
    }//GEN-LAST:event_fieldFechaPubMouseClicked
    
    
    private boolean VerificarDatos() {
        Date fecha1 = calendarIni.getDate();
        Date fecha2 = calendarFin.getDate();
        Date fechaAlta = calendarPub.getDate();
        
        return fecha1.after(fecha2) || fechaAlta.after(fecha1);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JTextArea areaDescripcion;
    private javax.swing.JTextField fieldDateFin;
    private javax.swing.JTextField fieldDateIni;
    private javax.swing.JTextField fieldFechaPub;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
