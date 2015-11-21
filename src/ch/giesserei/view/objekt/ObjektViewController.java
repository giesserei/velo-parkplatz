package ch.giesserei.view.objekt;

import ch.giesserei.service.MietobjektService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.view.AbstractViewController;

import com.google.inject.Inject;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Controller für die View {@link ObjektView}.
 * 
 * @author Steffen Förster
 */
public class ObjektViewController extends AbstractViewController {

    private final MietobjektService objektService = ServiceLocator.getObjektService();
	
    private ObjektView view;
    
	@Inject
	public ObjektViewController() {
	}
	
	/**
	 * Erstellt die View und liefert diese zurück.
	 * 
	 * @return siehe Beschreibung
	 */
	public Component createView() {
		this.view = new ObjektView();
		addData(this.view);
		return this.view;
	}
	
	@Override
    public Component getView() {
        return this.view;
    }
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------

    private void addData(ObjektView view) {
		Table table = view.getObjektTable();
		table.setContainerDataSource(this.objektService.getObjekte());
		table.setVisibleColumns(new Object[]{"nummer", "bezeichnung", "typ"});
	}
	
}
