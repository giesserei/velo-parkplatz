package ch.giesserei.model;

/**
 * Es gibt verschiedene Typen von Velo-Stellplätzen in der Siedlung.
 * 
 * @author Steffen Förster
 */
public enum StellplatzTyp {

    PEDALPARC_TIEF("stellplatz.lb.typ.pedalparc.tief"),
    
    PEDALPARC_HOCH("stellplatz.lb.typ.pedalparc.hoch"),
    
    SPEZIAL("stellplatz.lb.typ.spezial");
    
    private final String resourceKey;
    
    private StellplatzTyp(String resourceKey) {
    	this.resourceKey = resourceKey;
    }
    
    public String getResourceKey() {
    	return resourceKey;
    }
    
    public static StellplatzTyp getValueByName(String typName) {
    	for (StellplatzTyp value : values()) {
    		if (value.toString().equals(typName)) {
    			return value;
    		}
    	}
    	throw new IllegalArgumentException("unbekannter StellplatzTyp: " + typName);
    }
    
}