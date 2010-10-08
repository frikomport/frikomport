package no.unified.soak.service.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import no.unified.soak.Constants;
import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.Role;
import no.unified.soak.model.User;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.RoleManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.UserUtil;

import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * User: gv Date: 04.jun.2008 Time: 11:35:18
 */

public class UserSynchronizeManagerImpl extends BaseManager implements UserSynchronizeManager {
    private ExtUserDAO extUserDAO = null;
    private UserDAO userDAO = null;
    private UserManager userManager = null;
    private RegistrationManager registrationManager = null;
    private RoleManager roleManager = null;

    public void setExtUserDAO(ExtUserDAO extUserDAO) {
        this.extUserDAO = extUserDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

	public void synchronizeUsers() {
		if (ApplicationResourcesUtil.isSVV()) {
			// TODO SVV-synking mot LDAP av alle påloggingsbrukere trengs i
			// fall en brukerrolle fjernes i LDAP.
			Hashtable<String, User> users = new Hashtable<String, User>();

			List roles = roleManager.getRoles(null);
			Iterator r = roles.iterator();
			while (r.hasNext()) {
				Role role = (Role) r.next();
				// Brukere med rolle annonym eller ansatt skal utelates fra
				// synkronisering mot LDAP
				if (!role.getName().equals(Constants.ANONYMOUS_ROLE) && !role.getName().equals(Constants.EMPLOYEE_ROLE)) {
					List tmp = userDAO.getUsersByRole(role.getName());
					Iterator u = tmp.iterator();
					while (u.hasNext()) {
						User user = (User) u.next();
						users.put(user.getUsername(), user);
					}
				}
			}
			// brukere som skal synkroniseres
			Enumeration e = users.elements();
			log.info("Synkronisering av brukere starter...");
			int antall = 0;
			while (e.hasMoreElements()) {
				User local = (User) e.nextElement();
				ExtUser ldapUser = extUserDAO.findUserByUsername(local.getUsername());
				antall++;
				if (ldapUser == null) {
					// brukeren finnes ikke lenger i eksternt system, settes som inaktiv bruker
					processUser(null, local.getUsername());
					log.info("Deaktivert: " + local.getFullName() + " [" + local.getUsername() + "] " + local.getEmail());
					log.debug("Deaktivert: " + local.getFullName() + " [" + local.getUsername() + "] " + local.getEmail());
				} else {
					processUser(ldapUser, null);
					log.info("LDAP: " + ldapUser.getName() + " [" + ldapUser.getUsername() + "] " + ldapUser.getEmail());
					log.debug("LDAP: " + ldapUser.getName() + " [" + ldapUser.getUsername() + "] " + ldapUser.getEmail());
				}
			}
			log.info("Synkronisering av " + antall + " brukere ferdig!");

		} else {
			List<ExtUser> ezUsers = extUserDAO.findAll();
			if (ezUsers != null) {
				Iterator<ExtUser> it = ezUsers.iterator();
				while (it.hasNext()) {
					ExtUser current = it.next();
					processUser(current, null);
				}
			}
			log.debug("Synchronized users");
		}
	}

    public User processUser(ExtUser current, String disableThisUser) {

    	User user = null;
		if(disableThisUser != null){
			try {
				user = userManager.getUser(disableThisUser);
				userManager.disableUser(user);
	        } catch (ObjectRetrievalFailureException e) {
	        	log.warn("Cannot find user [" + disableThisUser + "] to disable.");
	        } 
			return user;
		}
    	
    	User emailuser = null;
        // Sjekker om epostadressa er brukt som username i FriKomPort-databasen.
        try {
            emailuser = userManager.getUser(current.getEmail().toLowerCase());
        } catch (ObjectRetrievalFailureException e) {
        	// extUser finnes ikke. Forsøker med annen vri.
            User tmpUser = userManager.findUserByEmail(current.getEmail().toLowerCase());
            if ((tmpUser != null) && !tmpUser.getUsername().equals(current.getUsername())) {
                emailuser = tmpUser;
            }
        }

        if (emailuser != null) {
            byttNavnOgDisable(emailuser);
        }

        try {
			user = userManager.getUser(current.getUsername());
			userManager.updateUser(user, current.getFirst_name(), current.getLast_name(), current.getEmail().toLowerCase(), current
					.getId(), current.getRolenames(), current.getKommune(), current.getMobilePhone(), current.getPhoneNumber());
        } catch (ObjectRetrievalFailureException e) {
        	// extUser finnes ikkje og må opprettes
        	user = userManager.addUser(current.getUsername(), current.getFirst_name(), current.getLast_name(), current.getEmail()
        			.toLowerCase(), current.getId(), current.getRolenames(), current.getKommune(), current.getMobilePhone(),
        			current.getPhoneNumber());
        } 

        // Flytt registreringer
        if (emailuser != null) {
            user.setHash(emailuser.getHash());
            userManager.updateUser(user);
            registrationManager.moveRegistrations(emailuser, user);
        }

        return user;
    }

    private void byttNavnOgDisable(User user) {
        user = UserUtil.transformEmail(user, "@nonexist.no");
        user.setEnabled(false);
        userManager.updateUser(user);
    }

    public void executeTask() {
        log.info("running userSynchronizeManager");
        synchronizeUsers();
    }

    public void setLocale(Locale locale) {
        // Do nothing
    }
}
