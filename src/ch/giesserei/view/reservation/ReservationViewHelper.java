package ch.giesserei.view.reservation;

import org.apache.commons.lang3.StringUtils;

import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.util.Utility;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

/**
 * Hilfsklasse für die Reservations-Views.
 * 
 * @author Steffen Förster
 */
public class ReservationViewHelper {

    private ReservationViewHelper() {
    }
    
    @SuppressWarnings("serial")
    public static void addBemerkungColumn(final String columnId, final Table table) {
        table.addGeneratedColumn(columnId, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                HorizontalLayout layout = new HorizontalLayout();
                ReservationStellplatz reservation = (ReservationStellplatz) itemId;
                if (StringUtils.isNotBlank(reservation.getBemerkung())) {
                    Label labelBemerkung = new Label(Utility.cropString(reservation.getBemerkung(), 5) + " (...)");
                    labelBemerkung.setDescription("<h2>" + reservation.getBemerkung() +"</h2>");
                    layout.addComponent(labelBemerkung);
                }
                return layout;
            }
        });
    }
    
}
