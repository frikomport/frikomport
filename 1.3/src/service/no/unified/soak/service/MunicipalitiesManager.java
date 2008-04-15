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

import no.unified.soak.dao.MunicipalitiesDAO;
import no.unified.soak.model.Municipalities;

import java.util.List;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author hrj
 */
public interface MunicipalitiesManager {
    public void setMunicipalitiesDAO(MunicipalitiesDAO dao);

    /**
     * Retrieves municipality by id
     *
     * @param id
     * @return Municipality
     */
    public Municipalities getMunicipalities(Long id);

    /**
     * Saves or updates given municipality
     *
     * @param municipalities
     */
    public void saveMunicipalities(Municipalities municipalities);

    /**
     * Deletes municipality with given id
     *
     * @param id
     */
    public void removeMunicipalities(Long id);

    /**
     * Gets all enabled municipalities
     *
     * @return List of all enabled municipalities
     */
    public List getAll();

    /**
     * Gets all municipalities ever registered, disabled or not
     *
     * @return List of all municipalities
     */
    public List getAllIncludingDisabled();
}
