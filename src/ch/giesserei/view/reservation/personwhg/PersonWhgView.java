package ch.giesserei.view.reservation.personwhg;

import ch.giesserei.auth.Auth;
import ch.giesserei.auth.UserRights;
import ch.giesserei.core.Images;
import ch.giesserei.resource.AppRes;
import ch.giesserei.view.TableItemAction;
import ch.giesserei.view.reservation.ReservationViewHelper;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Definiert die View "Reservationen je Stellplatz".
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class PersonWhgView  extends CustomComponent{

	private static final int TABLE_PAGE_LENGTH = 10;

    private static final String GEN_COL_BEMERKUNG = "bemerkung";

	private ComboBox personFilter;
	
	private ComboBox wohnungFilter; 
	
	private Table aktivTable;
	
	private Table inaktivTable;
	
	private Button buttonFilterByPerson;
	
	private Button buttonFilterByWhg;
	
	private TabSheet tabsheet;
	
    private TableItemAction removeAction;
    
	private TableItemAction editAction;
	
	private TableItemAction payAction;
	
	private final Images images;
	
	public PersonWhgView(Images images) {
		this.images = images;
		initComponents();
	}
	
	public ComboBox getPersonFilter() {
		return this.personFilter;
	}
	
	public ComboBox getWohnungFilter() {
		return this.wohnungFilter;
	}
	
	public Table getAktivTable() {
		return aktivTable;
	}

	public Table getInaktivTable() {
		return inaktivTable;
	}
	
	public TabSheet getTabSheet() {
	    return tabsheet;
	}
	
	public void setRemoveAction(TableItemAction removeAction) {
		this.removeAction = removeAction;
	}

	public void setEditAction(TableItemAction editAction) {
		this.editAction = editAction;
	}
	
	public void setPayAction(TableItemAction payAction) {
        this.payAction = payAction;
    }
	
	public void setFilterPersonAction(ClickListener action) {
		this.buttonFilterByPerson.addClickListener(action);
	}
	
	public void setFilterWohnungAction(ClickListener action) {
        this.buttonFilterByWhg.addClickListener(action);
    }
	
	/**
	 * Aktualisiert die Bezeichnung des Tabs "Aktive Reservationen".
	 */
	public void updateTabCaptionAktiv() {
	    updateTabCaption(this.aktivTable, this.tabsheet.getTab(0), "reservation.lb.tab.aktiv");
	}
	
	/**
     * Aktualisiert die Bezeichnung des Tabs "Inkative Reservationen".
     */
	public void updateTabCaptionInaktiv() {
	    updateTabCaption(this.inaktivTable, this.tabsheet.getTab(1), "reservation.lb.tab.inaktiv");
    }
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void initComponents() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		
		addFilterComponents(layout);
		
		// TabSheet mit zwei Tabellen
		this.tabsheet = new TabSheet();
		this.tabsheet.setHeight(100.0f, Unit.PERCENTAGE);

        VerticalLayout layoutAktivTable = new VerticalLayout();
        layoutAktivTable.setMargin(true);
        layoutAktivTable.addComponent(createAktivTable());
        this.tabsheet.addTab(layoutAktivTable, AppRes.getString("reservation.lb.tab.aktiv", 0));
		
        VerticalLayout layoutInaktivTable = new VerticalLayout();
        layoutInaktivTable.setMargin(true);
        layoutInaktivTable.addComponent(createInaktivTable());
        this.tabsheet.addTab(layoutInaktivTable, AppRes.getString("reservation.lb.tab.inaktiv", 0));
		
		layout.addComponent(this.tabsheet);
		setCompositionRoot(layout);
	}
	
	private Component createAktivTable() {
		this.aktivTable = new Table();		
		this.aktivTable.setPageLength(TABLE_PAGE_LENGTH);
		addEditAndRemoveButtons(this.aktivTable);
		ReservationViewHelper.addBemerkungColumn(GEN_COL_BEMERKUNG, this.aktivTable);
		return this.aktivTable;
	}
	
	private Component createInaktivTable() {
		this.inaktivTable = new Table();	
		this.inaktivTable.setPageLength(TABLE_PAGE_LENGTH);
		addEditAndRemoveButtons(this.inaktivTable);
		ReservationViewHelper.addBemerkungColumn(GEN_COL_BEMERKUNG, this.inaktivTable);
		return this.inaktivTable;
	}
	
	/**
	 * Darstellung der Filter in einem GridLayout. Die Filter werden in einem FormLayout
	 * eingebettet, damit die Bezeichnung links angezeigt wird.
	 */
	private void addFilterComponents(Layout layout) {
	    GridLayout gridLayout = new GridLayout();
        layout.addComponent(gridLayout);
        gridLayout.setColumns(2);
        gridLayout.setRows(2);
        gridLayout.setSpacing(true);

        FormLayout layoutPerson = new FormLayout();
		this.personFilter = new ComboBox(AppRes.getString("reservation.lb.person"));
		this.personFilter.setInputPrompt(AppRes.getString("reservation.lb.person.select"));
        this.personFilter.setNullSelectionAllowed(false);
        this.personFilter.setImmediate(true);
        layoutPerson.addComponent(this.personFilter);
        gridLayout.addComponent(layoutPerson, 0, 0);
        
        this.buttonFilterByPerson = new Button(AppRes.getString("btn.filter.by.person"));
        gridLayout.addComponent(this.buttonFilterByPerson, 1, 0);
        gridLayout.setComponentAlignment(this.buttonFilterByPerson, Alignment.MIDDLE_LEFT);

        FormLayout layoutWohnung = new FormLayout();
        this.wohnungFilter = new ComboBox(AppRes.getString("reservation.lb.wohnung"));
        this.wohnungFilter.setInputPrompt(AppRes.getString("reservation.lb.wohnung.select"));
        this.wohnungFilter.setNullSelectionAllowed(false);
        this.wohnungFilter.setImmediate(true);
        layoutWohnung.addComponent(this.wohnungFilter);
        gridLayout.addComponent(layoutWohnung, 0, 1);
        
        this.buttonFilterByWhg = new Button(AppRes.getString("btn.filter.by.wohnung"));
        gridLayout.addComponent(this.buttonFilterByWhg, 1, 1);
        gridLayout.setComponentAlignment(this.buttonFilterByWhg, Alignment.MIDDLE_LEFT);
        
        layout.addComponent(gridLayout);
	}
	
	private void addEditAndRemoveButtons(final Table table) {
		table.addGeneratedColumn("buttons", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, final Object columnId) {
			    HorizontalLayout layout = new HorizontalLayout();
			    layout.setSpacing(true);
			    
				Button removeButton = new Button();
				removeButton.setIcon(images.getImgDelete());
				removeButton.setDescription(AppRes.getString("btn.table.delete.reservation"));
				removeButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						removeAction.actionPerformed(event, source, itemId, columnId);
					}
				});
				layout.addComponent(removeButton);
				
				Button editButton = new Button();
				editButton.setIcon(images.getImgEdit());
				editButton.setDescription(AppRes.getString("btn.table.edit.reservation"));
				editButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						editAction.actionPerformed(event, source, itemId, columnId);
					}
				});
				layout.addComponent(editButton);
				
				if (Auth.isPermitted(UserRights.RESERVATION_BEZAHL_STATUS)) {
    				Button payButton = new Button();
    				payButton.setIcon(images.getImgMoney(), AppRes.getString("btn.table.pay"));
    				payButton.setDescription(AppRes.getString("btn.table.pay"));
    				payButton.addClickListener(new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            payAction.actionPerformed(event, source, itemId, columnId);
                        }
                    });
    				layout.addComponent(payButton);
				}
				
				return layout;
			}
		});
	}
	
	/**
     * Aktualisiert die Bezeichnung des Tabs "Aktive Reservationen".
     */
    private void updateTabCaption(Table table, Tab tab, String resourceKey) {
        int size = 0;
        if (table.getContainerDataSource() != null) {
            size = table.getContainerDataSource().size();
        }
        tab.setCaption(AppRes.getString(resourceKey, size));
    }
}
