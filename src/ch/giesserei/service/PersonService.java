package ch.giesserei.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.core.MailProvider;
import ch.giesserei.model.Adresse;
import ch.giesserei.model.JpaJob;
import ch.giesserei.model.JpaTransactionExecutor;
import ch.giesserei.model.Person;
import ch.giesserei.model.Vertrag;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

/**
 * Service-Klasse für das Modell {@link Person}.
 * 
 * @author Steffen Förster
 */
@Singleton
public class PersonService extends BaseService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

	private final JpaTransactionExecutor<Person> executor;
	
	private final MailProvider mailProvider;
	
	@Inject
    public PersonService(MessageInterpolator interpolator, Provider<EntityManager> entityManager, 
            MailProvider mailProvider) {
		super(interpolator, entityManager);
		this.executor = new JpaTransactionExecutor<Person>(entityManager);
		this.mailProvider = mailProvider;
    }
	
	/**
     * Liefert alle Personen als Liste.
     * 
     * @return siehe Beschreibung
     */
    @SuppressWarnings("unchecked")
    public List<Person> getPersonenAsList() {
        Query q = getEntityManager().createQuery("select p from Person p order by p.nachname, p.vorname");
        return q.getResultList();
    }
	
	/**
	 * Liefert alle Personen in einem Container.
	 * 
	 * @return siehe Beschreibung
	 */
	@SuppressWarnings("unchecked")
    public Container getPersonen() {
		BeanItemContainer<Person> container = new BeanItemContainer<Person>(Person.class);
		container.addNestedContainerProperty(Person.NESTED_PROPERTY_ADRESSE_ORT);
		
		Query q = getEntityManager().createQuery("select p from Person p order by p.nachname, p.vorname");
        List<Person> personList = q.getResultList();
        for (Person person : personList) {
        	if (person.getAdresse() == null) {
        		person.setAdresse(new Adresse());
        	}
        	container.addBean(person);
        }
		return container;
	}
	
	/**
	 * Liefert die Anzahl der Verträge, mit denen die Person in Beziehung steht.
	 * 
	 * @param person zu prüfende Person
	 * @return siehe Beschreibung
	 */
	@SuppressWarnings("unchecked")
    public int getCountVertrag(Person person) {
		Query q = getEntityManager().createQuery("SELECT v FROM Person p, Vertrag v WHERE p.id = ?1 AND"
				+ " (p MEMBER OF v.mieterPersonen OR p MEMBER OF v.mitbewohner)");
		q.setParameter(1, person.getId());
		List<Vertrag> vertragList = (List<Vertrag>) q.getResultList();
		return vertragList.size();
	}
	
	/**
	 * Liefert die Person mit dem übergebenen Nachnamen.
	 * 
	 * @param nachname Nachname
	 * @return siehe Beschreibung
	 */
	public Person getPersonByNachname(String nachname) {
		Query q = getEntityManager().createQuery("SELECT p FROM Person p WHERE UPPER(p.nachname) = UPPER(?1)");
		q.setParameter(1, nachname);
		return (Person) q.getSingleResult();
	}
    
	/**
	 * Erstellt die übergebene Person in der Datenbank.
	 * 
	 * @param person zu speichernde Person
	 * @return siehe Beschreibung
	 */
    public Person persist(final Person person) {
        LOG.info("persisting person id: " + person.getId());
        
        // leere Adresse als null speichern
        boolean adresseEmpty = person.getAdresse().istEmpty();
        if (adresseEmpty) {
        	person.setAdresse(null);
        }
        
        this.executor.persist(new JpaJob<Person>() {
            @Override
            public void persist(EntityManager em) {
                validate(person);
                em.persist(person);
                LOG.info("persisted person id: " + person.getId());
            }
        });
        
        // wieder Adressenobjekt setzen, damit die View keine Probleme bekommt
        if (adresseEmpty) {
        	person.setAdresse(new Adresse());
        }
        
        return person;
    }
    
    /**
     * Speichert die Änderungen der übergebenen Person in der DB.
     * 
     * @param person zu speichernde Person
     * @return siehe Beschreibung
     */
    public Person save(final Person person) {
        LOG.info("saving person id: " + person.getId());
        
        // leere Adresse als null speichern
        boolean adresseEmpty = person.getAdresse().istEmpty();
        if (adresseEmpty) {
        	person.setAdresse(null);
        }
        
        Person mergedPerson = this.executor.merge(new JpaJob<Person>() {
            @Override
            public Person merge(EntityManager em) {
                validate(person);
                return em.merge(person);
            }
        });
        
        // wieder Adressenobjekt setzen, damit die View keine Probleme bekommt
        if (adresseEmpty) {
        	mergedPerson.setAdresse(new Adresse());
        }
        
        return mergedPerson;
    }
    
    /**
     * Löscht die übergebene Person.
     * 
     * @param personDetached zu löschende Person
     */
    public void remove(final Person personDetached) {
        LOG.info("removing person id: " + personDetached.getId());
        
        this.executor.remove(new JpaJob<Person>() {
            @Override
            public void remove(EntityManager em) {
                Person person = em.find(Person.class, personDetached.getId());
                em.remove(person);
            }
        });
    }
    
    /**
     * Synchronisiert die Liste mit den übergebenen Personen mit den bereits gespeicherten 
     * Personen. Der Key ist die Joomla-Userid.
     * 
     * @param personen Liste mit Personen aus einer Import-Quelle
     */
    public void synchronize(final List<Person> personen) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Alle Personen so kennzeichnen, dass kein Update durch einen Sync stattgefunden hat
            // So kann erkannt werden, welche Personen durch den Sync nicht aktualisiert wurden
            Query query = em.createQuery("UPDATE Person SET updatedInLastSync = false");
            query.executeUpdate();
            
            Map<Long, Person> userIdToPerson = createSyncMap();
            for (Person person : personen) {
                Person persistedPerson = userIdToPerson.get(person.getUserId());
                if (persistedPerson == null) {
                    LOG.info("import new person, userId: " + person.getUserId());
                    person.setUpdatedInLastSync(true);
                    em.persist(person);
                }
                else {
                    LOG.info("update person, userId: " + person.getUserId());
                    persistedPerson.setVorname(person.getVorname());
                    persistedPerson.setNachname(person.getNachname());
                    persistedPerson.setEmail(person.getEmail());
                    persistedPerson.setWohnungNr(person.getWohnungNr());
                    persistedPerson.setUpdatedInLastSync(true);
                    em.merge(persistedPerson);
                }
            }
            em.getTransaction().commit();
            
            TypedQuery<Long> queryAnzahl = em.createQuery(
                    "SELECT count(p) FROM Person p WHERE p.updatedInLastSync = false", Long.class);
            long anzahl = queryAnzahl.getSingleResult();
            if (anzahl > 0) {
                this.mailProvider.sendPersonenSyncMail(anzahl);
            }
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
    
    private void validate(Person person) {
        super.validate(person);
        
        if (person.getAdresse() != null) {
            super.validate(person.getAdresse());
        }
    }
	
    /**
     * Alle Personen aus der DB holen.
     */
    @SuppressWarnings("unchecked")
    private Map<Long, Person> createSyncMap() {
        Query q = getEntityManager().createQuery("SELECT p FROM Person p");
        List<Person> persistedPersonen = (List<Person>) q.getResultList();
        Map<Long, Person> userIdToPerson = new HashMap<Long, Person>();
        for (Person person : persistedPersonen) {
            if (person.getUserId() != null) {
                userIdToPerson.put(person.getUserId(), person);
            }
        }
        return userIdToPerson;
    }
}
