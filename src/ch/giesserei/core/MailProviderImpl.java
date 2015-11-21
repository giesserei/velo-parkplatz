package ch.giesserei.core;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.app.gui.ConfirmWindow.Decision;
import ch.giesserei.app.gui.MailWindow;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.resource.AppRes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * Provider zum Versenden von E-Mails.
 * 
 * @author Steffen Förster
 */
@Singleton
public class MailProviderImpl implements MailProvider {

    private static final Logger LOG = LoggerFactory.getLogger(MailProviderImpl.class);
    
    private final Config config;
    
    private final TemplateProvider templateProvider;
    
    @Inject
    public MailProviderImpl(Config config, TemplateProvider templateProvider) {
        this.config = config;
        this.templateProvider = templateProvider;
    }
    
    @Override
    public boolean sendMail(String subject, String message, String mailto) {
        try {
            Email email = createEmail();
            email.setCharset("UTF-8");
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(mailto);
            email.send();
            return true;
        }
        catch (AddressException e) {
            LOG.warn(e.getMessage(), e);
        }
        catch (EmailException e) {
            LOG.warn(e.getMessage(), e);
        }
        return false;
    }
    
    @Override
    public boolean sendReservationMail(String name, int wohnung, int nummer, Date beginn, Date ende, 
            double kosten, String typ, String mailto) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("wohnung", String.valueOf(wohnung));
        model.put("name", name);
        model.put("email", mailto);
        model.put("nummer", String.valueOf(nummer));
        model.put("beginn", beginn);
        model.put("ende", ende);
        model.put("kosten", kosten);
        model.put("typ", typ);
        
        boolean sendSuccess = false;
        
        // Mail an Bewohner
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        this.templateProvider.processOutput("mail_reservation.ftl", model, out1);
        try {
            String msg = out1.toString("UTF-8");
            sendSuccess = sendMail(AppRes.getString("mail.subject.reservation"), msg, mailto);
        }
        catch (UnsupportedEncodingException e) {
            LOG.warn(e.getMessage(), e);
            return false;
        }
         
        // Mail an Verwaltung
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        this.templateProvider.processOutput("mail_reservation_notification.ftl", model, out2);
        try {
            String msg = out2.toString("UTF-8");
            sendMail(AppRes.getString("mail.subject.reservation.notification"), msg, this.config.getMailVerwaltung());
        }
        catch (UnsupportedEncodingException e) {
            LOG.warn(e.getMessage(), e);
        }
        
        return sendSuccess;
    }
    
    @Override
    public boolean sendRolloverMail(ReservationStellplatz reservation, double kosten, String typ, String mailto) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("reservation", reservation);
        model.put("kosten", kosten);
        model.put("typ", typ);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.templateProvider.processOutput("mail_rollover.ftl", model, out);
        try {
            String msg = out.toString("UTF-8");
            return sendMail(AppRes.getString("mail.subject.rollover"), msg, mailto);
        }
        catch (UnsupportedEncodingException e) {
            LOG.warn(e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public void sendReminderMail(ReservationStellplatz reservation, double kosten, String typ, String mailto) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("reservation", reservation);
        model.put("kosten", kosten);
        model.put("typ", typ);
        
        sendMailWithConfirmation(
                "mail_reminder.ftl",
                model,
                AppRes.getString("mail.subject.reminder"),
                mailto);
    }
    
    @Override
    public void sendVeloGruppeMail(ReservationStellplatz reservation, String typ) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("reservation", reservation);
        model.put("typ", typ);
        
        sendMailWithConfirmation(
                "mail_velogruppe.ftl",
                model,
                AppRes.getString("mail.subject.velogruppe"),
                this.config.getMailVelogruppe());
    }
    
    @Override
    public boolean sendPersonenSyncMail(long anzahlNichtAktualisiert) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("anzahl", String.valueOf(anzahlNichtAktualisiert));
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.templateProvider.processOutput("mail_personen_sync.ftl", model, out);
        try {
            String msg = out.toString("UTF-8");
            return sendMail(AppRes.getString("mail.subject.personen.sync"), msg, this.config.getMailVerwaltung());
        }
        catch (UnsupportedEncodingException e) {
            LOG.warn(e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public void sendZahlungErhaltenMail(ReservationStellplatz reservation, double kosten, String typ, String mailto) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("reservation", reservation);
        model.put("kosten", kosten);
        model.put("typ", typ);
        
        sendMailWithConfirmation(
                "mail_zahlung_erhalten.ftl",
                model,
                AppRes.getString("mail.subject.zahlung.erhalten"),
                mailto);
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
    
    private void sendMailWithConfirmation(String template, Map<String, Object> model, final String subject, final String mailto) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.templateProvider.processOutput(template, model, out);
        try {
            String msg = out.toString("UTF-8");
            final MailWindow mailWindow = new MailWindow(mailto, subject, msg);
            Decision decision = new Decision() {
                @Override
                public void yes() {
                    boolean success = sendMail(subject, mailWindow.getText().getValue(), mailto);
                    if (success) {
                        Notification.show(AppRes.getString("notification.caption.mail.success"), 
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                    else {
                        Notification.show(AppRes.getString("notification.caption.mail.failed"), 
                                Notification.Type.ERROR_MESSAGE);
                    }
                }

                @Override
                public void no() {
                }
            };
            mailWindow.setDecision(decision);
            UI.getCurrent().addWindow(mailWindow);
        }
        catch (UnsupportedEncodingException e) {
            LOG.warn(e.getMessage(), e);
        }
    }
    
    private Email createEmail() throws EmailException, AddressException {
        Email email = new SimpleEmail();
        email.setHostName(this.config.getMailHost());
        email.setSmtpPort(this.config.getMailPort());
        email.setAuthenticator(new DefaultAuthenticator(this.config.getMailUser(), this.config.getMailPasswort()));
        email.setSSLOnConnect(this.config.isMailUseSsl());
        email.setFrom(this.config.getMailSender());
        Collection<InternetAddress> replyTo = new ArrayList<InternetAddress>();
        replyTo.add(new InternetAddress(this.config.getMailVerwaltung()));
        email.setReplyTo(replyTo);
        return email;
    }
    
}
