package train;

public class ControllerAB {

    private int nbrTrainsLR;
    private int nbrTrainsRL;
    private boolean debugCtrlAB;

    ControllerAB() {
        this.nbrTrainsLR = 0;
        this.nbrTrainsRL = 0;
        this.debugCtrlAB = false;
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void newTrainLR() {
        while (!canNewTrainLR()) {
            try {
                if (this.debugCtrlAB) {
                    System.out.println("Un train attend ctrlAB newTrainLR et nbrTrainsLR vaut " + this.nbrTrainsLR);
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsLR += 1;
        if (this.debugCtrlAB) {
            System.out.println("newTrainLR et nbrTrainsLR vaut " + this.nbrTrainsLR);
        }
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void newTrainRL() {
        while (!canNewTrainRL()) {
            try {
                if (this.debugCtrlAB) {
                    System.out.println("Un train attend ctrlAB newTrainRL et nbrTrainsRL vaut " + this.nbrTrainsRL);
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsRL += 1;
        if (this.debugCtrlAB) {
            System.out.println("newTrainRL et nbrTrainsRL vaut " + this.nbrTrainsRL);
        }
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void arrivedTrainLR() {
        while (!(this.nbrTrainsLR > 0)) {
            try {
                if (this.debugCtrlAB) {
                    System.out.println("Un train attend ctrlAB arrivedTrainLR et nbrTrainsLR vaut " + this.nbrTrainsLR);
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsLR -= 1;
        if (this.debugCtrlAB) {
            System.out.println("arrivedTrainLR et nbrTrainsLR vaut " + this.nbrTrainsLR);
        }
        notifyAll();
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void arrivedTrainRL() {
        while (!(this.nbrTrainsRL > 0)) {
            try {
                if (this.debugCtrlAB) {
                    System.out.println("Un train attend ctrlAB arrivedTrainRL et nbrTrainsRL vaut " + this.nbrTrainsRL);
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.nbrTrainsRL -= 1;
        if (this.debugCtrlAB) {
            System.out.println("arrivedTrainRL et nbrTrainsRL vaut " + this.nbrTrainsRL);
        }
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