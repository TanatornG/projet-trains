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

	private boolean canDeployer;

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
	public void arriver(String trainName) {
		int indexOfPos = this.pos.railway.getIndexOfElement(this.pos);
		if (this.pos.railway.debugPosition) {
			System.out.println("\n" + "L'index est " + indexOfPos + " et la direction est " +
					this.direction);
		}
		if (this.pos.railway.debugPosition) {
			System.out.println("arriver");
		}
		if (indexOfPos > 1 & indexOfPos < (railwayLength)) {
			if (this.direction == Direction.LR) {
				this.pos.newTrain(trainName);
				this.pos.railway.free(indexOfPos - 1, trainName, this.pos);
				this.canLeaveToGoLR = true;

			} else if (this.direction == Direction.RL) {
				this.pos.newTrain(trainName);
				this.pos.railway.free(indexOfPos, trainName, this.pos);
				this.canLeaveToGoRL = true;
			}

		} else if (indexOfPos == 1) {
			this.pos.newTrain(trainName);
			this.pos.railway.free(1, trainName, this.pos);
			this.canLeaveGareA = true;

		} else if (indexOfPos == railwayLength) {
			this.pos.newTrain(trainName);
			this.pos.railway.free(railwayLength - 1, trainName, this.pos);
			this.canLeaveGareB = true;

		} else if (indexOfPos == 0) {
			this.pos = this.pos.railway.getElementLR(this.pos);
			this.pos.newTrain(trainName);
			this.canDeployer = true;
		}
	}

	/**
	 * Méthode réalisant concrètement le déplacement du train
	 *
	 * @author Nicolas Sempéré
	 */
	public void quitter(String trainName) {
		if (this.pos.railway.debugPosition) {
			System.out.println("quitter");
		}
		Element currentPosition = this.pos;
		int indexOfPos = currentPosition.railway.getIndexOfElement(currentPosition);

		if (this.canLeaveToGoLR) {
			this.pos.railway.inUse(indexOfPos, trainName);
			this.pos.leaveTrain(trainName);
			this.pos = this.pos.railway.getElementLR(this.pos);
			this.canLeaveToGoLR = false;

		} else if (this.canLeaveToGoRL) {
			this.pos.railway.inUse(indexOfPos - 1, trainName);
			this.pos.leaveTrain(trainName);
			this.pos = this.pos.railway.getElementRL(this.pos);
			this.canLeaveToGoRL = false;

		} else if (this.canLeaveGareA || this.canDeployer) {
			this.pos.railway.inUse(1, trainName);
			this.pos.leaveTrain(trainName);
			this.pos = this.pos.railway.getElementLR(this.pos);
			this.direction = Direction.LR;
			this.canLeaveGareA = false;
			this.canDeployer = false;

		} else if (this.canLeaveGareB) {
			this.pos.railway.inUse(railwayLength - 1, trainName);
			this.pos.leaveTrain(trainName);
			this.pos = this.pos.railway.getElementRL(this.pos);
			this.direction = Direction.RL;
			this.canLeaveGareB = false;

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
