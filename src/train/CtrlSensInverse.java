package train;

/**
 * Contrôleur empêchant deux trains de s'engager en sens inverse entre deux
 * gares adjacentes.
 *
 * Utilisé quelque soit la forme de la ligne (c'est à dire pour tous les
 * exercices) :
 * Ex 2 et 3 : Gare A – Section 2 – Section 3 – Gare B. (A et B sont adjacentes)
 * Ex 4 : GareA – Section 2 – Section 3 – GareM – Section 5 – Section 6 – GareB
 * (A et M sont adjacentes, M et B sont adjacentes).
 *
 * Cf --> CONTROLLER_GARES en FSP.
 *
 * @author Nicolas Sempéré
 */
public class CtrlSensInverse {

    private int nbrTrainsLR;
    private int nbrTrainsRL;

    CtrlSensInverse() {
        this.nbrTrainsLR = 0;
        this.nbrTrainsRL = 0;
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void newTrainLR() {
        while (!canNewTrainLR()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsLR += 1;
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void newTrainRL() {
        while (!canNewTrainRL()) {
            try {

                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsRL += 1;
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void arrivedTrainLR() {
        while (!(this.nbrTrainsLR > 0)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsLR -= 1;
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void arrivedTrainRL() {
        while (!(this.nbrTrainsRL > 0)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsRL -= 1;
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    private boolean canNewTrainLR() {
        int previousNbrTrainsLR = this.nbrTrainsLR;
        this.nbrTrainsLR += 1;
        boolean result = invariant();
        this.nbrTrainsLR = previousNbrTrainsLR;
        return result;
    }

    /**
     * @author Nicolas Sempéré
     */
    private boolean canNewTrainRL() {
        int previousNbrTrainsRL = this.nbrTrainsRL;
        this.nbrTrainsRL += 1;
        boolean result = invariant();
        this.nbrTrainsRL = previousNbrTrainsRL;
        return result;
    }

    /**
     * @author Nicolas Sempéré
     */
    private boolean invariant() {
        if (this.nbrTrainsLR > 0) {
            return (this.nbrTrainsRL == 0);
        } else if (this.nbrTrainsRL > 0) {
            return (this.nbrTrainsLR == 0);
        }
        // S'il n'y a aucun train déjà engagé sur la ligne, l'invariant est vrai.
        return true;
    }
}
