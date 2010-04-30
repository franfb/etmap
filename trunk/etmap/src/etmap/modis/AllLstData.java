package etmap.modis;

import datos.LstConstants;
import datos.LstData;
import datos.ModisLoader;
import etmap.stations.AllStations;
import etmap.stations.StationConstants;
import etmap.stations.StationData;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class AllLstData {
	private LstData lstDay;
	private LstData lstNight;
	private ModisLoader loader;
	private static final int MAX_LST_FILES = 365;

	public AllLstData() {
		loader = new ModisLoader();
	}

	public void writComparisonFile(String hdfDirName) {
		// Buscamos todos los ficheros HDF del directorio que queremos usar
		FilenameFilter hdfFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".hdf");
			}
		};

		File dir = new File(hdfDirName);
		String[] children = dir.list(hdfFilter);
		if (children != null) {
			if (children.length > MAX_LST_FILES) {
				System.out
						.println("AVISO: Hay m�s de 365 ficheros HDF en el directorio");
			}
			
			// Cargamos los datos de todas las estaciones
			AllStations stations = new AllStations();
			stations.readStationsData("D:\\etsii\\pfc\\datos\\estacionesagrocabildo.txt", "2009");
			
			// Leemos los ficheros HDF y generamos la comparaci�n diaria entre los HDF y las estaciones
			Double interpTempDay, interpTempNight;
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			DecimalFormat formatter = new DecimalFormat("0.00", symbols);
			for (int i = 0; i < children.length; i++) {
//			for (int i = 0; i < 1; i++) {
				System.out.println("Leyendo " + children[i]);
				try {
					loader.openFile(dir.getAbsolutePath() + "\\" + children[i]);
					lstDay = readDataset(ModisLoader.LST_DAY_1KM);
					lstNight = readDataset(ModisLoader.LST_NIGHT_1KM);
					for (int j = 0; j < stations.getNumStations(); j++) {
						StationData station = stations.getStations()[j];
						interpTempDay = lstDay.getInterpolatedTemperature(station.getLat(), station.getLon());
						interpTempNight = lstNight.getInterpolatedTemperature(station.getLat(), station.getLon());
						String line = new Integer(i + 1).toString();
						line += " " + new Double(station.getTmin()[i]).toString();
						line += " " + new Double(station.getTmax()[i]).toString();
						
						if (interpTempDay == 0.0)
							line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
						else
							line += " " + formatter.format(interpTempDay - 273.15);
						if (interpTempNight == 0.0)
							line += " " + formatter.format(StationConstants.NO_TEMPERATURE);
						else
							line += " " + formatter.format(interpTempNight - 273.15);
						
						line += " " + new Double(station.getEvTransp()[i]).toString();
						station.writeOutLine(line);
					}
					loader.closeFile();
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
		}

	}

	private LstData readDataset(int datasetType) throws Exception {
		LstData data = new LstData(LstConstants.DIM_X, LstConstants.DIM_Y,
				LstConstants.SCALE_FACTOR);
		loader.readDataset(data, datasetType);
//		data.setCoordinates();
		return data;
	}
}
