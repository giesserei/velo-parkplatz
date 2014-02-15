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
import ch.giesserei.model.Mietobjekt;
import ch.giesserei.model.MietobjektTyp;
import ch.giesserei.service.MietobjektService;

import com.google.inject.Inject;

/**
 * Diese Klasse importiert Mietobjekte von einer Excel-Datei (XLS).
 * 
 * @author Steffen Förster
 */
public class ImportMietobjekt {

    private static final Logger LOG = LoggerFactory.getLogger(ImportMietobjekt.class);
    
    private static final String FILE_MIETOBJEKTE = "/mietobjekte.xls";

    private final Config config;
    
    private final MietobjektService mietobjektService;
    
    @Inject
    public ImportMietobjekt(Config config, MietobjektService mietobjektService) {
        this.config = config;
        this.mietobjektService = mietobjektService;
    }
    
    public void importMietobjekte() {
        try {
            InputStream in = new FileInputStream(this.config.getImportPath() + "/" + FILE_MIETOBJEKTE);
            try {
                Workbook wb = new HSSFWorkbook(in);
                Sheet sheet = wb.getSheetAt(0);
                
                boolean readNext = true;
                int rowIndex = 0;
                List<Mietobjekt> objekte = new ArrayList<Mietobjekt>();
                while (readNext) {
                    Row row = sheet.getRow(rowIndex++);
                    readNext = readCellAndAddObjekt(rowIndex, row, objekte);
                }
                this.mietobjektService.persist(objekte);
                
                LOG.info("imported rows: " + (rowIndex - 1));
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

    private boolean readCellAndAddObjekt(int rowIndex, Row row, List<Mietobjekt> objekte) {
        boolean readNext = false;
        if (row == null) {
            return false;
        }
        
        Cell cell = row.getCell(0);
        if (cell == null) {
            readNext = false;
        }
        else {
            Integer nummer = ExcelHelper.getCellValueInteger(cell);
            if (nummer == null) {
                readNext = false;
            }
            else {
                Mietobjekt mietobjekt = new Mietobjekt();
                mietobjekt.setNummer(nummer);
                // ist zwar nicht immer richtig, für die Velo-DB aber ausreichend, da der Typ nicht öffentlich 
                // angezeigt wird
                mietobjekt.setTyp(MietobjektTyp.WOHNUNG);
                objekte.add(mietobjekt);
                readNext = true;
            }
        }
        return readNext;
    }
    
    
}
