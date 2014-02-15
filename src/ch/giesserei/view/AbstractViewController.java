package ch.giesserei.view;

import com.vaadin.ui.Component;

/**
 * Basisklasse für alle ViewController.
 * 
 * @author Steffen Förster
 */
public abstract class AbstractViewController {

	public AbstractViewController() {
	}
	
	/**
	 * Erstellt die View und liefert diese zurück.
	 * 
	 * @return siehe Beschreibung
	 */
	public abstract Component createView();
	
	public abstract Component getView();
}
