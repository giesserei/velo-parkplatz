package ch.giesserei.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.app.gui.MailWindow;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.ui.UI;

/**
 * Dieser MailProvider zeigt die Mail in einem Fenster an.
 * 
 * @author Steffen Förster
 */
@Singleton
public class WindowMailProvider extends MailProviderImpl {

    private static final Logger LOG = LoggerFactory.getLogger(WindowMailProvider.class);
    
    @Inject
    public WindowMailProvider(Config config, TemplateProvider templateProvider) {
        super(config, templateProvider);
    }
    
    /**
     * Zeigt die Mail in einem Fenster an.
     * @param subject Betreff
     * @param message Text
     * @param mailto Empfänger der Mail
     * @return immer true
     */
    public boolean sendMail(String subject, String message, String mailto) {
        MailWindow mailWindow = new MailWindow(mailto, subject, message);
        
        if (UI.getCurrent() != null) {
            UI.getCurrent().addWindow(mailWindow);
        }
        
        LOG.info("mailto: " + mailto);
        LOG.info("subject: " + subject);
        LOG.info("message: " + message);
        
        return true;
    }
    
}
