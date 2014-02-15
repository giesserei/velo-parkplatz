package ch.giesserei.view.reservation;

/**
 * Definiert Methoden, die von mehreren Reservations-ViewControllern verwendet werden.
 * 
 * @author Steffen Förster
 */
public interface ReservationViewController {

    /**
     * Läd die Daten der Daten-Tabelle neu.
     */
    void updateTableData();
    
}
