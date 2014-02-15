package ch.giesserei.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Modell für eine Juristische Person (Bisher nicht verwendet).
 * <p/>
 * Eine JuristischePerson hat eine 1-n Beziehung zu Verträgen (Bisher nicht verwendet).
 * 
 * @author Steffen Förster
 */
@Entity
@Table(name="NN_PERSON")
public class JuristischePerson {
	
	public static final String NESTED_PROPERTY_ADRESSE_ORT = "adresse.ort";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
    @NotNull(message = "{val.name.not.null}")
    @Size(min = 1, max = 100, message = "val.name.max.length")
    @Column(nullable = false)
    private String name;
    
    @Size(min=1, max=100, message = "{val.ansprechpartner.max.length}")
    private String ansprechpartner;
    
    @Embedded
    private Adresse adresse;
    
    @OneToMany(mappedBy="mieterJuristischePerson")
    private List<Vertrag> vertraege = new ArrayList<Vertrag>();

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getAnsprechpartner() {
        return ansprechpartner;
    }

    public void setAnsprechpartner(String ansprechpartner) {
        this.ansprechpartner = ansprechpartner;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public List<Vertrag> getVertraege() {
        return vertraege;
    }

    public Long getId() {
        return id;
    }
}
