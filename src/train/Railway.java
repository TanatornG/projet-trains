package train;

/**
 * Représentation d'un circuit constitué d'éléments de voie ferrée : gare ou
 * section de voie
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Railway {
	private final Element[] elements;
	protected final int railwayLength;

	public Railway(Element[] elements) {
		if (elements == null)
			throw new NullPointerException();

		this.elements = elements;
		for (Element e : elements)
			e.setRailway(this);
		this.railwayLength = this.elements.length;
	}

	/**
	 *
	 * @author Nicolas Sempéré
	 */
	public Element getNextElement(Element pos, Direction direction) {
		int indexOfPos = getIndexOfElement(pos);
		if (direction == Direction.LR) {
			return this.elements[indexOfPos + 1];
		} else {
			return this.elements[indexOfPos - 1];
		}
	}

	/**
	 * @param pos (soit une gare, soit une section de rails)
	 * @return l'indice de pos dans le tableau représentant la ligne
	 *
	 * @author Nicolas Sempéré
	 */
	public int getIndexOfElement(Element pos) {
		for (int i = 0; i <= (this.elements.length - 1); i++) {
			// System.out.println("this.elements[i] est" + this.elements[i] + " et pos est"
			// + pos);
			if (this.elements[i] == pos) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Element e : this.elements) {
			if (first)
				first = false;
			else
				result.append("--");
			result.append(e);
		}
		return result.toString();
	}
}
