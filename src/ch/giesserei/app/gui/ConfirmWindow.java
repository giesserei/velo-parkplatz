package ch.giesserei.app.gui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Definiert einen Bestätigungsdialog.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ConfirmWindow extends Window {
	
	private Decision decision;
	
	private Button btnYes = new Button();
	
	private Button btnNo = new Button();
	
	private VerticalLayout layout = new VerticalLayout();
	
	private HorizontalLayout buttonsLayout = new HorizontalLayout();

	/**
	 * Konstruktor.
	 * 
	 * @param caption Dialog-Titel
	 * @param question Frage
	 * @param yesCaption Ja-Button-Bezeichnung
	 * @param noCaption Nein-Button-Bezeichnung
	 * @param decision Callback
	 */
    public ConfirmWindow(String caption, String question, String yesCaption, String noCaption, final Decision decision) {
		setCaption(caption);
		this.btnYes.setCaption(yesCaption);
		this.btnYes.focus();
		this.btnNo.setCaption(noCaption);
		setModal(true);
		center();
		this.buttonsLayout.addComponent(this.btnYes);
		this.buttonsLayout.setComponentAlignment(this.btnYes, Alignment.MIDDLE_CENTER);
		this.buttonsLayout.addComponent(this.btnNo);
		this.buttonsLayout.setComponentAlignment(this.btnNo, Alignment.MIDDLE_CENTER);
		this.layout.addComponent(new Label(question));
		this.layout.addComponent(this.buttonsLayout);
		setContent(this.layout);
		this.layout.setMargin(true);
		this.buttonsLayout.setMargin(true);
		this.buttonsLayout.setWidth("100%");
		setWidth("300px");
		setHeight("160px");
		setResizable(false);
		this.decision = decision;
		
		addListners();
	}
	
    private void addListners() {
		this.btnYes.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				decision.yes();
				close();
			}
		});

		this.btnNo.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				decision.no();
				close();
			}
		});
		
		addShortcutListener(new ShortcutListener("Close", ShortcutAction.KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				close();
			}
		});
	}
	
	// ---------------------------------------------------------
    // Callback-Interface
    // ---------------------------------------------------------
	
	public interface Decision {

		void yes();
		
		void no();
		
	}
}
