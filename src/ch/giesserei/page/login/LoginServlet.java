package ch.giesserei.page.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.giesserei.core.TemplateProvider;
import ch.giesserei.core.WebUtils;
import ch.giesserei.injection.Injection;
import ch.giesserei.resource.ValMsg;

import com.google.inject.Inject;

/**
 * Dieses Servlet steuert den Login-Dialog.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
@WebServlet(value = "/login", asyncSupported = false)
public class LoginServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);

    private static final String PAGE_LOGIN = "login.ftl";
    
    @Inject
    private TemplateProvider templateProvider;
    
    public LoginServlet() {
        Injection.injectMembers(this);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("begin doGet");
        resp.setContentType("text/html; charset=UTF-8");
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("model", new LoginBean());
        this.templateProvider.processOutput(PAGE_LOGIN, model, resp.getOutputStream());
        LOG.info("end doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Fomular sendet Daten als UTF-8
        req.setCharacterEncoding("UTF-8");
        processCommit(req, resp);
    }
    
    /**
     * Post-Request verarbeiten und Login durchführen.
     */
    private void processCommit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOG.info("begin processCommit");
        LoginBean bean = new LoginBean();
        bean.setParamValues(req.getParameterMap());
        Map<String, Object> model = new HashMap<String, Object>();
        
        if (validate(bean)) {
            if (login(resp, bean)) {
                return;
            }
        }

        model.put("model", bean);
        this.templateProvider.processOutput(PAGE_LOGIN, model, resp.getOutputStream());
        LOG.info("end processCommit");
    }
    
    private boolean validate(LoginBean bean) {
        LOG.info("begin validate");
        if (StringUtils.isBlank(bean.getUserName())) {
            bean.setValidationError(ValMsg.getString("login.user.not.null"));
            return false;
        }
        else if (StringUtils.isBlank(bean.getUserPassword())) {
            bean.setValidationError(ValMsg.getString("login.password.not.null"));
            return false;
        }
        LOG.info("end validate");
        return true;
    }
    
    private boolean login(HttpServletResponse resp, LoginBean bean) throws IOException {
        LOG.info("begin login");
        Subject currentUser = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken(bean.getUserName(), bean.getUserPassword());
        try {
            LOG.info("login ...");
            currentUser.login(token);
            LOG.info("user logged in");
            resp.sendRedirect(WebUtils.getPathDesktop());
            return true;
        } catch (AuthenticationException ae) {
            LOG.error(ae.getMessage());
            bean.setValidationError(ValMsg.getString("login.failed"));
            return false;
        }
    }
}
