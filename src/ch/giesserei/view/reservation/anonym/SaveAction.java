package ch.giesserei.view.reservation.anonym;

import static ch.giesserei.model.ReservationStellplatz.PROPERTY_RESERVATION_DATUM;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

/**
 * Das Action wird ausgeführt, wenn eine anonyme Reservation gespeichert werden soll.
 * 
 * @author Steffen Förster
 */
public class SaveAction extends AbstractTableItemAction {

	public SaveAction(AnonymViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, final Table table, final Object itemId, Object columnId) {
    	try {
    	    ReservationStellplatz reservation = (ReservationStellplatz) itemId;
    	    reservation.setAnonym(false);
    	    ServiceLocator.getReservationService().save(reservation);
    	    // gespeicherte Reservation ausblenden
    	    table.removeItem(itemId);
            //table.markAsDirtyRecursive();
    	    table.setColumnFooter(PROPERTY_RESERVATION_DATUM, AppRes.getString("table.row.count", table.getContainerDataSource().size()));
            
            Notification.show(
                    AppRes.getString("reservation.notification.person.saved"),
                    Notification.Type.TRAY_NOTIFICATION);
        }
        catch (Exception e) {
            CrudFormHelper.handleEditError(e);
        } 
    }
}
