package ch.giesserei.app.command;

import java.io.Serializable;

import ch.giesserei.imp.SyncPerson;
import ch.giesserei.injection.Injection;
import ch.giesserei.resource.AppRes;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;

/**
 * Wird ausgeführt, wenn die Personen mit einer Datenquelle synchronisiert werden sollen.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandSynchPersonen implements Command, Serializable {

    @Inject
    private SyncPerson importPersonen;
    
	public CommandSynchPersonen() {
	    Injection.injectMembers(this);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
		int count = this.importPersonen.syncPersonen();
		
		Notification.show(
                AppRes.getString("admin.notification.caption.sync.personen.finished", count),
                Notification.Type.HUMANIZED_MESSAGE);
	}
	
}
