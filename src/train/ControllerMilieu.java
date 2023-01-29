package train;

public class ControllerMilieu {

    private final int nbrQuaisGareM;
    private int nbrTrainsToM;
    private boolean debugCtrlMilieu;

    ControllerMilieu(int nbrQuaisGareM) {
        this.nbrTrainsToM = 0;
        this.nbrQuaisGareM = nbrQuaisGareM;
        this.debugCtrlMilieu = false;
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void newTrainToM() {
        while (!canNewTrainToM()) {
            try {
                if (this.debugCtrlMilieu) {
                    System.out.println("Un train attend ctrlM newTrainToM et nbrTrainsToM vaut " + this.nbrTrainsToM);
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsToM += 1;
        if (this.debugCtrlMilieu) {
            System.out.println("ctrlM newTrainToM et nbrTrainsToM vaut " + this.nbrTrainsToM);
        }
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void arrivedTrainFromM() {
        while (!(this.nbrTrainsToM > 0)) {
            try {
                if (this.debugCtrlMilieu) {
                    System.out.println(
                            "Un train attend ctrlM arrivedTrainFromM et nbrTrainsToM vaut " + this.nbrTrainsToM);
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsToM -= 1;
        if (this.debugCtrlMilieu) {
            System.out.println("ctrlM arrivedTrainFromM et nbrTrainsToM vaut " + this.nbrTrainsToM);
        }
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
