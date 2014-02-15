package ch.giesserei.view.reservation.position;

import ch.giesserei.auth.Auth;
import ch.giesserei.auth.UserRights;
import ch.giesserei.core.Images;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.resource.AppRes;
import ch.giesserei.view.TableItemAction;
import ch.giesserei.view.reservation.ReservationViewHelper;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Definiert die View "Reservationen je Stellplatz".
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class PositionView  extends CustomComponent{

	private static final String GEN_COL_BEMERKUNG = "bemerkung";

	private ComboBox nummerFilter;
	
	private NativeSelect typFilter; 
	
	private Label nummer;
	
	private Label sektor;
	
	private Label typ;
	
	private Table aktivTable;
	
	private Table inaktivTable;
	
	private Button buttonAdd;
	
	private TabSheet tabsheet;
	
    private TableItemAction removeAction;
    
	private TableItemAction editAction;
	
	private TableItemAction payAction;
	
	private final Images images;
	
	public PositionView(Images images) {
		this.images = images;
		initComponents();
	}
	
	public ComboBox getNummerFilter() {
		return this.nummerFilter;
	}
	
	public NativeSelect getTypFilter() {
		return this.typFilter;
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
	
	public void setAddAction(ClickListener action) {
		this.buttonAdd.addClickListener(action);
	}
	
	public Button getButtonAdd() {
	    return this.buttonAdd;
	}
	
    /**
	 * Aktualisiert die Stellplatz-Detailanzeige. 
	 * 
	 * @param stellplatz wenn NULL, werden keine Details angezeigt
	 */
	public void fillDetails(Stellplatz stellplatz) {
		if (stellplatz == null) {
			this.nummer.setValue("");
			this.sektor.setValue("");
			this.typ.setValue("");
		}
		else {
			this.nummer.setValue(String.valueOf(stellplatz.getNummer()));
			this.sektor.setValue(String.valueOf(stellplatz.getSektor()));
			this.typ.setValue(AppRes.getString(stellplatz.getTyp().getResourceKey()));
		}
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
		addDetailsComponents(layout);
		
		this.buttonAdd = new Button(AppRes.getString("btn.add"));
		layout.addComponent(this.buttonAdd);
		
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
		this.aktivTable.setPageLength(8);
		addEditAndRemoveButtons(this.aktivTable);
		ReservationViewHelper.addBemerkungColumn(GEN_COL_BEMERKUNG, this.aktivTable);
		return this.aktivTable;
	}
	
	private Component createInaktivTable() {
		this.inaktivTable = new Table();	
		this.inaktivTable.setPageLength(8);
		addEditAndRemoveButtons(this.inaktivTable);
		ReservationViewHelper.addBemerkungColumn(GEN_COL_BEMERKUNG, this.inaktivTable);
		return this.inaktivTable;
	}
	
	private void addFilterComponents(Layout layout) {
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(true);
		
		this.nummerFilter = new ComboBox(AppRes.getString("stellplatz.lb.nummer"));
		hlayout.addComponent(this.nummerFilter);
		this.nummerFilter.setInputPrompt(AppRes.getString("stellplatz.lb.nummer.filter"));
        this.nummerFilter.setWidth(200.0f, Unit.PIXELS);
        this.nummerFilter.setNullSelectionAllowed(false);
        this.nummerFilter.setImmediate(true);

        this.typFilter = new NativeSelect(AppRes.getString("stellplatz.lb.typ"));
        hlayout.addComponent(this.typFilter);
        this.typFilter.addItem(StellplatzTyp.PEDALPARC_HOCH);
        this.typFilter.setItemCaption(StellplatzTyp.PEDALPARC_HOCH, AppRes.getString("stellplatz.lb.typ.pedalparc"));
        this.typFilter.addItem(StellplatzTyp.SPEZIAL);
        this.typFilter.setItemCaption(StellplatzTyp.SPEZIAL, AppRes.getString("stellplatz.lb.typ.spezial"));
        this.typFilter.select(StellplatzTyp.PEDALPARC_HOCH);
        this.typFilter.setNullSelectionAllowed(false);
        this.typFilter.setImmediate(true);
        
        layout.addComponent(hlayout);
	}
	
	/**
	 * Erstellt die Ansicht mit den Details zu einem Stellplatz.
	 */
	private void addDetailsComponents(Layout layout) {
		GridLayout gridLayout = new GridLayout();
		layout.addComponent(gridLayout);
		gridLayout.setColumns(2);
		gridLayout.setRows(3);
		gridLayout.setSpacing(true);
		
		gridLayout.addComponent(new Label(AppRes.getString("stellplatz.lb.nummer") + ":"), 0, 0);
		this.nummer = new Label();
		gridLayout.addComponent(this.nummer, 1, 0);
		
		gridLayout.addComponent(new Label(AppRes.getString("stellplatz.lb.sektor") + ":"), 0, 1);
		this.sektor = new Label();
		gridLayout.addComponent(this.sektor, 1, 1);
		
		gridLayout.addComponent(new Label(AppRes.getString("stellplatz.lb.typ") + ":"), 0, 2);
		this.typ = new Label();
		gridLayout.addComponent(this.typ, 1, 2);
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
	
    private void updateTabCaption(Table table, Tab tab, String resourceKey) {
        int size = 0;
        if (table.getContainerDataSource() != null) {
            size = table.getContainerDataSource().size();
        }
        tab.setCaption(AppRes.getString(resourceKey, size));
    }
	
}
