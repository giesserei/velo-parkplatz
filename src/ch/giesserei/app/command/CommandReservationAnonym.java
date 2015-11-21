package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.reservation.anonym.AnonymViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Reservationen ohne Personen" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandReservationAnonym extends BaseCommand {

    @Inject
    private AnonymViewController ctrl;
    
	public CommandReservationAnonym(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
	    super.showView(this.ctrl, "header.reservation.anonym");
	}
	
}
