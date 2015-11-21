package ch.giesserei.resource;

import java.util.Locale;

import ch.giesserei.core.Config;

import com.google.inject.Inject;

/**
 * Die Klasse stellt das Locale für den Benutzer bereit. Bisher immer das fixe App-Locale.
 * 
 * @author Steffen Förster
 */
public class LocaleProvider {

    private final Config config;
    
    @Inject
    public LocaleProvider(Config config) {
        this.config = config;
    }
    
    public Locale getUserLocale() {
        return config.getAppLocale();
    }
    
}
