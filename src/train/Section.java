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
	public synchronized void newTrain(String trainName) {
		while (!this.sectionDispo) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------------------>" + "Le train " + trainName +
				" arrive en " + super.getName());
		this.sectionDispo = false;
		notifyAll();
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public synchronized void leaveTrain(String trainName) {
		while (this.sectionDispo) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------------------>" + "Le train " + trainName +
				" quitte " + super.getName());
		this.sectionDispo = true;
		notifyAll();
	}
}
