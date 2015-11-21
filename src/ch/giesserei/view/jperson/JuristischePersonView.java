package ch.giesserei.view.jperson;

import ch.giesserei.resource.AppRes;
import ch.giesserei.view.TableItemAction;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Definiert die View "Personen".
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class JuristischePersonView extends CustomComponent {

	private Table table;
	
	private TableItemAction removeAction;
	
	private TableItemAction editAction;
	
	private Button buttonAdd;
	
	public JuristischePersonView() {
		initComponents();
	}
	
	public Table getTable() {
		return this.table;
	}
	
	public void setRemoveAction(TableItemAction action) {
		this.removeAction = action;
	}
	
	public void setEditAction(TableItemAction action) {
		this.editAction = action;
	}
	
	public void setAddAction(ClickListener action) {
		this.buttonAdd.addClickListener(action);
	}
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void initComponents() {
		VerticalLayout layout = new VerticalLayout();
		this.buttonAdd = new Button(AppRes.getString("btn.add"));
        layout.setSpacing(true);
		layout.addComponent(this.buttonAdd);
		layout.addComponent(createTable());
		setCompositionRoot(layout);
	}
	
	private Component createTable() {
		this.table = new Table();		
		addEditAndRemoveButtons(this.table);
		return this.table;
	}
	
	private void addEditAndRemoveButtons(final Table table) {
		table.addGeneratedColumn("buttons", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, final Object columnId) {
				Button removeButton = new Button(AppRes.getString("btn.table.delete"));
				removeButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						removeAction.actionPerformed(event, source, itemId, columnId);
					}
				});
				
				Button editButton = new Button(AppRes.getString("btn.table.edit"));
				editButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						editAction.actionPerformed(event, source, itemId, columnId);
					}
				});
				
				HorizontalLayout layout = new HorizontalLayout();
				layout.addComponent(removeButton);
				layout.addComponent(editButton);
				return layout;
			}
		});
	}
}
