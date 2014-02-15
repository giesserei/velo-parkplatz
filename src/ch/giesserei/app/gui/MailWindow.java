package ch.giesserei.app.gui;

import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.resource.AppRes;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Zeigt eine Mail an. Der Mail-Text kann vor dem Senden bearbeitet werden.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class MailWindow extends Window {
	
	private Button btnCancel;
	
	private Button btnSend;
	
	private TextArea text;
	
	private Decision decision;
	
	private VerticalLayout layout = new VerticalLayout();
	
	/**
	 * Konstruktor.
	 */
    public MailWindow(String mailto, String subject, String mailText) {
        initComponents(mailto, subject, mailText);
	}
    
    public void setDecision(Decision decision) {
        this.decision= decision;
    }
    
    public TextArea getText() {
        return this.text;
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
    
    private void initComponents(String mailto, String subject, String mailText) {
        this.layout.setMargin(true);
        this.layout.setSpacing(true);
        
        setCaption(AppRes.getString("mail.confirm.title"));
        
        setModal(true);
        center();
        
        TextField mailtoField = new TextField(AppRes.getString("mail.confirm.mailto"), mailto);
        mailtoField.setWidth("100%");
        this.layout.addComponent(mailtoField);
        
        TextField subjectField = new TextField(AppRes.getString("mail.confirm.subject"), subject);
        subjectField.setWidth("100%");
        this.layout.addComponent(subjectField);
        
        this.text = new TextArea(AppRes.getString("mail.confirm.text"), mailText);
        this.text.setWidth("100%");
        this.text.setHeight("325px");
        this.layout.addComponent(this.text);
        
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);
        
        this.btnSend = new Button(AppRes.getString("btn.send"));
        btnLayout.addComponent(this.btnSend);
        this.btnSend.focus();
        this.btnCancel = new Button(AppRes.getString("btn.cancel"));
        btnLayout.addComponent(this.btnCancel);
        
        this.layout.addComponent(btnLayout);
        
        setContent(this.layout);
        setWidth("900px");
        setHeight("600px");
        setResizable(true);
        addListners();
    }
	
    private void addListners() {
		this.btnCancel.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				close();
				decision.no();
			}
		});
		
		this.btnSend.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
                decision.yes();
            }
        });
		
		addShortcutListener(new ShortcutListener("Close", ShortcutAction.KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				close();
				decision.no();
			}
		});
	}
	
}
