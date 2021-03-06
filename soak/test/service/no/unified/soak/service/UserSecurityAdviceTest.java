/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service;

import no.unified.soak.Constants;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.model.Role;
import no.unified.soak.model.User;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.jmock.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class UserSecurityAdviceTest extends BaseManagerTestCase {
    Mock userDAO = null;

    protected void setUp() throws Exception {
        super.setUp();

        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user",
                "password",
                new GrantedAuthority[] {
                    new GrantedAuthorityImpl(Constants.DEFAULT_ROLE)
                });
        token.setAuthenticated(false);
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
    }

    public void testAddUserWithoutAdminRole() throws Exception {
        // TODO: Feiler i test. Sannsynligvis pga oppgradering til acegi 1.0 i forbindelse med spring 2
//        Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
//        assertTrue(auth.isAuthenticated());

        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("admin");

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    public void testAddUserAsAdmin() throws Exception {
        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin",
                "password",
                new GrantedAuthority[] {
                    new GrantedAuthorityImpl(Constants.ADMIN_ROLE)
                });
        token.setAuthenticated(false);
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);

        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("admin");

        userDAO.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDAO.verify();
    }

    public void testUpdateUserProfile() throws Exception {
        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("user");
        ;
        user.getRoles().add(new Role(Constants.DEFAULT_ROLE));

        userDAO.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDAO.verify();
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testChangeToAdminRoleFromUserRole() throws Exception {
        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testAddAdminRoleWhenAlreadyHasUserRole()
        throws Exception {
        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.DEFAULT_ROLE));

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testAddUserRoleWhenHasAdminRole() throws Exception {
        SecurityContext context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user",
                "password",
                new GrantedAuthority[] {
                    new GrantedAuthorityImpl(Constants.ADMIN_ROLE)
                });
        token.setAuthenticated(false);
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);

        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.DEFAULT_ROLE));

        userDAO.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDAO.verify();
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    public void testUpdateUserWithUserRole() throws Exception {
        UserManager userManager = (UserManager) makeInterceptedTarget();
        User user = new User("user");
        user.getRoles().add(new Role(Constants.DEFAULT_ROLE));

        userDAO.expects(once()).method("saveUser");
        userManager.saveUser(user);
        userDAO.verify();
    }

    private UserManager makeInterceptedTarget() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "no/unified/soak/service/applicationContext-test.xml");

        UserManager userManager = (UserManager) context.getBean("target");

        // Mock the userDAO
        userDAO = new Mock(UserDAO.class);
        userManager.setUserDAO((UserDAO) userDAO.proxy());

        return userManager;
    }
}
