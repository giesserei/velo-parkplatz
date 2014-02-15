package ch.giesserei.view.reservation.personwhg;

import ch.giesserei.resource.AppRes;
import ch.giesserei.view.AbstractClickListener;
import ch.giesserei.view.AbstractViewController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;

/**
 * Dieses Aktion informiert den Controller, dass nach einer Wohnung gefiltert werden soll.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class FilterWohnungAction extends AbstractClickListener {

	public FilterWohnungAction(AbstractViewController viewController) {
		super(viewController);
	}
	
	@Override
    public void buttonClick(ClickEvent event) {
	    if (getCtrl().getView().getWohnungFilter().getValue() == null) {
	        Notification.show(AppRes.getString("notification.caption.no.filter"), Notification.Type.WARNING_MESSAGE);
	    }
	    else {
	        getCtrl().setCurrentFilter(PersonWhgViewController.CurrentFilter.WOHNUNG);
	        getCtrl().updateTableData();
	    }
    }
	
	private PersonWhgViewController getCtrl() {
		return (PersonWhgViewController) getViewController();
	}
}
