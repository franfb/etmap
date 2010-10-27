/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.modelo.exceptions;

/**
 *
 * @author Fran
 */
public class DataNotLoadedException extends LinearModelException {

    /**
     * Creates a new instance of <code>DataNotLoadedException</code> with the default detail message
     */
    public DataNotLoadedException() {
        super("There isn't any data loaded. You should call method cargarDia() first.");
    }


    /**
     * Constructs an instance of <code>DataNotLoadedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DataNotLoadedException(String msg) {
        super(msg);
    }
}
