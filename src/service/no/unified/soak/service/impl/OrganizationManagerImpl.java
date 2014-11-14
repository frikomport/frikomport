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
package no.unified.soak.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import no.unified.soak.dao.OrganizationDAO;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.service.OrganizationManager;

/**
 * Implementation of OrganizationManager interface to talk to the persistence
 * layer.
 * 
 * @author hrj
 */
public class OrganizationManagerImpl extends BaseManager implements OrganizationManager {
    private OrganizationDAO dao;

    /**
     * @see no.unified.soak.service.OrganizationManager#setOrganizationDAO(no.unified.soak.dao.OrganizationDAO)
     */
    public void setOrganizationDAO(OrganizationDAO dao) {
        this.dao = dao;
    }

    /**
     * @see no.unified.soak.service.OrganizationManager#getOrganization(java.lang.Long)
     */
    public Organization getOrganization(Long id) {
        return dao.getOrganization(id);
    }

    /**
     * @see no.unified.soak.service.OrganizationManager#saveOrganization(no.unified.soak.model.Organization)
     */
    public void saveOrganization(Organization organization) {
        dao.saveOrganization(organization);
    }

    /**
     * @see no.unified.soak.service.OrganizationManager#removeOrganization(java.lang.Long)
     */
    public void removeOrganization(Long id) {
        dao.removeOrganization(id);
    }

    /**
     * @see no.unified.soak.service.OrganizationManager#getAll(no.unified.soak.model.Organization)
     */
    public List getAll() {
        return dao.getAll(new Boolean(false));
    }

    /**
     * @see no.unified.soak.service.OrganizationManager#getAllIncludingDisabled()
     */
    public List getAllIncludingDisabled() {
        return dao.getAll(new Boolean(true));
    }

    public List getAllIncludingDummy(String dummy) {
        List organizations = new ArrayList();
        organizations.add(makeDummyOrganization(dummy));
        organizations.addAll(getAll());
        return organizations;
    }

    public List getByTypeIncludingDummy(Type type, String dummy) {
        List organizations = new ArrayList();
        organizations.add(makeDummyOrganization(dummy));
        organizations.addAll(getOrganizationsByType(type));
        return organizations;
    }

    public List getOrganizationsByType(Type orgType) {
        return dao.getByType(orgType.getTypeDBValue());
    }

    public Map<Long, Organization> getOrganizationsNumbermapByType(Type orgType) {
        List<Organization> organizationsByType = dao.getByType(orgType.getTypeDBValue());
        Map<Long, Organization> organizationsNumbermapByType = new HashMap<Long, Organization>();
        
        for (Organization organization : organizationsByType) {
			organizationsNumbermapByType.put(organization.getNumber(), organization);
		}
		return organizationsNumbermapByType;
    }

    public List getByTypeIncludingParentAndDummy(Type type, Type parentType, String dummy) {
        List organizations = new ArrayList();
        organizations.add(makeDummyOrganization(dummy));
        organizations.addAll(getOrganizationsByTypeIncludingParent(type, parentType));
        return organizations;
    }

    public List getOrganizationsByTypeIncludingParent(Type type, Type parentType) {
        List organizations = new ArrayList();
    	List parents = getOrganizationsByType(parentType);
    	if(!parents.isEmpty()){
    		Iterator p = parents.iterator();
    		while(p.hasNext()){
    			Organization parent = (Organization)p.next();
    			organizations.add(parent);
    			organizations.addAll(getOrganizationsByParent(parent, type));
    		}
    	}
        return organizations;
    }
    
    public List getOrganizationsByParent(Organization parent, Type type){
    	return dao.getByParent(parent.getId(), type);
    }
    
    private Organization makeDummyOrganization(String dummy) {
        Organization organizationDummy = new Organization();
        organizationDummy.setId(null);
        organizationDummy.setName(dummy);
        return organizationDummy;
    }


}
