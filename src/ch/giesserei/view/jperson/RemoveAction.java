package ch.giesserei.view.jperson;

import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.model.JuristischePerson;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

/**
 * Das Action wird ausgeführt, wenn ein Datensatz in der Tabelle Juristische Person gelöscht wird.
 * 
 * @author Steffen Förster
 */
public class RemoveAction extends AbstractTableItemAction {

	public RemoveAction(JuristischePersonViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, final Table table, final Object itemId, Object columnId) {
		// Prüfen, ob der Person Verträge zugeordnet sind
		int countVertraege = ServiceLocator.getJuristischePersonService().getCountVertrag((JuristischePerson) itemId);
		if (countVertraege > 0) {
			Notification.show(AppRes.getString("notification.caption.remove.not.possible"),
			        AppRes.getString("msg.juristische.person.has.contract", countVertraege),
					Notification.Type.ERROR_MESSAGE);
		}
		else {
			CrudFormHelper.showRemoveConfirm(new Decision() {
    				@Override
                    public void yes() {
    					try {
                            ServiceLocator.getJuristischePersonService().remove((JuristischePerson) itemId);
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
    			}
    		);
		}
    }
}
