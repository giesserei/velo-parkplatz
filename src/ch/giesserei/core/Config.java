package ch.giesserei.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Diese Klasse hält die Konfigurationsparameter der Applikation.
 * 
 * @author Steffen Förster
 */
@Singleton
public class Config {
    
    private static final String PATH_MAIL_PROPERTIES = Const.PATH_ETC + "/mail.properties";
    
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);
    
    private static final String APP_LOCALE = "APP_LOCALE";
    
    private static final String IMPORT_PATH = "IMPORT_PATH";
    
    private static final String MAIL_HOST = "mail.host";
    
    private static final String MAIL_PORT = "mail.port";
    
    private static final String MAIL_USE_SSL = "mail.use.ssl";
  
    private static final String MAIL_SENDER = "mail.sender";
  
    private static final String MAIL_USER = "mail.user";
  
    private static final String MAIL_PASSWORT = "mail.password";
    
    private static final String MAIL_VERWALTUNG = "mail.verwaltung";
    
    private static final String MAIL_VELOGRUPPE = "mail.velogruppe";

    private Locale appLocale;
    
    private String mailHost;
    
    private int mailPort;
    
    private boolean mailUseSsl;
    
    private String mailSender;
    
    private String mailUser;
    
    private String mailPasswort;
    
    private String mailVerwaltung;
    
    private String mailVelogruppe;
    
    private String importPath;
    
    @Inject
    public Config() {
    }
    
    public void initialize(ServletContext context) {
        readAppLocale(context);
        this.importPath = context.getInitParameter(IMPORT_PATH);
        initMailProperties();
        
    }
    
    private void initMailProperties() {
        try {
            Properties mailProps = new Properties();
            mailProps.load(new FileInputStream(new File(PATH_MAIL_PROPERTIES)));
            
            this.mailHost = mailProps.getProperty(MAIL_HOST);
            this.mailSender = mailProps.getProperty(MAIL_SENDER);
            this.mailUser = mailProps.getProperty(MAIL_USER);
            this.mailPasswort = mailProps.getProperty(MAIL_PASSWORT);
            this.mailVerwaltung = mailProps.getProperty(MAIL_VERWALTUNG);
            this.mailVelogruppe = mailProps.getProperty(MAIL_VELOGRUPPE);
            
            readMailPort(mailProps);
            readMailUseSsl(mailProps);
        }
        catch (IOException e) {
            throw NestedException.wrap(e);
        }
        
    }
    
    public Locale getAppLocale() {
        return appLocale;
    }

    public String getMailHost() {
        return mailHost;
    }
    
    public int getMailPort() {
        return mailPort;
    }

    public boolean isMailUseSsl() {
        return mailUseSsl;
    }

    public String getMailSender() {
        return mailSender;
    }

    public String getMailUser() {
        return mailUser;
    }

    public String getMailPasswort() {
        return mailPasswort;
    }
    
    public String getMailVerwaltung() {
        return mailVerwaltung;
    }
    
    public String getMailVelogruppe() {
        return mailVelogruppe;
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------

    public String getImportPath() {
        return importPath;
    }

    private void readMailPort(Properties mailProps) {
        this.mailPort = 25;
        try {
            this.mailPort = Integer.parseInt(mailProps.getProperty(MAIL_PORT));
        }
        catch (Exception e) {
            LOG.warn("error while reading mail port");
        }
    }
    
    private void readMailUseSsl(Properties mailProps) {
        this.mailUseSsl = false;
        try {
            this.mailUseSsl = "true".equals(mailProps.getProperty(MAIL_USE_SSL));
        }
        catch (Exception e) {
            LOG.warn("error while reading mail use ssl");
        }
    }
    
    private void readAppLocale(ServletContext context) {
        this.appLocale = new Locale("de", "CH");
        try {
            String localeStr = context.getInitParameter(APP_LOCALE);
            String[] localeParts = localeStr.split("_");
            this.appLocale = new Locale(localeParts[0], localeParts[1]);
            LOG.info("app locale: " + this.appLocale);
        }
        catch (Exception e) {
            LOG.warn("error while reading locale");
        }
    }
}
