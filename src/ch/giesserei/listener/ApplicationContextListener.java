package ch.giesserei.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.core.Config;
import ch.giesserei.core.Const;
import ch.giesserei.core.WebUtils;
import ch.giesserei.injection.GuiModule;
import ch.giesserei.injection.CoreModule;
import ch.giesserei.injection.FreeMarkerModule;
import ch.giesserei.injection.Injection;
import ch.giesserei.injection.ResourceModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Dieser Listener initialisiert die Applikation.
 * 
 * @author Steffen Förster
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContextListener.class);
    
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOG.info("preparing logging ...");
        PropertyConfigurator.configure(Const.PATH_ETC + "/log4j.properties");
        LOG.info("logging is ready to use");
        
        initInjection(event.getServletContext());
        WebUtils.setContext(event.getServletContext());
        initConfig(event.getServletContext());
    }

    private void initInjection(ServletContext context) {
        LOG.info("preparing injection ...");
        Injector injector = Guice.createInjector(
                new CoreModule(),
                new ResourceModule(),
                new FreeMarkerModule(context),
                new GuiModule());

        Injection.setInjector(injector);
        LOG.info("injection is ready to use");
    }
    
    private void initConfig(ServletContext context) {
        Injection.get(Config.class).initialize(context);
    }
    
}
