package ch.giesserei.imp;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelHelper {

    private ExcelHelper() {
    }
    
    public static String getCellValueString(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            String value = cell.getStringCellValue();
            if (StringUtils.isBlank(value)) {
                return null;
            }
            return value;
        }
        else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        else {
            return null;
        }
    }
    
    public static Integer getCellValueInteger(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            String value = cell.getStringCellValue();
            if (StringUtils.isBlank(value)) {
                return null;
            }
            else {
                try {
                    BigDecimal valueNumber = new BigDecimal(value);
                    return valueNumber.intValue();
                }
                catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            BigDecimal value = new BigDecimal(String.valueOf(cell.getNumericCellValue()));
            return value.intValue();
        }
        else {
            return null;
        }
    }
    
    public static Date getCellValueDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        return cell.getDateCellValue();
    }
    
}
