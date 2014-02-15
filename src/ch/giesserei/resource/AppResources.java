package ch.giesserei.resource;

import java.util.Locale;

/**
 * Hilfsklasse für den Zugriff auf die Ressourcen.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class AppResources extends FixedResources {

    /**
     * Konstruktor.
     * 
     * @param locale Sprache des Benutzers
     */
    public AppResources(Locale locale) {
        super(AppResources.class, locale);
    }
    
}
