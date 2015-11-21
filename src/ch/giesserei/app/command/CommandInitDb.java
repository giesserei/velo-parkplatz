package ch.giesserei.app.command;

import java.io.Serializable;

import ch.giesserei.imp.ImportMietobjekt;
import ch.giesserei.imp.ImportReservation;
import ch.giesserei.imp.ImportStellplatz;
import ch.giesserei.imp.SyncPerson;
import ch.giesserei.injection.Injection;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.service.StellplatzService;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;

/**
 * Wird ausgeführt, wenn der Benutzer die Datenbank initialisieren möchte.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandInitDb implements Command, Serializable {

    @Inject
    private SyncPerson syncPersonen;
    
    @Inject
    private ImportReservation importReservation;
    
    @Inject
    private ImportMietobjekt importMietobjekt;
    
    @Inject
    private ImportStellplatz importStellplatz;
   
    private final StellplatzService stellplatzService = ServiceLocator.getStellplatzService();
    
	public CommandInitDb() {
	    Injection.injectMembers(this);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
	    // Prüfen, ob die DB bereits initialisiert ist
	    if (this.stellplatzService.getStellplatz(1, StellplatzTyp.SPEZIAL) != null) {
	        Notification.show(
	                AppRes.getString("admin.notification.caption.init.initialzed"),
	                Notification.Type.WARNING_MESSAGE);
	    }
	    else {
    	    this.importStellplatz.importStellplaetze();
    	    this.importMietobjekt.importMietobjekte();
    	    this.syncPersonen.syncPersonen();
    	    this.importReservation.importReservationen();
    	    
    		Notification.show(
    		        AppRes.getString("admin.notification.caption.init.finished"),
                    Notification.Type.HUMANIZED_MESSAGE);
	    }
	}
}
