package ch.giesserei.resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Basisklasse für den Zugriff auf die Resourcen, wobei das verwendete Local für eine Instanz fix ist.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public abstract class FixedResources implements Serializable {
   
    protected static final Locale DEFAULT_LOCALE = Locale.GERMAN;
    
    private static final int COUNT_LANGUAGES = 2;

    private final Map<Locale, ResourceBundle> bundles;
    
    private Locale locale;
    
    /**
     * Konstruktor.
     * 
     * @param resourceClass Resource-Klasse
     */ 
	@SuppressWarnings("rawtypes")
    private FixedResources(Class resourceClass) {
    	this.bundles = new HashMap<Locale, ResourceBundle>(COUNT_LANGUAGES);
    	this.bundles.put(Locale.GERMAN, 
                ResourceBundle.getBundle(resourceClass.getName(), Locale.GERMAN, new Utf8Control()));
 //   	this.bundles.put(Locale.ENGLISH, 
 //               ResourceBundle.getBundle(resourceClass.getName(), Locale.ENGLISH, new Utf8Control()));
    }
    
    /**
     * Konstruktor.
     * 
     * @param resourceClass Resource-Klasse
     * @param locale Locale, welches für den Zugriff auf die Resources verwendet werden soll
     */
	@SuppressWarnings("rawtypes")
	protected FixedResources(Class resourceClass, Locale locale) {
    	this(resourceClass);
        this.locale = locale;
    }
    
    /**
     * Liefert einen Resource-String.
     * 
     * @param key Resource-Schlüssel
     * 
     * @return Resource-String zum übergebenen Schlüssel
     */
    public String getString(String key) {
        ResourceBundle bundle = getResourceBundle(this.locale);
        
        String result = "??? " + key + " ???";
        try {
            if (bundle != null) {
                result = bundle.getString(key);
            }
        } catch (MissingResourceException e) {
        }
        
        return result;
    }
    
    /**
     * Liefert einen Resource-String.
     * 
     * @param key Resource-Schlüssel
     * @param args Parameter, die in den Resource-String eingefügt werden sollen.
     * 
     * @return Resource-String zum übergebenen Schlüssel
     */
    public String getString(String key, Object ... args) {
        String result = getString(key);
        return MessageFormatHelper.format(result, args);
    }
    
    /**
     * Liefert das Locale-Objekt für die Benutzersprache.
     * 
     * @return Locale, welches der Benutzersprache entspricht
     */
    public Locale getLocale() {
    	return this.locale;
    }
    
    private ResourceBundle getResourceBundle(Locale loc) {
        ResourceBundle bundle = this.bundles.get(loc);
        if (bundle == null) {
            bundle = this.bundles.get(DEFAULT_LOCALE);
        }
        
        return bundle;
    }
}