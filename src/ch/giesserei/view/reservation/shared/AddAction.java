package ch.giesserei.view.reservation.shared;

import java.util.Date;

import ch.giesserei.core.Const;
import ch.giesserei.model.ReservationStatus;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.util.Utility;
import ch.giesserei.view.AbstractClickListener;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.reservation.position.PositionViewController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Dieses Aktion steuert das Hinzufügen eines neuen Reservierung.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class AddAction extends AbstractClickListener {

	public AddAction(AbstractViewController viewController) {
		super(viewController);
	}
	
	@Override
    public void buttonClick(ClickEvent event) {
		ReservationStellplatz item = new ReservationStellplatz();
		item.setStellplatz(getCtrl().getSelectedStellplatz());
		item.setReservationDatum(new Date());
		item.setBeginnDatum(new Date());
		item.setEndDatumExklusiv(Utility.addYear(new Date()));
		item.setKostenProMonat(getCtrl().getSelectedStellplatz().getTyp() == StellplatzTyp.SPEZIAL 
				? Const.KOSTEN_STELLPLATZ_SPEZIAL 
				: Const.KOSTEN_STELLPLATZ_PEDALPARC);
		
		ReservationEditForm form = new ReservationEditForm(item);		
		Window window = new Window(AppRes.getString("reservation.title.add"));
		window.setModal(true);
		window.setContent(form);
		addSaveAction(window, form, item);
		UI.getCurrent().addWindow(window);
    }
	
	// ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
	
	private void addSaveAction(final Window window, final ReservationEditForm form, final ReservationStellplatz item) {
		form.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
	                form.validate();
	                form.fillBean();
	                
	                ReservationService service = ServiceLocator.getReservationService();
	                ReservationStellplatz persitedItem = service.persist(item);
	                window.close();
	                
	                if (ReservationStatus.isAktivStatus(persitedItem.getReservationStatus())) {
	                	getCtrl().getView().getAktivTable().getContainerDataSource().addItem(persitedItem);
	                	getCtrl().getView().updateTabCaptionAktiv();
	                	getCtrl().getView().getAktivTable().markAsDirtyRecursive();
	                }
	                else {
	                	getCtrl().getView().getInaktivTable().getContainerDataSource().addItem(persitedItem);
	                	getCtrl().getView().updateTabCaptionInaktiv();
	                	getCtrl().getView().getInaktivTable().markAsDirtyRecursive();
	                }
                }
				catch (Exception e) {
	                CrudFormHelper.handleEditError(e);
                }
			}
		});
	}
	
	private PositionViewController getCtrl() {
		return (PositionViewController) getViewController();
	}
}
