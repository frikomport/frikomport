/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/**
 *
 */
package no.unified.soak.util;

import org.apache.commons.beanutils.ConversionException;


/**
 * Converts between String and java.lang.Integer and possibly other number
 * types.
 *
 * @author kst
 */
public class NumConvert {
    public static Object convertTolerant(Class type, Object value) {
        if (value == null) {
            return null;
        } else if ((type == Integer.class) && value instanceof String) {
            return convertToIntegerTolerant((String) value);
        }

        throw new ConversionException("Could not convert value [" + value +
            "] of type " + value.getClass().getName() + " to " +
            type.getName());
    }

    public static Integer convertToIntegerTolerant(String num) {
        if ((num != null) && (num.trim().length() > 0)) {
            return Integer.parseInt(num);
        }

        return null;
    }
}
