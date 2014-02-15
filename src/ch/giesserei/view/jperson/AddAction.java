package ch.giesserei.view.jperson;

import ch.giesserei.model.Adresse;
import ch.giesserei.model.JuristischePerson;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.JuristischePersonService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractClickListener;
import ch.giesserei.view.AbstractViewController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Dieses Aktion steuert das Hinzufügen einer neuen Juristischen Person.
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
		JuristischePerson item = new JuristischePerson();
		item.setAdresse(new Adresse());
		
		JuristischePersonEditForm form = new JuristischePersonEditForm(item);		
		Window window = new Window(AppRes.getString("jperson.title.add"));
		window.setModal(true);
		window.setContent(form);
		addSaveAction(window, form, item);
		UI.getCurrent().addWindow(window);
    }
	
	private void addSaveAction(final Window window, final JuristischePersonEditForm form, final JuristischePerson item) {
		form.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
	                form.validate();
	                form.fillBean();
	                JuristischePersonService service = ServiceLocator.getJuristischePersonService();
	                JuristischePerson persitedItem = service.persist(item);
	                window.close();
	                JuristischePersonViewController controller = (JuristischePersonViewController) getViewController();
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
