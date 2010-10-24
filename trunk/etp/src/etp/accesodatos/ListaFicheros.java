/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.accesodatos;

import java.io.*;
import java.util.*;

/**
 *
 * @author Javier Perera
 */
public class ListaFicheros {
    private String ruta_directorio;
    private ArrayList ficheros;
    private int numero_ficheros;

    public ListaFicheros(String ruta_directorio) {
        this.ruta_directorio = ruta_directorio;
        Actualizar();
    }

    public ArrayList getNombres_ficheros() {
        return ficheros;
    }

    public String getRuta_directorio() {
        return ruta_directorio;
    }

    public int getNumero_ficheros() {
        return numero_ficheros;
    }

    public ArrayList stringToArrayListOfHDFs(String[] lista) {
        ArrayList lista2 = new ArrayList();
        for (int i = 0; i < lista.length; i++) {
            if ((lista[i].contains(".hdf")) && ((lista[i].contains("MOD11A1")) || (lista[i].contains("MYD11A1")))) {
                lista2.add(new EstructuraFichero(lista[i]));
            }
        }
        return lista2;
    }

    public boolean Actualizar() {
        File dir = new File(ruta_directorio);
        String[] listaAuxiliar = dir.list();
        ArrayList listaFicheros = stringToArrayListOfHDFs(listaAuxiliar);
        if (listaFicheros != ficheros) {
            ficheros = listaFicheros;
            numero_ficheros = ficheros.toArray().length;
        }
        return false;
    }

    public EstructuraFichero ObtenerHDF(String satelite, String granulo, String fecha) {
        for (int i = 0; i < numero_ficheros; i++) {
            EstructuraFichero fichero = (EstructuraFichero) ficheros.toArray()[i];
            if (fichero.getSat().equals(satelite) && fichero.getGranulo().equals(granulo) && fichero.getFechaToString().equals(fecha)) {
                return fichero;
            }
        }
        return null;
    }

    // Este método no se puede usar todavía, hasta que no sea implementado uno similar
    // haciendo uso de la fecha en Calendar en la clase ListaFTP
    private EstructuraFichero ObtenerHDF(String satelite, String granulo, Calendar fecha) {
         for (int i = 0; i < numero_ficheros; i++) {
            EstructuraFichero fichero = (EstructuraFichero) ficheros.toArray()[i];
            if (fichero.getSat().equals(satelite) && fichero.getGranulo().equals(granulo) && 
               (fichero.getDia() == fecha.get(Calendar.DAY_OF_MONTH)) && (fichero.getMes() == fecha.get(Calendar.MONTH)) &&
               (fichero.getAno() == fecha.get(Calendar.YEAR)) ) {
                return fichero;
            }
        }
        return null;
    }
}
