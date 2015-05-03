package ch.giesserei.injection;

import javax.persistence.EntityManager;

import ch.giesserei.calendar.CalendarRenderProvider;
import ch.giesserei.calendar.SvgCalendarRenderProvider;
import ch.giesserei.core.Config;
import ch.giesserei.core.Images;
import ch.giesserei.core.MailProvider;
import ch.giesserei.core.MailProviderImpl;
import ch.giesserei.filter.JpaFilter;
import ch.giesserei.imp.ImportMietobjekt;
import ch.giesserei.imp.ImportReservation;
import ch.giesserei.imp.ImportStellplatz;
import ch.giesserei.imp.SyncPerson;
import ch.giesserei.resource.LocaleProvider;
import ch.giesserei.service.JuristischePersonService;
import ch.giesserei.service.MietobjektService;
import ch.giesserei.service.PersonService;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.service.StellplatzService;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Module definiert die Dependencies, die in der Anwendung verwendet werden.
 * 
 * @author Steffen Förster
 */
public class CoreModule extends AbstractModule {

    /**
     * Konstruktor.
     */
    public CoreModule() {
    }
    
    @Override
    protected void configure() {
        bind(LocaleProvider.class);
        bind(PersonService.class);
        bind(MietobjektService.class);
        bind(JuristischePersonService.class);
        bind(StellplatzService.class);
        bind(ReservationService.class);   
        bind(Images.class);
        bind(Config.class);
        bind(MailProvider.class).to(MailProviderImpl.class);
        bind(ImportMietobjekt.class);
        bind(SyncPerson.class);
        bind(ImportReservation.class);
        bind(ImportStellplatz.class);
        bind(CalendarRenderProvider.class).to(SvgCalendarRenderProvider.class);
        
        requestInjection(ServiceLocator.getInstance());
    }
    
    @Provides
    protected EntityManager getEntityManager() {
        return JpaFilter.getEntityManager();
    }
}
