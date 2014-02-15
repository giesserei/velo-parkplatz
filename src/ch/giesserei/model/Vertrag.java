package ch.giesserei.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Dieses Modell wird vorläufig nicht benötigt.
 *  
 * @author Steffen Förster
 */
@Entity
public class Vertrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
    @Temporal(TemporalType.DATE)
    @Column(name="beginn_datum")
    private Date beginnDatum;
    
    @Temporal(TemporalType.DATE)
    @Column(name="end_datum")
    private Date endDatum;
    
    @ManyToMany
    @JoinTable(
        name="vertrag_mieter",
        joinColumns={@JoinColumn(name="vertrag_id")},
        inverseJoinColumns={@JoinColumn(name="person_id")})
    private List<Person> mieterPersonen = new ArrayList<Person>();
    
    @ManyToMany
    @JoinTable(
        name="vertrag_bewohner",
        joinColumns={@JoinColumn(name="vertrag_id")},
        inverseJoinColumns={@JoinColumn(name="person_id")})
    private List<Person> mitbewohner = new ArrayList<Person>();
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mieter_firma")
    private JuristischePerson mieterJuristischePerson;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private Mietobjekt mietobjekt;
    
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Date getBeginnDatum() {
        return beginnDatum;
    }

    public void setBeginnDatum(Date beginnDatum) {
        this.beginnDatum = beginnDatum;
    }

    public Date getEndDatum() {
        return endDatum;
    }

    public void setEndDatum(Date endDatum) {
        this.endDatum = endDatum;
    }

    public Mietobjekt getMietobjekt() {
        return mietobjekt;
    }

    public void setMietobjekt(Mietobjekt mietobjekt) {
        this.mietobjekt = mietobjekt;
    }
    
    public JuristischePerson getMieterJuristischePerson() {
        return mieterJuristischePerson;
    }
    
    public void setMieterJuristischePerson(JuristischePerson mieterJuristischePerson) {
        this.mieterJuristischePerson = mieterJuristischePerson;
    }

    public List<Person> getMieterPersonen() {
        return mieterPersonen;
    }

    public List<Person> getMitbewohner() {
        return mitbewohner;
    }

    public Long getId() {
        return id;
    }
}
