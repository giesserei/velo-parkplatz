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
 * Modell für eine Raum-Reservation. Eine Raum-Reservation wird nicht mit einer Person verbunden.
 * Alle Daten des Mieters sind in der Reservation gespeichert.
 * 
 * @author Steffen Förster
 */
@Entity
@Table(name="RESERVATION_RAUM")
public class ReservationRaum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
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
    private String bemerkung;
    
    /**
     * True, wenn die Reservation ohne Anmeldung erfolgt ist.
     */
    private boolean anonym;
    
    @Size(min = 1, max = 150, message = "{val.name.max.length}")
    @Column(nullable = false)
    private String name;
    
    @NotNull(message = "{val.email.not.null}")
    @Size(min = 1, max = 75, message="{val.email.max.length}")
    @Column(nullable = false)
    private String email;
    
    @NotNull(message = "{val.telefon.not.null}")
    @Size(min = 1, max = 20, message = "{val.telefon.max.length}")
    @Column(nullable = false)
    private String telefon;
    
    @NotNull(message = "{val.wohnung.nr.not.null}")
    @Min(value = 2000, message = "{val.wohnung.nr.min}")
    @Max(value = 2999, message = "{val.wohnung.nr.max}")
    @Column(nullable = false)
    private int wohnungNr;
    
    
}
