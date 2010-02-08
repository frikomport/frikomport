package no.unified.soak.util;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class ApplicationResourcesUtil {

	private static MessageSource messageSource = null;

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
     * Method for getting a key's value (with i18n support). Calling
     * getMessageSourceAccessor() is used because the RequestContext variable is
     * not set in unit tests b/c there's no DispatchServlet Request.
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
        } catch (Exception e) {
            // TODO Handle exception
        }
        return result;
    }

    /**
     * Method for getting a key's value (with i18n support). Calling
     * getMessageSourceAccessor() is used because the RequestContext variable is
     * not set in unit tests b/c there's no DispatchServlet Request.
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
     * Method for getting a key's value (with i18n support). Calling
     * getMessageSourceAccessor() is used because the RequestContext variable is
     * not set in unit tests b/c there's no DispatchServlet Request.
     * 
     * @param msgKey
     *            The key to the message
     * @param args
     *            An arbitrary number of arguments to be inserted into the
     *            retrieved message
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

}