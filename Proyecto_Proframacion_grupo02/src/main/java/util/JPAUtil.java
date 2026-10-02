package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    // La fábrica se crea una sola vez al cargar la clase
    private static final EntityManagerFactory emf = 
        Persistence.createEntityManagerFactory("ControladorPU");

    // Método estático para obtener un EntityManager en cada consulta
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Método para cerrar la fábrica al finalizar la aplicación
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}