package ch.giesserei.view.reservation.shared;

import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.reservation.ReservationViewController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;

/**
 * Das Action wird ausgeführt, wenn eine Reservation gelöscht wird.
 * 
 * @author Steffen Förster
 */
public class RemoveAction extends AbstractTableItemAction {

	public RemoveAction(AbstractViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, final Table table, final Object itemId, Object columnId) {
		CrudFormHelper.showRemoveConfirm(new Decision() {
				@Override
                public void yes() {
					try {
                        ServiceLocator.getReservationService().remove((ReservationStellplatz) itemId);
                        
                        // Reservationen neu laden
                        ReservationViewController ctrl = (ReservationViewController) getController();
                        ctrl.updateTableData();
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
