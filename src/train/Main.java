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
		Gare B = new Gare("GareB", 3);
		Section S2 = new Section("S2");
		Section S3 = new Section("S3");
		Section S4 = new Section("S4");
		Railway r = new Railway(new Element[] { GareAvantDeploiement, A, S2, S3, S4, B });
		System.out.println("The railway is:" + "\t" + r);
		Controller controller = new Controller(r);
		ControllerAB ctrlAB = new ControllerAB();
		Position p = new Position(GareAvantDeploiement, Direction.LR, controller, ctrlAB);
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
