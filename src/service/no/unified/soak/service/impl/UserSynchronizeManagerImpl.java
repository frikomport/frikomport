package no.unified.soak.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.ez.ExtUser;
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
    private ExtUserDAO extUserDAO = null;
    private UserManager userManager = null;
    private RegistrationManager registrationManager = null;

    public void setExtUserDAO(ExtUserDAO extUserDAO) {
        this.extUserDAO = extUserDAO;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void synchronizeUsers() {
        List<ExtUser> ezUsers = extUserDAO.findAll();
        if(ezUsers != null)
        {
            Iterator<ExtUser> it = ezUsers.iterator();
            while (it.hasNext()){
                ExtUser current = it.next();
                processUser(current);
            }
        }
        log.debug("Synchronized users");
    }

    public User processUser(ExtUser current) {
        
        User emailuser = null;
        User ezUser = null;
        // sjekker om epostaddressa er brukt som usernane
        try {
            emailuser = userManager.getUser(current.getEmail().toLowerCase());
        } catch (ObjectRetrievalFailureException e) {
            User user = userManager.findUser(current.getEmail().toLowerCase());
            if((user != null) && !user.getUsername().equals(current.getUsername())) {
                emailuser = user;
            }
        }

        if(emailuser != null) {
            byttNavnOgDisable(emailuser);
        }
        
        try {
            ezUser = userManager.getUser(current.getUsername());
            userManager.updateUser(ezUser,current.getFirst_name(),current.getLast_name(),current.getEmail().toLowerCase(),current.getId(),current.getRolenames(),current.getKommune());
        } catch (Exception e) {
            // ezUser finnes ikkje og m� opprettes
            ezUser = userManager.addUser(current.getUsername(), current.getFirst_name(), current.getLast_name(), current.getEmail().toLowerCase(), current.getId(), current.getRolenames(), current.getKommune());
        }
        
        // Flytt registreringer
        if (emailuser != null) {
            ezUser.setHash(emailuser.getHash());
            userManager.updateUser(ezUser);
            registrationManager.moveRegistrations(emailuser, ezUser);
        }
        
        return ezUser;
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
        //Do nothing
    }
}
