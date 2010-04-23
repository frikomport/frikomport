/**
 * 
 */
package no.unified.soak.util;

import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.User;

/**
 *
 */
public class ConvertDAO {

    public static ExtUser User2ExtUser(User user) {
        ExtUser extUser = new ExtUser();
        extUser.setEmail(user.getEmail());
        extUser.setUsername(user.getUsername());
        extUser.setName(user.getFullName());
        extUser.setFirst_name(user.getFirstName());
        extUser.setLast_name(user.getLastName());
        extUser.setKommune(user.getOrganizationid() == null ? null : user.getOrganizationid().intValue());
        extUser.setRolenames(user.getRoleNameList());
        extUser.setMobilePhone(user.getMobilePhone());
        return extUser;
    }
}
