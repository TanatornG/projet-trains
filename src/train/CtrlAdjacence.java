package train;

import java.util.Arrays;

/**
 * Contrôleur empêchant deux trains de se doubler ou de se croiser.
 *
 * Utilisé quelque soit la forme de la ligne (c'est à dire pour tous les
 * exercices) :
 * Ex 2 et 3 : Gare A – Section 2 – Section 3 – Gare B.
 * Ex 4 : Gare A – Section 2 – Section 3 – Gare M – Section 5 – Section 6 – Gare
 *
 * Cf --> CONTROLLER en FSP.
 *
 * @author Nicolas Sempéré
 */
public class CtrlAdjacence {

    private int[] controller;

    CtrlAdjacence(int railwayLength) {
        // -2 car :
        // on compte uniquement les "liaisons" entre les éléments,
        // on ne compte pas la liaison entre la gare avant déploiement.
        this.controller = new int[railwayLength - 2];
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
