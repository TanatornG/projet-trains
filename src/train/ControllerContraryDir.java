package train;

public class ControllerContraryDir {

    private int nbrTrainsLR;
    private int nbrTrainsRL;

    ControllerContraryDir() {
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
