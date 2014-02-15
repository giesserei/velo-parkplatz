package ch.giesserei.view.validator;

import ch.giesserei.core.Const;
import ch.giesserei.resource.ValMsg;

import com.vaadin.data.Validator;

/**
 * Validator für die Nummer eines Stellplatzes.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class StellplatzNummerValidator implements Validator {

	public StellplatzNummerValidator() {
	}
	
	@Override
    public void validate(Object value) throws InvalidValueException {
	    String valueStr = (String) value;
	    try {
	        int nummer = Integer.parseInt(valueStr);
	        if (nummer < 1 || nummer > Const.MAX_NUMMER_STELLPLATZ) {
	        	throw new InvalidValueException(ValMsg.getString("val.stellplatz.nummer.invalid"));
	        }
        }
        catch (NumberFormatException e) {
	        throw new InvalidValueException(ValMsg.getString("val.stellplatz.nummer.invalid"));
        }
    }

}
