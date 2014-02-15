package ch.giesserei.imp;

import static ch.giesserei.model.StellplatzTyp.PEDALPARC_HOCH;
import static ch.giesserei.model.StellplatzTyp.PEDALPARC_TIEF;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.service.StellplatzService;

import com.google.inject.Inject;

/**
 * Diese Klasse legt für die Initialisierung der Datenbank alle Stellplätze an.
 * 
 * @author Steffen Förster
 */
public class ImportStellplatz {
    
    private static final Logger LOG = LoggerFactory.getLogger(ImportStellplatz.class);

    private final StellplatzService stellplatzService;
    
    @Inject
    public ImportStellplatz(StellplatzService stellplatzService) {
        this.stellplatzService = stellplatzService;
    }
    
    /**
     * Erstellt die Stellplätze in der Datenbank. Dies ist nur bei der Initialisierung der 
     * Datenbank nötig.
     */
    public void importStellplaetze() {
        LOG.info("begin createStellplaetze");
        List<Stellplatz> stellplatzListe = new ArrayList<Stellplatz>();
        
        createSektor1(stellplatzListe);
        createSektor2(stellplatzListe);
        createSektor3(stellplatzListe);
        createSektor4(stellplatzListe);
        createSektor5(stellplatzListe);
        createSektor6(stellplatzListe);
        
        this.stellplatzService.persist(stellplatzListe);
        
        LOG.info("end createStellplaetze");
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
    
    private void createSektor1(List<Stellplatz> stellplatzListe) {
        createStellplatz(stellplatzListe, PEDALPARC_HOCH, PEDALPARC_TIEF, 41, 43, 1);
        createStellplatz(stellplatzListe, PEDALPARC_TIEF, PEDALPARC_HOCH, 44, 50, 1);
        createStellplatz(stellplatzListe, PEDALPARC_HOCH, PEDALPARC_TIEF, 51, 69, 1);
    }
    
    private void createSektor2(List<Stellplatz> stellplatzListe) {
        createStellplatz(stellplatzListe, PEDALPARC_TIEF, PEDALPARC_HOCH, 70, 76, 2);
        createStellplatz(stellplatzListe, PEDALPARC_HOCH, PEDALPARC_TIEF, 77, 81, 2);
        createStellplatz(stellplatzListe, PEDALPARC_TIEF, PEDALPARC_HOCH, 82, 106, 2);
        createStellplatz(stellplatzListe, PEDALPARC_HOCH, PEDALPARC_TIEF, 107, 127, 2);
    }
    
    private void createSektor3(List<Stellplatz> stellplatzListe) {
        for (int i = 1; i <= 15; i++) {
            createStellplatz(stellplatzListe, i, 3, StellplatzTyp.SPEZIAL);
        }
    }
    
    private void createSektor4(List<Stellplatz> stellplatzListe) {
        for (int i = 16; i <= 26; i++) {
            createStellplatz(stellplatzListe, i, 4, StellplatzTyp.SPEZIAL);
        }
        createStellplatz(stellplatzListe, PEDALPARC_HOCH, PEDALPARC_TIEF, 128, 183, 4);
    }
    
    private void createSektor5(List<Stellplatz> stellplatzListe) {
        for (int i = 27; i <= 32; i++) {
            createStellplatz(stellplatzListe, i, 5, StellplatzTyp.SPEZIAL);
        }
        createStellplatz(stellplatzListe, PEDALPARC_TIEF, PEDALPARC_HOCH, 184, 244, 5);
    }
    
    private void createSektor6(List<Stellplatz> stellplatzListe) {
        for (int i = 33; i <= 39; i++) {
            createStellplatz(stellplatzListe, i, 6, StellplatzTyp.SPEZIAL);
        }
        createStellplatz(stellplatzListe, PEDALPARC_HOCH, PEDALPARC_TIEF, 245, 298, 6);
    }
    
    private void createStellplatz(List<Stellplatz> stellplatzListe, StellplatzTyp typGerade, StellplatzTyp typUngerade, 
            int nummerVon, int nummerBis, int sektor) {
        
        for (int i = nummerVon; i <= nummerBis; i++) {
            if (i % 2 == 0) {
                createStellplatz(stellplatzListe, i, sektor, typGerade);
            }
            else {
                createStellplatz(stellplatzListe, i, sektor, typUngerade);
            }
        }
    }
    
    private void createStellplatz(List<Stellplatz> stellplatzListe, int nummer, int sektor, StellplatzTyp typ) {
        Stellplatz stellplatz = new Stellplatz();
        stellplatz.setNummer(nummer);
        stellplatz.setSektor(sektor);
        stellplatz.setTyp(typ);
        stellplatzListe.add(stellplatz);
    }
    
}
