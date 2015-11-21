package ch.giesserei.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.SocketException;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

/**
 * Diese verabeitet FreeMarker-Templates (http://freemarker.org) zu einem Output.
 * 
 * @author Steffen Förster
 */
@Singleton
public class FreeMarkerProvider implements TemplateProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FreeMarkerProvider.class);
    
    private final Configuration cfg;
    
    /**
     * Konstruktor.
     */
    @Inject
    public FreeMarkerProvider(ServletContext context, Config config) {
        this.cfg = new Configuration();
        initializeConfig(context, config);
    }
    
    /**
     * Verarbeitet das übergebene Template zu einem Output. Der Output wird in den übergebenen
     * Stream geschrieben.
     * 
     * @param templateName Name des Templates
     * @param model Modell
     * @param out Ziel des Outputs
     */
    @Override
    public void processOutput(String templateName, Object model, OutputStream out) {
        try {
            Template temp = this.cfg.getTemplate(templateName);

            Writer writer = new OutputStreamWriter(out, "UTF-8");
            temp.process(model, writer);
        }
        catch (SocketException e) {
            // ignore -> Client hat Verbindung abgebrochen
            LOG.debug(e.getMessage(), e);
        }
        catch (TemplateException e) {
            throw NestedException.wrap(e);
        }
        catch (IOException e) {
            // ignore -> Client hat Verbindung abgebrochen
            LOG.debug(e.getMessage(), e);
        }
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
    
    private void initializeConfig(ServletContext context, Config config) {
        this.cfg.setTemplateLoader(new WebappTemplateLoader(context, "/ftl"));

        this.cfg.setObjectWrapper(new DefaultObjectWrapper());
        this.cfg.setDefaultEncoding("UTF-8");
        this.cfg.setLocale(config.getAppLocale());

        // Sets how errors will appear. Here we assume we are developing HTML pages.
        // For production systems TemplateExceptionHandler.RETHROW_HANDLER is better.
        this.cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // At least in new projects, specify that you want the fixes that aren't
        // 100% backward compatible too (these are very low-risk changes as far as the
        // 1st and 2nd version number remains):
        this.cfg.setIncompatibleImprovements(new Version(2, 3, 20));  // FreeMarker 2.3.20
    }
}
