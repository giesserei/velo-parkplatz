package ch.giesserei.view.reservation.rollover;

import java.util.Collection;
import java.util.Date;

import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.core.MailProvider;
import ch.giesserei.filter.JpaFilter;
import ch.giesserei.model.ReservationStatus;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.GuiHelper;
import ch.giesserei.util.Utility;
import ch.giesserei.view.AbstractClickListener;
import ch.giesserei.view.AbstractViewController;

import com.vaadin.data.Container;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * Dieses Aktion steuert die Verlängerung von Stellplatz-Mieten.
 * Es wird zu einer bestehenden Reservation eine neue Reservation angelegt.
 * Gleichzeitig wird der Bewohner per Mail darüber informiert.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ProcessRolloverAction extends AbstractClickListener {

    private final ReservationService reservationService = ServiceLocator.getReservationService();
    
    private final MailProvider mailProvider;
    
	public ProcessRolloverAction(AbstractViewController viewController, MailProvider mailProvider) {
		super(viewController);
		this.mailProvider = mailProvider;
	}
	
    @SuppressWarnings("unchecked")
    @Override
    public void buttonClick(ClickEvent event) {
        Container container = getCtrl().getView().getRolloverTable().getContainerDataSource();
        Collection<ReservationStellplatz> reservationen = (Collection<ReservationStellplatz>) container.getItemIds();
        int count = getCountMarkedReservationen(reservationen);
        if (count == 0) {
            Notification.show(AppRes.getString("reservation.notification.rollover.nichts.gewaehlt"), 
                    Notification.Type.WARNING_MESSAGE);
        }
        else {
    	    Decision decision = new Decision() {
                @Override
                public void yes() {
                    // Disable the button until the work is done
                    getCtrl().getView().getProgress().setEnabled(true);
                    getCtrl().getView().getProgress().setVisible(true);
                    getCtrl().getView().getButtonProcess().setEnabled(false);
                    getCtrl().getView().getButtonDeselectAll().setEnabled(false);
                    
                    final RolloverThread thread = new RolloverThread();
                    thread.start();
    
                    // Polling -> UI fragt immer nach dem aktuellen Stand
                    UI.getCurrent().setPollInterval(1000);
                }
    
                @Override
                public void no() {
                }
            };
            GuiHelper.showConfirm("title.confirm.rollover", "msg.confirm.rollover", decision);
        }
    }
	
	// ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
	
	private RolloverViewController getCtrl() {
		return (RolloverViewController) getViewController();
	}
	
	private int getCountMarkedReservationen(Collection<ReservationStellplatz> reservationen) {
        int steps = 0;
        for (ReservationStellplatz reservation : reservationen) {
            if (reservation.isMarkAsRollover()) {
                steps++;
            }
        }
        return steps;
    }
	
	// ---------------------------------------------------------
    // Task
    // ---------------------------------------------------------

    public class RolloverThread extends Thread {
        // Volatile because read in another thread in access()
        volatile double current = 0.0;

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            // EntityManager für diesen Thread bereitstellen
            JpaFilter.setEntityManager();
            UI currentUi = UI.getCurrent();
            
            try {
                Container container = getCtrl().getView().getRolloverTable().getContainerDataSource();
                Collection<ReservationStellplatz> reservationen = (Collection<ReservationStellplatz>) container.getItemIds();
                
                float step = 1 / getCountMarkedReservationen(reservationen);
                for (ReservationStellplatz reservation : reservationen) {
                    if (reservation.isMarkAsRollover()) {
                        rollover(reservation);
                        this.current += step;
                        
                        // Update the UI thread-safely
                        if (this.current < 1.0) {
                            currentUi.access(new Runnable() {
                                @Override
                                public void run() {
                                    getCtrl().getView().getProgress().setValue(new Float(current));
                                }
                            });
                        }
                    }
                }
                
                // Kurz zeigen, dass alles fertig ist
                try {
                    sleep(2000); 
                } catch (InterruptedException e) {}
    
                // Update the UI thread-safely
                updateUi(currentUi);
            }
            finally  {
                JpaFilter.closeEntityManager();
            }
        }
        
        private void rollover(ReservationStellplatz reservation) {
            // Neue Reservation erstellen
            ReservationStellplatz newReservation = new ReservationStellplatz();
            newReservation.setReservationDatum(new Date());
            newReservation.setBeginnDatum(reservation.getEndDatumExklusiv());
            newReservation.setEndDatumExklusiv(Utility.addYear(newReservation.getBeginnDatum()));
            newReservation.setMieterPerson(reservation.getMieterPerson());
            
            Stellplatz stellplatz = reservation.getStellplatz();
            newReservation.setKostenProMonat(reservationService.getKostenProMonat(stellplatz.getTyp()));
            newReservation.setStellplatz(stellplatz);
            newReservation.setReservationStatus(ReservationStatus.ROLLOVER);
            
            // Reservation speichern
            newReservation = reservationService.persist(newReservation);
            
            String typ = AppRes.getString(stellplatz.getTyp().getResourceKey());
            boolean sendSuccess = mailProvider.sendRolloverMail(
                    newReservation, 
                    reservationService.getKostenProPeriode(stellplatz.getTyp()), 
                    typ, 
                    newReservation.getMieterPerson().getEmail()
            );
            
            // Reservation aktualisieren
            newReservation.setEmailRollover(newReservation.getMieterPerson().getEmail());
            newReservation.setEmailRolloverErfolgreich(sendSuccess);
            reservationService.save(newReservation);
        }
        
        private void updateUi(UI currentUi) {
            currentUi.access(new Runnable() {
                @Override
                public void run() {
                    getCtrl().getView().getProgress().setValue(new Float(0.0));
                    getCtrl().getView().getProgress().setEnabled(false);
                    getCtrl().getView().getProgress().setVisible(false);
                            
                    // Stop polling
                    UI.getCurrent().setPollInterval(-1);
                    
                    getCtrl().getView().getButtonProcess().setEnabled(true);
                    getCtrl().getView().getButtonDeselectAll().setEnabled(true);
                    getCtrl().reloadData(true);
                }
            });
        }
        
    }
    
}
