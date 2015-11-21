package ch.giesserei.injection;

import javax.servlet.ServletContext;

import ch.giesserei.core.Config;
import ch.giesserei.core.FreeMarkerProvider;
import ch.giesserei.core.TemplateProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Module definiert die Dependencies, die von der FreeMarker-Integration verwendet werden.
 * 
 * @author Steffen Förster
 */
public class FreeMarkerModule extends AbstractModule {

    private final ServletContext context;
    
    /**
     * Konstruktor.
     */
    public FreeMarkerModule(ServletContext context) {
        this.context = context;
    }
    
    @Override
    protected void configure() {
    }
    
    @Provides
    protected TemplateProvider createFreeMarker(Config config) {
        return new FreeMarkerProvider(this.context, config);
    }
}
