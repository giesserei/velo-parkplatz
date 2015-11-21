package ch.giesserei.view.converter;

import java.util.Locale;

import ch.giesserei.model.ReservationStatus;
import ch.giesserei.resource.AppRes;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter für {@link ReservationStatus} Werte.
 * Dies ist nur ein Converter zur Anzeige.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ReservationStatusConverter implements Converter<String, ReservationStatus> {

	public ReservationStatusConverter() {
	}
	
	@Override
    public ReservationStatus convertToModel(String value, Class<? extends ReservationStatus> targetType, Locale locale)
            throws Converter.ConversionException {
	    throw new UnsupportedOperationException();
    }

	@Override
    public String convertToPresentation(ReservationStatus value, Class<? extends String> targetType, Locale locale)
            throws Converter.ConversionException {
        return AppRes.getString(value.getResourceKey());
    }

	@Override
    public Class<ReservationStatus> getModelType() {
	    return ReservationStatus.class;
    }

	@Override
    public Class<String> getPresentationType() {
	    return String.class;
    }
	
}
