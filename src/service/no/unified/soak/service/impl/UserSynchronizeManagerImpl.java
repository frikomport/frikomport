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
                EzUser ezUser = it.next();
                User user = null;
                User user2 = null;
                try{
                    user = userManager.getUser(ezUser.getUsername());
                    user2 = userManager.findUser(ezUser.getEmail().toLowerCase());
                    if(user2 != null && user2.getEnabled() && !user.getUsername().equals(user2.getUsername())){
                        // user2 skal disablast og user overta påmeldinger
                        byttNavnOgDisable(user2);
                        registrationManager.moveRegistrations(user2, user);
                    }
                    userManager.updateUser(user, ezUser.getFirst_name(), ezUser.getLast_name(), ezUser.getEmail().toLowerCase(), ezUser.getId(), ezUser.getRolenames(), ezUser.getKommune());
                }
                catch (ObjectRetrievalFailureException orfe){
                    // No user, might be registered by email
                    user = userManager.findUser(ezUser.getEmail().toLowerCase());
                    if(user != null){
                        // Endre epost til nokke som ikkje finnes
                        byttNavnOgDisable(user);
                        // Ny bruker basert på riktig brukernavn
                        User newuser = userManager.addUser(ezUser.getUsername(), ezUser.getFirst_name(), ezUser.getLast_name(), ezUser.getEmail().toLowerCase(), ezUser.getId(), ezUser.getRolenames(), ezUser.getKommune());
                        newuser.setHash(user.getHash());
                        userManager.updateUser(newuser);
                        // Flytt påmeldinger
                        registrationManager.moveRegistrations(user, newuser);
                    }
                    else{
                        // definitly no user
                        user = userManager.addUser(ezUser.getUsername(), ezUser.getFirst_name(), ezUser.getLast_name(), ezUser.getEmail().toLowerCase(), ezUser.getId(), ezUser.getRolenames(), ezUser.getKommune());
                    }
                }
            }
        }
        log.debug("Synchronized users");
    }

    private void byttNavnOgDisable(User user) {
        String useremail = user.getEmail();
        user.setEmail(useremail.substring(0,useremail.indexOf('@')) + "@nonexist.no");
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
