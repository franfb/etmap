package etmap.stations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class AllStations {
	private StationData[] stations;
	private String year;
	private int maxStations = 45;
	private int numStations;

	public void readStationsData(String stationsFile, String year) {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			archivo = new File (stationsFile);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea, nombre, tipo;
			double lon, lat;
			int alt;
			this.year = year;
			stations = new StationData[maxStations];
			int i = 0;
			while ((linea = br.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(linea);
				nombre = tokens.nextToken();
				tipo = tokens.nextToken();
				lon = new Double(tokens.nextToken());
				lat = new Double(tokens.nextToken());
				alt = new Integer(tokens.nextToken());
				stations[i] = new StationData();
				// Con el \\..\\ eliminamos el nombre del archivo al final de la ruta que devuelve getAbsolutePath()
				stations[i].setData(nombre, lat, lon, alt, year, archivo.getAbsolutePath() + "\\..\\");
//				stations[i].show();
				i++;
			}
			numStations = i;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (fr != null)
				try {
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public StationData[] getStations() {
		return stations;
	}

	public String getYear() {
		return year;
	}

	public int getNumStations() {
		return numStations;
	}
}
