/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.modelo.exceptions;

/**
 *
 * @author Fran
 */
public class NoHdfFilesException extends ModelException {

    /**
     * Creates a new instance of <code>NoHdfFilesException</code> without detail message.
     */
    public NoHdfFilesException() {
    }


    /**
     * Constructs an instance of <code>NoHdfFilesException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NoHdfFilesException(String msg) {
        super(msg);
    }
}
