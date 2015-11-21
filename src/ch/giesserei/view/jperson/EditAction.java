package ch.giesserei.view.jperson;

import ch.giesserei.model.JuristischePerson;
import ch.giesserei.resource.AppRes;
import ch.giesserei.service.JuristischePersonService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.util.CrudFormHelper;
import ch.giesserei.view.AbstractTableItemAction;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Das Action wird ausgeführt, wenn ein Datensatz in der Tabelle Juristischen Person bearbeitet werden soll.
 * 
 * @author Steffen Förster
 */
public class EditAction extends AbstractTableItemAction {

	public EditAction(JuristischePersonViewController controller) {
		super(controller);
	}
	
	@Override
    public void actionPerformed(ClickEvent event, Table source, Object itemId, Object columnId) {
		JuristischePerson item = (JuristischePerson) itemId;
		JuristischePersonEditForm form = new JuristischePersonEditForm(item);		
		Window window = new Window(AppRes.getString("jperson.title.edit"));
		window.setModal(true);
		window.setContent(form);
		addSaveAction(window, form, source, item);
		UI.getCurrent().addWindow(window);
    }
	
	@SuppressWarnings("serial")
    private void addSaveAction(final Window window, final JuristischePersonEditForm form, 
    		final Table table, final JuristischePerson item) {
		form.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
	                form.validate();
	                form.fillBean();
	                JuristischePersonService service = ServiceLocator.getJuristischePersonService();
	                JuristischePerson savedItem = service.save(item);
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
