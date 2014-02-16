package ch.giesserei.page.reservation;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.core.MailProvider;
import ch.giesserei.core.TemplateProvider;
import ch.giesserei.injection.Injection;
import ch.giesserei.model.ReservationStatus;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.model.Stellplatz;
import ch.giesserei.model.StellplatzTyp;
import ch.giesserei.resource.AppRes;
import ch.giesserei.resource.ValMsg;
import ch.giesserei.service.MietobjektService;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.service.StellplatzService;
import ch.giesserei.util.Utility;

import com.google.inject.Inject;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.EmailValidator;

/**
 * Dieses Servlet steuert die Seite, über die die BewohnerInnen einen Velo-Stellplatz reservieren können.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
@WebServlet(value = "/reservation", asyncSupported = false)
public class ReservationServlet extends HttpServlet {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReservationServlet.class);

    private static final String PAGE_RESERVATION = "reservation.ftl";
    
    @Inject
    private TemplateProvider templateProvider;
    
    @Inject
    private MailProvider mailProvider;
    
    private final StellplatzService stellplatzService = ServiceLocator.getStellplatzService();
    
    private final ReservationService reservationService = ServiceLocator.getReservationService();
    
    private final MietobjektService mietobjektService = ServiceLocator.getObjektService();
    
    public ReservationServlet() {
        Injection.injectMembers(this);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processPost(req, resp);
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
    
    private void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typ = Utility.getParamValue(req.getParameterMap(), "typ");
        String nummer = Utility.getParamValue(req.getParameterMap(), ReservationBean.PARAM_NUMMER);
        
        ReservationBean bean;
        if (StringUtils.isNotBlank(typ) && StringUtils.isNotBlank(nummer)) {
            typ = StellplatzTyp.PEDALPARC_TIEF.toString().equals(typ) ? StellplatzTyp.PEDALPARC_HOCH.toString() : typ;
            bean = new ReservationBean(true, nummer, typ);
        }
        else {
            bean = new ReservationBean(true);
        }
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("model", bean);
        
        resp.setContentType("text/html; charset=UTF-8");
        this.templateProvider.processOutput(PAGE_RESERVATION, model, resp.getOutputStream());
    }
    
    /**
     * Post-Request verarbeiten und Reservierung durchführen.
     */
    private void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Fomular sendet Daten als UTF-8
        req.setCharacterEncoding("UTF-8");
        
        ReservationBean bean = new ReservationBean();
        bean.setParamValues(req.getParameterMap());
        
        if (validate(bean)) {
            if (saveReservation(bean)) {
                // Mail versenden
                boolean sendMail = sendMail(bean);
                
                // Bean zurücksetzen
                bean = new ReservationBean();
                bean.setSuccess(true);
                bean.setMailSend(sendMail);
            }
        }
        else {
            bean.setSuccess(false);
        }
        
        LOG.info("Bean before output: " + bean.toString());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("model", bean);
        
        resp.setContentType("text/html; charset=UTF-8");
        this.templateProvider.processOutput(PAGE_RESERVATION, model, resp.getOutputStream());
    }
    
    private boolean sendMail(ReservationBean bean) {
        return this.mailProvider.sendReservationMail(
                bean.getName(),
                Integer.parseInt(bean.getWohnung()), 
                Integer.parseInt(bean.getNummer()), 
                bean.getBeginnDate(), 
                Utility.addDay(bean.getEndeDate(), -1),
                this.reservationService.getKostenProPeriode(bean.getStellplatzTyp()), 
                AppRes.getString(bean.getStellplatzTyp().getResourceKey()), 
                bean.getEmail());
    }
    
    /**
     * Daten sind in Ordnung -> Reservierung speichern.
     */
    private boolean saveReservation(ReservationBean bean) {
        ReservationStellplatz reservation = new ReservationStellplatz();
        reservation.setReservationDatum(new Date());
        reservation.setBeginnDatum(bean.getBeginnDate());
        reservation.setEndDatumExklusiv(bean.getEndeDate());
        reservation.setKostenProMonat(this.reservationService.getKostenProMonat(bean.getStellplatzTyp()));
        reservation.setReservationStatus(ReservationStatus.RESERVIERT);
        reservation.setAnonym(true);
        reservation.setBemerkung(Utility.cropString(bean.getBemerkung(), ReservationStellplatz.LENGTH_BEMERKUNG));
        reservation.setWohnungNr(Integer.parseInt(bean.getWohnung()));
        reservation.setStellplatz(bean.getStellplatz());
        
        try {
            this.reservationService.persist(reservation);
            return true;
        }
        catch (Exception e) {
            LOG.error("Fehler beim Speichern: ", e);
            bean.setReservationSaveError(true);
            return false;
        }
    }
    
    // ---------------------------------------------------------
    // Validierung
    // ---------------------------------------------------------
    
    private boolean validate(ReservationBean bean) {
        LOG.info("Bean before validate: " + bean.toString());
        if (StringUtils.isBlank(bean.getTyp())) {
            bean.setValidationError(ValMsg.getString("reservation.typ.not.null"));
            bean.setErrorField(ReservationBean.PARAM_TYP);
        }
        else if (StringUtils.isBlank(bean.getName())) {
            bean.setValidationError(ValMsg.getString("reservation.name.not.null"));
            bean.setErrorField(ReservationBean.PARAM_NAME);
        }
        else if (StringUtils.isBlank(bean.getBeginn())) {
            bean.setValidationError(ValMsg.getString("reservation.beginn.not.null"));
            bean.setErrorField(ReservationBean.PARAM_BEGINN);
        }
        else if (StringUtils.isBlank(bean.getEmail())) {
            bean.setValidationError(ValMsg.getString("reservation.email.not.null"));
            bean.setErrorField(ReservationBean.PARAM_EMAIL);
        }
        else if (StringUtils.isBlank(bean.getNummer())) {
            bean.setValidationError(ValMsg.getString("reservation.nummer.not.null"));
            bean.setErrorField(ReservationBean.PARAM_NUMMER);
        }
        else if (StringUtils.isBlank(bean.getWohnung())) {
            bean.setValidationError(ValMsg.getString("reservation.wohnung.not.null"));
            bean.setErrorField(ReservationBean.PARAM_WOHNUNG);
        }
        
        if (bean.getValidationError() == null) {
            validateExtented(bean);
        }
        
        return bean.getValidationError() == null;
    }
    
    private void validateExtented(ReservationBean bean) {
        boolean valid = validateEmail(bean);
        if (valid) {
            valid = validateStellplatzNummer(bean);
        }
        if (valid) {
            valid = validateWohnungnummer(bean);
        }
        if (valid) {
            valid = validateDateFormat(bean.getBeginn(), bean, "reservation.beginn.format");
        }
        if (valid) {
            valid = validateZeitraum(bean);
        }
    }
    
    private boolean validateEmail(ReservationBean bean) {
        EmailValidator validator = new EmailValidator("");
        try {
            validator.validate(bean.getEmail());
            return true;
        }
        catch (InvalidValueException e) {
            bean.setValidationError(ValMsg.getString("reservation.email.format"));
            bean.setErrorField(ReservationBean.PARAM_EMAIL);
            return false;
        }
    }
    
    private boolean validateStellplatzNummer(ReservationBean bean) {
        if (StringUtils.isNumeric(bean.getNummer())) {
            int nummer = Integer.parseInt(bean.getNummer());
            Stellplatz stellplatz = this.stellplatzService.getStellplatz(nummer, bean.getStellplatzTyp());
            if (stellplatz == null) {
                bean.setValidationError(ValMsg.getString("reservation.nummer.ungueltig"));
                bean.setErrorField(ReservationBean.PARAM_NUMMER);
                return false;
            }
            else {
                bean.setStellplatz(stellplatz);
            }
        }
        else {
            bean.setValidationError(ValMsg.getString("reservation.nummer.format"));
            bean.setErrorField(ReservationBean.PARAM_NUMMER);
            return false;
        }
        return true;
    }
    
    private boolean validateWohnungnummer(ReservationBean bean) {
        if (StringUtils.isNumeric(bean.getWohnung())) {
            int nummer = Integer.parseInt(bean.getWohnung());
            if (!this.mietobjektService.existObjekt(nummer)) {                 
                bean.setValidationError(ValMsg.getString("reservation.wohnung.ungueltig"));
                bean.setErrorField(ReservationBean.PARAM_WOHNUNG);
                return false;
            }
        }
        else {
            bean.setValidationError(ValMsg.getString("reservation.wohnung.format"));
            bean.setErrorField(ReservationBean.PARAM_WOHNUNG);
            return false;
        }
        return true;
    }
    
    private boolean validateDateFormat(String value, ReservationBean bean, String errorKey) {
        try {
            bean.getDateFormat().parse(value);
        }
        catch (ParseException e) {
            bean.setValidationError(ValMsg.getString(errorKey));
            bean.setErrorField(ReservationBean.PARAM_BEGINN);
            return false;
        }
        return true;
    }
    
    private boolean validateZeitraum(ReservationBean bean) {
        Date beginn = bean.getBeginnDate();
        Calendar calBeginn = new GregorianCalendar();
        calBeginn.setTime(beginn);
        if (calBeginn.get(Calendar.DAY_OF_MONTH) != 1) {
            bean.setValidationError(ValMsg.getString("reservation.beginn.tag"));
            bean.setErrorField(ReservationBean.PARAM_BEGINN);
            return false;
        }
                
        // automatisch Ende ein Jahr später
        bean.setEnde(bean.getDateFormat().format(Utility.addYear(beginn)));
        Date ende = bean.getEndeDate();

        ReservationStellplatz reservation = new ReservationStellplatz();
        reservation.setBeginnDatum(beginn);
        reservation.setEndDatumExklusiv(ende);
        reservation.setStellplatz(bean.getStellplatz());
        reservation.setAnonym(true);
        
        if (!this.reservationService.isStellplatzFrei(reservation)) {
            bean.setValidationError(ValMsg.getString("reservation.zeitraum"));
            bean.setErrorField(ReservationBean.PARAM_BEGINN);
            return false;
        }

        return true;
    }
}
