/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;
import DTsClasses.DTCurso;
import DTsClasses.DTMaster;
import DTsClasses.EnumDT;
import Logica.IController;
import Logica.Fabric;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
public class ConsultaEdicionCurso extends javax.swing.JInternalFrame {
    IController ico;
    public ConsultaEdicionCurso() {
        Fabric f = Fabric.GetInstance();
        ico = f.GetIController();
        initComponents();
        IniciarArbol();
        
    }
    public void IniciarArbol(){
        DefaultTreeModel modelo = (DefaultTreeModel)treeEdiciones.getModel();
        List<DTMaster> listDt = ico.ListarClase(EnumDT.DT_CURSO);
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("edEXT"); 
        modelo.setRoot(raiz);
        if(listDt!=null){
            for(int i = 0; i<listDt.size();i++){
                DTMaster auxDt = listDt.get(i);
                if(auxDt instanceof DTCurso){
                    DTCurso dti = (DTCurso)auxDt;
                    DefaultMutableTreeNode raizAyuda = new DefaultMutableTreeNode(dti.getNombre()); 
                    raiz.add(raizAyuda);
                    System.out.println("las ediciones: "+dti.getEdiCursos());
                    for(String edi : dti.getEdiCursos()){
                        DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(edi);
                        raizAyuda.add(hijo);
                        
                    }
                } 
            }
        }
        modelo.reload();
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        treeEdiciones = new javax.swing.JTree();

        setClosable(true);

        treeEdiciones.addTreeSelectionListener(this::treeEdicionesValueChanged);
        jScrollPane1.setViewportView(treeEdiciones);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treeEdicionesValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeEdicionesValueChanged
        DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) treeEdiciones.getLastSelectedPathComponent();

        // 2. Comprobar que no sea null
        if (nodoSeleccionado != null) {
            if(nodoSeleccionado.isLeaf()){
                //Obtener el nombre/texto visible directamente:
                String nombre = nodoSeleccionado.getUserObject().toString();
                DTMaster dt = ico.ConsultaEdicionCurso(nombre);
                MiniInterfazDeConsultaEdicion mice = new MiniInterfazDeConsultaEdicion();
                this.getDesktopPane().add(mice);
                mice.setTitle("(Info) "+nombre);
                mice.setVisible(true);
                mice.toFront();
                mice.ColocarDatos(dt);
            }

        }
        
        
    }//GEN-LAST:event_treeEdicionesValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree treeEdiciones;
    // End of variables declaration//GEN-END:variables
}
