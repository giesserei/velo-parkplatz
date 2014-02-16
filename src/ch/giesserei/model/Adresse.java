package ch.giesserei.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

/**
 * Adresse einer natürlichen oder juristischen Person.
 * Die Adress-Felder werden in ein anderes Model eingebettet.
 * 
 * @author Steffen Förster
 */
@Embeddable 
public class Adresse {
    
    public static final int LENGTH_STRASSE = 100;
    
    public static final int LENGTH_ORT = 100;

    @NotNull(message = "{val.strasse.not.null}")
    @Size(min = 1, max = 100, message = "{val.strasse.max.length}")
    @Column(length = 100)
    private String strasse;
    
    @NotNull(message="{val.plz.not.null}")
    @Min(value = 1000, message = "val.plz.min")
    @Max(value = 9999, message = "val.plz.max")
    private int plz;
    
    @NotNull(message="{val.ort.not.null}")
    @Size(min = 1, max = 100, message = "{val.ort.max.length}")
    @Column(length = 100)
    private String ort;

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }
    
    public boolean istEmpty() {
    	return StringUtils.isBlank(this.ort) && StringUtils.isBlank(this.strasse) && this.plz == 0;
    }
    
}