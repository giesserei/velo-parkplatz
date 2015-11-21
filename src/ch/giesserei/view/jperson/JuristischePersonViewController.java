package ch.giesserei.view.jperson;

import ch.giesserei.model.JuristischePerson;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.JuristischePersonService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.view.AbstractViewController;

import com.google.inject.Inject;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link JuristischePersonView}.
 * 
 * @author Steffen Förster
 */
public class JuristischePersonViewController extends AbstractViewController {

	private final JuristischePersonService jpersonService = ServiceLocator.getJuristischePersonService();
	
	private JuristischePersonView view;
	
	/**
	 * Konstruktor.
	 */
	@Inject
	public JuristischePersonViewController() {
	}
	
	@Override
	public Component createView() {
		this.view = new JuristischePersonView();
		setData();
		setActions();
		return this.view;
	}
	
	@Override
	public JuristischePersonView getView() {
		return this.view;
	}
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void setData() {
		Table table = this.view.getTable();
		table.setContainerDataSource(this.jpersonService.getPersonen());
		table.setVisibleColumns(new Object[]{"name", "ansprechpartner", 
				JuristischePerson.NESTED_PROPERTY_ADRESSE_ORT, "buttons"});
		table.setColumnHeader("name", AppRes.getString("jperson.lb.name"));
		table.setColumnHeader("ansprechpartner", AppRes.getString("jperson.lb.ansprechpartner"));
		table.setColumnHeader(JuristischePerson.NESTED_PROPERTY_ADRESSE_ORT, AppRes.getString("jperson.lb.ort"));
		table.setColumnHeader("buttons", "");
	}
	
	private void setActions() {
		this.view.setEditAction(new EditAction(this));
		this.view.setRemoveAction(new RemoveAction(this));
		this.view.setAddAction(new AddAction(this));
	}
}
