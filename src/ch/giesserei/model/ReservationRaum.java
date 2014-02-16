package ch.giesserei.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Modell für eine Raum-Reservation.
 * 
 * @author Steffen Förster
 */
@Entity
@Table(name="RESERVATION_RAUM")
public class ReservationRaum {

    public static final int LENGTH_BEMERKUNG = 500;
    
    public static final int LENGTH_NAME = 100;
    
    public static final int LENGTH_EMAIL = 100;
    
    public static final int LENGTH_TELEFON = 20;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
    /**
     * 1-n Beziehung zu einer Person -> Mieter des Raumes.
     * Ist null, wenn von der öffentlichen Seite eine Reservation durchgeführt wird.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mieter_person")
    private Person mieterPerson;
    
    /**
     * 1-n Beziehung zu einem Raum.
     */
    @NotNull(message = "{val.raum.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Raum raum;
    
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
    
    @NotNull(message = "{val.kosten.raum.not.null}")
    @Min(value = 0, message = "{val.kosten.raum.min}")
    @Column(nullable = false)
    private double kosten;
    
    @Column(nullable = false)
    private boolean bezahlt;
    
    @Size(min = 0, max = 500, message = "{val.bemerkung.max.length}")
    @Column(length = 500)
    private String bemerkung;
    
    @NotNull(message = "{val.anzahl.personen.not.null}")
    @Min(value = 1, message = "{val.anzahl.personen.min}")
    @Column(name="anzahl_personen", nullable = false)
    private int anzahlPersonen;
    
    /**
     * True, wenn die Reservation ohne Anmeldung erfolgt ist.
     */
    private boolean anonym;
    
    @Size(min = 1, max = 100, message = "{val.name.max.length}")
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotNull(message = "{val.email.not.null}")
    @Size(min = 1, max = 100, message="{val.email.max.length}")
    @Column(nullable = false, length = 100)
    private String email;
    
    @Size(min = 1, max = 20, message = "{val.telefon.max.length}")
    @Column(length = 20)
    private String telefon;
    
    @Min(value = 2000, message = "{val.wohnung.nr.min}")
    @Max(value = 2999, message = "{val.wohnung.nr.max}")
    private int wohnungNr;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Person getMieterPerson() {
        return mieterPerson;
    }

    public void setMieterPerson(Person mieterPerson) {
        this.mieterPerson = mieterPerson;
    }

    public Raum getRaum() {
        return raum;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
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

    public double getKosten() {
        return kosten;
    }

    public void setKosten(double kosten) {
        this.kosten = kosten;
    }

    public boolean isBezahlt() {
        return bezahlt;
    }

    public void setBezahlt(boolean bezahlt) {
        this.bezahlt = bezahlt;
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

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public int getWohnungNr() {
        return wohnungNr;
    }

    public void setWohnungNr(int wohnungNr) {
        this.wohnungNr = wohnungNr;
    }
    
}
