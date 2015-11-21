package ch.giesserei.calendar;

/**
 * Repräsentiert den Typ eines gebuchten Tages.
 * 
 * @author Steffen Förster
 */
public enum BuchungTyp {
    
    /**
     * Der Tag ist fest gebucht. 
     */
	BOOKED,
	
	/**
	 * Ein sonstiger Buchungstyp.
	 */
	MISC,
	
	/**
	 * Der Tag ist bisher nur reserviert.
	 */
	RESERVED,
	
	/**
	 * Der Tag ist noch frei.
	 */
	FREE
}
