package ch.giesserei.view.reservation.shared;

import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import ch.giesserei.core.Images;
import ch.giesserei.model.Person;
import ch.giesserei.model.ReservationStatus;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.PersonService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.view.BaseEditForm;
import ch.giesserei.view.converter.PreisConverter;
import ch.giesserei.view.validator.ReservationDatumValidator;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * Definiert das Formular, mit dem eine Reservation für einen Stellplatz bearbeitet werden kann.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ReservationEditForm extends BaseEditForm {
	
	private final ReservationStellplatz reservation;
	
	private PopupDateField datumVon;
	
	private PopupDateField datumBis;
	
	private NativeSelect status;
	
	private TextField kosten;
	
	private ComboBox person;
	
	private TextArea bemerkung;
	
	private CheckBox bezahlt;
	
	private TextField nameWohnungBuchung;
	
	private FormLayout layout;
	
	private Button saveButton;
	
	private Button mailVelogruppeButton;
	
	private Button mailReminderButton;
	
	private Button mailZahlungErhaltenButton;
	
	/**
	 * Konstruktor. 
	 * 
	 * @param reservation Bean, welches bearbeitet werden soll
	 */
	public ReservationEditForm(ReservationStellplatz reservation) {
		this.reservation = reservation;
		this.layout = new FormLayout();
		this.layout.setMargin(true);
		addFields(this.layout);
		addButtons(this.layout);
		fillForm();
		setCompositionRoot(this.layout);
	}
	
	public Button getBtnSave() {
		return this.saveButton;
	}

    public Button getBtnMailVelogruppe() {
        return mailVelogruppeButton;
    }

    public Button getBtnMailReminder() {
        return mailReminderButton;
    }
    
    public Button getBtnMailZahlungErhalten() {
        return mailZahlungErhaltenButton;
    }

    public void fillBean() {
		this.reservation.setKostenProMonat((Double) this.kosten.getPropertyDataSource().getValue());
		this.reservation.setBeginnDatum(this.datumVon.getValue());
		this.reservation.setEndDatumExklusiv(this.datumBis.getValue());
		this.reservation.setReservationStatus((ReservationStatus) this.status.getValue());
		this.reservation.setMieterPerson((Person) this.person.getValue());
		this.reservation.setBemerkung(this.bemerkung.getValue());
		this.reservation.setBezahlt(this.bezahlt.getValue());
	}
	
	// ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
	
	private void addFields(Layout layout) {
		this.datumVon = new PopupDateField(AppRes.getString("reservation.lb.beginn"));
		this.datumVon.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		this.datumVon.setLocale(Locale.GERMAN);
		this.datumVon.setResolution(Resolution.DAY);
		layout.addComponent(this.datumVon);
		
		this.datumBis = new PopupDateField(AppRes.getString("reservation.lb.ende"));
		this.datumBis.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		this.datumBis.setLocale(Locale.GERMAN);
		this.datumBis.setResolution(Resolution.DAY);
		this.datumBis.addValidator(new ReservationDatumValidator(this.datumVon, this.datumBis));
		registerForValidation(this.datumBis);
		layout.addComponent(this.datumBis);
		
		this.status = new NativeSelect(AppRes.getString("reservation.lb.status"));
        for (ReservationStatus status : ReservationStatus.values()) {
        	this.status.addItem(status);
        	this.status.setItemCaption(status, AppRes.getString(status.getResourceKey()));
        }
        this.status.setNullSelectionAllowed(false);
        layout.addComponent(this.status);
		
		this.kosten = createTextField(layout, "reservation.lb.kosten.pro.monat", 10, "val.stellplatz.kosten.not.null", true);
		this.kosten.setConverter(new PreisConverter());
		this.kosten.setConversionError("{1}"); // setzt die ConversionError-Message
		
		this.person = new ComboBox(AppRes.getString("reservation.lb.person"));
		this.person.setInputPrompt(AppRes.getString("reservation.lb.person.select"));
        this.person.setNullSelectionAllowed(false);
        this.person.setNewItemsAllowed(false);
        layout.addComponent(this.person);
        
        this.bemerkung = new TextArea(AppRes.getString("reservation.lb.bemerkung"));
        this.bemerkung.setRows(5);
        this.bemerkung.setSizeFull();
        this.bemerkung.setMaxLength(ReservationStellplatz.LENGTH_BEMERKUNG);
        layout.addComponent(this.bemerkung);
        
        this.bezahlt = new CheckBox(AppRes.getString("reservation.lb.bezahlt"));
        layout.addComponent(this.bezahlt);
        
        this.nameWohnungBuchung = createTextField(layout, "reservation.lb.name.buchung", ReservationStellplatz.LENGTH_NAME, false);
        this.nameWohnungBuchung.setWidth("100%");
	}
	
	private void addButtons(Layout layout) {
	    this.saveButton = new Button(AppRes.getString("btn.save"));
	    this.saveButton.setIcon(Images.getInstance().getImgSave());
	    layout.addComponent(this.saveButton);
	    
	    HorizontalLayout mailBtnLayout = new HorizontalLayout();
	    mailBtnLayout.setSpacing(true);
	    
	    this.mailReminderButton = new Button(AppRes.getString("btn.mail.reminder"));
	    mailBtnLayout.addComponent(this.mailReminderButton);
	    this.mailZahlungErhaltenButton = new Button(AppRes.getString("btn.mail.zahlung.erhalten"));
        mailBtnLayout.addComponent(this.mailZahlungErhaltenButton);
	    this.mailVelogruppeButton = new Button(AppRes.getString("btn.mail.velogruppe"));
	    mailBtnLayout.addComponent(this.mailVelogruppeButton);
	    
	    layout.addComponent(mailBtnLayout);
	}
	
	private void fillForm() {
		final ObjectProperty<Double> property = new ObjectProperty<Double>(this.reservation.getKostenProMonat());
		this.kosten.setPropertyDataSource(property);
		
		this.datumVon.setValue(this.reservation.getBeginnDatum());
		this.datumBis.setValue(this.reservation.getEndDatumExklusiv());				
		
		if (this.reservation.getReservationStatus() == null) {
			this.status.setValue(ReservationStatus.RESERVIERT);
		}
		else {
			this.status.setValue(this.reservation.getReservationStatus());
		}
		
		PersonService service = ServiceLocator.getPersonService();
		this.person.setContainerDataSource(service.getPersonen());
		this.person.setItemCaptionPropertyId(Person.PROPERTY_NAME_WOHNUNG);
		this.person.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		this.person.setFilteringMode(FilteringMode.CONTAINS);
		if (this.reservation.getMieterPerson() != null) {
		    this.person.setValue(this.reservation.getMieterPerson());
		}
		
		this.bemerkung.setValue(this.reservation.getBemerkung() == null ? "" : this.reservation.getBemerkung());
		this.bezahlt.setValue(this.reservation.isBezahlt());
		
		// Nur bei einer anonymer Reservation sind Name und Wohnungsnummer abgefüllt
		// Die Anzeige erleichtet die Zuordnung zu einer Person
		this.nameWohnungBuchung.setReadOnly(false);
		if (StringUtils.isNotBlank(this.reservation.getName())) {
		    String value = String.format("%s (%s)", this.reservation.getName(), String.valueOf(this.reservation.getWohnungNr()));
		    this.nameWohnungBuchung.setValue(value);
		}
		else {
		    this.nameWohnungBuchung.setValue("");
		}
		this.nameWohnungBuchung.setReadOnly(true);
	}
}
