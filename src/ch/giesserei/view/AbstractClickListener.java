package ch.giesserei.view;

import com.vaadin.ui.Button.ClickListener;

/**
 * Basisklasse für einen {@link ClickListener}, welcher von einer View genutzt wird.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public abstract class AbstractClickListener implements ClickListener {

	private final AbstractViewController viewController;
	
	public AbstractClickListener(AbstractViewController viewController) {
		this.viewController = viewController;
	}
	
	public AbstractViewController getViewController() {
		return viewController;
	}

}
