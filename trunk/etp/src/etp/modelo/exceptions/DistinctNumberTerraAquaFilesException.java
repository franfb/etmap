/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.modelo.exceptions;

/**
 *
 * @author Fran
 */
public class DistinctNumberTerraAquaFilesException extends ModelException {

    /**
     * Creates a new instance of <code>DistinctNumberTerraAquaFilesException</code> with default message.
     */
    public DistinctNumberTerraAquaFilesException() {
        super("The number of Aqua and Terra files found are distinct.");
    }


    /**
     * Constructs an instance of <code>DistinctNumberTerraAquaFilesException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DistinctNumberTerraAquaFilesException(String msg) {
        super(msg);
    }
}
