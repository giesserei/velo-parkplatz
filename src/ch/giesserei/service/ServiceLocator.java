package ch.giesserei.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ServiceLocator {

    private static final ServiceLocator INSTANCE = new ServiceLocator();
    
    /**
     * Der {@link ServiceLocator} ist ein Singleton. Durch Guice werden nur die Provider injiziert. So ist sichergestellt,
     * dass der Locator immer die korrekten Service-Instanzen liefert, unabhängig davon, ob die Services selbst
     * Singletons sind. Auch ist so das Ersetzen der Services durch Mocks möglich.
     */
    
    @Inject
    private Provider<PersonService> personService;
    
    @Inject
    private Provider<JuristischePersonService> jpersonService;
    
    @Inject
    private Provider<MietobjektService> objektService;
    
    @Inject
    private Provider<StellplatzService> stellplatzService;
    
    @Inject
    private Provider<ReservationService> reservationService;
    
    private ServiceLocator() {
    }
    
    public static ServiceLocator getInstance() {
    	return INSTANCE;
    }
    
    public static PersonService getPersonService() {
        return INSTANCE.personService.get();
    }
    
    public static JuristischePersonService getJuristischePersonService() {
        return INSTANCE.jpersonService.get();
    }
    
    public static MietobjektService getObjektService() {
    	return INSTANCE.objektService.get();
    }
    
    public static StellplatzService getStellplatzService() {
    	return INSTANCE.stellplatzService.get();
    }
    
    public static ReservationService getReservationService() {
    	return INSTANCE.reservationService.get();
    }
    
}
