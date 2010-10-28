/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etp.modelo.exceptions;

/**
 *
 * @author Fran
 */
public class TooManyDaysRequestedException extends ModelException {

    /**
     * Creates a new instance of <code>TooManyDaysRequestedException</code> with default message.
     */
    public TooManyDaysRequestedException(int maxDays) {
        super("The number of days to use can't be greater than " + Integer.toString(maxDays));
    }


    /**
     * Constructs an instance of <code>TooManyDaysRequestedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TooManyDaysRequestedException(String msg) {
        super(msg);
    }
}
