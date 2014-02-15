package ch.giesserei.calendar;

/**
 * Repräsentiert einen Buchungstag in der Kalenderdarstellung.
 * 
 * @author Steffen Förster
 */
public class SvgBuchungTag {

    private final double x;
    
    private final double y;
    
    private final String color;

    public SvgBuchungTag(double x, double y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getColor() {
        return color;
    }
    
}
