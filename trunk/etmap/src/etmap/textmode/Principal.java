package etmap.textmode;

import etmap.modis.AllLstData;
import etmap.modis.ModisLoader;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Principal {

	/**
	 * @param args
	 */
	public static void main(String[] args){
        try {
            // TODO Auto-generated method stub
            //		AllLstData lstData = new AllLstData();
            //		lstData.writeComparisonFile("D:\\etsii\\pfc\\datos\\Aqua11A1");
            ModisLoader loader = new ModisLoader();
            loader.openFile("D:\\etsii\\pfc\\Datos\\Terra11A1\\MOD11A1.A2009001.h16v06.005.2009010022401.hdf");
            //		loader.printFileStructure("C:\\Documents and Settings\\Fran_Javi\\Escritorio\\PFC\\Datos\\Terra11A1\\MOD11A1.A2009001.h16v06.005.2009010022401.hdf");
            loader.printAttribute(ModisLoader.LST_NIGHT_1KM);
            //		LstData dataDay = new LstData(LstConstants.DIM_X, LstConstants.DIM_Y, LstConstants.SCALE_FACTOR);
            //		loader.readDataset(dataDay, ModisLoader.LST_DAY_1KM);
            //		dataDay.setCoordinates();
            //		dataDay.getInterpolatedTemperature(28.2230644372507, -16.75267301976);
            //		AllStations stations = new AllStations();
            //		stations.readStationsData("C:\\Documents and Settings\\Fran_Javi\\Escritorio\\PFC\\Estaciones\\estacionesagrocabildo.txt");
            //
            //		System.out.println("Coordenada[0,0] = (" + dataDay.getLat()[0] + ", " + dataDay.getLon()[0] + ")");
            //		System.out.println("Coordenada[0,1199] = (" + dataDay.getLat()[1199] + ", " + dataDay.getLon()[1199] + ")");
            //		System.out.println("Coordenada[1199,0] = (" + dataDay.getLat()[1199 * 1200] + ", " + dataDay.getLon()[1199 * 1200] + ")");
            //		System.out.println("Coordenada[1199,1199] = (" + dataDay.getLat()[1199 * 1200 + 1199] + ", " + dataDay.getLon()[1199 * 1200 + 1199] + ")");
            //		dataDay.show(false);
            //
            //		LstData dataNight = new LstData(LstConstants.DIM_X, LstConstants.DIM_Y, LstConstants.SCALE_FACTOR);
            //		loader.readDataset(dataNight, ModisLoader.LST_NIGHT_1KM);
            //		dataNight.setCoordinates();
            //		dataNight.getInterpolatedTemperature(28.2230644372507, -16.75267301976);
            //		System.out.println("Coordenada[0,0] = (" + dataNight.getLat()[0] + ", " + dataNight.getLon()[0] + ")");
            //		System.out.println("Coordenada[0,1199] = (" + dataNight.getLat()[1199] + ", " + dataNight.getLon()[1199] + ")");
            //		System.out.println("Coordenada[1199,0] = (" + dataNight.getLat()[1199 * 1200] + ", " + dataNight.getLon()[1199 * 1200] + ")");
            //		System.out.println("Coordenada[1199,1199] = (" + dataNight.getLat()[1199 * 1200 + 1199] + ", " + dataNight.getLon()[1199 * 1200 + 1199] + ")");
            //		dataNight.show(false);
            //		loader.closeFile();
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
