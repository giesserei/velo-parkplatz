package ch.giesserei.view.jperson;

import org.apache.commons.lang3.StringUtils;

import ch.giesserei.model.JuristischePerson;
import ch.giesserei.resource.AppRes;
import ch.giesserei.view.BaseEditForm;
import ch.giesserei.view.validator.AdresseValidator;
import ch.giesserei.view.validator.PlzValidator;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;

/**
 * Definiert das Formular, mit dem eine juristische Person bearbeitet werden kann.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class JuristischePersonEditForm extends BaseEditForm {
	
	private final JuristischePerson person;
	
	private TextField name;
	
	private TextField ansprechpartner;
	
	private TextField strasse;
	
	private TextField ort;
	
	private TextField plz;
	
	private FormLayout layout;
	
	private Button saveButton;
	
	/**
	 * Konstruktor. 
	 * 
	 * @param person Bean, welches bearbeitet werden soll
	 */
	public JuristischePersonEditForm(JuristischePerson person) {
		this.person = person;
		this.layout = new FormLayout();
		this.layout.setMargin(true);
		createFields(this.layout);
		fillForm();
		this.saveButton = new Button(AppRes.getString("btn.save"));
		this.layout.addComponent(this.saveButton);
		setCompositionRoot(this.layout);
	}
	
	public Button getBtnSave() {
		return this.saveButton;
	}
	
	public void fillBean() {
		this.person.setName(this.name.getValue());
		this.person.setAnsprechpartner(this.ansprechpartner.getValue());
		
		this.person.getAdresse().setStrasse(this.strasse.getValue());
		this.person.getAdresse().setOrt(this.ort.getValue());
		if (StringUtils.isBlank(this.plz.getValue())) {
			this.person.getAdresse().setPlz(0);
		}
		else {
			this.person.getAdresse().setPlz(Integer.parseInt(this.plz.getValue()));
		}
	}
	
	// ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
	
	private void createFields(Layout layout) {
		this.name = createTextField(layout, "jperson.lb.name", 50, "val.name.not.null", true);
		this.ansprechpartner = createTextField(layout, "jperson.lb.ansprechpartner", 50, "val.anprechpartner.not.null", true);
		
		this.strasse = createTextField(layout, "person.lb.strasse", 100, false);
		this.ort = createTextField(layout, "person.lb.ort", 100, false);
		
		this.plz = createTextField(layout, "person.lb.plz", 4, true);
		this.plz.addValidator(new PlzValidator());
		this.plz.addValidator(new AdresseValidator(this.strasse, this.ort, this.plz));
		layout.addComponent(this.plz);
	}
	
	private void fillForm() {
		fillField(this.name, this.person.getName());
		fillField(this.ansprechpartner, this.person.getAnsprechpartner());
		fillField(this.strasse, this.person.getAdresse().getStrasse());
		fillField(this.ort, this.person.getAdresse().getOrt());
		fillField(this.plz, this.person.getAdresse().getPlz(), true);
	}
}
