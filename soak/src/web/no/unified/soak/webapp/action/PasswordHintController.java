/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.webapp.action;

import no.unified.soak.model.User;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.UserManager;
import no.unified.soak.webapp.util.RequestUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Simple class to retrieve and send a password hint to users.
 *
 * <p>
 * <a href="PasswordHintController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class PasswordHintController implements Controller {
    private transient final Log log = LogFactory.getLog(PasswordHintController.class);
    private UserManager mgr = null;
    private MessageSource messageSource = null;
    protected MailEngine mailEngine = null;
    protected SimpleMailMessage message = null;

    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        String username = request.getParameter("username");
        MessageSourceAccessor text = new MessageSourceAccessor(messageSource,
                request.getLocale());

        // ensure that the username has been sent
        if (username == null) {
            log.warn(
                "Username not specified, notifying user that it's a required field.");

            request.setAttribute("error",
                text.getMessage("errors.required",
                    new Object[] { text.getMessage("user.username") }));

            return new ModelAndView("login");
        }

        if (log.isDebugEnabled()) {
            log.debug("Processing Password Hint...");
        }

        // look up the user's information
        try {
            User user = (User) mgr.getUser(username);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: " + user.getPasswordHint());
            msg.append("\n\nLogin at: " + RequestUtil.getAppURL(request));

            message.setTo(user.getEmail());

            String subject = text.getMessage("webapp.prefix") +
                text.getMessage("user.passwordHint");
            message.setSubject(subject);
            message.setText(msg.toString());
            mailEngine.send(message);

            saveMessage(request,
                text.getMessage("login.passwordHint.sent",
                    new Object[] { username, user.getEmail() }));
        } catch (Exception e) {
            saveErrorMessage(request,
                text.getMessage("login.passwordHint.error",
                    new Object[] { username }));
        }

        return new ModelAndView(new RedirectView(request.getContextPath()));
    }

    public void saveErrorMessage(HttpServletRequest request, String error) {
        List errors = (List) request.getSession().getAttribute("listOfErrorMessages");

        if (errors == null) {
            errors = new ArrayList();
        }

        errors.add(error);
        request.getSession().setAttribute("listOfErrorMessages", errors);
    }

    // this method is also in BaseForm Controller
    public void saveMessage(HttpServletRequest request, String msg) {
        List messages = (List) request.getSession().getAttribute("listOfMessages");

        if (messages == null) {
            messages = new ArrayList();
        }

        messages.add(msg);
        request.getSession().setAttribute("listOfMessages", messages);
    }
}
