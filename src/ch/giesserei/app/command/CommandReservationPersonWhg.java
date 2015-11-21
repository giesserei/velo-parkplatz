package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.reservation.personwhg.PersonWhgViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Reservation je Stellplatz" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandReservationPersonWhg extends BaseCommand {

    @Inject
    private PersonWhgViewController ctrl;
    
	public CommandReservationPersonWhg(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
		super.showView(this.ctrl, "header.reservation.person");
	}
	
}
