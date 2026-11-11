package Logica.DTsClasses;

/**
 * Resumen de valoraciones de un curso (Requerimiento 7.6): total, cantidad por estrella
 * (posición 0 = 1 estrella ... 4 = 5 estrellas), promedio y el puntaje propio (0 = sin valorar).
 */
public class DTValoracionCurso extends DTMaster {

    public static final int MAX_ESTRELLAS = 5;

    private final String curso;
    private final int total;
    private final int[] estrellas;
    private final float promedio;
    private final int miPuntaje;

    public DTValoracionCurso(String curso, int total, int[] estrellas, float promedio, int miPuntaje) {
        this.curso = curso;
        this.total = total;
        this.estrellas = new int[MAX_ESTRELLAS];
        if (estrellas != null) {
            System.arraycopy(estrellas, 0, this.estrellas, 0, Math.min(estrellas.length, MAX_ESTRELLAS));
        }
        this.promedio = promedio;
        this.miPuntaje = miPuntaje;
    }

    public String getCurso() { return curso; }
    public int getTotal() { return total; }
    public float getPromedio() { return promedio; }
    public int getMiPuntaje() { return miPuntaje; }
    public boolean isValorado() { return miPuntaje > 0; }

    /** Cantidad de valoraciones con {@code n} estrellas (1 a 5). */
    public int cantidad(int n) {
        return (n >= 1 && n <= MAX_ESTRELLAS) ? estrellas[n - 1] : 0;
    }

    /** Porcentaje (0-100) de valoraciones con {@code n} estrellas, para las barras del resumen. */
    public int porcentaje(int n) {
        return (total == 0) ? 0 : Math.round(100f * cantidad(n) / total);
    }

    /** Promedio con un decimal, p. ej. "3,5" ("-" si nadie valoró). */
    public String getPromedioTexto() {
        return (total == 0) ? "-" : String.format(java.util.Locale.forLanguageTag("es-UY"), "%.1f", promedio);
    }

    /**
     * Tipo de cada una de las 5 estrellas para dibujar el promedio:
     * "full", "half" o "empty" (se redondea a la media estrella más cercana).
     */
    public String[] getIconos() {
        return iconos(promedio);
    }

    public static String[] iconos(float valor) {
        float redondeado = Math.round(valor * 2) / 2f;
        String[] res = new String[MAX_ESTRELLAS];
        for (int i = 0; i < MAX_ESTRELLAS; i++) {
            if (redondeado >= i + 1) {
                res[i] = "full";
            } else if (redondeado >= i + 0.5f) {
                res[i] = "half";
            } else {
                res[i] = "empty";
            }
        }
        return res;
    }

    /** Filas del resumen de 5 a 1 estrellas: {estrellas, cantidad, porcentaje}. */
    public int[][] getFilas() {
        int[][] filas = new int[MAX_ESTRELLAS][];
        for (int n = MAX_ESTRELLAS; n >= 1; n--) {
            filas[MAX_ESTRELLAS - n] = new int[]{n, cantidad(n), porcentaje(n)};
        }
        return filas;
    }
}
