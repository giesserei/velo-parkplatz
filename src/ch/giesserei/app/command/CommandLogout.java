package ch.giesserei.app.command;

import java.io.Serializable;

import ch.giesserei.auth.Auth;
import ch.giesserei.core.WebUtils;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

/**
 * Wird ausgeführt, wenn der Benutzer sich abmeldet.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class CommandLogout implements Command, Serializable {

	public CommandLogout() {
	}
	
	@Override
    public void menuSelected(MenuItem selectedItem) {
		Auth.logout();
		UI.getCurrent().getSession().close();
        // Redirect to avoid keeping the removed UI open in the browser
		UI.getCurrent().getPage().setLocation(WebUtils.getPathLogin());
	}
	
}
