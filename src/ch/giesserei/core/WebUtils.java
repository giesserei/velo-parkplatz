package ch.giesserei.core;

import javax.servlet.ServletContext;

/**
 * Die Klasse liefert insbesondere Pfade für spezielle Seiten.
 * 
 * @author Steffen Förster
 */
public class WebUtils {

    private static ServletContext context;
    
    public static void setContext(ServletContext context) {
        WebUtils.context = context;
    }
    
    public static String getPathLogin() {
        return context.getContextPath() + "/login"; 
    }
    
    public static String getPathDesktop() {
        return context.getContextPath() + "/desktop"; 
    }
    
    public static String getPathReservation() {
        return context.getContextPath() + "/reservation"; 
    }
    
}
