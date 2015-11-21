package ch.giesserei.calendar;

import java.io.OutputStream;
import java.util.List;

/**
 * Ein {@link CalendarRenderProvider} rendert einen Buchungskalender für eine Darstellung im Browser.
 * 
 * @author Steffen Förster
 */
public interface CalendarRenderProvider {

    /**
     * Liefert die Kalenderdarstellung.
     * 
     * @param months Monate, die der Belegungskalender anzeigen soll
     * @param columns Anzahl der Monate, die in einer Reihe dargestellt werden sollen
     * @param rows Anzahl der Monate, die untereinander dargestellt werden sollen
     * @param colors Definition der Farben
     * @param scale Skalierung des Kalenders
     * 
     * @return Kalenderdarstellung
     */
    public String getCalendar(List<BuchungMonat> months, int columns, int rows, CalendarColors colors, double scale);
    
    /**
     * Schreibt die kalenderdarstellung in den übergebenen OutputStream.
     * 
     * @param months Monate, die der Belegungskalender anzeigen soll
     * @param columns Anzahl der Monate, die in einer Reihe dargestellt werden sollen
     * @param rows Anzahl der Monate, die untereinander dargestellt werden sollen
     * @param colors Definition der Farben
     * @param scale Skalierung des Kalenders
     * @param stream Stream, in welchen der Kalender geschrieben werden soll
     * 
     * @return Kalenderdarstellung
     */
    public void writeCalendar(List<BuchungMonat> months, int columns, int rows, CalendarColors colors, 
            double scale, OutputStream stream);
    
}
