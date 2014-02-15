package ch.giesserei.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.model.JpaJob;
import ch.giesserei.model.JpaTransactionExecutor;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

/**
 * Service-Klasse für das Modell {@link Stellplatz}.
 * 
 * @author Steffen Förster
 */
@Singleton
public class StellplatzService extends BaseService {

	private static final Logger LOG = LoggerFactory.getLogger(StellplatzService.class);
	
	private final JpaTransactionExecutor<Stellplatz> executor;
	
	@Inject
	public StellplatzService(MessageInterpolator interpolator, Provider<EntityManager> entityManager) {
		super(interpolator, entityManager);
		this.executor = new JpaTransactionExecutor<Stellplatz>(entityManager);
	}
	
	/**
	 * Liefert alle Stellplätze in einem Container.
	 * 
	 * @return siehe Beschreibung
	 */
	@SuppressWarnings("unchecked")
    public Container getStellplaetze() {
		BeanItemContainer<Stellplatz> container = new BeanItemContainer<Stellplatz>(Stellplatz.class);
		
		Query q = getEntityManager().createQuery("select o from Stellplatz o");
        List<Stellplatz> stellplatzList = q.getResultList();
        for (Stellplatz stellplatz : stellplatzList) {
        	container.addBean(stellplatz);
        }
        
		return container;
	}
	
	/**
	 * Liefert alle Stellplätze eines Typs in einem Container.
	 * 
	 * @param typ Typ der gesuchten Stellplätze
	 * @param bothPedalParc wenn true, werden beide PedalParc-Typen geliefert
	 * 
	 * @return siehe Beschreibung
	 */
	@SuppressWarnings("unchecked")
    public Container getStellplaetze(StellplatzTyp typ, boolean bothPedalParc) {
		BeanItemContainer<Stellplatz> container = new BeanItemContainer<Stellplatz>(Stellplatz.class);
		
		Query q = getEntityManager().createQuery("select s from Stellplatz s where s.typ = ?1 or s.typ = ?2 order by s.nummer");
		if (bothPedalParc && (typ == StellplatzTyp.PEDALPARC_HOCH || typ == StellplatzTyp.PEDALPARC_TIEF)) {
			q.setParameter(1, StellplatzTyp.PEDALPARC_HOCH);
			q.setParameter(2, StellplatzTyp.PEDALPARC_TIEF);
		}
		else {
			q.setParameter(1, typ);
			q.setParameter(2, typ);
		}
		
        List<Stellplatz> stellplatzList = q.getResultList();
        for (Stellplatz stellplatz : stellplatzList) {
        	container.addBean(stellplatz);
        }
        
		return container;
	}
	
	/**
	 * Liefert die Anzahl der aktiven und inaktiven Reservationen für den übergebenen Stellplatz.
	 * 
	 * @param stellplatz zu prüfender Stellplatz
	 * 
	 * @return siehe Beschreibung
	 */
	@SuppressWarnings("unchecked")
    public int getCountReservation(Stellplatz stellplatz) {
		Query q = getEntityManager().createQuery("SELECT r FROM Stellplatz s, ReservationStellplatz r "
				+ " WHERE s.id = ?1 AND s = r.stellplatz");
		q.setParameter(1, stellplatz.getId());
		List<ReservationStellplatz> reservationList = (List<ReservationStellplatz>) q.getResultList();
		return reservationList.size();
	}
	
	/**
	 * Liefert den Stellplatz, wenn der Stellplatz mit der übergebenen Nummer und dem übergebenen Typ existiert.
	 * Es wird dabei nur zwischen PEDALPARC und SPEZIAL unterschieden.
	 * 
	 * @param nummer Stellplatznummer
	 * @param typ Typ
	 * @return NULL, wenn der Stellplatz nicht exisitiert
	 */
	public Stellplatz getStellplatz(int nummer, StellplatzTyp typ) {
	    try {
            return getStellplatzByNummer(nummer, typ, true);
        }
        catch (Exception e) {
            return null;
        }
	}
	
	/**
	 * Liefert den Stellplatz mit der übergebenen Nummer und den übergebenen Typ.
	 * 
	 * @param nummer Stellplatznummer
	 * @param bothPedalParc wenn true, werden beide PedalParc-Typen geliefert
	 * @return siehe Beschreibung
	 */
	public Stellplatz getStellplatzByNummer(int nummer, StellplatzTyp typ, boolean bothPedalParc) {
		Query q = getEntityManager().createQuery("SELECT s FROM Stellplatz s WHERE s.nummer = ?1 and (s.typ = ?2 or s.typ = ?3)");
		q.setParameter(1, nummer);
		
		if (bothPedalParc && (typ == StellplatzTyp.PEDALPARC_HOCH || typ == StellplatzTyp.PEDALPARC_TIEF)) {
			q.setParameter(2, StellplatzTyp.PEDALPARC_HOCH);
			q.setParameter(3, StellplatzTyp.PEDALPARC_TIEF);
		}
		else {
			q.setParameter(2, typ);
			q.setParameter(3, typ);
		}
		
		return (Stellplatz) q.getSingleResult();
	}
    
	/**
	 * Erstellt den übergebenen Stellplatz in der Datenbank.
	 * 
	 * @param stellplatz zu erstellender Stellplatz
	 * @return erstellter Stellplatz
	 */
    public Stellplatz persist(final Stellplatz stellplatz) {
        LOG.info("persisting stellplatz id: " + stellplatz.getId());
        
        this.executor.persist(new JpaJob<Stellplatz>() {
            @Override
            public void persist(EntityManager em) {
                validate(stellplatz);
                em.persist(stellplatz);
                LOG.info("persisted stellplatz id: " + stellplatz.getId());
            }
        });
        
        return stellplatz;
    }
    
    /**
     * Speichert die Änderungen des übergebenen Stellplatzes.
     * 
     * @param stellplatz zu speichernder Stellplatz
     * @return gespeicherter Stellplatz
     */
    public Stellplatz save(final Stellplatz stellplatz) {
        LOG.info("saving stellplatz id: " + stellplatz.getId());
        
        Stellplatz mergedStellplatz = this.executor.merge(new JpaJob<Stellplatz>() {
            @Override
            public Stellplatz merge(EntityManager em) {
                validate(stellplatz);
                return em.merge(stellplatz);
            }
        });
        
        return mergedStellplatz;
    }
    
    /**
     * Löscht den übergebenen Stellplatz aus der Datenbank.
     * 
     * @param stellplatzDetached zu löschender Stellplatz
     */
    public void remove(final Stellplatz stellplatzDetached) {
        LOG.info("removing stellplatz id: " + stellplatzDetached.getId());
        
        this.executor.remove(new JpaJob<Stellplatz>() {
            @Override
            public void remove(EntityManager em) {
                Stellplatz stellplatz = em.find(Stellplatz.class, stellplatzDetached.getId());
                em.remove(stellplatz);
            }
        });
    }
    
    /**
     * Erstellt die Stellplätze in der übergebenen Liste.
     *  
     * @param stellplatzListe zu erstellende Stellplätze
     */
    public void persist(final List<Stellplatz> stellplatzListe) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            for (Stellplatz stellplatz : stellplatzListe) {
                em.persist(stellplatz);
            }
            
            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }
    
    // ----------------------------------------------------------
    // private
    // ----------------------------------------------------------
    
    private void validate(Stellplatz stellplatz) {
        super.validate(stellplatz);
    }

}
