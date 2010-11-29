/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etp.modelo;

import etp.configuracion.ParamConfig;
import etp.modelo.events.ModelWarningEvent;
import etp.modelo.exceptions.DistinctNumberTerraAquaFilesException;
import etp.modelo.exceptions.ModelException;
import etp.modelo.exceptions.NoFilesLoadedException;
import etp.modelo.exceptions.NoHdfFilesException;
import etp.modelo.exceptions.TooManyDaysRequestedException;

/**
 *
 * @author Fran
 */
class CargadorHdf extends BuscadorHdf {

    //private BuscadorHdf buscador;
    protected LstData[] lst11h;  // Terra at 7:00 AM
    protected LstData[] lst14h; // Aqua at 13:00 PM
    protected LstData[] lst23h; // Terra at 19:00 PM
    protected LstData[] lst02h;  // Aqua at 01:00 AM
    protected boolean datosCargados;
    private ModisLoader cargadorModis;
    private String dirHdf;
    private String separador;
    public static final int MAX_HDF_FILES = 10;

    protected CargadorHdf(ParamConfig config) {
        super(config);
        dirHdf = config.getDirHdfs();
        separador = config.getSeparador();
        //buscador = new BuscadorHdf(config);
        datosCargados = false;
        cargadorModis = new ModisLoader();
    }

    /**
     * Busca los ficheros HDF correspondientes al intervalo de días solicitado y
     * los carga en memoria. El intervalo se especifica mediante una fecha de referencia y
     * los días que se van a utilizar, contando el día de referencia. Por ejemplo, si
     * especificamos el día 10-04-2010 y 5 días utilizados, buscará los HDF
     * desde el día 06 hasta el día 10 de abril de 2010, ambos inclusive.
     *
     * @param dia Día del mes de la fecha de referencia.
     * @param mes Mes de la fecha referencia.
     * @param ano Año de la fecha referencia.
     * @param diasUtilizados Días utilizados, inclusive la fecha de referencia, que
     * permiten especificar el intervalo de fechas solicitado.
     *
     * @return Número de días efectivamente cargados en memoria (pueden ser menos
     * de los solicitados).
     */
    public int buscarHdfs(int dia, int mes, int ano, int diasUtilizados) throws ModelException {
        if (diasUtilizados > MAX_HDF_FILES) {
            System.out.println("Error: la cantidad de días utilizados no debe ser superior a " + Integer.toString(MAX_HDF_FILES) + ".");
            throw new TooManyDaysRequestedException(MAX_HDF_FILES);
        } else {
            buscarIntervalo(dia, mes, ano, diasUtilizados);
            if (fichAquaFallo.length > 0) {
                fireModelWarningEvent(new ModelWarningEvent(this, ModelWarningEvent.MISSING_AQUA_FILES));
                System.out.println("Aviso: no se han encontrado los ficheros HDF de Aqua en las siguientes fechas:");
                for (int i = 0; i < fichAquaFallo.length; i++) {
                    System.out.println(getFichAquaFallo()[i]);
                }
            }
            if (fichTerraFallo.length > 0) {
                fireModelWarningEvent(new ModelWarningEvent(this, ModelWarningEvent.MISSING_TERRA_FILES));
                System.out.println("Aviso: no se han encontrado los ficheros HDF de Terra en las siguientes fechas:");
                for (int i = 0; i < fichTerraFallo.length; i++) {
                    System.out.println(fichTerraFallo[i]);
                }
            }

            if (fichAqua.length != fichTerra.length) {
                System.out.println("Error: el número de ficheros de Aqua y Terra encontrados es distinto.");
                throw new DistinctNumberTerraAquaFilesException();
            } else if (fichAqua.length == 0) {
                System.out.println("Error: el número de ficheros de Aqua es 0.");
                throw new NoHdfFilesException("Aqua files number is 0.");
            } else if (fichTerra.length == 0) {
                System.out.println("Error: el número de ficheros de Terra es 0.");
                throw new NoHdfFilesException("Terra files number is 0.");
            } else {
                // Cargamos los ficheros en memoria
                return cargarHdfs();
            }
        }
    }

    /**
     * Carga en memoria los ficheros HDF previamente buscados por buscarHdfs()
     *
     * @return Devuelve el número máximo de días disponibles en memoria
     */
    private int cargarHdfs() throws NoFilesLoadedException {
//        String[] fichAqua = getFichAqua();
//        String[] fichTerra = getFichTerra();
        lst11h = new LstData[fichTerra.length];
        lst14h = new LstData[fichAqua.length];
        lst23h = new LstData[fichTerra.length];
        lst02h = new LstData[fichAqua.length];

        // Cargamos los ficheros de Terra
        for (int i = 0; i < fichTerra.length; i++) {
            try {
                cargadorModis.openFile(dirHdf + separador + fichTerra[i]);
                lst11h[i] = readDataset(ModisLoader.LST_DAY_1KM, cargadorModis);
                lst23h[i] = readDataset(ModisLoader.LST_NIGHT_1KM, cargadorModis);
            }
            catch (Exception e) {
                System.out.println("Error cargando ficheros Terra");
                e.printStackTrace();
                throw new NoFilesLoadedException();
            }
        }

        // Cargamos los ficheros de Aqua
        for (int i = 0; i < fichAqua.length; i++) {
            try {
                cargadorModis.openFile(dirHdf + separador + fichAqua[i]);
                lst14h[i] = readDataset(ModisLoader.LST_DAY_1KM, cargadorModis);
                lst02h[i] = readDataset(ModisLoader.LST_NIGHT_1KM, cargadorModis);
            }
            catch (Exception e) {
                System.out.println("Error cargando ficheros Aqua");
                e.printStackTrace();
                throw new NoFilesLoadedException();
            }
        }

        /* Los dos deben tener la misma longitud (se ha comprobado previa-
         * mente en buscarHdfs()), por lo que da igual el que devolvamos.
         */
        datosCargados = true;
        return fichAqua.length;
    }

    private LstData readDataset(int datasetType, ModisLoader loader) throws Exception {
        LstData data = new LstData(LstConstants.DIM_X, LstConstants.DIM_Y,
                LstConstants.SCALE_FACTOR);
        loader.readDataset(data, datasetType);
        return data;
    }

    public LstData[] getLst14h() {
        return lst14h;
    }

    public LstData[] getLst23h() {
        return lst23h;
    }

    public LstData[] getLst02h() {
        return lst02h;
    }

    public LstData[] getLst11h() {
        return lst11h;
    }

    public String getDirHdf() {
        return dirHdf;
    }

    public void setDirHdf(String dirHdf) {
        this.dirHdf = dirHdf;
    }

    
    /**
     * Indica si para la búsqueda de los ficheros HDF se usará el FTP si dichos
     * ficheros no se encuentran en el disco duro local.
     *
     * @return True si se usa el FTP para buscar los HDF; false si no se usa.
     */
//    public Boolean getUsarFtp() {
//        return buscador.getUsarFtp();
//    }

    /**
     * Indica si para la búsqueda de los ficheros HDF se usará el FTP si dichos
     * ficheros no se encuentran en el disco duro local.
     *
     * @param usarFtp A true indica que se use el FTP para buscar los HDF.
     */
//    public void setUsarFtp(Boolean usarFtp) {
//        buscador.setUsarFtp(usarFtp);
//    }
}
