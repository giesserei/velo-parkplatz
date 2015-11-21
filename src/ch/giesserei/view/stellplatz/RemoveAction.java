package ch.giesserei.view.stellplatz;

import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

/**
 * Das Action wird ausgeführt, wenn ein Datensatz in der Tabelle Stellplatz gelöscht wird.
 * 
 * @author Steffen Förster
 */
public class RemoveAction extends AbstractTableItemAction {

	public RemoveAction(StellplatzViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, final Table table, final Object itemId, Object columnId) {
		// Prüfen, ob dem Stellplatz Reservationen zugeordnet sind
		int countReservationen = ServiceLocator.getStellplatzService().getCountReservation((Stellplatz) itemId);
		if (countReservationen > 0) {
			Notification.show(AppRes.getString("notification.caption.remove.not.possible"),
			        AppRes.getString("msg.stellplatz.has.reservation", countReservationen),
					Notification.Type.ERROR_MESSAGE);
		}
		else {
			CrudFormHelper.showRemoveConfirm(new Decision() {
    				@Override
                    public void yes() {
    					try {
                            ServiceLocator.getStellplatzService().remove((Stellplatz) itemId);
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
