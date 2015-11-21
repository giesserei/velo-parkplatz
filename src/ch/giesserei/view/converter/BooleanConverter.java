package ch.giesserei.view.converter;

import java.util.Locale;

import ch.giesserei.resource.AppRes;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter für Boolean-Werte.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class BooleanConverter implements Converter<String, Boolean> {

	public BooleanConverter() {
	}
	
	@Override
    public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale)
            throws Converter.ConversionException {
	    return AppRes.getString("lb.yes").equals(value);
    }

	@Override
    public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale)
            throws Converter.ConversionException {
        return value ? AppRes.getString("lb.yes") : AppRes.getString("lb.no");
    }

	@Override
    public Class<Boolean> getModelType() {
	    return Boolean.class;
    }

	@Override
    public Class<String> getPresentationType() {
	    return String.class;
    }
	
}
