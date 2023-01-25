package train;

import java.util.Arrays;

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

	private int[] controller;

	protected final boolean debugCtrl;
	protected final boolean debugGare;
	protected final boolean debugPosition;
	protected final boolean debugSection;

	public Railway(Element[] elements) {
		if (elements == null)
			throw new NullPointerException();

		this.elements = elements;
		for (Element e : elements)
			e.setRailway(this);
		this.railwayLength = this.elements.length;
		this.controller = new int[railwayLength - 2];
		Arrays.fill(this.controller, 0);
		this.debugCtrl = false;
		this.debugGare = true;
		this.debugSection = false;
		this.debugPosition = true;
	}

	/**
	 * @param pos
	 * @return L'élément à droite de pos
	 * @author Nicolas Sempéré
	 */
	public Element getElementLR(Element pos) {
		return this.elements[getIndexOfElement(pos) + 1];
	}

	/**
	 * @param pos
	 * @return L'élément à gauche de pos
	 * @author Nicolas Sempéré
	 */
	public Element getElementRL(Element pos) {
		return this.elements[getIndexOfElement(pos) - 1];
	}

	/**
	 * @param pos (soit une gare, soit une section de rails)
	 * @return l'indice de pos dans le tableau représentant la ligne de chemin de
	 *         fer
	 *
	 * @author Nicolas Sempéré
	 */
	public int getIndexOfElement(Element pos) {
		for (int i = 0; i <= (this.railwayLength - 1); i++) {
			// System.out.println("this.elements[i] est" + this.elements[i] + " et pos est"
			// + pos);
			if (this.elements[i] == pos) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @author Nicolas Sempéré
	 */
	public synchronized void inUse(int posIndex, String trainName) {
		if (debugCtrl) {
			System.out.println("Le train " + trainName + " inUse");
		}
		int index = posIndex - 1;
		if (debugCtrl) {
			System.out.println("index vaut " + index + " et controller de index vaut " + this.controller[index]);
		}
		while (!(this.controller[index] == 1)) {
			try {
				System.out.println("Le train " + trainName + " attend ctrl");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.controller[index] = 0;
		if (debugCtrl) {
			System.out
					.println(
							"Le controller vaut " + Arrays.toString(this.controller) + " et le train vaut" + trainName);
		}
		notifyAll();

	}

	/**
	 * @author Nicolas Sempéré
	 */
	public synchronized void free(int posIndex, String trainName) {
		if (debugCtrl) {
			System.out.println("Le train " + trainName + " free");
		}
		int index = posIndex - 1;
		if (debugCtrl) {
			System.out.println("index vaut " + index + " et controller de index vaut " + this.controller[index]);
		}
		while (!(this.controller[index] == 0)) {
			try {
				System.out.println("Le train " + trainName + " attend ctrl");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.controller[index] = 1;
		if (debugCtrl) {
			System.out
					.println(
							"Le controller vaut " + Arrays.toString(this.controller) + " et le train vaut" + trainName);
		}
		notifyAll();

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
