/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.modelo.exceptions;

/**
 *
 * @author Fran
 */
public class LinearModelException extends ModelException {

    /**
     * Creates a new instance of <code>LinearModelException</code> without detail message.
     */
    public LinearModelException() {
    }


    /**
     * Constructs an instance of <code>LinearModelException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public LinearModelException(String msg) {
        super(msg);
    }
}
