package ch.giesserei.core;

/**
 * Wird bei einem schweren Fehler innerhalb der Anwendung geworfen.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

}
