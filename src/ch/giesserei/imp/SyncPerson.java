package ch.giesserei.imp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.core.NestedException;
import ch.giesserei.core.Config;
import ch.giesserei.model.Person;
import ch.giesserei.service.PersonService;

import com.google.inject.Inject;

/**
 * Diese Klasse importiert Personen von einer Excel-Datei (XLS). Existierende Personen 
 * werden in der Datenbank aktualisiert.
 * 
 * @author Steffen Förster
 */
public class SyncPerson {

    private static final Logger LOG = LoggerFactory.getLogger(SyncPerson.class);
    
    private static final String FILE_PERSONEN = "/personen.xls";

    private final Config config;
    
    private final PersonService personService;
    
    @Inject
    public SyncPerson(Config config, PersonService personService) {
        this.config = config;
        this.personService = personService;
    }
    
    /**
     * Startet die Synchronization der Personen-Daten.
     * 
     * @return Anzahl der synchronisierten Personen
     */
    public int syncPersonen() {
        try {
            InputStream in = new FileInputStream(this.config.getImportPath() + "/" + FILE_PERSONEN);
            try {
                Workbook wb = new HSSFWorkbook(in);
                Sheet sheet = wb.getSheetAt(0);
                
                boolean readNext = true;
                int rowIndex = 0;
                List<Person> personen = new ArrayList<Person>();
                while (readNext) {
                    Row row = sheet.getRow(rowIndex++);
                    readNext = readCellAndAddPerson(rowIndex, row, personen);
                }
                this.personService.synchronize(personen);
                
                LOG.info("imported rows: " + (rowIndex - 1));
                return rowIndex - 1;
            }
            finally {
                in.close();
            }
        }
        catch (Exception e) {
            throw NestedException.wrap(e);
        }
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------

    private boolean readCellAndAddPerson(int rowIndex, Row row, List<Person> personen) {
        boolean readNext = false;
        if (row == null) {
            return false;
        }
        
        Cell cell = row.getCell(0);
        if (cell == null) {
            readNext = false;
        }
        else {
            Integer id = ExcelHelper.getCellValueInteger(cell);
            String email = ExcelHelper.getCellValueString(row.getCell(1));
            String nachname = ExcelHelper.getCellValueString(row.getCell(2));
            String vorname = ExcelHelper.getCellValueString(row.getCell(3));
            String wohnung = ExcelHelper.getCellValueString(row.getCell(4));
            
            if (id == null || email == null || nachname == null || vorname == null || wohnung == null) {
                readNext = false;
            }
            else {
                Person person = new Person();
                person.setUserId((long) id);
                person.setEmail(email);
                person.setNachname(nachname);
                person.setVorname(vorname);
                person.setWohnungNr(formatWohnungNr(wohnung));
                personen.add(person);
                readNext = true;
            }
        }
        return readNext;
    }
    
    private String formatWohnungNr(String wohnung) {
        // ".0" abschneiden
        if (wohnung.length() == 6) {
            return wohnung.substring(0, 4);
        }
        return wohnung;
    }
}
