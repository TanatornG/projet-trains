package train;

import java.util.Arrays;

public class Controller {

    private Railway railway;
    private int[] controller;
    private boolean debugCtrl;

    Controller(Railway railway) {
        this.railway = railway;
        // -2 car :
        // on compte uniquement les "liaisons" entre les éléments,
        // on ne compte pas la liaison entre la gare avant déploiement.
        this.controller = new int[this.railway.railwayLength - 2];
        Arrays.fill(this.controller, 1);
        this.debugCtrl = false;
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void inUse(int posIndex, String trainName) {
        int index = posIndex - 1;
        while (!(this.controller[index] == 1)) {
            try {
                if (this.debugCtrl) {
                    System.out.println("Le train " + trainName + " attend ctrl inUse");
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.controller[index] = 0;
        if (this.debugCtrl) {
            System.out.println("Le train " + trainName + " inUse la liaison numéro" + index);
        }
        notifyAll();

    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void free(int posIndex, String trainName) {
        int index = posIndex - 1;
        while (!(this.controller[index] == 0)) {
            try {
                if (this.debugCtrl) {
                    System.out.println("Le train " + trainName + " attend ctrl free");
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.controller[index] = 1;
        if (this.debugCtrl) {
            System.out.println("Le train " + trainName + " free la liaison numéro" + index);
        }
        notifyAll();

    }
}
