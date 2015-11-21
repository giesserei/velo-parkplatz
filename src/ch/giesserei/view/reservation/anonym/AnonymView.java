package ch.giesserei.view.reservation.anonym;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ch.giesserei.core.Images;
import ch.giesserei.model.Person;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.PersonService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.Utility;
import ch.giesserei.view.TableItemAction;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Definiert die View "Reservationen ohne Personen". In dieser View werden
 * die anonymen Reservationen bearbeitet -> Personen zugewiesen.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class AnonymView  extends CustomComponent{

    protected static final String GEN_COL_BUTTONS = "buttons";
    
    protected static final String GEN_COL_BEMERKUNG = "bemerkung";
    
    protected static final String GEN_COL_SELECT = "select";
    
	private Table anonymTable;
	
    private TableItemAction saveAction;
    
    private TableItemAction removeAction;
    
    private List<Person> personen;
    
	public AnonymView() {
		initComponents();
	}
	
	public Table getAnonymTable() {
		return anonymTable;
	}

	public void setSaveAction(TableItemAction saveAction) {
		this.saveAction = saveAction;
	}
	
	public void setRemoveAction(TableItemAction removeAction) {
        this.removeAction = removeAction;
    }

	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void initComponents() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(createAnonymTable());
		setCompositionRoot(layout);
	}
	
	private Component createAnonymTable() {
	    // Personen aus der DB laden
	    PersonService service = ServiceLocator.getPersonService();
        this.personen = service.getPersonenAsList();
        
		this.anonymTable = new Table();		
		this.anonymTable.setPageLength(12);
		addButton(this.anonymTable);
		addBemerkungColumn(this.anonymTable);
		addPersonSelect(this.anonymTable);
		return this.anonymTable;
	}
	
	private void addButton(final Table table) {
		table.addGeneratedColumn(GEN_COL_BUTTONS, new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, final Object columnId) {
			    HorizontalLayout layout = new HorizontalLayout();
			    layout.setSpacing(true);
			    
				Button saveButton = new Button(AppRes.getString("btn.table.save"));
				saveButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
					    saveAction.actionPerformed(event, source, itemId, columnId);
					}
				});
				layout.addComponent(saveButton);
				
				Button removeButton = new Button();
                removeButton.setIcon(Images.getInstance().getImgDelete());
                removeButton.setDescription(AppRes.getString("btn.table.delete.reservation"));
                removeButton.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        removeAction.actionPerformed(event, source, itemId, columnId);
                    }
                });
                layout.addComponent(removeButton);
				
				return layout;
			}
		});
	}
	
	private void addPersonSelect(final Table table) {
        table.addGeneratedColumn(GEN_COL_SELECT, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                HorizontalLayout layout = new HorizontalLayout();
                final ReservationStellplatz reservation = (ReservationStellplatz) itemId;
                               
                ComboBox person = new ComboBox();
                person.setInputPrompt(AppRes.getString("reservation.lb.person.select"));
                person.setNullSelectionAllowed(false);
                person.setNewItemsAllowed(false);
                person.setContainerDataSource(getContainer());
                person.setItemCaptionPropertyId(Person.PROPERTY_NAME_WOHNUNG);
                person.setItemCaptionMode(ItemCaptionMode.PROPERTY);
                person.setFilteringMode(FilteringMode.CONTAINS);
                person.setImmediate(true);
                if (reservation.getMieterPerson() != null) {
                    person.setValue(reservation.getMieterPerson());
                }
                person.addValueChangeListener(new ValueChangeListener() {
                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        reservation.setMieterPerson((Person) event.getProperty().getValue());
                    }
                });
                
                layout.addComponent(person);
                return layout;
            }
        });
    }
	
	private void addBemerkungColumn(final Table table) {
	    table.addGeneratedColumn(GEN_COL_BEMERKUNG, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                HorizontalLayout layout = new HorizontalLayout();
                ReservationStellplatz reservation = (ReservationStellplatz) itemId;
                if (StringUtils.isNotBlank(reservation.getBemerkung())) {
                    Label labelBemerkung = new Label(Utility.cropString(reservation.getBemerkung(), 5) + " (...)");
                    labelBemerkung.setDescription("<h2>" + reservation.getBemerkung() +"</h2>");
                    layout.addComponent(labelBemerkung);
                }
                return layout;
            }
	    });
	}
	
	/**
	 * Vaadin unterstützt es noch nicht, dass mehrere Komponenten auf den gleichen Container zugreifen.
     * Daher wird für jede ComboBox ein eigener Container erstellt.
	 */
	private Container getContainer() {
	    BeanItemContainer<Person> container = new BeanItemContainer<Person>(Person.class);
	    container.addAll(this.personen);
	    return container;
	}
	
}
