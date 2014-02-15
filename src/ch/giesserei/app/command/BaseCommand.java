package ch.giesserei.app.command;

import java.io.Serializable;

import ch.giesserei.app.Context;
import ch.giesserei.injection.Injection;
import ch.giesserei.view.AbstractViewController;

import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar.Command;

/**
 * Basisklasse für die Menü-Commands.
 *  
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public abstract class BaseCommand implements Command, Serializable{

    private final Context context;
    
    protected BaseCommand(Context context) {
        Injection.injectMembers(this);
        this.context = context;
    }
    
    public void showView(AbstractViewController ctrl, String key) {
        Component view = ctrl.createView();
        this.context.getMainComponent().getContentComponent().setView(view, key);
    }
    
}
