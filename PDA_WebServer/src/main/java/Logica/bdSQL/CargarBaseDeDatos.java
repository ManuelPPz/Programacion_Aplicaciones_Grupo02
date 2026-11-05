package Logica.bdSQL;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CargarBaseDeDatos {

    public CargarBaseDeDatos() {
        iniciarConexion();
    }

    public static void iniciarConexion() {
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU")) {
            System.out.println("Tablas verificadas/creadas correctamente en la base de datos.");
        } catch (Exception e) {
            System.err.println("Error al inicializar EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Puedes ejecutar este main para testear la creación de tablas localmente
        iniciarConexion();
    }
}