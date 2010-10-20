package no.unified.soak.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import no.unified.soak.dao.EzUserDAO;
import no.unified.soak.ez.EzUser;
import no.unified.soak.model.User;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.util.UserUtil;

import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * User: gv
 * Date: 04.jun.2008
 * Time: 11:35:18
 */

public class UserSynchronizeManagerImpl extends BaseManager implements UserSynchronizeManager {
    private EzUserDAO ezUserDAO = null;
    private UserManager userManager = null;
    private RegistrationManager registrationManager = null;

    public void setEzUserDAO(EzUserDAO ezUserDAO) {
        this.ezUserDAO = ezUserDAO;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void synchronizeUsers() {
        List<EzUser> ezUsers = ezUserDAO.findAll();
        if(ezUsers != null)
        {
            Iterator<EzUser> it = ezUsers.iterator();
            while (it.hasNext()){
                EzUser current = it.next();
                processUser(current);
            }
        }
        log.debug("Synchronized users");
    }

    public User processUser(EzUser current) {
        
        User ezUser = null;
        User emailuser = userManager.getUserByEmail(current.getEmail());

        if(emailuser != null && !emailuser.getUsername().equals(current.getUsername())) {
            userManager.changeEmailAndDisable(emailuser);
        }
        
        try {
            ezUser = userManager.getUser(current.getUsername());
            // Burde sjekke om brukerene er ulike før oppdatering
            userManager.updateUser(ezUser,current.getFirst_name(),current.getLast_name(),current.getEmail().toLowerCase(),current.getId(),current.getRolenames(),current.getKommune());
        } catch (Exception e) {
            // ezUser finnes ikkje og må opprettes
            ezUser = userManager.addUser(current.getUsername(), current.getFirst_name(), current.getLast_name(), current.getEmail().toLowerCase(), current.getId(), current.getRolenames(), current.getKommune());
        }
        
        // Flytt registreringer
        // Dersom emailuser er samme som ezUser trengs ikkje dette gjøres!
        if (emailuser != null && !emailuser.getUsername().equals(ezUser.getUsername())) {
            ezUser.setHash(emailuser.getHash());
            userManager.updateUser(ezUser);
            registrationManager.moveRegistrations(emailuser, ezUser);
        }
        
        return ezUser;
    }

    public void executeTask() {
        log.info("running userSynchronizeManager");
        synchronizeUsers();
    }

    public void setLocale(Locale locale) {
        //Do nothing
    }
}
