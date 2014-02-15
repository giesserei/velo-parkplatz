package ch.giesserei.view.validator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import ch.giesserei.resource.ValMsg;

import com.vaadin.data.Validator;

/**
 * Validator für das Geburtsjahr.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class GeburtsjahrValidator implements Validator {

	
	public GeburtsjahrValidator() {
	}
	
	@Override
    public void validate(Object value) throws InvalidValueException {
	    String valueStr = (String) value;
	    if (StringUtils.isNotBlank(valueStr)) {
    	    try {
    	        int geburtsjahr = Integer.parseInt(valueStr);
    	        Calendar cal = new GregorianCalendar();
    	        int year = cal.get(Calendar.YEAR);
    	        if (geburtsjahr < (year - 150) || geburtsjahr > year) {
    	        	throw new InvalidValueException(ValMsg.getString("val.geburtsjahr.invalid"));
    	        }
            }
            catch (NumberFormatException e) {
    	        throw new InvalidValueException(ValMsg.getString("val.geburtsjahr.invalid"));
            }
	    }
    }

}
