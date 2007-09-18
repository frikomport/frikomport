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

import no.unified.soak.model.Municipalities;

import java.util.List;


/**
 * User Data Access Object (DAO) interface.
 *
 * @author hrj
 */
public interface MunicipalitiesDAO extends DAO {
    /**
     * Retrieve municipality by id
     *
     * @param municipalitiesId
     * @return municipality with given id
     */
    public Municipalities getMunicipalities(Long municipalitiesId);

    /**
     * Save or update given municipality
     *
     * @param municipalities
     */
    public void saveMunicipalities(Municipalities municipalities);

    /**
     * Delete municipality with given id
     *
     * @param id
     */
    public void removeMunicipalities(Long id);

    /**
     * Retrieve all available municipalities
     *
     * @param includeDisabled
     *            whether or not to also include disabled municipalities
     * @return A list of all available municipalities
     */
    public List getAll(Boolean includeDisabled);
}
