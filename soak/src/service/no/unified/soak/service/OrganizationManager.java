/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 13.des.2005
 */
package no.unified.soak.service;


import java.util.List;

import no.unified.soak.dao.OrganizationDAO;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Organization.Type;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author hrj
 */
public interface OrganizationManager {
    public void setOrganizationDAO(OrganizationDAO dao);

    /**
     * Retrieves organization by id
     *
     * @param id
     * @return Organization
     */
    public Organization getOrganization(Long id);

    /**
     * Saves or updates given organization
     *
     * @param organizations
     */
    public void saveOrganization(Organization organization);

    /**
     * Deletes organization with given id
     *
     * @param id
     */
    public void removeOrganization(Long id);

    /**
     * Gets all enabled organizations
     *
     * @return List of all enabled organizations
     */
    public List<Organization> getAll();

    /**
     * Gets all organizations ever registered, disabled or not
     *
     * @return List of all organizations
     */
    public List<Organization> getAllIncludingDisabled();

    /**
     * Gets all enabled organizations including a dummy
     * @param dummy of the dummy organization
     * @return
     */
    public List<Organization> getAllIncludingDummy(String dummyOrganizationName);

    public List<Organization> getByTypeIncludingDummy(Organization.Type type, String dummyOrganizationName);
    
    public List<Organization> getOrganizationsByType(Organization.Type type);
    
    
    public List getByTypeIncludingParentAndDummy(Type type, Type parentType, String dummy);

    public List getOrganizationsByTypeIncludingParent(Type type, Type parentType);
    
    public List getOrganizationsByParent(Organization parent, Type type);

    
	/**
	 * Evict entity for hibernate sessions. This avoids automatic saving
	 * (flush) of the entity.
	 * 
	 * @param entity
	 */
	public void evict(Object entity);


    
}
