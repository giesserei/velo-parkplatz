package ch.giesserei.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Modell für einen Raum, welcher für mehrere Tage vermietet werden kann.
 * Bisher sind dies nur die Gästezimmer in der Giesserei.
 * 
 * @author Steffen Förster
 */
@Entity
public class Raum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
    @OneToMany(mappedBy="raum")
    private List<ReservationRaum> reservationen = new ArrayList<ReservationRaum>();
    
    @NotNull(message="{val.bezeichnung.not.null}")
    @Size(min = 1, max = 50, message = "{val.bezeichnung.max.length}")
    @Column(nullable = false, length = 50)
    private String bezeichnung;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
    
}
