/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.dao;

import java.util.List;


/**
 * Lookup Data Access Object (DAO) interface.  This is used to lookup values in
 * the database (i.e. for drop-downs).
 *
 * <p>
 * <a href="LookupDAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface LookupDAO extends DAO {
    public List getRoles();
}
