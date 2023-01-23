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
	public void newTrain() {
		if (this.quaisDispos > 0) {
			this.quaisDispos -= 1;
		}
	}

	/**
	 *
	 *
	 * @author Nicolas Sempéré
	 */
	public void leaveTrain() {
		if (this.quaisDispos >= 0 && this.quaisDispos < this.size) {
			this.quaisDispos += 1;
		}
	}
}
