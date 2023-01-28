package train;

/**
 * Représentation d'une gare. C'est une sous-classe de la classe
 * {@link Element}.
 * Une gare est caractérisée par un nom et un nombre de quais (donc de trains
 * qu'elle est susceptible d'accueillir à un instant donné).
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Gare extends Element {
	private final int size;
	private int quaisDispos;

	public Gare(String name, int size) {
		super(name);
		if (name == null || size <= 0)
			throw new NullPointerException();
		this.size = size;
		this.quaisDispos = size;
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public synchronized void newTrain(String trainName) {
		if (this.railway.debugGare) {
			System.out.println(super.getName() + " newTrain et il y a " + this.quaisDispos
					+ " quais dispos et la size vaut " + this.size);
		}
		while (!(this.quaisDispos > 0)) {
			try {
				if (this.railway.debugGare) {
					System.out.println("Train attend gare newTrain");
				}
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------------------>" + "Le train " + trainName +
				" arrive en " + super.getName());
		this.quaisDispos -= 1;
		if (this.railway.debugGare) {
			System.out.println("newTrain exécutée et quais dispos vaut" + this.quaisDispos);
		}
		notifyAll();
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public synchronized void leaveTrain(String trainName) {
		if (this.railway.debugGare) {
			System.out.println(super.getName() + " leaveTrain et il y a " + this.quaisDispos
					+ " quais dispos et la size vaut " + this.size);
		}
		while (!(this.quaisDispos < this.size && this.quaisDispos >= 0)) {
			try {
				if (this.railway.debugGare) {
					System.out.println("Train attend gare leave" + super.getName());
				}
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------------------>" + "Le train " + trainName +
				" quitte " + super.getName());
		this.quaisDispos += 1;

		if (this.railway.debugGare) {
			System.out.println("leaveTrain exécutée et quais dispos vaut" + this.quaisDispos);
		}
		notifyAll();
	}
}
