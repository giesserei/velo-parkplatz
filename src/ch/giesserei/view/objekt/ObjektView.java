package ch.giesserei.view.objekt;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;

/**
 * Definiert die View "Objekte".
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ObjektView extends CustomComponent {

	private Table table;
	
	public ObjektView() {
		initComponents();
	}
	
	public Table getObjektTable() {
		return this.table;
	}
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void initComponents() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(createObjektTable());
		setCompositionRoot(layout);
	}
	
	private Component createObjektTable() {
		this.table = new Table();
		return this.table;
	}
}
