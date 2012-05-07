/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import no.unified.soak.Constants;
import no.unified.soak.model.Role;
import no.unified.soak.model.User;
import no.unified.soak.util.GrantedAuthorityUtil;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationTrustResolver;
import org.acegisecurity.AuthenticationTrustResolverImpl;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.MethodBeforeAdvice;


public class UserSecurityAdvice implements MethodBeforeAdvice {
    public final static String ACCESS_DENIED = "Access Denied: Special access rules apply when editing users.";
    protected final Log log = LogFactory.getLog(UserSecurityAdvice.class);
    UserManager userManager;

	public void before(Method method, Object[] args, Object target)
        throws Throwable {
        SecurityContext ctx = (SecurityContext) SecurityContextHolder.getContext();

        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            boolean canEditUsers = false;
            GrantedAuthority[] roles = auth.getAuthorities();

            canEditUsers = GrantedAuthorityUtil.containsOneOrMore(roles, Constants.ADMIN_ROLE, Constants.EDITOR_ROLE);

            User user = (User) args[0];
            String username = user.getUsername();

            String currentUsername = null;

            if (auth.getPrincipal() instanceof UserDetails) {
                currentUsername = ((UserDetails) auth.getPrincipal()).getUsername();
            } else {
                currentUsername = String.valueOf(auth.getPrincipal());
            }

            if (!username.equals(currentUsername)) {
                AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();

                // allow new users to signup - this is OK b/c Signup doesn't allow setting of roles
                boolean signupUser = resolver.isAnonymous(auth);

                if (!signupUser) {
                    if (log.isDebugEnabled()) {
                        log.debug("Verifying that '" + currentUsername +
                            "' can modify '" + username + "'");
                    }

                    if (!canEditUsers) {
                        log.warn("Access Denied: '" + currentUsername +
                            "' tried to modify '" + username + "'!");
                        throw new AccessDeniedException(ACCESS_DENIED);
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Registering new user '" + username + "'");
                    }
                }
            }
            // fix for http://issues.appfuse.org/browse/APF-96
            // don't allow users with "user" role to upgrade to "admin" role
            else if (username.equalsIgnoreCase(currentUsername) && !canEditUsers) {
           		populateUserManager(target);
                checkEditSelfWithoutGeneralEditAccess(roles, user, currentUsername);
            }
        }
    }

	private void populateUserManager(Object target) {
		if (target != null && target instanceof UserManager) {
			userManager = (UserManager) target;
		} else {
			userManager = null;
		}
	}

	private void checkEditSelfWithoutGeneralEditAccess(GrantedAuthority[] roles, User user, String currentUsername) {
		// get the list of roles the user is trying add
		Set userRoles = new HashSet();

		if (user.getRoles() != null) {
			for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
				Role role = (Role) it.next();
				userRoles.add(role.getName());
			}
		}

		// get the list of roles the user currently has
		Set authorizedRoles = new HashSet();

		for (int i = 0; i < roles.length; i++) {
			authorizedRoles.add(roles[i].getAuthority());
		}

		// if they don't match - access denied
		// users aren't allowed to change their roles
		if (!CollectionUtils.isEqualCollection(userRoles, authorizedRoles)) {
			log.warn("Access Denied: '" + currentUsername + "' tried to change their role(s)!");
			throw new AccessDeniedException(ACCESS_DENIED);
		}

		// Disallow change of User.organization2id for user without general user
		// edit access.
		User storedUser = userManager.getUser(currentUsername);
		if (!ObjectUtils.equals(storedUser.getOrganization2id(), user.getOrganization2id())) {
			log.warn("Access Denied: '" + currentUsername + "' can't change Organization2id from "
					+ storedUser.getOrganization2id() + " to " + user.getOrganization2id());
			throw new AccessDeniedException(ACCESS_DENIED);
		}
	}
}
