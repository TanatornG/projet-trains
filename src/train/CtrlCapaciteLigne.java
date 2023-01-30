package train;

/**
 * Contrôleur empêchant trop de trains de s'engager sur la ligne.
 * Soit QM le nombre de quais de la gare M
 * Le contrôleur assure qu'il y a au maximum QM trains en déplacement sur la
 * ligne.
 *
 * Utilisé quand la ligne est de la forme :
 * Gare A – Section 2 – Section 3 – Gare M – Section 5 – Section 6 – Gare B.
 *
 * Cf --> CONTROLLER_MILIEU en FSP.
 *
 * @author Nicolas Sempéré
 */
public class CtrlCapaciteLigne {

    private final int nbrQuaisGareM;
    private int nbrTrainsOnLine;

    CtrlCapaciteLigne(int nbrQuaisGareM) {
        this.nbrTrainsOnLine = 0;
        this.nbrQuaisGareM = nbrQuaisGareM;
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void newTrainOnLine() {
        while (!canNewTrainOnLine()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsOnLine += 1;
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void arrivedTrainFromLine() {
        while (!(this.nbrTrainsOnLine > 0)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsOnLine -= 1;
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    private boolean canNewTrainOnLine() {
        int previousNbrTrainsToM = this.nbrTrainsOnLine;
        this.nbrTrainsOnLine += 1;
        boolean result = invariant();
        this.nbrTrainsOnLine = previousNbrTrainsToM;
        return result;
    }

    /**
     * @author Nicolas Sempéré
     */
    private boolean invariant() {
        return (this.nbrTrainsOnLine <= this.nbrQuaisGareM);
    }
}
