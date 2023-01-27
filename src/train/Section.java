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
		this.sectionDispo = true;
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public synchronized void newTrain() {
		if (this.railway.debugSection) {
			System.out.println(super.getName() + " section newTrain");
		}
		while (!this.sectionDispo) {
			try {
				if (this.railway.debugSection) {
					System.out.println("Train attend section");
				}
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.sectionDispo = false;
		if (this.railway.debugSection) {
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
		if (this.railway.debugSection) {
			System.out.println(super.getName() + "section leaveTrain");
		}
		while (this.sectionDispo) {
			try {
				if (this.railway.debugSection) {
					System.out.println("Train attend section");
				}
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.sectionDispo = true;
		if (this.railway.debugSection) {
			System.out.println("leaveTrain exécutée");
		}
		notifyAll();
	}
}
