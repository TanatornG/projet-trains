package train;

/**
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// L'implémentation du déploiement d'un train est faite dès l'exercice 2.
		Gare GareAvantDeploiement = new Gare("GareAvantDeploiement", 3);
		Gare A = new Gare("GareA", 1);
		Gare B = new Gare("GareB", 1);
		Section S2 = new Section("S2");
		Section S3 = new Section("S3");
		Section S4 = new Section("S4");
		Railway r = new Railway(new Element[] { GareAvantDeploiement, A, S2, S3, S4, B });
		System.out.println("The railway is:" + "\t" + r);
		// System.out.println("\t" + r);
		Position p = new Position(A, Direction.LR);
		try {
			Train t1 = new Train("1", p);
			// Train t2 = new Train("2", p);
			// Train t3 = new Train("3", p);
			System.out.println(t1);
			// System.out.println(t2);
			// System.out.println(t3);
			for (int i = 0; i < 20; i++) {
				t1.atteindre();
				t1.partir();
			}
		} catch (BadPositionForTrainException e) {
			System.out.println("Le train " + e.getMessage());
		}

	}
}
