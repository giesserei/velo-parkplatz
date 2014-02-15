package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.reservation.position.PositionViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Reservation je Stellplatz" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandReservationPosition extends BaseCommand {

    @Inject
    private PositionViewController ctrl;
    
	public CommandReservationPosition(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
		super.showView(this.ctrl, "header.reservation.position");
	}
	
}
