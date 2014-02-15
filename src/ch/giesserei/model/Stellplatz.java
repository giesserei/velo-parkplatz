package ch.giesserei.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Repäsentiert einen Velo-Stellplatz.
 * 
 * @author Steffen Förster
 */
@Entity
public class Stellplatz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
    @OneToMany(mappedBy="stellplatz")
    private List<ReservationStellplatz> reservationen = new ArrayList<ReservationStellplatz>();
    
    @Min(value = 1, message = "{val.stellplatz.nummer.min}")
    @Max(value = 500, message = "{val.stellplatz.nummer.max}")
    private int nummer;
    
    @Min(value = 1, message = "{val.stellplatz.sektor.range}")
    @Max(value = 6, message = "{val.stellplatz.sektor.range}")
    private int sektor;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{val.stellplatz.typ.not.null}")
    @Column(nullable = false)
    private StellplatzTyp typ;
    
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

    public int getSektor() {
		return sektor;
	}

	public void setSektor(int sektor) {
		this.sektor = sektor;
	}

    public StellplatzTyp getTyp() {
        return typ;
    }

    public void setTyp(StellplatzTyp typ) {
        this.typ = typ;
    }

    public List<ReservationStellplatz> getReservationen() {
        return reservationen;
    }

    public Long getId() {
        return id;
    }
    
}
