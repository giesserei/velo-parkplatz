package ch.giesserei.view.person;

import ch.giesserei.model.Person;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.PersonService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Das Action wird ausgeführt, wenn ein Datensatz in der Tabelle Person bearbeitet werden soll.
 * 
 * @author Steffen Förster
 */
public class EditAction extends AbstractTableItemAction {

	public EditAction(PersonViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, Table source, Object itemId, Object columnId) {
		Person item = (Person) itemId;
		PersonEditForm form = new PersonEditForm(item);		
		Window window = new Window(AppRes.getString("person.title.edit"));
		window.setModal(true);
		window.setContent(form);
		addSaveAction(window, form, source, item);
		UI.getCurrent().addWindow(window);
    }
	
	@SuppressWarnings("serial")
    private void addSaveAction(final Window window, final PersonEditForm form, final Table table, final Person item) {
		form.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
	                form.validate();
	                form.fillBean();
	                PersonService service = ServiceLocator.getPersonService();
	                Person savedItem = service.save(item);
	                window.close();
	                CrudFormHelper.replaceItemAndUpdateTable(table, item, savedItem);
                }
                catch (Exception e) {
                	CrudFormHelper.handleEditError(e);
                }
			}
		});
	}
}
