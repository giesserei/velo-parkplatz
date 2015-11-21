package ch.giesserei.service;

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import ch.giesserei.util.Utility;

import com.google.inject.Provider;

/**
 * Basisklasse für die Services.
 * 
 * @author Steffen Förster
 */
public class BaseService {

    private final MessageInterpolator interpolator;
    
    private final Provider<EntityManager> entityManager;
    
    protected BaseService(MessageInterpolator interpolator, Provider<EntityManager> entityManager) {
    	this.interpolator = interpolator;
    	this.entityManager = entityManager;
    }
 
    /**
     * Liefert einen neuen EntityManager.
     * 
     * @return siehe Beschreibung
     */
    protected EntityManager getEntityManager() {
        return this.entityManager.get();
    }
    
    protected Validator getValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.usingContext().messageInterpolator(this.interpolator).getValidator();
    }
    
    protected void validate(Object entity) {
        Set<ConstraintViolation<Object>> violations = getValidator().validate(entity);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.iterator().next().getMessage());
        }
    }
    
    protected String getParameterListString(int count, int startIndex) {
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < count; i++) {
    		if (sb.length() > 0) {
    			sb.append(", ");
    		}
    		sb.append("?" + (startIndex + i));
    	}
    	return sb.toString();
    }
    
    protected String getParameterListStringNative(int count, int startIndex) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        return sb.toString();
    }
    
    protected Date trimDate(Date date) {
        return Utility.stripTime(date);
    }
	
}
