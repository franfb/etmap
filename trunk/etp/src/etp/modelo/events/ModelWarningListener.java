package etp.modelo.events;

import java.util.EventListener;

/**
 * Interface que se tiene que implementar para escuchar eventos del tipo
 * <i>ModelWarning</i>.
 */
public interface ModelWarningListener extends EventListener {

    /**
     * Este método se ejecuta cada vez que se produce un warning en alguna de
     * las clases del paquete etp.modelo
     */
    public void onModelWarning(ModelWarningEvent e);
}
