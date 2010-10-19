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
    private HorasSat horaDeseada;
    private boolean datosCargados;
    private double pendiente, despY;
    public static final double NO_EVAPOTRANSP = -1.0;
    public static final int AQUA_02_AM = 0;
    public static final int AQUA_14_PM = 2;
    public static final int TERRA_11_AM = 1;
    public static final int TERRA_23_PM = 3;

    public ModeloEt(int diasHaciaAtras, HorasSat horaDeseada, String dirHdfs, String dirFtps, double pendiente, double despY) {
        cargador = new CargadorHdf(dirHdfs, dirFtps);
        diasAtrasUtilizados = diasHaciaAtras;
        this.horaDeseada = horaDeseada;
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
            double sumaTemp = 0;
            int numElemValid = 0;

            for (int i = 0; i <= diasAtrasUtilizados; i++) {
//                double temp = HorasSat.getHoraSat(horaDeseada).getLst(cargador)[i].getInterpolatedTemperature(lat, lon);
                double temp = horaDeseada.getLst(cargador)[i].getInterpolatedTemperature(lat, lon);
                if (temp != 0) {
                    sumaTemp += temp;
                    numElemValid++;
                }
//                switch (horaDeseada) {
//                    case AQUA_02_AM:
//                        temp += cargador.getLst02h()[i].getInterpolatedTemperature(lat, lon);
//                        break;
//                    case TERRA_11_AM:
//                        temp += cargador.getLst11h()[i].getInterpolatedTemperature(lat, lon);
//                        break;
//                    case AQUA_14_PM:
//                        temp += cargador.getLst14h()[i].getInterpolatedTemperature(lat, lon);
//                        break;
//                    case TERRA_23_PM:
//                        temp += cargador.getLst23h()[i].getInterpolatedTemperature(lat, lon);
//                        break;
//                    default:
//                        System.out.println("Error: la hora solicitada no es correcta.");
//                }
            }
            if (sumaTemp != 0) {
                sumaTemp /= numElemValid;
                return ((sumaTemp - 273.15) * pendiente) + despY;
            }
            else
                return NO_EVAPOTRANSP;
        } else {
            return -1;
        }
    }

    public double getEtByPos(int x, int y) {
        if (datosCargados) {
            double[] latLon = cargador.getLst11h()[0].getLatLngByPos(x, y);
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
