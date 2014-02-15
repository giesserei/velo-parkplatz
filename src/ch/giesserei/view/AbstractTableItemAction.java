package ch.giesserei.view;


/**
 * Basisklasse für TableItemActions.
 *  
 * @author Steffen Förster
 */
public abstract class AbstractTableItemAction implements TableItemAction {

	private final AbstractViewController viewController;
	
	public AbstractTableItemAction(AbstractViewController viewController) {
		this.viewController = viewController;
	}
	
	public AbstractViewController getController() {
		return this.viewController;
	}
	
}
