package no.unified.soak.service.impl;

import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.dao.jdbc.UserEzDaoJdbc;
import no.unified.soak.ez.EzUser;
import no.unified.soak.model.User;

import java.util.Locale;
import java.util.List;
import java.util.Iterator;

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
        /*
        Get users from ez
        Get users from local db
        * add missing users
        * disable disabled users
         */
        List<EzUser> ezUsers = userEzDaoJdbc.findKursansvarligeUser();// Make new method to get all users.
        if(ezUsers != null)
        {
            Iterator<EzUser> it = ezUsers.iterator();
            while (it.hasNext()){
                EzUser ezUser = it.next();
                User user = null;
                try{
                    user = userManager.getUser(ezUser.getUsername());
                }
                catch (ObjectRetrievalFailureException orfe){
                    // No user, create
                    user = userManager.addUser(ezUser.getUsername(), ezUser.getFirst_name(), ezUser.getLast_name(), ezUser.getEmail(), ezUser.getId(), ezUser.getRolenames());
                }
            }
        }
        log.debug("Synchronized users");
    }

    public void executeTask() {
        synchronizeUsers();
        log.info("Ran userSynchronizeManager");
    }

    public void setLocale(Locale locale) {
        //Do nothing
    }
}
