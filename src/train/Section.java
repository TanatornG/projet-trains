package train;

/**
 * Représentation d'une section de voie ferrée. C'est une sous-classe de la
 * classe {@link Element}.
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Section extends Element {
	private boolean sectionDispo;

	public Section(String name) {
		super(name);
		this.sectionDispo = false;
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public synchronized void newTrain() {
		if (this.railway.debug) {
			System.out.println("newTrain");
		}
		while (!sectionDispo) {
			try {
				if(super.debug){
					System.out.println("Train attend section");
				}
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.sectionDispo = false;
		if (this.railway.debug) {
			System.out.println("newTrain exécutée");
		}
		notifyAll();
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public synchronized void leaveTrain() {
		if (this.railway.debug) {
			System.out.println("leaveTrain et il y a " + this.sectionDispo);
		}
		while (this.sectionDispo) {
			try {
				if(super.debug){
					System.out.println("Train attend section");
				}
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.sectionDispo = true;
		if (this.railway.debug) {
			System.out.println("leaveTrain exécutée");
		}
		notifyAll();
	}
}
