package ch.giesserei.view.reservation.anonym;

import static ch.giesserei.model.ReservationStellplatz.PROPERTY_BEGINN_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_END_DATUM_EXKL;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_NAME_BUCHUNG;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_RESERVATION_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_STELLPLATZ;
import static ch.giesserei.view.reservation.anonym.AnonymView.GEN_COL_BEMERKUNG;
import static ch.giesserei.view.reservation.anonym.AnonymView.GEN_COL_BUTTONS;
import static ch.giesserei.view.reservation.anonym.AnonymView.GEN_COL_SELECT;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.converter.DatumConverter;
import ch.giesserei.view.converter.StellplatzConverter;

import com.google.inject.Inject;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link AnonymView}.
 * 
 * @author Steffen Förster
 */
public class AnonymViewController extends AbstractViewController {

    private final ReservationService reservationService = ServiceLocator.getReservationService();

    private AnonymView view;

    @Inject
    public AnonymViewController() {
    }

    @Override
    public Component createView() {
        this.view = new AnonymView();
        setTableData();
        setActions();
        return this.view;
    }

    @Override
    public AnonymView getView() {
        return this.view;
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------

    /**
     * Setzt die anonymen Reservationen in der Tabelle.
     */
    private void setTableData() {
        Table table = this.view.getAnonymTable();
        table.setContainerDataSource(this.reservationService.getReservationenAnonym());
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
                PROPERTY_NAME_BUCHUNG,
                GEN_COL_SELECT,
                GEN_COL_BUTTONS,
                PROPERTY_BEGINN_DATUM, 
                PROPERTY_END_DATUM_EXKL,
                PROPERTY_STELLPLATZ,
                GEN_COL_BEMERKUNG});
        table.setColumnHeader(PROPERTY_RESERVATION_DATUM, AppRes.getString("reservation.lb.datum"));
        table.setColumnHeader(PROPERTY_BEGINN_DATUM, AppRes.getString("reservation.lb.beginn"));
        table.setColumnHeader(PROPERTY_END_DATUM_EXKL, AppRes.getString("reservation.lb.ende"));
        table.setColumnHeader(PROPERTY_STELLPLATZ, AppRes.getString("reservation.lb.stellplatz"));
        table.setColumnHeader(PROPERTY_NAME_BUCHUNG, AppRes.getString("reservation.lb.name.buchung"));
        table.setColumnHeader(GEN_COL_BEMERKUNG, AppRes.getString("reservation.lb.bemerkung"));
        table.setColumnHeader(GEN_COL_SELECT, AppRes.getString("reservation.lb.person"));
        table.setColumnHeader(GEN_COL_BUTTONS, "");
        
        table.setConverter(PROPERTY_RESERVATION_DATUM, new DatumConverter());
        table.setConverter(PROPERTY_BEGINN_DATUM, new DatumConverter());
        table.setConverter(PROPERTY_END_DATUM_EXKL, new DatumConverter());
        table.setConverter(PROPERTY_STELLPLATZ, new StellplatzConverter());
        
        table.setFooterVisible(true);
    }

    private void setActions() {
        this.view.setSaveAction(new SaveAction(this));
        this.view.setRemoveAction(new RemoveAnonymAction(this));
    }
}
