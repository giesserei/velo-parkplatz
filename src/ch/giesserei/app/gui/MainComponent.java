package ch.giesserei.app.gui;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

/**
 * Definiert die Main-Komponente der Applikation.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class MainComponent extends CustomComponent {

	private final NavComponent navComponent;
	
	private final ContentComponent contentComponent;
	
	/**
	 * Konstruktor.
	 */
	public MainComponent() {
		this.navComponent = new NavComponent();
		this.contentComponent = new ContentComponent();
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(this.navComponent);
		layout.addComponent(this.contentComponent);
		
		setCompositionRoot(layout);
	}

	public NavComponent getNavComponent() {
		return navComponent;
	}

	public ContentComponent getContentComponent() {
		return contentComponent;
	}

}
