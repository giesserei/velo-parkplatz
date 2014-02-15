package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.stellplatz.StellplatzViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Stellplatz" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandStellplatz extends BaseCommand {

    @Inject
    private StellplatzViewController ctrl;
    
	public CommandStellplatz(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
	    super.showView(this.ctrl, "header.stellplatz");
	}
	
}
