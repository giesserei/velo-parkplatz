package ch.giesserei.core;

/**
 * Exception, welche immer dann geworfen wird, wenn beim Speichern einer Model-Instanz 
 * gegen einen Contraint verstossen wird.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class ModelConstraintException extends RuntimeException {

    /**
     * Konstruktor.
     * 
     * @param message Fehlermeldung
     */
    public ModelConstraintException(String message) {
        super(message);
    }
    
}
