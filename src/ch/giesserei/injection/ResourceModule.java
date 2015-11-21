package ch.giesserei.injection;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.MessageInterpolator;

import org.apache.bval.jsr303.DefaultMessageInterpolator;

import ch.giesserei.resource.AppResources;
import ch.giesserei.resource.LocaleProvider;
import ch.giesserei.resource.ValidationMessages;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Module definiert die Resource-Dependencies.
 * 
 * @author Steffen Förster
 */
public class ResourceModule extends AbstractModule {

    private final Map<Locale, DefaultMessageInterpolator> interpolators;
    
    private final Map<Locale, AppResources> appResources;
    
    private final Map<Locale, ValidationMessages> valMessages;
    
    /**
     * Konstruktor.
     */
    public ResourceModule() {
    	this.interpolators = new HashMap<Locale, DefaultMessageInterpolator>();
    	this.appResources = new HashMap<Locale, AppResources>();
    	this.valMessages = new HashMap<Locale, ValidationMessages>();
    }
    
    @Override
    protected void configure() {
    }
    
    @Provides
    protected MessageInterpolator createMessageInterpolator(LocaleProvider provider) {
        if (this.interpolators.get(provider.getUserLocale()) == null) {
            DefaultMessageInterpolator defaultInterpolator = new DefaultMessageInterpolator(
                    ResourceBundle.getBundle(ValidationMessages.class.getName(), provider.getUserLocale()));
            this.interpolators.put(provider.getUserLocale(), defaultInterpolator);
        }
        return this.interpolators.get(provider.getUserLocale());
    }
    
    @Provides
    protected AppResources createAppResources(LocaleProvider provider) {
    	if (this.appResources.get(provider.getUserLocale()) == null) {
    		AppResources resources = new AppResources(provider.getUserLocale());
    		this.appResources.put(provider.getUserLocale(), resources);
    	}
    	return this.appResources.get(provider.getUserLocale());
    }
    
    @Provides
    protected ValidationMessages createValMessages(LocaleProvider provider) {
    	if (this.valMessages.get(provider.getUserLocale()) == null) {
    		ValidationMessages resources = new ValidationMessages(provider.getUserLocale());
    		this.valMessages.put(provider.getUserLocale(), resources);
    	}
    	return this.valMessages.get(provider.getUserLocale());
    }
}
