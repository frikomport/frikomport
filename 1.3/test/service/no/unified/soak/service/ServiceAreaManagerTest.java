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
package no.unified.soak.service;

import no.unified.soak.dao.ServiceAreaDAO;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.service.impl.ServiceAreaManagerImpl;

import org.jmock.Mock;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;


/**
 * Test class for ServiceAreaManager
 *
 * @author hrj
 */
public class ServiceAreaManagerTest extends BaseManagerTestCase {
    private final String serviceAreaId = "1";
    private ServiceAreaManager serviceAreaManager = new ServiceAreaManagerImpl();
    private Mock serviceAreaDAO = null;
    private ServiceArea serviceArea = null;

    protected void setUp() throws Exception {
        super.setUp();
        serviceAreaDAO = new Mock(ServiceAreaDAO.class);
        serviceAreaManager.setServiceAreaDAO((ServiceAreaDAO) serviceAreaDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        serviceAreaManager = null;
    }

    /**
     * Tests getting a List of all ServiceAreas using a mock DAO
     *
     * @throws Exception
     */
    public void testGetServiceAreas() throws Exception {
        List results = new ArrayList();
        serviceArea = new ServiceArea();
        results.add(serviceArea);

        // set expected behavior on dao
        serviceAreaDAO.expects(once()).method("getServiceAreas")
                      .will(returnValue(results));

        List serviceAreas = serviceAreaManager.getServiceAreas();
        assertTrue(serviceAreas.size() == 1);
        serviceAreaDAO.verify();
    }

    /**
     * Tests getting a List of all ServiceAreas using a mock DAO
     *
     * @throws Exception
     */
    public void testGetServiceAreasIncludingDisabled()
        throws Exception {
        List results = new ArrayList();
        serviceArea = new ServiceArea();
        results.add(serviceArea);

        // set expected behavior on dao
        serviceAreaDAO.expects(once()).method("getServiceAreas")
                      .will(returnValue(results));

        List serviceAreas = serviceAreaManager.getServiceAreasIncludingDisabled();
        assertTrue(serviceAreas.size() == 1);
        serviceAreaDAO.verify();
    }

    /**
     * Test getting a single Service Area using a mock DAO
     *
     * @throws Exception
     */
    public void testGetServiceArea() throws Exception {
        // set expected behavior on dao
        serviceAreaDAO.expects(once()).method("getServiceArea")
                      .will(returnValue(new ServiceArea()));
        serviceArea = serviceAreaManager.getServiceArea(serviceAreaId);
        assertTrue(serviceArea != null);
        serviceAreaDAO.verify();
    }

    /**
     * Tests persisting a ServiceArea using a mock DAO
     * @throws Exception
     */
    public void testSaveServiceArea() throws Exception {
        // set expected behavior on dao
        serviceAreaDAO.expects(once()).method("saveServiceArea")
                      .with(same(serviceArea)).isVoid();

        serviceAreaManager.saveServiceArea(serviceArea);
        serviceAreaDAO.verify();
    }

    /**
     * Tests adding and then removing a ServiceArea using a mock DAO
     *
     * 1) Set the required fields
     * 2) "Save" it
     * 3) "Remove it"
     * 4) Check that everything went according to plan
     *
     * @throws Exception
     */
    public void testAddAndRemoveServiceArea() throws Exception {
        serviceArea = new ServiceArea();

        // set required fields
        serviceArea.setName("OK-tjenesten");
        serviceArea.setSelectable(new Boolean("false"));

        // set expected behavior on dao
        serviceAreaDAO.expects(once()).method("saveServiceArea")
                      .with(same(serviceArea)).isVoid();
        serviceAreaManager.saveServiceArea(serviceArea);
        serviceAreaDAO.verify();

        // reset expectations
        serviceAreaDAO.reset();

        serviceAreaDAO.expects(once()).method("removeServiceArea")
                      .with(eq(new Long(serviceAreaId)));
        serviceAreaManager.removeServiceArea(serviceAreaId);
        serviceAreaDAO.verify();

        // reset expectations
        serviceAreaDAO.reset();

        // remove
        Exception ex = new ObjectRetrievalFailureException(ServiceArea.class,
                serviceArea.getId());
        serviceAreaDAO.expects(once()).method("removeServiceArea").isVoid();
        serviceAreaDAO.expects(once()).method("getServiceArea")
                      .will(throwException(ex));
        serviceAreaManager.removeServiceArea(serviceAreaId);

        try {
            serviceAreaManager.getServiceArea(serviceAreaId);
            fail("ServiceArea with identifier '" + serviceAreaId +
                "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }

        serviceAreaDAO.verify();
    }
}
