package ch.giesserei.view.person;

import ch.giesserei.model.Person;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.PersonService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.view.AbstractViewController;
import ch.giesserei.view.converter.BooleanConverter;

import com.google.inject.Inject;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link PersonView}.
 * 
 * @author Steffen Förster
 */
public class PersonViewController extends AbstractViewController {

    private final PersonService personService = ServiceLocator.getPersonService();
    
	private PersonView view;
	
	/**
	 * Konstruktor.
	 */
	@Inject
	public PersonViewController() {
	}
	
	@Override
	public Component createView() {
		this.view = new PersonView();
		setData();
		setActions();
		return this.view;
	}
	
	@Override
	public PersonView getView() {
		return this.view;
	}
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void setData() {
		Table table = this.view.getTable();
		table.setContainerDataSource(this.personService.getPersonen());
		table.setVisibleColumns(new Object[]{
		        Person.PROPERTY_USER_ID,
		        Person.PROPERTY_VORNAME, 
		        Person.PROPERTY_NACHNAME,
		        Person.PROPERTY_EMAIL,
		        Person.PROPERTY_WOHNUNG,
		        Person.PROPERTY_UPDATED,
				"buttons"});
		table.setColumnHeader(Person.PROPERTY_USER_ID, AppRes.getString("person.lb.joomla.id"));
		table.setColumnHeader(Person.PROPERTY_VORNAME, AppRes.getString("person.lb.vorname"));
		table.setColumnHeader(Person.PROPERTY_NACHNAME, AppRes.getString("person.lb.nachname"));
		table.setColumnHeader(Person.PROPERTY_EMAIL, AppRes.getString("person.lb.email"));
		table.setColumnHeader(Person.PROPERTY_WOHNUNG, AppRes.getString("person.lb.wohnung"));
		table.setColumnHeader(Person.PROPERTY_UPDATED, AppRes.getString("person.lb.updated"));
		table.setColumnHeader("buttons", "");
		
		table.setConverter(Person.PROPERTY_UPDATED, new BooleanConverter());
	}
	
	private void setActions() {
		this.view.setEditAction(new EditAction(this));
		this.view.setRemoveAction(new RemoveAction(this));
		this.view.setAddAction(new AddAction(this));
	}
}
