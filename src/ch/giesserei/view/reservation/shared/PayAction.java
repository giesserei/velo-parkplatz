package ch.giesserei.view.reservation.shared;

import ch.giesserei.model.Person;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.reservation.ReservationViewController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;

/**
 * Das Action wird ausgeführt, wenn der Bezahlstatus einer Reservation verändert wird.
 * 
 * @author Steffen Förster
 */
public class PayAction extends AbstractTableItemAction {

	public PayAction(AbstractViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, Table source, Object itemId, Object columnId) {
	    try {
    		ReservationStellplatz item = (ReservationStellplatz) itemId;
    		if (item.getMieterPerson() == Person.ANONYM) {
    		    Notification.show(AppRes.getString("reservation.notification.person.nicht.zugeordnet"), 
    		            Type.WARNING_MESSAGE);
    		}
    		else {
        		item.setBezahlt(!item.isBezahlt());
        		ReservationService service = ServiceLocator.getReservationService();
                service.save(item);
                ReservationViewController ctrl = (ReservationViewController) getController();
                ctrl.updateTableData();
    		}
	    }
        catch (Exception e) {
            CrudFormHelper.handleEditError(e);
        }
    }
}
