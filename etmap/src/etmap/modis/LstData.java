package etmap.modis;

import java.lang.Math;

public class LstData {
	// Temperature data
	private short[] data;
	private int dimX, dimY;
	private double scaleFactor;
	// Coordinates data
	private double[] lat;
	private double[] lon;

	public LstData(int dimX, int dimY, double scaleFactor) {
		this.dimX = dimX;
		this.dimY = dimY;
		this.scaleFactor = scaleFactor;
	}

	public int getDimX() {
		return dimX;
	}

	public int getDimY() {
		return dimY;
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public void setData(short[] data) {
		this.data = data;
	}

	public short[] getData() {
		return data;
	}
	
	public double getScaledData(int x, int y) {
		return data[y * dimY + x] * scaleFactor;
	}
	
	public double[] getLatLngByPos(int x, int y) {
		double[] latLng = new double[2];
		double delta = LstConstants.DELTA;
		double vllat = LstConstants.VLLAT;
		double vulat = LstConstants.VULAT;
		double vllon = LstConstants.VLLON;
		double vrlon = LstConstants.VRLON;
		double r = LstConstants.EARTH_RADIUS;
		double pi = Math.PI;
		double la, lo;
		
		la = y * delta * (vllat - vulat) + vulat;
		latLng[0] = (la / r) * (180.0 / pi);
		lo = x * delta * (vrlon - vllon) + vllon;
		latLng[1] = (lo / (r * Math.cos(la / r))) * (180.0 / pi);
		
		return latLng;
	}
	
	public void setCoordinates() {
		if ((lat != null) && (lon != null)) {
			double[] latLng = new double[2];
			for (int y = 0; y < dimY; y++) {
				for (int x = 0; x < dimX; x++) {
					latLng = getLatLngByPos(x, y);
					lat[y * dimY + x] = latLng[0];
					lon[y * dimY + x] = latLng[1];
				}
			}
		} else {
			System.out.println("Las variables lat/lon no est�n inicializadas.");
		}
	}
	
	public double[] getLat() {
		return lat;
	}

	public double[] getLon() {
		return lon;
	}
	
	public double getInterpolatedTemperature(double lat, double lon) {
		// Buscamos la posici�n del dataset m�s pr�xima a la lat/lon solicitada
		double la = lat * (Math.PI / 180.0) * LstConstants.EARTH_RADIUS;
		double y_interp = (la - LstConstants.VULAT)
				/ (LstConstants.DELTA * (LstConstants.VLLAT - LstConstants.VULAT));
		double lo = (lon * (Math.PI / 180.0))
				* (LstConstants.EARTH_RADIUS * Math
						.cos(lat * (Math.PI / 180.0)));
		double x_interp = (lo - LstConstants.VLLON)
				/ (LstConstants.DELTA * (LstConstants.VRLON - LstConstants.VLLON));
		
		int x_izq = (int) x_interp;
		int x_der = x_izq + 1;
		int y_sup = (int) y_interp;
		int y_inf = y_sup + 1;
		
		// Calculamos las posiciones a interpolar en un cuadrado de lado = 1
		x_interp = x_interp - x_izq;
		y_interp = y_inf - y_interp;
		
		double temp_interp = 0.0;
		// Interpolamos la temperatura a partir de las parejas (x,y) si todas las temperaturas no son 0
		if ((getScaledData(x_izq, y_inf) != 0.0) && (getScaledData(x_der, y_inf) != 0.0) && 
				(getScaledData(x_izq, y_sup) != 0.0) && (getScaledData(x_der, y_sup) != 0.0)) {
			
			temp_interp = bilinear(0, 1, x_interp, 0, 1, y_interp, 
					getScaledData(x_izq, y_inf), getScaledData(x_der, y_inf), 
					getScaledData(x_izq, y_sup), getScaledData(x_der, y_sup));
		}

		return temp_interp;
	}
	

	private static double bilinear(double x1, double x2, double x, double y1,
			double y2, double y, double q11, double q21, double q12, double q22) {
		double denominator = (x2 - x1) * (y2 - y1);

		double line1A = q11 / denominator;
		double line1B = (x2 - x) * (y2 - y);
		double line1 = line1A * line1B;

		double line2A = q21 / denominator;
		double line2B = (x - x1) * (y2 - y);
		double line2 = line2A * line2B;

		double line3A = q12 / denominator;
		double line3B = (x2 - x) * (y - y1);
		double line3 = line3A * line3B;

		double line4A = q22 / denominator;
		double line4B = (x - x1) * (y - y1);
		double line4 = line4A * line4B;

		return (double) (line1 + line2 + line3 + line4);
	}
	
	public void show(boolean showZeros) {
		if (data != null) {
			for (int i = 0; i < dimX; i++) {
				for (int j = 0; j < dimY; j++) {
					if (showZeros)
						System.out.print(getScaledData(j, i) + " ");
					else if (getScaledData(j, i) != 0) {
						System.out.print(getScaledData(j, i) + " ");
					}
				}
				System.out.println();
			}
		}
		else {
			System.out.println("La variable data no est� inicializada");
		}
	}
}
