/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.accesodatos;

import java.util.ArrayList;

/**
 *
 * @author Javier Perera
 */
public class CapturaDatos {
    ListaFicheros Ficheros;
    ListaFTP Ftp;

    public CapturaDatos(String directorioHdfLocal, String directorioHdfFTP, int puerto, String login, String pass) {
        // ¡IMPORTANTE! Hay que pasar los ficheros con dobles barras invertidas
        // para que Java no las reconozca como un símbolo especial
        Ficheros = new ListaFicheros(directorioHdfLocal);
        Ftp = new ListaFTP();
        Ftp.Conectar("e4ftl01u.ecs.nasa.gov", 21, "anonymous", "anonymous");
    }

    public String obtenerHdfLocal(String satelite, String granulo, String fecha) {
        EstructuraFichero fich = Ficheros.ObtenerHDF(satelite, granulo, fecha);
        if (fich != null) {
            System.out.println("El HDF solicitado es " + fich.getNombreFichero() + " ya se encentra en el repositorio local");
            return fich.getNombreFichero();
        } else {
            System.out.println("El HDF solicitado no se encentra en el repositorio local");
            return null;
        }
    }

    public String obtenerHdfFTP(String satelite, String granulo, String fecha) {
        String dirBase;
        if (satelite.equals("MOD11A1")) {
            dirBase = "/MOLT/MOD11A1.005";
        } else {
            if (satelite.equals("MYD11A1")) {
                dirBase = "/MOLA/MYD11A1.005";
            } else {
                System.err.println("Error en ObtenerHdf. Satelite incorrecto");
                return null;
            }
        }

        String nombreHdf;
        nombreHdf = Ftp.ExisteHDF(dirBase, satelite, fecha, granulo);
        if (nombreHdf == null) {
            return null;
        } else {
            if (Ftp.descargarFichero("mBinary", dirBase, fecha, nombreHdf, Ficheros.getRuta_directorio())) {
                System.out.println("El HDF " + nombreHdf + " ha sido descargado del FTP");
                Ficheros.Actualizar();
                return nombreHdf;
            } else {
                System.out.println("El HDF solicitado existe pero no se ha podido descargar del FTP");
                return null;
            }
        }
    }

    public ArrayList obtenerListaHDF(String diaIni, String DiaFin) {
        ArrayList lista = new ArrayList();
        return lista;
    }

    

}
