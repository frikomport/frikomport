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

import no.unified.soak.model.Address;
import no.unified.soak.model.Organization;

import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Test of the OrganizationDAO class
 *
 * @author hrj
 */
public class OrganizationDAOTest extends BaseDAOTestCase {
    private Organization organization = null;
    private OrganizationDAO dao = null;

    /**
     * @param dao
     *            The dao to set.
     */
    public void setOrganizationDao(OrganizationDAO dao) {
        this.dao = dao;
    }

    /**
     * Test of the get-functionality 1) Save a Organization 2) Try to retrieve
     * the one we just saved
     *
     * @throws Exception
     */
    public void testGetOrganization() throws Exception {
        organization = new Organization();
        organization.setName("Kongsberg");
        organization.setNumber(new Long(9999));
        organization.setSelectable(new Boolean(true));
        organization.setInvoiceName("Kongsberg kommune");
        Address address = new Address();
        address.setAddress("Postboks 115");
        address.setPostalCode("3601");
        address.setCity("Kongsberg");

        dao.saveOrganization(organization);
        assertNotNull(organization.getId());

        organization = dao.getOrganization(organization.getId());
        assertEquals(organization.getName(), "Kongsberg");
    }

    /**
     * Tests the getAll-fuctionality. Retrieves all munipalities and checks if
     * the result has one or more entries.
     *
     * @throws Exception
     */
    public void testGetAll() throws Exception {
        organization = new Organization();

        List result = dao.getAll(new Boolean(true));
        assertTrue(result.size() > 0);
    }

    /**
     * Tests the save-functionality of Organization 1) Set all required
     * attributes 2) Save it 3) Check whether it was saved ok
     *
     * @throws Exception
     */
    public void testSaveOrganization() throws Exception {
        organization = new Organization();
        organization.setName("Kongsberg updated");
        organization.setNumber(new Long(9999));
        organization.setSelectable(new Boolean(true));
        organization.setInvoiceName("Kongsberg kommune");
        Address address = new Address();
        address.setAddress("Postboks 115");
        address.setPostalCode("3601");
        address.setCity("Kongsberg");

        dao.saveOrganization(organization);

        if (log.isDebugEnabled()) {
            log.debug("updated Organization: " + organization);
        }

        assertEquals(organization.getName(), "Kongsberg updated");
    }

    /**
     * Tests add and then remove of a Organization
     *
     * 1) Add a new Organization 2) Check whether it is added correctly 3)
     * Remove it 4) Try to retrieve it (should fail with an exception)
     *
     * @throws Exception
     */
    public void testAddAndRemoveOrganization() throws Exception {
        organization = new Organization();
        organization.setName("Notodden");
        organization.setNumber(new Long(9999));
        organization.setSelectable(new Boolean(true));
        organization.setInvoiceName("Kongsberg kommune");
        Address address = new Address();
        address.setAddress("Postboks 115");
        address.setPostalCode("3601");
        address.setCity("Kongsberg");

        dao.saveOrganization(organization);
        assertEquals(organization.getName(), "Notodden");
        assertNotNull(organization.getId());

        if (log.isDebugEnabled()) {
            log.debug("removing organization");
        }

        dao.removeOrganization(organization.getId());

        try {
            organization = dao.getOrganization(organization.getId());
            fail("Organization found in database! :(");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
}
