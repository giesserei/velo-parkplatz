package ch.giesserei.view.reservation.personwhg;

import static ch.giesserei.model.ReservationStellplatz.PROPERTY_BEGINN_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_BEZAHLT;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_END_DATUM_EXKL;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_MIETER_PERSON;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_RESERVATION_DATUM;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_STATUS;
import static ch.giesserei.model.ReservationStellplatz.PROPERTY_STELLPLATZ;
import ch.giesserei.core.ApplicationException;
import ch.giesserei.core.Images;
import ch.giesserei.core.MailProvider;
import ch.giesserei.model.Mietobjekt;
import ch.giesserei.model.Person;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.MietobjektService;
import ch.giesserei.service.PersonService;
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
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link PersonWhgView}.
 * 
 * @author Steffen Förster
 */
public class PersonWhgViewController extends AbstractViewController implements ReservationViewController {

    private final ReservationService reservationService = ServiceLocator.getReservationService();

    private final MietobjektService objektService = ServiceLocator.getObjektService();
    
    private final PersonService personService = ServiceLocator.getPersonService();
    
    private final Images images;

    private PersonWhgView view;
    
    private CurrentFilter currentFilter = CurrentFilter.NONE;
    
    private MailProvider mailProvider;
    
    @Inject
    public PersonWhgViewController(Images images, MailProvider mailProvider) {
        this.images = images;
        this.mailProvider = mailProvider;
    }

    @Override
    public Component createView() {
        this.view = new PersonWhgView(this.images);
        setFilterData();
        setActions();
        setReservationenVisible(false);
        return this.view;
    }

    @Override
    public PersonWhgView getView() {
        return this.view;
    }
    
    @Override
    public void updateTableData() {
        if (checkFilterStatus()) {
            updateAktivTableData();
            updateInaktivTableData();
            setReservationenVisible(true);
        }
        else {
            setReservationenVisible(false);
        }
    }
    
    public void setCurrentFilter(CurrentFilter currentFilter) {
        this.currentFilter = currentFilter;
    }
    
    // ---------------------------------------------------------
    // Filter enum
    // ---------------------------------------------------------

    public enum CurrentFilter {
        PERSON,
        WOHNUNG,
        NONE
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------

    /**
     * Liefert true, wenn der Filter korrekt gesetzt ist, so dass Daten gelesen werden können.
     */
    private boolean checkFilterStatus() {
        boolean filterNone = this.currentFilter == CurrentFilter.NONE;
        boolean filterPersonNull = this.currentFilter == CurrentFilter.PERSON && this.view.getPersonFilter().getValue() == null;
        boolean filterWohnungNull = this.currentFilter == CurrentFilter.WOHNUNG && this.view.getWohnungFilter().getValue() == null;
        
        return !filterNone && !filterPersonNull && !filterWohnungNull;
    }
    
    private void updateAktivTableData() {
        if (this.currentFilter == CurrentFilter.PERSON) {
            this.view.getAktivTable().setContainerDataSource(
                    this.reservationService.getReservationenAktiv((Person) this.view.getPersonFilter().getValue()));
        }
        else if (this.currentFilter == CurrentFilter.WOHNUNG) {
            this.view.getAktivTable().setContainerDataSource(
                    this.reservationService.getReservationenAktiv((Mietobjekt) this.view.getWohnungFilter().getValue()));
        }
        else {
            throw new ApplicationException("ungültiger CurrentFilter: " + this.currentFilter);
        }
        configureColumns(this.view.getAktivTable());
        this.view.updateTabCaptionAktiv();
    }

    private void updateInaktivTableData() {
        if (this.currentFilter == CurrentFilter.PERSON) {
            this.view.getInaktivTable().setContainerDataSource(
                    this.reservationService.getReservationenInaktiv((Person) this.view.getPersonFilter().getValue()));
        }
        else if (this.currentFilter == CurrentFilter.WOHNUNG) {
            this.view.getInaktivTable().setContainerDataSource(
                    this.reservationService.getReservationenInaktiv((Mietobjekt) this.view.getWohnungFilter().getValue()));
        }
        else {
            throw new ApplicationException("ungültiger CurrentFilter: " + this.currentFilter);
        }
        configureColumns(this.view.getInaktivTable());
        this.view.updateTabCaptionInaktiv();
    }
    
    private void setReservationenVisible(boolean visible) {
        this.view.getAktivTable().setVisible(visible);
        this.view.getInaktivTable().setVisible(visible);
    }
    
    private void setFilterData() {
        ComboBox personFilter = this.view.getPersonFilter();
        personFilter.setContainerDataSource(this.personService.getPersonen());
        personFilter.setItemCaptionPropertyId(Person.PROPERTY_NAME_WOHNUNG);
        personFilter.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        personFilter.setFilteringMode(FilteringMode.CONTAINS);
        
        ComboBox wohnungFilter = this.view.getWohnungFilter();
        wohnungFilter.setContainerDataSource(this.objektService.getObjekte());
        wohnungFilter.setItemCaptionPropertyId(Mietobjekt.PROPERTY_NUMMER);
        wohnungFilter.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        wohnungFilter.setFilteringMode(FilteringMode.CONTAINS);
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
    }

    private void setActions() {
        this.view.setEditAction(new EditAction(this, this.mailProvider));
        this.view.setRemoveAction(new RemoveAction(this));
        this.view.setPayAction(new PayAction(this));
        this.view.setFilterPersonAction(new FilterPersonAction(this));
        this.view.setFilterWohnungAction(new FilterWohnungAction(this));
    }
}
