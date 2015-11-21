package ch.giesserei.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.model.JpaJob;
import ch.giesserei.model.JpaTransactionExecutor;
import ch.giesserei.model.Mietobjekt;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

/**
 * Service-Klasse für das Modell {@link Mietobjekt}.
 * 
 * @author Steffen Förster
 */
@Singleton
public class MietobjektService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(MietobjektService.class);
    
    private final JpaTransactionExecutor<Mietobjekt> executor;
    
	@Inject
	public MietobjektService(MessageInterpolator interpolator, Provider<EntityManager> entityManager) {
		super(interpolator, entityManager);
		this.executor = new JpaTransactionExecutor<Mietobjekt>(entityManager);
	}
	
	/**
	 * Liefert alle Objekte in einem Container.
	 * 
	 * @return siehe Beschreibung
	 */
	@SuppressWarnings("unchecked")
    public Container getObjekte() {
		BeanItemContainer<Mietobjekt> container = new BeanItemContainer<Mietobjekt>(Mietobjekt.class);
		
		Query q = getEntityManager().createQuery("select o from Mietobjekt o");
        List<Mietobjekt> objektList = q.getResultList();
        for (Mietobjekt objekt : objektList) {
        	container.addBean(objekt);
        }
        
		return container;
	}
	
	/**
     * Liefert das Objekt mit der übergebenen Nummer.
     * 
     * @return siehe Beschreibung
     */
    public Mietobjekt getObjektByNummer(int nummer) {
        Query q = getEntityManager().createQuery("select o from Mietobjekt o where o.nummer = ?1");
        q.setParameter(1, nummer);
        return (Mietobjekt) q.getSingleResult();
    }
    
    /**
     * Liefert das Objekt mit der übergebenen Nummer.
     * 
     * @return siehe Beschreibung
     */
    public boolean existObjekt(int nummer) {
        try {
            getObjektByNummer(nummer);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public Mietobjekt persist(final Mietobjekt mietobjekt) {
        LOG.info("persisting mietobjekt id: " + mietobjekt.getId());
        
        this.executor.persist(new JpaJob<Mietobjekt>() {
            @Override
            public void persist(EntityManager em) {
                validate(mietobjekt);
                em.persist(mietobjekt);
                LOG.info("persisted mietobjekt id: " + mietobjekt.getId());
            }
        });
        
        return mietobjekt;
    }
    
    /**
     * Persistiert die übergebene Liste von Mietobjekten. Wird nur für das erste Abfüllen der 
     * Datenbank benötigt. 
     */
    public void persist(final List<Mietobjekt> mietobjekte) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            for (Mietobjekt objekt : mietobjekte) {
                em.persist(objekt);
            }
            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

}
