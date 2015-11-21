package ch.giesserei.core;

/**
 * Diese Exception dient zum Transport einer Exception zum Exception-Handler.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class NestedException extends RuntimeException {

    private Throwable throwable;

    private NestedException(Throwable t) {
        this.throwable = t;
    }

    public static RuntimeException wrap(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        }
        return new NestedException(t);
    }

    public Throwable getCause() {
        return this.throwable;
    }

}
