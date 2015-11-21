package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.jperson.JuristischePersonViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Juristische Personen" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandJuristischePerson extends BaseCommand {

	@Inject
	private JuristischePersonViewController ctrl;
	
	public CommandJuristischePerson(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
		super.showView(this.ctrl, "-");
    }
	
}
