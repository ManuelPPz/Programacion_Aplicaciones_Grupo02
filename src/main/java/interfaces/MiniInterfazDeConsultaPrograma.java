/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;

import DTsClasses.DTMaster;
import DTsClasses.DTProgramaForm;
import Logica.Fabric;
import Logica.IController;
import interfaces.MiniInterfazDeConsultaCurso;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author mateo
 */
public class MiniInterfazDeConsultaPrograma extends javax.swing.JInternalFrame {
    IController ico;
    DTProgramaForm programaActual;
    DefaultListModel<String> modeloListaCursos;
    /**
     * Creates new form MiniInterfazDeConsultaPrograma
     */
    public MiniInterfazDeConsultaPrograma() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
 
        // >>> AGREGAR: todo lo que sigue es código propio, fuera del bloque generado <<<
        configurarComponentesAdicionales();
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
    }
    
    public void mostrarDatosPrograma(DTMaster dti) {
        if(dti instanceof DTProgramaForm dt){
            this.programaActual = (DTProgramaForm)dt;
 
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
            ListarCursos();
        }
        
    }
    private void ListarCursos(){
        List<String> auxList = programaActual.getCursos();
        DefaultListModel<String> modeloListCurso = new DefaultListModel<String>();
        listCursos.setModel(modeloListCurso);
        for(int i = 0;i<auxList.size();i++){
            modeloListCurso.addElement(auxList.get(i));
        }
    }
    
    private void MostrarDataCurso(String nombre){
        DTMaster dt = ico.ConsultaCurso(nombre);
        MiniInterfazDeConsultaCurso micc = new MiniInterfazDeConsultaCurso();
        this.getDesktopPane().add(micc);
        micc.setTitle("(Info) "+nombre);
        micc.setLocation(0, 0);
        micc.setVisible(true);
        micc.ColocarDatos(dt);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pane2 = new javax.swing.JInternalFrame();
        jScrollPane4 = new javax.swing.JScrollPane();
        listCursos = new javax.swing.JList<>();
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

        setClosable(true);

        pane2.setTitle("Cursos que integran el Programa");
        pane2.setVisible(true);

        listCursos.setModel(new javax.swing.DefaultListModel<String>());
        listCursos.addListSelectionListener(this::listCursosValueChanged);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pane1)
                        .addComponent(pane2))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listCursosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listCursosValueChanged
        String nombre = listCursos.getSelectedValue();
        MostrarDataCurso(nombre);
    }//GEN-LAST:event_listCursosValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> listCursos;
    private javax.swing.JInternalFrame pane1;
    private javax.swing.JInternalFrame pane2;
    private javax.swing.JSpinner spinnerFechadeAlta;
    private javax.swing.JSpinner spinnerFechadeFinal;
    private javax.swing.JSpinner spinnerFechadeInicio;
    private javax.swing.JTextArea txtDescripcion;
    // End of variables declaration//GEN-END:variables
}
