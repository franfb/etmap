/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etp.modelo;

import etp.configuracion.ParamConfig;

/**
 *
 * @author Fran
 */
public class CargadorHdf {

    private BuscadorHdf buscador;
    private LstData[] lst11h;  // Terra at 7:00 AM
    private LstData[] lst14h; // Aqua at 13:00 PM
    private LstData[] lst23h; // Terra at 19:00 PM
    private LstData[] lst02h;  // Aqua at 01:00 AM
    private ModisLoader cargadorModis;
    private String dirHdf;
    private static final int MAX_HDF_FILES = 10;

    public CargadorHdf(ParamConfig config) {
        dirHdf = config.getDirHdfs();
        buscador = new BuscadorHdf(config);
        cargadorModis = new ModisLoader();
    }

    public int buscarHdfs(int dia, int mes, int ano, int diasUtilizados) {
        if (diasUtilizados > MAX_HDF_FILES) {
            System.out.println("Error: la cantidad de días utilizados no debe ser superior a " + Integer.toString(MAX_HDF_FILES) + ".");
        } else {
            buscador.buscarIntervalo(dia, mes, ano, diasUtilizados);
            if (buscador.getFichAquaFallo().length > 0) {
                System.out.println("Aviso: no se han encontrado los ficheros HDF de Aqua en las siguientes fechas:");
                for (int i = 0; i < buscador.getFichAquaFallo().length; i++) {
                    System.out.println(buscador.getFichAquaFallo()[i]);
                }
            }
            if (buscador.getFichTerraFallo().length > 0) {
                System.out.println("Aviso: no se han encontrado los ficheros HDF de Terra en las siguientes fechas:");
                for (int i = 0; i < buscador.getFichTerraFallo().length; i++) {
                    System.out.println(buscador.getFichTerraFallo()[i]);
                }
            }

            if (buscador.getFichAqua().length != buscador.getFichTerra().length) {
                System.out.println("Error: el número de ficheros de Aqua y Terra encontrados es distinto.");
            } else if (buscador.getFichAqua().length == 0) {
                System.out.println("Error: el número de ficheros de Aqua es 0.");
            } else if (buscador.getFichTerra().length == 0) {
                System.out.println("Error: el número de ficheros de Terra es 0.");
            } else {
                // Cargamos los ficheros en memoria
                return cargarHdfs();
            }
        }
        return 0;
    }

    /**
     * Carga en memoria los ficheros HDF previamente buscados por buscarHdfs()
     *
     * @return Devuelve el número máximo de días disponibles en memoria
     */
    private int cargarHdfs() {
        String[] fichAqua = buscador.getFichAqua();
        String[] fichTerra = buscador.getFichTerra();
        lst11h = new LstData[fichTerra.length];
        lst14h = new LstData[fichAqua.length];
        lst23h = new LstData[fichTerra.length];
        lst02h = new LstData[fichAqua.length];

        boolean status = true;
        // Cargamos los ficheros de Terra
        for (int i = 0; i < fichTerra.length; i++) {
            try {
                cargadorModis.openFile(dirHdf + "\\" + fichTerra[i]);
                lst11h[i] = readDataset(ModisLoader.LST_DAY_1KM, cargadorModis);
                lst23h[i] = readDataset(ModisLoader.LST_NIGHT_1KM, cargadorModis);
            }
            catch (Exception e) {
                e.printStackTrace();
                status = false;
            }
        }

        // Cargamos los ficheros de Aqua
        for (int i = 0; i < fichAqua.length; i++) {
            try {
                cargadorModis.openFile(dirHdf + "\\" + fichAqua[i]);
                lst14h[i] = readDataset(ModisLoader.LST_DAY_1KM, cargadorModis);
                lst02h[i] = readDataset(ModisLoader.LST_NIGHT_1KM, cargadorModis);
            }
            catch (Exception e) {
                e.printStackTrace();
                status = false;
            }
        }

        if (status) {
            return (fichTerra.length < fichAqua.length) ? fichTerra.length : fichAqua.length;
        }
        else {
            return 0;
        }
    }

    private LstData readDataset(int datasetType, ModisLoader loader) throws Exception {
        LstData data = new LstData(LstConstants.DIM_X, LstConstants.DIM_Y,
                LstConstants.SCALE_FACTOR);
        loader.readDataset(data, datasetType);
//		data.setCoordinates();
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

    /**
     * Indica si para la búsqueda de los ficheros HDF se usará el FTP si dichos
     * ficheros no se encuentran en el disco duro local.
     *
     * @return True si se usa el FTP para buscar los HDF; false si no se usa.
     */
    public Boolean getUsarFtp() {
        return buscador.getUsarFtp();
    }

    /**
     * Indica si para la búsqueda de los ficheros HDF se usará el FTP si dichos
     * ficheros no se encuentran en el disco duro local.
     *
     * @param usarFtp A true indica que se use el FTP para buscar los HDF.
     */
    public void setUsarFtp(Boolean usarFtp) {
        buscador.setUsarFtp(usarFtp);
    }
}
