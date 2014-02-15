package ch.giesserei.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Dieser MailProvider schreibt die Mails nur in das Log-File.
 * 
 * @author Steffen Förster
 */
@Singleton
public class LocalMailProvider extends MailProviderImpl {

    private static final Logger LOG = LoggerFactory.getLogger(LocalMailProvider.class);
    
    @Inject
    public LocalMailProvider(Config config, TemplateProvider templateProvider) {
        super(config, templateProvider);
    }
    
    /**
     * Schreibt die Mail in das Log-File.
     * @param subject Betreff
     * @param message Text
     * @param mailto Empfänger der Mail
     * @return immer true
     */
    public boolean sendMail(String subject, String message, String mailto) {
        LOG.info("mailto: " + mailto);
        LOG.info("subject: " + subject);
        LOG.info("message: " + message);
        return true;
    }
    
}
