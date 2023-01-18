package train;

/**
 * Représentation de la position d'un train dans le circuit. Une position
 * est caractérisée par deux valeurs :
 * <ol>
 * <li>
 * L'élément où se positionne le train : une gare (classe {@link Station})
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

	public Position(Element elt, Direction d) {
		if (elt == null || d == null)
			throw new NullPointerException();
		this.pos = elt;
		this.direction = d;
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
	 * Calcul du prochain élément où le train va se rendre.
	 *
	 * @author Nicolas Sempéré
	 */
	public void setNewPos() {
		Element oldPosition = this.pos;
		Element newPosition = oldPosition.railway.getNextElement(this.pos, this.direction);
		this.pos = newPosition;
		System.out.println("Le train sort de " + oldPosition.getName() + " et entre dans " + this.pos.getName());
	}

	/**
	 * Si le train est en bout de ligne, sa direction est inversée.
	 *
	 * @author Nicolas Sempéré
	 */
	public void setNewDir() {
		int indexOfPos = this.pos.railway.getIndexOfElement(this.pos);
		// System.out.println("L'index est " + indexOfPos + " et la direction est " +
		// this.direction);
		if (indexOfPos == 0 || indexOfPos == (this.pos.railway.railwayLength - 1)) {
			if (this.direction == Direction.RL) {
				this.direction = Direction.LR;
				System.out.println("Le train se retourne.");
			} else {
				this.direction = Direction.RL;
				System.out.println("Le train se retourne.");
			}
		}
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
