/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etmap.stations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Fran
 */
public class StationModisComp {

    private static final int MAXDAYS = 365;
    private double[] minTemp, maxTemp, dayTemp, nightTemp, et;

    public void readCompFile(String path, String stationName, String year) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File(path + stationName + year + "_hdfComp.dat");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea;
            double tmin, tmax, tday, tnight, evtr;
            int diaJuliano;
            minTemp = new double[MAXDAYS];
            maxTemp = new double[MAXDAYS];
            dayTemp = new double[MAXDAYS];
            nightTemp = new double[MAXDAYS];
            et = new double[MAXDAYS];
            int i = 0;
            while ((linea = br.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(linea);
                diaJuliano = new Integer(tokens.nextToken());

                tmin = new Double(tokens.nextToken());
                tmax = new Double(tokens.nextToken());
                tday = new Double(tokens.nextToken());
                tnight = new Double(tokens.nextToken());
                evtr = new Double(tokens.nextToken());

                // Puede que falten datos entre dos días no sucesivos
                if ((i + 1) != diaJuliano) {
                    for (int j = i; j < diaJuliano; j++) {
                        minTemp[j] = maxTemp[j] = dayTemp[j] = nightTemp[j] = StationConstants.NO_TEMPERATURE;
                        et[j] = StationConstants.NO_EVAPO_TRANSP;
                    }
                    i = diaJuliano - 1;
                }

                minTemp[i] = tmin;
                maxTemp[i] = tmax;
                dayTemp[i] = tday;
                nightTemp[i] = tnight;
                et[i] = evtr;
                i++;
            }
            // Algunas estaciones no tienen datos hasta el final del año
            if (i < MAXDAYS) {
                for (int j = i; j < MAXDAYS; j++) {
                    minTemp[j] = maxTemp[j] = dayTemp[j] = nightTemp[j] = StationConstants.NO_TEMPERATURE;
                    et[j] = StationConstants.NO_EVAPO_TRANSP;
                }
            }
            System.out.println("Fichero " + archivo.getAbsolutePath() + " leído.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isAssigned() {
        return (minTemp != null);
    }

    private double getInterpValue(double[] dataset, int pos, double noValue, boolean interpolateMissingValues) {
        double interpTemp = dataset[pos];
        if (interpolateMissingValues) {
            if (dataset[pos] == noValue) {
                int i = pos, j = pos;

                while ((i >= 0) && (dataset[i] == noValue)) {
                    i--;
                }
                while ((j < MAXDAYS) && (dataset[j] == noValue)) {
                    j++;
                }
                if ((i >= 0) && (j < MAXDAYS)) {
                    interpTemp = dataset[i] + (pos - i) * ((dataset[j] - dataset[i]) / (j - i));
                }
                else if (i < 0) {
                    interpTemp = dataset[j];
                }
                else { // j >= MAXDAYS
                    interpTemp = dataset[i];
                }
            }
        }
        else {
            if (dataset[pos] == noValue)
                interpTemp = 0;
        }
        return interpTemp;
    }

    public void showGraph(boolean showMinTemp, boolean showMaxTemp, boolean showDayTemp, boolean showNightTemp, boolean showEt, boolean interpolateMissingValues) {
        if (isAssigned()) {
            String minTempStr = "Temperatura de estación mínima";
            String maxTempStr = "Temperatura de estación máxima";
            String dayTempStr = "Temperatura de satélite de día";
            String nightTempStr = "Temperatura de satélite de noche";
            String etStr = "Evapotranspiración";

            // Creamos y rellenamos el modelo de datos
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Minimum temperature values
            for (Integer i = 0; i < MAXDAYS; i++) {
                if (showMinTemp) {
                    dataset.setValue(getInterpValue(minTemp, i, StationConstants.NO_TEMPERATURE, interpolateMissingValues), minTempStr, i.toString());
                }
                if (showMaxTemp) {
                    dataset.setValue(getInterpValue(maxTemp, i, StationConstants.NO_TEMPERATURE, interpolateMissingValues), maxTempStr, i.toString());
                }
                if (showDayTemp) {
                    dataset.setValue(getInterpValue(dayTemp, i, StationConstants.NO_TEMPERATURE, interpolateMissingValues), dayTempStr, i.toString());
                }
                if (showNightTemp) {
                    dataset.setValue(getInterpValue(nightTemp, i, StationConstants.NO_TEMPERATURE, interpolateMissingValues), nightTempStr, i.toString());
                }
                if (showEt) {
                    dataset.setValue(getInterpValue(et, i, StationConstants.NO_EVAPO_TRANSP, interpolateMissingValues), etStr, i.toString());
                }
            }

            JFreeChart chart = ChartFactory.createLineChart("Comparación estaciones - satélite", "Día Juliano", "Valor", dataset, PlotOrientation.VERTICAL, true, true, true);

            // Creación del panel con el gráfico
            ChartPanel panel = new ChartPanel(chart);

            JFrame ventana = new JFrame("Gráfico");
            ventana.getContentPane().add(panel);
            ventana.pack();
            ventana.setVisible(true);
            //ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
}
