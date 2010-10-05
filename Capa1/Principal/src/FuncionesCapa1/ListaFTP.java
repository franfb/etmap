/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FuncionesCapa1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Javier Perera
 */
public class ListaFTP {
    public FTPClient ftp;
    public boolean conectado;
    public String rutaDirBase;
    public ArrayList listaFechasFTP;
    public ArrayList listaFechasReal;

    public ListaFTP() {
        conectado = false;
        rutaDirBase = "";
        listaFechasFTP = null;
        listaFechasReal = null;
    }

    public boolean Conectar(String direc_ftp) {
        ftp = new FTPClient();
        try {
            ftp.connect(direc_ftp);
            conectado = true;
            ftp.enterLocalPassiveMode();
            return true;
        } catch (IOException ex) {
            System.err.println("Error en Conectar. No se pudo hacer ftp.connect()");
            return false;
        }
    }

    public boolean Conectar(String direc_ftp, int puerto) {
        ftp = new FTPClient();
        try {
            ftp.connect(direc_ftp, puerto);
            conectado = true;
            ftp.enterLocalPassiveMode();
            return true;
        } catch (IOException ex) {
            System.err.println("Error en Conectar. No se pudo hacer ftp.connect()");
            return false;
        }
    }

    public boolean Conectar(String direc_ftp, int puerto, String usuario, String pass) {
        ftp = new FTPClient();
        try {
            ftp.connect(direc_ftp, puerto);
            ftp.login(usuario, pass);
            conectado = true;
            ftp.enterLocalPassiveMode();
            return true;
        } catch (IOException ex) {
            System.err.println("Error en Conectar. No se pudo hacer ftp.connect()");
            return false;
        }
    }

    public boolean desconectar() {
        try {
            ftp.disconnect();
            conectado = true;
            rutaDirBase = "";
            return true;
        } catch (IOException ex) {
            System.err.println("Error en desconectar. No se pudo hacer ftp.disconnect()");
            try {
                ftp.logout();
                ftp.disconnect();
                conectado = true;
                return true;
            } catch (IOException ex2){
                System.err.println("Error en desconectar. No se pudo hacer ftp.logout()");
                conectado = false;
                return false;
            }
        }
    }

    public boolean irDirectorio(String dir) {
        try {
            ftp.changeWorkingDirectory(dir);
        } catch (IOException ex) {
            System.err.println("Error en irDirectorio. No se pudo hacer ftp.changeWorkingDirectory()");
        }

        if (ftp.getReplyCode() == 250) {
            return true;
        } else {
            return false;
        }
    }
    
    public String directorioActual() {
        try {       
            return ftp.printWorkingDirectory();
        } catch (IOException ex) {
            System.err.println("Error en directorioActual. No se pudo hacer ftp.printWorkingDirectory()");
            return null;
        }
    }

    private String[] nombreArchivos() {
        try {
            return ftp.listNames();
        } catch (IOException ex) {
            System.err.println("Error en nombreArchivos. No se pudo hacer ftp.listNames()");
            return null;
        }
    }

    public boolean subirDirectorio() {
        try {
            String aux = directorioActual();
            ftp.cdup();
            if (aux.equals(directorioActual())) {
                return false;
            } else {
                return true;
            }
        } catch(IOException ex) {
            System.err.println("Error en subirDirectorio. No se pudo hacer ftp.cdup()");
            return false;
        }
    }

    public ArrayList listaDirectorios() {
        FTPFile[] lista = null;
        try {
            lista = ftp.listFiles();
        } catch (IOException ex) {
            System.err.println("Error en listaDirectorio. No se pudo hacer ftp.listFiles()");
        }

        ArrayList directorios = new ArrayList();

        for (int i = 0; i < lista.length; i++) {
            if (lista[i].isDirectory()) {
                directorios.add(lista[i]);
            }
        }
        return directorios;
    }

    public ArrayList listaArchivos() {
        FTPFile[] lista = null;
        try {
            lista = ftp.listFiles();
        } catch (IOException ex) {
            System.err.println("Error en listaArchivos. No se pudo hacer ftp.listFiles()");
        }

        ArrayList directorios = new ArrayList();

        for (int i = 0; i < lista.length; i++) {
            if (lista[i].isFile()) {
                directorios.add(lista[i]);
            }
        }
        return directorios;
    }

    private void crearListaFechas() {
        ArrayList directorios = listaDirectorios();
        rutaDirBase = directorioActual();
        listaFechasFTP = new ArrayList();
        listaFechasReal = new ArrayList();

        for (int i = 0; i < directorios.toArray().length; i++) {
            FTPFile aux = (FTPFile) directorios.toArray()[i];
            listaFechasFTP.add(aux.getName());
            String[] fechaOK = aux.getName().split("\\.");
            listaFechasReal.add(fechaOK[2] + "/" + fechaOK[1] + "/" + fechaOK[0]);
        }
    }

    public String ExisteHDF(String dirBase, String satelite, String fecha, String granulo) {
        // ¡IMPORTANTE! -> dirBase no debe tener la última barra de directorio, es decir:
        //                 pondremos "/dir1/dir2" en vez de "/dir1/dir2/"
        if ((conectado) && (irDirectorio(dirBase))) {
            if ((listaFechasReal == null) || (!rutaDirBase.equals(dirBase))) {
                crearListaFechas();
                // System.out.println("Lista de fechas disponibles actualizada");
            }
            Calendar fechaJul = Calendar.getInstance();
            String[] parseado = fecha.split("/");
            fechaJul.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parseado[0]));
            fechaJul.set(Calendar.MONTH, Integer.parseInt(parseado[1]) - 1);
            fechaJul.set(Calendar.YEAR, Integer.parseInt(parseado[2]));

            for (int i = 0; i < listaFechasReal.toArray().length; i++) {
                if (listaFechasReal.toArray()[i].toString().equals(fecha)) {
                    irDirectorio(listaFechasFTP.toArray()[i].toString());
                    ArrayList files = listaArchivos();
                    for (int j = 0; j < files.toArray().length; j++) {
                        if (files.toArray()[j].toString().contains(satelite + ".A" + fechaJul.get(Calendar.YEAR)
                            + String.format("%03d", fechaJul.get(Calendar.DAY_OF_YEAR)) + "." + granulo)) {
                            FTPFile encontrado = (FTPFile) files.toArray()[j];
                            return encontrado.getName();
                        }
                    }
                }
            }
        } else {
            System.err.println("Error en ExisteHDF. Ruta de directorio base inválida.");
        }
        return null;
    }

    public boolean descargarFichero(String Mode, String dirBaseFTP, String fecha, String nombreArchivo, String dirLocal) {
        if ((conectado) && irDirectorio(dirBaseFTP)) {
            if (Mode.equals("mBinary")) {
                try {
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
                } catch (IOException ex) {
                    System.err.println("Error en listaArchivos. No se pudo hacer ftp.listFiles()");
                    return false;
                }
            } else {
                if (Mode.equals("mText")) {
                    try {
                        ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    } catch (IOException ex) {
                        System.err.println("Error en listaArchivos. No se pudo hacer ftp.listFiles()");
                        return false;
                    }
                } else {
                    System.err.println("Error en listaArchivos. Modo de transferencia desconocido");
                    return false;
                }
            }

            for (int i = 0; i < listaFechasReal.toArray().length; i++) {
                if (listaFechasReal.toArray()[i].toString().equals(fecha)) {
                    irDirectorio(listaFechasFTP.toArray()[i].toString());
                }
            }

            try {
                FileOutputStream fos = null;
                String filename = dirLocal + nombreArchivo;
                fos = new FileOutputStream(filename);
                try {
                    ftp.retrieveFile(directorioActual() + "/" + nombreArchivo, fos);
                } catch (IOException ex) {
                    System.err.println("Error en descargarFichero. No se pudo crear hacer ftp.retrieveFile()");
                    return false;
                }
                try {
                    fos.close();
                } catch (IOException ex) {
                    System.err.println("Error en descargarFichero. No se pudo cerrar el FileOutputStream");
                    return false;
                }
                return true;
            } catch (FileNotFoundException ex) {
                System.err.println("Error en descargarFichero. No se pudo crear un FileOutputStream");
                return false;
            }
        }
        return false;
    }
}
