package ch.giesserei.view.validator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.giesserei.resource.ValMsg;
import ch.giesserei.util.Utility;

import com.vaadin.data.Validator;
import com.vaadin.ui.PopupDateField;

/**
 * Validator stellt sicher, dass der Beginn einer Reservierung vor dem Ende liegt.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ReservationDatumValidator implements Validator {

    private final PopupDateField datumVon;
        
    private final PopupDateField datumBis;
    
    public ReservationDatumValidator(PopupDateField datumVon, PopupDateField datumBis) {
        this.datumVon = datumVon;
        this.datumBis = datumBis;
    }
    
    @Override
    public void validate(Object value) throws InvalidValueException {
        Calendar calVon = new GregorianCalendar();
        calVon.setTime(Utility.stripTime(this.datumVon.getValue()));
        
        Calendar calBis = new GregorianCalendar();
        calBis.setTime(Utility.stripTime(this.datumBis.getValue()));
        
        if (!calBis.after(calVon)) {
            throw new InvalidValueException(ValMsg.getString("val.zeitraum"));
        }
    }

}
