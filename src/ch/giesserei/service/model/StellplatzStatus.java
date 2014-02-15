package ch.giesserei.service.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ch.giesserei.model.StellplatzTyp;

/**
 * Diese Klasse wird verwendet, um den Reservations-Status zu einem Stellplatz anzuzeigen.
 * 
 * @author Steffen Förster
 */
public class StellplatzStatus {

    private final int nummer;
    
    private final String wohnungsNr;
    
    private final StellplatzTyp typ;
    
    private final Date reserviertBis;
    
    public StellplatzStatus(Integer nummer, StellplatzTyp typ, Date reserviertBis, String wohnungsNr) {
        this.nummer = nummer;
        this.typ = typ;
        this.wohnungsNr = wohnungsNr;
        this.reserviertBis = reserviertBis;
    }

    public int getNummer() {
        return nummer;
    }

    public String getWohnungsNr() {
        return this.wohnungsNr == null ? "-" : this.wohnungsNr;
    }

    public StellplatzTyp getTyp() {
        return typ;
    }
    
    /**
     * Liefert das effektive Mietende, also nicht inklusiv.
     */
    public Date getReserviertBis() {
        return reserviertBis;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
