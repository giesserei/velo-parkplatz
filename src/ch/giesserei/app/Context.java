package ch.giesserei.app;

import ch.giesserei.app.gui.MainComponent;

/**
 * Der Kontext hält diverse Referenzen auf die GUI und Modell-Elemente, welche
 * vom Controller benötigt werden.
 * 
 * @author Steffen Förster
 */
public class Context {

	private final MainComponent mainComponent;
	
	/**
	 * Konstruktor.
	 * 
	 * @param mainComponent Main-Komponente der Applikation
	 */
	public Context(MainComponent mainComponent) {
		this.mainComponent = mainComponent;
	}

	public MainComponent getMainComponent() {
		return mainComponent;
	}
}
