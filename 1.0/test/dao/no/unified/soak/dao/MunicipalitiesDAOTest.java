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

import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Test of the MunicipalitiesDAO class
 *
 * @author hrj
 */
public class MunicipalitiesDAOTest extends BaseDAOTestCase {
    private Municipalities municipalities = null;
    private MunicipalitiesDAO dao = null;

    /**
     * @param dao
     *            The dao to set.
     */
    public void setMunicipalitiesDao(MunicipalitiesDAO dao) {
        this.dao = dao;
    }

    /**
     * Test of the get-functionality 1) Save a Municipality 2) Try to retrieve
     * the one we just saved
     *
     * @throws Exception
     */
    public void testGetMunicipalities() throws Exception {
        municipalities = new Municipalities();
        municipalities.setName("Kongsberg");
        municipalities.setNumber(new Long(9999));
        municipalities.setSelectable(new Boolean(true));

        dao.saveMunicipalities(municipalities);
        assertNotNull(municipalities.getId());

        municipalities = dao.getMunicipalities(municipalities.getId());
        assertEquals(municipalities.getName(), "Kongsberg");
    }

    /**
     * Tests the getAll-fuctionality. Retrieves all munipalities and checks if
     * the result has one or more entries.
     *
     * @throws Exception
     */
    public void testGetAll() throws Exception {
        municipalities = new Municipalities();

        List result = dao.getAll(new Boolean(true));
        assertTrue(result.size() > 0);
    }

    /**
     * Tests the save-functionality of Municipalities 1) Set all required
     * attributes 2) Save it 3) Check whether it was saved ok
     *
     * @throws Exception
     */
    public void testSaveMunicipalities() throws Exception {
        municipalities = new Municipalities();
        municipalities.setName("Kongsberg updated");
        municipalities.setNumber(new Long(9999));
        municipalities.setSelectable(new Boolean(true));

        dao.saveMunicipalities(municipalities);

        if (log.isDebugEnabled()) {
            log.debug("updated Municipalities: " + municipalities);
        }

        assertEquals(municipalities.getName(), "Kongsberg updated");
    }

    /**
     * Tests add and then remove of a Municipalities
     *
     * 1) Add a new Municipalities 2) Check whether it is added correctly 3)
     * Remove it 4) Try to retrieve it (should fail with an exception)
     *
     * @throws Exception
     */
    public void testAddAndRemoveMunicipalities() throws Exception {
        municipalities = new Municipalities();
        municipalities.setName("Notodden");
        municipalities.setNumber(new Long(9999));
        municipalities.setSelectable(new Boolean(true));

        dao.saveMunicipalities(municipalities);
        assertEquals(municipalities.getName(), "Notodden");
        assertNotNull(municipalities.getId());

        if (log.isDebugEnabled()) {
            log.debug("removing municipalities");
        }

        dao.removeMunicipalities(municipalities.getId());

        try {
            municipalities = dao.getMunicipalities(municipalities.getId());
            fail("Municipalities found in database! :(");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
}
