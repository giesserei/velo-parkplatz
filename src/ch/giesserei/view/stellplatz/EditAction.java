package ch.giesserei.view.stellplatz;

import ch.giesserei.model.Stellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.service.StellplatzService;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Das Action wird ausgeführt, wenn ein Stellplatz bearbeitet werden soll.
 * 
 * @author Steffen Förster
 */
public class EditAction extends AbstractTableItemAction {

	public EditAction(StellplatzViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, Table source, Object itemId, Object columnId) {
		Stellplatz item = (Stellplatz) itemId;
		StellplatzEditForm form = new StellplatzEditForm(item);		
		Window window = new Window(AppRes.getString("stellplatz.title.edit"));
		window.setModal(true);
		window.setContent(form);
		addSaveAction(window, form, source, item);
		UI.getCurrent().addWindow(window);
    }
	
	@SuppressWarnings("serial")
    private void addSaveAction(final Window window, final StellplatzEditForm form, final Table table, final Stellplatz item) {
		form.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
	                form.validate();
	                form.fillBean();
	                StellplatzService service = ServiceLocator.getStellplatzService();
	                Stellplatz savedItem = service.save(item);
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
