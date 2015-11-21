package ch.giesserei.page.stellplatzliste;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.giesserei.core.TemplateProvider;
import ch.giesserei.injection.Injection;
import ch.giesserei.service.ReservationService;
import ch.giesserei.service.ServiceLocator;
import ch.giesserei.service.model.StellplatzStatus;

import com.google.inject.Inject;

/**
 * Dieses Servlet bereits die Liste mit den Stellplätzen auf.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
@WebServlet(value = "/liste", asyncSupported = false)
public class StellplatzlisteServlet extends HttpServlet {
    
    private static final String PAGE_LISTE = "stellplatzliste.ftl";
    
    @Inject
    private TemplateProvider templateProvider;
    
    private final ReservationService reservationService = ServiceLocator.getReservationService();
    
    public StellplatzlisteServlet() {
        Injection.injectMembers(this);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        
        StellplatzlisteBean bean = new StellplatzlisteBean();
        for (int i = 1; i <= 6; i++) {
            List<StellplatzStatus> statusListe = this.reservationService.getStellplatzStatus(i);
            bean.addSektor(statusListe);
        }
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("model", bean);
        this.templateProvider.processOutput(PAGE_LISTE, model, resp.getOutputStream());
    }

}
