package ch.giesserei.view.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ch.giesserei.resource.ValMsg;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter für Datumswerte -> dd.MM.yyyy.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class DatumConverter implements Converter<String, Date> {

    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    
	public DatumConverter() {
	}
	
	@Override
    public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale)
            throws Converter.ConversionException {
		try {
	        return this.format.parse(value);
        }
        catch (ParseException e) {
        	throw new ConversionException(ValMsg.getString("conversion.datum"));
        }
    }

	@Override
    public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale)
            throws Converter.ConversionException {
        return format.format(value);
    }

	@Override
    public Class<Date> getModelType() {
	    return Date.class;
    }

	@Override
    public Class<String> getPresentationType() {
	    return String.class;
    }
	
}
