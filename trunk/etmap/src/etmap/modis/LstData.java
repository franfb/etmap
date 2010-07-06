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
//		data = new short[dimX * dimY];
//		lat = new double[dimX * dimY];
//		lon = new double[dimX * dimY];
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
//		for (int y = 0; y < dimX; y++)
//			for (int x = 0; x < dimY; x++) {
//				this.data[y * dimY + x] = data[y * dimY + x] * scaleFactor;
//			}
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
		
//		System.out.println("Y_interp = " + y_interp);
//		System.out.println("X_interp = " + x_interp);
		
		int x_izq = (int) x_interp;
		int x_der = x_izq + 1;
		int y_sup = (int) y_interp;
		int y_inf = y_sup + 1;
		
//		System.out.println("x_izq = " + x_izq);
//		System.out.println("x_der = " + x_der);
//		System.out.println("y_inf = " + y_inf);
//		System.out.println("y_sup = " + y_sup);
		
		// Calculamos las posiciones a interpolar en un cuadrado de lado = 1
		x_interp = x_interp - x_izq;
		y_interp = y_inf - y_interp;
		
//		System.out.println("Y_interp = " + y_interp);
//		System.out.println("X_interp = " + x_interp);
		
		double temp_interp = 0.0;
		// Interpolamos la temperatura a partir de las parejas (x,y) si todas las temperaturas no son 0
		if ((getScaledData(x_izq, y_inf) != 0.0) && (getScaledData(x_der, y_inf) != 0.0) && 
				(getScaledData(x_izq, y_sup) != 0.0) && (getScaledData(x_der, y_sup) != 0.0)) {
			
			temp_interp = bilinear(0, 1, x_interp, 0, 1, y_interp, 
					getScaledData(x_izq, y_inf), getScaledData(x_der, y_inf), 
					getScaledData(x_izq, y_sup), getScaledData(x_der, y_sup));

//			System.out.println("Punto a interpolar:");
//			System.out.println("X = " + x_interp);
//			System.out.println("Y = " + y_interp);
//			System.out.println("x_izq = " + x_izq);
//			System.out.println("x_der = " + x_der);
//			System.out.println("y_inf = " + y_inf);
//			System.out.println("y_sup = " + y_sup);
//
//			System.out.println("Latitudes:");
//			System.out.println("lat1 = " + getLatLngByPos(x_izq, y_sup)[0]);
//			System.out.println("lat = " + lat);
//			System.out.println("lat2 = " + getLatLngByPos(x_izq, y_inf)[0]);
//			
//			System.out.println("Longitudes:");
//			System.out.println("lon1 = " + getLatLngByPos(x_der, y_sup)[1]);
//			System.out.println("lon = " + lon);
//			System.out.println("lon2 = " + getLatLngByPos(x_izq, y_sup)[1]);
//			System.out.println("Temperatura y_sup,x_der = "
//					+ (getScaledData(x_der, y_sup) - 273.15));
//			System.out.println("Temperatura y_sup,x_izq = "
//					+ (getScaledData(x_izq, y_sup) - 273.15));
//			System.out.println("Temperatura y_inf,x_der = "
//					+ (getScaledData(x_der, y_inf) - 273.15));
//			System.out.println("Temperatura y_inf,x_izq = "
//					+ (getScaledData(x_izq, y_inf) - 273.15));
//			System.out.println("Temperatura interpolada = "
//					+ (temp_interp - 273.15));
		}
		return temp_interp;
	}
	
//	public double getInterpolatedTemperature(double lat, double lon) {
//		// Buscamos la latitud m�s pr�xima a la solicitada
//		int y_sup, y_inf;
//		int y = 0;
//		while ((y < dimY) && (lat <= this.lat[y * dimY])) {
//			y++;
//		}
//		y_sup = y - 1;
//		y_inf = y;
//
//		System.out.println("Latitudes:");
//		System.out.println("lat1 = " + this.lat[y_sup * dimY]);
//		System.out.println("lat = " + lat);
//		System.out.println("lat2 = " + this.lat[y_inf * dimY]);
//		
//		// Buscamos la longitud m�s pr�xima a la solicitada
//		int x_der, x_izq;
//		int x = dimX - 1;
//		while ((x >= 0) && (lon <= this.lon[y_sup * dimY + x])) {
//			x--;
//		}
//		x_der = x + 1;
//		x_izq = x;
//		
//		double temp_interp = 0.0;
//		// Interpolamos la temperatura a partir de las parejas (latX,lonX) si todas las temperaturas no son 0
//		if ((data[y_inf * dimY + x_izq] != 0.0) && (data[y_inf * dimY + x_der] != 0.0) && 
//				(data[y_sup * dimY + x_izq] != 0.0) && (data[y_sup * dimY + x_der] != 0.0)) {
//			// Calculamos las coordenadas cartesianas del punto que necesitamos
//			double y_interp = (lat - this.lat[y_inf * dimY])
//					/ (this.lat[y_sup * dimY] - this.lat[y_inf * dimY]);
//			// lo = x * delta * (vrlon - vllon) + vllon;
//			// lon[y * dimY + x] = (lo / (r * Math.cos(la / r))) * (180.0 / pi);
//			double lo = (lon * (Math.PI / 180.0))
//					* (LstConstants.EARTH_RADIUS * Math.cos(lat
//							* (Math.PI / 180.0)));
//			double x_interp = (lo - LstConstants.VLLON)
//					/ (LstConstants.DELTA * (LstConstants.VRLON - LstConstants.VLLON))
//					- x_izq;
//			temp_interp = bilinear(0, 1, x_interp, 0, 1, y_interp, 
//					data[y_inf * dimY + x_izq], data[y_inf * dimY + x_der], 
//					data[y_sup * dimY + x_izq], data[y_sup * dimY + x_der]);
//
//			System.out.println("Punto a interpolar:");
//			System.out.println("X = " + x_interp);
//			System.out.println("Y = " + y_interp);
//			System.out.println("x_izq = " + x_izq);
//			System.out.println("x_der = " + x_der);
//			System.out.println("y_inf = " + y_inf);
//			System.out.println("y_sup = " + y_sup);
//
//			System.out.println("Longitudes:");
//			System.out.println("lon1 = " + this.lon[y_sup * dimY + x_der]);
//			System.out.println("lon = " + lon);
//			System.out.println("lon2 = " + this.lon[y_sup * dimY + x_izq]);
//			System.out.println("Temperatura y_sup,x_der = "
//					+ (data[y_sup * dimY + x_der] - 273.15));
//			System.out.println("Temperatura y_sup,x_izq = "
//					+ (data[y_sup * dimY + x_izq] - 273.15));
//			System.out.println("Temperatura y_inf,x_der = "
//					+ (data[y_inf * dimY + x_der] - 273.15));
//			System.out.println("Temperatura y_inf,x_izq = "
//					+ (data[y_inf * dimY + x_izq] - 273.15));
//			System.out.println("Temperatura interpolada = "
//					+ (temp_interp - 273.15));
//		}
//		return temp_interp;
//	}

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
