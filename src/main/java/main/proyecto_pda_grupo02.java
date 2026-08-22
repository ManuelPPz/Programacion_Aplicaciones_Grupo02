package main;

// 1. Importamos la clase Presentacion que está en el paquete Interfaces
import interfaces.Presentacion;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class proyecto_pda_grupo02 {

    public static void main(String[] args) {
        // 2. Ejecutamos la interfaz gráfica en el hilo de eventos de Swing
        java.awt.EventQueue.invokeLater(() -> {
            Presentacion vPresentacion = new Presentacion();
            vPresentacion.setBounds(2, 2, 800, 600);
            vPresentacion.setLocationRelativeTo(null); // Centra la ventana en pantalla
            vPresentacion.setResizable(false);
            vPresentacion.setVisible(true);            // Muestra la ventana
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");
        
        System.out.println("Tablas verificadas/creadas correctamente.");
        
        emf.close();
        });
    }
}