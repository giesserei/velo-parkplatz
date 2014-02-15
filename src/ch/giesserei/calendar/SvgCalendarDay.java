package ch.giesserei.calendar;

/**
 * Repräsentiert einen Tag in einem Kalendermonat.
 * 
 * @author Steffen Förster
 */
public class SvgCalendarDay {

    private final double x;
    
    private final double y;
    
    private final String color;
    
    private final String text;

    public SvgCalendarDay(double x, double y, String color, String text) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.text = text;
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

    public String getText() {
        return text;
    }
    
}
