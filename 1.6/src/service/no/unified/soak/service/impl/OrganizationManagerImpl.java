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

import no.unified.soak.dao.OrganizationDAO;
import no.unified.soak.model.Organization;
import no.unified.soak.service.OrganizationManager;

import java.util.List;
import java.util.ArrayList;


/**
 * Implementation of OrganizationManager interface to talk to the persistence layer.
 *
 * @author hrj
 */
public class OrganizationManagerImpl extends BaseManager
    implements OrganizationManager {
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
        Organization organizationDummy = new Organization();
        organizationDummy.setId(null);
        organizationDummy.setName(dummy);
        organizations.add(organizationDummy);
        organizations.addAll(getAll());
        return organizations;
    }
}
