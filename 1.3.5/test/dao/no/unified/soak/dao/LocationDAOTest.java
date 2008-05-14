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

import no.unified.soak.model.Location;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.List;


/**
 * Test class for the LocationDAO class
 *
 * @author hrj
 */
public class LocationDAOTest extends BaseDAOTestCase {
    private Long locationId = new Long("1");
    private Location location = null;
    private LocationDAO dao = null;

    /**
     * @param dao The DAO to set
     */
    public void setLocationDAO(LocationDAO dao) {
        this.dao = dao;
    }

    /**
     * Tests the add new Location functionality.
     * 1) Set required fields
     * 2) Save the new Location
     * 3) Verify that it has been saved correctly
     *
     * @throws Exception
     */
    public void testAddLocation() throws Exception {
        location = new Location();

        // set required fields
        String address = "Ola Nordmannsvei 40";
        location.setAddress(address);

        String name = "Ola Nordmanns Kurssenter";
        location.setName(name);

        Long organizationid = new Long(1);
        location.setOrganizationid(organizationid);

        Boolean selectable = new Boolean("true");
        location.setSelectable(selectable);

        dao.saveLocation(location);

        // verify a primary key was assigned
        assertNotNull(location.getId());

        // verify set fields are same after save
        assertEquals(address, location.getAddress());
        assertEquals(name, location.getName());
        assertEquals(organizationid, location.getOrganizationid());
        assertEquals(selectable, location.getSelectable());
    }

    /**
     * Tests getting an existing Location
     * Prerequisite: A Location with Id = 1 exists in the database
     *
     * @throws Exception
     */
    public void testGetLocation() throws Exception {
        location = dao.getLocation(locationId);
        assertNotNull(location);
    }

    /**
     * Tests getting a list of all Locations in the database
     * Prerequisite: A minimum of one Location must exist in the datbase
           * minimum of one selectable Location must exist in the database
    *
     * @throws Exception
     */
    public void testGetLocations() throws Exception {
        location = new Location();

        List results = dao.getLocations(location, new Boolean(false));
        assertTrue(results.size() > 0);
    }

    /**
     * Tests the persistance of a Location to the databsae
     * 1) Set required fields
     * 2) Save it
     * 3) Verify that it has been stored correctly
     *
     * @throws Exception
     */
    public void testSaveLocation() throws Exception {
        location = dao.getLocation(locationId);

        // update required fields
        String address = "Peder Hansens vei 30";
        location.setAddress(address);

        String name = "Peder Hansens Kurs";
        location.setName(name);

        Long organizationid = new Long(2);
        location.setOrganizationid(organizationid);

        Boolean selectable = new Boolean("true");
        location.setSelectable(selectable);

        dao.saveLocation(location);

        assertEquals(address, location.getAddress());
        assertEquals(name, location.getName());
        assertEquals(organizationid, location.getOrganizationid());
        assertEquals(selectable, location.getSelectable());
    }

    /**
     * Tests the removal of an existing Location
     * Prerequisite: A Location with id = 3 must exist in the database
     *
     * 1) Remove Location
     * 2) Try to retrieve the (hopefully) deleted object from the db
     * 3) If Exception gotten => Object not found => Everything is ok!
     *
     * @throws Exception
     */
    public void testRemoveLocation() throws Exception {
        Long removeId = new Long("3");
        dao.removeLocation(removeId);

        try {
            dao.getLocation(removeId);
            fail("location found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Tests the basic search functionaly for Location
     *
     * Prerequisite: A Location with organizationid=2
     * where all dates are in the futur must exist in the database
     *
     * 1) Set the object
     * 2) Search
     * 3) See if we got any results (we should get at least one)
     *
     * @throws Exception
     */
    public void testSearchLocations() throws Exception {
        Location location = new Location();
        location.setOrganizationid(new Long("2"));

        List searchResults = dao.searchLocations(location);
        assertNotNull(searchResults);
    }
}
