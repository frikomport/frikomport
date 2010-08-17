package no.unified.soak.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class ApplicationResourcesUtil {

    private static MessageSource messageSource = null;
	private static String urlContextAppendix;
	private static String publicUrlContextAppendix;
	private static String loggedinUrlContextAppendix;
    private static final Log log = LogFactory.getLog(ApplicationResourcesUtil.class);

    private static final String localeVariant = System.getenv("FRIKOMPORT_VARIANT");

    public static String getLocaleVariant() {
        return localeVariant;
    }

    public static void setLocale(Locale l) {
    }

    public static void setMessageSource(MessageSource m) {
        messageSource = m;
    }

    /**
     * Method for getting a key's value (with i18n support). Calling getMessageSourceAccessor() is used because the
     * RequestContext variable is not set in unit tests b/c there's no DispatchServlet Request.
     * 
     * @param msgKey
     *            The key to the message
     * @param locale
     *            the current locale
     * @param messageSource
     *            The source of our messages
     */
    public static String getText(String msgKey) {
        String result = "";
        try {
            result = messageSource.getMessage(msgKey, new String[] {}, LocaleContextHolder.getLocale());
        } catch (NullPointerException e) {
            if (log != null) {
                log.error("messageSource object not found. Initial bean injection probably failed. " + e);
            } else {
                System.out.println("messageSource object not found. Initial bean injection probably failed." + e);
            }
        } catch (Exception e2) {
            if (log != null) {
                log.error("Failed getting a string from messageSource. Something wrong with bean injection?" + e2);
            } else {
                System.out.println("Failed getting a string from messageSource. Something wrong with bean injection?" + e2);
            }
        }
        return result;
    }

    /**
     * Method for getting a key's value (with i18n support). Calling getMessageSourceAccessor() is used because the
     * RequestContext variable is not set in unit tests b/c there's no DispatchServlet Request.
     * 
     * @param msgKey
     *            The key to the message
     * @param arg0
     *            Text to insert into the message
     * @param locale
     *            the current locale
     * @param messageSource
     *            The source of our messages
     */
    public static String getText(String msgKey, String arg0) {
        String result = "";
        try {
            result = messageSource.getMessage(msgKey, new String[] { arg0 }, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            // TODO Handle exception
        }
        return result;
    }

    /**
     * Method for getting a key's value (with i18n support). Calling getMessageSourceAccessor() is used because the
     * RequestContext variable is not set in unit tests b/c there's no DispatchServlet Request.
     * 
     * @param msgKey
     *            The key to the message
     * @param args
     *            An arbitrary number of arguments to be inserted into the retrieved message
     * @param locale
     *            the current locale
     * @param messageSource
     *            The source of our messages
     */
    public static String getText(String msgKey, Object[] args) {
        String result = "";
        try {
            result = messageSource.getMessage(msgKey, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            // TODO Handle exception
        }
        return result;
    }

    public static Locale getNewLocaleWithDefaultCountryAndVariant(Locale preferredLocale) {
    	if (preferredLocale == null) {
    		preferredLocale = new Locale("no", "NO");
    	}
        String localeCountry = preferredLocale.getCountry();
        Locale returnLocale;
        if (StringUtils.isEmpty(localeCountry)) {
            localeCountry = "NO";
        }
        if (getLocaleVariant() == null) {
            returnLocale = new Locale(preferredLocale.getLanguage(), localeCountry);
        } else {
            returnLocale = new Locale(preferredLocale.getLanguage(), localeCountry, getLocaleVariant());
        }
        return returnLocale;
    }

    public static boolean isSVV() {
        String localeVariant = getLocaleVariant();
        if (localeVariant != null && localeVariant.equalsIgnoreCase("FKPSVV")) {
            return true;
        }
        return false;
    }

    public static void saveErrorMessage(HttpServletRequest request, String error) {
        List errors = (List) request.getSession().getAttribute("listOfErrorMessages");

        if (errors == null) {
            errors = new ArrayList();
        }

        errors.add(error);
        request.getSession().setAttribute("listOfErrorMessages", errors);
    }

    public static void saveMessage(HttpServletRequest request, String msg) {
        List<String> messages = (List<String>) request.getSession().getAttribute("listOfMessages");

        if (messages == null) {
            messages = new ArrayList<String>();
        }

        messages.add(msg);
        request.getSession().setAttribute("listOfMessages", messages);
    }

	/**
	 * Sets the part of the url to use immediately after the application context
	 * part but before the page identifier part or the url.
	 * 
	 * @param contextAppendix
	 *            the context appendix should end with a slash (/) but a slash
	 *            at the beginning should be avoided.
	 */
	public static void setUrlContextAppendix(String contextAppendix) {
		urlContextAppendix = contextAppendix;
	}

	/**
	 * Gets the part of the url to use immediately after the application context
	 * part but before the page identifier part or the url. The returned value
	 * should end with a slash (/) but a slash at the beginning should be
	 * avoided. The function never returns null. If no contextAppendix is set,
	 * an empty string ("") is returned.
	 */
	public static String getUrlContextAppendix() {
		if (urlContextAppendix != null) {
			return urlContextAppendix;
		}
		return "";
	}

	public static String getPublicUrlContextAppendix() {
		if (publicUrlContextAppendix == null) {
			publicUrlContextAppendix = StringUtils.strip(ApplicationResourcesUtil.getText("publicUrlprefix"), "/") + "/";
		}
		return publicUrlContextAppendix;
	}

	public static String getLoggedinUrlContextAppendix() {
		if (loggedinUrlContextAppendix == null) {
			loggedinUrlContextAppendix = StringUtils.strip(ApplicationResourcesUtil.getText("loggedinUrlprefix"), "/") + "/";
		}
		return loggedinUrlContextAppendix;
	}
}
