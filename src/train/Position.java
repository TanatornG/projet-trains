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

	private CtrlAdjacence ctrlAdj;
	private CtrlSensInverse ctrlSensAM;
	private CtrlSensInverse ctrlSensMB;
	private CtrlCapaciteLigne ctrlCapacite;

	private final int mainRailwayLength;
	private final int indexOfGareM;

	private boolean canLeaveToGoRL;
	private boolean canLeaveToGoLR;
	private boolean canLeaveGareA;
	private boolean canLeaveGareB;
	private boolean canDeployer;
	private boolean canLeaveGareM;

	public Position(Element elt, Direction d,
			CtrlAdjacence ctrlAdj,
			CtrlSensInverse ctrlSensAM,
			CtrlSensInverse ctrlSensMB,
			CtrlCapaciteLigne ctrlCapacite) {

		if (elt == null || d == null)
			throw new NullPointerException();
		this.pos = elt;
		this.direction = d;

		// La longueur de la ligne de chemin de fer (sans compter
		// "GareAvantDeploiement")
		this.mainRailwayLength = this.pos.railway.railwayLength - 1;
		this.indexOfGareM = this.pos.railway.getIndexOfGareM();

		this.ctrlAdj = ctrlAdj;
		this.ctrlSensAM = ctrlSensAM;
		this.ctrlSensMB = ctrlSensMB;
		this.ctrlCapacite = ctrlCapacite;
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

		// Sections et Gare M
		if (this.indexOfPos > 1 && this.indexOfPos < (mainRailwayLength)) {

			if (this.direction == Direction.LR) {
				this.canLeaveToGoLR = true;

				this.indexOfCtrl = this.indexOfPos - 1;

			} else if (this.direction == Direction.RL) {
				this.canLeaveToGoRL = true;

				this.indexOfCtrl = this.indexOfPos;
			}
		}

		// Gare M
		if (this.indexOfPos == this.indexOfGareM) {
			this.canLeaveGareM = true;

			if (this.direction == Direction.LR) {
				this.ctrlSensAM.arrivedTrainLR();
			} else if (this.direction == Direction.RL) {
				this.ctrlSensMB.arrivedTrainRL();
			}

		}

		// Gare A
		if (this.indexOfPos == 1) {
			this.canLeaveGareA = true;
			this.indexOfCtrl = 1;

			this.ctrlCapacite.arrivedTrainFromLine();
			this.ctrlSensAM.arrivedTrainRL();
		}

		// Gare B
		if (this.indexOfPos == mainRailwayLength) {
			this.canLeaveGareB = true;
			this.indexOfCtrl = this.mainRailwayLength - 1;

			this.ctrlCapacite.arrivedTrainFromLine();
			this.ctrlSensMB.arrivedTrainLR();
		}

		// Gare Avant Déploiement
		if (this.indexOfPos == 0) {
			this.canDeployer = true;

			this.pos = this.pos.railway.getElementLR(this.pos);
		}
		// Actions communes pour arriver
		this.ctrlArriver(trainName);
	}

	/**
	 * Réalisation des contrôles pour arriver
	 *
	 * @author Nicolas Sempéré
	 */
	private void ctrlArriver(String trainName) {
		this.pos.newTrain(trainName);
		if (!this.canDeployer) {
			this.ctrlAdj.free(this.indexOfCtrl, trainName);
		}
	}

	/**
	 * Méthode réalisant concrètement le déplacement du train
	 *
	 * @author Nicolas Sempéré
	 */
	public void quitter(String trainName) {
		this.indexOfPos = this.pos.railway.getIndexOfElement(this.pos);

		// Sections et Gare M (sens LR)
		if (this.canLeaveToGoLR) {
			this.indexOfCtrl = this.indexOfPos;
			if (this.canLeaveGareM) {
				this.ctrlSensMB.newTrainLR();
				this.canLeaveGareM = false;
			}
			this.canLeave(trainName);
			this.canLeaveToGoLR = false;
			this.pos = this.pos.railway.getElementLR(this.pos);

		}

		// Sections et Gare M (sens RL)
		else if (this.canLeaveToGoRL) {
			this.indexOfCtrl = this.indexOfPos - 1;
			if (this.canLeaveGareM) {
				this.ctrlSensAM.newTrainRL();
				this.canLeaveGareM = false;
			}
			this.canLeave(trainName);
			this.canLeaveToGoRL = false;
			this.pos = this.pos.railway.getElementRL(this.pos);

		}

		// Gare A et Gare Avant Déploiement
		else if (this.canLeaveGareA || this.canDeployer) {
			this.indexOfCtrl = 1;
			this.ctrlCapacite.newTrainOnLine();
			this.ctrlSensAM.newTrainLR();
			this.canLeave(trainName);
			this.direction = Direction.LR;
			this.pos = this.pos.railway.getElementLR(this.pos);
			this.canLeaveGareA = false;
			this.canDeployer = false;

		}

		// Gare B
		else if (this.canLeaveGareB) {
			this.indexOfCtrl = this.mainRailwayLength - 1;
			this.ctrlCapacite.newTrainOnLine();
			this.ctrlSensMB.newTrainRL();
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
		this.ctrlAdj.inUse(this.indexOfCtrl, trainName);
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
