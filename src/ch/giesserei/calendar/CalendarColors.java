package ch.giesserei.calendar;

/**
 * Datenhalter für alle Farben, die im Kalender verändert werden können.
 * 
 * @author Steffen Förster
 */
public class CalendarColors {

    private String colorTypeBooked = "#FF0000";
    
    private String colorTypeReserved = "#FF0000";
    
    private String colorTypeMisc = "#FF0000";
    
    private String colorTextWeekday = "#9C2215";
    
    private String colorTextWeekend = "#9C2215";
    
    private String colorTextMonth = "#333333";
    
    private String colorTextNameWeekday = "#333333";
    
    private String colorTextNameWeekend = "#333333";
    
    private String colorBorder = "#7BA428";
    
    private String colorBackMonth = "#7BA428";
    
    private String colorBackCalendar = "#FFFFFF";     
    
    public CalendarColors() {
    }

    public String getColorTypeBooked() {
        return colorTypeBooked;
    }

    public void setColorTypeBooked(String colorTypeBooked) {
        this.colorTypeBooked = colorTypeBooked;
    }

    public String getColorTypeReserved() {
        return colorTypeReserved;
    }

    public void setColorTypeReserved(String colorTypeReserved) {
        this.colorTypeReserved = colorTypeReserved;
    }

    public String getColorTypeMisc() {
        return colorTypeMisc;
    }

    public void setColorTypeMisc(String colorTypeMisc) {
        this.colorTypeMisc = colorTypeMisc;
    }

    public String getColorTextWeekday() {
        return colorTextWeekday;
    }

    public void setColorTextWeekday(String colorTextWeekday) {
        this.colorTextWeekday = colorTextWeekday;
    }

    public String getColorTextWeekend() {
        return colorTextWeekend;
    }

    public void setColorTextWeekend(String colorTextWeekend) {
        this.colorTextWeekend = colorTextWeekend;
    }

    public String getColorTextMonth() {
        return colorTextMonth;
    }

    public void setColorTextMonth(String colorTextMonth) {
        this.colorTextMonth = colorTextMonth;
    }

    public String getColorTextNameWeekday() {
        return colorTextNameWeekday;
    }

    public void setColorTextNameWeekday(String colorTextNameWeekday) {
        this.colorTextNameWeekday = colorTextNameWeekday;
    }

    public String getColorTextNameWeekend() {
        return colorTextNameWeekend;
    }

    public void setColorTextNameWeekend(String colorTextNameWeekend) {
        this.colorTextNameWeekend = colorTextNameWeekend;
    }

    public String getColorBorder() {
        return colorBorder;
    }

    public void setColorBorder(String colorBorder) {
        this.colorBorder = colorBorder;
    }

    public String getColorBackMonth() {
        return colorBackMonth;
    }

    public void setColorBackMonth(String colorBackMonth) {
        this.colorBackMonth = colorBackMonth;
    }

    public String getColorBackCalendar() {
        return colorBackCalendar;
    }

    public void setColorBackCalendar(String colorBackCalendar) {
        this.colorBackCalendar = colorBackCalendar;
    }
}
