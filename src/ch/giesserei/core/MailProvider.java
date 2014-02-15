package ch.giesserei.core;

import java.util.Date;

import ch.giesserei.model.ReservationStellplatz;

/**
 * Provider zum Versenden von E-Mails.
 * 
 * @author Steffen Förster
 */
public interface MailProvider {
   
    /**
     * Versendet eine Text-Mail an den übergebenen Empfänger.
     * @param subject Betreff
     * @param message Text
     * @param mailto Empfänger der Mail
     * @return true, wenn die Mail erfolgreich versendet wurde
     */
    boolean sendMail(String subject, String message, String mailto);
    
    /**
     * Versendet ein Bestätigungsmail nach einer erfolgten Reservationan den Bewohner.
     * Zusätzlich wird auch die Verwaltung informiert.
     * 
     * @param name Name des Mieters
     * @param wohnung Wohnungsnummer
     * @param nummer Stellplatznummer
     * @param beginn Beginn der Reservation
     * @param ende Ende der Reservation
     * @param kosten Gesamtkosten der Reservation
     * @param typ Typ des Stellplatzes
     * @param mailto Empfänger der Mail
     * @return true, wenn die Mail erfolgreich versendet wurde
     */
    boolean sendReservationMail(String name, int wohnung, int nummer, Date beginn, Date ende, 
            double kosten, String typ, String mailto);
    
    /**
     * Versendet ein Bestätigungsmail nach einer erfolgten Mietverlängerung.
     * 
     * @param reservation Reservation der Mietverlängerung
     * @param kosten Gesamtkosten der Reservation
     * @param typ Typ des Stellplatzes
     * @param mailto Empfänger der Mail
     * @return true, wenn die Mail erfolgreich versendet wurde
     */
    boolean sendRolloverMail(ReservationStellplatz reservation, double kosten, String typ, String mailto);
    
    /**
     * Versendet eine Zahlungserinnerung. Vor dem Senden kann der Mailtext noch bearbeitet werden.
     * 
     * @param reservation Reservation
     * @param kosten Gesamtkosten der Reservation
     * @param typ Typ des Stellplatzes
     * @param mailto Empfänger der Mail
     */
    void sendReminderMail(ReservationStellplatz reservation, double kosten, String typ, String mailto);
    
    /**
     * Versendet eine Information an die Velo-Gruppe. Vor dem Senden kann der Mailtext noch bearbeitet werden.
     * 
     * @param reservation Reservation
     * @param typ Typ des Stellplatzes
     */
    void sendVeloGruppeMail(ReservationStellplatz reservation, String typ);
    
    /**
     * Sendet eine Mail, dass nicht alle Personen aktualisiert wurden.
     * 
     * @param anzahlNichtAktualisiert Anzahl der Personen, die nicht aktualisiert wurden -> wahrscheinlich keine Bewohner mehr
     * @return true, wenn die Mail erfolgreich versendet wurde
     */
    boolean sendPersonenSyncMail(long anzahlNichtAktualisiert);
    
    /**
     * Versendet eine Zahlungsbestätigung. Vor dem Senden kann der Mailtext noch bearbeitet werden.
     * 
     * @param reservation Reservation
     * @param kosten Gesamtkosten der Reservation
     * @param typ Typ des Stellplatzes
     * @param mailto Empfänger der Mail
     */
    void sendZahlungErhaltenMail(ReservationStellplatz reservation, double kosten, String typ, String mailto);
}
