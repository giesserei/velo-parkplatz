package ch.giesserei.page.error;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.core.NestedException;
import ch.giesserei.core.TemplateProvider;
import ch.giesserei.injection.Injection;

import com.google.inject.Inject;

/**
 * Zentraler Exception-Handler für die Applikation.
 * 
 * @author Steffen Förster
 */
@WebServlet("/error_handler")
public class AppExceptionHandler extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private static final String PAGE_ERROR = "error.ftl";

    private static final Logger LOG = LoggerFactory.getLogger(AppExceptionHandler.class);
    
    @Inject
    private TemplateProvider templateProvider;
    
    public AppExceptionHandler() {
        Injection.injectMembers(this);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processError(request, response);
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processError(request, response);
    }
 
    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (throwable == null) {
            LOG.warn("manueller Aufruf der Seite /error");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }
         
        // ursprüngliche Exception holen
        if (throwable instanceof NestedException) {
            throwable = throwable.getCause();
        }
        
        // 1. Logging -> Mail an Support
        LOG.error(throwable.getMessage() + ", requestUri: " + requestUri, throwable);
        
        // 2. Fehlerseite anzeigen
        response.setContentType("text/html; charset=UTF-8");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("model", new HashMap<String, Object>());
        this.templateProvider.processOutput(PAGE_ERROR, model, response.getOutputStream());
    }
}