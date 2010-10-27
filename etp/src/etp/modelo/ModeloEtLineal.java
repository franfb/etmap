/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etp.modelo;

import etp.configuracion.ParamConfig;
import etp.modelo.exceptions.DataNotLoadedException;

/**
 *
 * @author Fran
 */
public class ModeloEtLineal {

    private CargadorHdf cargador;
    private int diasUtilizados;
    private HorasSat horaDeseada;
    private boolean datosCargados;
    private double pendiente, despY;
    public static final double NO_EVAPOTRANSP = -1.0;
    public static final int AQUA_02_AM = 0;
    public static final int AQUA_14_PM = 2;
    public static final int TERRA_11_AM = 1;
    public static final int TERRA_23_PM = 3;

    public ModeloEtLineal(ParamConfig config, double pendiente, double despY) {
        cargador = new CargadorHdf(config);
        this.diasUtilizados = config.getDiasUtilizados();
        this.horaDeseada = config.getHoraDeseada();
        datosCargados = false;
        this.pendiente = pendiente;
        this.despY = despY;
    }

    public boolean cargarDia(int dia, int mes, int ano) {
        if (cargador.buscarHdfs(dia, mes, ano, diasUtilizados)) {
            datosCargados = true;
            return true;
        } else {
            System.out.println("Error: no se han podido cargar los ficheros para las fechas solicitadas.");
            return false;
        }
    }

    // Este método es el que hay que cambiar si cambiamos el modelo de evapotranspiración
    public double getEtByLatLon(double lat, double lon) throws DataNotLoadedException {
        if (datosCargados) {
            double sumaTemp = 0;
            int numElemValid = 0;

            Integer maxDias = diasUtilizados;
            // Si no hay suficientes días cargados en memoria, usamos los que haya
            if (diasUtilizados > horaDeseada.getLst(cargador).length) {
                maxDias = horaDeseada.getLst(cargador).length;
            }

            for (int i = 0; i < maxDias; i++) {
                double temp = horaDeseada.getLst(cargador)[i].getInterpolatedTemperature(lat, lon);
                if (temp != 0) {
                    sumaTemp += temp;
                    numElemValid++;
                }
            }
            if (sumaTemp != 0) {
                sumaTemp /= numElemValid;
                return ((sumaTemp - 273.15) * pendiente) + despY;
            }
            else
                return NO_EVAPOTRANSP;
        } else {
            throw new DataNotLoadedException();
        }
    }

    public double getEtByPos(int x, int y) throws DataNotLoadedException {
        if (datosCargados) {
            double[] latLon = cargador.getLst11h()[0].getLatLngByPos(x, y);
            return getEtByLatLon(latLon[0], latLon[1]);
        }
        else {
            return -1;
        }
    }

    public int getDiasUtilizados() {
        return diasUtilizados;
    }

    public void setDiasUtilizados(int diasUtilizados) {
        this.diasUtilizados = diasUtilizados;
    }

    public double getDespY() {
        return despY;
    }

    public void setDespY(double despY) {
        this.despY = despY;
    }

    public HorasSat getHoraDeseada() {
        return horaDeseada;
    }

    public void setHoraDeseada(HorasSat horaDeseada) {
        this.horaDeseada = horaDeseada;
    }

    public double getPendiente() {
        return pendiente;
    }

    public void setPendiente(double pendiente) {
        this.pendiente = pendiente;
    }

    /**
     * Indica si para la búsqueda de los ficheros HDF se usará el FTP si dichos
     * ficheros no se encuentran en el disco duro local.
     *
     * @return True si se usa el FTP para buscar los HDF; false si no se usa.
     */
    public Boolean getUsarFtp() {
        return cargador.getUsarFtp();
    }

    /**
     * Indica si para la búsqueda de los ficheros HDF se usará el FTP si dichos
     * ficheros no se encuentran en el disco duro local.
     *
     * @param usarFtp A true indica que se use el FTP para buscar los HDF.
     */
    public void setUsarFtp(Boolean usarFtp) {
        cargador.setUsarFtp(usarFtp);
    }
}
