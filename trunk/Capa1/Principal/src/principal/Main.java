/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package principal;

import FuncionesCapa1.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String directorio_local_de_hdfs = "c:\\proy\\hdfs\\";
        String ftp_de_hdfs = "e4ftl01u.ecs.nasa.gov";

        String satelite_usado = "MOD11A1";
        String granulo_estudiado = "h16v06";
        String fecha = "07/07/2010";

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
