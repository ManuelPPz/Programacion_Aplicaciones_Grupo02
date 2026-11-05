package main;

// 1. Importamos la clase Presentacion que está en el paquete Interfaces
import interfaces.Presentacion;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import WebServer.LabServiceImpl;
import jakarta.xml.ws.Endpoint;

public class proyecto_pda_grupo02 {

    public static void main(String[] args) {
        
    //Activar el servidor
        String url = "http://0.0.0.0:8181/ws/lab";
        System.out.println("Iniciando servidor SOAP...");
        Endpoint.publish(url, new LabServiceImpl());

        System.out.println("¡Servidor SOAP activo y escuchando!");
        System.out.println("WSDL disponible en: http://<IP DE ESTA PC>:8181/ws/lab?wsdl");
        
        //Llamar a las interfaces
        java.awt.EventQueue.invokeLater(() -> {
            Presentacion vPresentacion = new Presentacion();
            vPresentacion.setBounds(2, 2, 1280, 720);
            vPresentacion.setLocationRelativeTo(null); // Centra la ventana en pantalla
            vPresentacion.setResizable(false);
            vPresentacion.setVisible(true);            // Muestra la ventana
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");
        
        System.out.println("Tablas verificadas/creadas correctamente.");
        
        emf.close();
        });
    }
}