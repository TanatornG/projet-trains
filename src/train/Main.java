package train;

/**
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Un train est initialement en position 0 (il se trouve en
		// GareAvantDeploiement).
		Gare GareAvantDeploiement = new Gare("GareAvantDeploiement", 1);

		Gare A = new Gare("GareA", 3);

		Section S2 = new Section("S2");
		Section S3 = new Section("S3");
		Section S4 = new Section("S4");

		Gare M = new Gare("GareM", 2);

		Section S6 = new Section("S6");
		Section S7 = new Section("S7");
		Section S8 = new Section("S8");

		Gare B = new Gare("GareB", 3);

		Railway r = new Railway(new Element[] { GareAvantDeploiement, A, S2, S3, S4, M, S6, S7, S8, B });
		System.out.println("The railway is:" + "\t" + r);

		CtrlAdjacence controller = new CtrlAdjacence(r.railwayLength);
		CtrlSensInverse ctrlAM = new CtrlSensInverse();
		CtrlSensInverse ctrlMB = new CtrlSensInverse();
		CtrlCapaciteLigne ctrlM = new CtrlCapaciteLigne(M.getNbrQuais());

		Position p = new Position(GareAvantDeploiement, Direction.LR, controller, ctrlAM, ctrlMB, ctrlM);
		try {
			new Thread(new Train("1", p)).start();
			new Thread(new Train("2", p.clone())).start();
			new Thread(new Train("3", p.clone())).start();
			// new Thread(new Train("4", p.clone())).start();
			// new Thread(new Train("5", p.clone())).start();
			// new Thread(new Train("6", p.clone())).start();
		} catch (BadPositionForTrainException e) {
			System.out.println("Le train " + e.getMessage());
		}

	}
}
