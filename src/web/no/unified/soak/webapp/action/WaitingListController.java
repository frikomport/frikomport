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
package no.unified.soak.webapp.action;

import no.unified.soak.service.WaitingListManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author kst
 *
 */
public class WaitingListController extends ParameterizableViewController {
    private WaitingListManager waitingListManager = null;

    protected ModelAndView handleRequestInternal(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        waitingListManager.processEntireWaitingList();
        response.setContentType("text/html");

        return new ModelAndView();
    }

    /**
     * @return Returns the waitingListManager.
     */
    public WaitingListManager getWaitingListManager() {
        return waitingListManager;
    }

    /**
     * @param waitingListManager
     *            The waitingListManager to set.
     */
    public void setWaitingListManager(WaitingListManager waitingListManager) {
        this.waitingListManager = waitingListManager;
    }
}
