package DTsClasses;



import DTsClasses.DTMaster;

public class DTUnitValoracion extends DTMaster{
    private int totalValoracion;
    private int[] estrellas;
    private float promedio;

    public DTUnitValoracion() {
    }

    
    public DTUnitValoracion(int totalValoracion, int[] estrellas, float promedio) {
        this.totalValoracion = totalValoracion;
        this.estrellas=estrellas;
        this.promedio = promedio;
    }

    public int getTotalValoracion() {
        return totalValoracion;
    }

    public int[] getEstrellas() {
        return estrellas;
    }

    public float getPromedio() {
        return promedio;
    }

    public void setTotalValoracion(int totalValoracion) {
        this.totalValoracion = totalValoracion;
    }

    public void setEstrellas(int[] estrellas) {
        this.estrellas = estrellas;
    }


    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }
    
    
}
