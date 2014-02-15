package ch.giesserei.view.validator;

import org.apache.commons.lang3.StringUtils;

import ch.giesserei.resource.ValMsg;

import com.vaadin.data.Validator;

/**
 * Validator für die PLZ.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class PlzValidator implements Validator {

	public PlzValidator() {
	}
	
	@Override
    public void validate(Object value) throws InvalidValueException {
	    String valueStr = (String) value;
	    if (StringUtils.isNotBlank(valueStr)) {
    	    try {
    	        int plz = Integer.parseInt(valueStr);
    	        if (plz < 1000 || plz > 9999) {
    	        	throw new InvalidValueException(ValMsg.getString("val.plz.invalid"));
    	        }
            }
            catch (NumberFormatException e) {
    	        throw new InvalidValueException(ValMsg.getString("val.plz.invalid"));
            }
	    }
    }

}
