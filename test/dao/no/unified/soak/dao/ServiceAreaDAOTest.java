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
package no.unified.soak.dao;

import no.unified.soak.model.ServiceArea;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.List;


/**
 * Test class for the ServiceAreaDAO class
 *
 * @author hrj
 */
public class ServiceAreaDAOTest extends BaseDAOTestCase {
    private Long serviceAreaId = new Long("1");
    private ServiceArea serviceArea = null;
    private ServiceAreaDAO dao = null;

    /**
     * @param dao
     *            The dao to set
     */
    public void setServiceAreaDAO(ServiceAreaDAO dao) {
        this.dao = dao;
    }

    /**
     * Tests the add new ServiceArea functionality. 1) Set required fields 2)
     * Save the new ServiceArea 3) Verify that it has been saved correctly
     *
     * @throws Exception
     */
    public void testAddServiceArea() throws Exception {
        serviceArea = new ServiceArea();

        // set required fields
        String name = "Testtjenesten";
        serviceArea.setName(name);

        Boolean selectable = new Boolean("true");
        serviceArea.setSelectable(selectable);
        serviceArea.setOrganizationid(new Long(1));

        dao.saveServiceArea(serviceArea);

        // verify a primary key was assigned
        assertNotNull(serviceArea.getId());

        // verify set fields are same after save
        assertEquals(name, serviceArea.getName());
        assertEquals(selectable, serviceArea.getSelectable());
    }

    /**
     * Tests getting an existing ServiceArea Prerequisite: A ServiceArea with Id =
     * 1 exists in the database
     *
     * @throws Exception
     */
    public void testGetServiceArea() throws Exception {
        serviceArea = dao.getServiceArea(serviceAreaId);
        assertNotNull(serviceArea);
    }

    /**
     * Tests getting a list of all ServiceAreas in the database Prerequisite: A
     * minimum of one selectable ServiceArea must exist in the database
     *
     * @throws Exception
     */
    public void testGetServiceAreas() throws Exception {
        serviceArea = new ServiceArea();

        // The test is wider if we set the parameter to false
        List results = dao.getAll(new Boolean(false));
        assertTrue(results.size() > 0);
    }

    /**
     * Tests the persistance of a ServiceArea to the databsae 1) Set required
     * fields 2) Save it 3) Verify that it has been stored correctly
     *
     * @throws Exception
     */
    public void testSaveServiceArea() throws Exception {
        serviceArea = dao.getServiceArea(serviceAreaId);

        // update required fields
        String name = "Test av tjenesteomraade";
        serviceArea.setName(name);

        Boolean selectable = new Boolean("true");
        serviceArea.setSelectable(selectable);

        dao.saveServiceArea(serviceArea);

        assertEquals(name, serviceArea.getName());
        assertEquals(selectable, serviceArea.getSelectable());
    }

    /**
     * Tests the removal of an existing ServiceArea Prerequisite: A ServiceArea
     * with id = 3 must exist in the database
     *
     * 1) Remove ServiceArea 2) Try to retrieve the (hopefully) deleted object
     * from the db 3) If Exception gotten => Object not found => Everything is
     * ok!
     *
     * @throws Exception
     */
    public void testRemoveServiceArea() throws Exception {
        Long removeId = new Long("3");
        dao.removeServiceArea(removeId);

        try {
            dao.getServiceArea(removeId);
            fail("serviceArea found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
    }
}
