package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.reservation.rollover.RolloverViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Reservationen verlängern" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandReservationRollover extends BaseCommand {

    @Inject
    private RolloverViewController ctrl;
    
	public CommandReservationRollover(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
	    super.showView(this.ctrl, "header.reservation.rollover");
	}
	
}
