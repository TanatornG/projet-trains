package train;

/**
 * Cette classe abstraite est la représentation générique d'un élément de base
 * d'un
 * circuit, elle factorise les fonctionnalitÃ©s communes des deux sous-classes :
 * l'entrée d'un train, sa sortie et l'appartenance au circuit.<br/>
 * Les deux sous-classes sont :
 * <ol>
 * <li>La représentation d'une gare : classe {@link Gare}</li>
 * <li>La représentation d'une section de voie ferrée : classe
 * {@link Section}</li>
 * </ol>
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public abstract class Element {
	private final String name;
	protected Railway railway;

	private CtrlAdjacence controller;
	private CtrlSensInverse ctrlAM;
	private CtrlSensInverse ctrlMB;
	private CtrlCapaciteLigne ctrlM;

	protected Element(String name) {
		if (name == null)
			throw new NullPointerException();

		this.name = name;

	}

	public void setRailway(Railway r) {
		if (r == null)
			throw new NullPointerException();

		this.railway = r;
	}

	/**
	 * Implémentée dans l'exercice 2
	 *
	 * @author Nicolas Sempéré
	 */
	public void newTrain(String trainName) {
	}

	/**
	 * Implémentée dans l'exercice 2
	 *
	 * @author Nicolas Sempéré
	 */
	public void leaveTrain(String trainName) {
	}

	/**
	 * @author Nicolas Sempéré
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
