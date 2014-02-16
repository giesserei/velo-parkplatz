package ch.giesserei.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Modell für ein Mietobjekt.
 * <p/>
 * Ein Mietobjekt hat eine 1-n Beziehung zu Verträgen. (Bisher nicht verwendet.)
 * 
 * @author Steffen Förster
 */
@Entity
public class Mietobjekt {

    public static final String PROPERTY_NUMMER = "nummer";
    
    public static final int LENGTH_BEZEICHNUNG = 50;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
    @NotNull(message = "{val.wohnung.nr.not.null}")
    @Min(value = 2000, message = "{val.wohnung.nr.min}")
    @Max(value = 2999, message = "{val.wohnung.nr.max}")
    @Column(nullable = false)
    private int nummer;
    
    @Size(min = 0, max = 50, message = "{val.bezeichnung.max.length}")
    @Column(length = 50)
    private String bezeichnung;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MietobjektTyp typ;
    
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    
    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public MietobjektTyp getTyp() {
        return typ;
    }

    public void setTyp(MietobjektTyp typ) {
        this.typ = typ;
    }

    public Long getId() {
        return id;
    }
    
}
