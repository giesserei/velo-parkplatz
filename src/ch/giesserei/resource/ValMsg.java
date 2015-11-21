package ch.giesserei.resource;

import ch.giesserei.injection.Injection;

/**
 * Hilfsklasse für den Zugriff auf die ValidationMessages von der Vaadin-GUI.
 * 
 * @author Steffen Förster
 */
public class ValMsg {

    private ValMsg() {
    }
    
    public static String getString(String key) {
        return getValMsg().getString(key);
    }
    
    public static String getString(String key, Object ... params) {
        return getValMsg().getString(key, params);
    }
    
    private static ValidationMessages getValMsg() {
        return Injection.get(ValidationMessages.class);
    }
    
}
