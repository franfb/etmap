package etmap.stations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class StationData {
	private double lat, lon;
	private int alt;
	private String nombre, year;
	private double[] tmin, tmed, tmax, evTransp;
	private int maxDays = 365;
	private FileWriter fw;
	private BufferedWriter bw;
	
	public void setData(String nombre, double lat, double lon, int alt, String year, String path) {
		this.nombre = nombre;
		this.lat = lat;
		this.lon = lon;
		this.alt = alt;
		this.year = year;
		
		File archivo = null, outFile = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			archivo = new File (path + "\\Porestaciones2009et\\" + nombre + year + ".dat");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			
			outFile = new File(path + "\\Porestaciones2009et\\" + nombre + year + "_hdfComp.dat");
			fw = new FileWriter(outFile);
			bw = new BufferedWriter(fw);
			
			String linea;
			double tmin, tmed, tmax, evTransp;
			int diaJuliano;
			this.tmin = new double[maxDays];
			this.tmed = new double[maxDays];
			this.tmax = new double[maxDays];
			this.evTransp = new double[maxDays];
			int i = 0;
			while ((linea = br.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(linea);
				diaJuliano = new Integer(tokens.nextToken());
				// Nos saltamos el a�o, el mes y el dia
				tokens.nextToken();
				tokens.nextToken();
				tokens.nextToken();
				
				tmed = new Double(tokens.nextToken());
				tmax = new Double(tokens.nextToken());
				tmin = new Double(tokens.nextToken());
				
				// Nos saltamos los par�metros que no nos interesan (viento, precipitaci�n, etc.)
				tokens.nextToken();
				tokens.nextToken();
				tokens.nextToken();
				tokens.nextToken();
				tokens.nextToken();
				tokens.nextToken();
				tokens.nextToken();
				
				evTransp = new Double(tokens.nextToken());
				
				// Puede que falten datos entre dos d�as no sucesivos
				if ((i + 1) != diaJuliano) {
					for (int j = i; j < diaJuliano; j++) {
						this.tmin[j] = this.tmed[j] = this.tmax[j] = StationConstants.NO_TEMPERATURE;
					}
					i = diaJuliano - 1;
				}
				
				this.tmin[i] = tmin;
				this.tmed[i] = tmed;
				this.tmax[i] = tmax;
				this.evTransp[i] = evTransp;
				i++;
			}
			// Algunas estaciones no tienen datos hasta el final del a�o
			if (i < maxDays) {
				for (int j = i; j < maxDays; j++) {
					this.tmin[j] = this.tmed[j] = this.tmax[j] = StationConstants.NO_TEMPERATURE;
					this.evTransp[j] = StationConstants.NO_EVAPO_TRANSP;
				}
			}
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
	
	public void writeOutLine(String line) {
		try {
			bw.write(line + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeOutFile() {
		if (fw != null) {
			try {
				bw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void show() {
		System.out.println("Estaci�n: " + nombre);
		System.out.println("\tLatitud: " + lat);
		System.out.println("\tLongitud: " + lon);
		System.out.println("Datos:");
		for (int i = 0; i < maxDays; i++) {
			System.out.println("\t" + tmin[i] + " " + tmed[i] + " " + tmax[i] + " " + evTransp[i]);
		}
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public int getAlt() {
		return alt;
	}

	public String getNombre() {
		return nombre;
	}

	public String getYear() {
		return year;
	}

	public double[] getTmin() {
		return tmin;
	}

	public double[] getTmed() {
		return tmed;
	}

	public double[] getTmax() {
		return tmax;
	}
	
	public double[] getEvTransp() {
		return evTransp;
	}
}
