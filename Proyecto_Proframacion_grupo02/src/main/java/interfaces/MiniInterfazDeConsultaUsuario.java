/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package interfaces;
import DTsClasses.DTUsuarioBase;
import DTsClasses.DTDocente;
import DTsClasses.DTUsuario;
import java.awt.Image;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author mateo
 */
public class MiniInterfazDeConsultaUsuario extends javax.swing.JInternalFrame {

    /**
     * Creates new form MiniInterfazDeConsultaUsuario
     */
    public MiniInterfazDeConsultaUsuario() {
        initComponents();
    }
    public void ColocarDatos(DTUsuarioBase dt) {
    
    labelNickname.setText(dt.getNickname());
    labelNombre.setText(dt.getNombre());
    labelApellido.setText(dt.getApellido());
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String fechaStr = (dt.getFNac() != null) ? sdf.format(dt.getFNac()) : "";
    
    labelFecha.setText(fechaStr);
    String str = "<html><a href=''>" + dt.getCorreo() + "</a></html>";
    labelCorreo.setText(str);
    
    // --- CARGA DE LA IMAGEN DESDE EL SERVIDOR ---
    String nombreImagen = dt.getImg();
    if (nombreImagen != null && !nombreImagen.isBlank()) {
        cargarImagenDesdeServidor(nombreImagen);
    } else {
        labelIcon.setIcon(null); // Si no tiene foto, limpia la vista
    }
    
    if (dt instanceof DTDocente dti) {
        InfoExtraDocente ied = new InfoExtraDocente();
        ColocarDatosEnListas(dti.getCursos(), ied.getListCursos());
        ColocarDatosEnListas(dti.getEdiciones(), ied.getListEdiciones());
        ColocarDatosEnListas(dti.getProgramas(), ied.getListProgramas());
        
        MostrarPanelInfoExtra(ied);
    } else if (dt instanceof DTUsuario dtu) {
        InfoExtraUsuario ieu = new InfoExtraUsuario();
        ColocarDatosEnListas(dtu.getEdiciones(), ieu.getListEdiciones());
        ColocarDatosEnListas(dtu.getProgramas(), ieu.getListProgramas());
        MostrarPanelInfoExtra(ieu);
    }
}

/**
 * Carga y escala la imagen de perfil de forma asíncrona desde el servidor Tomcat o disco local
 */
    private void cargarImagenDesdeServidor(String nombreImagen) {
        new Thread(() -> {
            try {
                Image img = null;

                // Opción 1: Intentar leer desde la ruta absoluta del servidor (Si corre localmente)
                File archivoLocal = new File("C:" + File.separator + "mi_proyecto_data" + File.separator + "uploads" + File.separator + "perfiles" + File.separator + nombreImagen);

                if (archivoLocal.exists()) {
                    img = javax.imageio.ImageIO.read(archivoLocal);
                } else {
                    // Opción 2: Si no está local o el servidor es remoto, solicitar por HTTP
                    java.net.URL url = new java.net.URL("http://localhost:8080/PDA_WebServer/uploads/perfiles/" + nombreImagen);
                    img = javax.imageio.ImageIO.read(url);
                }

                if (img != null) {
                    int width = labelIcon.getWidth() > 0 ? labelIcon.getWidth() : 114;
                    int height = labelIcon.getHeight() > 0 ? labelIcon.getHeight() : 114;

                    Image imgEscalada = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    ImageIcon iconFinal = new ImageIcon(imgEscalada);

                    // Actualizar el componente Swing en el EDT (Event Dispatch Thread)
                    javax.swing.SwingUtilities.invokeLater(() -> labelIcon.setIcon(iconFinal));
                }
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen de perfil: " + e.getMessage());
                javax.swing.SwingUtilities.invokeLater(() -> labelIcon.setIcon(null));
            }
        }).start();
    }
    private void ColocarDatosEnListas(List<String> list, JList jList){
        DefaultListModel<String> modelo = new DefaultListModel<>();
        jList.setModel(modelo);
        modelo.clear();
        List<String> auxList = list;
        auxList = OrdenarLista(auxList);
        for(int i =0;i<list.size();i++){
            modelo.addElement(auxList.get(i));
        }
    }
    
    
    //Metodo de ordenar mas simple, fue creado por la ia
    private List<String> OrdenarLista(List<String> listaParam) {
        if (listaParam == null || listaParam.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> auxStr = new ArrayList<>(listaParam);

        auxStr.sort((a, b) -> a.compareToIgnoreCase(b));

        return auxStr;
    }
    private void MostrarPanelInfoExtra(JPanel jp){
        int wPanel = panelInfoExtra.getWidth(), hPanel = panelInfoExtra.getHeight(); // Width y Height del panelInfoExtra
        jp.setSize(wPanel, hPanel);
        jp.setLocation(0, 0);
        panelInfoExtra.removeAll();
        panelInfoExtra.add(jp, java.awt.BorderLayout.CENTER);
        panelInfoExtra.revalidate();
        panelInfoExtra.repaint();
    }

    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelIcon = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        labelNickname = new javax.swing.JLabel();
        labelNombre = new javax.swing.JLabel();
        labelApellido = new javax.swing.JLabel();
        labelFecha = new javax.swing.JLabel();
        labelCorreo = new javax.swing.JLabel();
        panelInfoExtra = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);

        labelIcon.setBackground(new java.awt.Color(255, 255, 255));
        labelIcon.setForeground(new java.awt.Color(255, 255, 255));
        labelIcon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setText("Nickname");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText("Nombre");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setText("Apellido");
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setText("Fecha de Nacimiento");
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setText("Correo Electronico");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelNickname.setText("Texto");
        labelNickname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelNombre.setText("Texto");
        labelNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelApellido.setText("Texto");
        labelApellido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelFecha.setText("Texto");
        labelFecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelCorreo.setText("<html><a href=''>Texto</a></html>");
        labelCorreo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panelInfoExtra.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoExtra.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelInfoExtraLayout = new javax.swing.GroupLayout(panelInfoExtra);
        panelInfoExtra.setLayout(panelInfoExtraLayout);
        panelInfoExtraLayout.setHorizontalGroup(
            panelInfoExtraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 489, Short.MAX_VALUE)
        );
        panelInfoExtraLayout.setVerticalGroup(
            panelInfoExtraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCorreo)
                    .addComponent(labelFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelApellido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelNickname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(panelInfoExtra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelNickname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelNombre)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelApellido)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelFecha)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInfoExtra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel labelApellido;
    private javax.swing.JLabel labelCorreo;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JLabel labelIcon;
    private javax.swing.JLabel labelNickname;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JPanel panelInfoExtra;
    // End of variables declaration//GEN-END:variables
}
