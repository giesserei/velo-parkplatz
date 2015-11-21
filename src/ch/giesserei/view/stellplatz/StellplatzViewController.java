package ch.giesserei.view.stellplatz;

import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.service.StellplatzService;
import ch.giesserei.view.AbstractViewController;

import com.google.inject.Inject;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link StellplatzView}.
 * 
 * @author Steffen Förster
 */
public class StellplatzViewController extends AbstractViewController {

    private StellplatzService stellplatzService = ServiceLocator.getStellplatzService();
    
    private StellplatzView view;
	
	/**
	 * Konstruktor.
	 */
    @Inject
	public StellplatzViewController() {
	}
	
	@Override
	public Component createView() {
		this.view = new StellplatzView();
		setData();
		setActions();
		return this.view;
	}
	
	@Override
	public StellplatzView getView() {
		return this.view;
	}
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void setData() {
		Table table = this.view.getTable();
		table.setContainerDataSource(this.stellplatzService.getStellplaetze());
		table.setVisibleColumns(new Object[]{"nummer", "sektor", "typ", "buttons"});
		table.setColumnHeader("nummer", AppRes.getString("stellplatz.lb.nummer"));
		table.setColumnHeader("sector", AppRes.getString("stellplatz.lb.sektor"));
		table.setColumnHeader("typ", AppRes.getString("stellplatz.lb.typ"));
		table.setColumnHeader("buttons", "");
	}
	
	private void setActions() {
		this.view.setEditAction(new EditAction(this));
		this.view.setRemoveAction(new RemoveAction(this));
		this.view.setAddAction(new AddAction(this));
	}
	
}
