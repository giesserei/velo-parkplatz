package ch.giesserei.model;

import javax.persistence.EntityManager;

/**
 * Wird einem {@link JpaTransactionExecutor} übergeben und von diesem innerhalb einer 
 * Transaktion ausgeführt.
 * 
 * @author Steffen Förster
 *
 * @param <T> Entity-Typ
 */
public abstract class JpaJob<T> {

    public void persist(EntityManager em) {
        throw new UnsupportedOperationException("nicht implementiert");
    }
    
    public T merge(EntityManager em) {
        throw new UnsupportedOperationException("nicht implementiert");
    }
    
    public void remove(EntityManager em) {
        throw new UnsupportedOperationException("nicht implementiert");
    }
    
}
