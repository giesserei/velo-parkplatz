package ch.giesserei.resource;

import java.text.MessageFormat;

/**
 * Wird die Klasse MessageFormat verwendet, muss ein im Pattern ggf. enthaltener Apostroph verdoppelt werden. Damit dies
 * nicht in allen Resource-Dateien und Mapper-Tabellen erfolgen muss, wird in Universal diese Utility-Klasse verwendet,
 * die zur Laufzeit ein Apostroph verdoppelt.
 * 
 * @author Steffen Förster
 */
public class MessageFormatHelper {

    private MessageFormatHelper() {
    }

    /**
     * Ersetzt im übergebenen Pattern die Platzhalter durch die Argumente.
     * 
     * @param pattern Muster
     * @param args Argumente
     * 
     * @return formatierter Text
     */
    public static String format(String pattern, Object... args) {
        String newPattern = doubleApostrophe(pattern);
        return MessageFormat.format(newPattern, args);
    }

    private static String doubleApostrophe(String pattern) {
        return pattern.replaceAll("'", "''");
    }
}
