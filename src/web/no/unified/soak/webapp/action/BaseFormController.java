/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.webapp.action;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.validation.Email;
import no.unified.soak.validation.MinValue;
import no.unified.soak.validation.Required;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.NoSuchMessageException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;


/**
 * Implementation of <strong>SimpleFormController</strong> that contains
 * convenience methods for subclasses. For example, getting the current user and
 * saving messages/errors. This class is intended to be a base class for all
 * Form controllers.
 *
 * <p>
 * <a href="BaseFormController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseFormController extends SimpleFormController {
    protected final transient Log log = LogFactory.getLog(getClass());
    protected MailEngine mailEngine = null;
    protected SimpleMailMessage message = null;
    protected String templateName = null;
    protected String cancelView;
    private UserManager userManager;
    protected ConfigurationManager configurationManager = null;

    public UserManager getUserManager() {
    	return userManager;
    }

    public void setUserManager(UserManager userManager) {
    	this.userManager = userManager;
    }
    
    public void setConfigurationManager(ConfigurationManager configurationManager) {
    	this.configurationManager = configurationManager;
    }

    public void saveMessage(HttpServletRequest request, String msg) {
        List<String> messages = (List<String>) request.getSession().getAttribute("listOfMessages");

        if (messages == null) {
            messages = new ArrayList<String>();
        }

        messages.add(msg);
        request.getSession().setAttribute("listOfMessages", messages);
    }

    public void saveErrorMessage(HttpServletRequest request, String error) {
        List errors = (List) request.getSession().getAttribute("listOfErrorMessages");

        if (errors == null) {
            errors = new ArrayList();
        }

        errors.add(error);
        request.getSession().setAttribute("listOfErrorMessages", errors);
    }

    /**
     * Convenience method for getting a i18n key's value. Calling
     * getMessageSourceAccessor() is used because the RequestContext variable is
     * not set in unit tests b/c there's no DispatchServlet Request.
     *
     * @param msgKey
     * @param locale
     *            the current locale
     */
	public String getText(String msgKey, Locale locale) {
		String theMessage;
		try {
			theMessage = getMessageSourceAccessor().getMessage(msgKey, locale);
		} catch (NoSuchMessageException e) {
			log.warn("Error getting message for key [" + msgKey + "], using locale [" + locale + "]: " + e);
			theMessage = msgKey;
		}
		return theMessage;
	}

    /**
     * Convenient method for getting a i18n key's value with a single string
     * argument.
     *
     * @param msgKey
     * @param arg
     * @param locale
     *            the current locale
     */
    public String getText(String msgKey, String arg, Locale locale) {
        return getText(msgKey, new Object[] { arg }, locale);
    }

    /**
     * Convenience method for getting a i18n key's value with arguments.
     *
     * @param msgKey
     * @param args
     * @param locale
     *            the current locale
     */
    public String getText(String msgKey, Object[] args, Locale locale) {
        return getMessageSourceAccessor().getMessage(msgKey, args, locale);
    }

    /**
     * Convenience method to get the user object from the session
     *
     * @param request
     *            the current request
     * @return the user's populated object from the session
     */
    protected User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(Constants.USER_KEY);
    }

    /**
     * Convenience method to get the Configuration HashMap from the servlet
     * context.
     *
     * @return the user's populated form from the session
     */
    public Map getConfiguration() {
        Map config = (HashMap) getServletContext().getAttribute(Constants.CONFIG);

        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap();
        }

        return config;
    }

    /**
     * Default behavior for FormControllers - redirect to the successView when
     * the cancel button has been pressed.
     */
    public ModelAndView processFormSubmission(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (request.getParameter("cancel") != null) {
            return new ModelAndView(getCancelView());
        }

        return super.processFormSubmission(request, response, command, errors);
    }

    /**
     * Set up a custom property editor for converting form inputs to real
     * objects
     */
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) {
        String overrideGroupingUsed = null;
        String overrideIntegerDigits = null;
        String overrideDecimalDigits = null;

        // Figure out if number formats are overridden on application level from within web.xml
        HttpSession session = request.getSession(true);

        if (session != null) {
            ServletContext ctx = session.getServletContext();

            if (ctx != null) {
                overrideGroupingUsed = ctx.getInitParameter(Constants.OVERRIDE_GROUPING_KEY);
                overrideIntegerDigits = ctx.getInitParameter(Constants.OVERRIDE_INTEGER_KEY);
                overrideDecimalDigits = ctx.getInitParameter(Constants.OVERRIDE_DECIMAL_KEY);
            }
        }

        NumberFormat intf = NumberFormat.getNumberInstance();
        NumberFormat decf = NumberFormat.getNumberInstance();

        if (overrideGroupingUsed != null) {
            if (overrideGroupingUsed.compareTo("false") == 0) {
                intf.setGroupingUsed(false);
                decf.setGroupingUsed(false);
            }

            if (overrideGroupingUsed.compareTo("true") == 0) {
                intf.setGroupingUsed(true);
                decf.setGroupingUsed(true);
            }
        }

        if (overrideIntegerDigits != null) {
            intf.setMinimumFractionDigits(Integer.parseInt(
                    overrideIntegerDigits));
            intf.setMaximumFractionDigits(Integer.parseInt(
                    overrideIntegerDigits));
        }

        if (overrideDecimalDigits != null) {
            decf.setMinimumFractionDigits(Integer.parseInt(
                    overrideDecimalDigits));
            decf.setMaximumFractionDigits(Integer.parseInt(
                    overrideDecimalDigits));
        }

        binder.registerCustomEditor(Integer.class, null,
            new CustomNumberEditor(Integer.class, intf, true));
        binder.registerCustomEditor(Long.class, null,
            new CustomNumberEditor(Long.class, intf, true));
        binder.registerCustomEditor(Double.class, null,
            new CustomNumberEditor(Double.class, decf, true));
        binder.registerCustomEditor(byte[].class,
            new ByteArrayMultipartFileEditor());

        SimpleDateFormat dateFormat = new SimpleDateFormat(getText(
                    "date.format", request.getLocale()));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null,
            new CustomDateEditor(dateFormat, true));
    }

    /**
     * Convenience message to send messages to users, includes app URL as
     * footer.
     *
     * @param user
     * @param msg
     * @param url
     */
    protected void sendUserMessage(User user, String msg, String url) {
        if (log.isDebugEnabled()) {
            log.debug("sending e-mail to user [" + user.getEmail() + "]...");
        }

        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        Map model = new HashMap();
        model.put("user", user);

        // TODO: once you figure out how to get the global resource bundle in
        // WebWork, then figure it out here too. In the meantime, the Username
        // and Password labels are hard-coded into the template.
        // model.put("bundle", getTexts());
        model.put("message", msg);
        model.put("applicationURL", url);
        mailEngine.sendMessage(message, templateName, model);
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Indicates what view to use when the cancel button has been pressed.
     */
    public final void setCancelView(String cancelView) {
        this.cancelView = cancelView;
    }

    public final String getCancelView() {
        // Default to successView if cancelView is invalid
        if ((this.cancelView == null) || (this.cancelView.length() == 0)) {
            return getSuccessView();
        }

        return this.cancelView;
    }
    
	protected int validateAnnotations(Object obj, BindException errors) {
		int i = 0;
		for (Method method : obj.getClass().getMethods()) {
			if (!method.getName().substring(0, 3).equals("set")) {
				continue;
			}

			i += validateRequired(obj, errors, method);
			i += validateMinValue(obj, errors, method);
			i += validateEmail(obj, errors, method);
		}
		return i;
	}

	private int validateMinValue(Object obj, BindException errors, Method method) {
		int nErrors = 0;
		MinValue minValueAnnotation = method.getAnnotation(MinValue.class);
		if (minValueAnnotation != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();

			try {
				Method getMethod = objClass.getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);

				int minValue = Integer.parseInt(minValueAnnotation.value());
				if (methodResult != null && methodResult instanceof Integer && ((Integer)methodResult) < minValue ) {
					String fieldText = getFieldDisplayName(obj, fieldName);
					Object[] args = new Object[] { fieldText, minValue};
					nErrors = 1;
					errors.rejectValue(fieldName, "errors.XMustBeGreaterThanY", args, fieldName + " is required.");
				}
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}

		return nErrors;
	}

	private int validateEmail(Object obj, BindException errors, Method method) {
		int nErrors = 0;
		if (method.getAnnotation(Email.class) != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			try {
				Method getMethod = obj.getClass().getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);

				if (methodResult != null && methodResult instanceof String && !StringUtils.isEmpty((String) methodResult)
						&& !EmailValidator.getInstance().isValid((String) methodResult)) {

					String fieldText = getFieldDisplayName(obj, fieldName);
					Object[] args = new Object[] { fieldText };
					nErrors = 1;
					errors.rejectValue(fieldName, "errors.email", args, methodResult
							+ " er ikke en gyldig epostadresse i feltet " + fieldName + ".");
				}
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}
		return nErrors;
	}

	private int validateRequired(Object obj, BindException errors, Method method) {
		int nErrors=0;
		if (method.getAnnotation(Required.class) != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();
			boolean isInUse = configurationManager.isActive("access." + objClass.getSimpleName().toLowerCase() + ".use" + fieldNameCamelCase, true);
			if (isInUse) {
				isInUse = configurationManager.isActive("access." + objClass.getSimpleName().toLowerCase() + ".use" + fieldName, true);
			}
			if (isInUse && fieldNameCamelCase.endsWith("id")){
				isInUse = configurationManager.isActive("access." + objClass.getSimpleName().toLowerCase() + ".use" + fieldNameCamelCase.substring(0,fieldNameCamelCase.length()-"id".length()), true);
			}
			if (!isInUse) {
				return nErrors;
			}
			try {
				Method getMethod = objClass.getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);

				if (methodResult == null || (methodResult instanceof String && StringUtils.isEmpty((String) methodResult))) {
					String fieldText = getFieldDisplayName(obj, fieldName);
					Object[] args = new Object[] { fieldText };
					nErrors = 1;
					errors.rejectValue(fieldName, "errors.required", args, fieldName + " is required.");
				}
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}
		return nErrors;
	}

	private String getFieldDisplayName(Object obj, String fieldName) {
		String fieldText = ApplicationResourcesUtil.getText(obj.getClass().getSimpleName().toLowerCase() + "."
				+ fieldName);
		if (StringUtils.isEmpty(fieldText)) {
			fieldText = ApplicationResourcesUtil.getText(fieldName);
		}
		if (StringUtils.isEmpty(fieldText)) {
			fieldText = fieldName;
		}
		return fieldText;
	}

	private String lowercaseFirstLetter(String fieldNameCamelCase) {
		String firstLetterLowercase = fieldNameCamelCase.substring(0, 1).toLowerCase();
		String fieldName = firstLetterLowercase+fieldNameCamelCase.substring(1);
		return fieldName;
	}
}
