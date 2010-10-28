package etp.modelo.exceptions;

/**
 *
 */
public class NoFilesLoadedException extends ModelException {

    /**
     * Creates a new instance of <code>NoFilesLoadedException</code> with default message.
     */
    public NoFilesLoadedException() {
        super("An error occurred while trying to load files to memory.");
    }


    /**
     * Constructs an instance of <code>NoFilesLoadedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NoFilesLoadedException(String msg) {
        super(msg);
    }
}
