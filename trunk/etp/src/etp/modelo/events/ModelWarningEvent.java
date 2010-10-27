package etp.modelo.events;

import java.util.EventObject;

/**
 * Esta clase encapsula la información necesaria al producirse un evento <i>ModelWarning</i>.
 *
 */
public class ModelWarningEvent extends EventObject {
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
