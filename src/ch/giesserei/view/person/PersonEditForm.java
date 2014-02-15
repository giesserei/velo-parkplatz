package ch.giesserei.view.person;

import org.apache.commons.lang3.StringUtils;

import ch.giesserei.model.Person;
import ch.giesserei.resource.AppRes;
import ch.giesserei.resource.ValMsg;
import ch.giesserei.view.BaseEditForm;
import ch.giesserei.view.validator.AdresseValidator;
import ch.giesserei.view.validator.GeburtsjahrValidator;
import ch.giesserei.view.validator.PlzValidator;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;

/**
 * Definiert das Formular, mit dem eine Person bearbeitet werden kann.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class PersonEditForm extends BaseEditForm {
	
	private final Person person;
	
	private TextField vorname;
	
	private TextField nachname;
	
	private TextField email;
	
	private TextField geburtsjahr;
	
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
	public PersonEditForm(Person person) {
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
		this.person.setVorname(this.vorname.getValue());
		this.person.setNachname(this.nachname.getValue());
		this.person.setEmail(this.email.getValue());
		if (StringUtils.isBlank(this.geburtsjahr.getValue())) {
			this.person.setGeburtsjahr(0);
		}
		else {
			this.person.setGeburtsjahr(Integer.parseInt(this.geburtsjahr.getValue()));
		}
		
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
		this.vorname = createTextField(layout, "person.lb.vorname", 75, "val.vorname.not.null", true);
		this.nachname = createTextField(layout, "person.lb.nachname", 75, "val.nachname.not.null", true);
		
		this.email = createTextField(layout, "person.lb.email", 75, "val.email.not.null", true);
		this.email.addValidator(new EmailValidator(ValMsg.getString("val.email.format")));
		
		this.geburtsjahr = createTextField(layout, "person.lb.geburtsjahr", 4, true);
		this.geburtsjahr.addValidator(new GeburtsjahrValidator());
		
		this.strasse = createTextField(layout, "person.lb.strasse", 100, false);
		this.ort = createTextField(layout, "person.lb.ort", 100, false);
		
		this.plz = createTextField(layout, "person.lb.plz", 4, true);
		this.plz.addValidator(new PlzValidator());
		this.plz.addValidator(new AdresseValidator(this.strasse, this.ort, this.plz));
	}
	
	private void fillForm() {
		fillField(this.vorname, this.person.getVorname());
		fillField(this.nachname, this.person.getNachname());
		fillField(this.email, this.person.getEmail());
		fillField(this.geburtsjahr, this.person.getGeburtsjahr(), true);
		fillField(this.strasse, this.person.getAdresse().getStrasse());
		fillField(this.ort, this.person.getAdresse().getOrt());
		fillField(this.plz, this.person.getAdresse().getPlz(), true);
	}
}
