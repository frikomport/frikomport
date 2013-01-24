/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service;

import java.util.ArrayList;
import java.util.List;

import no.unified.soak.Constants;
import no.unified.soak.dao.RoleDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.model.Role;
import no.unified.soak.model.User;
import no.unified.soak.model.UserCookie;
import no.unified.soak.service.impl.RoleManagerImpl;
import no.unified.soak.service.impl.UserManagerImpl;
import no.unified.soak.util.StringUtil;

import org.jmock.Mock;
import org.springframework.dao.DataIntegrityViolationException;


public class UserManagerTest extends BaseManagerTestCase {
    private UserManager userManager = new UserManagerImpl();
    private RoleManager roleManager = new RoleManagerImpl();
    private Mock userDAO = null;
    private Mock roleDAO = null;
    private User user = null;
    private Role role = null;

    protected void setUp() throws Exception {
        super.setUp();
        userDAO = new Mock(UserDAO.class);
        userManager.setUserDAO((UserDAO) userDAO.proxy());
        roleDAO = new Mock(RoleDAO.class);
        roleManager.setRoleDAO((RoleDAO) roleDAO.proxy());
    }

    public void testGetUser() throws Exception {
        User testData = new User("tomcat");
        testData.getRoles().add(new Role("user"));
        // set expected behavior on dao
        userDAO.expects(once()).method("getUser").with(eq("tomcat"))
               .will(returnValue(testData));

        user = userManager.getUser("tomcat");
        assertTrue(user != null);
        assertTrue(user.getRoles().size() == 1);
        userDAO.verify();
    }

    public void testSaveUser() throws Exception {
        User testData = new User("tomcat");
        testData.getRoles().add(new Role("user"));
        // set expected behavior on dao
        userDAO.expects(once()).method("getUser").with(eq("tomcat"))
               .will(returnValue(testData));

        user = userManager.getUser("tomcat");
        user.setPhoneNumber("30355512");
        userDAO.verify();

        // reset expectations
        userDAO.reset();
        userDAO.expects(once()).method("saveUser").with(same(user));

        userManager.saveUser(user);
        assertTrue(user.getPhoneNumber().equals("30355512"));
        assertTrue(user.getRoles().size() == 1);
        userDAO.verify();
    }

    public void testAddAndRemoveUser() throws Exception {
        user = new User();

        // call populate method in super class to populate test data
        // from a properties file matching this class name
        user = (User) populate(user);

        // set expected behavior on role dao
        roleDAO.expects(once()).method("getRole").with(eq("anonymous"))
               .will(returnValue(new Role("anonymous")));

        role = roleManager.getRole(Constants.DEFAULT_ROLE);
        roleDAO.verify();
        user.addRole(role);

        // set expected behavior on user dao
        userDAO.expects(once()).method("saveUser").with(same(user));

        userManager.saveUser(user);
        assertTrue(user.getUsername().equals("john"));
        assertTrue(user.getRoles().size() == 1);
        userDAO.verify();

        // reset expectations
        userDAO.reset();

        userDAO.expects(once()).method("removeUser").with(eq(user.getUsername()));
        userManager.removeUser(user.getUsername());
        userDAO.verify();

        // reset expectations
        userDAO.reset();
        userDAO.expects(once()).method("getUser").will(returnValue(null));
        user = userManager.getUser("john");
        assertNull(user);
        userDAO.verify();
    }

    public void testLoginWithCookie() {
        // set expectations
        userDAO.expects(once()).method("saveUserCookie");

        String cookieString = userManager.createLoginCookie("anonymous");

        assertNotNull(cookieString);
        userDAO.verify();

        // reset expectations
        userDAO.expects(once()).method("getUserCookie")
               .will(returnValue(new UserCookie()));
        // lookup succeeds, save will be called to generate a new one
        userDAO.expects(once()).method("saveUserCookie");

        String newCookie = userManager.checkLoginCookie(cookieString);
        assertNotNull(newCookie);
        userDAO.verify();

        // reset expectations
        userDAO.expects(once()).method("getUserCookie").will(returnValue(null));
        newCookie = userManager.checkLoginCookie(cookieString);
        assertNull(newCookie);
        userDAO.verify();
    }

    public void testUserExistsException() {
        // set expectations
        user = new User("Administrator User");
        user.setEmail("matt@raibledesigns.com");

        List users = new ArrayList();

        users.add(user);

        Exception ex = new DataIntegrityViolationException("");
        userDAO.expects(once()).method("saveUser").with(same(user))
               .will(throwException(ex));

        // run test
        try {
            userManager.saveUser(user);
            fail("Expected UserExistsException not thrown");
        } catch (UserExistsException e) {
            log.debug("expected exception: " + e.getMessage());
            assertNotNull(e);
        }
    }
    
    public void testGetUserWithHash(){
    	String username = "testuser";
    	String email = "test@frikomport.no";
    	String encoded = StringUtil.encodeString(email);
    	
    	user = new User(username);
    	user.setEmail(email);
    	user.setHash(encoded);
    	
    	userDAO.expects(once()).method("getUserByHash").with(eq(user.getHash()));
    	userDAO.expects(once()).method("findUserByEmail").with(eq(email));
    	
    	userManager.getUserByHash(encoded);
    	userDAO.verify();
    	
    	encoded = StringUtil.encodeString(username);
    	user.setHash(encoded);

    	userDAO.expects(once()).method("getUserByHash").with(eq(user.getHash()));
    	userDAO.expects(once()).method("getUser").with(eq(username));

    	userManager.getUserByHash(encoded);
    	userDAO.verify();
    	
    }
    
}
