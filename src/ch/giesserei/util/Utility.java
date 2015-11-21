package ch.giesserei.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.utility.StringUtil;

/**
 * Utility Methoden.
 * 
 * @author Steffen Förster
 */
public class Utility {

	public static Date addYear(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}
	
	public static Date addDay(Date date, int amount) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return cal.getTime();
    }
	
	public static Date addMonth(Date date, int amount) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, amount);
        return cal.getTime();
    }
	
	public static Date getFirstOfNextMonth() {
	    Calendar cal = new GregorianCalendar();
	    cal.add(Calendar.MONTH, 1);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    return cal.getTime();
	}
	
	public static Date getFirstOfNextMonth(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
	
	/**
	 * Liefert die Differenz der Monate einer Reservation.
	 * 
	 * @param from Beginn der Reservation
	 * @param toExklusive Ende der Reservation (exclusive)
	 */
	public static int getMonthsDifference(Date from, Date toExklusive) {
	    Calendar calFrom = new GregorianCalendar();
	    calFrom.setTime(from);
	    
	    Calendar calTo = new GregorianCalendar();
	    calTo.setTime(toExklusive);
        
        return (calTo.get(Calendar.YEAR) - calFrom.get(Calendar.YEAR)) * 12 
                + (calTo.get(Calendar.MONTH) - calFrom.get(Calendar.MONTH));
	}
	
	/**
	 * Entfernt die Uhrzeit.
	 * 
	 * @param date Datum
	 * @return siehe Beschreibung
	 */
	public static Date stripTime(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        
        cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
	
	public static String cropString(String value, int length) {
	    if (StringUtils.isNotBlank(value)) {
	        if (value.length() > length) {
	            return value.substring(0, length);
	        }
	    }
	    return value;
	}
	
	/**
	 * Liefert den Parameter-Wert aus einer Request-ParameterMap. XSS wird unterbunden.
	 * 
	 * @param parameterMap Request-ParameterMap
	 * @param paramName Name des Parameters
	 * @return siehe Beschreibung
	 */
	@SuppressWarnings("rawtypes")
    public static String getParamValue(Map parameterMap, String paramName) {
        Object value = parameterMap.get(paramName);
        if (value == null) {
            return "";
        }
        
        String[] valueField = (String[]) value;
        if (valueField.length == 0) {
            return "";
        }
        return StringUtils.replace(StringUtil.jQuoteNoXSS(valueField[0]), "\"", "");
    }
	
}
