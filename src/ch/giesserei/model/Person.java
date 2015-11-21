package ch.giesserei.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Modell für eine natürliche Person.
 * 
 * @author Steffen Förster
 */
@Entity
public class Person {
	
	public static final String NESTED_PROPERTY_ADRESSE_ORT = "adresse.ort";
	
	public static final String PROPERTY_NAME_WOHNUNG = "nameUndWohnung";
	
	public static final String PROPERTY_USER_ID = "userId";
	
	public static final String PROPERTY_VORNAME = "vorname";
	
	public static final String PROPERTY_NACHNAME = "nachname";
	
	public static final String PROPERTY_EMAIL = "email";
	
	public static final String PROPERTY_WOHNUNG = "wohnungNr";
	
	public static final String PROPERTY_UPDATED = "updatedInLastSync";
	
	public static final Person ANONYM;
	
	static {
	    ANONYM = new Person();
	    ANONYM.setNachname("Anonym");
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private long version;
    
    /**
     * User-ID aus Joomla -> für Replikation.
     */
    @Column(unique = true)
    private Long userId;
    
    /**
     * Ist true, wenn bei der letzten Synchronisation ein Update durchgeführt wurde.
     * Wenn dieser Wert false ist, ist zu prüfen, ob die Person in der Master-DB
     * noch vorhanden ist.
     */
    private boolean updatedInLastSync;
    
    @NotNull(message = "{val.vorname.not.null}")
    @Size(min = 1, max = 75, message = "{val.vorname.max.length}")
    @Column(nullable = false)
    private String vorname;
    
    @NotNull(message="{val.name.not.null}")
    @Size(min = 1, max = 75, message = "{val.name.max.length}")
    @Column(nullable = false)
    private String nachname;
    
    @NotNull(message = "{val.email.not.null}")
    @Size(min = 1, max = 75, message="{val.email.max.length}")
    @Column(nullable = false)
    private String email;
    
    private int geburtsjahr;
    
    /**
     * Wohnungsnummer wird in Person gespeichert, da es für die Velo-Verwaltung 
     * keine Wohnungsmietverträge braucht. Die Information der Wohnung wird jedoch
     * in der Stellplatzliste benötigt. Dies könnte allenfalls später geändert werden.
     */
    @NotNull(message="{val.wohnung.nr.not.null}")
    @Size(min = 4, max = 100, message="{val.wohnung.nr.max.length}")
    @Column(name="wohnung_nr", nullable = false)
    private String wohnungNr;

    @Embedded
    private Adresse adresse;
    
    /**
     * Flag wird nicht persistiert.
     */
    @Transient
    private boolean vertragVorhanden;
    
    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGeburtsjahr() {
        return geburtsjahr;
    }

    public void setGeburtsjahr(int geburtsjahr) {
        this.geburtsjahr = geburtsjahr;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }
    
    public String getWohnungNr() {
		return wohnungNr;
	}

	public void setWohnungNr(String wohnungNr) {
		this.wohnungNr = wohnungNr;
	}

	public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public boolean isUpdatedInLastSync() {
        return updatedInLastSync;
    }

    public void setUpdatedInLastSync(boolean updatedInLastSync) {
        this.updatedInLastSync = updatedInLastSync;
    }

    public Long getId() {
        return id;
    }

	public boolean isVertragVorhanden() {
		return vertragVorhanden;
	}

	public void setVertragVorhanden(boolean vertragVorhanden) {
		this.vertragVorhanden = vertragVorhanden;
	}
    
	public String getNameVollstaendig() {
		return this.vorname + " " + this.nachname;
	}
	
	public String getNameUndWohnung() {
		return this.vorname + " " + this.nachname + " (" + this.wohnungNr + ")";
	}

    /**
     * In der GUI müssen Personen mit der gleichen ID auch als eine Person behandelt werden.
     * Daher werden equals() und hashCode() überschrieben.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
	
}
