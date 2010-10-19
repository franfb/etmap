/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FuncionesCapa2;

/**
 *
 * @author Fran
 */
public enum HorasSat {

    AQUA_02H() {

        @Override
        public LstData[] getLst(CargadorHdf cargador) {
            return cargador.getLst02h();
        }
    },
    TERRA_11H() {

        @Override
        public LstData[] getLst(CargadorHdf cargador) {
            return cargador.getLst11h();
        }
    },
    AQUA_14H() {

        @Override
        public LstData[] getLst(CargadorHdf cargador) {
            return cargador.getLst14h();
        }
    },
    TERRA_23H() {

        @Override
        public LstData[] getLst(CargadorHdf cargador) {
            return cargador.getLst23h();
        }
    };

    public abstract LstData[] getLst(CargadorHdf cargador);

    public static HorasSat getHoraSat(int i) {
        return values()[i];
    }
}
