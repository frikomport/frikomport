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
package no.unified.soak.service;

import no.unified.soak.dao.ServiceAreaDAO;
import no.unified.soak.model.ServiceArea;

import java.util.List;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author hrj
 */
public interface ServiceAreaManager extends Manager {
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setServiceAreaDAO(ServiceAreaDAO serviceAreaDAO);

    /**
     * Retrieves all of the serviceAreas
     */
    public List<ServiceArea> getAll();

    /**
     * Finds all service areas registered in the database, regardless of status
     *
     * @return A list of all registered serviceareas
     */
    public List<ServiceArea> getAllIncludingDisabled();

    /**
     * Retrieves all of the serviceAreas
     * @param value
     */
    public List<ServiceArea> getAllIncludingDummy(String value);

    /**
     * Gets serviceArea's information based on id.
     *
     * @param id
     *            the serviceArea's id
     * @return serviceArea populated serviceArea object
     */
    public ServiceArea getServiceArea(final String id);

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
    public void removeServiceArea(final String id);

    /**
     * Searches serviceAreas for given parameters
     * @param serviceArea
     * @return
     */
    public List<ServiceArea> searchServiceAreas(ServiceArea serviceArea);
}
