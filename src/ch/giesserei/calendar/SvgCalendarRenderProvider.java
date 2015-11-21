package ch.giesserei.calendar;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import ch.giesserei.core.NestedException;
import ch.giesserei.core.TemplateProvider;
import ch.giesserei.resource.AppRes;
import ch.giesserei.resource.LocaleProvider;

import com.google.inject.Inject;

/**
 * Rendert einen Kalender als SVG..
 * 
 * @author Steffen Förster
 */
public class SvgCalendarRenderProvider implements CalendarRenderProvider{

	/**
	 * Delta x für das nächste rechte Symbol.
	 */
	private static final double SYMBOL_OCCUPIED_DELTA_X = 29.11;
	
	/**
     * Delta y für das nächste untere Symbol.
	 */
	private static final double SYMBOL_OCCUPIED_DELTA_Y = 18.32;
	
	/**
	 * X-Koordinate des linken oberen Symbols.
	 */
	private static final double SYMBOL_OCCUPIED_X = 125.46;
	
	/**
	 * Y-Koordinaten des linken oberen Symbols.
	 */
    private static final double SYMBOL_OCCUPIED_Y = 89.66;
	
	//private static final double SYMBOL_OCCUPIED_WIDTH = 24.93;
	
	private static final double SYMBOL_OCCUPIED_HEIGHT = 14.32;
	
	private static final Map<Integer, Double> DAY_TO_OCCUPIED_DELTA_X;
	
	/**
	 * X-Koordinate des oberen linken Kalendertags.
	 */
    private static final double TEXT_DAY_X = 147.5;
   
    /**
	 * Y-Koordinate des oberen linken Kalendertags.
	 */
    private static final double TEXT_DAY_Y = 101.8;
    
    private final LocaleProvider localeProvider;
    
    private final TemplateProvider templateProvider;
	
	static {
		 DAY_TO_OCCUPIED_DELTA_X = new HashMap<Integer, Double>();
		 DAY_TO_OCCUPIED_DELTA_X.put(Calendar.MONDAY, 0 * SYMBOL_OCCUPIED_DELTA_X);
		 DAY_TO_OCCUPIED_DELTA_X.put(Calendar.TUESDAY, 1 * SYMBOL_OCCUPIED_DELTA_X);
		 DAY_TO_OCCUPIED_DELTA_X.put(Calendar.WEDNESDAY, 2 * SYMBOL_OCCUPIED_DELTA_X);
		 DAY_TO_OCCUPIED_DELTA_X.put(Calendar.THURSDAY, 3 * SYMBOL_OCCUPIED_DELTA_X);
		 DAY_TO_OCCUPIED_DELTA_X.put(Calendar.FRIDAY, 4 * SYMBOL_OCCUPIED_DELTA_X);
		 DAY_TO_OCCUPIED_DELTA_X.put(Calendar.SATURDAY, 5 * SYMBOL_OCCUPIED_DELTA_X);
		 DAY_TO_OCCUPIED_DELTA_X.put(Calendar.SUNDAY, 6 * SYMBOL_OCCUPIED_DELTA_X);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param localeProvider Locale
	 * @param templateProvider Template-Engine
	 */
	@Inject
	public SvgCalendarRenderProvider(LocaleProvider localeProvider, TemplateProvider templateProvider) {
	    this.localeProvider = localeProvider;
	    this.templateProvider = templateProvider;
	}
	
	@Override
	public String getCalendar(List<BuchungMonat> months, int columns, int rows, CalendarColors colors, double scale) {
		try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            this.writeCalendar(months, columns, rows, colors, scale, out);
            return out.toString("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw NestedException.wrap(e);
        }
	}
	
	@Override
    public void writeCalendar(List<BuchungMonat> months, int columns, int rows, CalendarColors colors, double scale,
            OutputStream stream) {
        List<SvgCalendarMonth> calMonths = transformCalendarMonth(months, colors);
        Map<String, Object> model = getModel(calMonths, columns, rows, colors, scale);
        this.templateProvider.processOutput("calendar/svg/calendar.ftl", model, stream);
    }
	
	// ------------------------------------------------------
    // private section
    // ------------------------------------------------------
	
	/**
	 * Erstellt das Model für die Template-Verarbeitung.
	 */
	private Map<String, Object> getModel(List<SvgCalendarMonth> calMonths, int columns, int rows, 
	        CalendarColors colors, double scale) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("months", calMonths);
        model.put("columns", columns);
        model.put("rows", rows);
        model.put("colors", colors);
        model.put("locale", this.localeProvider.getUserLocale());
        model.put("scale", scale);
        model.put("dayNames", getDayNames());
        return model;
    }
	
	/**
	 * Wandelt die übergebene Monats-Liste in eine Liste mit Monaten um, die besser für die Generierung des 
	 * SVG-Kalenders geeignet ist.
	 */
	private List<SvgCalendarMonth> transformCalendarMonth(List<BuchungMonat> months, CalendarColors colors) {
	    List<SvgCalendarMonth> calMonths = new ArrayList<SvgCalendarMonth>();
	    for (BuchungMonat month : months) {
	        String title = getMonthTitle(month.getMonth());
	        SvgCalendarMonth calMonth = new SvgCalendarMonth(title);
	        calMonths.add(calMonth);
	        
	        Calendar cal = DateUtils.toCalendar(month.getMonth());
	        cal.set(Calendar.DAY_OF_MONTH, 1);
	        int monthToRender = cal.get(Calendar.MONTH);
	        
	        while (cal.get(Calendar.MONTH) == monthToRender) {
	            int dayToRender = cal.get(Calendar.DAY_OF_MONTH);
	            Position pos = calcPosDayText(cal.getTime());
	            boolean isWeekend = isWeekend(cal.getTime());
	            SvgCalendarDay calDay = new SvgCalendarDay(
	                    pos.x, 
	                    pos.y, 
	                    isWeekend ? colors.getColorTextWeekend() : colors.getColorTextWeekday(), 
	                    String.valueOf(dayToRender));
	            calMonth.addDay(calDay);
	            
	            cal.add(Calendar.DAY_OF_MONTH, 1);
	        }
	        
	        for (BuchungTag arrivalDay : month.getArrivalDays()) {
	            Position pos = calcPosSymbolArrival(arrivalDay.getDate());
	            String color = getColorByType(arrivalDay.getTyp(), colors);
	            SvgBuchungTag svgDay = new SvgBuchungTag(pos.x, pos.y, color);
	            calMonth.addArrivalDay(svgDay);
	        }
	        
	        for (BuchungTag departureDay : month.getDepartureDays()) {
                Position pos = calcPosSymbolDeparture(departureDay.getDate());
                String color = getColorByType(departureDay.getTyp(), colors);
                SvgBuchungTag svgDay = new SvgBuchungTag(pos.x, pos.y, color);
                calMonth.addDepartureDay(svgDay);
            }
	        
	        for (BuchungTag occupiedDay : month.getOccupiedDays()) {
                Position pos = calcPosSymbolOccupied(occupiedDay.getDate());
                String color = getColorByType(occupiedDay.getTyp(), colors);
                SvgBuchungTag svgDay = new SvgBuchungTag(pos.x, pos.y, color);
                calMonth.addOccupiedDay(svgDay);
            }
	    }
	    return calMonths;
	}
	
	private String getMonthTitle(Date month) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM yyyy");
        return sdf.format(month);
	}
	
	/**
	 * Liefert die Postion für den übergebenen Kalendertag.
	 */
    private Position calcPosDayText(Date date) {
	    Calendar cal = DateUtils.toCalendar(date);
	    cal.setFirstDayOfWeek(Calendar.MONDAY);
	    cal.setMinimalDaysInFirstWeek(1);
	   
	    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	    int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH) - 1;
	   
	    double x = calcDayTextX(dayOfWeek);
	    double y = calcDayTextY(weekOfMonth);
	   
	    return new Position(x, y);
    }
	
	/**
	 * Liefert true, wenn der übergebenen Tag auf ein Wochenende fällt.
	 */
    private boolean isWeekend(Date date) {
	    Calendar cal = DateUtils.toCalendar(date);
	 
	    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }
	
	/**
	 * Liefert die Namen der Wochentage.
     */
    private Map<String, String> getDayNames() {
        Map<String, String> dayNames = new HashMap<String, String>();
        dayNames.put("mo", AppRes.getString("day.mo"));
        dayNames.put("tu", AppRes.getString("day.tu"));
        dayNames.put("we", AppRes.getString("day.we"));
        dayNames.put("th", AppRes.getString("day.th"));
        dayNames.put("fr", AppRes.getString("day.fr"));
        dayNames.put("sa", AppRes.getString("day.sa"));
        dayNames.put("su", AppRes.getString("day.su"));
        return dayNames;
    }
    
    /**
     * Liefert die Farbe für den übergebenen Buchungstyp.
     */
    private String getColorByType(BuchungTyp typ, CalendarColors colors) {
        switch (typ) {
            case MISC : 
                return colors.getColorTypeMisc();
            case RESERVED : 
                return colors.getColorTypeReserved();
            default : 
                return colors.getColorTypeBooked();
        }
    }
    
    /**
     * Liefert die Postion des Symbols "occupied" am übergebenen Datum.
     */
    private Position calcPosSymbolOccupied(Date date) {
        Calendar cal = DateUtils.toCalendar(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(1);
        
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH) - 1;
        
        double x = calcSymbolOccupiedX(dayOfWeek);
        double y = calcSymbolOccupiedY(weekOfMonth);
        
        return new Position(x, y);
    }
    
    /**
     * Liefert die Postion des Symbols "arrival" am übergebenen Datum.
     */
    private Position calcPosSymbolArrival(Date date) {
        Position pos = calcPosSymbolOccupied(date);
        pos.y = pos.y + SYMBOL_OCCUPIED_HEIGHT;
        return pos;
    }
    
    /**
     * Liefert die Postion des Symbols "departure" am übergebenen Datum.
     */
    private Position calcPosSymbolDeparture(Date date) {
        Position pos = calcPosSymbolOccupied(date);
        pos.y = pos.y + SYMBOL_OCCUPIED_HEIGHT;
        return pos;
    }
	
	private double calcSymbolOccupiedX(int dayOfWeek) {
		return SYMBOL_OCCUPIED_X + DAY_TO_OCCUPIED_DELTA_X.get(dayOfWeek);
	}
	
	private double calcSymbolOccupiedY(int weekOfMonth) {
		return SYMBOL_OCCUPIED_Y + weekOfMonth * SYMBOL_OCCUPIED_DELTA_Y;
	}
	
	private double calcDayTextX(int dayOfWeek) {
		return TEXT_DAY_X + DAY_TO_OCCUPIED_DELTA_X.get(dayOfWeek);
	}
	
	private double calcDayTextY(int weekOfMonth) {
		return TEXT_DAY_Y + weekOfMonth * SYMBOL_OCCUPIED_DELTA_Y;
	}
	
	// ---------------------------------------------------------
    // class Position
    // ---------------------------------------------------------
    
    private static class Position {
        private double x;
        private double y;
        
        private Position(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}