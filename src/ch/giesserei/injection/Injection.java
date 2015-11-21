package ch.giesserei.injection;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * Mit dieser Klasse können die Komponenten auf Abhängigkeiten zugreifen.
 * 
 * @author Steffen Förster
 */
public class Injection {

    private static Injector injector;

    private Injection() {
    }

    public static void setInjector(Injector injector) {
        Injection.injector = injector;
    }

    /**
     * Liefert die Instanz des übergebenen Typs.
     * 
     * @param type Injection-Typ
     * @param <T> Type
     * @return siehe Beschreibung
     */
    public static <T> T get(Class<T> type) {
        return injector.getInstance(type);
    }

    /**
     * Liefert einen Provider für das holen einer Instanz des übergebenen Typs.
     * 
     * @param type Injection-Typ
     * @param <T> Type
     * @return siehe Beschreibung
     */
    public static <T> Provider<T> getProvider(Class<T> type) {
        return injector.getProvider(type);
    }

    /**
     * Iniziert Abhängigkeiten in das übergebene Objekt.
     * <p/>
     * So können z.B. in einen Mapper, welcher durch Consor instanziert wird, auch Abhängigkeiten iniziert werden. Der
     * Aufruf dieser Methode sollte dann im Konstruktor durchgeführt werden.
     * 
     * @param object Objektinstanz
     */
    public static void injectMembers(Object object) {
        injector.injectMembers(object);
    }

    /**
     * Erstellt einen Child-Injector mit Hilfe der übergebenen Module. In der Anwendung wird danach der Child-Injector
     * verwendet.
     * 
     * @param modules Zusätzliche Module
     */
    public static void createChildInjector(Module... modules) {
        injector = injector.createChildInjector(modules);
    }
}
