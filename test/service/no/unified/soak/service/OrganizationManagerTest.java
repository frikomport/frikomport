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

import no.unified.soak.dao.OrganizationDAO;
import no.unified.soak.model.Organization;
import no.unified.soak.service.impl.OrganizationManagerImpl;

import org.jmock.Mock;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;


/**
 * Test class for OrganizationManager
 *
 * @author hrj
 */
public class OrganizationManagerTest extends BaseManagerTestCase {
    private final Long organizationId = new Long(1);
    private OrganizationManager organizationManager = new OrganizationManagerImpl();
    private Mock organizationDAO = null;
    private Organization organization = null;

    protected void setUp() throws Exception {
        super.setUp();
        organizationDAO = new Mock(OrganizationDAO.class);
        organizationManager.setOrganizationDAO((OrganizationDAO) organizationDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        organizationManager = null;
    }

    /**
     * Tests the get-functionality of the manager with a mock DAO
     *
     * @throws Exception
     */
    public void testGetOrganization() throws Exception {
        // set behaviour on DAO
        organizationDAO.expects(once()).method("getOrganization")
                         .will(returnValue(new Organization()));
        organization = organizationManager.getOrganization(organizationId);
        assertTrue(organization != null);
        organizationDAO.verify();
    }

    /**
     * Tests the save-functionality of the manager with a mock version of the
     * DAO
     *
     * @throws Exception
     */
    public void testSaveOrganization() throws Exception {
        // set expected behavior on dao
        organizationDAO.expects(once()).method("saveOrganization")
                         .with(same(organization)).isVoid();

        organizationManager.saveOrganization(organization);
        organizationDAO.verify();
    }

    /**
     * Tests the add-functionality of the manager and then the
     * remove-functionality with a mock version of the DAO
     *
     * @throws Exception
     */
    public void testAddAndRemoveOrganization() throws Exception {
        organization = new Organization();

        // set required fields
        organization.setName("Rollag Kommune");
        organization.setNumber(new Long(10));
        organization.setSelectable(new Boolean(true));

        // set expected behavior on dao
        organizationDAO.expects(once()).method("saveOrganization")
                         .with(same(organization)).isVoid();
        organizationManager.saveOrganization(organization);
        organizationDAO.verify();

        // reset expectations
        organizationDAO.reset();

        organizationDAO.expects(once()).method("removeOrganization")
                         .with(eq(organizationId));
        organizationManager.removeOrganization(organizationId);
        organizationDAO.verify();

        // reset expectations
        organizationDAO.reset();

        // remove
        Exception ex = new ObjectRetrievalFailureException(Organization.class,
                organization.getId());
        organizationDAO.expects(once()).method("removeOrganization").isVoid();
        organizationDAO.expects(once()).method("getOrganization")
                         .will(throwException(ex));
        organizationManager.removeOrganization(organizationId);

        try {
            organizationManager.getOrganization(organizationId);
            fail("Organization with identifier '" + organizationId +
                "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }

        organizationDAO.verify();
    }

    /**
     * Tests the get all-functionality of the manager with a mock version of the
     * DAO
     *
     * @throws Exception
     */
    public void testGetAll() throws Exception {
        List results = new ArrayList();
        organization = new Organization();
        results.add(organization);

        // set expected behavior on dao
        organizationDAO.expects(once()).method("getAll")
                         .will(returnValue(results));

        List organizationlist = organizationManager.getAll();
        assertTrue(organizationlist.size() == 1);
        organizationDAO.verify();
    }

    public void testGetAllIncludingDisabled() throws Exception {
        List results = new ArrayList();
        organization = new Organization();
        results.add(organization);

        // set expected behavior on dao
        organizationDAO.expects(once()).method("getAll")
                         .will(returnValue(results));

        List organizationlist = organizationManager.getAllIncludingDisabled();
        assertTrue(organizationlist.size() == 1);
        organizationDAO.verify();
    }
}
