package ch.giesserei.view.person;

import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.model.Person;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

/**
 * Das Action wird ausgeführt, wenn ein Datensatz in der Tabelle Person gelöscht wird.
 * 
 * @author Steffen Förster
 */
public class RemoveAction extends AbstractTableItemAction {

	public RemoveAction(PersonViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, final Table table, final Object itemId, Object columnId) {
		// Prüfen, ob der Person Verträge zugeordnet sind
		int countVertraege = ServiceLocator.getPersonService().getCountVertrag((Person) itemId);
		if (countVertraege > 0) {
			Notification.show(AppRes.getString("notification.caption.remove.not.possible"),
					AppRes.getString("msg.person.has.contract", countVertraege),
					Notification.Type.ERROR_MESSAGE);
		}
		else {
			CrudFormHelper.showRemoveConfirm(new Decision() {
    				@Override
                    public void yes() {
    					try {
                            ServiceLocator.getPersonService().remove((Person) itemId);
                            table.removeItem(itemId);
    						table.markAsDirtyRecursive();
                        }
                        catch (Exception e) {
                            CrudFormHelper.handleEditError(e);
                        } 
                    }
    
    				@Override
                    public void no() {
                    }
			});
		}
    }
}
