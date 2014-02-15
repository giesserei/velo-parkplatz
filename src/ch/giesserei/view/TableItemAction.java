package ch.giesserei.view;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Diese Aktion wird ausgelöst, wenn ein Button in einer Tabellenzeile angeklickt wird.
 * 
 * @author Steffen Förster
 *
 */
public interface TableItemAction {

	/**
	 * Wird aufgerufen, wenn das Action ausgeführt werden soll.
	 * 
	 * @param event Event
	 * @param source Tabelle
	 * @param itemId Objekt, welches in der Zeile dargestellt wird
	 * @param columnId Spalte, in der der Button angezeigt wird
	 */
	void actionPerformed(ClickEvent event, Table source, final Object itemId, Object columnId);
	
}
