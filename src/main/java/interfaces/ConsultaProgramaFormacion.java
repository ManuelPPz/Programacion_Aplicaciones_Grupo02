/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;
import Logica.Controller;
import Logica.IController;
import DTsClasses.DTProgramaForm;
import DTsClasses.DTCurso;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 *
 * @author leoli
 */
public class ConsultaProgramaFormacion extends javax.swing.JInternalFrame {
    IController controlador = new Controller();
    DTProgramaForm programaActual;
    DefaultListModel<String> modeloListaCursos;

    /**
     * Creates new form ConsultaProgramaFormacion
     */
    public ConsultaProgramaFormacion() {
        super("Consulta de Programa de Formación", true, true, true, true);
        initComponents();
 
        // >>> AGREGAR: todo lo que sigue es código propio, fuera del bloque generado <<<
        configurarComponentesAdicionales();
//        cargarProgramas();
    }
     private void configurarComponentesAdicionales() {
        // El modelo de listCursos que trae el generado es un DefaultListModel
        // anónimo y vacío. Lo reemplazamos por el que vamos a controlar nosotros.
        modeloListaCursos = new DefaultListModel<>();
        listCursos.setModel(modeloListaCursos);
 
        // Es una pantalla de consulta: las fechas no las carga el usuario.
        spinnerFechadeInicio.setEnabled(false);
        spinnerFechadeFinal.setEnabled(false);
        spinnerFechadeAlta.setEnabled(false);
        txtDescripcion.setEditable(false);
 
        // Combo: cada cambio de selección dispara la consulta al controlador.
        comboProgramas.addActionListener(e -> onProgramaSeleccionado());
 
        // Doble clic sobre un curso -> ver su detalle (puente a Consulta de Curso).
        listCursos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //mostrarDetalleCursoSeleccionado();
                }
            }
        });
    }
 /*    private void cargarProgramas() {
        comboProgramas.removeAllItems();
        comboProgramas.addItem("-- Seleccione un programa --");
        try {
            List<String> nombres = controlador.ListarProgramaDeForm();
            for (String nombre : nombres) {
                comboProgramas.addItem(nombre);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo obtener la lista de programas de formación.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
     private void onProgramaSeleccionado() {
        String seleccionado = (String) comboProgramas.getSelectedItem();
        if (seleccionado == null || comboProgramas.getSelectedIndex() <= 0) {
            limpiarDatos();
            return;
        }
 
        try {
            DTProgramaForm dt = controlador.ConsultaProgramaFormacion(seleccionado);
            if (dt == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron datos para el programa seleccionado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                limpiarDatos();
                return;
            }
            mostrarDatosPrograma(dt);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al consultar el programa.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void mostrarDatosPrograma(DTProgramaForm dt) {
        this.programaActual = dt;
 
        txtDescripcion.setText(dt.getDescripcion());
        txtDescripcion.setCaretPosition(0);
 
        // JSpinner con SpinnerDateModel necesita un Date no nulo -> si falta, usamos hoy.
        spinnerFechadeInicio.setValue(dt.getVigenciaProg().getFechaFin() != null ? dt.getVigenciaProg().getFechaFin() : new Date());
        spinnerFechadeFinal.setValue(dt.getVigenciaProg().getFechaFin() != null ? dt.getVigenciaProg().getFechaFin() : new Date());
        spinnerFechadeAlta.setValue(dt.getFechaAlta() != null ? dt.getFechaAlta() : new Date());
 
        modeloListaCursos.clear();
        List<String> cursos = dt.getCursos();
        if (cursos != null) {
            for (String nombreCurso : cursos) {
                modeloListaCursos.addElement(nombreCurso);
            }
        }
    }
    private void limpiarDatos() {
        this.programaActual = null;
        txtDescripcion.setText("");
        spinnerFechadeInicio.setValue(new Date());
        spinnerFechadeFinal.setValue(new Date());
        spinnerFechadeAlta.setValue(new Date());
        modeloListaCursos.clear();
    }
    /*private void mostrarDetalleCursoSeleccionado() {
        String nombreCurso = listCursos.getSelectedValue();
        if (nombreCurso == null) {
            return;
        }
 
        DTCurso dtCurso = buscarDTCursoPorNombre(nombreCurso);
 
        if (dtCurso == null) {
            JOptionPane.showMessageDialog(this,
                    "Curso: " + nombreCurso,
                    "Detalle del curso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
 
        String mensaje = "Instituto: " + dtCurso.getInstituto() + "\n"
                + "Nombre: " + dtCurso.getNombre() + "\n"
                + "Descripción: " + dtCurso.getDescripcion() + "\n"
                + "Duración: " + dtCurso.getDuracion() + " meses\n"
                + "Cant. horas: " + dtCurso.getCantHoras() + "\n"
                + "Cant. créditos: " + dtCurso.getCantCreditos();
 
        JOptionPane.showMessageDialog(this, mensaje,
                "Detalle del curso", JOptionPane.INFORMATION_MESSAGE);
 
        // --- PUENTE al Caso de Uso "Consulta de Curso" ---
        // Descomentar y ajustar cuando esa pantalla esté disponible:
        //
        // ConsultaCurso ventanaCurso = new ConsultaCurso(dtCurso.getInstituto(), dtCurso.getNombre());
        // JDesktopPane escritorio = getDesktopPane();
        // if (escritorio != null) {
        //     escritorio.add(ventanaCurso);
        //     ventanaCurso.setVisible(true);
        //     try { ventanaCurso.setSelected(true); } catch (Exception ignored) {}
        // }
    }*/
 
    //** Busca, dentro de los datos del programa ya cargados, el DTCurso correspondiente al nombre dado. */
    /*private String buscarDTCursoPorNombre(String nombreCurso) {
        if (programaActual == null || programaActual.getCursos() == null) {
            return null;
        }
        for (DTMaster c : programaActual.getCursos()) {
            if (c.getNombre() != null && c.getNombre().equals(nombreCurso)) {
                return c;
            }
        }
        return null;
    }*/
    
   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        comboProgramas = new javax.swing.JComboBox<>();
        pane1 = new javax.swing.JInternalFrame();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        spinnerFechadeInicio = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        spinnerFechadeFinal = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        spinnerFechadeAlta = new javax.swing.JSpinner();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jInternalFrame3 = new javax.swing.JInternalFrame();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        pane2 = new javax.swing.JInternalFrame();
        jScrollPane4 = new javax.swing.JScrollPane();
        listCursos = new javax.swing.JList<>();

        jLabel1.setText("Programa de Formacion:");

        comboProgramas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        pane1.setTitle("Datos del Programa");
        pane1.setVisible(true);

        jLabel2.setText("Descripcion:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        jLabel3.setText("Fecha de Inicio:");

        spinnerFechadeInicio.setModel(new javax.swing.SpinnerDateModel());
        spinnerFechadeInicio.setEditor(new javax.swing.JSpinner.DateEditor(spinnerFechadeInicio, "dd/MM/yyyy"));

        jLabel8.setText("Fecha de Final:");

        spinnerFechadeFinal.setModel(new javax.swing.SpinnerDateModel());
        spinnerFechadeFinal.setEditor(new javax.swing.JSpinner.DateEditor(spinnerFechadeFinal, "dd/MM/yyyy"));

        jLabel7.setText("Fecha de Alta:");

        spinnerFechadeAlta.setModel(new javax.swing.SpinnerDateModel());
        spinnerFechadeAlta.setEditor(new javax.swing.JSpinner.DateEditor(spinnerFechadeAlta, "dd/MM/yyyy"));

        javax.swing.GroupLayout pane1Layout = new javax.swing.GroupLayout(pane1.getContentPane());
        pane1.getContentPane().setLayout(pane1Layout);
        pane1Layout.setHorizontalGroup(
            pane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pane1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pane1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinnerFechadeInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinnerFechadeFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pane1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinnerFechadeAlta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        pane1Layout.setVerticalGroup(
            pane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(pane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spinnerFechadeInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(spinnerFechadeFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(spinnerFechadeAlta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel4.setText("Programa de Formacion:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jInternalFrame3.setTitle("Datos del Programa");
        jInternalFrame3.setVisible(true);

        jLabel5.setText("Descripcion:");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel6.setText("Fecha de Inicio:");

        jSpinner2.setModel(new javax.swing.SpinnerDateModel());
        jSpinner2.setEditor(new javax.swing.JSpinner.DateEditor(jSpinner2, "dd/MM/yyyy"));

        javax.swing.GroupLayout jInternalFrame3Layout = new javax.swing.GroupLayout(jInternalFrame3.getContentPane());
        jInternalFrame3.getContentPane().setLayout(jInternalFrame3Layout);
        jInternalFrame3Layout.setHorizontalGroup(
            jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jInternalFrame3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jInternalFrame3Layout.setVerticalGroup(
            jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jInternalFrame2Layout = new javax.swing.GroupLayout(jInternalFrame2.getContentPane());
        jInternalFrame2.getContentPane().setLayout(jInternalFrame2Layout);
        jInternalFrame2Layout.setHorizontalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jInternalFrame3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jInternalFrame2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jInternalFrame2Layout.setVerticalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jInternalFrame3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pane2.setTitle("Cursos que integran el Programa");
        pane2.setVisible(true);

        listCursos.setModel(new javax.swing.DefaultListModel<String>());
        jScrollPane4.setViewportView(listCursos);

        javax.swing.GroupLayout pane2Layout = new javax.swing.GroupLayout(pane2.getContentPane());
        pane2.getContentPane().setLayout(pane2Layout);
        pane2Layout.setHorizontalGroup(
            pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        pane2Layout.setVerticalGroup(
            pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboProgramas, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pane1)
                    .addComponent(pane2))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 275, Short.MAX_VALUE)
                    .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 276, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboProgramas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboProgramas;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JInternalFrame jInternalFrame3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JList<String> listCursos;
    private javax.swing.JInternalFrame pane1;
    private javax.swing.JInternalFrame pane2;
    private javax.swing.JSpinner spinnerFechadeAlta;
    private javax.swing.JSpinner spinnerFechadeFinal;
    private javax.swing.JSpinner spinnerFechadeInicio;
    private javax.swing.JTextArea txtDescripcion;
    // End of variables declaration//GEN-END:variables
}
