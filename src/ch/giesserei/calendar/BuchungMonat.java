package ch.giesserei.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repräsentiert einen Monat in einem Buchungskalender.
 * 
 * @author Steffen Förster
 */
public class BuchungMonat {

	private final List<BuchungTag> arrivalDays = new ArrayList<BuchungTag>();
	
	private final List<BuchungTag> departureDays = new ArrayList<BuchungTag>();
	
	private final List<BuchungTag> occupiedDays = new ArrayList<BuchungTag>();
	
	private final Date month;
	
	public BuchungMonat(Date month) {
	    this.month = month;
	}
	
	public Date getMonth() {
        return month;
    }

    public void addArrivalDay(Date date, BuchungTyp typ) {
		this.arrivalDays.add(new BuchungTag(date, typ));
	} 
	
	public void addDepartureDay(Date date, BuchungTyp typ) {
	    this.departureDays.add(new BuchungTag(date, typ));
	}
	
	public void addOccupiedDay(Date date, BuchungTyp typ) {
	    this.occupiedDays.add(new BuchungTag(date, typ));
	}

    public List<BuchungTag> getArrivalDays() {
        return arrivalDays;
    }

    public List<BuchungTag> getDepartureDays() {
        return departureDays;
    }

    public List<BuchungTag> getOccupiedDays() {
        return occupiedDays;
    }
	
}
