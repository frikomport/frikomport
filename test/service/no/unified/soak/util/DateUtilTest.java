/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.util;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Date;
import java.util.Locale;


public class DateUtilTest extends TestCase {
    private final Log log = LogFactory.getLog(DateUtilTest.class);

    public DateUtilTest(String name) {
        super(name);
    }

    public void testGetNonEnglishDatePattern() {
        LocaleContextHolder.setLocale(new Locale("no"));
        assertEquals("dd.MM.yyyy", DateUtil.getDatePattern());
    }

    public void testGetDate() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("db date to convert: " + new Date());
        }

        String date = DateUtil.getDate(new Date());

        if (log.isDebugEnabled()) {
            log.debug("converted ui date: " + date);
        }

        assertTrue(date != null);
    }

    public void testGetDateTime() {
        if (log.isDebugEnabled()) {
            log.debug("entered 'testGetDateTime' method");
        }

        String now = DateUtil.getTimeNow(new Date());
        assertTrue(now != null);
        log.debug(now);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DateUtilTest.class);
    }
}
