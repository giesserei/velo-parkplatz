package ch.giesserei.model;

import java.util.Arrays;
import java.util.List;

/**
 * Enum für die verschiedenen Reservationsstatus einer Velo-Reservation.
 * <p/>
 * Die Reihenfolge der Enum-Elemente darf nicht verändert werden, da das Ordinal der Elemente von JPA 
 * in der Datenbank gespeichert wird, z.B. Status "reserviert" ist mit 0 in der DB gespeichert.
 * 
 * @author Steffen Förster
 */
public enum ReservationStatus {

	/**
	 * Der Stellplatz wurde reserviert.
	 */
	RESERVIERT("reservation.lb.status.reserviert"),
	
	/**
	 * Eine vorhergehende Reservation wurde verlängert. 
	 */
	ROLLOVER("reservation.lb.status.verlaengerung"),
	
	/**
     * Eine vorhergehende Reservation wurde verlängert. 
     */
    GEKUENDIGT("reservation.lb.status.gekuendigt"),
    
    /**
     * Die Reservation wurde storniert.
     */
    STORNIERT("reservation.lb.status.storniert");
	
	private final String resourcekey;
	
	private ReservationStatus(String resourcekey) {
		this.resourcekey = resourcekey;
	}
	
	public String getResourceKey() {
		return this.resourcekey;
	}
	
	/**
	 * Liefert die aktiven Reservations-Status.
	 */
	public static ReservationStatus[] getAktivStatus() {
		return new ReservationStatus[] {
				ReservationStatus.RESERVIERT, 
				ReservationStatus.ROLLOVER,
				ReservationStatus.GEKUENDIGT};
	}
	
	/**
	 * Liefert die Reservations-Status, bei denen eine Verlängerung der Miete möglich ist.
	 */
	public static ReservationStatus[] getRolloverStatus() {
        return new ReservationStatus[] {
                ReservationStatus.RESERVIERT, 
                ReservationStatus.ROLLOVER};
    }
	
	public static List<ReservationStatus> getAktivStatusAsList() {
        return Arrays.asList(getAktivStatus());
    }
	
	public static ReservationStatus[] getInaktivStatus() {
		return new ReservationStatus[] {ReservationStatus.STORNIERT};
	}
	
	public static boolean isAktivStatus(ReservationStatus status) {
		for (ReservationStatus aStatus : getAktivStatus()) {
			if (aStatus == status) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isInaktivStatus(ReservationStatus status) {
		for (ReservationStatus aStatus : getInaktivStatus()) {
			if (aStatus == status) {
				return true;
			}
		}
		return false;
	}
    
}