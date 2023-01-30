package train;

import java.util.Arrays;

public class Controller {

    private Railway railway;
    private int[] controller;

    Controller(Railway railway) {
        this.railway = railway;
        // -2 car :
        // on compte uniquement les "liaisons" entre les éléments,
        // on ne compte pas la liaison entre la gare avant déploiement.
        this.controller = new int[this.railway.railwayLength - 2];
        Arrays.fill(this.controller, 1);
    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void inUse(int posIndex, String trainName) {
        int index = posIndex - 1;
        while (!(this.controller[index] == 1)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.controller[index] = 0;
        notifyAll();

    }

    /**
     * @author Nicolas Sempéré
     */
    public synchronized void free(int posIndex, String trainName) {
        int index = posIndex - 1;
        while (!(this.controller[index] == 0)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.controller[index] = 1;
        notifyAll();

    }
}
