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
	private int indexOfPos;
	private int indexOfCtrl;

	private Controller controller;
	private ControllerContraryDir ctrlAM;
	private ControllerContraryDir ctrlMB;
	private ControllerMilieu ctrlM;

	private final int mainRailwayLength;
	private final int indexOfGareM;

	private boolean canLeaveToGoRL;
	private boolean canLeaveToGoLR;
	private boolean canLeaveGareA;
	private boolean canLeaveGareB;
	private boolean canDeployer;
	private boolean canLeaveGareM;

	public Position(Element elt, Direction d, Controller controller, ControllerContraryDir ctrlAM,
			ControllerContraryDir ctrlMB,
			ControllerMilieu ctrlM) {
		if (elt == null || d == null)
			throw new NullPointerException();
		this.pos = elt;
		this.direction = d;

		// La longueur de la ligne de chemin de fer (sans compter
		// "GareAvantDeploiement")
		this.mainRailwayLength = this.pos.railway.railwayLength - 1;
		this.indexOfGareM = this.pos.railway.getIndexOfGareM();

		this.controller = controller;
		this.ctrlAM = ctrlAM;
		this.ctrlMB = ctrlMB;
		this.ctrlM = ctrlM;
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
		this.indexOfPos = this.pos.railway.getIndexOfElement(this.pos);

		if (this.pos.railway.debugPosition) {
			System.out.println("\n" + "L'index est " + this.indexOfPos + " et la direction est " + this.direction);
		}
		if (this.pos.railway.debugPosition) {
			System.out.println("arriver");
		}

		// Sections et Gare M
		if (this.indexOfPos > 1 && this.indexOfPos < (mainRailwayLength)) {
			if (this.direction == Direction.LR) {
				this.indexOfCtrl = this.indexOfPos - 1;
				this.canLeaveToGoLR = true;

			} else if (this.direction == Direction.RL) {
				this.indexOfCtrl = this.indexOfPos;
				this.canLeaveToGoRL = true;
			}
		}
		// Gare M
		if (this.indexOfPos == this.indexOfGareM) {
			if (this.direction == Direction.LR) {
				this.ctrlAM.arrivedTrainLR();
			} else if (this.direction == Direction.RL) {
				this.ctrlMB.arrivedTrainRL();
			}
			this.canLeaveGareM = true;

		}
		// Gare A
		if (this.indexOfPos == 1) {
			this.ctrlM.arrivedTrainFromM();
			this.ctrlAM.arrivedTrainRL();
			this.indexOfCtrl = 1;
			this.canLeaveGareA = true;
		}

		// Gare B
		if (this.indexOfPos == mainRailwayLength) {
			this.ctrlM.arrivedTrainFromM();
			this.ctrlMB.arrivedTrainLR();
			this.indexOfCtrl = this.mainRailwayLength - 1;
			this.canLeaveGareB = true;
		}
		// Gare Avant Déploiement
		if (this.indexOfPos == 0) {
			this.pos = this.pos.railway.getElementLR(this.pos);
			this.canDeployer = true;
		}
		this.pos.newTrain(trainName);
		if (!canDeployer) {
			this.controller.free(this.indexOfCtrl, trainName);
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
		this.indexOfPos = this.pos.railway.getIndexOfElement(this.pos);

		if (this.canLeaveToGoLR) {
			this.indexOfCtrl = this.indexOfPos;
			if (this.canLeaveGareM) {
				this.ctrlMB.newTrainLR();
				this.canLeaveGareM = false;
			}
			this.canLeave(trainName);
			this.canLeaveToGoLR = false;
			this.pos = this.pos.railway.getElementLR(this.pos);

		} else if (this.canLeaveToGoRL) {
			this.indexOfCtrl = this.indexOfPos - 1;
			if (this.canLeaveGareM) {
				this.ctrlAM.newTrainRL();
				this.canLeaveGareM = false;
			}
			this.canLeave(trainName);
			this.canLeaveToGoRL = false;
			this.pos = this.pos.railway.getElementRL(this.pos);

		} else if (this.canLeaveGareA || this.canDeployer) {
			this.indexOfCtrl = 1;
			this.ctrlM.newTrainToM();
			this.ctrlAM.newTrainLR();
			this.canLeave(trainName);
			this.direction = Direction.LR;
			this.pos = this.pos.railway.getElementLR(this.pos);
			this.canLeaveGareA = false;
			this.canDeployer = false;

		} else if (this.canLeaveGareB) {
			this.indexOfCtrl = this.mainRailwayLength - 1;
			this.ctrlM.newTrainToM();
			this.ctrlMB.newTrainRL();
			this.canLeave(trainName);
			this.direction = Direction.RL;
			this.pos = this.pos.railway.getElementRL(this.pos);
			this.canLeaveGareB = false;
		}
	}

	/**
	 * @author Nicolas Sempéré
	 */
	private void canLeave(String trainName) {
		this.controller.inUse(this.indexOfCtrl, trainName);
		this.pos.leaveTrain(trainName);
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
