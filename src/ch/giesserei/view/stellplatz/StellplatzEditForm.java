package ch.giesserei.view.stellplatz;

import ch.giesserei.core.Const;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.resource.AppRes;
import ch.giesserei.view.BaseEditForm;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;

/**
 * Definiert das Formular, mit dem ein Stellplatz bearbeitet werden kann.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class StellplatzEditForm extends BaseEditForm {
	
	private final Stellplatz stellplatz;
	
	private TextField nummer;
	
	private NativeSelect sektor;
	
	private NativeSelect typ;
	
	private FormLayout layout;
	
	private Button saveButton;
	
	/**
	 * Konstruktor. 
	 * 
	 * @param stellplatz Bean, welches bearbeitet werden soll
	 */
	public StellplatzEditForm(Stellplatz stellplatz) {
		this.stellplatz = stellplatz;
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
		this.stellplatz.setNummer(Integer.parseInt(this.nummer.getValue()));
		this.stellplatz.setSektor((Integer) this.sektor.getValue());
		this.stellplatz.setTyp((StellplatzTyp) this.typ.getValue());
	}
	
	// ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
	
	private void createFields(Layout layout) {
		this.nummer = createTextField(layout, "stellplatz.lb.nummer", 10, "val.stellplatz.nummer.not.null", true);
		
		this.sektor = new NativeSelect("stellplatz.lb.sektor");
        for (int i = 1; i <= Const.ANZAHL_STELLPLATZ_SEKTOREN; i++) {
        	this.sektor.addItem(i);
        	this.sektor.setItemCaption(i, AppRes.getString("stellplatz.lb.sektor.option", i));
        }
        this.sektor.setNullSelectionAllowed(false);
        layout.addComponent(this.sektor);
		
		this.typ = new NativeSelect("stellplatz.lb.typ");
        for (StellplatzTyp typ : StellplatzTyp.values()) {
        	this.typ.addItem(typ);
        	this.typ.setItemCaption(typ, AppRes.getString(typ.getResourceKey()));
        }
        this.typ.setNullSelectionAllowed(false);
        layout.addComponent(this.typ);
	}
	
	private void fillForm() {
		fillField(this.nummer, this.stellplatz.getNummer(), true);
		
		if (this.stellplatz.getSektor() == 0) {
			this.sektor.setValue(1);
		}
		else {
			this.sektor.setValue(this.stellplatz.getSektor());
		}
		
		if (this.stellplatz.getTyp() == null) {
			this.typ.setValue(StellplatzTyp.PEDALPARC_HOCH);
		}
		else {
			this.typ.setValue(this.stellplatz.getTyp());
		}
	}
}
