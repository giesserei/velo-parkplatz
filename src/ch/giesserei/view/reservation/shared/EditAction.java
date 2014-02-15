package ch.giesserei.view.reservation.shared;

import ch.giesserei.core.MailProvider;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.util.Utility;
import ch.giesserei.view.AbstractTableItemAction;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.reservation.ReservationViewController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Das Action wird ausgeführt, wenn eine Reservation bearbeitet werden soll.
 * Es werden hier der Aktionen definiert, die im Formular ausgeführt werden können.
 * 
 * @author Steffen Förster
 */
public class EditAction extends AbstractTableItemAction {

    private final MailProvider mailProvider;
    
	public EditAction(AbstractViewController controller, MailProvider mailProvider) {
		super(controller);
		this.mailProvider = mailProvider;
	}
	
	@Override
    public void actionPerformed(ClickEvent event, Table source, Object itemId, Object columnId) {
		ReservationStellplatz item = (ReservationStellplatz) itemId;
		ReservationEditForm form = new ReservationEditForm(item);		
		Window window = new Window(AppRes.getString("reservation.title.edit"));
		window.setModal(true);
		window.setContent(form);
		addSaveAction(window, form, source, item);
		addMailReminderAction(window, form, source, item);
		addMailVeloGruppeAction(window, form, source, item);
		addMailZahlungErhaltenAction(window, form, source, item);
		UI.getCurrent().addWindow(window);
    }
	
	/**
	 * Formulardaten speichern.
	 */
	@SuppressWarnings("serial")
    private void addSaveAction(final Window window, final ReservationEditForm form, final Table table, final ReservationStellplatz item) {
		form.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
	                form.validate();
	                form.fillBean();
	                ReservationService service = ServiceLocator.getReservationService();
	                
	                // Das Flag anonym wird entfernt, sonst kann ggf. der Zeitraum nicht gespeichert werden
	                item.setAnonym(false);
	                
	                service.save(item);
	                window.close();
	                
	                // Je nach neuem Status wird verschiebt wird die Reservation in einer anderen Tabelle angezeigt
	                // -> alle Reservierungen neu laden
	                ReservationViewController ctrl = (ReservationViewController) getController();
	                ctrl.updateTableData();
                }
                catch (Exception e) {
                	CrudFormHelper.handleEditError(e);
                }
			}
		});
	}
	
	/**
	 * Erinnerungsmail senden.
	 */
	@SuppressWarnings("serial")
    private void addMailReminderAction(final Window window, final ReservationEditForm form, final Table table, final ReservationStellplatz item) {
	    form.getBtnMailReminder().addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (item.getMieterPerson() == null) {
                    Notification.show(AppRes.getString("reservation.notification.person.nicht.zugeordnet"), 
                            Notification.Type.ERROR_MESSAGE);
                    return;
                }
                
                String typ = AppRes.getString(item.getStellplatz().getTyp().getResourceKey());
                double kosten = item.getKostenProMonat() * Utility.getMonthsDifference(
                        item.getBeginnDatum(), item.getEndDatumExklusiv());
                mailProvider.sendReminderMail(item, kosten, typ, item.getMieterPerson().getEmail());
            }
        });
	}
	
	/**
	 * Mail an Velogruppe.
	 */
	@SuppressWarnings("serial")
    private void addMailVeloGruppeAction(final Window window, final ReservationEditForm form, final Table table, final ReservationStellplatz item) {
        form.getBtnMailVelogruppe().addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                String typ = AppRes.getString(item.getStellplatz().getTyp().getResourceKey());
                mailProvider.sendVeloGruppeMail(item, typ);
            }
        });
    }
	
	/**
     * Zahlungseingangs-Mail senden.
     */
    @SuppressWarnings("serial")
    private void addMailZahlungErhaltenAction(final Window window, final ReservationEditForm form, final Table table, final ReservationStellplatz item) {
        form.getBtnMailZahlungErhalten().addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (item.getMieterPerson() == null) {
                    Notification.show(AppRes.getString("reservation.notification.person.nicht.zugeordnet"), 
                            Notification.Type.ERROR_MESSAGE);
                    return;
                }
                
                String typ = AppRes.getString(item.getStellplatz().getTyp().getResourceKey());
                double kosten = item.getKostenProMonat() * Utility.getMonthsDifference(
                        item.getBeginnDatum(), item.getEndDatumExklusiv());
                mailProvider.sendZahlungErhaltenMail(item, kosten, typ, item.getMieterPerson().getEmail());
            }
        });
    }
}
