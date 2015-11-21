package ch.giesserei.view.person;

import ch.giesserei.model.Adresse;
import ch.giesserei.model.Person;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.PersonService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractClickListener;
import ch.giesserei.view.AbstractViewController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Dieses Aktion steuert das Hinzufügen einer neuen Person.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class AddAction extends AbstractClickListener {

	public AddAction(AbstractViewController viewController) {
		super(viewController);
	}
	
	@Override
    public void buttonClick(ClickEvent event) {
		Person item = new Person();
		item.setAdresse(new Adresse());
		
		PersonEditForm form = new PersonEditForm(item);		
		Window window = new Window(AppRes.getString("person.title.add"));
		window.setModal(true);
		window.setContent(form);
		addSaveAction(window, form, item);
		UI.getCurrent().addWindow(window);
    }
	
	private void addSaveAction(final Window window, final PersonEditForm form, final Person item) {
		form.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
	                form.validate();
	                form.fillBean();
	                PersonService service = ServiceLocator.getPersonService();
	                Person persitedItem = service.persist(item);
	                window.close();
	                PersonViewController controller = (PersonViewController) getViewController();
	                controller.getView().getTable().getContainerDataSource().addItem(persitedItem);
	                controller.getView().getTable().markAsDirtyRecursive();
                }
				catch (Exception e) {
	                CrudFormHelper.handleEditError(e);
                }
			}
		});
	}
}
