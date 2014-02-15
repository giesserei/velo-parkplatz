package ch.giesserei.page.stellplatzliste;

import java.util.ArrayList;
import java.util.List;

import ch.giesserei.core.WebUtils;
import ch.giesserei.service.model.StellplatzStatus;

/**
 * Bean für die öffentliche Stellplatzliste.
 * 
 * @author Steffen Förster
 */
public class StellplatzlisteBean {

    private List<List<StellplatzStatus>> sektors = new ArrayList<List<StellplatzStatus>>();
    
    public StellplatzlisteBean() {
    }

    public List<List<StellplatzStatus>> getSektors() {
        return sektors;
    }

    public void addSektor(List<StellplatzStatus> sektor) {
        this.sektors.add(sektor);;
    }
    
    public String getLinkReservation() {
        return WebUtils.getPathReservation();
    }
}
