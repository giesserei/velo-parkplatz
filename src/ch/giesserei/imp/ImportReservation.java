package ch.giesserei.imp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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
import ch.giesserei.model.ReservationStatus;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.service.ReservationService;
import ch.giesserei.util.Utility;

import com.google.inject.Inject;

/**
 * Diese Klasse importiert die bestehenden Reservationen von einer Excel-Datei (XLS).
 * 
 * @author Steffen Förster
 */
public class ImportReservation {

    private static final Logger LOG = LoggerFactory.getLogger(ImportReservation.class);
    
    private static final String FILE_RESERVATIONEN = "/reservationen.xls";

    private final Config config;
    
    private final ReservationService reservationService;
    
    @Inject
    public ImportReservation(Config config, ReservationService reservationService) {
        this.config = config;
        this.reservationService = reservationService;
    }
    
    public void importReservationen() {
        try {
            InputStream in = new FileInputStream(this.config.getImportPath() + "/" + FILE_RESERVATIONEN);
            try {
                Workbook wb = new HSSFWorkbook(in);
                Sheet sheet = wb.getSheetAt(0);
                
                boolean readNext = true;
                int rowIndex = 0;
                List<ReservationStellplatz> objekte = new ArrayList<ReservationStellplatz>();
                while (readNext) {
                    Row row = sheet.getRow(rowIndex++);
                    readNext = readCellAndAddReservation(rowIndex, row, objekte);
                }
                this.reservationService.persist(objekte);
                
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

    private boolean readCellAndAddReservation(int rowIndex, Row row, List<ReservationStellplatz> reservationen) {
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
            String name = ExcelHelper.getCellValueString(row.getCell(1));
            Integer wohnungNr = ExcelHelper.getCellValueInteger(row.getCell(2));
            Date beginnDate = ExcelHelper.getCellValueDate(row.getCell(3));
            String bezahlt = ExcelHelper.getCellValueString(row.getCell(5));
            
            if (nummer == null || name == null) {
                readNext = false;
            }
            else {
                ReservationStellplatz reservation = new ReservationStellplatz();
                reservation.setAnonym(true);
                reservation.setReservationDatum(beginnDate);
                // wir runden den Mietbeginn auf den nächsten 1. auf
                reservation.setBeginnDatum(Utility.getFirstOfNextMonth(beginnDate));
                Date endDate = Utility.addYear(reservation.getBeginnDatum());
                reservation.setEndDatumExklusiv(endDate);
                reservation.setBezahlt("x".equals(bezahlt));
                reservation.setName(name);
                reservation.setWohnungNr(wohnungNr == null ? 0 : wohnungNr);
                reservation.setReservationStatus(ReservationStatus.RESERVIERT);
                Stellplatz stellplatz = new Stellplatz();
                stellplatz.setNummer(nummer);
                reservation.setStellplatz(stellplatz);
                
                reservationen.add(reservation);
                readNext = true;
            }
        }
        return readNext;
    }
    
}
