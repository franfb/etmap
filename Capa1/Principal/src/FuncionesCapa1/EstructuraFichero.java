/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FuncionesCapa1;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author Javier Perera
 */
public class EstructuraFichero {
    // Los Substrings por si los utilizamos en un futuro
    private String SubString0;
    private String SubString1;
    private String SubString2;
    private String SubString3;
    private String SubString4;
    private String SubString5;
    private String NombreFichero;
    private String Sat;
    private int Ano;
    private int DiaJul;
    private String Granulo;
    private Calendar Fecha;
    private SimpleDateFormat FormatoFecha;
    private String FechaToString;
    private int Dia;
    private int Mes;

    public EstructuraFichero(String fichero) {
        NombreFichero = fichero;

        String[] cadena = fichero.split("\\.");

        SubString0 = cadena[0];
        SubString1 = cadena[1];
        SubString2 = cadena[2];
        SubString3 = cadena[3];
        SubString4 = cadena[4];
        SubString5 = cadena[5];

        Sat = SubString0;
        Ano = Integer.parseInt(SubString1.substring(1, 5));
        DiaJul = Integer.parseInt(SubString1.substring(5,8));
        Granulo = SubString2;

        Fecha = Calendar.getInstance();
        Fecha.set(Calendar.YEAR, this.Ano);
        Fecha.set(Calendar.DAY_OF_YEAR, this.DiaJul);

        Dia = Fecha.get(Calendar.DAY_OF_MONTH);
        Mes = Fecha.get(Calendar.MONTH) + 1; // representa los meses de 1 a 11
        FormatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        FechaToString = FormatoFecha.format(Fecha.getTime());
    }

    public String getFechaToString() {
        return FechaToString;
    }

    public SimpleDateFormat getFormatoFecha() {
        return FormatoFecha;
    }

    public Calendar getFecha() {
        return Fecha;
    }

    public int getAno() {
        return Ano;
    }

    public int getDiaJul() {
        return DiaJul;
    }

    public String getGranulo() {
        return Granulo;
    }

    public String getNombreFichero() {
        return NombreFichero;
    }

    public String getSat() {
        return Sat;
    }

    public int getDia() {
        return Dia;
    }

    public int getMes() {
        return Mes;
    }
}
