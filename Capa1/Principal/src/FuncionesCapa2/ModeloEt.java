/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FuncionesCapa2;

/**
 *
 * @author Fran
 */
public class ModeloEt {

    private CargadorHdf cargador;
    private int diasAtrasUtilizados;
    private boolean datosCargados;
    private double pendiente, despY;
    public static final double NO_EVAPOTRANSP = -1.0;

    public ModeloEt(int diasHaciaAtras, String dirHdfs, String dirFtps, double pendiente, double despY) {
        cargador = new CargadorHdf(dirHdfs, dirFtps);
        diasAtrasUtilizados = diasHaciaAtras;
        datosCargados = false;
        this.pendiente = pendiente;
        this.despY = despY;
    }

    public boolean cargarDia(int dia, int mes, int ano) {
        if (cargador.buscarHdfs(dia, mes, ano, diasAtrasUtilizados)) {
            datosCargados = true;
            return true;
        } else {
            System.out.println("Error: no se han podido cargar los ficheros para las fechas solicitadas.");
            return false;
        }
    }

    // Este método es el que hay que cambiar si cambiamos el modelo de evapotranspiración
    public double getEtByLatLon(double lat, double lon) {
        if (datosCargados) {
            double temp = cargador.getLst7am()[0].getInterpolatedTemperature(lat, lon);
            if (temp != 0)
                return ((temp - 273.15) * pendiente) + despY;
            else
                return NO_EVAPOTRANSP;
        } else {
            return -1;
        }
    }

    public double getEtByPos(int x, int y) {
        if (datosCargados) {
            double[] latLon = cargador.getLst7am()[0].getLatLngByPos(x, y);
            return getEtByLatLon(latLon[0], latLon[1]);
        }
        else {
            return -1;
        }
    }

    public int getDiasAtrasUtilizados() {
        return diasAtrasUtilizados;
    }

    public void setDiasAtrasUtilizados(int diasAtrasUtilizados) {
        this.diasAtrasUtilizados = diasAtrasUtilizados;
    }
}
