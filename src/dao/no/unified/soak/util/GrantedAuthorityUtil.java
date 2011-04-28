package no.unified.soak.util;

import no.unified.soak.Constants;

import org.acegisecurity.GrantedAuthority;

public class GrantedAuthorityUtil {

	/**
	 * @param authorizedRoles
	 * @param role
	 * @return
	 */
	public static boolean containsOneOrMore(GrantedAuthority[] authorizedRoles, String... checkingRoles) {
        for (int i = 0; i < authorizedRoles.length; i++) {
        	for (String roleString : checkingRoles) {
        		if (authorizedRoles[i].getAuthority().equals(roleString)) {
        			return true;
        		}
			}
        }
        return false;
	}

}
