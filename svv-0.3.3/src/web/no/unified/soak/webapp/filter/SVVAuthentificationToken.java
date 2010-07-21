/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/**
 *
 */
package no.unified.soak.webapp.filter;

import java.util.ArrayList;
import java.util.List;

import no.unified.soak.model.User;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.apache.commons.lang.StringUtils;


/**
 * @author kst
 *
 */
public class SVVAuthentificationToken implements Authentication {
    private boolean authenticated;
    private String username;
    private User user;
    List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

    /**
     * Constructor to "ensure" there is a User and a username.
     * <code>null</code> for both means <b>not</b> authenticated.
     *
     * @param user
     *            the authenticated user.
     * @param username
     *            The username from the http-request.
     */
    SVVAuthentificationToken(User user, String username) {
        this.user = user;
        this.username = username;

        if (!StringUtils.isBlank(username) &&
                (user != null) && (user.getUsername() != null) && (user.getUsername().equals(username))) {
            authenticated = true;

            for (String rolename : user.getRoleNameList()) {
                addAuthority(rolename);
            }
        }
    }

    public void setAuthenticated(boolean isAuth) {
        this.authenticated = isAuth;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public GrantedAuthority[] getAuthorities() {
        return grantedAuthorities.toArray(new GrantedAuthority[grantedAuthorities.size()]);
    }

    private void addAuthority(String rolename) {
        GrantedAuthority authority = new GrantedAuthorityImpl(rolename);
        grantedAuthorities.add(authority);
    }

    /**
     * username is our credentials.
     *
     * @see net.sf.acegisecurity.Authentication#getCredentials()
     */
    public Object getCredentials() {
        return username;
    }

    public Object getDetails() {
        return user;
    }

    public Object getPrincipal() {
        return user.getUsername();
    }

    public String getName() {
        return user.getFullName();
    }
}
