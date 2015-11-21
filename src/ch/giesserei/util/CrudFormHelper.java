package ch.giesserei.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.core.ModelConstraintException;
import ch.giesserei.resource.AppRes;
import ch.giesserei.resource.ValMsg;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

/**
 * Hilfklasse für ein CRUD-Form.
 * 
 * @author Steffen Förster
 */
public class CrudFormHelper {

	private static final Logger LOG = LoggerFactory.getLogger(CrudFormHelper.class); 
	
	/**
	 * Es wird versucht das geänderte Item in der Tabelle an der gleichen Stelle zu ersetzen.
	 * Dies ist nicht möglich, wenn das Item das erste Item in der Tabelle ist. Dann wird das 
	 * Item an letzter Stelle eingefügt.
	 */
    public static void replaceItemAndUpdateTable(final Table table, final Object oldItem, Object savedItem) {
        Object previousItem = table.prevItemId(oldItem);
        Object nextItem = table.nextItemId(oldItem);
        table.removeItem(oldItem);
        
        if (previousItem != null) {
        	table.addItemAfter(previousItem, savedItem);
        }
        else if (nextItem != null) {
        	table.addItemAfter(nextItem, savedItem);
        }
        else {
        	table.addItem(savedItem);
        }
        
        table.markAsDirtyRecursive();
    }
    
    /**
     * Führt die Fehlerbehandlung nach einer CRUD-Operation durch.
     * 
     * @param exception Fehler
     */
    public static void handleEditError(Exception exception) {
		if (exception instanceof InvalidValueException) {
            Notification.show(
            		ValMsg.getString("notification.caption.validation"),
            		exception.getMessage(),
            		Notification.Type.ERROR_MESSAGE);
        }
		else if (exception instanceof ModelConstraintException) {
            Notification.show(
                    ValMsg.getString("notification.caption.validation"),
                    exception.getMessage(),
                    Notification.Type.ERROR_MESSAGE);
        }
		else if (exception instanceof Exception) {
			LOG.error(exception.getMessage(), exception);
			Notification.show(
					AppRes.getString("notification.caption.save.failed"),
            		exception.getMessage(),
            		Notification.Type.ERROR_MESSAGE);
		}
	}
    
    /**
     * Zeigt einen Bestätigungsdialog für das Löschen eines Items an.
     * 
     * @param desision Callback für Entscheidung
     * @param appResources Resources
     */
    public static void showRemoveConfirm(Decision decision) {
        GuiHelper.showConfirm("title.confirm.remove", "msg.confirm.remove", decision);
    }
}
