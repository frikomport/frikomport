/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Date Utility Class
 * This is used to convert Strings to Dates and Timestamps
 *
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 *   to correct time pattern. Minutes should be mm not MM
 *         (MM is month).
 * @version $Revision: 1.3 $ $Date: 2006/02/08 21:35:43 $
 */
public class DateUtil {
    private static Log log = LogFactory.getLog(DateUtil.class);
    private static String timePattern = "HH:mm";

    public static String DATETIME = "dd.MM.YYYY HH:mm";
    
	/**
	 * Return default datePattern (MM/dd/yyyy)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDatePattern() {
		return ApplicationResourcesUtil.getText("date.format");
	}

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate, String aMask) throws ParseException {
		if (StringUtils.isBlank(strDate) || StringUtils.isBlank(aMask)) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat(aMask);
		df.setLenient(false); // reject dates like 37.10.2010 

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}

		return df.parse(strDate);
	}

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     *
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
        	SimpleDateFormat df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert
     * @return a date object
     *
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
        throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + getDatePattern());
            }

            aDate = convertStringToDate(strDate, getDatePattern());
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate +
                "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return aDate;
    }

	/**
	 * @param date
	 * @param beginOrEndOfDay
	 *            True if time should be at beginning of date. False if time
	 *            should be at end of date. null if time should be unchanged.
	 * @return
	 */
	public static String convertDateTimeToISOString(Date date, Boolean beginOrEndOfDay) {
		// 1970-01-01 10:00:01.0
		if (beginOrEndOfDay == null) {
			String isoPattern = ApplicationResourcesUtil.getText("datetime.formatISO");
			return getDateTime(isoPattern, date);
		}
		String isoPattern = ApplicationResourcesUtil.getText("date.formatISO");
		String dateTimeStr = getDateTime(isoPattern, date);
		if (beginOrEndOfDay) {
			dateTimeStr += " 00:00:00.0";
		} else {
			dateTimeStr += " 23:59:59.999";
		}
		return dateTimeStr;
	}
}
