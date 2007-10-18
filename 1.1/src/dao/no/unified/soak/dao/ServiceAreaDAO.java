/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * created 14. dec 2005
 */
package no.unified.soak.dao;

import no.unified.soak.model.ServiceArea;

import java.util.List;


/**
 * User Data Access Object (DAO) interface.
 *
 * @author hrj
 */
public interface ServiceAreaDAO extends DAO {
    /**
     * Retrieves all of the serviceAreas
     *
     * @param includeDisabled
     *            set to 'true' to include the disabled ones
     */
    public List getServiceAreas(Boolean includeDisabled);

    /**
     * Gets serviceArea's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param id
     *            the serviceArea's id
     * @return serviceArea populated serviceArea object
     */
    public ServiceArea getServiceArea(final Long id);

    /**
     * Saves a serviceArea's information
     *
     * @param serviceArea
     *            the object to be saved
     */
    public void saveServiceArea(ServiceArea serviceArea);

    /**
     * Removes a serviceArea from the database by id
     *
     * @param id
     *            the serviceArea's id
     */
    public void removeServiceArea(final Long id);
}
