package ch.giesserei.view.reservation.position;

import static ch.giesserei.model.ReservationStellplatz.PROPERTY_BEGINN_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_BEZAHLT;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_END_DATUM_EXKL;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_MIETER_PERSON;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_RESERVATION_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_STATUS;
import ch.giesserei.core.Images;
import ch.giesserei.core.MailProvider;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.service.StellplatzService;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.converter.BooleanConverter;
import ch.giesserei.view.converter.DatumConverter;
import ch.giesserei.view.converter.PersonConverter;
import ch.giesserei.view.converter.ReservationStatusConverter;
import ch.giesserei.view.reservation.ReservationViewController;
import ch.giesserei.view.reservation.shared.AddAction;
import ch.giesserei.view.reservation.shared.EditAction;
import ch.giesserei.view.reservation.shared.PayAction;
import ch.giesserei.view.reservation.shared.RemoveAction;

import com.google.inject.Inject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link PositionView}.
 * 
 * @author Steffen Förster
 */
public class PositionViewController extends AbstractViewController implements ReservationViewController {

    private final ReservationService reservationService = ServiceLocator.getReservationService();

    private final StellplatzService stellplatzService = ServiceLocator.getStellplatzService();
    
    private final Images images;

    private PositionView view;

    private Stellplatz selectedStellplatz;
    
    private MailProvider mailProvider;
    
    @Inject
    public PositionViewController(Images images, MailProvider mailProvider) {
        this.images = images;
        this.mailProvider = mailProvider;
    }

    @Override
    public Component createView() {
        this.view = new PositionView(this.images);
        setFilterData();
        addFilterListeners();
        setAktivData();
        setInaktivData();
        setActions();
        setReservationenVisible(false);
        return this.view;
    }

    @Override
    public PositionView getView() {
        return this.view;
    }

    public Stellplatz getSelectedStellplatz() {
        return this.selectedStellplatz;
    }
    
    @Override
    public void updateTableData() {
        updateAktivTableData();
        updateInaktivTableData();
    }

    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------

    private void updateAktivTableData() {
        this.view.getAktivTable().setContainerDataSource(
                this.reservationService.getReservationenAktiv(this.selectedStellplatz));
        configureColumns(this.view.getAktivTable());
        this.view.updateTabCaptionAktiv();
    }

    private void updateInaktivTableData() {
        this.view.getInaktivTable().setContainerDataSource(
                this.reservationService.getReservationenInaktiv(this.selectedStellplatz));
        configureColumns(this.view.getInaktivTable());
        this.view.updateTabCaptionInaktiv();
    }
    
    private void setReservationenVisible(boolean visible) {
        this.view.getAktivTable().setVisible(visible);
        this.view.getInaktivTable().setVisible(visible);
        this.view.getButtonAdd().setVisible(visible);
    }
    
    private void setFilterData() {
        ComboBox nummerFilter = this.view.getNummerFilter();
        nummerFilter.setContainerDataSource(this.stellplatzService.getStellplaetze(StellplatzTyp.PEDALPARC_HOCH, true));
        nummerFilter.setItemCaptionPropertyId("nummer");
        nummerFilter.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        nummerFilter.setFilteringMode(FilteringMode.CONTAINS);
    }

    /**
     * Setzt die aktiven Reservationen in der Tabelle.
     */
    private void setAktivData() {
        Table table = this.view.getAktivTable();

        if (this.selectedStellplatz == null) {
            setEmptyContainer(table);
        }
        else {
            table.setContainerDataSource(this.reservationService.getReservationenAktiv(this.selectedStellplatz));
        }
        configureColumns(table);
        this.view.updateTabCaptionAktiv();
    }

    /**
     * Setzt die inaktiven Reservationen in der Tabelle.
     */
    private void setInaktivData() {
        Table table = this.view.getInaktivTable();

        if (this.selectedStellplatz == null) {
            setEmptyContainer(table);
        }
        else {
            table.setContainerDataSource(this.reservationService.getReservationenInaktiv(this.selectedStellplatz));
        }
        configureColumns(table);
        this.view.updateTabCaptionInaktiv();
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
                PROPERTY_STATUS,
                PROPERTY_BEZAHLT,
                "bemerkung", "buttons" });
        table.setColumnHeader(PROPERTY_RESERVATION_DATUM, AppRes.getString("reservation.lb.datum"));
        table.setColumnHeader(PROPERTY_BEGINN_DATUM, AppRes.getString("reservation.lb.beginn"));
        table.setColumnHeader(PROPERTY_END_DATUM_EXKL, AppRes.getString("reservation.lb.ende"));
        table.setColumnHeader(PROPERTY_MIETER_PERSON, AppRes.getString("person.lb.name.vollstaendig"));
        table.setColumnHeader(PROPERTY_STATUS, AppRes.getString("reservation.lb.status"));
        table.setColumnHeader(PROPERTY_BEZAHLT, AppRes.getString("reservation.lb.bezahlt"));
        table.setColumnHeader("bemerkung", AppRes.getString("reservation.lb.bemerkung"));
        table.setColumnHeader("buttons", "");
        
        table.setConverter(PROPERTY_RESERVATION_DATUM, new DatumConverter());
        table.setConverter(PROPERTY_BEGINN_DATUM, new DatumConverter());
        table.setConverter(PROPERTY_END_DATUM_EXKL, new DatumConverter());
        table.setConverter(PROPERTY_BEZAHLT, new BooleanConverter());
        table.setConverter(PROPERTY_STATUS, new ReservationStatusConverter());
        table.setConverter(PROPERTY_MIETER_PERSON, new PersonConverter());
    }

    private void setEmptyContainer(Table table) {
        BeanItemContainer<ReservationStellplatz> container = new BeanItemContainer<ReservationStellplatz>(
                ReservationStellplatz.class);
        table.setContainerDataSource(container);
    }

    @SuppressWarnings("serial")
    private void addFilterListeners() {
        this.view.getNummerFilter().addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                selectedStellplatz = (Stellplatz) event.getProperty().getValue();
                view.fillDetails(selectedStellplatz);
                setAktivData();
                setInaktivData();
                setReservationenVisible(selectedStellplatz != null);
            }
        });
        this.view.getTypFilter().addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                StellplatzTyp typ = (StellplatzTyp) event.getProperty().getValue();
                view.getNummerFilter().setContainerDataSource(stellplatzService.getStellplaetze(typ, true));
            }
        });
    }

    private void setActions() {
        this.view.setEditAction(new EditAction(this, this.mailProvider));
        this.view.setRemoveAction(new RemoveAction(this));
        this.view.setAddAction(new AddAction(this));
        this.view.setPayAction(new PayAction(this));
    }
}
