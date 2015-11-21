package ch.giesserei.view.reservation.rollover;

import static ch.giesserei.model.ReservationStellplatz.PROPERTY_BEGINN_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_END_DATUM_EXKL;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_MIETER_PERSON;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_STELLPLATZ;
import static ch.giesserei.view.reservation.rollover.RolloverView.GEN_COL_BEMERKUNG;
import static ch.giesserei.view.reservation.rollover.RolloverView.GEN_COL_MARK;

import java.util.Date;
import java.util.List;

import ch.giesserei.core.Const;
import ch.giesserei.core.MailProvider;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.Utility;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.converter.DatumConverter;
import ch.giesserei.view.converter.PersonEmailConverter;
import ch.giesserei.view.converter.StellplatzConverter;

import com.google.inject.Inject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link RolloverView}.
 * 
 * @author Steffen Förster
 */
public class RolloverViewController extends AbstractViewController {

    private final ReservationService reservationService = ServiceLocator.getReservationService();

    private RolloverView view;
    
    private final MailProvider mailProvider;

    @Inject
    public RolloverViewController(MailProvider mailProvider) {
        this.mailProvider = mailProvider;
    }

    @Override
    public Component createView() {
        this.view = new RolloverView();
        setTableData(true, Const.MONATE_ROLLOVER);
        setActions();
        setPeriodListener();
        return this.view;
    }

    @Override
    public RolloverView getView() {
        return this.view;
    }
    
    /**
     * Läd die Daten noch einmal in die Tabelle.
     * 
     * @param select Wenn die ablaufenden Reservationen markiert werden sollen
     * @param period Monate, wann die Reservationen zukünftig ablaufen
     */
    public void reloadData(boolean select) {
        setTableData(select, (Integer) getView().getPeriod().getValue());
    }

    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------

    /**
     * Setzt die verlängerbaren Reservationen in der Tabelle.
     * 
     * @param select Wenn die ablaufenden Reservationen markiert werden sollen
     * @param period Monate, wann die Reservationen zukünftig ablaufen
     */
    private void setTableData(boolean select, int period) {
        Table table = this.view.getRolloverTable();
        // Alle Reservationen holen, die in x Monaten ablaufen oder bereits abgelaufen sind
        Date date = Utility.addMonth(new Date(), period);
        List<ReservationStellplatz> reservationList = this.reservationService.getReservationenAblaufend(date);
        
        // Markieren der Reservationen, die verlängert werden können -> Reservationen ohne Person sind nicht verlängerbar
        int countMarked = 0;
        for (ReservationStellplatz res : reservationList) {
            res.setMarkAsRollover(res.getMieterPerson() != null && select);
            if (res.isMarkAsRollover()) {
                countMarked ++;
            }
        }
        BeanItemContainer<ReservationStellplatz> container = 
                new BeanItemContainer<ReservationStellplatz>(ReservationStellplatz.class);
        for (ReservationStellplatz reservation : reservationList) {
            container.addBean(reservation);
        }
        
        table.setContainerDataSource(container);
        configureColumns(table);
        table.setColumnFooter(PROPERTY_BEGINN_DATUM, AppRes.getString("table.row.count", container.size()));
        table.setColumnFooter(RolloverView.GEN_COL_MARK, AppRes.getString("reservation.table.row.count.marked", countMarked));
    }

    /**
     * Konfiguriert die sichtbaren Spalten, für die übergebene Tabelle.
     * Der Tabelle muss vorher ein Container zugewiesen werden.
     */
    private void configureColumns(Table table) {
        table.setVisibleColumns(new Object[] {  
                PROPERTY_BEGINN_DATUM, 
                PROPERTY_END_DATUM_EXKL,
                PROPERTY_MIETER_PERSON, 
                PROPERTY_STELLPLATZ,
                GEN_COL_BEMERKUNG, GEN_COL_MARK});
        table.setColumnHeader(PROPERTY_BEGINN_DATUM, AppRes.getString("reservation.lb.beginn"));
        table.setColumnHeader(PROPERTY_END_DATUM_EXKL, AppRes.getString("reservation.lb.ende"));
        table.setColumnHeader(PROPERTY_MIETER_PERSON, AppRes.getString("person.lb.name.vollstaendig"));
        table.setColumnHeader(PROPERTY_STELLPLATZ, AppRes.getString("reservation.lb.stellplatz"));
        table.setColumnHeader(GEN_COL_BEMERKUNG, AppRes.getString("reservation.lb.bemerkung"));
        table.setColumnHeader(GEN_COL_MARK, "");
        
        table.setConverter(PROPERTY_BEGINN_DATUM, new DatumConverter());
        table.setConverter(PROPERTY_END_DATUM_EXKL, new DatumConverter());
        table.setConverter(PROPERTY_MIETER_PERSON, new PersonEmailConverter());
        table.setConverter(PROPERTY_STELLPLATZ, new StellplatzConverter());
        
        table.setFooterVisible(true);
    }

    private void setActions() {
        this.view.setProcessAction(new ProcessRolloverAction(this, this.mailProvider));
        this.view.setDeselectAllAction(new DeselectAction(this));
    }
    
    @SuppressWarnings({ "serial" })
    private void setPeriodListener() {
        this.view.getPeriod().addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                Integer period = (Integer) event.getProperty().getValue();
                setTableData(true, period);
            }
        });
    }
}
