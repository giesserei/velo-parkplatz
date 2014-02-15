package ch.giesserei.resource;

import java.util.Locale;

/**
 * Hilfsklasse für den Zugriff auf die Ressourcen.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ValidationMessages extends FixedResources  {

	/**
     * Konstruktor.
     * 
     * @param locale Sprache des Benutzers
     */
    public ValidationMessages(Locale locale) {
        super(ValidationMessages.class, locale);
    }
	
}
