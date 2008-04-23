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

import no.unified.soak.dao.LocationDAO;
import no.unified.soak.model.Location;
import no.unified.soak.service.impl.LocationManagerImpl;

import org.jmock.Mock;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;


/**
 * Test class for LocationManager
 *
 * @author hrj
 */
public class LocationManagerTest extends BaseManagerTestCase {
    private final String locationId = "1";
    private LocationManager locationManager = new LocationManagerImpl();
    private Mock locationDAO = null;
    private Location location = null;

    protected void setUp() throws Exception {
        super.setUp();
        locationDAO = new Mock(LocationDAO.class);
        locationManager.setLocationDAO((LocationDAO) locationDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        locationManager = null;
    }

    /**
     * Tests getting a list of Locations using a mock DAO
     *
     * @throws Exception
     */
    public void testGetLocations() throws Exception {
        List results = new ArrayList();
        location = new Location();
        results.add(location);

        // set expected behavior on dao
        locationDAO.expects(once()).method("getLocations")
                   .will(returnValue(results));

        List locations = locationManager.getLocations(null, new Boolean(false));
        assertTrue(locations.size() == 1);
        locationDAO.verify();
    }

    /**
     * Tests searching for locations using a mock DAO
     * @throws Exception
     */
    public void testSearchLocations() throws Exception {
        location = new Location();
        location.setOrganizationid(new Long("2"));

        locationDAO.expects(once()).method("searchLocations")
                   .will(returnValue(new ArrayList()));

        List searchResults = locationManager.searchLocations(location);
        assertNotNull(searchResults);
        locationDAO.verify();
    }

    /**
     * Tests getting a single Location using a mock DAO
     *
     * @throws Exception
     */
    public void testGetLocation() throws Exception {
        // set expected behavior on dao
        locationDAO.expects(once()).method("getLocation")
                   .will(returnValue(new Location()));
        location = locationManager.getLocation(locationId);
        assertTrue(location != null);
        locationDAO.verify();
    }

    /**
     * Tests persisting a Location using a mock DAO
     *
     * @throws Exception
     */
    public void testSaveLocation() throws Exception {
        // set expected behavior on dao
        locationDAO.expects(once()).method("saveLocation").with(same(location))
                   .isVoid();

        locationManager.saveLocation(location);
        locationDAO.verify();
    }

    /**
     * Tests adding and then removing a Location using a mock DAO
     *
     * 1) Set the required fields
     * 2) "Save" it
     * 3) "Remove it"
     * 4) Check that everything went according to plan
     *
     * @throws Exception
     */
    public void testAddAndRemoveLocation() throws Exception {
        location = new Location();

        // set required fields
        location.setAddress("Kari Karisens vei 11");
        location.setName("Karis Kurslokaler");
        location.setOrganizationid(new Long(1505726338));

        // set expected behavior on dao
        locationDAO.expects(once()).method("saveLocation").with(same(location))
                   .isVoid();
        locationManager.saveLocation(location);
        locationDAO.verify();

        // reset expectations
        locationDAO.reset();

        locationDAO.expects(once()).method("removeLocation")
                   .with(eq(new Long(locationId)));
        locationManager.removeLocation(locationId);
        locationDAO.verify();

        // reset expectations
        locationDAO.reset();

        // remove
        Exception ex = new ObjectRetrievalFailureException(Location.class,
                location.getId());
        locationDAO.expects(once()).method("removeLocation").isVoid();
        locationDAO.expects(once()).method("getLocation")
                   .will(throwException(ex));
        locationManager.removeLocation(locationId);

        try {
            locationManager.getLocation(locationId);
            fail("Location with identifier '" + locationId +
                "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }

        locationDAO.verify();
    }
}
