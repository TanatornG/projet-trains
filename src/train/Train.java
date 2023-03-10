package train;

/**
 * Représentation d'un train. Un train est caractérisé par deux valeurs :
 * <ol>
 * <li>
 * Son nom pour l'affichage.
 * </li>
 * <li>
 * La position qu'il occupe dans le circuit (un élément avec une direction) :
 * classe {@link Position}.
 * </li>
 * <li>
 * Le déplacement du train est décomposé en deux temps :
 * - il quitte une section (ou une gare)
 * - il atteint une section (ou une gare)
 * <li>
 * </ol>
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Mayte segarra <mt.segarra@imt-atlantique.fr>
 *         Test if the first element of a train is a station
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @version 0.3
 */
public class Train implements Runnable {
	private final String name;
	private final Position pos;

	public Train(String name, Position p) throws BadPositionForTrainException {
		if (name == null || p == null)
			throw new NullPointerException();

		// A train should be first be in a station
		if (!(p.getPos() instanceof Gare)) {

			throw new BadPositionForTrainException(name);
		}

		this.name = name;
		this.pos = p.clone();
	}

	/**
	 * @author Nicolas Sempéré
	 */
	private void atteindre() {
		this.pos.arriver(this.name);
	}

	/**
	 * @author Nicolas Sempéré
	 */
	private void partir() {
		this.pos.quitter(this.name);
	}

	/**
	 * @author Nicolas Sempéré
	 */
	public void run() {
		while (true) {
			atteindre();
			partir();
		}
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Train[");
		result.append(this.name);
		result.append("]");
		result.append(" is on ");
		result.append(this.pos);
		return result.toString();
	}
}
