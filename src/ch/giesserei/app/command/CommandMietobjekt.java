package ch.giesserei.app.command;

import ch.giesserei.app.Context;
import ch.giesserei.view.objekt.ObjektViewController;

import com.google.inject.Inject;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Wird ausgeführt, wenn der Benutzer die View "Mieter" aufruft.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandMietobjekt extends BaseCommand {

	@Inject
	private ObjektViewController ctrl;
	
	public CommandMietobjekt(Context context) {
		super(context);
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
		super.showView(this.ctrl, "header.objekte");
	}
	
}
