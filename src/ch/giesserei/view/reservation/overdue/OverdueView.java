package ch.giesserei.view.reservation.overdue;

import ch.giesserei.auth.Auth;
import ch.giesserei.auth.UserRights;
import ch.giesserei.core.Images;
import ch.giesserei.resource.AppRes;
import ch.giesserei.view.TableItemAction;
import ch.giesserei.view.reservation.ReservationViewHelper;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Definiert die View "Reservationen nicht bezahlt".
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class OverdueView  extends CustomComponent{

	private static final String GEN_COLUMN_BEMERKUNG = "bemerkung";

    private static final String GEN_COL_BUTTONS = "buttons";

	private Table overdueTable;
	
    private TableItemAction removeAction;
    
	private TableItemAction editAction;
	
	private TableItemAction payAction;
	
	private final Images images;
	
	public OverdueView(Images images) {
		this.images = images;
		initComponents();
	}
	
	public Table getOverdueTable() {
		return overdueTable;
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
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void initComponents() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(createOverdueTable());
		setCompositionRoot(layout);
	}
	
	private Component createOverdueTable() {
		this.overdueTable = new Table();		
		this.overdueTable.setPageLength(12);
		addEditAndRemoveButtons(this.overdueTable);
		ReservationViewHelper.addBemerkungColumn(GEN_COLUMN_BEMERKUNG, this.overdueTable);
		return this.overdueTable;
	}
	
	private void addEditAndRemoveButtons(final Table table) {
		table.addGeneratedColumn(GEN_COL_BUTTONS, new Table.ColumnGenerator() {
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
}
