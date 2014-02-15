package ch.giesserei.core;

import ch.giesserei.resource.AppResources;
import ch.giesserei.resource.ValidationMessages;

/**
 * Im SessionContainer werden Daten für einen User gespeichert, auf die 
 * während einer Session zugegriffen werden soll.
 *  
 * @author Steffen Förster
 */
public class SessionContainer {

    public static final String KEY = "session_container";
    
    private AppResources appResources;
    
    private ValidationMessages valMessages;

    public AppResources getAppResources() {
        return appResources;
    }

    public void setAppResources(AppResources appResources) {
        this.appResources = appResources;
    }

    public ValidationMessages getValMessages() {
        return valMessages;
    }

    public void setValMessages(ValidationMessages valMessages) {
        this.valMessages = valMessages;
    }
    
}
