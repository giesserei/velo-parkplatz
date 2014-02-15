package ch.giesserei.view.reservation.rollover;

import ch.giesserei.view.AbstractClickListener;
import ch.giesserei.view.AbstractViewController;

import com.vaadin.ui.Button.ClickEvent;

/**
 * Mit diesem Action werden alle markierten Reservationen abgewählt.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class DeselectAction extends AbstractClickListener {
    
    public DeselectAction(AbstractViewController viewController) {
        super(viewController);
    }
    
    @Override
    public void buttonClick(ClickEvent event) {
        getCtrl().reloadData(false);
    }
    
    private RolloverViewController getCtrl() {
        return (RolloverViewController) getViewController();
    }
}
