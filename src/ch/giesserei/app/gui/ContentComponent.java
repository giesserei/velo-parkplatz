package ch.giesserei.app.gui;

import ch.giesserei.resource.AppRes;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Komponente repräsentiert den Content-Bereich der Applikation.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ContentComponent extends CustomComponent {

	private final VerticalLayout layout;
	
	private Component view;
	
	private Component header;
	
	/**
	 * Konstruktor.
	 */
	public ContentComponent() {
		this.layout = new VerticalLayout();
		this.layout.setMargin(true);
		this.layout.setSpacing(true);
		setCompositionRoot(this.layout);
	}
	
	/**
     * Setzt einen neuen Content.
     */
	public void setView(Component newView, Component newHeader) {
		if (this.view != null) {
			this.layout.removeComponent(this.view);
		}
		if (this.header != null) {
		    this.layout.removeComponent(this.header);
		}
		this.layout.addComponent(newHeader);
		this.layout.addComponent(newView);
		this.view = newView;
		this.header = newHeader;
	}

	/**
	 * Setzt einen neuen Content.
	 */
	public void setView(Component newView, String headerKey) {
	    Label label = new Label(AppRes.getString(headerKey));
	    label.setContentMode(ContentMode.HTML);
	    setView(newView, label);
    }
}
