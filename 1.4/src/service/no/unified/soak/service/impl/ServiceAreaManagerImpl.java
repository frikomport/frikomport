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
package no.unified.soak.service.impl;

import no.unified.soak.dao.ServiceAreaDAO;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.service.ServiceAreaManager;

import java.util.List;
import java.util.ArrayList;


/**
 * Implementation of ServiceAreaManager interface to talk to the persistence
 * layer.
 *
 * @author hrj
 */
public class ServiceAreaManagerImpl extends BaseManager
    implements ServiceAreaManager {
    private ServiceAreaDAO dao;

    /**
     * Set the DAO for communication with the data layer.
     *
     * @param dao
     */
    public void setServiceAreaDAO(ServiceAreaDAO dao) {
        this.dao = dao;
    }

    /**
     * @see no.unified.soak.service.ServiceAreaManager#getServiceAreas(no.unified.soak.model.ServiceArea)
     */
    public List getAll() {
        return dao.getAll(new Boolean(false));
    }

    public List getAllIncludingDisabled() {
        return dao.getAll(new Boolean(true));
    }

    public List getAllIncludingDummy(String value) {
        List serviceAreas = new ArrayList();
        ServiceArea dummy = new ServiceArea();
        dummy.setId(null);
        dummy.setName(value);
        serviceAreas.add(dummy);
        serviceAreas.addAll(getAll());
        return serviceAreas;
    }

    /**
     * @see no.unified.soak.service.ServiceAreaManager#getServiceArea(String id)
     */
    public ServiceArea getServiceArea(final String id) {
        return dao.getServiceArea(new Long(id));
    }

    /**
     * @see no.unified.soak.service.ServiceAreaManager#saveServiceArea(ServiceArea
     *      serviceArea)
     */
    public void saveServiceArea(ServiceArea serviceArea) {
        dao.saveServiceArea(serviceArea);
    }

    /**
     * @see no.unified.soak.service.ServiceAreaManager#removeServiceArea(String
     *      id)
     */
    public void removeServiceArea(final String id) {
        dao.removeServiceArea(new Long(id));
    }
}
