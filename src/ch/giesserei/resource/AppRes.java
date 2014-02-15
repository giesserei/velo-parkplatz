package ch.giesserei.resource;

import ch.giesserei.injection.Injection;

/**
 * Hilfsklasse für den Zugriff auf die AppResources von der Vaadin-GUI.
 * 
 * @author Steffen Förster
 */
public class AppRes {

    private AppRes() {
    }
    
    public static String getString(String key) {
        return getAppRes().getString(key);
    }
    
    public static String getString(String key, Object ... params) {
        return getAppRes().getString(key, params);
    }
    
    private static AppResources getAppRes() {
        return Injection.get(AppResources.class);
    }
    
}
