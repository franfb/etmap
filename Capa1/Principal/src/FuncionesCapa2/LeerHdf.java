/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FuncionesCapa2;

import FuncionesCapa1.CapturaDatos;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Fran
 */
public class LeerHdf {

    private static String SAT_TERRA = "MOD11A1";
    private static String SAT_AQUA = "MYD11A1";
    private static String GRANULO = "h16v06";
    CapturaDatos datos;

    public LeerHdf() {
        String directorio_local_de_hdfs = "d:\\etsii\\pfc\\hdfs\\";
        String ftp_de_hdfs = "e4ftl01u.ecs.nasa.gov";
        datos = new CapturaDatos(directorio_local_de_hdfs, ftp_de_hdfs, 21, "anonymous", "anonymous");
    }

    /**
     * Busca los ficheros HDF correspondientes al intervalo de fechas 
     * solicitado. El intervalo se especifica mediante una fecha de referencia y 
     * los días hacia atrás, sin contar el día de referencia. Por ejemplo, si 
     * especificamos el día 10-04-2010 y 4 días hacia trás, buscará los HDF 
     * desde el día 06 hasta el día 10 de abril de 2010, ambos inclusive.
     * 
     * @param dia Día del mes de la fecha de referencia.
     * @param mes Mes de la fecha referencia.
     * @param ano Año de la fecha referencia.
     * @param diasHaciaAtras Días hacia atrás desde la fecha de referencia que 
     * permiten especificar el intervalo de fechas solicitado.
     * @see #buscarIntervalo(int, int, int, int, int, int) 
     */
    public void buscarIntervalo(int dia, int mes, int ano, int diasHaciaAtras) {
        Calendar orig = Calendar.getInstance();
        orig.set(ano, mes - 1, dia);  // Enero == 0
        orig.add(Calendar.DAY_OF_MONTH, -diasHaciaAtras);

        buscarIntervalo(orig.get(Calendar.DAY_OF_MONTH),
                orig.get(Calendar.MONTH) + 1,
                orig.get(Calendar.YEAR),
                dia, mes, ano);
    }

    /**
     * Busca los ficheros HDF correspondientes al intervalo de fechas
     * solicitado. El intervalo se especifica mediante una fecha
     * inicial y una fecha final. Por ejemplo, si especificamos el día
     * 06-04-2010 como fecha inicial y el día 10-04-2010 como fecha final,
     * se buscarán los HDF desde el día 06 hasta el día 10 de abril de 2010,
     * ambos inclusive.
     *
     * @param diaIni Día del mes de la fecha inicial.
     * @param mesIni Mes de la fecha inicial.
     * @param anoIni Año de la fecha inicial.
     * @param diaFin Día del mes de la fecha final.
     * @param mesFin Mes de la fecha final.
     * @param anoFin Año de la fecha final.
     * @see #buscarIntervalo(int, int, int, int)
     */
    public void buscarIntervalo(int diaIni, int mesIni, int anoIni, int diaFin, int mesFin, int anoFin) {
        Calendar orig = Calendar.getInstance();
        orig.set(anoIni, mesIni - 1, diaIni);  // Enero == 0

        Calendar fin = Calendar.getInstance();
        fin.set(anoFin, mesFin - 1, diaFin);

        SimpleDateFormat FormatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        // Por hacer: guardar los nombres que devuelve la clase datos en un array de string
        // para leerlos luego con los métodos de la clase ModisLoader y AllLstData


        while (orig.compareTo(fin) <= 0) {
            String nombre = datos.obtenerHdfLocal(SAT_AQUA, GRANULO, FormatoFecha.format(orig.getTime()));
            if (nombre == null) {
                nombre = datos.obtenerHdfFTP(SAT_AQUA, GRANULO, FormatoFecha.format(orig.getTime()));
            }
            orig.add(Calendar.DAY_OF_MONTH, 1);
        }
    }


}
