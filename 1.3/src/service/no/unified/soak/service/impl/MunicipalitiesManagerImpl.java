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

import no.unified.soak.dao.MunicipalitiesDAO;
import no.unified.soak.model.Municipalities;
import no.unified.soak.service.MunicipalitiesManager;

import java.util.List;


/**
 * Implementation of MunicipalitiesManager interface to talk to the persistence layer.
 *
 * @author hrj
 */
public class MunicipalitiesManagerImpl extends BaseManager
    implements MunicipalitiesManager {
    private MunicipalitiesDAO dao;

    /**
     * @see no.unified.soak.service.MunicipalitiesManager#setMunicipalitiesDAO(no.unified.soak.dao.MunicipalitiesDAO)
     */
    public void setMunicipalitiesDAO(MunicipalitiesDAO dao) {
        this.dao = dao;
    }

    /**
     * @see no.unified.soak.service.MunicipalitiesManager#getMunicipalities(java.lang.Long)
     */
    public Municipalities getMunicipalities(Long id) {
        return dao.getMunicipalities(id);
    }

    /**
     * @see no.unified.soak.service.MunicipalitiesManager#saveMunicipalities(no.unified.soak.model.Municipalities)
     */
    public void saveMunicipalities(Municipalities municipalities) {
        dao.saveMunicipalities(municipalities);
    }

    /**
     * @see no.unified.soak.service.MunicipalitiesManager#removeMunicipalities(java.lang.Long)
     */
    public void removeMunicipalities(Long id) {
        dao.removeMunicipalities(id);
    }

    /**
     * @see no.unified.soak.service.MunicipalitiesManager#getAll(no.unified.soak.model.Municipalities)
     */
    public List getAll() {
        return dao.getAll(new Boolean(false));
    }

    /**
     * @see no.unified.soak.service.MunicipalitiesManager#getAllIncludingDisabled()
     */
    public List getAllIncludingDisabled() {
        return dao.getAll(new Boolean(true));
    }
}
