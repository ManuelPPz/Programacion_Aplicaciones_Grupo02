package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    // Ya no es final ni se inicializa al cargar la clase
    private static EntityManagerFactory emf;

    // La fábrica se crea una sola vez, pero recién cuando se la pide por primera vez
    private static synchronized EntityManagerFactory getEmf() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("ControladorPU");
        }
        return emf;
    }

    // Método estático para obtener un EntityManager en cada consulta
    public static EntityManager getEntityManager() {
        return getEmf().createEntityManager();
    }

    // Método para cerrar la fábrica al finalizar la aplicación
    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        emf = null;
    }
}