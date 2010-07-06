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

    private LstData lstDay;    // Aqua at 13:00
    private LstData lstNight;  // Aqua at 01:00
    // Terra satellite data
    private LstData lstMorning;    // Terra at 07:00
    private LstData lstAfternoon;  // Terra at 19:00
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
            Double interpTempDay, interpTempNight, interpTempMorning, interpTempAfternoon;

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            DecimalFormat formatter = new DecimalFormat("0.00", symbols);

            for (int i = 0; i < aquaFiles.length; i++) {
//			for (int i = 0; i < 1; i++) {
                System.out.println("Leyendo " + aquaFiles[i]);
                System.out.println("Leyendo " + terraFiles[i]);
                try {
                    aquaLoader.openFile(dirAqua.getAbsolutePath() + "\\" + aquaFiles[i]);
                    lstDay = readDataset(ModisLoader.LST_DAY_1KM, aquaLoader);
                    lstNight = readDataset(ModisLoader.LST_NIGHT_1KM, aquaLoader);

                    terraLoader.openFile(dirTerra.getAbsolutePath() + "\\" + terraFiles[i]);
                    lstMorning = readDataset(ModisLoader.LST_NIGHT_1KM, terraLoader);
                    lstAfternoon = readDataset(ModisLoader.LST_DAY_1KM, terraLoader);

                    for (int j = 0; j < stations.getNumStations(); j++) {
                        StationData station = stations.getStations()[j];
                        interpTempDay = lstDay.getInterpolatedTemperature(station.getLat(), station.getLon());
                        interpTempNight = lstNight.getInterpolatedTemperature(station.getLat(), station.getLon());
                        interpTempMorning = lstMorning.getInterpolatedTemperature(station.getLat(), station.getLon());
                        interpTempAfternoon = lstAfternoon.getInterpolatedTemperature(station.getLat(), station.getLon());
                        String line = new Integer(i + 1).toString();
                        line += " " + new Double(station.getTmin()[i]).toString();
                        line += " " + new Double(station.getTmax()[i]).toString();

                        if ((interpTempNight == 0.0) && (writeMissingTemp)) {
                            line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
                        } else if (interpTempNight != 0.0) {
                            line += " " + formatter.format(interpTempNight - 273.15);
                        } else {
                            continue;
                        }

                        if ((interpTempMorning == 0.0) && (writeMissingTemp)) {
                            line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
                        } else if (interpTempMorning != 0.0) {
                            line += " " + formatter.format(interpTempMorning - 273.15);
                        } else {
                            continue;
                        }

                        if ((interpTempDay == 0.0) && (writeMissingTemp)) {
                            line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
                        } else if (interpTempDay != 0.0) {
                            line += " " + formatter.format(interpTempDay - 273.15);
                        } else {
                            continue;
                        }

                        if ((interpTempAfternoon == 0.0) && (writeMissingTemp)) {
                            line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
                        } else if (interpTempAfternoon != 0.0) {
                            line += " " + formatter.format(interpTempAfternoon - 273.15);
                        } else {
                            continue;
                        }
//						if (interpTempDay == 0.0)
//							line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
//						else
//							line += " " + formatter.format(interpTempDay - 273.15);
//						if (interpTempNight == 0.0)
//							line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
//						else
//							line += " " + formatter.format(interpTempNight - 273.15);

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
//					lstDay[i].getInterpolatedTemperature(28.2230644372507, -16.75267301976);
//					lstDay[i].getInterpTemp(28.2230644372507, -16.75267301976);
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
