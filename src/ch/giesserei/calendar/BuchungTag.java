package ch.giesserei.calendar;

import java.util.Date;

/**
 * Repräsentiert einen gebuchten Tag in einem Buchungskalender.
 * 
 * @author Steffen Förster
 */
public class BuchungTag {
	
	private final BuchungTyp type;
	
	private final Date date;

    public BuchungTag(Date date, BuchungTyp type) {
        this.type = type;
        this.date = date;
    }

    public BuchungTyp getTyp() {
        return type;
    }

    public Date getDate() {
        return date;
    }
    
}