package ch.giesserei.view.stellplatz;

import ch.giesserei.model.Stellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.service.StellplatzService;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractClickListener;
import ch.giesserei.view.AbstractViewController;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Dieses Aktion steuert das Hinzufügen eines neuen Stellpatzes.
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
		Stellplatz item = new Stellplatz();
		
		StellplatzEditForm form = new StellplatzEditForm(item);		
		Window window = new Window(AppRes.getString("stellplatz.title.add"));
		window.setModal(true);
		window.setContent(form);
		addSaveAction(window, form, item);
		UI.getCurrent().addWindow(window);
    }
	
	private void addSaveAction(final Window window, final StellplatzEditForm form, final Stellplatz item) {
		form.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
	                form.validate();
	                form.fillBean();
	                StellplatzService service = ServiceLocator.getStellplatzService();
	                Stellplatz persitedItem = service.persist(item);
	                window.close();
	                StellplatzViewController controller = (StellplatzViewController) getViewController();
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
