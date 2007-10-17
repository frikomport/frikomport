/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 08.jan.2006
 */
package no.unified.soak.webapp.decorators;

import java.util.Date;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.time.FastDateFormat;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;


/**
 * Wraps a full date to a all numeric day + month + four digit year format
 *
 * For now it is hardcoded as we are unable to get hold of locale etc. until
 * version 1.1 of the display tag library.
 *
 * @author hrj
 */
public class StandardDateWrapper implements DisplaytagColumnDecorator {
    FastDateFormat dateFormat = FastDateFormat.getInstance("dd.MM.yyyy");

    /**
     * Constructor
     */
    public StandardDateWrapper() {
        super();
    }
    
    /**
     * @see org.displaytag.decorator.DisplaytagColumnDecorator#decorate(java.lang.Object, javax.servlet.jsp.PageContext, org.displaytag.properties.MediaTypeEnum)
     */
    public Object decorate(Object arg0, PageContext arg1, MediaTypeEnum arg2) throws DecoratorException {
        String result = "";

        if (arg0 != null) {
            result = dateFormat.format((Date) arg0);
        }

        return result;
    }
}
