package ch.giesserei.page.reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.util.Utility;

/**
 * Bean für die öffentliche Reservationsseite.
 * 
 * @author Steffen Förster
 */
public class ReservationBean {
    
    public static final String PARAM_TYP = "typeGroup";
    
    public static final String PARAM_NUMMER = "nummer";
    
    public static final String PARAM_NAME = "name";
    
    public static final String PARAM_EMAIL = "email";
    
    public static final String PARAM_WOHNUNG = "wohnung";
    
    public static final String PARAM_BEMERKUNG = "bemerkung";
    
    public static final String PARAM_BEGINN = "beginn";
    
    public static final String PARAM_ENDE = "ende";
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    
    private String typ;
    
    private String nummer;
    
    private String name;
    
    private String email;
    
    private String wohnung;
    
    private String bemerkung;
    
    private String beginn;
    
    private String ende;
    
    private String validationError;
    
    private String errorField;
    
    private boolean success = false;
    
    private boolean mailSend = false;
    
    private boolean reservationSaveError = false;
    
    private final boolean init;
    
    private Stellplatz stellplatz;
    
    public ReservationBean() {
        this(false);
    }
    
    public ReservationBean(boolean init) {
        this.init = init;
        this.typ = StellplatzTyp.PEDALPARC_HOCH.toString();
        this.beginn = this.dateFormat.format(Utility.getFirstOfNextMonth());
    }
    
    /**
     * Konstruktor wird genutzt, wenn die Reservationsseite mit für einen konkreten 
     * Stellplatz aufgerufen wird.
     */
    public ReservationBean(boolean init, String nummer, String typ) {
        this.init = init;
        this.typ = typ;
        this.nummer = nummer;
        this.beginn = this.dateFormat.format(Utility.getFirstOfNextMonth());
    }
    
    @SuppressWarnings("rawtypes")
    public void setParamValues(Map parameterMap) {
        this.typ = Utility.getParamValue(parameterMap, PARAM_TYP);
        this.nummer = Utility.getParamValue(parameterMap, PARAM_NUMMER);
        this.name = Utility.getParamValue(parameterMap, PARAM_NAME);
        this.email = Utility.getParamValue(parameterMap, PARAM_EMAIL);
        this.wohnung = Utility.getParamValue(parameterMap, PARAM_WOHNUNG);
        this.bemerkung = Utility.getParamValue(parameterMap, PARAM_BEMERKUNG);
        this.beginn = Utility.getParamValue(parameterMap, PARAM_BEGINN);
        this.ende = Utility.getParamValue(parameterMap, PARAM_ENDE);
    }
    
    public boolean isInit() {
        return init;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public boolean isMailSend() {
        return mailSend;
    }

    public void setMailSend(boolean mailSend) {
        this.mailSend = mailSend;
    }

    public boolean isReservationSaveError() {
        return reservationSaveError;
    }

    public void setReservationSaveError(boolean reservationSaveError) {
        this.reservationSaveError = reservationSaveError;
    }

    public String getValidationError() {
        return validationError;
    }

    public void setValidationError(String validationError) {
        this.validationError = validationError;
    }
    
    public String getErrorField() {
        return errorField;
    }

    public void setErrorField(String errorField) {
        this.errorField = errorField;
    }

    public String getTyp() {
        return typ;
    }

    public String getNummer() {
        return nummer;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getWohnung() {
        return wohnung;
    }
    public String getBemerkung() {
        return bemerkung;
    }
    
    public String getBeginn() {
        return beginn;
    }
    
    public Date getBeginnDate() {
        try {
            return this.dateFormat.parse(this.beginn);
        }
        catch (ParseException e) {
            return new Date();
        }
    }

    public String getEnde() {
        return ende;
    }
    
    public void setEnde(String ende) {
        this.ende = ende;
    }

    public Date getEndeDate() {
        try {
            return this.dateFormat.parse(this.ende);
        }
        catch (ParseException e) {
            return new Date();
        }
    }

    public StellplatzTyp getStellplatzTyp() {
        return StellplatzTyp.getValueByName(this.typ);
    }
    
    public SimpleDateFormat getDateFormat() {
        return this.dateFormat;
    }
    
    public Stellplatz getStellplatz() {
        return stellplatz;
    }

    public void setStellplatz(Stellplatz stellplatz) {
        this.stellplatz = stellplatz;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
