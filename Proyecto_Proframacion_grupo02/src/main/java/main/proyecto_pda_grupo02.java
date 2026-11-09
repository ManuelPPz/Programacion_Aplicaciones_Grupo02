package main;

import interfaces.Presentacion;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import util.ConfigSC;
import webservices.ServidorWS;

public class proyecto_pda_grupo02 {

    public static void main(String[] args) {
        // 1. Configuración (.properties): URL del Web Service y carpeta de imágenes
        ConfigSC config = ConfigSC.cargar();
        config.aplicarImagenesDir();
        System.out.println("Configuración leída de: " + config.getOrigen());

        // 2. Verificar/crear las tablas antes de atender pedidos
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");
            emf.close();
            System.out.println("Tablas verificadas/creadas correctamente.");
        } catch (Exception e) {
            System.err.println("No se pudo conectar a la base de datos: " + e.getMessage());
        }

        // 3. Publicar el Web Service para el servidor web
        String errorWS = null;
        try {
            String wsdl = ServidorWS.publicar(config.getWsUrl());
            System.out.println("Web Service publicado. WSDL: " + wsdl);
        } catch (Exception e) {
            errorWS = "No se pudo publicar el Web Service en " + config.getWsUrl() + ":\n" + e.getMessage();
            System.err.println(errorWS);
        }

        // 4. Interfaz gráfica en el hilo de eventos de Swing
        final String avisoWS = errorWS;
        java.awt.EventQueue.invokeLater(() -> {
            Presentacion vPresentacion = new Presentacion();
            vPresentacion.setBounds(2, 2, 1280, 720);
            vPresentacion.setLocationRelativeTo(null); // Centra la ventana en pantalla
            vPresentacion.setResizable(false);
            vPresentacion.setVisible(true);
            if (avisoWS != null) {
                javax.swing.JOptionPane.showMessageDialog(vPresentacion,
                        avisoWS + "\n\nRevisá ws.url en config.properties (¿puerto ocupado?).",
                        "Web Service", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
