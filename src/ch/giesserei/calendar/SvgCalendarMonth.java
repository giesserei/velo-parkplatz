package ch.giesserei.calendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Repäsentiert einen Kalendermonat mit Buchungstagen.
 * 
 * @author Steffen Förster
 */
public class SvgCalendarMonth {

    private final String title;
    
    private final List<SvgCalendarDay> days = new ArrayList<SvgCalendarDay>();
    
    private final List<SvgBuchungTag> arrivalDays = new ArrayList<SvgBuchungTag>();
    
    private final List<SvgBuchungTag> departureDays = new ArrayList<SvgBuchungTag>();
    
    private final List<SvgBuchungTag> occupiedDays = new ArrayList<SvgBuchungTag>();
    
    public SvgCalendarMonth(String title) {
        this.title = title;
    }
    
    public List<SvgCalendarDay> getDays() {
        return this.days;
    }
    
    public void addDay(SvgCalendarDay day) {
        this.days.add(day);
    }

    public void addArrivalDay(SvgBuchungTag day) {
        this.arrivalDays.add(day);
    } 
    
    public void addDepartureDay(SvgBuchungTag day) {
        this.departureDays.add(day);
    }
    
    public void addOccupiedDay(SvgBuchungTag day) {
        this.occupiedDays.add(day);
    }
    
    public String getTitle() {
        return title;
    }

    public List<SvgBuchungTag> getArrivalDays() {
        return arrivalDays;
    }

    public List<SvgBuchungTag> getDepartureDays() {
        return departureDays;
    }

    public List<SvgBuchungTag> getOccupiedDays() {
        return occupiedDays;
    }
    
}
