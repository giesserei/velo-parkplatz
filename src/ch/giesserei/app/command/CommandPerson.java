package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.person.PersonViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Personen" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandPerson extends BaseCommand {

	@Inject
	private PersonViewController ctrl; 
	
	public CommandPerson(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
		super.showView(this.ctrl, "header.personen");
    }
	
}
