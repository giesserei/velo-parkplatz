package ch.giesserei.view.validator;

import org.apache.commons.lang3.StringUtils;

import ch.giesserei.resource.ValMsg;

import com.vaadin.data.Validator;
import com.vaadin.ui.TextField;

/**
 * Validator für die Adresse. Wenn ein Feld ausgefüllt ist, müssen alle Felder erfasst sein.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class AdresseValidator implements Validator {

    private final TextField strasse;
    
    private final TextField ort;
    
    private final TextField plz;
    
    public AdresseValidator(TextField strasse, TextField ort, TextField plz) {
        this.strasse = strasse;
        this.ort = ort;
        this.plz = plz;
    }
    
    @Override
    public void validate(Object value) throws InvalidValueException {
        boolean strasseExists = StringUtils.isNotBlank(this.strasse.getValue());
        boolean ortExists = StringUtils.isNotBlank(this.ort.getValue());
        boolean plzExists = StringUtils.isNotBlank(this.plz.getValue());
        
        if (strasseExists || ortExists || plzExists) {
            if (!strasseExists || !ortExists || !plzExists) {
                throw new InvalidValueException(ValMsg.getString("val.adresse.incomplete"));
            }
        }
    }
    
}
