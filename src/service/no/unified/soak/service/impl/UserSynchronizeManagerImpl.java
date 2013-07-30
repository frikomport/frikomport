package no.unified.soak.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.ez.ExtUser;
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
			// SVV-synking mot LDAP av alle p�loggingsbrukere trengs i fall en brukerrolle fjernes i LDAP.
			log.info("Synkronisering av brukere starter...");
			User filter = new User();
			filter.setHashuser(false);
			List users = userManager.getUsers(filter, true);
			Iterator<User> it = users.iterator();
			int antall = 0;
			while (it.hasNext()) {
				User local = it.next();
				antall++;
				ExtUser ldapUser = null; 
				try {
					ldapUser = extUserDAO.findUserByUsername(local.getUsername());
					if(ldapUser != null && ldapUser.getRolenames() != null){
						if(ldapUser.getRolenames().isEmpty()){
							// brukeren har ikke lenger rolle(r) for Mengdetrening
							// settes som inaktiv bruker
							processUser(null, local.getUsername());
							log.info("Deaktivert (pga. ingen roller): " + local.getFullName() + " [" + local.getUsername() + "] " + local.getEmail());
						}
						else {
							processUser(ldapUser, null);
							String roller = "";
							try { roller = " roller: " + ldapUser.getRolenames().toString(); }catch(Exception e) { /* do noting */ }
							log.info("LDAP: " + ldapUser.getName() + " [" + ldapUser.getUsername() + "] " + ldapUser.getEmail() + roller);
						}
					}
					else {
						// brukerenavnet ble ikke funnet
						// settes som inaktiv bruker
						processUser(null, local.getUsername());
						log.info("Deaktivert (pga. brukernavn ikke funnet): " + local.getFullName() + " [" + local.getUsername() + "] " + local.getEmail());
					}
				}
				catch(Exception e){
					// ldapUser is null - do nothing -- LDAP might be unavailable
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
        	// extUser finnes ikke. Fors�ker med annen vri.
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
        	// extUser finnes ikkje og m� opprettes
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
