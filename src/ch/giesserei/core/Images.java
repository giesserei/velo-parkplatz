package ch.giesserei.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.server.ThemeResource;

/**
 * Diese Klasse verwaltet die ThemeResources für die Images.
 * <p/>
 * Sollte zeitgleich dasselbe Image zum ersten Mal angefordert werden, so wird die Resource zweimal erstellt.
 * Dies hat jedoch keine Auswirkungen.
 * 
 * @author Steffen Förster
 */
@Singleton
public class Images {

    private static final String IMG_MONEY = "img/money.png";
    
    private static final String IMG_DELETE = "img/delete.png";
    
    private static final String IMG_EDIT = "img/pencil.png";
    
    private static final String IMG_SAVE = "img/disk.png";
    
    private static Images instance;
    
    private Map<String, ThemeResource> resources = 
            Collections.synchronizedMap(new HashMap<String, ThemeResource>());
    
    @Inject
    public Images() {
        Images.instance = this;
    }
    
    public static Images getInstance() {
        return instance;
    }

    public ThemeResource getImgDelete() {
        return getResource(IMG_DELETE);
    }
    
    public ThemeResource getImgMoney() {
        return getResource(IMG_MONEY);
    }
    
    public ThemeResource getImgEdit() {
        return getResource(IMG_EDIT);
    }
    
    public ThemeResource getImgSave() {
        return getResource(IMG_SAVE);
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
    
    private ThemeResource getResource(String resourceId) {
        if (this.resources.get(resourceId) == null) {
            ThemeResource res = new ThemeResource(resourceId);
            this.resources.put(resourceId, res);
        }
        return this.resources.get(resourceId);
    }
    
}
