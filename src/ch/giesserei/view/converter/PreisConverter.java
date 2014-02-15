package ch.giesserei.view.converter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import ch.giesserei.resource.ValMsg;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter für Preis-Werte der Form "#0.00".
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class PreisConverter implements Converter<String, Double> {

	public PreisConverter() {
	}
	
	@Override
    public Double convertToModel(String value, Class<? extends Double> targetType, Locale locale)
            throws Converter.ConversionException {
		try {
	        DecimalFormat format = new DecimalFormat("#0.00");
	        Number number = format.parse(value);
	        return number.doubleValue();
        }
        catch (ParseException e) {
        	throw new ConversionException(ValMsg.getString("conversion.preis"));
        }
    }

	@Override
    public String convertToPresentation(Double value, Class<? extends String> targetType, Locale locale)
            throws Converter.ConversionException {
		DecimalFormat format = new DecimalFormat("#0.00");
        return format.format(value);
    }

	@Override
    public Class<Double> getModelType() {
	    return Double.class;
    }

	@Override
    public Class<String> getPresentationType() {
	    return String.class;
    }
	
}
