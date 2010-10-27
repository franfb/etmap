/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.modelo.exceptions;

/**
 *
 * @author Fran
 */
public class ModelException extends Exception {

    /**
     * Creates a new instance of <code>ModelException</code> without detail message.
     */
    public ModelException() {
    }


    /**
     * Constructs an instance of <code>ModelException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ModelException(String msg) {
        super(msg);
    }
}
