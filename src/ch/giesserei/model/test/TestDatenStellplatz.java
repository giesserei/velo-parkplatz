package ch.giesserei.model.test;

import static ch.giesserei.model.StellplatzTyp.PEDALPARC_HOCH;
import static ch.giesserei.model.StellplatzTyp.PEDALPARC_TIEF;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.model.Person;
import ch.giesserei.model.ReservationStatus;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;

/**
 * Definiert die Test-Daten für die Stellplatzverwaltung.
 * 
 * @author Steffen Förster
 */
public class TestDatenStellplatz {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestDatenStellplatz.class);

	public void createEntities(EntityManager em) {
	    createSektor1(em);
	    createSektor2(em);
	    createSektor3(em);
	    createSektor4(em);
	    createSektor5(em);
	    createSektor6(em);
		//createReservationen(em);
	}
	
	private void createSektor1(EntityManager em) {
		createStellplatz(em, PEDALPARC_HOCH, PEDALPARC_TIEF, 41, 43, 1);
		createStellplatz(em, PEDALPARC_TIEF, PEDALPARC_HOCH, 44, 50, 1);
		createStellplatz(em, PEDALPARC_HOCH, PEDALPARC_TIEF, 51, 69, 1);
	}
	
	private void createSektor2(EntityManager em) {
	    createStellplatz(em, PEDALPARC_TIEF, PEDALPARC_HOCH, 70, 76, 2);
	    createStellplatz(em, PEDALPARC_HOCH, PEDALPARC_TIEF, 77, 81, 2);
	    createStellplatz(em, PEDALPARC_TIEF, PEDALPARC_HOCH, 82, 106, 2);
	    createStellplatz(em, PEDALPARC_HOCH, PEDALPARC_TIEF, 107, 127, 2);
    }
	
	private void createSektor3(EntityManager em) {
	    for (int i = 1; i <= 15; i++) {
            createStellplatz(em, i, 3, StellplatzTyp.SPEZIAL);
        }
	}
	
	private void createSektor4(EntityManager em) {
        for (int i = 16; i <= 26; i++) {
            createStellplatz(em, i, 4, StellplatzTyp.SPEZIAL);
        }
        createStellplatz(em, PEDALPARC_HOCH, PEDALPARC_TIEF, 128, 183, 4);
    }
	
	private void createSektor5(EntityManager em) {
        for (int i = 27; i <= 32; i++) {
            createStellplatz(em, i, 5, StellplatzTyp.SPEZIAL);
        }
        createStellplatz(em, PEDALPARC_TIEF, PEDALPARC_HOCH, 184, 244, 5);
    }
	
	private void createSektor6(EntityManager em) {
        for (int i = 33; i <= 39; i++) {
            createStellplatz(em, i, 6, StellplatzTyp.SPEZIAL);
        }
        createStellplatz(em, PEDALPARC_HOCH, PEDALPARC_TIEF, 245, 298, 6);
    }
	
	private void createStellplatz(EntityManager em, StellplatzTyp typGerade, StellplatzTyp typUngerade, 
            int nummerVon, int nummerBis, int sektor) {
        
        for (int i = nummerVon; i <= nummerBis; i++) {
            if (i % 2 == 0) {
                createStellplatz(em, i, sektor, typGerade);
            }
            else {
                createStellplatz(em, i, sektor, typUngerade);
            }
        }
    }
	
	private Stellplatz createStellplatz(EntityManager em, int nummer, int sektor, StellplatzTyp typ) {
		Stellplatz stellplatz = new Stellplatz();
		stellplatz.setNummer(nummer);
		stellplatz.setSektor(sektor);
		stellplatz.setTyp(typ);
        em.persist(stellplatz);
        return stellplatz;
    }
	
	// ---------------------------------------------------------
    // Reservationen
    // ---------------------------------------------------------
	
	private void createReservationen(EntityManager em) {	
		Stellplatz stellplatz = getStellplatzByNummer(em, 1, StellplatzTyp.SPEZIAL, false);
		Person person = getPersonByName(em, "Gustav", "Muster");
		
		createReservation(em, person, stellplatz, 7, ReservationStatus.RESERVIERT,
				new GregorianCalendar(2013, 1, 1).getTime(), 
				new GregorianCalendar(2014, 1, 1).getTime());
		
		createReservation(em, person, stellplatz, 7, ReservationStatus.ROLLOVER,
				new GregorianCalendar(2014, 1, 1).getTime(), 
				new GregorianCalendar(2015, 1, 1).getTime());
		
		createReservation(em, person, stellplatz, 7, ReservationStatus.STORNIERT,
				new GregorianCalendar(2012, 1, 1).getTime(), 
				new GregorianCalendar(2013, 1, 1).getTime());
		
		createReservation(em, person, stellplatz, 7, ReservationStatus.STORNIERT,
				new GregorianCalendar(2013, 0, 1).getTime(), 
				new GregorianCalendar(2014, 0, 1).getTime());
		
		createAnonymReservation(em, stellplatz, 7, ReservationStatus.RESERVIERT,
                new GregorianCalendar(2015, 1, 1).getTime(), 
                new GregorianCalendar(2016, 1, 1).getTime(),
                "Steffen Förster", 2412, "steffen@4foerster.ch");
		
		stellplatz = getStellplatzByNummer(em, 2, StellplatzTyp.SPEZIAL, false);
		
		createReservation(em, person, stellplatz, 7, ReservationStatus.RESERVIERT,
                new GregorianCalendar(2013, 2, 1).getTime(), 
                new GregorianCalendar(2014, 2, 1).getTime());

	}
	
	private ReservationStellplatz createReservation(EntityManager em, Person person, Stellplatz stellplatz, 
			double kostenProMonat, ReservationStatus status, Date beginnDatum, Date endDatumExklusiv) {
		
		ReservationStellplatz res = new ReservationStellplatz();
		res.setStellplatz(stellplatz);
		res.setMieterPerson(person);
		
		res.setReservationDatum(new Date());
		res.setBeginnDatum(beginnDatum);
		res.setEndDatumExklusiv(endDatumExklusiv);
		res.setKostenProMonat(kostenProMonat);
		res.setReservationStatus(status);
		
		LOG.info("before persist: " + res.toString());
		
		em.persist(res);
		return res;
	}
	
	private ReservationStellplatz createAnonymReservation(EntityManager em, Stellplatz stellplatz, 
            double kostenProMonat, ReservationStatus status, Date beginnDatum, Date endDatumExklusiv, 
            String name, int wohnung, String email) {
        
        ReservationStellplatz res = new ReservationStellplatz();
        res.setStellplatz(stellplatz);
        
        res.setReservationDatum(new Date());
        res.setBeginnDatum(beginnDatum);
        res.setEndDatumExklusiv(endDatumExklusiv);
        res.setKostenProMonat(kostenProMonat);
        res.setReservationStatus(status);
        
        res.setAnonym(true);
        res.setName(name);
        res.setWohnungNr(wohnung);
        res.setEmail(email);
        
        LOG.info("before persist: " + res.toString());
        
        em.persist(res);
        return res;
    }
	
	private Stellplatz getStellplatzByNummer(EntityManager em, int nummer, StellplatzTyp typ, boolean bothPedalParc) {
		Query q = em.createQuery("SELECT s FROM Stellplatz s WHERE s.nummer = ?1 and (s.typ = ?2 or s.typ = ?3)");
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
	
	private Person getPersonByName(EntityManager em, String vorname, String nachname) {
		Query q = em.createQuery("SELECT p FROM Person p WHERE UPPER(p.vorname) = UPPER(:vorname) AND UPPER(p.nachname) = UPPER(:nachname)");
		q.setParameter("vorname", vorname);
		q.setParameter("nachname", nachname);
		return (Person) q.getSingleResult();
	}
	
}
