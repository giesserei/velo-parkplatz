package ch.giesserei.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.core.Const;

/**
 * Dieser Filter stellt für jeden Request-Thread einen Entity-Manager bereit.
 * Der Filter schliesst auch den Entity-Manager und beendet vorher die offene
 * Transaktion.
 *
 * @author Steffen Förster
 */
@WebFilter(value={"/desktop/*", "/liste/*", "/reservation/*"})
public class JpaFilter implements Filter {

    /**
     * Die DB-Properties einschliesslich User&Passwort liegen in einer Properties-Datei. 
     * So können diese Daten einfach aus der Source-Verwaltung ausgeschlossen werden.
     */
    private static final String PATH_PERSISTENCE_PROPERTIES = Const.PATH_ETC + "/persistence.properties";

    private static final Logger LOG = LoggerFactory.getLogger(JpaFilter.class);
    
    private static final ThreadLocal<EntityManager> EM_HOLDER = new ThreadLocal<EntityManager>();

    /**
     * EntityManagerFactory ist threadsafe.
     */
    private static EntityManagerFactory emf;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            String jpaName = filterConfig.getServletContext().getInitParameter("PERSISTENCE_UNIT_NAME");
            Properties props = new Properties();
            props.load(new FileInputStream(new File(PATH_PERSISTENCE_PROPERTIES)));
            emf = Persistence.createEntityManagerFactory(jpaName, props);
        }
        catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
        emf.close();
        emf = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        EntityManager em = emf.createEntityManager();
        LOG.debug("EntityManager has been created");
        setEntityManager(em);
        try {
            chain.doFilter(request, response);
        }
        finally {
            // In jedem Fall den Entity-Manager schliessen und vom ThreadLocal entfernen
            LOG.debug("EntityManager has been closed");
            em.close();
            EM_HOLDER.remove();
        }
    }

    public static EntityManager getEntityManager() {
        return EM_HOLDER.get();
    }
    
    // ---------------------------------------------------------
    // Methoden nur für Worker-Threads
    // ---------------------------------------------------------
    
    /**
     * Sofern ein Worker-Thread gebaut wird, muss der EntityManager "von Hand" gesetzt werden.
     */
    public static void setEntityManager() {
        EntityManager em = emf.createEntityManager();
        EM_HOLDER.set(em);
    }
    
    /**
     * EntityManager eines Worker-Threads wieder schliessen.
     */
    public static void closeEntityManager() {
        EM_HOLDER.get().close();
        EM_HOLDER.remove();
    }
    
    // ---------------------------------------------------------
    // private section
    // ---------------------------------------------------------
    
    /**
     * Speichert einen neuen Entity-Manager im ThreadLocal. Sollte für den 
     * Thread noch ein EntityManager vorhanden sein, so wird dieser geschlossen. Hier wurde 
     * beim Beenden eines Requests der EntityManager nicht geschlossen.
     */
    private void setEntityManager(EntityManager em) {
        if (EM_HOLDER.get() != null) {
            LOG.warn("entity manager not closed");
            EM_HOLDER.get().close();
            EM_HOLDER.remove();
        }
        EM_HOLDER.set(em);
    } 
}