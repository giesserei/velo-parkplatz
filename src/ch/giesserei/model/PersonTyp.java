package ch.giesserei.model;


/**
 * Enum für die verschiedenen Typen einer Person.
 * <p/>
 * Die Reihenfolge der Enum-Elemente darf nicht verändert werden, da das Ordinal der Elemente von JPA 
 * in der Datenbank gespeichert wird, z.B. Typ "Bewohner" ist mit 0 in der DB gespeichert.
 * 
 * @author Steffen Förster
 */
public enum PersonTyp {

	/**
	 * Bewohner der Giesserei
	 */
	BEWOHNER("person.lb.typ.bewohner"),
	
	/**
     * Gewerbe in der Giesserei
     */
    GEWERBE("person.lb.typ.gewerbe"),
	
	/**
	 * Passivmitglied des Hausvereins
	 */
	PASSIVMITGLIED("person.lb.typ.passivmitglied"),
	
	/**
     * Genossenschaftler der Gesewo ohne die Bewohner der Giesserei 
     */
    GESEWO_GENOSSENSCHAFTLER("person.lb.typ.gesewo.genossenschaftler"),
    
    /**
     * Sonstige Person
     */
    EXTERN("person.lb.typ.extern");
	
	private final String resourcekey;
	
	private PersonTyp(String resourcekey) {
		this.resourcekey = resourcekey;
	}
	
	public String getResourceKey() {
		return this.resourcekey;
	}
    
}