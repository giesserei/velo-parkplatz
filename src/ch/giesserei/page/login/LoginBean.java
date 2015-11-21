package ch.giesserei.page.login;

import java.util.Map;

import ch.giesserei.util.Utility;

/**
 * Bean für das Login-Form.
 * 
 * @author Steffen Förster
 */
public class LoginBean {

    public static final String PARAM_USER_NAME = "userName";
    
    public static final String PARAM_USER_PASSWORD = "userPassword";
    
    private String validationError;
    
    private String userName;
    
    private String userPassword;
    
    public LoginBean() {
    }
    
    @SuppressWarnings("rawtypes")
    public void setParamValues(Map parameterMap) {
        this.userName = Utility.getParamValue(parameterMap, PARAM_USER_NAME);
        this.userPassword = Utility.getParamValue(parameterMap, PARAM_USER_PASSWORD);
    }

    public String getValidationError() {
        return validationError;
    }

    public void setValidationError(String validationError) {
        this.validationError = validationError;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    
}
