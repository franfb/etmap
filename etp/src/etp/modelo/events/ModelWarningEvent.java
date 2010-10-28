package etp.modelo.events;

import java.util.EventObject;

/**
 * Esta clase encapsula la información necesaria al producirse un evento <i>ModelWarning</i>.
 *
 */
public class ModelWarningEvent extends EventObject {
    public static final int MISSING_AQUA_FILES = 100;
    public static final int MISSING_TERRA_FILES = 101;
    private int type;

    public ModelWarningEvent(Object source, int type) {
        super(source);
        this.type = type;
    }

    /**
     * Obtiene el tipo del evento ModelWarning
     *
     * @return tipo del evento
     */
    public int getType() {
        return this.type;
    }
}
