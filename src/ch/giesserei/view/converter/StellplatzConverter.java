package ch.giesserei.view.converter;

import java.util.Locale;

import ch.giesserei.model.Stellplatz;
import ch.giesserei.resource.AppRes;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter für {@link Stellplatz}.
 * Dies ist nur ein Converter zur Anzeige eines Stellplatzes.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class StellplatzConverter implements Converter<String, Stellplatz> {

    private final Format format;
    
    /**
     * Zeigt die Nummer und den Typ.
     */
    public StellplatzConverter() {
        this(Format.NUMMER_UND_TYP);
    }
    
    /**
     * Zeigt den Stellplatz im übergebenen Format.
     * @param format Format
     */
    public StellplatzConverter(Format format) {
        this.format = format;
    }
    
    @Override
    public Stellplatz convertToModel(String value, Class<? extends Stellplatz> targetType, Locale locale)
            throws ConversionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String convertToPresentation(Stellplatz value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        if (value == null) {
            return "";
        }
        else {
            if (this.format == Format.NUMMER_UND_TYP) {
                String typName = AppRes.getString(value.getTyp().getResourceKey());
                return String.format("%s (%s)", String.valueOf(value.getNummer()), typName);
            }
            else {
                return String.valueOf(value.getNummer());
            }
        }
    }

    @Override
    public Class<Stellplatz> getModelType() {
        return Stellplatz.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
    
    /**
     * Mit dem Format kann die Ausgabe gesteuert werden.
     */
    public enum Format {
        NUMMER_UND_TYP,
        NUMMER
    }
}
