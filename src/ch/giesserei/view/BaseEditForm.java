package ch.giesserei.view;

import java.util.ArrayList;
import java.util.List;

import ch.giesserei.resource.AppRes;
import ch.giesserei.resource.ValMsg;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;

/**
 * Basisklasse für ein CRUD-Form.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class BaseEditForm extends CustomComponent {

	@SuppressWarnings("rawtypes")
    private final List<Field> fieldsToValidate = new ArrayList<Field>();
	
	public BaseEditForm() {
		super();
	}
	
	/**
	 * Validiert die Feldinhalte.
	 * 
	 * @throws InvalidValueException Wenn ein Feld einen falschen Inhalt enthält
	 */
	@SuppressWarnings("rawtypes")
    public void validate() throws InvalidValueException {
		for (Field field : this.fieldsToValidate) {
			field.validate();
		}
	}
	
	/**
	 * Registriert ein Feld für die Validierung.
	 * 
	 * @param field Feld
	 */
	protected void registerForValidation(Field<?> field) {
	    this.fieldsToValidate.add(field);
	}
	
	/**
	 * Erstellt ein obligatorischen TextField und fügt es dem übergebenen Layout hinzu.
	 * 
	 * @param layout Layout, in welches das TextField eingefügt werden soll
	 * @param captionKey Ressource-Key für den Feldnamen
	 * @param maxLength Maximale Eingabe-Länge
	 * @param requiredErrorKey Ressource-Key für die Validierungsmeldung "required field"
	 * @param validate wenn true, wird das Feld validiert
	 * 
	 * @return das erstellte TextField
	 */
	protected TextField createTextField(Layout layout, String captionKey, int maxLength, 
			String requiredErrorKey, boolean validate) {
		TextField field = new TextField(AppRes.getString(captionKey));
		field.setMaxLength(maxLength);
		field.setRequired(true);
		field.setRequiredError(ValMsg.getString(requiredErrorKey));
		if (validate) {
			this.fieldsToValidate.add(field);
		}
		layout.addComponent(field);
		return field;
	}
	
	/**
	 * Erstellt ein TextField und fügt es dem übergebenen Layout hinzu.
	 * 
	 * @param layout Layout, in welches das TextField eingefügt werden soll
	 * @param captionKey Ressource-Key für den Feldnamen
	 * @param maxLength Maximale Eingabe-Länge
	 * @param validate wenn true, wird das Feld validiert
	 * 
	 * @return das erstellte TextField
	 */
	protected TextField createTextField(Layout layout, String captionKey, int maxLength, boolean validate) {
		TextField field = new TextField(AppRes.getString(captionKey));
		field.setMaxLength(maxLength);
		if (validate) {
			this.fieldsToValidate.add(field);
		}
		layout.addComponent(field);
		return field;
	}

	/**
	 * Weist dem TextField den Wert {@code value} zu. Ist {@code value} gleich
	 * {@code null} wird ein Leerstring zugewiesen.
	 * 
	 * @param field Text-Feld
	 * @param value Wert
	 */
	protected void fillField(TextField field, String value) {
		field.setValue(value == null ? "" : value);
	}
	
	/**
	 * Weist dem TextField den Wert {@code value} zu. 
	 * 
	 * @param field Text-Feld
	 * @param value Wert
	 * @param zeroToEmpty wenn true, wird ein Leerstring zugewiesen, wenn {@code value} 0 ist
	 */
	protected void fillField(TextField field, int value, boolean zeroToEmpty) {
		if (value == 0 && zeroToEmpty) {
			field.setValue("");
		}
		else {
			field.setValue("" + value);
		}
	}
}