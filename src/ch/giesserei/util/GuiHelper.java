package ch.giesserei.util;

import ch.giesserei.app.gui.ConfirmWindow;
import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.resource.AppRes;

import com.vaadin.ui.UI;

/**
 * Hilfsklasse für den Umgang mit der GUI zur Vermeidung von Redundanzen.
 * 
 * @author Steffen Förster
 */
public class GuiHelper {

    private GuiHelper() {
    }
    
    /**
     * Zeigt einen Confirm-Dialog an.
     * 
     * @param keyTile Resource-Key für den Titel
     * @param keyMsg Resource-Key für die Meldung
     * @param decision Callback
     */
    public static void showConfirm(String keyTile, String keyMsg, Decision decision) {
        ConfirmWindow confirm = new ConfirmWindow(
                AppRes.getString(keyTile),
                AppRes.getString(keyMsg),
                AppRes.getString("btn.yes"),
                AppRes.getString("btn.no"),
                decision
        );
        UI.getCurrent().addWindow(confirm);
    }
    
}
