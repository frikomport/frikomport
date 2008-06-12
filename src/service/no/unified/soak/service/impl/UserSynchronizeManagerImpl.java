package no.unified.soak.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import no.unified.soak.dao.jdbc.UserEzDaoJdbc;
import no.unified.soak.ez.EzUser;
import no.unified.soak.model.User;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.UserSynchronizeManager;

import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * User: gv
 * Date: 04.jun.2008
 * Time: 11:35:18
 */
public class UserSynchronizeManagerImpl extends BaseManager implements UserSynchronizeManager {

    private UserEzDaoJdbc userEzDaoJdbc = null;
    private UserManager userManager = null;

    public void setUserEzDaoJdbc(UserEzDaoJdbc userEzDaoJdbc) {
        this.userEzDaoJdbc = userEzDaoJdbc;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void synchronizeUsers() {
        List<EzUser> ezUsers = userEzDaoJdbc.findAll();
        if(ezUsers != null)
        {
            Iterator<EzUser> it = ezUsers.iterator();
            while (it.hasNext()){
                EzUser ezUser = it.next();
                User user = null;
                try{
                    user = userManager.getUser(ezUser.getUsername());
                    userManager.updateUser(user, ezUser.getFirst_name(), ezUser.getLast_name(), ezUser.getEmail(), ezUser.getId(), ezUser.getRolenames(), ezUser.getKommune());
                }
                catch (ObjectRetrievalFailureException orfe){
                    // No user, add
                    user = userManager.addUser(ezUser.getUsername(), ezUser.getFirst_name(), ezUser.getLast_name(), ezUser.getEmail(), ezUser.getId(), ezUser.getRolenames(), ezUser.getKommune());
                }

                // Set transaction?
//                userManager.
//
//                userManager.saveUser(user);
                // Update organization?
            }
        }
        log.debug("Synchronized users");
    }

    public void executeTask() {
        log.info("running userSynchronizeManager");
        synchronizeUsers();
    }

    public void setLocale(Locale locale) {
        //Do nothing
    }
}
