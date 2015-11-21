package ch.giesserei;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.app.Context;
import ch.giesserei.app.gui.GuiBuilder;
import ch.giesserei.core.NestedException;
import ch.giesserei.core.SessionContainer;
import ch.giesserei.resource.AppRes;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * Vaadin Root Application-Component.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
@Theme("giesserei")
//@Theme("reindeer")
public class GiessereiUI extends UI {
	
	private static final Logger LOG = LoggerFactory.getLogger(GiessereiUI.class);
	
	@WebServlet(value = {"/desktop/*", "/VAADIN/*"}, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = GiessereiUI.class)
	public static class Servlet extends VaadinServlet {
	}

	public GiessereiUI() {
	}
	
	@Override
	protected void init(VaadinRequest request) {
		LOG.info("init vaadin ...");
		
		GuiBuilder builder = new GuiBuilder();
		Context context = builder.createGui();
		setContent(context.getMainComponent());
		
		initErrorHandler();
		initSessionContainer();
		
		// Testdaten speichern
		//LOG.info("init test data ...");
		//TestDaten testDaten = new TestDaten();
		//testDaten.initTestDaten();
	}
	
	private void initSessionContainer() {
	    SessionContainer container = new SessionContainer();
	    getSession().setAttribute(SessionContainer.KEY, container);
	}

	/**
	 * Bei einem nicht abgefangenen Fehler wird dem Benutzer eine Meldung angezeigt 
	 * und der Fehler ins LOG-File geschrieben.
	 */
	private void initErrorHandler() {
	    setErrorHandler(new DefaultErrorHandler() {
	        @Override
	        public void error(com.vaadin.server.ErrorEvent event) {
	            Throwable throwable = event.getThrowable();
	            
	            // ursprüngliche Exception holen
	            if (throwable instanceof NestedException) {
	                throwable = throwable.getCause();
	            }
	            
	            LOG.error(throwable.getMessage(), throwable);
	            if (throwable instanceof NestedException) {
	                throwable = throwable.getCause();
	            }
	            
	            // 1. Logging -> Mail an Support
	            LOG.error(throwable.getMessage(), throwable);
	            
	            // 2. Meldung an Benutzer
	            Notification.show(
	                    AppRes.getString("notification.caption.app.error"),
	                    Notification.Type.ERROR_MESSAGE);
	        } 
	    });
	}
}