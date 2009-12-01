/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.util;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;


/**
 * This class is converts a java.util.Date to a String
 * and a String to a java.util.Date.
 *
 * <p>
 * <a href="DateConverter.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class DateConverter implements Converter {
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        } else if (type == Date.class) {
            return convertToDate(type, value);
        } else if (type == String.class) {
            return convertToString(type, value);
        }

        throw new ConversionException("Could not convert " +
            value.getClass().getName() + " to " + type.getName());
    }

    protected Object convertToDate(Class type, Object value) {
        DateFormat df = new SimpleDateFormat(DateUtil.getDatePattern());

        if (value instanceof String) {
            try {
                if (StringUtils.isEmpty(value.toString())) {
                    return null;
                }

                return df.parse((String) value);
            } catch (Exception pe) {
                throw new ConversionException("Error converting String to Date");
            }
        }

        throw new ConversionException("Could not convert " +
            value.getClass().getName() + " to " + type.getName());
    }

    protected Object convertToString(Class type, Object value) {
        DateFormat df = new SimpleDateFormat(DateUtil.getDatePattern());

        if (value instanceof Date) {
            try {
                return df.format(value);
            } catch (Exception e) {
                throw new ConversionException("Error converting Date to String");
            }
        }

        return value.toString();
    }
}
