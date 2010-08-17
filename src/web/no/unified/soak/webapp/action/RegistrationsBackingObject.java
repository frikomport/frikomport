/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created January 16th, 2006
 */
package no.unified.soak.webapp.action;

import no.unified.soak.model.Registration;

import java.util.ArrayList;
import java.util.List;


/**
 * formBackingObject used to retrieve lists of registrations from Forms
 *
 * @author hrj
 */
public class RegistrationsBackingObject {
    List<Registration> registrations = new ArrayList<Registration>();

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }
}
