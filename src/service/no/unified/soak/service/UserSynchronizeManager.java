package no.unified.soak.service;

import no.unified.soak.ez.EzUser;
import no.unified.soak.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 04.jun.2008
 * Time: 11:30:38
 * To change this template use File | Settings | File Templates.
 */
public interface UserSynchronizeManager extends Task{
    
    public void setUserManager(UserManager userManager);
    
    public void setRegistrationManager(RegistrationManager registrationManager);

    /**
     * Checks users and updates local database
     */
    public void synchronizeUsers();
    
    /**
     * Processes one singe user
     */
    public User processUser(EzUser user);
}
