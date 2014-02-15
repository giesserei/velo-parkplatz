package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.reservation.overdue.OverdueViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Reservation nicht bezahlt" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandReservationOverdue extends BaseCommand {

    @Inject
    private OverdueViewController ctrl;
    
	public CommandReservationOverdue(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
	    super.showView(this.ctrl, "header.reservation.overdue");
	}
	
}
