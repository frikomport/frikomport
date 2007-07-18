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
import no.unified.soak.service.impl.MunicipalitiesManagerImpl;

import org.jmock.Mock;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;


/**
 * Test class for MunicipalitiesManager
 *
 * @author hrj
 */
public class MunicipalitiesManagerTest extends BaseManagerTestCase {
    private final Long municipalitiesId = new Long(1);
    private MunicipalitiesManager municipalitiesManager = new MunicipalitiesManagerImpl();
    private Mock municipalitiesDAO = null;
    private Municipalities municipalities = null;

    protected void setUp() throws Exception {
        super.setUp();
        municipalitiesDAO = new Mock(MunicipalitiesDAO.class);
        municipalitiesManager.setMunicipalitiesDAO((MunicipalitiesDAO) municipalitiesDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        municipalitiesManager = null;
    }

    /**
     * Tests the get-functionality of the manager with a mock DAO
     *
     * @throws Exception
     */
    public void testGetMunicipalities() throws Exception {
        // set behaviour on DAO
        municipalitiesDAO.expects(once()).method("getMunicipalities")
                         .will(returnValue(new Municipalities()));
        municipalities = municipalitiesManager.getMunicipalities(municipalitiesId);
        assertTrue(municipalities != null);
        municipalitiesDAO.verify();
    }

    /**
     * Tests the save-functionality of the manager with a mock version of the
     * DAO
     *
     * @throws Exception
     */
    public void testSaveMunicipalities() throws Exception {
        // set expected behavior on dao
        municipalitiesDAO.expects(once()).method("saveMunicipalities")
                         .with(same(municipalities)).isVoid();

        municipalitiesManager.saveMunicipalities(municipalities);
        municipalitiesDAO.verify();
    }

    /**
     * Tests the add-functionality of the manager and then the
     * remove-functionality with a mock version of the DAO
     *
     * @throws Exception
     */
    public void testAddAndRemoveMunicipalities() throws Exception {
        municipalities = new Municipalities();

        // set required fields
        municipalities.setName("Rollag Kommune");
        municipalities.setNumber(new Long(10));
        municipalities.setSelectable(new Boolean(true));

        // set expected behavior on dao
        municipalitiesDAO.expects(once()).method("saveMunicipalities")
                         .with(same(municipalities)).isVoid();
        municipalitiesManager.saveMunicipalities(municipalities);
        municipalitiesDAO.verify();

        // reset expectations
        municipalitiesDAO.reset();

        municipalitiesDAO.expects(once()).method("removeMunicipalities")
                         .with(eq(municipalitiesId));
        municipalitiesManager.removeMunicipalities(municipalitiesId);
        municipalitiesDAO.verify();

        // reset expectations
        municipalitiesDAO.reset();

        // remove
        Exception ex = new ObjectRetrievalFailureException(Municipalities.class,
                municipalities.getId());
        municipalitiesDAO.expects(once()).method("removeMunicipalities").isVoid();
        municipalitiesDAO.expects(once()).method("getMunicipalities")
                         .will(throwException(ex));
        municipalitiesManager.removeMunicipalities(municipalitiesId);

        try {
            municipalitiesManager.getMunicipalities(municipalitiesId);
            fail("Municipalities with identifier '" + municipalitiesId +
                "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }

        municipalitiesDAO.verify();
    }

    /**
     * Tests the get all-functionality of the manager with a mock version of the
     * DAO
     *
     * @throws Exception
     */
    public void testGetAll() throws Exception {
        List results = new ArrayList();
        municipalities = new Municipalities();
        results.add(municipalities);

        // set expected behavior on dao
        municipalitiesDAO.expects(once()).method("getAll")
                         .will(returnValue(results));

        List municipalitieslist = municipalitiesManager.getAll();
        assertTrue(municipalitieslist.size() == 1);
        municipalitiesDAO.verify();
    }

    public void testGetAllIncludingDisabled() throws Exception {
        List results = new ArrayList();
        municipalities = new Municipalities();
        results.add(municipalities);

        // set expected behavior on dao
        municipalitiesDAO.expects(once()).method("getAll")
                         .will(returnValue(results));

        List municipalitieslist = municipalitiesManager.getAllIncludingDisabled();
        assertTrue(municipalitieslist.size() == 1);
        municipalitiesDAO.verify();
    }
}
