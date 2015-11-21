package ch.giesserei.model;

import javax.persistence.EntityManager;

import com.google.inject.Provider;

/**
 * Mit dem Executor wird vermieden, dass der immer gleiche Code für das Erstellen
 * von Transaktionen sich in den Service-Klassen wiederholt.
 * 
 * @author Steffen Förster
 *
 * @param <T> Entity-Typ
 */
public class JpaTransactionExecutor<T> {

    private final Provider<EntityManager> entityManager;
    
    public JpaTransactionExecutor(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }
    
    public void persist(JpaJob<T> job) {
        EntityManager em = this.entityManager.get();
        try {
            em.getTransaction().begin();
            job.persist(em);
            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }
    
    public void remove(JpaJob<T> job) {
        EntityManager em = this.entityManager.get();
        try {
            em.getTransaction().begin();
            job.remove(em);
            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }
    
    
    public T merge(JpaJob<T> job) {
        EntityManager em = this.entityManager.get();
        T mergedEntity = null;
        try {
            em.getTransaction().begin();
            mergedEntity = job.merge(em);
            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
        return mergedEntity;
    }
}
