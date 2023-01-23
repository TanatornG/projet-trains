package train;

/**
 * Représentation de la position d'un train dans le circuit. Une position
 * est caractérisée par deux valeurs :
 * <ol>
 * <li>
 * L'élément où se positionne le train : une gare (classe {@link Gare})
 * ou une section de voie ferrée (classe {@link Section}).
 * </li>
 * <li>
 * La direction qu'il prend (enumération {@link Direction}) : de gauche à
 * droite ou de droite à gauche.
 * </li>
 * </ol>
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr> Modifié par Mayte
 *         Segarra
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 *
 * @version 0.3
 */
public class Position implements Cloneable {
	private Direction direction;
	private Element pos;

	private boolean canLeaveToGoRL;
	private boolean canLeaveToGoLR;
	private boolean canLeaveGareA;
	private boolean canLeaveGareB;
	private final int railwayLength;

	public Position(Element elt, Direction d) {
		if (elt == null || d == null)
			throw new NullPointerException();
		this.pos = elt;
		this.direction = d;

		// The length of the railway (not counting "GareAvantDeploiement")
		railwayLength = this.pos.railway.railwayLength - 1;
	}

	/**
	 * @return Position
	 */
	@Override
	public Position clone() {
		try {
			return (Position) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return Element
	 */
	public Element getPos() {
		return pos;
	}

	/**
	 * Méthode permettant de déterminer où le train peut se rendre
	 *
	 * @author Nicolas Sempéré
	 */
	public void arriver() {
		int indexOfPos = this.pos.railway.getIndexOfElement(this.pos);
		// System.out.println("L'index est " + indexOfPos + " et la direction est " +
		// this.direction);
		if (indexOfPos > 1 & indexOfPos < (railwayLength)) {
			if (this.direction == Direction.LR) {
				this.canLeaveToGoLR = true;
			} else if (this.direction == Direction.RL) {
				this.canLeaveToGoRL = true;
			}
		} else if (indexOfPos == 1) {
			this.canLeaveGareA = true;
		} else if (indexOfPos == railwayLength) {
			this.canLeaveGareB = true;
		}
	}

	/**
	 * Méthode réalisant concrètement le déplacement du train
	 *
	 * @author Nicolas Sempéré
	 */
	public void quitter() {
		Element currentPosition = this.pos;
		if (this.canLeaveToGoLR) {
			this.pos = this.pos.railway.getElementLR(this.pos);
			this.canLeaveToGoLR = false;
		} else if (this.canLeaveToGoRL) {
			this.pos = this.pos.railway.getElementRL(this.pos);
			this.canLeaveToGoRL = false;
		} else if (this.canLeaveGareA) {
			this.pos = this.pos.railway.getElementLR(this.pos);
			this.direction = Direction.LR;
			this.canLeaveGareA = false;
		} else if (this.canLeaveGareB) {
			this.pos = this.pos.railway.getElementRL(this.pos);
			this.direction = Direction.RL;
			this.canLeaveGareB = false;
		}
		System.out.println("Le train sort de " + currentPosition.getName()
				+ " et entre dans " + this.pos.getName()
				+ " Sa direction est " + this.direction);
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(this.pos.toString());
		result.append(" going ");
		result.append(this.direction);
		return result.toString();
	}
}
