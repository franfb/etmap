package etmap.modis;

import etmap.stations.AllStations;
import etmap.stations.StationConstants;
import etmap.stations.StationData;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class AllLstData {
    // Aqua satellite data
    // In the Aqua satellite data, the dataset LST_DAY_1KM = 14:00
    // Dataset LST_NIGHT_1KM = 02:00
    private LstData lstAqua14;    // Aqua at 14:00
    private LstData lstAqua02;  // Aqua at 02:00
    // Terra satellite data
    // In the Terra satellite data, the dataset LST_DAY_1KM = 11:00
    // Dataset LST_NIGHT_1KM = 23:00
    private LstData lstTerra11;    // Terra at 11:00
    private LstData lstTerra23;  // Terra at 23:00
    private ModisLoader aquaLoader, terraLoader;
    private static final int MAX_LST_FILES = 365;

    public AllLstData() {
        aquaLoader = new ModisLoader();
        terraLoader = new ModisLoader();
    }

    public void writeComparisonFile(String aquaDirName, String terraDirName, String stationsFileName, String year, boolean writeMissingTemp, boolean writeMissingEt) {
        // Buscamos todos los ficheros HDF del directorio que queremos usar
        FilenameFilter hdfFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".hdf");
            }
        };

        File dirAqua = new File(aquaDirName);
        String[] aquaFiles = dirAqua.list(hdfFilter);
        File dirTerra = new File(terraDirName);
        String[] terraFiles = dirTerra.list(hdfFilter);

        if ((aquaFiles != null) && (aquaFiles.length == terraFiles.length)) {
            if (aquaFiles.length > MAX_LST_FILES) {
                System.out.println("AVISO: Hay más de 365 ficheros HDF en el directorio");
            }

            // Cargamos los datos de todas las estaciones
            AllStations stations = new AllStations();
            stations.readStationsData(stationsFileName, year);

            // Leemos los ficheros HDF y generamos la comparaci�n diaria entre los HDF y las estaciones
            Double interpTempAqua14, interpTempAqua02, interpTempTerra11, interpTempTerra23;

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            DecimalFormat formatter = new DecimalFormat("0.00", symbols);

            for (int i = 0; i < aquaFiles.length; i++) {
//			for (int i = 0; i < 1; i++) {
                System.out.println("Leyendo " + aquaFiles[i]);
                System.out.println("Leyendo " + terraFiles[i]);
                try {
                    aquaLoader.openFile(dirAqua.getAbsolutePath() + "\\" + aquaFiles[i]);
                    lstAqua14 = readDataset(ModisLoader.LST_DAY_1KM, aquaLoader);
                    lstAqua02 = readDataset(ModisLoader.LST_NIGHT_1KM, aquaLoader);

                    terraLoader.openFile(dirTerra.getAbsolutePath() + "\\" + terraFiles[i]);
                    lstTerra11 = readDataset(ModisLoader.LST_DAY_1KM, terraLoader);
                    lstTerra23 = readDataset(ModisLoader.LST_NIGHT_1KM, terraLoader);

                    for (int j = 0; j < stations.getNumStations(); j++) {
                        StationData station = stations.getStations()[j];
                        interpTempAqua14 = lstAqua14.getInterpolatedTemperature(station.getLat(), station.getLon());
                        interpTempAqua02 = lstAqua02.getInterpolatedTemperature(station.getLat(), station.getLon());
                        interpTempTerra11 = lstTerra11.getInterpolatedTemperature(station.getLat(), station.getLon());
                        interpTempTerra23 = lstTerra23.getInterpolatedTemperature(station.getLat(), station.getLon());
                        String line = new Integer(i + 1).toString();
                        line += " " + new Double(station.getTmin()[i]).toString();
                        line += " " + new Double(station.getTmax()[i]).toString();

                        if ((interpTempAqua02 == 0.0) && (writeMissingTemp)) {
                            line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
                        } else if (interpTempAqua02 != 0.0) {
                            line += " " + formatter.format(interpTempAqua02 - 273.15);
                        } else {
                            continue;
                        }

                        if ((interpTempTerra11 == 0.0) && (writeMissingTemp)) {
                            line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
                        } else if (interpTempTerra11 != 0.0) {
                            line += " " + formatter.format(interpTempTerra11 - 273.15);
                        } else {
                            continue;
                        }

                        if ((interpTempAqua14 == 0.0) && (writeMissingTemp)) {
                            line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
                        } else if (interpTempAqua14 != 0.0) {
                            line += " " + formatter.format(interpTempAqua14 - 273.15);
                        } else {
                            continue;
                        }

                        if ((interpTempTerra23 == 0.0) && (writeMissingTemp)) {
                            line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
                        } else if (interpTempTerra23 != 0.0) {
                            line += " " + formatter.format(interpTempTerra23 - 273.15);
                        } else {
                            continue;
                        }
//						if (interpTempAqua14 == 0.0)
//							line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
//						else
//							line += " " + formatter.format(interpTempAqua14 - 273.15);
//						if (interpTempAqua02 == 0.0)
//							line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
//						else
//							line += " " + formatter.format(interpTempAqua02 - 273.15);

                        if ((station.getEvTransp()[i] == StationConstants.NO_EVAPO_TRANSP) && (!writeMissingEt)) {
                            continue;
                        }
                        else {
                            line += " " + new Double(station.getEvTransp()[i]).toString();
                        }
                        station.writeOutLine(line);
                    }
                    aquaLoader.closeFile();
                    terraLoader.closeFile();
//					lstAqua14[i].getInterpolatedTemperature(28.2230644372507, -16.75267301976);
//					lstAqua14[i].getInterpTemp(28.2230644372507, -16.75267301976);
//					System.in.read();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < stations.getNumStations(); i++) {
                stations.getStations()[i].closeOutFile();
            }
            System.out.println("Terminado.");
        } else if (aquaFiles.length != terraFiles.length) {
            System.out.println("Error: los directorios de Terra y de Aqua tienen distinto número de ficheros.");
        }

    }

    private LstData readDataset(int datasetType, ModisLoader loader) throws Exception {
        LstData data = new LstData(LstConstants.DIM_X, LstConstants.DIM_Y,
                LstConstants.SCALE_FACTOR);
        loader.readDataset(data, datasetType);
//		data.setCoordinates();
        return data;
    }
}
