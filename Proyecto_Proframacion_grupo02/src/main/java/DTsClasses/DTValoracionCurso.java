package DTsClasses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * Resumen de las valoraciones de un curso (Requerimiento 7.6): total, cantidad por
 * estrella (posición 0 = 1 estrella ... posición 4 = 5 estrellas), promedio y el
 * puntaje que le dio el estudiante consultado (0 si todavía no lo valoró).
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DTValoracionCurso extends DTMaster {

    private String curso;
    private int total;
    private int[] estrellas = new int[5];
    private float promedio;
    private int miPuntaje;

    /** Constructor sin argumentos requerido por JAXB (Web Services). */
    protected DTValoracionCurso() {
    }

    public DTValoracionCurso(String curso, int[] estrellas, int miPuntaje) {
        this.curso = curso;
        this.estrellas = (estrellas != null) ? estrellas.clone() : new int[5];
        this.miPuntaje = miPuntaje;
        int suma = 0;
        for (int i = 0; i < this.estrellas.length; i++) {
            total += this.estrellas[i];
            suma += this.estrellas[i] * (i + 1);
        }
        this.promedio = (total == 0) ? 0f : (float) suma / total;
    }

    public String getCurso() { return curso; }
    public int getTotal() { return total; }
    public int[] getEstrellas() { return estrellas.clone(); }
    public float getPromedio() { return promedio; }
    public int getMiPuntaje() { return miPuntaje; }
}
