package ch.giesserei.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.core.Const;
import ch.giesserei.core.ModelConstraintException;
import ch.giesserei.model.JpaJob;
import ch.giesserei.model.JpaTransactionExecutor;
import ch.giesserei.model.Mietobjekt;
import ch.giesserei.model.Person;
import ch.giesserei.model.ReservationStatus;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.resource.ValMsg;
import ch.giesserei.service.model.StellplatzStatus;
import ch.giesserei.util.Utility;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

/**
 * Service-Klasse für das Modell {@link ReservationStellplatz}.
 * 
 * @author Steffen Förster
 */
@Singleton
public class ReservationService extends BaseService {

	private static final Logger LOG = LoggerFactory.getLogger(ReservationService.class);
	
	private final JpaTransactionExecutor<ReservationStellplatz> executor;
	
	@Inject
	public ReservationService(MessageInterpolator interpolator, Provider<EntityManager> entityManager) {
		super(interpolator, entityManager);
		this.executor = new JpaTransactionExecutor<ReservationStellplatz>(entityManager);
	}
	
	public double getKostenProMonat(StellplatzTyp typ) {
	    if (typ == StellplatzTyp.PEDALPARC_HOCH || typ == StellplatzTyp.PEDALPARC_TIEF) {
	        return Const.KOSTEN_STELLPLATZ_PEDALPARC;
	    }
	    else {
	        return Const.KOSTEN_STELLPLATZ_SPEZIAL;
	    }
	}
	
	/**
	 * Liefert die Kosten für eine Reservierungsperiode -> zur Zeit 1 Jahr.
	 * 
	 * @param typ Stellplatztyp
	 * @return siehe Beschreibung
	 */
	public double getKostenProPeriode(StellplatzTyp typ) {
	    return getKostenProMonat(typ) * Const.MONATE_PRO_PERIODE;
    }
	
	/**
     * Liefert alle aktive Reservationen, deren Mietende vor übergebenen Datum liegt.
     * Von jedem Stellplatz wird nur die Reservation mit dem ältestem Mietende berücksichtigt.
     * 
     * @param datum Datumsgrenze
     * @return siehe Beschreibung
     */
    @SuppressWarnings({ "unchecked" })
    public List<ReservationStellplatz> getReservationenAblaufend(Date datum) {
        ReservationStatus[] rolloverStatus = ReservationStatus.getRolloverStatus();
        ReservationStatus[] aktivStatus = ReservationStatus.getAktivStatus();
        
        int startIndexStatus = 2;
        // Joins zusätzlich angegeben, da der SQL-Compiler sonst mit diesem Statement nicht klar kommt
        Query q = getEntityManager().createQuery(
              " SELECT r "
            + " FROM ReservationStellplatz r JOIN Stellplatz s ON r.stellplatz.id = s.id"  
            + " WHERE r.endDatumExklusiv = ("
            + "   SELECT MAX(r1.endDatumExklusiv)"
            + "   FROM ReservationStellplatz r1 JOIN Stellplatz s1 ON r1.stellplatz.id = s1.id"
            + "   WHERE r1.endDatumExklusiv < ?1 AND r1.stellplatz = r.stellplatz"
            + "     AND r1.reservationStatus IN ("
            +           getParameterListString(rolloverStatus.length, startIndexStatus)
            + "     )"
            + " )"
            + " AND r.reservationStatus IN ("
            +       getParameterListString(rolloverStatus.length, startIndexStatus + rolloverStatus.length)
            + " )"
            + " AND NOT EXISTS ("
            + "   SELECT r3 "
            + "   FROM ReservationStellplatz r3 JOIN Stellplatz s3 ON r3.stellplatz.id = s3.id"
            + "   WHERE r3.endDatumExklusiv >= ?1 AND r3.stellplatz = r.stellplatz"
            + "     AND r3.reservationStatus IN ("
            +           getParameterListString(aktivStatus.length, startIndexStatus + 2 * rolloverStatus.length)
            + "     )"
            + " )");     
        
        q.setParameter(1, datum);
        
        for (int i = startIndexStatus; i <= (rolloverStatus.length + (startIndexStatus - 1)); i++) {
            q.setParameter(i, rolloverStatus[i - startIndexStatus]);
        }
        
        startIndexStatus = startIndexStatus + rolloverStatus.length;
        for (int i = startIndexStatus; i <= (rolloverStatus.length + (startIndexStatus - 1)); i++) {
            q.setParameter(i, rolloverStatus[i - startIndexStatus]);
        }
        
        startIndexStatus = startIndexStatus + rolloverStatus.length;
        for (int i = startIndexStatus; i <= (aktivStatus.length + (startIndexStatus - 1)); i++) {
            q.setParameter(i, aktivStatus[i - startIndexStatus]);
        }
        
        return q.getResultList();
    }
    
    /**
     * Liefert die Reservationen, deren Zahlung überfällig ist.
     * Reservationsdatum war bereits vor mindestens {@link Const#TAGE_ZAHLUNGSFRIST} Tagen und 
     * der Beginn der Miete liegt in der Vergangenheit.
     * 
     * @return siehe Beschreibung
     */
    @SuppressWarnings("unchecked")
    public Container getReservationenOverdue() {
        BeanItemContainer<ReservationStellplatz> container = 
                new BeanItemContainer<ReservationStellplatz>(ReservationStellplatz.class);
        
        ReservationStatus[] status = ReservationStatus.getAktivStatus();
        int startIndex = 2;
        Date zahlungsfrist = Utility.addDay(new Date(), -1 * Const.TAGE_ZAHLUNGSFRIST);
        zahlungsfrist = Utility.stripTime(zahlungsfrist);
        
        String queryStr = "SELECT r FROM ReservationStellplatz r WHERE"
                + " r.bezahlt = false AND r.reservationDatum < ?1 AND r.beginnDatum < CURRENT_DATE"
                + " AND r.reservationStatus IN ("
                + getParameterListString(status.length, startIndex)
                + ")";
        
        Query q = getEntityManager().createQuery(queryStr);
        
        q.setParameter(1, zahlungsfrist);
        
        for (int i = startIndex; i <= (status.length + 1); i++) {
            q.setParameter(i, status[i - startIndex]);
        }
        
        List<ReservationStellplatz> reservationList = q.getResultList();
        for (ReservationStellplatz reservation : reservationList) {
            if (reservation.getMieterPerson() == null) {
                reservation.setMieterPerson(Person.ANONYM);
            }
            container.addBean(reservation);
        }
        
        return container;
    }
	
	/**
	 * Liefert den Status zu allen Stellplätzen des übergebenen Sektors.
	 * 
	 * @param sektor Sektornummer
	 * @return siehe Beschreibung
	 */
    @SuppressWarnings("rawtypes")
    public List<StellplatzStatus> getStellplatzStatus(int sektor) {
	    ReservationStatus[] aktivStatus = ReservationStatus.getAktivStatus();
	    
	    int startIndexStatus = 1;
	    Query q = getEntityManager().createNativeQuery(
	        " SELECT s1.nummer, s1.typ, s1.datum, ("
	      + "   SELECT p.wohnung_nr FROM reservation_stellplatz r JOIN person p ON p.id = r.mieter_person"
	      + "   WHERE r.stellplatz_id = s1.id AND r.end_datum_exklusiv = datum"
	      + "     AND r.reservation_status IN ("
	      +         getParameterListStringNative(aktivStatus.length, startIndexStatus)
	      + "     )"
	      + " )"
	      + " FROM ("    
	      + "   SELECT s.nummer, s.typ, s.id, ("
	      + "     SELECT MAX(r.end_datum_exklusiv) end_datum_exklusiv"
	      + "     FROM reservation_stellplatz r"
	      + "     WHERE r.end_datum_exklusiv > current_date"
	      + "       AND r.stellplatz_id = s.id"
	      + "       AND r.reservation_status IN ("
	      +           getParameterListStringNative(aktivStatus.length, startIndexStatus + aktivStatus.length)
	      + "       )"
	      + "   ) datum"
	      + "   FROM stellplatz s"
	      + "   WHERE sektor = ?"
	      + " ) s1"
	      + " ORDER BY s1.nummer"
	    );
	    
	    for (int i = startIndexStatus; i <= (aktivStatus.length + (startIndexStatus - 1)); i++) {
            q.setParameter(i, aktivStatus[i - startIndexStatus].ordinal());
        }
	    
	    startIndexStatus = startIndexStatus + aktivStatus.length;
        for (int i = startIndexStatus; i <= (aktivStatus.length + (startIndexStatus - 1)); i++) {
            q.setParameter(i, aktivStatus[i - startIndexStatus].ordinal());
        }
	            
        q.setParameter(1 + (2 * aktivStatus.length), sektor);
        List nativeResult =  q.getResultList();
        List<StellplatzStatus> statusList = new ArrayList<StellplatzStatus>();
        for (Object v : nativeResult) {
            Object[] values = (Object[]) v;
            
            Date reserviertBis = (Date) values[2];
            reserviertBis = reserviertBis == null ? null : Utility.addDay(reserviertBis, -1);
            
            StellplatzStatus status = new StellplatzStatus(
                    (Integer) values[0], 
                    StellplatzTyp.valueOf((String) values[1]),
                    reserviertBis,
                    (String) values[3]
            );
            statusList.add(status);
        }
        return statusList;
	}
    
    /**
     * Liefert alle aktiven Reservationen, welchen noch keine Person zugeordnet ist.
     */
    @SuppressWarnings("unchecked")
    public Container getReservationenAnonym() {
        BeanItemContainer<ReservationStellplatz> container = 
                new BeanItemContainer<ReservationStellplatz>(ReservationStellplatz.class);
        
        ReservationStatus[] status = ReservationStatus.getAktivStatus();
        
        int startIndex = 1;
        String queryStr = "SELECT r FROM ReservationStellplatz r WHERE"
                + " r.mieterPerson IS NULL AND r.reservationStatus IN ("
                + getParameterListString(status.length, startIndex)
                + " )";
        Query q = getEntityManager().createQuery(queryStr);
        
        for (int i = startIndex; i <= (status.length + (startIndex - 1)); i++) {
            q.setParameter(i, status[i - startIndex]);
        }
        
        List<ReservationStellplatz> reservationList = q.getResultList();
        for (ReservationStellplatz reservation : reservationList) {
            container.addBean(reservation);
        }
        
        return container;
    }
	
	/**
	 * Liefert alle aktiven Reservationen zu einem Stellplatz in einem Container.
	 * 
	 * @param stellplatz Stellplatz
	 * @return siehe Beschreibung
	 */
    public Container getReservationenAktiv(Stellplatz stellplatz) {
		return getReservationen(stellplatz, ReservationStatus.getAktivStatus());
	}
    
    /**
     * Liefert alle aktiven Reservationen zu einer Person in einem Container.
     * 
     * @param person Person
     * @return siehe Beschreibung
     */
    public Container getReservationenAktiv(Person person) {
        return getReservationen(person, ReservationStatus.getAktivStatus());
    }
    
    /**
     * Liefert alle aktiven Reservationen, die den Personen zugeordnet sind, die das übergebene 
     * Mietobjekt gemietet haben.
     * 
     * @param objekt Mietobjekt
     * @return siehe Beschreibung
     */
    public Container getReservationenAktiv(Mietobjekt objekt) {
        return getReservationen(objekt, ReservationStatus.getAktivStatus());
    }
	
	/**
	 * Liefert alle inaktiven Reservationen zu einem Stellplatz in einem Container.
	 * 
	 * @param stellplatz Stellplatz
	 * @return siehe Beschreibung
	 */
    public Container getReservationenInaktiv(Stellplatz stellplatz) {
		return getReservationen(stellplatz, ReservationStatus.getInaktivStatus());
	}
    
    /**
     * Liefert alle inaktiven Reservationen zu einer Person in einem Container.
     * 
     * @param person Person
     * @return siehe Beschreibung
     */
    public Container getReservationenInaktiv(Person person) {
        return getReservationen(person, ReservationStatus.getInaktivStatus());
    }
    
    /**
     * Liefert alle inaktiven Reservationen, die den Personen zugeordnet sind, die das übergebene 
     * Mietobjekt gemietet haben.
     * 
     * @param objekt Mietobjekt
     * @return siehe Beschreibung
     */
    public Container getReservationenInaktiv(Mietobjekt objekt) {
        return getReservationen(objekt, ReservationStatus.getInaktivStatus());
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
	 * Prüft, ob die übergebene Reservation durchgeführt werden könnte, d.h. ob der gewünschte Zeitraum 
	 * noch frei ist.
	 * <p/>
	 * Bei einer anonymen Reservation wird zusätzlich geprüft, ob am Buchungstag oder in der Zukunft eine 
	 * aktive Reservation vorliegt.
	 * 
	 * @param reservation zu prüfende Reservation
	 * @return true, wenn der Zeitraum noch frei ist
	 */
	public boolean isStellplatzFrei(ReservationStellplatz reservation) {
	    if (ReservationStatus.isInaktivStatus(reservation.getReservationStatus())) {
	        // inaktive Reservationen können ohne Prüfung angelegt oder gespeichert werden
	        return true;
	    }
	    
	    if (!isAnonymeReservationZulaessig(reservation)) {
	        LOG.info("anonyme Reservation nicht zulässig -> aktive Reservation(en) vorhanden, "
	                + "stellplatz:" + reservation.getStellplatz().getNummer());
	        return false;
	    }
	    
	    trimDate(reservation);
	    ReservationStatus[] aktivStatus = ReservationStatus.getAktivStatus();
	    int startIndexStatus = 5;
	    String queryStr = "SELECT r FROM ReservationStellplatz r WHERE"
                + " r.stellplatz.id = ?1 AND r.endDatumExklusiv > ?2 AND r.beginnDatum < ?3"
	            + " AND r.id != ?4"
                + " AND r.reservationStatus IN ("
                + getParameterListString(aktivStatus.length, startIndexStatus)
                + ")";
        
        Query q = getEntityManager().createQuery(queryStr);
        
        q.setParameter(1, reservation.getStellplatz().getId());
        q.setParameter(2, reservation.getBeginnDatum());
        q.setParameter(3, reservation.getEndDatumExklusiv());
        // ID ist vor persist NULL
        q.setParameter(4, reservation.getId() == null ? 0 : reservation.getId());
        
        for (int i = startIndexStatus; i <= (aktivStatus.length + (startIndexStatus - 1)); i++) {
            q.setParameter(i, aktivStatus[i - startIndexStatus]);
        }
        
        return q.getResultList().size() == 0;
	}
	
	/**
	 * Bei einer anonymen Reservation wird zusätzlich geprüft, ob am Buchungstag oder in der Zukunft eine 
     * aktive Reservation vorliegt.
	 * 
	 * @param reservation zu prüfende Reservation
	 * @return
	 */
	public boolean isAnonymeReservationZulaessig(ReservationStellplatz reservation) {
	    if (reservation.isAnonym()) {
            trimDate(reservation);
            ReservationStatus[] aktivStatus = ReservationStatus.getAktivStatus();
            int startIndexStatus = 2;
            String queryStr = "SELECT r FROM ReservationStellplatz r WHERE"
                    + " r.stellplatz.id = ?1 AND r.endDatumExklusiv > CURRENT_DATE"
                    + " AND r.reservationStatus IN ("
                    + getParameterListString(aktivStatus.length, startIndexStatus)
                    + ")";
            
            Query q = getEntityManager().createQuery(queryStr);
            
            q.setParameter(1, reservation.getStellplatz().getId());
            
            for (int i = startIndexStatus; i <= (aktivStatus.length + (startIndexStatus - 1)); i++) {
                q.setParameter(i, aktivStatus[i - startIndexStatus]);
            }
            
            return q.getResultList().size() == 0;
	    }
	    else {
	        return true;
	    }
    }
    
	/**
	 * Speichert eine neue Reservation. Die Methode ist synchronized, damit vom Service sichergestellt werden
	 * kann, dass es für einen Stellplatz keine Überschneidung von aktiven Reservationen gibt.
	 * 
	 * @param reservation Neu zu erstellende Reservation
	 * 
	 * @return siehe Beschreibung
	 */
    public synchronized ReservationStellplatz persist(final ReservationStellplatz reservation) {
        LOG.info("persisting reservation");
        
        trimDate(reservation);
        if (!isStellplatzFrei(reservation)) {
            throw new ModelConstraintException(ValMsg.getString("reservation.zeitraum"));
        }
        
        this.executor.persist(new JpaJob<ReservationStellplatz>() {
            @Override
            public void persist(EntityManager em) {
                validate(reservation);
                em.persist(reservation);
                LOG.info("persisted reservation id: " + reservation.getId());
            }
        });
        
        return reservation;
    }
    
    /**
     * Speichert eine neue Reservation. Die Methode ist synchronized, damit vom Service sichergestellt werden
     * kann, dass es für einen Stellplatz keine Überschneidung von aktiven Reservationen gibt.
     * 
     * @param reservation Neu zu speichernde Reservation
     * 
     * @return siehe Beschreibung
     */
    public synchronized ReservationStellplatz save(final ReservationStellplatz reservation) {
        LOG.info("saving reservation id: " + reservation.getId());
        
        trimDate(reservation);
        if (!isStellplatzFrei(reservation)) {
            throw new ModelConstraintException(ValMsg.getString("reservation.zeitraum"));
        }
        
        ReservationStellplatz mergedReservation = this.executor.merge(new JpaJob<ReservationStellplatz>() {
            @Override
            public ReservationStellplatz merge(EntityManager em) {
                validate(reservation);
                return em.merge(reservation);
            }
        });
        
        return mergedReservation;
    }
    
    public void remove(final ReservationStellplatz reservationDetached) {
        LOG.info("removing reservation id: " + reservationDetached.getId());
        
        this.executor.remove(new JpaJob<ReservationStellplatz>() {
            @Override
            public void remove(EntityManager em) {
                ReservationStellplatz reservation = em.find(ReservationStellplatz.class, reservationDetached.getId());
                em.remove(reservation);
            }
        });
    }
    
    /**
     * Importiert die bestehenden Reservationen aus einer Datenquelle.
     * 
     * @param reservationen zu importierende Reservationen
     */
    public void persist(List<ReservationStellplatz> reservationen) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Map<Integer, Stellplatz> nummerToStellplatz = createStellplatzMap();
            
            for (ReservationStellplatz reservation : reservationen) {
                Stellplatz stellplatzPersisted = nummerToStellplatz.get(
                        reservation.getStellplatz().getNummer());
                
                // konkreten Stellplatz und Preis setzen 
                reservation.setStellplatz(stellplatzPersisted);
                reservation.setKostenProMonat(this.getKostenProMonat(stellplatzPersisted.getTyp()));
                
                em.persist(reservation);
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
    
    private void validate(ReservationStellplatz reservation) {
        super.validate(reservation);
        
        // Zeitraum validieren
        Calendar calVon = new GregorianCalendar();
        calVon.setTime(Utility.stripTime(reservation.getBeginnDatum()));
        
        Calendar calBis = new GregorianCalendar();
        calBis.setTime(Utility.stripTime(reservation.getEndDatumExklusiv()));
        
        if (!calBis.after(calVon)) {
            throw new ModelConstraintException(ValMsg.getString("val.zeitraum"));
        }
    }
    
    @SuppressWarnings("unchecked")
    private Map<Integer, Stellplatz> createStellplatzMap() {
        Query q = getEntityManager().createQuery("SELECT s FROM Stellplatz s");
        List<Stellplatz> stellplatzList = (List<Stellplatz>) q.getResultList();
        Map<Integer, Stellplatz> nummerToStellplatz = new HashMap<Integer, Stellplatz>();
        for (Stellplatz stellplatz : stellplatzList) {
            nummerToStellplatz.put(stellplatz.getNummer(), stellplatz);
        }
        return nummerToStellplatz;
    }
    
    /**
     * Liefert alle Reservationen für den übergebenen Stellplatz, welche einen der übergebenen Status haben.
     */
    private Container getReservationen(Stellplatz stellplatz, ReservationStatus[] status) {
        int startIndex = 2;
        String queryStr = "SELECT r FROM ReservationStellplatz r WHERE"
                + " r.stellplatz.id = ?1 AND r.reservationStatus IN ("
                + getParameterListString(status.length, startIndex)
                + ")";
        Query q = getEntityManager().createQuery(queryStr);
        q.setParameter(1, stellplatz.getId());
        return getReservationen(q, status, startIndex);
    }
    
    /**
     * Liefert alle Reservationen der Personen, die das übergebene Mietobjekt gemietet haben und welche einen der 
     * übergebenen Status haben.
     */
    private Container getReservationen(Mietobjekt mietObjekt, ReservationStatus[] status) {
        int startIndex = 2;
        String queryStr = "SELECT r FROM ReservationStellplatz r WHERE"
                + " r.mieterPerson.wohnungNr LIKE ?1 AND r.reservationStatus IN ("
                + getParameterListString(status.length, startIndex)
                + ")";
        Query q = getEntityManager().createQuery(queryStr);
        q.setParameter(1, "%" + mietObjekt.getNummer() + "%");
        return getReservationen(q, status, startIndex);
    }
    
    /**
     * Liefert alle Reservationen für die übergebene Person, welche einen der übergebenen Status haben.
     */
    private Container getReservationen(Person person, ReservationStatus[] status) {
        int startIndex = 2;
        String queryStr = "SELECT r FROM ReservationStellplatz r WHERE"
                + " r.mieterPerson = ?1 AND r.reservationStatus IN ("
                + getParameterListString(status.length, startIndex)
                + ")";
        Query q = getEntityManager().createQuery(queryStr);
        q.setParameter(1, person);
        return getReservationen(q, status, startIndex);
    }
    
	@SuppressWarnings("unchecked")
    private Container getReservationen(Query q, ReservationStatus[] status, int startIndex) {
		BeanItemContainer<ReservationStellplatz> container = 
				new BeanItemContainer<ReservationStellplatz>(ReservationStellplatz.class);
		
		for (int i = startIndex; i <= (status.length + 1); i++) {
			q.setParameter(i, status[i - startIndex]);
		}
		
        List<ReservationStellplatz> reservationList = q.getResultList();
        for (ReservationStellplatz reservation : reservationList) {
            if (reservation.getMieterPerson() == null) {
                reservation.setMieterPerson(Person.ANONYM);
            }
        	container.addBean(reservation);
        }
        
		return container;
	}
	
	private void trimDate(ReservationStellplatz reservation) {
	    reservation.setBeginnDatum(trimDate(reservation.getBeginnDatum()));
        reservation.setEndDatumExklusiv(trimDate(reservation.getEndDatumExklusiv()));
	}

}
