/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 09.des.2005
 */
package no.unified.soak.dao;

import no.unified.soak.model.Organization;

import java.util.List;


/**
 * User Data Access Object (DAO) interface.
 *
 * @author hrj
 */
public interface OrganizationDAO extends DAO {
    /**
     * Retrieve organization by id
     *
     * @param organizationId
     * @return organization with given id
     */
    public Organization getOrganization(Long organizationId);

    /**
     * Save or update given organization
     *
     * @param organization
     */
    public void saveOrganization(Organization organization);

    /**
     * Delete organization with given id
     *
     * @param id
     */
    public void removeOrganization(Long id);

    /**
     * Retrieve all available organizations
     *
     * @param includeDisabled
     *            whether or not to also include disabled organizations
     * @return A list of all available organizations
     */
    public List getAll(Boolean includeDisabled);
}
