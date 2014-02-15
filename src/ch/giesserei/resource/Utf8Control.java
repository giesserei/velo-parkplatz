package ch.giesserei.resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Standardmässig wird ein Property-File für ein ResourceBundle immer im Encoding ISO-8859-1 eingelesen. für die
 * einfachere Unterstützung der anderen Sprachen speichern wir die Property-Files aber im UTF-8 Format ab. Seit dem JDK
 * 6 kann die Klasse <java.util.Properties> nun auch Dateien mit einem anderen Encoding einlesen. Diese Klasse wird
 * benötigt, damit die Dateien mit dem Encoding UTF-8 eingelesen werden.
 * 
 * @author Steffen Förster
 */
public class Utf8Control extends ResourceBundle.Control {
    
    /**
     * Liefert Locale.ROOT als Default.
     * <p/>
     * {@inheritDoc}
     */
    public Locale getFallbackLocale(String baseName, Locale locale) {
        return Locale.ROOT;
    }
    
    @Override
    public List<String> getFormats(String baseName) {
        return FORMAT_PROPERTIES;
    }
    
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, 
    		ClassLoader loader, boolean reload)
        throws IllegalAccessException, InstantiationException, IOException {
        
        if (baseName == null || locale == null || format == null || loader == null) {
            throw new NullPointerException();
        }
        
        ResourceBundle bundle = null;
        if ("java.properties".equals(format)) {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            InputStream stream = null;
            
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            
            if (stream != null) {
                BufferedInputStream bis = new BufferedInputStream(stream);
                bundle = new Utf8PropertiesResourceBundle(bis);
                bis.close();
            }
        }
        
        return bundle;
    }

    /**
     * Erweitert die Klasse ResourceBundle, damit Dateien im UTF-8 Encoding 
     * eingelesen werden können.
     * 
     * @author Steffen Förster
     */
    private static class Utf8PropertiesResourceBundle extends ResourceBundle {
        private Properties props;
        
        Utf8PropertiesResourceBundle(InputStream stream) throws IOException {
            // Hier wird ein spezieller Reader gebaut, der die Property-Datei
            // mit dem UTF-8 Encoding einliest  
            InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            props = new Properties();
            props.load(reader);
        }
        
        protected Object handleGetObject(String key) {
            return props.getProperty(key);
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Enumeration getKeys() {
            return props.keys();
        }
    }
}