package ch.giesserei.view.reservation.overdue;

import static ch.giesserei.model.ReservationStellplatz.PROPERTY_BEGINN_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_BEZAHLT;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_END_DATUM_EXKL;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_MIETER_PERSON;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_RESERVATION_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_STATUS;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_STELLPLATZ;
import ch.giesserei.core.Images;
import ch.giesserei.core.MailProvider;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.converter.BooleanConverter;
import ch.giesserei.view.converter.DatumConverter;
import ch.giesserei.view.converter.PersonEmailConverter;
import ch.giesserei.view.converter.ReservationStatusConverter;
import ch.giesserei.view.converter.StellplatzConverter;
import ch.giesserei.view.reservation.ReservationViewController;
import ch.giesserei.view.reservation.shared.EditAction;
import ch.giesserei.view.reservation.shared.PayAction;
import ch.giesserei.view.reservation.shared.RemoveAction;

import com.google.inject.Inject;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link OverdueView}.
 * 
 * @author Steffen Förster
 */
public class OverdueViewController extends AbstractViewController implements ReservationViewController {

    private final ReservationService reservationService = ServiceLocator.getReservationService();

    private final Images images;

    private OverdueView view;
    
    private MailProvider mailProvider;

    @Inject
    public OverdueViewController(Images images, MailProvider mailProvider) {
        this.images = images;
        this.mailProvider = mailProvider;
    }

    @Override
    public Component createView() {
        this.view = new OverdueView(this.images);
        setOverdueData();
        setActions();
        return this.view;
    }

    @Override
    public OverdueView getView() {
        return this.view;
    }
    
    @Override
    public void updateTableData() {
        setOverdueData();
    }

    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------

    /**
     * Setzt die Reservationen, deren Zahlung überfällig ist, in der Tabelle.
     */
    private void setOverdueData() {
        Table table = this.view.getOverdueTable();
        table.setContainerDataSource(this.reservationService.getReservationenOverdue());
        configureColumns(table);
        table.setColumnFooter(PROPERTY_RESERVATION_DATUM, AppRes.getString("table.row.count", table.getContainerDataSource().size()));
    }

    /**
     * Konfiguriert die sichtbaren Spalten, für die übergebene Tabelle.
     * Der Tabelle muss vorher ein Container zugewiesen werden.
     */
    private void configureColumns(Table table) {
        table.setVisibleColumns(new Object[] {  
                PROPERTY_RESERVATION_DATUM,
                PROPERTY_BEGINN_DATUM, 
                PROPERTY_END_DATUM_EXKL,
                PROPERTY_MIETER_PERSON,
                PROPERTY_STELLPLATZ,
                PROPERTY_STATUS,
                PROPERTY_BEZAHLT,
                "bemerkung", "buttons" });
        table.setColumnHeader(PROPERTY_RESERVATION_DATUM, AppRes.getString("reservation.lb.datum"));
        table.setColumnHeader(PROPERTY_BEGINN_DATUM, AppRes.getString("reservation.lb.beginn"));
        table.setColumnHeader(PROPERTY_END_DATUM_EXKL, AppRes.getString("reservation.lb.ende"));
        table.setColumnHeader(PROPERTY_MIETER_PERSON, AppRes.getString("person.lb.name.vollstaendig"));
        table.setColumnHeader(PROPERTY_STELLPLATZ, AppRes.getString("reservation.lb.stellplatz"));
        table.setColumnHeader(PROPERTY_STATUS, AppRes.getString("reservation.lb.status"));
        table.setColumnHeader(PROPERTY_BEZAHLT, AppRes.getString("reservation.lb.bezahlt"));
        table.setColumnHeader("bemerkung", AppRes.getString("reservation.lb.bemerkung"));
        table.setColumnHeader("buttons", "");
        
        table.setConverter(PROPERTY_RESERVATION_DATUM, new DatumConverter());
        table.setConverter(PROPERTY_BEGINN_DATUM, new DatumConverter());
        table.setConverter(PROPERTY_END_DATUM_EXKL, new DatumConverter());
        table.setConverter(PROPERTY_BEZAHLT, new BooleanConverter());
        table.setConverter(PROPERTY_STATUS, new ReservationStatusConverter());
        table.setConverter(PROPERTY_MIETER_PERSON, new PersonEmailConverter());
        table.setConverter(PROPERTY_STELLPLATZ, new StellplatzConverter(StellplatzConverter.Format.NUMMER));
        
        table.setFooterVisible(true);
    }

    private void setActions() {
        this.view.setEditAction(new EditAction(this, this.mailProvider));
        this.view.setRemoveAction(new RemoveAction(this));
        this.view.setPayAction(new PayAction(this));
    }
}
