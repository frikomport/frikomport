/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.webapp.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.Address;
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
import no.unified.soak.util.DateUtil;
import no.unified.soak.validation.DigitsOnly;
import no.unified.soak.validation.Email;
import no.unified.soak.validation.LessThanField;
import no.unified.soak.validation.MaxLength;
import no.unified.soak.validation.MinLength;
import no.unified.soak.validation.MinValue;
import no.unified.soak.validation.NotLessThanField;
import no.unified.soak.validation.Required;
import no.unified.soak.validation.ValidateOnlyIfConfigurationIsTrue;

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
    
	protected int validateAnnotations(Object obj, BindException errors, String referencingObjectFieldnamePrefix) {
		int i = 0;
		if (referencingObjectFieldnamePrefix == null) {
			referencingObjectFieldnamePrefix = "";
		}
		
		if(obj == null) return 0;
		
		for (Method method : obj.getClass().getMethods()) {
			String methodName = method.getName();
			if (!methodName.substring(0, 3).equals("set")) {
				continue;
			}

			if (Address.class.getSimpleName().equals(method.getParameterTypes()[0].getSimpleName())
					&& !hasValidationAnnotation(method)) {
				try {
					Method getterMethod = obj.getClass().getMethod("get" + methodName.substring(3), (Class[]) null);
					Object objUsed = getterMethod.invoke(obj, (Object[]) null);
					String fieldNameCamelCase = method.getName().substring(3);
					String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
					i += validateAnnotations(objUsed, errors, fieldName+".");
				} catch (Exception e) {
					log.warn("Validation error connected to obj=[]" + obj + " and method=[" + method + "].", e);
				}
			}

			i += validateRequired(obj, errors, method, referencingObjectFieldnamePrefix);
			i += validateMinValue(obj, errors, method, referencingObjectFieldnamePrefix);
			i += validateEmail(obj, errors, method, referencingObjectFieldnamePrefix);
			i += validateDigitsOnly(obj, errors, method, referencingObjectFieldnamePrefix);
			i += validateMinLength(obj, errors, method, referencingObjectFieldnamePrefix);
			i += validateMaxLength(obj, errors, method, referencingObjectFieldnamePrefix);
			i += validateLessThanField(obj, errors, method, referencingObjectFieldnamePrefix);
			i += validateNotLessThanField(obj, errors, method, referencingObjectFieldnamePrefix);
		}
		return i;
	}

	private int validateLessThanField(Object obj, BindException errors, Method method, String referencingObjectFieldnamePrefix) {
		int nErrors = 0;
		LessThanField lessThanFieldAnnotation = method.getAnnotation(LessThanField.class);
		if (lessThanFieldAnnotation != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();

			try {
				Method getMethod = objClass.getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);
				if (methodResult == null) {
					return nErrors;
				}

				String lessThanField = lessThanFieldAnnotation.value();
				Object lessThanFieldValue = callGetterOfField(obj, lessThanField);
				if (lessThanFieldValue instanceof Date && methodResult instanceof Date) {
					Date lessThanFieldValueDate = (Date) lessThanFieldValue;
					if (((Date) methodResult).after(lessThanFieldValueDate) || ((Date)methodResult).equals(lessThanFieldValueDate)) {
						String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix + fieldName);
						String lessThanFieldsText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix + lessThanField);
						String LessThanValueMessage = DateUtil.getDateTime("dd.MM.yyyy HH:mm", lessThanFieldValueDate) + " ("
								+ lessThanFieldsText + ")";
						
						Object[] args = new Object[] { fieldText, LessThanValueMessage };
						nErrors = 1;
						errors.rejectValue(referencingObjectFieldnamePrefix + fieldName, "errors.XMustBeLessThanY", args,
								fieldName + " can not be lower than " + LessThanValueMessage + ".");
					}

				} else if (lessThanFieldValue != null) {
					log.warn("Feil under validering av regel lessThanField for feltet " + lessThanField
							+ ": Kan ikke validere felt av datatypen " + methodResult.getClass());
					return nErrors;
				}

			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}

		return nErrors;
	}

	private int validateNotLessThanField(Object obj, BindException errors, Method method, String referencingObjectFieldnamePrefix) {
		int nErrors = 0;
		NotLessThanField notLessThanFieldAnnotation = method.getAnnotation(NotLessThanField.class);
		if (notLessThanFieldAnnotation != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();

			try {
				Method getMethod = objClass.getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);
				if (methodResult == null) {
					return nErrors;
				}

				String notLessThanField = notLessThanFieldAnnotation.value();
				Object notLessThanFieldValue = callGetterOfField(obj, notLessThanField);
				if (notLessThanFieldValue instanceof Date && methodResult instanceof Date) {
					Date notLessThanFieldValueDate = (Date) notLessThanFieldValue;
					if (((Date) methodResult).before(notLessThanFieldValueDate)) {
						String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix + fieldName);
						String notLessThanFieldsText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix + notLessThanField);
						String notLessThanValueMessage = DateUtil.getDateTime("dd.MM.yyyy HH:mm", notLessThanFieldValueDate) + " ("
								+ notLessThanFieldsText + ")";
						
						Object[] args = new Object[] { fieldText, notLessThanValueMessage };
						nErrors = 1;
						errors.rejectValue(referencingObjectFieldnamePrefix + fieldName, "errors.XCanNotBeLessThanY", args,
								fieldName + " can not be lower than " + notLessThanValueMessage + ".");
					}

				} else if (notLessThanFieldValue != null) {
					log.warn("Feil under validering av regel notLessThanField for feltet " + notLessThanField
							+ ": Kan ikke validere felt av datatypen " + methodResult.getClass());
					return nErrors;
				}

			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}

		return nErrors;
	}

	private Object callGetterOfField(Object obj, String field) {
		if (StringUtils.isBlank(field)) {
			return null;
		}
		Method getMethod;
		Object methodResult = null;
		try {
			getMethod = obj.getClass().getMethod("get" + StringUtils.capitalize(field));
			methodResult = getMethod.invoke(obj);
		} catch (Exception e) {
			log.warn("Feil under validering av feltet " + field + " for objektet " + obj + ": " + e);
		}
		return methodResult;
	}

	private boolean hasValidationAnnotation(Method method) {
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			if (MinLength.class.equals(annotation.annotationType()) || MinValue.class.equals(annotation.annotationType())
					|| DigitsOnly.class.equals(annotation.annotationType()) || Required.class.equals(annotation.annotationType()) || Email.class
					.equals(annotation.annotationType())) {
				return true;
			}

		}
		return false;
	}

	private int validateMinLength(Object obj, BindException errors, Method method, String referencingObjectFieldnamePrefix) {
		int nErrors=0;
		MinLength minLengthAnnotation = method.getAnnotation(MinLength.class);
		if (minLengthAnnotation != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();
			
			if (isDisabledByAnnotatedConfiguration(method)) {
				return nErrors;
			}

			try {
				Method getMethod = objClass.getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);
				int minLength = Integer.parseInt(minLengthAnnotation.value());

				if (methodResult != null && methodResult instanceof String && StringUtils.isNotBlank((String) methodResult) && ((String) methodResult).length() < minLength) {
					String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix+fieldName);
					Object[] args = new Object[] { fieldText, minLength };
					nErrors = 1;
					errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.minlength", args, fieldName + " must have at least "+minLength+" charcters.");
				} else if (methodResult != null && !(methodResult instanceof String)) {
					Object[] args = new String[] { "Tried to validate the length of a non-string field \"" + fieldName
							+ "\". Cannot validate." };
					errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.detail", args, "Illegal internal state tied to the value of "
							+ fieldName + ".");
				}
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}
		return nErrors;
	}

	private int validateMaxLength(Object obj, BindException errors, Method method, String referencingObjectFieldnamePrefix) {
		int nErrors=0;
		MaxLength maxLengthAnnotation = method.getAnnotation(MaxLength.class);
		if (maxLengthAnnotation != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();
			
			if (isDisabledByAnnotatedConfiguration(method)) {
				return nErrors;
			}

			try {
				Method getMethod = objClass.getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);
				int maxLength = Integer.parseInt(maxLengthAnnotation.value());

				if (methodResult != null && methodResult instanceof String && StringUtils.isNotBlank((String) methodResult) && ((String) methodResult).length() > maxLength) {
					String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix+fieldName);
					Object[] args = new Object[] { fieldText, maxLength };
					nErrors = 1;
					errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.maxlength", args, fieldName + " must have at most "+maxLength+" charcters.");
				} else if (methodResult != null && !(methodResult instanceof String)) {
					Object[] args = new String[] { "Tried to validate the length of a non-string field \"" + fieldName
							+ "\". Cannot validate." };
					errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.detail", args, "Illegal internal state tied to the value of "
							+ fieldName + ".");
				}
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}
		return nErrors;
	}
	private int validateDigitsOnly(Object obj, BindException errors, Method method, String referencingObjectFieldnamePrefix) {
		int nErrors=0;
		DigitsOnly digitsOnlyAnnotation = method.getAnnotation(DigitsOnly.class);
		if (digitsOnlyAnnotation != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();
			
			if (isDisabledByAnnotatedConfiguration(method)) {
				return nErrors;
			}
			
			try {
				Method getMethod = objClass.getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);

				if (methodResult != null && methodResult instanceof String) {
					String methodResultStr = (String) methodResult;
					if (StringUtils.isEmpty(methodResultStr)) {
						return nErrors;
					}
					
					Long methodResultInt = Long.parseLong(methodResultStr);
					if (methodResultInt < 0) {
						String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix+fieldName);
						Object[] args = new Object[] { fieldText };
						nErrors = 1;
						errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.digitsOnly", args, fieldName + " can only have digits.");
					}
				} else if (methodResult != null && !(methodResult instanceof String)) {
					Object[] args = new String[] { "Tried to validate the length of a non-string field \"" + fieldName
							+ "\". Cannot validate." };
					errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.detail", args, "Illegal internal state tied to the value of "
							+ fieldName + ".");
				}
			} catch (NumberFormatException e) {
				String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix+fieldName);
				Object[] args = new Object[] { fieldText };
				nErrors = 1;
				errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.digitsOnly", args, fieldName + " can only have digits.");
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}
		return nErrors;
	}

	private boolean isDisabledByAnnotatedConfiguration(Method method) {
		ValidateOnlyIfConfigurationIsTrue onlyIfConfigurationIsTrue = method.getAnnotation(ValidateOnlyIfConfigurationIsTrue.class);
		if (onlyIfConfigurationIsTrue == null) {
			return false;
		}
		return !configurationManager.isActive(onlyIfConfigurationIsTrue.value(), false);
	}

	private int validateMinValue(Object obj, BindException errors, Method method, String referencingObjectFieldnamePrefix) {
		int nErrors = 0;
		MinValue minValueAnnotation = method.getAnnotation(MinValue.class);
		if (minValueAnnotation != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();

			try {
				Method getMethod = objClass.getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);

				String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix+fieldName);

				if (methodResult != null && methodResult instanceof Integer){
					int minValue = Integer.parseInt(minValueAnnotation.value());
					if(((Integer) methodResult) < minValue){
						Object[] args = new Object[] { fieldText, minValue };
						nErrors = 1;
						errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.XCanNotBeLessThanY", args, fieldName + " can not be lower than "
								+ minValue + ".");
					}
				}
				else if (methodResult != null && !(methodResult instanceof Integer)) {
					Object[] args = new Object[] { fieldText };
					nErrors = 1;
					errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.integer", args, fieldName + " must be an integer number.");
				}
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}

		return nErrors;
	}

	private int validateEmail(Object obj, BindException errors, Method method, String referencingObjectFieldnamePrefix) {
		int nErrors = 0;
		if (method.getAnnotation(Email.class) != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			try {
				Method getMethod = obj.getClass().getMethod("get" + fieldNameCamelCase);
				Object methodResult = getMethod.invoke(obj);

				if (methodResult != null && methodResult instanceof String && !StringUtils.isEmpty((String) methodResult)
						&& !EmailValidator.getInstance().isValid((String) methodResult)) {

					String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix+fieldName);
					Object[] args = new Object[] { fieldText };
					nErrors = 1;
					errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.email", args, methodResult
							+ " er ikke en gyldig epostadresse i feltet " + fieldName + ".");
				}
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}
		return nErrors;
	}

	private int validateRequired(Object obj, BindException errors, Method method, String referencingObjectFieldnamePrefix) {
		int nErrors=0;
		if (method.getAnnotation(Required.class) != null) {
			String fieldNameCamelCase = method.getName().substring(3);
			String fieldName = lowercaseFirstLetter(fieldNameCamelCase);
			Class<? extends Object> objClass = obj.getClass();
			boolean isInUse = configurationManager.isActive("access." + objClass.getSimpleName().toLowerCase() + ".use" + fieldNameCamelCase, true); // TODO: her bør selve konfigurasjonen hentes slik at korrekt default verdi i stedet benyttes
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
					String fieldText = getFieldDisplayName(obj, referencingObjectFieldnamePrefix+fieldName);
					Object[] args = new Object[] { fieldText };
					nErrors = 1;
					errors.rejectValue(referencingObjectFieldnamePrefix+fieldName, "errors.required", args, fieldName + " is required.");
				}
			} catch (Exception e) {
				log.warn("Feil under validering av " + fieldName + ": " + e);
			}
		}
		return nErrors;
	}

	private String getFieldDisplayName(Object obj, String fieldName) {
		String fieldText = ApplicationResourcesUtil.getText(obj.getClass().getSimpleName().toLowerCase() + "." + fieldName);
		if (StringUtils.isEmpty(fieldText)) {
			fieldText = ApplicationResourcesUtil.getText(fieldName);
		}

		// Checks if fieldname is prefixed with referencing fieldname from
		// another class. Like Registration.invoiceAddress is referencing
		// Address class.
		if (StringUtils.isEmpty(fieldText) && fieldName.contains(".")) {
			fieldText = ApplicationResourcesUtil.getText(fieldName.substring(fieldName.indexOf(".") + 1));
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
