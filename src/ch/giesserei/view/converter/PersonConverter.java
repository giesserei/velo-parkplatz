package ch.giesserei.view.converter;

import java.util.Locale;

import ch.giesserei.model.Person;
import ch.giesserei.resource.AppRes;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter für {@link Person}.
 * Dies ist nur ein Converter zur Anzeige einer Person.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class PersonConverter implements Converter<String, Person> {

	public PersonConverter() {
	}
	
	@Override
    public Person convertToModel(String value, Class<? extends Person> targetType, Locale locale)
            throws Converter.ConversionException {
	    throw new UnsupportedOperationException();
    }

	@Override
    public String convertToPresentation(Person value, Class<? extends String> targetType, Locale locale)
            throws Converter.ConversionException {
	    if (value == null) {
	        return "";
	    }
	    else {
	        if (value == Person.ANONYM) {
	            return AppRes.getString("reservation.lb.anonym");
	        }
	        else {
	            return value.getNameVollstaendig();
	        }
	    }
    }

	@Override
    public Class<Person> getModelType() {
	    return Person.class;
    }

	@Override
    public Class<String> getPresentationType() {
	    return String.class;
    }
	
}
