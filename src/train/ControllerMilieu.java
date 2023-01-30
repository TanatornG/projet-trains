package train;

public class ControllerMilieu {

    private final int nbrQuaisGareM;
    private int nbrTrainsToM;

    ControllerMilieu(int nbrQuaisGareM) {
        this.nbrTrainsToM = 0;
        this.nbrQuaisGareM = nbrQuaisGareM;
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void newTrainToM() {
        while (!canNewTrainToM()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsToM += 1;
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void arrivedTrainFromM() {
        while (!(this.nbrTrainsToM > 0)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsToM -= 1;
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    private boolean canNewTrainToM() {
        int previousNbrTrainsToM = this.nbrTrainsToM;
        this.nbrTrainsToM += 1;
        boolean result = invariant();
        this.nbrTrainsToM = previousNbrTrainsToM;
        return result;
    }

    /**
     * @author Nicolas Sempéré
     */
    private boolean invariant() {
        return (this.nbrTrainsToM <= this.nbrQuaisGareM);
    }
}
