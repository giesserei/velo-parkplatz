package ch.giesserei.core;

import java.io.OutputStream;

/**
 * Für einige Web-Seiten und die E-Mails werden Templates verwendet. 
 * Die Verarbeitung der Templates wird durch diesen Provider durchgeführt.
 * 
 * @author Steffen Förster
 */
public interface TemplateProvider {

    /**
     * Verarbeitet das übergebene Template zu einem Output. Der Output wird in den übergebenen
     * Stream geschrieben.
     * 
     * @param templateName Name des Templates
     * @param model Modell
     * @param out Ziel des Outputs
     */
    void processOutput(String templateName, Object model, OutputStream out);
    
}
