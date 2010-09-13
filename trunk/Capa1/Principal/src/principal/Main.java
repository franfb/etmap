/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package principal;

import FuncionesCapa1.*;
import FuncionesCapa2.LeerHdf;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;


public class Main {
    /**
     * @param args the command line arguments
     *
     *
     * Para usar un rango de fechas:
     * Calendar fecha = Calendar.getInstance();
           fecha.set(Calendar.DAY_OF_MONTH, dia_del_mes);
           fecha.set(Calendar.MONTH, mes);
           fecha.set(Calendar.YEAR, año);
     *
     * fecha.set(Calendar.DAY_OF_YEAR, fecha.get(Calendar.DAY_OF_YEAR + 1));
     *
     * Calendar origDay = Calendar.getInstance();
System.out.println ("Original Date: " + origDay.getTime());

Calendar prevDay = (Calendar) origDay.clone();
prevDay.add (Calendar.DAY_OF_YEAR, -1);
System.out.println ("Previous Day: " + prevDay.getTime());

Calendar nextDay = (Calendar) origDay.clone();
nextDay.add (Calendar.DAY_OF_YEAR, 1);
System.out.println ("Next Day: " + nextDay.getTime());
}
     *
     */
    public static void main(String[] args) {
        String directorio_local_de_hdfs = "d:\\etsii\\pfc\\hdfs\\";
        String ftp_de_hdfs = "e4ftl01u.ecs.nasa.gov";

        String satelite_usado = "MOD11A1";
        String granulo_estudiado = "h16v06";
        String fecha = "07/07/2010";
//        Calendar orig = Calendar.getInstance();
//        LeerHdf leer = new LeerHdf();
//        leer.

        CapturaDatos Datos = new CapturaDatos(directorio_local_de_hdfs, ftp_de_hdfs, 21, "anonymous", "anonymous");
        String nombre = Datos.obtenerHdfLocal(satelite_usado, granulo_estudiado, fecha);

        if (nombre == null) {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);

            try {
                System.out.print("Desea descargar el fichero del FTP (si / no)? ");
                String respuesta = br.readLine();
                
                if (respuesta.equals("si")) {
                    System.out.println("Descargando... (esto puede tardar unos segundos, paciencia)");
                    nombre = Datos.obtenerHdfFTP(satelite_usado, granulo_estudiado, fecha);
                }
            } catch (IOException ex) {
                System.out.println("Error inesperado de entrada: " + ex);
                return;
            }
        }
    }
}
