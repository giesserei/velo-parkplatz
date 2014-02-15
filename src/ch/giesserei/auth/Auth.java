package ch.giesserei.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Wrapper für das Autorisierungs-Framework Shiro.
 * 
 * @author Steffen Förster
 */
public class Auth {

    /**
     * User abmelden.
     */
    public static void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }
    
    /**
     * @see {@link Subject#isPermitted(String)}
     */
    public static boolean isPermitted(String permission) {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isPermitted(permission);
    }
    
    /**
     * {@link Subject#hasRole(String)}
     */
    public static boolean hasRole(String role) {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.hasRole(role);
    }
    
}
