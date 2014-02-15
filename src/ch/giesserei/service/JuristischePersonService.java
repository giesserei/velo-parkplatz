package ch.giesserei.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.model.Adresse;
import ch.giesserei.model.JpaJob;
import ch.giesserei.model.JpaTransactionExecutor;
import ch.giesserei.model.JuristischePerson;
import ch.giesserei.model.Person;
import ch.giesserei.model.Vertrag;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

/**
 * Service zur Verwaltung der Entity {@link Person}.
 * 
 * @author Steffen Förster
 */
@Singleton
public class JuristischePersonService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(JuristischePersonService.class);
    
    private final JpaTransactionExecutor<JuristischePerson> executor;
    
    @Inject
    public JuristischePersonService(MessageInterpolator interpolator, Provider<EntityManager> entityManager) {
    	super(interpolator, entityManager);
    	executor = new JpaTransactionExecutor<JuristischePerson>(entityManager);
    }
    
    /**
	 * Liefert alle Juristischen Personen in einem Container.
	 * 
	 * @return siehe Beschreibung
	 */
	@SuppressWarnings("unchecked")
    public Container getPersonen() {
		BeanItemContainer<JuristischePerson> container = new BeanItemContainer<JuristischePerson>(JuristischePerson.class);
		container.addNestedContainerProperty(JuristischePerson.NESTED_PROPERTY_ADRESSE_ORT);
		
		Query q = getEntityManager().createQuery("select p from JuristischePerson p");
        List<JuristischePerson> personList = q.getResultList();
        for (JuristischePerson person : personList) {
        	if (person.getAdresse() == null) {
        		person.setAdresse(new Adresse());
        	}
        	container.addBean(person);
        }
		return container;
	}
	
	@SuppressWarnings("unchecked")
    public int getCountVertrag(JuristischePerson person) {
		Query q = getEntityManager().createQuery("SELECT v FROM JuristischePerson p, Vertrag v "
				+ " WHERE p.id = ?1 AND p = v.mieterJuristischePerson");
		q.setParameter(1, person.getId());
		List<Vertrag> vertragList = (List<Vertrag>) q.getResultList();
		return vertragList.size();
	}
    
    /**
     * Liefert alle Personen zu einem Nachnamen. 
     * 
     * @param name Name
     * @return siehe Beschreibung
     * 
     * @throws NoResultException
     */
    @SuppressWarnings("unchecked")
    public List<JuristischePerson> getPersonByName(String name) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT p FROM JuristischePerson p WHERE p.name = ?1");
        q.setParameter(1, name);
        List<JuristischePerson> result = (List<JuristischePerson>) q.getResultList();
        return result;
    }
    
    public JuristischePerson persist(final JuristischePerson person) {
        LOG.info("persisting JuristischePerson id: " + person.getId());
        
        // leere Adresse als null speichern
        boolean adresseEmpty = person.getAdresse().istEmpty();
        if (adresseEmpty) {
        	person.setAdresse(null);
        }
        
        this.executor.persist(new JpaJob<JuristischePerson>() {
            @Override
            public void persist(EntityManager em) {
                validate(person);
                em.persist(person);
            }
        });
        
        // wieder Adressenobjekt setzen, damit die View keine Probleme bekommt
        if (adresseEmpty) {
        	person.setAdresse(new Adresse());
        }
        
        return person;
    }
    
    public JuristischePerson save(final JuristischePerson person) {
        LOG.info("saving JuristischePerson id: " + person.getId());
        
        // leere Adresse als null speichern
        boolean adresseEmpty = person.getAdresse().istEmpty();
        if (adresseEmpty) {
        	person.setAdresse(null);
        }
        
        JuristischePerson mergedItem = this.executor.merge(new JpaJob<JuristischePerson>() {
            @Override
            public JuristischePerson merge(EntityManager em) {
                validate(person);
                return em.merge(person);
            }
        });
        
        // wieder Adressenobjekt setzen, damit die View keine Probleme bekommt
        if (adresseEmpty) {
        	mergedItem.setAdresse(new Adresse());
        }
        
        return mergedItem;
    }
    
    public void remove(final JuristischePerson personDetached) {
        LOG.info("removing JuristischePerson id: " + personDetached.getId());
        
        this.executor.remove(new JpaJob<JuristischePerson>() {
            @Override
            public void remove(EntityManager em) {
                JuristischePerson person = em.find(JuristischePerson.class, personDetached.getId());
                em.remove(person);
            }
        });
    }
    
    // ----------------------------------------------------------
    // private
    // ----------------------------------------------------------
    
    private void validate(JuristischePerson person) {
        super.validate(person);
        
        if (person.getAdresse() != null) {
            super.validate(person.getAdresse());
        }
    }
}
