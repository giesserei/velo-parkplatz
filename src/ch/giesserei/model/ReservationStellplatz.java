package ch.giesserei.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ch.giesserei.util.Utility;

/**
 * Modell für eine Stellplatz-Reservation.
 * 
 * @author Steffen Förster
 */
@Entity
@Table(name="RESERVATION_STELLPLATZ")
public class ReservationStellplatz {

	public static final String NESTED_PROPERTY_PERSON_NAME = "mieterPerson.nameVollstaendig";
	
	public static final String NESTED_PROPERTY_PERSON_NAME_WOHNUNG = "mieterPerson.nameUndWohnung";
	
	public static final String PROPERTY_RESERVATION_DATUM = "reservationDatum";
	
	public static final String PROPERTY_BEGINN_DATUM = "beginnDatum";
	
	public static final String PROPERTY_END_DATUM_EXKL = "endDatumExklusiv";
	
	public static final String PROPERTY_KOSTEN = "kostenProMonat";
	
	public static final String PROPERTY_STATUS = "reservationStatus";
	
	public static final String PROPERTY_BEZAHLT = "bezahlt";
	
	public static final String PROPERTY_MIETER_PERSON = "mieterPerson";
	
	public static final String PROPERTY_NAME_BUCHUNG = "nameBuchung";
	
	public static final String PROPERTY_STELLPLATZ = "stellplatz";
	
	public static final int LENGTH_BEMERKUNG = 500;
	
	public static final int LENGTH_NAME = 100;
	
	public static final int LENGTH_EMAIL = 100;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
    /**
     * 1-n Beziehung zu einer Person -> Mieter des Stellplatzes.
     * Ist null, wenn von der öffentlichen Seite eine Reservation durchgeführt wird.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mieter_person")
    private Person mieterPerson;
    
    /**
     * 1-n Beziehung zu einer juristischen Person -> Mieter des Stellplatzes.
     * (Wird von der GUI bisher nicht verwendet)
     */
    /*
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mieter_firma")
    private JuristischePerson mieterJuristischePerson;
    */
    
    /**
     * 1-n Beziehung zu einem Stellplatz.
     */
    @NotNull(message = "{val.stellplatz.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Stellplatz stellplatz;
    
    @NotNull(message = "{val.reservation.datum.not.null}")
    @Temporal(TemporalType.DATE)
    @Column(name = "reservation_datum", nullable = false)
    private Date reservationDatum;
    
    @NotNull(message = "{val.beginn.datum.not.null}")
    @Temporal(TemporalType.DATE)
    @Column(name = "beginn_datum", nullable = false)
    private Date beginnDatum;
    
    @NotNull(message = "{val.ende.datum.not.null}")
    @Temporal(TemporalType.DATE)
    @Column(name = "end_datum_exklusiv", nullable = false)
    private Date endDatumExklusiv;
    
    @NotNull(message = "{val.kosten.pro.monat.not.null}")
    @Min(value = 0, message = "{val.kosten.pro.monat.min}")
    @Max(value = 500, message = "{val.kosten.pro.monat.max}")
    @Column(name="kosten_pro_monat", nullable = false)
    private double kostenProMonat;
    
    @Enumerated(EnumType.ORDINAL)
    @NotNull(message = "{val.reservation.status.not.null}")
    @Column(name="reservation_status", nullable = false)
    private ReservationStatus reservationStatus; 
    
    @Column(nullable = false)
    private boolean bezahlt;
    
    @Size(min = 0, max = 500, message = "{val.bemerkung.max.length}")
    private String bemerkung;
    
    /**
     * True, wenn der Versand der Informationsmail für die durchgeführte Verlängerung 
     * der Miete erfolgreich war.
     */
    @Column(name="email_rollover_success", nullable = false)
    private boolean emailRolloverErfolgreich;
    
    /**
     * E-Mail Adresse, an welche das Informationsmail für die durchgeführte Verlängerung
     * versendet wurde.
     */
    @Column(name="email_rollover")
    private String emailRollover;
    
    /**
     * True, wenn die Reservation ohne Anmeldung erfolgt ist.
     */
    private boolean anonym;
    
    /**
     * Zusätzliche Angabe des Names bei einer anonymen Reservation.
     */
    @Size(min = 0, max = 100, message = "{val.name.max.length}")
    private String name;
    
    /**
     * Zusätzliche Angabe der E-Mail bei einer anonymen Reservation.
     */
    @Size(min = 0, max = 100, message = "{val.email.max.length}")
    private String email;
    
    /**
     * Zusätzliche Angabe der Wohnungsnummer bei einer anonymen Reservation.
     */
    private int wohnungNr;

    /**
     * Wird nur für den Dialog "Verlängerung" benötigt -> nicht persistiert.
     */
    @Transient
    private boolean markAsRollover;
    
    // ---------------------------------------------------------
    // getter and setter 
    // ---------------------------------------------------------
    
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    
    public Date getReservationDatum() {
		return reservationDatum;
	}

	public void setReservationDatum(Date reservationDatum) {
		this.reservationDatum = reservationDatum;
	}

	public Date getBeginnDatum() {
        return beginnDatum;
    }

    public void setBeginnDatum(Date beginnDatum) {
        this.beginnDatum = beginnDatum;
    }

    public Date getEndDatumExklusiv() {
        return endDatumExklusiv;
    }

    public void setEndDatumExklusiv(Date endDatumExklusiv) {
        this.endDatumExklusiv = endDatumExklusiv;
    }
    
    public Date getEndDatum() {
        return Utility.addDay(this.endDatumExklusiv, -1);
    }

    public Stellplatz getStellplatz() {
        return stellplatz;
    }

    public void setStellplatz(Stellplatz stellplatz) {
        this.stellplatz = stellplatz;
    }
    
    public Person getMieterPerson() {
        return mieterPerson;
    }
    
    public void setMieterPerson(Person mieterPerson) {
        this.mieterPerson = mieterPerson;
    }
    
    /*
    public JuristischePerson getMieterJuristischePerson() {
        return mieterJuristischePerson;
    }
    
    public void setMieterJuristischePerson(JuristischePerson mieterJuristischePerson) {
        this.mieterJuristischePerson = mieterJuristischePerson;
    }
    */

    public double getKostenProMonat() {
		return kostenProMonat;
	}

	public void setKostenProMonat(double kostenProMonat) {
		this.kostenProMonat = kostenProMonat;
	}
	
	public ReservationStatus getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(ReservationStatus reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	
	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	
	public boolean isAnonym() {
		return anonym;
	}

	public void setAnonym(boolean anonym) {
		this.anonym = anonym;
	}

	public boolean isBezahlt() {
        return bezahlt;
    }

    public void setBezahlt(boolean bezahlt) {
        this.bezahlt = bezahlt;
    }
    
    public int getWohnungNr() {
        return wohnungNr;
    }

    public void setWohnungNr(int wohnungNr) {
        this.wohnungNr = wohnungNr;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isMarkAsRollover() {
        return markAsRollover;
    }

    public void setMarkAsRollover(boolean markAsRollover) {
        this.markAsRollover = markAsRollover;
    }
    
    public boolean isEmailRolloverErfolgreich() {
        return emailRolloverErfolgreich;
    }

    public void setEmailRolloverErfolgreich(boolean emailRolloverErfolgreich) {
        this.emailRolloverErfolgreich = emailRolloverErfolgreich;
    }

    public String getEmailRollover() {
        return emailRollover;
    }

    public void setEmailRollover(String emailRollover) {
        this.emailRollover = emailRollover;
    }

    public Long getId() {
        return id;
    }
    
    public String getNameBuchung() {
        return this.name + " (" + this.wohnungNr + ")";
    }
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
