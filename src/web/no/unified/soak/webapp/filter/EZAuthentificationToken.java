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


/**
 * @author kst
 *
 */
public class EZAuthentificationToken implements Authentication {
    private boolean authenticated;
    private String eZSessionId;
    private User ezUser;
    List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

    /**
     * Constructor to "ensure" there is a EzUser and a eZSessionId.
     * <code>null</code> for both means <b>not</b> authenticated.
     *
     * @param ezUser
     *            the authenticated eZp user.
     * @param eZSessionId
     *            The session id of the http-session where user logged in.
     */
    EZAuthentificationToken(User ezUser, String eZSessionId) {
        this.ezUser = ezUser;
        this.eZSessionId = eZSessionId;

        if ((eZSessionId != null) && (eZSessionId.trim().length() > 0) &&
                (ezUser != null) && (ezUser.getId() != null) && (ezUser.getId() > 0)) {
            authenticated = true;

            for (String rolename : ezUser.getRoleNameList()) {
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
     * eZp Sessionid is our credentials.
     *
     * @see net.sf.acegisecurity.Authentication#getCredentials()
     */
    public Object getCredentials() {
        return eZSessionId;
    }

    /**
     * Additional details about user. Here the user object.
     *
     * @see net.sf.acegisecurity.Authentication#getDetails()
     */
    public Object getDetails() {
        return ezUser;
    }

    /**
     * Either username or UserDetails object. Here username
     */
    public Object getPrincipal() {
        return ezUser.getUsername();
    }

    public String getName() {
        return ezUser.getUsername();
    }
}
