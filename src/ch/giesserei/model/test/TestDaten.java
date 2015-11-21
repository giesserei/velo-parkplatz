package ch.giesserei.model.test;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.imp.ImportMietobjekt;
import ch.giesserei.imp.ImportReservation;
import ch.giesserei.imp.SyncPerson;
import ch.giesserei.injection.Injection;
import ch.giesserei.model.Adresse;
import ch.giesserei.model.JuristischePerson;
import ch.giesserei.model.Mietobjekt;
import ch.giesserei.model.MietobjektTyp;
import ch.giesserei.model.Person;
import ch.giesserei.model.Vertrag;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Hier werden die Testdaten definiert.
 * 
 * @author Steffen Förster
 */
public class TestDaten {

	private static final Logger LOG = LoggerFactory.getLogger(TestDaten.class);
	
	@Inject
	private Provider<EntityManager> entityManagerProvider;
	
	@Inject 
	private ImportMietobjekt importMietobjekt;
	
	@Inject 
    private ImportReservation importReservation;
	
	@Inject 
    private SyncPerson syncPerson;
	
	public TestDaten() {
	    Injection.injectMembers(this);
	}
	
	public void initTestDaten() {
		LOG.info("init test data ...");
		
        EntityManager em = this.entityManagerProvider.get();
        em.getTransaction().begin();
        
        createEntities(em);
        new TestDatenStellplatz().createEntities(em);
        
        em.getTransaction().commit();
        
        this.importMietobjekt.importMietobjekte();
        this.syncPerson.syncPersonen();
        this.importReservation.importReservationen();
        
        LOG.info("init test data finished");
    }
    
    private void createEntities(EntityManager em) {
        // laufender Vertrag mit einem Mieter und zwei anderen Bewohnern
        Mietobjekt objekt1 = createObjekt(em, 2701, MietobjektTyp.WOHNUNG);
        Person mieter1 = createPerson(em, "Franz", "Muster", null, "4481", "franz.muster@mail.ch");   
        Person bewohner1 = createPerson(em, "Hans", "Muster", null, "4223", "hans.muster@mail.ch");    
        Person bewohner2 = createPerson(em, "Willi", "Muster", null, "4354", "willi.muster@mail.ch");
        Vertrag vertrag1 = createVertrag(em, new GregorianCalendar(2013, 1, 1).getTime(), 
                new GregorianCalendar(9999, 0, 1).getTime());
        
        vertrag1.setMietobjekt(objekt1);
        vertrag1.getMieterPersonen().add(mieter1);
        vertrag1.getMitbewohner().add(bewohner1);
        vertrag1.getMitbewohner().add(bewohner2);
        em.persist(vertrag1);
        
        // abgelaufener Vertrag 
        Mietobjekt objekt2 = createObjekt(em, 2702, MietobjektTyp.WOHNUNG);
        
        Vertrag vertrag2 = createVertrag(em, new GregorianCalendar(2013, 1, 1).getTime(), 
                new GregorianCalendar(2013, 6, 31).getTime());
        Person mieter2 = createPerson(em, "Gustav", "Muster", null, "4252", "gustav.muster@mail.ch"); 
        vertrag2.setMietobjekt(objekt2);
        vertrag2.getMieterPersonen().add(mieter1);
        vertrag2.getMieterPersonen().add(mieter2);
        vertrag2.getMitbewohner().add(bewohner1);
        em.persist(vertrag2);
        
        // Vertrag mit Verein
        Mietobjekt objekt3 = createObjekt(em, 2703, MietobjektTyp.WOHNUNG);
        JuristischePerson mieter3 = createJuristischePerson(em, "Genossenschaft Giesserei");
        Vertrag vertrag3 = createVertrag(em, new GregorianCalendar(2013, 1, 1).getTime(), 
                new GregorianCalendar(9999, 0, 1).getTime());
        vertrag3.setMietobjekt(objekt3);
        vertrag3.setMieterJuristischePerson(mieter3);
        em.persist(vertrag3);
    }
    
    private Mietobjekt createObjekt(EntityManager em, int nummer, MietobjektTyp typ) {
        Mietobjekt objekt = new Mietobjekt();
        objekt.setNummer(nummer);
        objekt.setTyp(typ);
        em.persist(objekt);
        return objekt;
    }
    
    private Person createPerson(EntityManager em, String vorname, String nachname, String ort, String wohnungNr, String email) {
        Person person = new Person();
        person.setVorname(vorname);
        person.setNachname(nachname);
        person.setWohnungNr(wohnungNr);
        person.setEmail(email);
        if (ort != null) {
        	Adresse adresse = new Adresse();
        	adresse.setOrt(ort);
        	person.setAdresse(adresse);
        }
        em.persist(person);
        return person;
    }
    
    private JuristischePerson createJuristischePerson(EntityManager em, String name) {
        JuristischePerson person = new JuristischePerson();
        person.setName(name);
        em.persist(person);
        return person;
    }
    
    private Vertrag createVertrag(EntityManager em, Date beginn, Date ende) {
        Vertrag vertrag = new Vertrag();
        vertrag.setBeginnDatum(beginn);
        vertrag.setEndDatum(ende);
        em.persist(vertrag);
        return vertrag;
    }
	
}
