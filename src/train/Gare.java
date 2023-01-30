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

	public int getNbrQuais() {
		return this.size;
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public synchronized void newTrain(String trainName) {
		while (!this.canNewTrain()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------------------>" + "Le train " + trainName +
				" arrive en " + super.getName());
		this.quaisDispos -= 1;
		notifyAll();
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public synchronized void leaveTrain(String trainName) {
		while (!this.canLeaveTrain()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------------------>" + "Le train " + trainName +
				" quitte " + super.getName());
		this.quaisDispos += 1;
		notifyAll();
	}

	/**
	 * @author Nicolas Sempéré
	 */
	private boolean canNewTrain() {
		int previousQuaisDispos = this.quaisDispos;
		this.quaisDispos -= 1;
		boolean result = invariant();
		this.quaisDispos = previousQuaisDispos;
		return result;
	}

	/**
	 * @author Nicolas Sempéré
	 */
	private boolean canLeaveTrain() {
		int previousQuaisDispos = this.quaisDispos;
		this.quaisDispos += 1;
		boolean result = invariant();
		this.quaisDispos = previousQuaisDispos;
		return result;
	}

	/**
	 * @author Nicolas Sempéré
	 */
	private boolean invariant() {
		return (0 <= this.quaisDispos && this.quaisDispos <= this.size);
	}
}
